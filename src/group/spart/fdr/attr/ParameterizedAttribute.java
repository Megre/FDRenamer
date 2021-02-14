package group.spart.fdr.attr;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import group.spart.fdr.util.DecodeUtil;

/** 
 * 
 * @author megre
 * @email renhao.x@seu.edu.cn
 * @version created on: 2021年2月5日 下午4:48:55 
 */
public class ParameterizedAttribute implements SpecialCharDecoder {
	
	private static final Pattern fEntryPattern = 
			Pattern.compile("\\$([a-zA-Z_][a-zA-Z_0-9]*)|\\$\\{([^\\}]+)\\}");
	
	private final Matcher fEntryMatcher;
	private String fAttributeText;
	private StringBuffer fAttributeBuffer;
	private List<OptionalAttributeEntry> fEntryList;
	
	/**
	 * 
	 * @param quotedAttribute $<name> or:
	 * 		${<name> [,<formatter>] [:<modifier>] | <name> [,<formatter>] [:<modifier>] ...} or:
	 * 		${:<modifier> [,<formatter>] [:<modifier>] | <name> [,<formatter>] [:<modifier>] ...}
	 */
	public ParameterizedAttribute(String quotedAttribute) {
		fAttributeText = quotedAttribute.trim();
		fAttributeBuffer = new StringBuffer(fAttributeText);
		fEntryList = new ArrayList<>();
		fEntryMatcher = fEntryPattern.matcher(fAttributeBuffer);
	}
	
	public List<OptionalAttributeEntry> listEntries() {
		if(fEntryList.size() > 0) return fEntryList;
		
		int findStart = 0, entryIndex = 0;
		while(fEntryMatcher.find(findStart)) {
			for(int c=1; c<=fEntryMatcher.groupCount(); ++c) {
				if(fEntryMatcher.group(c) == null) continue;
				fEntryList.add(new OptionalAttributeEntry(decode(fEntryMatcher.group(c)), entryIndex++));
				break;
			}
			findStart = fEntryMatcher.end();
		}
		
		return fEntryList;
	}
	
	public ParameterizedAttribute inflate(String value) {
		fEntryMatcher.reset();
		if(fEntryMatcher.find()) {
			fAttributeBuffer.replace(fEntryMatcher.start(), fEntryMatcher.end(), value);
		}
		
		return this;
	}
	
	public String value() {
		return fAttributeBuffer.toString();
	}

	/**
	 * @see group.spart.fdr.attr.SpecialCharDecoder#decode(java.lang.String)
	 */
	@Override
	public String decode(String encodedString) {
		return DecodeUtil.decode(encodedString, "$", "{", "}");
	}
	
}

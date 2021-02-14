package group.spart.fdr.attr;

import java.util.ArrayList;
import java.util.List;

import group.spart.fdr.util.DecodeUtil;

/** 
 * ${<name> [, <formatter>] [: <modifier>] ... [| [, <formatter>] [: <modifier>] ...] ...}
 * @author megre
 * @email renhao.x@seu.edu.cn
 * @version created on: 2021年2月5日 下午8:43:03 
 */
public class OptionalAttributeEntry implements SpecialCharDecoder {
	public static final String OPTION_SPLITTER = "|";
	
	private String fAttributeText;
	private List<SimpleAttributeEntry> fEntryList;
	private int fIndex;
	
	/**
	 * @param attribute <name> [, <formatter>] [: <modifier>] ... [| [, <formatter>] [: <modifier>] ...] ...
	 * @param entryIndex index in a parameterized attribute 
	 */
	public OptionalAttributeEntry(String attribute, int entryIndex) {
		fEntryList = new ArrayList<>();
		fIndex = entryIndex;
		fAttributeText = attribute.trim();
		parse();
	}
	
	public List<SimpleAttributeEntry> listOptionalEntries() {
		return fEntryList;
	}
	
	
	public int getIndex() {
		return fIndex;
	}
	
	/**
	 * @see group.spart.fdr.attr.SpecialCharDecoder#decode(java.lang.String)
	 */
	@Override
	public String decode(String encodedString) {
		return DecodeUtil.decode(encodedString, "|");
	}
	
	private void parse() {
		String[] entries = fAttributeText.split("\\" + OPTION_SPLITTER);
		for(String entry: entries) {
			if(entry.trim().isEmpty()) continue;
			
			fEntryList.add(new SimpleAttributeEntry(decode(entry)));
		}
	}

}
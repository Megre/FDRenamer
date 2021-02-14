package group.spart.fdr.attr;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import group.spart.fdr.util.DecodeUtil;
import group.spart.fdr.util.StringUtil;

/** 
 * 
 * @author megre
 * @email renhao.x@seu.edu.cn
 * @version created on: 2021年2月5日 下午9:34:18 
 */
public class SimpleAttributeEntry implements SpecialCharDecoder {
	
	private Logger logger = LogManager.getLogger(SimpleAttributeEntry.class);
	
	public static final String FORMATTER_SPLITTER = ",";
	public static final String MODIFIER_SPLITTER = ":";
	
	private String fAttributeText;
	private String fName;
	private List<AttributeResolver> fResolverList;

	private static final Pattern fNamePattern = Pattern.compile(" *([a-zA-Z_][a-zA-Z_0-9]*) *");
	private static final Pattern fResolverPattern = Pattern.compile(" *([,:] *(?:[^\\(\\),:]+)(?:\\([^\\(\\):]+\\)|[^\\(\\):,]+))");
	
	/**
	 * 
	 * @param attribute <name> [, <formatter>] [: <modifier>] ...
	 * 		Using URL encode to allow special characters in name, formatter, or modifier 
	 * 		(e.g. "%2c" for "," and "%3a" for ":")
	 */
	public SimpleAttributeEntry(String attribute) {
		fAttributeText = attribute.trim();
		fName = StringUtil.EMPTY_VALUE;
		fResolverList = new ArrayList<>();
		parse();
	}
	
	public String getName() {
		return fName;
	}

	public List<AttributeResolver> listResolvers() {
		return fResolverList;
	}
	
	/**
	 * @see group.spart.fdr.attr.SpecialCharDecoder#decode(java.lang.String)
	 */
	@Override
	public String decode(String encodedString) {
		return DecodeUtil.decode(encodedString, ",", ":");
	}
	
	private void parse() {
		// name
		Matcher nameMatcher = fNamePattern.matcher(fAttributeText);
		if(nameMatcher.find()) {
			fName = nameMatcher.group(1);
		}
		
		// resolvers
		Matcher resolverMatcher = fResolverPattern.matcher(fAttributeText);
		int startIdx = 0;
		while(resolverMatcher.find(startIdx)) {
			AttributeResolver resolver = createResolver(resolverMatcher.group(1));
			if(resolver != null) {
				fResolverList.add(resolver);
			}
			else {
				logger.error("error resolver format: " + resolverMatcher.group(1));
			}
			startIdx = resolverMatcher.end();
		}
		
	}
	
	private AttributeResolver createResolver(String resolver) {
		if(resolver.startsWith(FORMATTER_SPLITTER)) {
			return new AttributeFormatter(decode(resolver.substring(1).trim()));
		}
		else if(resolver.startsWith(MODIFIER_SPLITTER)) {
			return new AttributeModifier(decode(resolver.substring(1).trim()));
		}
		
		return null;
	}
	
}

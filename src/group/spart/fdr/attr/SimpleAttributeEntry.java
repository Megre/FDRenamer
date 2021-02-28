package group.spart.fdr.attr;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import group.spart.fdr.util.StringUtil;

/** 
 * 
 * @author megre
 * @email renhao.x@seu.edu.cn
 * @version created on: 2021年2月5日 下午9:34:18 
 */
public class SimpleAttributeEntry {
	
	private Logger logger = LogManager.getLogger(SimpleAttributeEntry.class);
	
	private String fAttributeText;
	private String fName;
	private List<AttributeResolver> fResolverList;

	private static final Pattern NAME_PATTERN = Pattern.compile("^ *([a-zA-Z_][a-zA-Z_0-9]*) *");
	private static final Pattern RESOLVER_PATTERN = Pattern.compile(" *([,:] *(?:[^\\(\\),:]+)(?:\\([^\\(\\):]+\\)|[^\\(\\):,]+))");
	
	/**
	 * 
	 * @param attribute <name> [, <formatter>] [: <modifier>] ...
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
	
	private void parse() {
		// name
		Matcher nameMatcher = NAME_PATTERN.matcher(fAttributeText);
		if(nameMatcher.find()) {
			fName = nameMatcher.group(1);
		}
		
		// resolvers
		Matcher resolverMatcher = RESOLVER_PATTERN.matcher(fAttributeText);
		int startIdx = 0;
		while(resolverMatcher.find(startIdx)) {
			AttributeResolver resolver = ResolverFactory.create(resolverMatcher.group(1));
			if(resolver != null) {
				fResolverList.add(resolver);
			}
			else {
				logger.error("error resolver format: " + resolverMatcher.group(1));
			}
			startIdx = resolverMatcher.end();
		}
		
	}
	
}

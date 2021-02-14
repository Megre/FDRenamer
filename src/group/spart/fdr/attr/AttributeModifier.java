package group.spart.fdr.attr;

import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import group.spart.fdr.FDRFilter;
import group.spart.fdr.InflatedFilterOption;
import group.spart.fdr.matcher.RegexMatcher;

/** 
 * 
 * @author megre
 * @email renhao.x@seu.edu.cn
 * @version created on: 2021年2月6日 下午8:37:33 
 */
public class AttributeModifier extends AttributeResolver {

	private static Logger logger = LogManager.getFormatterLogger(AttributeModifier.class);
	
	private String fModifier;
	
	public AttributeModifier(String modifier) {
		super(modifier);
		fModifier = getResolverText();
	}
	

	/**
	 * @see group.spart.fdr.attr.AttributeResolver#resolve(java.lang.Object)
	 */
	@Override
	public String resolve(Object valueObject) {
		return modify(valueObject.toString());
	}
	
	private String modify(String value) {
		if(value == null) return null;
		
		if(fModifier.isEmpty()) return value;
		
		value = getGroupedAttributeValue(value);
		
		return value;
	}

	/**
	 * Calculate the modifier's value referring to the group captured by the filter.
	 * @param value the string value to modify
	 * @return
	 */
	private String getGroupedAttributeValue(String value) {
		FDRFilter filter = getFilter();
		if(filter == null) return null;
		
		final List<InflatedFilterOption> inflatedOptions = filter.listInflatedOptions();
		if(inflatedOptions.size() == 0) return value;
		
		/*
		 *  fModifier is in the format of $0.1 where $ starts the reference,
		 *  	0 refers to the index of the filter, and 
		 *  	1 refers to the index of the group captured by the filter.
		 */
		Pattern groupRefer = Pattern.compile("\\$(\\d+)\\.(\\d+)");
		Matcher groupReferMatcher = groupRefer.matcher(fModifier);
		if(!groupReferMatcher.find()) return value;
		
		String result = fModifier;
		int findStart = 0;
		while(groupReferMatcher.find(findStart)) {
			findStart = groupReferMatcher.end();
			
			int optionIndex = Integer.parseInt(groupReferMatcher.group(1)),
					groupIndex = Integer.parseInt(groupReferMatcher.group(2));
			if(optionIndex >= inflatedOptions.size()) {
				logger.error("[filter " + filter.getPosition() + "] option index out of range: " + fModifier);
				continue;
			}
			
			if(!(inflatedOptions.get(optionIndex).matcher() instanceof RegexMatcher)) {
				logger.error("[filter " + filter.getPosition() + "] refers to non-regular-expression-matcher: " + fModifier);
				continue;
			}
			
			RegexMatcher regexMatcher = (RegexMatcher) inflatedOptions.get(optionIndex).matcher();
			if(groupIndex > regexMatcher.groupCount()) {
				logger.error("[filter " + filter.getPosition() + "] group index out of range: " + fModifier);
				continue;
			}
			
			result = result.replace("$" + optionIndex + "." + groupIndex, regexMatcher.group(groupIndex));
		}

		
		return Objects.equals(result, fModifier)
				? value
				: result;
	}

}

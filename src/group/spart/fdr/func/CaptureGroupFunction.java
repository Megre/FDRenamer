package group.spart.fdr.func;

import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import group.spart.fdr.FDRFilter;
import group.spart.fdr.InflatedFilterOption;
import group.spart.fdr.matcher.RegexMatcher;
import group.spart.fdr.util.DecodeUtil;

/** 
 * 
 * @author megre
 * @email renhao.x@seu.edu.cn
 * @version created on: 2021-02-19 23:41:43
 */
public class CaptureGroupFunction extends ModifierFunction {
	private static Logger logger = LogManager.getFormatterLogger(CaptureGroupFunction.class);
	
	/*
	 *  fModifier is, for example, in the format of $0.1 where $ starts the reference,
	 *  	0 refers to the index of the filter, and 
	 *  	1 refers to the index of the group captured by the filter.
	 */
	private final static Pattern CAPTURE_GROUP_REFER_PATTERN = Pattern.compile("\\$(\\d+)\\.(\\d+)");
	
	public CaptureGroupFunction(String name, List<String> paramList) {
		super(name, paramList);
	}
	
	/**
	 * Calculate the modifier's value referring to the group captured by the filter.
	 * @param value the string value to modify
	 * @return
	 */
	@Override
	public String modify(String value, FDRFilter filter) {
		if(filter == null || listParams().size() < 1) return null;
		
		final List<InflatedFilterOption> inflatedOptions = filter.listInflatedOptions();
		if(inflatedOptions.size() == 0) return value;
		
		Matcher groupReferMatcher = CAPTURE_GROUP_REFER_PATTERN.matcher(listParams().get(0));
		String param = listParams().get(0), result = param;
		int findStart = 0;
		while(groupReferMatcher.find(findStart)) {
			findStart = groupReferMatcher.end();
			
			int optionIndex = Integer.parseInt(groupReferMatcher.group(1)),
					groupIndex = Integer.parseInt(groupReferMatcher.group(2));
			if(optionIndex >= inflatedOptions.size()) {
				logger.error("[filter " + filter.getPosition() + "] option index out of range: " + param);
				continue;
			}
			
			if(!(inflatedOptions.get(optionIndex).matcher() instanceof RegexMatcher)) {
				logger.error("[filter " + filter.getPosition() + "] refers to non-regular-expression-matcher: " + param);
				continue;
			}
			
			RegexMatcher regexMatcher = (RegexMatcher) inflatedOptions.get(optionIndex).matcher();
			if(groupIndex > regexMatcher.groupCount()) {
				logger.error("[filter " + filter.getPosition() + "] group index out of range: " + param);
				continue;
			}
			
			result = result.replaceAll("\\$" + optionIndex + "\\." + groupIndex, regexMatcher.group(groupIndex)); 
		}

		return Objects.equals(result, param)
				? null
				: decode(result);
	}

	/**
	 * @see group.spart.fdr.attr.SpecialCharDecoder#decode(java.lang.String)
	 */
	@Override
	public String decode(String encodedString) {
		return DecodeUtil.decode(encodedString, ".");
	}
}

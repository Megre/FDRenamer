package group.spart.fdr.matcher;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/** 
 * 
 * @author megre
 * @email renhao.x@seu.edu.cn
 * @version created on: Jan 27, 2021 5:10:24 AM 
 */
public class MatcherFactory {
	private static Logger logger = LogManager.getLogger(MatcherFactory.class);
	private static FalseMatcher fFalseMatcher = new FalseMatcher();
	
	public static FilterMatcher matcher(String filter) {
		if(filter == null) return fFalseMatcher;
		
		filter = filter.trim();
		if(filter.startsWith(RegexMatcher.REGEX_QUOTE)) {
			return createRegexMatcher(filter);
		}
		
		if(filter.startsWith(RangeMatcher.LEFT_CLOSE) || filter.startsWith(RangeMatcher.LEFT_OPEN)) {
			return createRangeMatcher(filter);
		}
		
		return new StringMatcher(filter);
	}
	
	private static FilterMatcher createRegexMatcher(String filter) {
		int idxLeft = filter.indexOf(RegexMatcher.REGEX_QUOTE), 
				idxRight = filter.lastIndexOf(RegexMatcher.REGEX_QUOTE);
		if(idxLeft == idxRight) return fFalseMatcher;
		
		int iFlag = 0;
		for(String flag: filter.substring(idxRight+1).trim().split(RegexMatcher.FLAG_SEPARATOR)) {
			if(flag.trim().isEmpty()) continue;
			
			if(RegexMatcher.isValidFlag(flag)) {
				iFlag |= RegexMatcher.flag(flag);
			}
			else {
				logger.warn("invalid regular expression flag: " + flag);
			}
		}
		
		return new RegexMatcher(filter.substring(idxLeft+1, idxRight), iFlag);
	}
	
	private static FilterMatcher createRangeMatcher(String filter) {
		boolean leftOpen = !filter.startsWith(RangeMatcher.LEFT_CLOSE);
		boolean rightOpen = !filter.endsWith(RangeMatcher.RIGHT_CLOSE);
		
		try {
			int index = filter.indexOf(RangeMatcher.SEPARATOR);
			String left = filter.substring(1, index),
					right = filter.substring(index + 1, filter.length() - 1);
			return new RangeMatcher(leftOpen, left, right, rightOpen);
		}
		catch (IndexOutOfBoundsException e) {
			logger.error("invalid range matcher: " + filter);
		}
		
		return fFalseMatcher;
	}
}

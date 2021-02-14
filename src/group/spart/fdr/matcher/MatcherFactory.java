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
		if(filter.startsWith("/")) {
			return createRegexMatcher(filter);
		}
		
		if(filter.startsWith("[") || filter.startsWith("(")) {
			// TODO
		}
		
		return fFalseMatcher;
	}
	
	private static RegexMatcher createRegexMatcher(String filter) {
		int idxLeft = filter.indexOf('/'), idxRight = filter.lastIndexOf('/');
		if(idxLeft == idxRight) return null;
		
		int iFlag = 0;
		for(String flag: filter.substring(idxRight+1).trim().split(RegexMatcher.FLAG_SPLITTER)) {
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
	
}

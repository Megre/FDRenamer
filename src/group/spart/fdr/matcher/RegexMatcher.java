package group.spart.fdr.matcher;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/** 
 * 
 * @author megre
 * @email renhao.x@seu.edu.cn
 * @version created on: 2021年2月7日 下午1:09:44 
 */
public class RegexMatcher implements FilterMatcher {
	private Logger logger = LogManager.getLogger(RegexMatcher.class);

	public static final String REGEX_QUOTE = "/";
	public static final String FLAG_SEPARATOR = ",";
	public static Map<String, Integer> fFlags = new HashMap<>();
	
	private Pattern fPattern;
	private Matcher fMatcher;
	
	public RegexMatcher(String regex, int flag) {
		try {
			fPattern = Pattern.compile(regex, flag);
		}
		catch (PatternSyntaxException e) {
			logger.error("invalid regex pattern: " + regex);
		}
	}
	
	/**
	 * @see group.spart.fdr.matcher.FilterMatcher#matches(java.lang.String)
	 */
	@Override
	public boolean matches(String value) {
		if(value == null || fPattern == null) return false;
		fMatcher = fPattern.matcher(value);
		
		return fMatcher.matches();
	}
	
	public String group(int index) {
		if(fMatcher == null || index > fMatcher.groupCount()) return null;
		
		return fMatcher.group(index);
	}
	
	public int groupCount() {
		if(fMatcher == null) return 0;
		
		return fMatcher.groupCount();
	}

	public static boolean isValidFlag(String flag) {
		if(flag == null) return false;
		
		return fFlags.containsKey(flag.trim().toUpperCase());
	}
	
	public static int flag(String flag) {
		return fFlags.get(flag.trim().toUpperCase());
	}
	
	static {
		fFlags.put("CASE_INSENSITIVE", Pattern.CASE_INSENSITIVE);
		fFlags.put("MULTILINE", Pattern.MULTILINE);
		fFlags.put("DOTALL", Pattern.DOTALL);
		fFlags.put("UNICODE_CASE", Pattern.UNICODE_CASE);
		fFlags.put("CANON_EQ", Pattern.CANON_EQ);
		fFlags.put("UNIX_LINES", Pattern.UNIX_LINES);
		fFlags.put("LITERAL", Pattern.LITERAL);
		fFlags.put("UNICODE_CHARACTER_CLASS", Pattern.UNICODE_CHARACTER_CLASS);
		fFlags.put("COMMENTS", Pattern.COMMENTS);
	}
}

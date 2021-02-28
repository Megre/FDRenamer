package group.spart.fdr.func;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import group.spart.fdr.FDRFilter;

/** 
 * 
 * @author megre
 * @email renhao.x@seu.edu.cn
 * @version created on: 2021-02-20 23:55:10
 */
public class CaseFunction extends ModifierFunction {

	private static Logger logger = LogManager.getFormatterLogger(CaseFunction.class);
	
	/**
	 * @param modifier
	 */
	public CaseFunction(String name, List<String> paramList) {
		super(name, paramList);
	}

	/**
	 * @see group.spart.fdr.func.ModifierFunction#modify(java.lang.String)
	 */
	@Override
	public String modify(String value, FDRFilter filter) {
		if(value == null || getName() == null || listParams().size() < 1) return null;
		if(getName().isEmpty()) return value;
		
		final String action = listParams().get(0);
		if(action.equalsIgnoreCase("uc")) { // uppercase
			return value.toUpperCase();
		}
		
		if(action.equalsIgnoreCase("lc")) { // lowercase
			return value.toLowerCase();
		}
		
		if(action.equalsIgnoreCase("ucfl")) { // uppercase the first letter
			return value.substring(0, 1).toUpperCase() + value.substring(1);
		}
		
		if(action.equalsIgnoreCase("lcfl")) { // lowercase the first letter
			return value.substring(0, 1).toLowerCase() + value.substring(1);
		}
		
		if(action.equalsIgnoreCase("ucw")) { // uppercase the first letter of each word
			return uppercaseWord(value, true);
		}
		
		if(action.equalsIgnoreCase("lcw")) { // lowercase the first letter of each word
			return uppercaseWord(value, false);
		}
		
		if(action.equalsIgnoreCase("ac")) { // alternate the case of each letter
			return alternateCase(value); 
		}
		
		logger.error("no such modifier: " + getName() + "(" + action + ")");
		
		return null;
	}
	
	/**
	 * @see group.spart.fdr.attr.SpecialCharDecoder#decode(java.lang.String)
	 */
	@Override
	public String decode(String encodedString) {
		return encodedString;
	}
	
	private String uppercaseWord(String value, boolean uppercase) {
		StringBuffer stringBuffer = new StringBuffer(value);
		if(stringBuffer.length() > 0) {
			stringBuffer.setCharAt(0, uppercase ? uppercaseChar(stringBuffer.charAt(0)) : lowercaseChar(stringBuffer.charAt(0)));
		}
		
		int spaceIndex = stringBuffer.indexOf(" ");
		while(spaceIndex >= 0 && spaceIndex < stringBuffer.length() - 1) {
			char letter = stringBuffer.charAt(spaceIndex + 1);
			stringBuffer.setCharAt(spaceIndex + 1, uppercase ? uppercaseChar(letter) : lowercaseChar(letter));
			
			spaceIndex = stringBuffer.indexOf(" ", spaceIndex + 1); 
		}
		
		return stringBuffer.toString();
	}
	
	private boolean isLowercase(char c) {
		return c >= 'a' && c <= 'z';
	}
	
	private boolean isUppercase(char c) {
		return c >= 'A' && c <= 'Z';
	}
	
	private char uppercaseChar(char c) {
		return isLowercase(c)
				? (char) (c - 32)
				: c;
	}
	
	private char lowercaseChar(char c) {
		return isUppercase(c)
				? (char) (c + 32)
				: c;
	}
	
	private String alternateCase(String value) {
		StringBuffer stringBuffer = new StringBuffer(value);
		for(int idx=0; idx<stringBuffer.length(); ++idx) {
			char c = stringBuffer.charAt(idx);
			stringBuffer.setCharAt(idx, isLowercase(c) ? uppercaseChar(c) : lowercaseChar(c));
		}
		
		return stringBuffer.toString();
	}


}

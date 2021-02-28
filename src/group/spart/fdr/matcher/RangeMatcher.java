package group.spart.fdr.matcher;

import group.spart.fdr.attr.SpecialCharDecoder;
import group.spart.fdr.util.DecodeUtil;

/** 
 * 
 * @author megre
 * @email renhao.x@seu.edu.cn
 * @version created on: 2021-02-23 19:58:20
 */
public class RangeMatcher implements FilterMatcher, SpecialCharDecoder {

	public static final String LEFT_OPEN = "(";
	public static final String LEFT_CLOSE = "[";
	public static final String RIGHT_OPEN = ")";
	public static final String RIGHT_CLOSE = "]";
	public static final String SEPARATOR = ",";
	
	private boolean fLeftOpen, fRightOpen;
	private String fLeft, fRight;
	
	public RangeMatcher(boolean leftOpen, String left, String right, boolean rightOpen) {
		fLeftOpen = leftOpen;
		fRightOpen = rightOpen;
		fLeft = decode(left.trim());
		fRight = decode(right.trim());
	}

	/**
	 * @see group.spart.fdr.matcher.FilterMatcher#matches(java.lang.String)
	 */
	@Override
	public boolean matches(String value) {
		return leftMatches(value) && rightMatches(value);
	}
	
	/**
	 * @see group.spart.fdr.attr.SpecialCharDecoder#decode(java.lang.String)
	 */
	@Override
	public String decode(String encodedString) {
		return DecodeUtil.decode(encodedString, "[", "]", "(", ")", ",");
	}
	
	private boolean leftMatches(String value) {
		if(value == null || fLeft == null) return false;
		if(fLeft.isEmpty()) return true;
		
		return (fLeftOpen && value.compareTo(fLeft) > 0)
				|| (!fLeftOpen && value.compareTo(fLeft) >= 0);
	}
	
	private boolean rightMatches(String value) {
		if (value == null || fRight == null) return false;
		if(fRight.isEmpty()) return true;
		
		return (fRightOpen && value.compareTo(fRight) < 0)
				|| (!fRightOpen && value.compareTo(fRight) <= 0);
	}

}

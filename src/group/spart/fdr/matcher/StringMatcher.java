package group.spart.fdr.matcher;

import group.spart.fdr.attr.SpecialCharDecoder;
import group.spart.fdr.util.DecodeUtil;

/** 
 * 
 * @author megre
 * @email renhao.x@seu.edu.cn
 * @version created on: 2021-02-23 20:50:47
 */
public class StringMatcher implements FilterMatcher, SpecialCharDecoder {

	private String fFilter;
	
	public StringMatcher(String filter) {
		fFilter = decode(filter.trim());
	}
	
	/**
	 * @see group.spart.fdr.matcher.FilterMatcher#matches(java.lang.String)
	 */
	@Override
	public boolean matches(String value) {
		if(value == null) return false;
		
 		return value.equals(fFilter);
	}

	/**
	 * @see group.spart.fdr.attr.SpecialCharDecoder#decode(java.lang.String)
	 */
	@Override
	public String decode(String encodedString) {
		return DecodeUtil.decode(encodedString, "[", "]", "(", ")");
	}

}

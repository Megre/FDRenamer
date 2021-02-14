package group.spart.fdr.matcher;

/** 
 * 
 * @author megre
 * @email renhao.x@seu.edu.cn
 * @version created on: 2021年2月7日 下午4:09:08 
 */
public class FalseMatcher implements FilterMatcher {
	
	/**
	 * @see group.spart.fdr.matcher.FilterMatcher#matches(java.lang.String)
	 */
	@Override
	public boolean matches(String value) {
		return false;
	}


}

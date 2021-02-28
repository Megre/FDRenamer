package group.spart.fdr.attr;

/** 
 * 
 * @author megre
 * @email renhao.x@seu.edu.cn
 * @version created on: 2021-02-26 22:58:15
 */
public class ResolverFactory {

	public static final String FORMATTER_SEPARATOR = ",";
	public static final String MODIFIER_SEPARATOR = ":";
	
	public static AttributeResolver create(String resolver) {
		if(resolver.startsWith(FORMATTER_SEPARATOR)) {
			return new AttributeFormatter(resolver.substring(1).trim());
		}
		else if(resolver.startsWith(MODIFIER_SEPARATOR)) {
			return new AttributeModifier(resolver.substring(1).trim());
		}
		
		return null;
	}
	

}

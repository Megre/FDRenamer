package group.spart.fdr.attr;

import group.spart.fdr.FDRFilter;
import group.spart.fdr.util.DecodeUtil;

/** 
 * 
 * @author megre
 * @email renhao.x@seu.edu.cn
 * @version created on: 2021-02-10 8:54:34 PM
 */
public abstract class AttributeResolver implements SpecialCharDecoder {

	private String fResolver;
	private FDRFilter fFilter;
	private FileAttribute fFileAttribute;
	
	public AttributeResolver(String resolver) {
		fResolver = decode(resolver.trim());
	}
	
	public String getResolverText() {
		return fResolver;
	}
	
	public FDRFilter getFilter() {
		return fFilter;
	}
	
	public FileAttribute getFileAttribute() {
		return fFileAttribute;
	}
	
	public AttributeResolver setFileAttribute(FileAttribute fileAttribute) {
		fFileAttribute = fileAttribute;
		return this;
	}
	
	public AttributeResolver setFilter(FDRFilter filter) {
		fFilter = filter;
		return this;
	}
	
	/**
	 * @see group.spart.fdr.attr.SpecialCharDecoder#decode(java.lang.String)
	 */
	@Override
	public String decode(String encodedString) {
		return DecodeUtil.decode(encodedString, "(", ")", 
				ResolverFactory.FORMATTER_SEPARATOR, ResolverFactory.MODIFIER_SEPARATOR);
	}
	
	public abstract Object resolve(Object valueObject);
}

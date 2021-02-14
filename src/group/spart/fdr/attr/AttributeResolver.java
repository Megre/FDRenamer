package group.spart.fdr.attr;

import group.spart.fdr.FDRFilter;

/** 
 * 
 * @author megre
 * @email renhao.x@seu.edu.cn
 * @version created on: 2021年2月10日 下午8:54:34 
 */
public abstract class AttributeResolver {

	private String fResolver;
	private FDRFilter fFilter;
	private FileAttribute fFileAttribute;
	
	public AttributeResolver(String resolver) {
		fResolver = resolver.trim();
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
	
	public abstract String resolve(Object valueObject);
}

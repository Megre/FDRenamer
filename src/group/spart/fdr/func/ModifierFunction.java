package group.spart.fdr.func;

import java.util.List;

import group.spart.fdr.FDRFilter;
import group.spart.fdr.attr.SpecialCharDecoder;

/** 
 * 
 * @author megre
 * @email renhao.x@seu.edu.cn
 * @version created on: 2021-02-19 17:42:35
 */
public abstract class ModifierFunction implements SpecialCharDecoder {
	
	private String fName;
	private List<String> fParamList;
	
	public ModifierFunction(String name, List<String> paramList) {
		fName = decode(name);
		fParamList = paramList;
		
		for(int index=0; index<fParamList.size(); ++index) {
			fParamList.set(index, decode(fParamList.get(index)));
		}
	}
	
	public String getName() {
		return fName;
	}
	
	public List<String> listParams() {
		return fParamList;
	}
	
	/**
	 * @see group.spart.fdr.attr.SpecialCharDecoder#decode(java.lang.String)
	 */
	@Override
	public String decode(String encodedString) {
		return encodedString;
	}
	
	public abstract String modify(String value, FDRFilter filter);

}

package group.spart.fdr;

import group.spart.fdr.attr.FileAttribute;
import group.spart.fdr.matcher.FilterMatcher;
import group.spart.fdr.matcher.MatcherFactory;
import group.spart.fdr.option.OptionValue;
import group.spart.fdr.option.PairValue;

/** 
 * 
 * @author megre
 * @email renhao.x@seu.edu.cn
 * @version created on: 2021年2月7日 下午4:13:15 
 */
public class InflatedFilterOption {
	
	private String fKey, fValue;
	private FilterMatcher fMatcher;
	
	public InflatedFilterOption(FileAttribute fileAttribute, FDRFilter filter, OptionValue optionValue) {
		if(PairValue.isPairValue(optionValue.getRawText())) {
			PairValue pairValue = optionValue.asPairValue();
			fKey = fileAttribute.inflate(pairValue.getKey(), filter);
			fValue = fileAttribute.inflate(pairValue.getValue().getRawText(), filter);
		}
		else {
			fKey = fileAttribute.inflate(optionValue.getRawText(), filter);
			fValue = null;
		}
		
		fMatcher = MatcherFactory.matcher(fValue);
	}
	
	public String key() {
		return fKey;
	}
	
	public String value() {
		return fValue;
	}
	
	public FilterMatcher matcher() {
		return fMatcher;
	}
	
	public boolean matches() {
		return fKey != null 
			&& fValue != null
			&&fMatcher.matches(fKey);
	}
}
package group.spart.fdr.option;

import group.spart.fdr.attr.SpecialCharDecoder;

/** 
 * 
 * @author megre
 * @email renhao.x@seu.edu.cn
 * @version created on: Jan 26, 2021 3:40:38 PM 
 */
 public abstract class OptionValue implements SpecialCharDecoder {
	protected String fText;
	
	public OptionValue(String text) { 
		fText = text.trim();
	}
	
	public String getRawText() {
		return fText;
	}
	
	public ListValue asListValue() {
		if(this instanceof ListValue) return (ListValue) this;
		
		return new ListValue(fText);
	}
	
	public PairValue asPairValue() {
		if(this instanceof PairValue) return (PairValue) this;
		
		return new PairValue(fText);
	}
	
	public OptionValue getValue(String key) {
		return asPairValue().getValue(key);
	}
	
	public OptionValue getValue(int index) {
		return asListValue().getValue(index);
	}
	
	public boolean containsKey(String key) {
		for(OptionValue value: asListValue().getValues()) {
			if(value.asPairValue().getKey().equals(key)) return true;
		}
		
		return false;
	}
	
	/**
	 * Append current value, e.g. "abc;def=ghi" appending "mno=pqr" equals "abc;def=ghi;mno=pqr";
	 * 		"abc;def=ghi" appending "abc=pqr" equals "abc;def=ghi".
	 * @param optionValue 
	 * @return
	 */
	public ListValue append(OptionValue optionValue) {
		return asListValue().append(optionValue);
	}
	
	@Override
	public String toString() {
		return getRawText();
	}
	 
}

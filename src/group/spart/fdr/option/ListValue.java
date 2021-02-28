package group.spart.fdr.option;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import group.spart.fdr.util.DecodeUtil;

/** 
 * 
 * @author megre
 * @email renhao.x@seu.edu.cn
 * @version created on: Jan 26, 2021 3:43:43 PM 
 */
public class ListValue extends OptionValue {
	
	public static final String SEPARATOR = ";";

	private List<OptionValue> fValues;
	
	public ListValue(String text) {
		super(text);
		
		String[] values = text.trim().split(ListValue.SEPARATOR);
		fValues = new ArrayList<>();
		for(String value: values) {
			fValues.add(OptionFactory.createNonListOptionValue(decode(value)));
		}
	}
	
	@Override
	public OptionValue getValue(int index) {
		if(index >= 0 && index < fValues.size()) return fValues.get(index);
		
		return null;
	}
	
	@Override
	public OptionValue getValue(String key) {
		for(OptionValue value: fValues) {
			if(PairValue.isPairValue(value.getRawText()) 
					&& value.asPairValue().getKey().equals(key)) {
				return value.asPairValue().getValue();
			}
		}
		
		return null;
	}
	
	@Override
	public ListValue append(OptionValue optionValue) {
		if(optionValue == null) return this;
		
		ListValue listValue = optionValue.asListValue();
		for(OptionValue value: listValue.getValues()) {
			if(containsKey(value.asPairValue().getKey())) continue;
			
			fValues.add(value);
			fText = fText + SEPARATOR + value.getRawText();
		}
		
		return this;
	}
	
	public String getText(String key) {
		if(getValue(key) == null) return null;
		
		return getValue(key).getRawText();
	}
	
	public String getText(int index) {
		if(getValue(index) == null) return null;
		
		return getValue(index).getRawText();
	}

	public List<OptionValue> getValues() {
		return fValues;
	}

	public static boolean isListValue(String text) {
		return text.contains(SEPARATOR);
	}
	
	@Override
	public String toString() {
		StringBuffer stringBuffer = new StringBuffer("ListValue: [");
		for(OptionValue value: fValues) {
			stringBuffer.append(value.toString()).append(", ");
		}
		stringBuffer.append("]");
		return stringBuffer.toString();
	}
	
	@Override
	public boolean equals(Object object) {
		if(!(object instanceof ListValue)) return false;
		
		return Objects.equals(fValues, ((ListValue) object).fValues);
	}

	/**
	 * @see group.spart.fdr.attr.SpecialCharDecoder#decode(java.lang.String)
	 */
	@Override
	public String decode(String encodedString) {
		return DecodeUtil.decode(encodedString, SEPARATOR);
	}
	
}

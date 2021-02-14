package group.spart.fdr.option;

import java.util.Objects;

/** 
 * 
 * @author megre
 * @email renhao.x@seu.edu.cn
 * @version created on: Jan 26, 2021 3:44:04 PM 
 */
public class PairValue extends OptionValue {
	public static final String SEPERATOR = "=";
	
	private String fKey;
	private OptionValue fValue;

	public PairValue(String text) {
		super(text);
		
		String[] keyValue = text.trim().split(SEPERATOR);
		fKey = keyValue[0];
		fValue = keyValue.length > 1 ? OptionFactory.createOptionValue(keyValue[1]) : null;
	}
	
	@Override
	public OptionValue getValue(String key) {
		if(fKey.equals(key)) return fValue;
		
		return null;
	}
	
	public String getKey() {
		return fKey;
	}

	public OptionValue getValue() {
		return fValue;
	}

	public static boolean isPairValue(String text) {
		return text.contains(SEPERATOR);
	}

	@Override
	public String toString() {
		return "PairValue: {<" + fKey + "> = <" + fValue + ">}";
	}
	
	@Override
	public boolean equals(Object object) {
		if(!(object instanceof PairValue)) return false;
		
		return Objects.equals(fKey, ((PairValue) object).fKey) 
				&& Objects.equals(fValue, ((PairValue) object).fValue); 
	}
}

package group.spart.fdr.option;

import java.util.Objects;

/** 
 * 
 * @author megre
 * @email renhao.x@seu.edu.cn
 * @version created on: Jan 26, 2021 3:40:31 PM 
 */
public class OptionItem {
	public static final String SPERATOR = "-";

	private String fName;
	private OptionValue fValue;
	
	public OptionItem(String name, OptionValue value) {
		fName = name.trim();
		fValue = value;
	}
	
	public String getItemName() {
		return fName;
	}
	
	public OptionValue getItemValue() {
		return fValue;
	}
	
	public OptionItem append(OptionValue optionValue) {
		if(fValue == null) fValue = optionValue;
		else {
			fValue = fValue.append(optionValue);
		}
		
		return this;
	}
	
	@Override
	public String toString() {
		return "{" + fName + " -> " + fValue + "}\n";
	}
	
	@Override
	public boolean equals(Object object) {
		if(!(object instanceof OptionItem)) return false;
		
		return Objects.equals(fName, ((OptionItem) object).fName)
				&& Objects.equals(fValue, ((OptionItem) object).fValue);
	}
	
}

package group.spart.fdr.option;

/** 
 * 
 * @author megre
 * @email renhao.x@seu.edu.cn
 * @version created on: Jan 26, 2021 3:42:33 PM 
 */
public class SingleValue extends OptionValue {
	
	public SingleValue(String text) {
		super(text);
	}
	
	@Override
	public String toString() {
		return "SingleValue: " + fText;
	}
	
	@Override
	public boolean equals(Object object) {
		if(!(object instanceof OptionValue)) return false;
		
		return fText.equals(((OptionValue) object).getRawText());
	}
	
}

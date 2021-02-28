package group.spart.fdr.option;

import java.util.ArrayList;
import java.util.List;

/** 
 * 
 * @author megre
 * @email renhao.x@seu.edu.cn
 * @version created on: Jan 26, 2021 3:45:19 PM 
 */
public class OptionFactory {
	
	private OptionFactory() { }

	public static List<OptionItem> parse(String[] args) {
		final List<OptionItem> optionItems = new ArrayList<>();
		
		for(int idx=0; args != null && idx<args.length; ++idx) {
			final OptionItem item = buildOptionItem(args, idx);
			if(item != null) optionItems.add(item);
		}
		
		return optionItems;
	}
	
	public static OptionValue createOptionValue(String text) {
		if(isListValue(text)) {
			return new ListValue(text);
		}
		
		return createNonListOptionValue(text);
	}
	
	public static OptionValue createNonListOptionValue(String text) {
		if(isPairValue(text)) {
			return new PairValue(text);
		}
		
		return new SingleValue(text);
	}
	
	public static boolean equals(OptionValue optionValue, Object object) {
		if(!(object instanceof OptionValue)) return false;
		
		if(isListValue(optionValue.getRawText())) return optionValue.asListValue().equals(object);
		if(isPairValue(optionValue.getRawText())) return optionValue.asPairValue().equals(object);
		
		return optionValue.getRawText().equals(((OptionValue) object).getRawText());
	}
	
	private static OptionItem buildOptionItem(String[] args, int index) {
		final String itemText = args[index].trim();
		if(itemText.length() < 2 || !itemText.startsWith(OptionItem.SPERATOR)) return null;
		
		return new OptionItem(itemText.substring(OptionItem.SPERATOR.length()), 
				index+1<args.length ? createOptionValue(args[index+1]) : null);
	}
	
	private static boolean isListValue(String text) {
		return ListValue.isListValue(text);
	}
	
	private static boolean isPairValue(String text) {
		return PairValue.isPairValue(text);
	}
	
}

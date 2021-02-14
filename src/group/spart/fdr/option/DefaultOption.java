package group.spart.fdr.option;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

/** 
 * 
 * @author megre
 * @email renhao.x@seu.edu.cn
 * @version created on: Jan 27, 2021 11:02:45 PM 
 */
public class DefaultOption {

	private HashMap<String, OptionItem> fItems = new HashMap<>();
	private HashSet<String> fSingleValues = new HashSet<>();
	
	public void put(String itemName, String optionValueText) {
		OptionValue optionValue = OptionFactory.createOptionValue(optionValueText);
		if(fItems.get(itemName) == null) {
			OptionItem item = new OptionItem(itemName, optionValue);
			fItems.put(itemName, item);
		}
		else if(!isSingleValue(itemName)) {
			OptionItem item = fItems.get(itemName);
			fItems.put(itemName, item.append(optionValue));
		}
	}
	
	public void put(String itemName, String optionValueText, boolean singleValue) {
		put(itemName, optionValueText);
		if(singleValue) fSingleValues.add(itemName);
	}
	
	public boolean isSingleValue(String itemName) {
		return fSingleValues.contains(itemName);
	}
	
	public OptionItem get(String itemName) {
		return fItems.get(itemName);
	}
	
	public Set<Entry<String, OptionItem>> getEntries() {
		return fItems.entrySet();
	}
}

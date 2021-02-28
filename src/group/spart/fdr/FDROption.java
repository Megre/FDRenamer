package group.spart.fdr;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import group.spart.fdr.option.DefaultOption;
import group.spart.fdr.option.OptionFactory;
import group.spart.fdr.option.OptionItem;
import group.spart.fdr.option.OptionValue;

/** 
 * 
 * @author megre
 * @email renhao.x@seu.edu.cn
 * @version created on: Jan 26, 2021 4:55:46 PM 
 */
public class FDROption {
	private static Logger logger = LogManager.getLogger(FDROption.class);
	
	private OptionInfo fOptionInfo;
	private DefaultOption fDefaultOption;
	
	public FDROption(String[] options) {
		fOptionInfo = new OptionInfo();
		
		parseOptions(options);
		if(getTextOptionValue("config") != null) {
			parseOptions(readOpitons(new File(getTextOptionValue("config"))));
		}
		loadDefaultOptions();
	}
	
	public List<OptionItem> getOptionItems() {
		return fOptionInfo.getOptionItems();
	}
	
	public String getTextOptionValue(String optionName) {
		if(getOptionValue(optionName) == null) return null;
		
		return getOptionValue(optionName).getRawText();
	}
	
	public OptionValue getOptionValue(String optionName) {
		return fOptionInfo.get(optionName);
	}
	
	private void initDefaultOptions() {
		fDefaultOption = new DefaultOption();
		
		// global
		fDefaultOption.put("mode", "preview", true); // preview, exec
		fDefaultOption.put("processSubdir", "false", true); // true, false
		
		// process local
		fDefaultOption.put("process", "action=copy"); // copy, move
		fDefaultOption.put("process", "replaceExisting=false"); // true, false
		fDefaultOption.put("process", "remainDirStructure=true"); // true, false
		fDefaultOption.put("process", "outputDir=./");
		fDefaultOption.put("process", "filePathName=${fileName}");
		fDefaultOption.put("process", "onInvalidParam=ignore"); // ignore, transfer
	}
	
	private void parseOptions(String[] options) {
		if(options == null) return;
		
		fOptionInfo.put(OptionFactory.parse(options));
	}
	
	private void loadDefaultOptions() {
		initDefaultOptions();
		for(Entry<String, OptionItem> entry: fDefaultOption.getEntries()) {
			if(!fOptionInfo.contains(entry.getKey())) fOptionInfo.put(entry.getValue());
			else if(!fDefaultOption.isSingleValue(entry.getKey())){
				appendItem(entry.getKey(), fOptionInfo, fDefaultOption);
			}
		}
	}
	
	private void appendItem(String itemName, OptionInfo optionInfo, DefaultOption defaultOption) {
		List<OptionItem> items = optionInfo.getOptionItems();
		for(int idx=0; idx<items.size(); ++idx) {
			OptionItem defaultItem = defaultOption.get(itemName);
			if(!itemName.equals(items.get(idx).getItemName()) || defaultItem == null) continue;
			
			optionInfo.set(idx, items.get(idx).append(defaultItem.getItemValue()));
		}
	}
	
	private String trimLine(String line) {
		if(line == null) return null;
		
		int commentIndex = line.lastIndexOf('#');
		if(commentIndex < 0) return line.trim();
		
		return line.substring(0, commentIndex).trim();
	}
	
	private String trimQuotation(String string) {
		if(string.startsWith("\"")) {
			return string.substring(1);
		}
		
		if(string.endsWith("\"")) {
			return string.substring(0, string.length()-1);
		}
		
		return string;
	}
	
	private String[] readOpitons(File cfgFile) {
		if(!cfgFile.isFile()) {
			logger.error("configuration file does not exist: " + cfgFile.getAbsolutePath());
			return null;
		}
		
		List<String> options = new ArrayList<>();
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(cfgFile), "UTF-8"))) {
			String line = trimLine(reader.readLine()), anOption = "";
			while(line != null) {
				if(line.length() <= 1) {
					line = trimLine(reader.readLine());
					continue;
				}
				
				if(line.startsWith("-")) {
					if(!anOption.isEmpty()) {
						options.add(anOption);
						anOption = "";
					}
					
					final int splitPos = line.indexOf(' ');
					if(splitPos < 0) options.add(line);
					else {
						options.add(line.substring(0, splitPos));
						anOption += trimQuotation(line.substring(splitPos + 1));
					}
				}
				else {
					anOption += trimQuotation(line);
				}
				
				line = trimLine(reader.readLine());
			}
			
			if(!anOption.isEmpty()) {
				options.add(anOption);
			}
			
			return options.toArray(new String[0]);
		} catch (IOException e) {
			logger.error(e);
		}
		
		return null;
	}
	
	private class OptionInfo {
		private List<OptionItem> fOptionItems;
		private MultipleMap<String, OptionValue> fOptionValues;
		private Map<Integer, Integer> fIndexMap;
		
		public OptionInfo() {
			fOptionItems = new ArrayList<>();
			fOptionValues = new MultipleMap<>();
			fIndexMap = new HashMap<>();
		}
		
		public boolean contains(String optionItemName) {
			return fOptionValues.getFirst(optionItemName) != null;
		}
		
		public List<OptionItem> getOptionItems() {
			return fOptionItems;
		}
		
		public void put(List<OptionItem> optionItems) {
			for(OptionItem item: optionItems) {
				put(item);
			}
		}
		
		public void put(OptionItem optionItem) {
			if(optionItem == null) return;
			
			fOptionItems.add(optionItem);
			int toIndex = fOptionValues.put(optionItem.getItemName(), optionItem.getItemValue());
			fIndexMap.put(fOptionItems.size() - 1, toIndex);
		}
		
		public OptionValue get(String optionName) {
			return fOptionValues.getFirst(optionName);
		}
		
		public void set(int index, OptionItem optionItem) {
			fOptionItems.set(index, optionItem);
			fOptionValues.getList(fOptionItems.get(index).getItemName())
				.set(fIndexMap.get(index), optionItem.getItemValue());
		}
	}
	
}

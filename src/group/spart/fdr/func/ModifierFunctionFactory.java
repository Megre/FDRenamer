package group.spart.fdr.func;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/** 
 * 
 * @author megre
 * @email renhao.x@seu.edu.cn
 * @version created on: 2021-02-20 23:54:49
 */
public class ModifierFunctionFactory {
	
	private static Logger logger = LogManager.getFormatterLogger(ModifierFunctionFactory.class);
	
	public final static String SEPARATOR = "[\\(,\\)]";
	
	private String fName;
	private List<String> fParamList;
	
	private ModifierFunctionFactory() { 
		fParamList = new ArrayList<>();
	}
	
	public static ModifierFunction create(String modifier) {
		final ModifierFunctionFactory factory = new ModifierFunctionFactory();
		factory.parse(modifier);
		
		String name = factory.getName();
		if(name == null) return null;
		
		if(name.equalsIgnoreCase("cg")) { // capture group
			return new CaptureGroupFunction(name, factory.listParams());
		}
		
		if(name.equalsIgnoreCase("case")) { // uppercase / lowercase
			return new CaseFunction(name, factory.listParams());
		}

		if(name.equalsIgnoreCase("substr")) { // substr(beginIndex) or sustr(beginIndex, endIndex)
			return new SubstrFunction(name, factory.listParams());
		}
		
		logger.error("no such modifier: " + name);
		
		return null;
	}
	
	
	private String getName() {
		return fName;
	}
	
	private List<String> listParams() {
		return fParamList;
	}
	
	private void parse(String modifier) {
		if(modifier == null) return;
		
		String[] array = modifier.trim().split(SEPARATOR);
		if(array.length < 1) return;
		
		// name
		fName = array[0].trim();
		
		// parameters
		for(int index=1; index<array.length; ++index) {
			fParamList.add(array[index].trim());
		}
	}
}

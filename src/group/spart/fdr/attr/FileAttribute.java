package group.spart.fdr.attr;

import java.io.File;
import java.util.HashMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import group.spart.fdr.FDRFilter;
import group.spart.fdr.util.StringUtil;

/** 
 * 
 * @author megre
 * @email renhao.x@seu.edu.cn
 * @version created on: Jan 26, 2021 7:28:17 PM 
 */
public class FileAttribute {
	@SuppressWarnings("unused")
	private Logger logger = LogManager.getLogger(FileAttribute.class);
	
	private File fFile;
	private HashMap<String, Object> fAttributes;
	
	public FileAttribute(File file) {
		fFile = file;
		fAttributes = new HashMap<>();
	}
	
	public File getFile() {
		return fFile;
	}
	
	/**
	 * Inflate the parameterized attributes by their values.
	 * @param parameterizedAttribute e.g. ${mediaCreationDate, yyyy-MM}/$fileName
	 * @param filter
	 * @return
	 */
	public String inflate(String parameterizedAttribute, FDRFilter filter) {
		if(parameterizedAttribute == null) return null;
		
		ParameterizedAttribute attribute = new ParameterizedAttribute(parameterizedAttribute);
		for(OptionalAttributeEntry entry: attribute.listEntries()) {
			String attributeValue = getAttributeValue(entry, filter);
			if(attributeValue == null || attributeValue.isEmpty()) return null;
			
			attribute.inflate(attributeValue);
		}
		return attribute.value();
	}
	
	/**
	 * 
	 * @param attributeEntry e.g. filePathName=${mediaCreationDate, yyyy-MM | creationDate, yyyy.MM}/%fileName%;" 
	 * @param filter
	 * @return calculated value of the attribute
	 */
	private String getAttributeValue(OptionalAttributeEntry attributeEntry, FDRFilter filter) {
		String result = StringUtil.EMPTY_VALUE;
		for(SimpleAttributeEntry entry: attributeEntry.listOptionalEntries()) {
			AttributeValue attributeValue = new AttributeValue(entry, this, filter);
			String value = fAttributes.containsKey(entry.getName()) 
					? attributeValue.value(fAttributes.get(entry.getName())) 
					: attributeValue.value();
			
			if((attributeValue.valueObject() != null || value == null) && !entry.getName().isEmpty()) {
				fAttributes.put(entry.getName(), attributeValue.valueObject());
			}
			
			if(value != null) result = value;
			if(!result.isEmpty()) return result;
		}
		
		return null;
	}
	
	@Override
	public String toString() {
		return "[" + FileAttribute.class.getSimpleName() + "] " + fFile.getAbsolutePath();
	}
}

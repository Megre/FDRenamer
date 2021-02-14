package group.spart.fdr.attr;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import group.spart.fdr.FDRFilter;
import group.spart.fdr.meta.MetadataReader;
import group.spart.fdr.util.FileNameUtil;
import group.spart.fdr.util.StringUtil;
import group.spart.fdr.util.TimeUtil;

/** 
 * 
 * @author megre
 * @email renhao.x@seu.edu.cn
 * @version created on: Jan 26, 2021 11:39:46 PM 
 */
public class AttributeValue {
	private static Logger logger = LogManager.getLogger(AttributeValue.class);
	
	private SimpleAttributeEntry fSimpleEntry;
	private FDRFilter fFilter;
	private FileAttribute fFileAttribute;
	private Object fValueObject;
	
	public AttributeValue(SimpleAttributeEntry simpleEntry, FileAttribute fileAttribute, FDRFilter filter) {
		fSimpleEntry = simpleEntry;
		fFilter = filter;
		fFileAttribute = fileAttribute;
	}
	
	public String value() {
		if("renamer".equals(fSimpleEntry.getName())) {
			return rename(fFileAttribute, fFilter);
		}
		
		return value(retrieveValueObject());
	}
	
	public String value(Object valueObject) {
		fValueObject = valueObject;
		Object result = valueObject;
		for(AttributeResolver resolver: fSimpleEntry.listResolvers()) {
			result = resolver.setFileAttribute(fFileAttribute).setFilter(fFilter).resolve(result);
		}
		return result == null ? null : result.toString();
	}
	
	public Object valueObject() {
		return fValueObject;
	}
	
	/**
	 * Rename the file using the path and name returned by a renamer.
	 * 		The renamer is specified in the "-process" option.
	 * @param fileAttribute
	 * @param filter
	 * @return
	 */
	public String rename(FileAttribute fileAttribute, FDRFilter filter) {
		FileRenamer renamer = getFileRenamer(filter);
		if(renamer == null) return null;
		
		return renamer.rename(fileAttribute, filter);
	}
	
	/**
	 * 
	 * @return value of the attribute. 
	 * 		Returns empty string if the attribute name is an enmpty string.
	 * 		Returns null if the specified attribute name cann't be retrieved.
	 */
	private Object retrieveValueObject() {
		if(fSimpleEntry.getName().isEmpty()) return StringUtil.EMPTY_VALUE;
		
		File file = fFileAttribute.getFile();
		String attributeName = fSimpleEntry.getName(); 
		
		if(attributeName.equals("fileName")) {
			return file.getName();
		}
		
		if(attributeName.equals("filePath")) {
			return file.getAbsolutePath().replaceAll("\\", "/");
		}
		
		if(attributeName.equals("fileNameExt")) {
			return FileNameUtil.getFileNameExt(file);
		}
		
		if(attributeName.equals("fileNameNoExt")) {
			return FileNameUtil.getFileNameNoExt(file);
		}
		
		BasicFileAttributes attributes = getBasicFileAttributes(file);
		if(attributes != null) {
			if(attributeName.equals("creationDate")) {
				return TimeUtil.fileTime2Date(attributes.creationTime());
			}
			
			if(attributeName.equals("lastAccessDate")) {
				return TimeUtil.fileTime2Date(attributes.lastAccessTime());
			}
			
			if(attributeName.equals("lastModifiedDate")) {
				return TimeUtil.fileTime2Date(attributes.lastModifiedTime());
			}
		}
		
		if(attributeName.startsWith("mediaCreationDate")) {
			String date = new MetadataReader().read(file, attributeName);
			if(date != null) return TimeUtil.str2Date(date);
		}
		
		logger.error("[filter " + fFilter.getPosition() + "] invalid attribute name: " + attributeName);
		
		return null;
	}
	
	private FileRenamer getFileRenamer(FDRFilter filter) {
		String renamerPath = filter.getProcessor().getProcessOption().asListValue().getValue("renamer").getRawText();
		if(renamerPath == null) return null;
		
		Class<?> renamer = JavaFileLoader.load(renamerPath);
		if(renamer == null) {
			logger.warn("load renamer failed: " + renamerPath);
			return null;
		}
		
		try {
			renamer.getMethod("rename", FileAttribute.class, FDRFilter.class);
			return (FileRenamer) renamer.newInstance();
		} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException e) {
			logger.error(e);
		} 
		
		return null;
	}
	
	private BasicFileAttributes getBasicFileAttributes(File file) {
		try {
			BasicFileAttributes attributes = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
			return attributes;
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		
		return null;
	}
}

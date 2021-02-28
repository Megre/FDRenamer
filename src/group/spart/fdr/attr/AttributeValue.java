package group.spart.fdr.attr;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Date;

import javax.swing.filechooser.FileSystemView;

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
		
		return renamer == null
				? null
				: renamer.rename(fileAttribute, filter);
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
		
		if(attributeName.equalsIgnoreCase("fileName")) {
			return file.getName();
		}
		
		if(attributeName.equalsIgnoreCase("fileNameExt")) {
			return FileNameUtil.getFileNameExt(file);
		}
		
		if(attributeName.equalsIgnoreCase("fileNameNoExt")) {
			return FileNameUtil.getFileNameNoExt(file);
		}		
		
		if(attributeName.equalsIgnoreCase("filePath")) {
			return file.getAbsolutePath();
		}
		
		if(attributeName.equalsIgnoreCase("fileSize")) {
			try {
				return new FileSize(file.length());
			}
			catch (SecurityException e) {
				logger.error(e);
				return null;
			}
		}
		
		if(attributeName.equalsIgnoreCase("rootDir")) {
			int index = file.getAbsolutePath().indexOf(File.separator);
			return index < 0 
					? file.getAbsolutePath() 
					: file.getAbsolutePath().substring(0, index);
		}
		
		if(attributeName.equalsIgnoreCase("parentDirPath")) {
			return file.getParentFile().getAbsolutePath();
		}
		
		if(attributeName.equalsIgnoreCase("parentDirName")) {
			return file.getParentFile().getName();
		}
		
		BasicFileAttributes attributes = getBasicFileAttributes(file);
		if(attributes != null) {
			if(attributeName.equalsIgnoreCase("creationDate")) {
				return TimeUtil.fileTime2Date(attributes.creationTime());
			}
			
			if(attributeName.equalsIgnoreCase("lastAccessDate")) {
				return TimeUtil.fileTime2Date(attributes.lastAccessTime());
			}
			
			if(attributeName.equalsIgnoreCase("lastModifiedDate")) {
				return TimeUtil.fileTime2Date(attributes.lastModifiedTime());
			}
			
		}
		
		if(attributeName.equalsIgnoreCase("currentDate")) {
			return new Date();
		}
		
		if(attributeName.toLowerCase().startsWith("mediaCreationDate".toLowerCase())) {
			return TimeUtil.str2Date(new MetadataReader().read(file, attributeName));
		}
		
		if(attributeName.equalsIgnoreCase("desktop")) {
			return FileSystemView.getFileSystemView().getHomeDirectory().getAbsolutePath();
		}

		try {
			if (System.getenv(attributeName) != null) {
				return System.getenv(attributeName);
			}
		} catch (SecurityException e) {
			logger.error(e);
			return null;
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

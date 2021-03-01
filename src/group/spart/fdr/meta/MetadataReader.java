package group.spart.fdr.meta;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;

import group.spart.fdr.util.FileNameUtil;

/** 
 * 
 * @author megre
 * @email renhao.x@seu.edu.cn
 * @version created on: Jan 28, 2021 12:51:42 AM 
 */
public class MetadataReader {
	private Logger logger = LogManager.getLogger(MetadataReader.class);
	
	public String read(File file, String attriuteName) {
		final String ext = FileNameUtil.getFileNameExt(file);
		final String directory = MetadataKey.getDirectory(ext, attriuteName);
		final String key = MetadataKey.getKey(ext, attriuteName);
		
		return read(file, directory, key);
	}
	
	private String read(File file, String directory, String key) {
		Metadata metadata = getMetadata(file);
		if(metadata == null) return null;
		
		Iterator<Directory> directories;
		directories = metadata.getDirectories().iterator();
		while(directories.hasNext()) {
			Directory curDir = directories.next();
			Iterator<Tag> tags = curDir.getTags().iterator();
			while(tags.hasNext()) {
				Tag tag = tags.next();
				logger.trace(curDir.getName() + ": " + tag.getTagName() + ": " + tag.getDescription());
				if((directory == null || containsIgnoreCase(curDir.getName(), directory))
						&& containsIgnoreCase(tag.getTagName(), key)) {
					return tag.getDescription();
				}
			}
		} 
		
		return null;
	}
	
	private boolean containsIgnoreCase(String string1, String string2) {
		if(string1 == null || string2 == null) return false;
		return string1.toUpperCase().contains(string2.toUpperCase());
	}
	
	private Metadata getMetadata(File file) {
		try {
			Class<?> metadataReader = Class.forName(getMetadataClassName(file));
			Method readMetadata = metadataReader.getMethod("readMetadata", File.class);
			return (Metadata) readMetadata.invoke(null, file);
		} catch (ClassNotFoundException | NoSuchMethodException | SecurityException
				| IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			logger.warn("unable to read the meta data of " + file.getAbsolutePath());
		} catch (Exception e) {
			logger.error(e);
		}
		
		return null;
	}
	
	private String getMetadataClassName(File file) {
		String ext = getExt(file);
		return ext == null
				? null
			    : ("com.drew.imaging." + ext + "." + curveWord(ext) + "MetadataReader");
	}
	
	private String getExt(File file) {
		String ext = FileNameUtil.getFileNameExt(file);
		if("jpg".equalsIgnoreCase(ext)) ext = "jpeg";
		return ext == null ? null : ext.toLowerCase();
	}
	
	private String curveWord(String word) {
		return word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();
	}

}
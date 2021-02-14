package group.spart.fdr.meta;

import java.util.HashMap;

/** 
 * 
 * @author megre
 * @email renhao.x@seu.edu.cn
 * @version created on: Jan 28, 2021 2:33:30 AM 
 */
public class MetadataKey {

	private static HashMap<String, HashMap<String, DirKey>> fMetaTable = new HashMap<>();
	
	static {
		addEntry("mp4", "mediaCreationDate", "mp4", "creation time");
		addEntry("jpg", "mediaCreationDate", "exif", "date/time");
		addEntry("jpeg", "mediaCreationDate", "exif", "date/time");
	}
	
	public static String getDirectory(String ext, String attriuteName) {
		DirKey dirKey = getDirKey(ext, attriuteName);
		if(dirKey == null) return null;
		
		return dirKey.fDir;
	}
	
	public static String getKey(String ext, String attriuteName) {
		DirKey dirKey = getDirKey(ext, attriuteName);
		if(dirKey == null) return null;
		
		return dirKey.fKey;
	}
	
	private static DirKey getDirKey(String ext, String attriuteName) {
		HashMap<String, DirKey> map = fMetaTable.get(ext);
		if(map == null) return null;
		
		return map.get(attriuteName);
	}
	
	private static void addEntry(String ext, String attriuteName, String directory, String key) {
		HashMap<String, DirKey> map = fMetaTable.get(ext);
		if(map == null) {
			map = new HashMap<>();
			fMetaTable.put(ext, map);
		}
		map.put(attriuteName, new DirKey(directory, key));
	}
	
	private static class DirKey {
		String fDir, fKey;
		DirKey(String dir, String key) {
			fDir = dir;
			fKey = key;
		}
		
	}
}

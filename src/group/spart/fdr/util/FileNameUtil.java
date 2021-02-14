package group.spart.fdr.util;

import java.io.File;

/** 
 * 
 * @author megre
 * @email renhao.x@seu.edu.cn
 * @version created on: Jan 28, 2021 1:30:07 AM 
 */
public class FileNameUtil {

	public static String getFileNameNoExt(File file) {
		int index = file.getName().lastIndexOf('.');
		if(index < 0) return file.getName();
		
		return file.getName().substring(0, index);
	}
	
	public static String getFileNameExt(File file) {
		int index = file.getName().lastIndexOf('.');
		if(index < 0) return null;
		
		return file.getName().substring(index + 1);
	}
	
	public static boolean isSubFile(File file1, File file2) {
		final String file1Path = file1.getAbsolutePath(), file2Path = file2.getAbsolutePath();
		return file1Path.startsWith(file2Path) 
				&& file1Path.replace(file2Path, "").matches("[/\\\\].*");
	}
	
}

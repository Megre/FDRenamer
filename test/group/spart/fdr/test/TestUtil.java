package group.spart.fdr.test;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

import group.spart.fdr.util.FileNameUtil;

/** 
 * 
 * @author megre
 * @email renhao.x@seu.edu.cn
 * @version created on: 2021-02-06 10:39:33 PM
 */
public class TestUtil {
	
	public static final String TEST_DIR = "testData";

	public static void deleteFile(File file) throws IOException {
		if(file.isFile()) {
			file.delete();
		}
		else if(file.isDirectory()) {
			for(File subFile: file.listFiles()) {
				deleteFile(subFile);
			}
			Files.delete(file.toPath());
		}
	}
	
	public static void copyFile(File source, File dest) throws IOException {
		if(dest.getAbsolutePath().equals(source.getAbsolutePath()) ||
				FileNameUtil.isSubFile(dest, source)) return;
		
		if(source.isDirectory()) {
			for(File subFile: source.listFiles()) {
				copyFile(subFile, new File(dest.getAbsolutePath() + "/" + subFile.getName()));
			}
		}
		else if(source.isFile()) {
			if(!dest.exists()) {
				dest.getParentFile().mkdirs();
				dest.createNewFile();
			}

			OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(dest));
			Files.copy(source.toPath(), outputStream);
			outputStream.flush();
			outputStream.close();
		}
	}
	
	public static void init() throws IOException {
		deleteFile(new File(TEST_DIR + "/source"));
		deleteFile(new File(TEST_DIR + "/destination"));
		copyFile(new File(TEST_DIR + "/source_init"), new File(TEST_DIR + "/source"));
		
		deleteFile(new File(TEST_DIR + "/TimeStampRenamer.class"));
		deleteFile(new File(TEST_DIR + "/TimeStampRenamer.java"));
		copyFile(new File("src/TimeStampRenamer.java"), new File(TEST_DIR + "/TimeStampRenamer.java"));
		copyFile(new File("bin/TimeStampRenamer.class"), new File(TEST_DIR + "/TimeStampRenamer.class"));
		
	}
}

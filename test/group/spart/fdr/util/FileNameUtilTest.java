package group.spart.fdr.util;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

/** 
 * 
 * @author megre
 * @email renhao.x@seu.edu.cn
 * @version created on: 2021年2月2日 上午2:21:43 
 */
public class FileNameUtilTest {

	@Test
	public void testFileNameUtil() {
		assertEquals("testFileName", FileNameUtil.getFileNameNoExt(new File("testFileName.jpg")));
		assertEquals("testFileName", FileNameUtil.getFileNameNoExt(new File("testFileName.")));
		assertEquals("testFileName", FileNameUtil.getFileNameNoExt(new File("testFileName")));
		
		assertEquals("jpg", FileNameUtil.getFileNameExt(new File("testFileName.jpg")));
		assertEquals(null, FileNameUtil.getFileNameExt(new File("testFileName")));
		assertEquals("", FileNameUtil.getFileNameExt(new File("testFileName.")));
		
		assertFalse(FileNameUtil.isSubFile(new File("abc"), new File("abc")));
		assertFalse(FileNameUtil.isSubFile(new File("abc"), new File("abc/")));
		assertFalse(FileNameUtil.isSubFile(new File("abc/"), new File("abc")));
		assertFalse(FileNameUtil.isSubFile(new File("abc"), new File("abc/def")));
		assertTrue(FileNameUtil.isSubFile(new File("abc/def"), new File("abc")));
	}
}

package group.spart.fdr.attr;

import java.io.File;
import java.lang.reflect.Method;

import org.junit.Test;
import static org.junit.Assert.*;

import group.spart.fdr.FDRFilter;
import group.spart.fdr.test.TestUtil;

/** 
 * 
 * @author megre
 * @email renhao.x@seu.edu.cn
 * @version created on: 2021年2月14日 下午11:42:52 
 */
public class TestJavaFileLoader {
	
	@Test
	public void testJavaFileLoader() throws Exception {
		Class<?> clazz = JavaFileLoader.load(TestUtil.TEST_DIR + "/TimeStampRenamer.java");
		Method rename = clazz.getMethod("rename", FileAttribute.class, FDRFilter.class);
		assertEquals("2018-08", rename.invoke(clazz.newInstance(), new FileAttribute(new File("mmexport1533467816512.jpg")), (FDRFilter) null).toString());
	}
}

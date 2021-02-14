
import java.io.File;
import java.io.IOException;

import org.junit.Test;

import group.spart.fdr.attr.FileAttribute;
import group.spart.fdr.test.TestUtil;

import static org.junit.Assert.*;

/** 
 * 
 * @author megre
 * @email renhao.x@seu.edu.cn
 * @version created on: 2021年1月30日 下午12:36:09 
 */
public class TimeStampRenamerTest {

	@Test
	public void testRename() throws IOException {
		TestUtil.init(); 
		
		String name = new TimeStampRenamer().rename(new FileAttribute(new File("mmexport1533467816512.jpg")), null);
		assertTrue(name.matches("2018.*08.*"));
		
		assertEquals(null, new TimeStampRenamer().rename(null, null));
	}
}

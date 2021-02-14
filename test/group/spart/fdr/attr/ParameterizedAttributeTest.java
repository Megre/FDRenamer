package group.spart.fdr.attr;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

/** 
 * 
 * @author megre
 * @email renhao.x@seu.edu.cn
 * @version created on: 2021年2月5日 下午6:19:54 
 */
public class ParameterizedAttributeTest {

	static ParameterizedAttribute attribute1, attribute2, attribute3, attribute4;
	
	@BeforeClass
	public static void Init() {
		attribute1 = new ParameterizedAttribute("dir_$creationDate, yyyy-MM ");
		attribute2 = new ParameterizedAttribute("dir_${creationDate, yyyy}_${creationDate, MM}");
		attribute3 = new ParameterizedAttribute("dir_${creationDate, yyyy}_${creationDate, MM}_01");
		attribute4 = new ParameterizedAttribute("dir_${creationDate, yyyy: dd}");
	}
	
	@Test(expected = NullPointerException.class)
	public void testParameterizedAttribute() {
		new ParameterizedAttribute(null);
	}
 	
	@Test
	public void testSplit() {
		assertEquals(1, attribute1.listEntries().size());
		assertEquals(2, attribute2.listEntries().size());
		assertEquals(2, attribute3.listEntries().size());
		assertEquals(1, attribute4.listEntries().size());
	}
	
	@Test
	public void testInflate() {
		assertEquals("dir_aaa, yyyy-MM", attribute1.inflate("aaa").value());
		assertEquals("dir_2021_${creationDate, MM}", attribute2.inflate("2021").value());
		assertEquals("dir_2021_02", attribute2.inflate("02").value());
		assertEquals("dir_2021_${creationDate, MM}_01", attribute3.inflate("2021").value());
		assertEquals("dir_2021_02_01", attribute3.inflate("02").value());
	}
}

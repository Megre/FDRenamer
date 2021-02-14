package group.spart.fdr.attr;

import static org.junit.Assert.*;

import org.junit.Test;

/** 
 * 
 * @author megre
 * @email renhao.x@seu.edu.cn
 * @version created on: 2021年2月6日 下午8:14:14 
 */
public class SingleAttributeEntryTest {

	@Test
	public void testSingleAttributeEntry() {
		assertEquals("", new SimpleAttributeEntry("").getName());
		assertEquals("name", new SimpleAttributeEntry(" name,").getName());
		assertEquals(0, new SimpleAttributeEntry(" name,").listResolvers().size());
		assertEquals("name", new SimpleAttributeEntry(" name, format").getName());
		assertEquals("format", new SimpleAttributeEntry(" name, format ").listResolvers().get(0).getResolverText());
		assertEquals("modifier", new SimpleAttributeEntry(" name, format : modifier ").listResolvers().get(1).getResolverText());
		assertEquals("modifier:", new SimpleAttributeEntry(" name%2c, format : modifier%3a ").listResolvers().get(1).getResolverText());
		assertEquals("name", new SimpleAttributeEntry(" name%2c, %10s : modifier%3a ").getName());
		assertEquals("%10s", new SimpleAttributeEntry(" name%2c, %10s : modifier%3a ").listResolvers().get(0).getResolverText());
		assertEquals("modifier:", new SimpleAttributeEntry(" name, %10s : modifier%3a ").listResolvers().get(1).getResolverText());
		assertEquals("++", new SimpleAttributeEntry("fileName: ++, %-10s").listResolvers().get(0).getResolverText());
		assertEquals("%-10s", new SimpleAttributeEntry("fileName: ++, %-10s").listResolvers().get(1).getResolverText());
		assertEquals("a(++)", new SimpleAttributeEntry("fileName: a(++), b(%-10s, d)").listResolvers().get(0).getResolverText());
		assertEquals("b(%-10s, d)", new SimpleAttributeEntry("fileName: a(++), b(%-10s, d)").listResolvers().get(1).getResolverText());
	}
}

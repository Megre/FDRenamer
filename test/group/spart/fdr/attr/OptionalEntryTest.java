package group.spart.fdr.attr;

import static org.junit.Assert.*;

import org.junit.Test;

/** 
 * 
 * @author megre
 * @email renhao.x@seu.edu.cn
 * @version created on: 2021年2月5日 下午9:17:00 
 */
public class OptionalEntryTest {

	@Test
	public void testAttributeEntry() {
		assertEquals("creationDate", new OptionalAttributeEntry(" creationDate , ", 0).listOptionalEntries().get(0).getName());
		assertEquals("creationDate", new OptionalAttributeEntry(" creationDate , yyyy-MM ", 0).listOptionalEntries().get(0).getName());
		assertEquals("yyyy-MM", new OptionalAttributeEntry(" creationDate , yyyy-MM ", 0).listOptionalEntries().get(0).listResolvers().get(0).getResolverText());
		assertEquals("creationDate", new OptionalAttributeEntry(" creationDate , yyyy-MM | mediaCreationDate", 0)
				.listOptionalEntries().get(0).getName());
		assertEquals("yyyy-MM", new OptionalAttributeEntry(" creationDate , yyyy-MM | mediaCreationDate", 0)
				.listOptionalEntries().get(0).listResolvers().get(0).getResolverText());
		assertEquals("mediaCreationDate", new OptionalAttributeEntry(" creationDate , yyyy-MM | mediaCreationDate", 0)
				.listOptionalEntries().get(1).getName());
		assertEquals(0, new OptionalAttributeEntry(" creationDate , yyyy-MM | mediaCreationDate", 0)
				.listOptionalEntries().get(1).listResolvers().size());
	}
}

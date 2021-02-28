package group.spart.fdr.option;

import org.junit.Test;

import static org.junit.Assert.*;
/** 
 * 
 * @author megre
 * @email renhao.x@seu.edu.cn
 * @version created on: 2021年2月1日 下午3:52:24 
 */
public class OptionValueTest {

	@Test(expected = NullPointerException.class)
	public void testOptionValueNullText() {
		OptionFactory.createOptionValue(null);
	}
	
	@Test
	public void testGetRawText() {
		assertEquals("abc", OptionFactory.createOptionValue(" abc").getRawText());
		assertEquals("abc", OptionFactory.createOptionValue(" abc ").getRawText());
	}
	
	@Test
	public void testAsListValue() {
		assertEquals(new ListValue(""), 
				OptionFactory.createOptionValue("  ").asListValue());
		
		assertEquals(new ListValue("abc;;"), 
				OptionFactory.createOptionValue("abc").asListValue());
		
		assertNotEquals(new ListValue("abc; ;"), 
				OptionFactory.createOptionValue("abc").asListValue());
		
		assertEquals(new ListValue(" abc" + ListValue.SEPARATOR + " bcd"), 
				OptionFactory.createOptionValue("abc " + ListValue.SEPARATOR + "bcd").asListValue());
	}
	
	@Test
	public void testAsPairValue() {
		assertEquals(new PairValue("a"), OptionFactory.createOptionValue("a= ").asPairValue());
		assertEquals(new PairValue("a=b"), OptionFactory.createOptionValue("a=b").asPairValue());
	}
	
	@Test 
	public void testGetValue() {
		assertNull(OptionFactory.createOptionValue("").getValue(""));
		assertNull(OptionFactory.createOptionValue("abc").getValue(null));
		assertNull(OptionFactory.createOptionValue("abc=").getValue("abc"));
		assertEquals(OptionFactory.createOptionValue("test_value"), OptionFactory.createOptionValue("abc=test_value").getValue("abc"));
		
		assertEquals(OptionFactory.createOptionValue(""), OptionFactory.createOptionValue("").getValue(0));
		assertNull(OptionFactory.createOptionValue("").getValue(1));
		assertEquals(OptionFactory.createOptionValue("abc"), OptionFactory.createOptionValue("abc").getValue(0));
		assertEquals(OptionFactory.createOptionValue("abc"), OptionFactory.createOptionValue("abc;").getValue(0));
		assertEquals(OptionFactory.createOptionValue(""), OptionFactory.createOptionValue("abc;;def").getValue(1));
		assertEquals(OptionFactory.createOptionValue("def"), OptionFactory.createOptionValue("abc;;def").getValue(2));
	}
	
	@Test
	public void testEquals() {
		assertTrue(OptionFactory.createOptionValue("abc; def").equals(OptionFactory.createOptionValue("abc;def")));
	}
	
	@Test
	public void testContainsKey() {
		assertTrue(OptionFactory.createOptionValue("abc;def=ghi").containsKey("abc"));
		assertTrue(OptionFactory.createOptionValue("abc;def=ghi").containsKey("def"));
		assertFalse(OptionFactory.createOptionValue("abc;def=ghi").containsKey("xyz"));
		assertFalse(OptionFactory.createOptionValue("abc;def=ghi").containsKey(null));
	}
	
	@Test
	public void testAppend() {
		assertEquals(new ListValue("abc;def=ghi"), OptionFactory.createOptionValue("abc;def=ghi").append(null));
		assertEquals(new ListValue("abc;def=ghi"), OptionFactory.createOptionValue("abc;def=ghi").append(OptionFactory.createOptionValue("abc")));
		assertEquals(new ListValue("abc;def=ghi"), OptionFactory.createOptionValue("abc;def=ghi").append(OptionFactory.createOptionValue("def=xyz")));
		assertEquals(new ListValue("abc;def=ghi;ghi"), OptionFactory.createOptionValue("abc;def=ghi").append(OptionFactory.createOptionValue("ghi")));
		assertEquals(new ListValue("abc;def=ghi;ghi=xyz"), OptionFactory.createOptionValue("abc;def=ghi").append(OptionFactory.createOptionValue("ghi=xyz")));
	}
}

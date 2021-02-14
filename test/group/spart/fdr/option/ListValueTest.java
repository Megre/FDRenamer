package group.spart.fdr.option;

import static org.junit.Assert.*;

import org.junit.Test;

/** 
 * 
 * @author megre
 * @email renhao.x@seu.edu.cn
 * @version created on: 2021年2月1日 下午3:50:44 
 */
public class ListValueTest {

	ListValue listValue1 = new ListValue("abc;opq");
	ListValue listValue2 = new ListValue("=abc;def=test_value");
	
	@Test(expected = NullPointerException.class)
	public void testListValue() {
		new ListValue(null);
	}
	
	@Test
	public void testGetValue() {
		// getValue(int)
		assertNull(listValue1.getValue(-1));
		assertEquals(OptionFactory.createOptionValue("abc"), listValue1.getValue(0));
		assertNull(listValue1.getValue(2));
		
		// getValue(String)
		assertNull(listValue1.getValue(null));
		assertNull(listValue1.getValue("abc"));
		assertEquals(OptionFactory.createOptionValue("abc"), listValue2.getValue(""));
	}
	
	@Test
	public void testGetText() {
		// getText(int)
		assertNull(listValue1.getText(-1));
		assertEquals("abc", listValue1.getText(0));
		assertEquals("=abc", listValue2.getText(0));
		
		// getText(String)
		assertNull(listValue1.getText(null));
		assertNull(listValue1.getText("abc"));
		assertEquals("abc", listValue2.getText(""));
	}
}

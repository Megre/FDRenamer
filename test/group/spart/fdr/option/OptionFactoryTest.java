package group.spart.fdr.option;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

/** 
 * 
 * @author megre
 * @email renhao.x@seu.edu.cn
 * @version created on: 2021年2月2日 上午1:18:32 
 */
public class OptionFactoryTest {

	@Test
	public void testParse() {
		assertEquals(0, OptionFactory.parse(null).size());
		assertEquals(0, OptionFactory.parse(new String[]{}).size());
		
		assertEquals(0, OptionFactory.parse(new String[]{" -"}).size());
		assertEquals(new OptionItem("t", null), OptionFactory.parse(new String[]{" - t"}).get(0));
		assertEquals(new OptionItem("t", OptionFactory.createOptionValue("test_value")), OptionFactory.parse(new String[]{" - t ", "test_value "}).get(0));
		
		final List<OptionItem> items = OptionFactory.parse(new String[]{"-list", "abc=def;ghi", 
				"-pair", "test_name=test_value", 
				"-single", "single_value"});
		
		final OptionItem item0 = items.get(0);
		assertEquals("list", item0.getItemName());
		assertEquals("def", item0.getItemValue().getValue("abc").getRawText());
		assertNull(item0.getItemValue().getValue("ghi"));
		
		final OptionItem item1 = items.get(1);
		assertEquals("pair", item1.getItemName());
		assertEquals("test_value", item1.getItemValue().getValue("test_name").getRawText());
		
		final OptionItem item2 = items.get(2);
		assertEquals("single", item2.getItemName());
		assertEquals("single_value", item2.getItemValue().getRawText());
	}
	
	@Test(expected = NullPointerException.class)
	public void testCreateOptionValueNull() {
		OptionFactory.createOptionValue(null);
	}
	
	@Test
	public void testCreateOptionValue() {
		assertEquals(new SingleValue(""), OptionFactory.createOptionValue(""));
		assertEquals(new SingleValue("abc"), OptionFactory.createOptionValue("abc"));
		assertEquals(new ListValue("abc"), OptionFactory.createOptionValue("abc;"));
		assertEquals(new PairValue("abc=def"), OptionFactory.createOptionValue("abc=def"));
	}
	
	@Test
	public void testEquals() {
		assertTrue(OptionFactory.equals(OptionFactory.createOptionValue("abc; def"), OptionFactory.createOptionValue("abc;def")));
	}
}

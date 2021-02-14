package group.spart.fdr.option;

import org.junit.Test;
import static org.junit.Assert.*;

/** 
 * 
 * @author megre
 * @email renhao.x@seu.edu.cn
 * @version created on: 2021年2月1日 下午3:48:02 
 */
public class DefaultOptionTest {

	@Test
	public void testDefaultOption() {
		// get
		assertNull(new DefaultOption().get(null));
		assertNull(new DefaultOption().get("abc"));
		
		// set
		DefaultOption defaultOption = new DefaultOption();
		defaultOption.put("abc", "def");
		assertEquals("def", defaultOption.get("abc").getItemValue().getRawText());
		
		defaultOption.put("abc", "ghi");
		assertEquals("def;ghi", defaultOption.get("abc").getItemValue().getRawText());
	}
}

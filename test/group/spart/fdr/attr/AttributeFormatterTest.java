package group.spart.fdr.attr;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;

/** 
 * 
 * @author megre
 * @email renhao.x@seu.edu.cn
 * @version created on: 2021-02-24 18:00:53
 */
public class AttributeFormatterTest {

	@Test
	public void testAttributeFormatter() {
		assertEquals("2021-02", new AttributeFormatter("yyyy-MM").resolve(new Date(1614161012770L)));
		assertEquals("1000.00", String.format("%.2f", new AttributeFormatter("b").resolve(new FileSize(1000))));
		assertEquals("1000.00", new AttributeFormatter("%.2f").resolve(1000f));
		assertEquals("       abc", new AttributeFormatter("%10s").resolve("abc"));
	}
}

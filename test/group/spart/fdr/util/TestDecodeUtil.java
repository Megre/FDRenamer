package group.spart.fdr.util;

import static org.junit.Assert.*;

import org.junit.Test;

/** 
 * 
 * @author megre
 * @email renhao.x@seu.edu.cn
 * @version created on: 2021年2月10日 下午6:32:22 
 */
public class TestDecodeUtil {

	@Test
	public void testDecode() {
		assertEquals("abc=def;", DecodeUtil.decode("abc%3ddef%3b", "=", ";"));
	}
}

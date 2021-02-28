package group.spart.fdr.func;

import static org.junit.Assert.*;

import org.junit.Test;

/** 
 * 
 * @author megre
 * @email renhao.x@seu.edu.cn
 * @version created on: 2021-02-24 00:59:58
 */
public class SubstrFunctionTest {

	@Test
	public void testSubstrFunction() {
		assertEquals("bcd", ModifierFunctionFactory.create("substr(1)").modify("abcd", null));
		assertEquals("b", ModifierFunctionFactory.create("substr(1, 2)").modify("abcd", null));
	}
}

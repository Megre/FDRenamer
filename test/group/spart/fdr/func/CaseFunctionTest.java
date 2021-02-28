package group.spart.fdr.func;

import static org.junit.Assert.*;

import org.junit.Test;

/** 
 * 
 * @author megre
 * @email renhao.x@seu.edu.cn
 * @version created on: 2021-02-24 00:49:55
 */
public class CaseFunctionTest {

	@Test
	public void testCaseFunction() {
		assertEquals("ABC", ModifierFunctionFactory.create("case(uc)").modify("abc", null));
		assertEquals("abc", ModifierFunctionFactory.create("case(lc)").modify("ABC", null));
		assertEquals("Abc", ModifierFunctionFactory.create("case(ucfl)").modify("abc", null));
		assertEquals("aBC", ModifierFunctionFactory.create("case(lcfl)").modify("ABC", null));
		assertEquals("Abc Def", ModifierFunctionFactory.create("case(ucw)").modify("abc def", null));
		assertEquals("abc def", ModifierFunctionFactory.create("case(lcw)").modify("Abc Def", null));
		assertEquals("aBC dEF", ModifierFunctionFactory.create("case(ac)").modify("Abc Def", null));
	}
}

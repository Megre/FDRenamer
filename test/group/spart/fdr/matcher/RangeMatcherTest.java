package group.spart.fdr.matcher;

import static org.junit.Assert.*;

import org.junit.Test;

/** 
 * 
 * @author megre
 * @email renhao.x@seu.edu.cn
 * @version created on: 2021-02-23 20:36:26
 */
public class RangeMatcherTest {

	@Test
	public void testRangeMatcher() {
		assertFalse(MatcherFactory.matcher("[,2020)").matches("2020"));
		assertTrue(MatcherFactory.matcher("(,2020]").matches("2020"));
		assertFalse(MatcherFactory.matcher("[2020,)").matches("2019"));
		assertTrue(MatcherFactory.matcher("[2020,)").matches("2020"));
	}
}

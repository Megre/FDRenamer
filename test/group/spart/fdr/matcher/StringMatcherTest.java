package group.spart.fdr.matcher;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/** 
 * 
 * @author megre
 * @email renhao.x@seu.edu.cn
 * @version created on: 2021-02-24 00:21:43
 */
public class StringMatcherTest {

	@Test
	public void testStringMatcher() {
		assertFalse(MatcherFactory.matcher("abc").matches("abcd"));
		assertTrue(MatcherFactory.matcher("abc").matches("abc"));
	}
	
}

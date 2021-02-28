package group.spart.fdr.matcher;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/** 
 * 
 * @author megre
 * @email renhao.x@seu.edu.cn
 * @version created on: 2021-02-24 00:20:31
 */
public class RegexMatcherTest {

	@Test
	public void testRegexMatcher() {
		assertFalse(MatcherFactory.matcher("/a+b/").matches("abc"));
		assertTrue(MatcherFactory.matcher("/a+b/").matches("aaab"));
		assertFalse(MatcherFactory.matcher("/a+b/").matches("AAAB"));
		assertTrue(MatcherFactory.matcher("/a+b/CASE_INSENSITIVE").matches("AAAB"));
		assertFalse(MatcherFactory.matcher("/^a+b$\\n^a+b$/CASE_INSENSITIVE").matches("AAAB\nAAB"));
		assertTrue(MatcherFactory.matcher("/^a+b$\\n^a+b$/CASE_INSENSITIVE, MULTILINE").matches("AAAB\nAAB"));
	}
}

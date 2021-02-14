package group.spart.fdr.util;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.util.Date;
import org.junit.Test;

/** 
 * 
 * @author megre
 * @email renhao.x@seu.edu.cn
 * @version created on: 2021年2月2日 下午1:35:51 
 */
public class TimeUtilTest {

	@Test
	public void testTimeUtil() throws ParseException {
		final Date date = new Date(1533467816000L); // 2018-08-05 19:16:56:000
		
		assertEquals(date, TimeUtil.str2Date("Sun Aug 05 19:16:56 CST 2018"));
		assertEquals(date, TimeUtil.str2Date("2018-08-05 19:16:56"));
		assertEquals(date, TimeUtil.str2Date("2018:08:05 19:16:56"));
		assertEquals(date, TimeUtil.str2Date("2018/08/05 19:16:56"));
		assertEquals(date, TimeUtil.str2Date("08/05/2018 19:16:56"));
	}
}

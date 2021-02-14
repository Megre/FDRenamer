package group.spart.fdr.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/** 
 * 
 * @author megre
 * @email renhao.x@seu.edu.cn
 * @version created on: 2021年2月10日 下午6:20:23 
 */
public class DecodeUtil {
	private static Logger logger = LogManager.getLogger(DecodeUtil.class);

	public static String decode(String encodedString, String... specialChar) {
		StringBuffer stringBuffer = new StringBuffer(encodedString);
		for(String c: specialChar) {
			try {
				String encodedChar = URLEncoder.encode(c, "UTF-8");
				int startIdx = stringBuffer.indexOf(encodedChar.toLowerCase());
				if(startIdx < 0) startIdx = stringBuffer.indexOf(encodedChar.toUpperCase());
				if(startIdx >= 0) {
					stringBuffer.replace(startIdx, startIdx + encodedChar.length(), c);
				}
			} catch (UnsupportedEncodingException e) {
				logger.error(e);
			}
		}
		
		return stringBuffer.toString();
	}
}

package group.spart.fdr.util;

import java.nio.file.attribute.FileTime;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/** 
 * 
 * @author megre
 * @email renhao.x@seu.edu.cn
 * @version created on: Jan 27, 2021 1:30:01 PM 
 */
public class TimeUtil {
	private static Logger logger = LogManager.getLogger(TimeUtil.class);
	
	public static Date fileTime2Date(FileTime fileTime) {
		return new Date(fileTime.toMillis());
	}
	
	public static FileTime date2FileTime(Date date) {
		return FileTime.fromMillis(date.getTime());
	}
	
	public static Date str2Date(String strDate) {
		if(strDate == null) return null;
		
		SimpleDateFormat dateFormat;
		
		if(strDate.matches("\\w{3} \\w{3} \\d{2} \\d{2}:\\d{2}:\\d{2} CST \\d{4}")) {
			dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
			try {
				
				return dateFormat.parse(strDate);
			} catch (ParseException e) {
				logger.error(e);
			}
		}
		
		if(strDate.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}")) {
			dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				return dateFormat.parse(strDate);
			} catch (ParseException e) {
				logger.trace(e);
			}
		}
		
		if(strDate.matches("\\d{4}:\\d{2}:\\d{2} \\d{2}:\\d{2}:\\d{2}")) {
			dateFormat = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
			try {
				return dateFormat.parse(strDate);
			} catch (ParseException e) {
				logger.trace(e);
			}
		}
		
		if(strDate.matches("\\d{4}/\\d{2}/\\d{2} \\d{2}:\\d{2}:\\d{2}")) {
			dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			try {
				return dateFormat.parse(strDate);
			} catch (ParseException e) {
				logger.trace(e);
			}
		}
		
		if(strDate.matches("\\d{2}/\\d{2}/\\d{4} \\d{2}:\\d{2}:\\d{2}")) {
			dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
			try {
				return dateFormat.parse(strDate);
			} catch (ParseException e) {
				logger.trace(e);
			}
		}
		
		return null;
	}
}

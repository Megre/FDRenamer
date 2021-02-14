
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import group.spart.fdr.FDRFilter;
import group.spart.fdr.attr.FileAttribute;
import group.spart.fdr.attr.FileRenamer;

/** 
 * 
 * @author megre
 * @email renhao.x@seu.edu.cn
 * @version created on: Jan 27, 2021 2:18:03 PM 
 */
public class TimeStampRenamer implements FileRenamer {
 
	/**
	 * @see group.spart.fdr.attr.FileRenamer#rename(group.spart.fdr.attr.FileAttribute)
	 */
	@Override
	public String rename(FileAttribute fileAttribute, FDRFilter filter) { 
		if(fileAttribute == null) return null;
		 
		String fileName = fileAttribute.inflate("$fileName", filter); 
		 
		// WeChat export format
		Pattern regexTimeStamp = Pattern.compile(".*?(\\d{13}).*");
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
		Matcher matcher = regexTimeStamp.matcher(fileName);
		if(matcher.matches()) {
			return dateFormat.format(new Date(Long.parseLong(matcher.group(1))));
		}

		// WeChat export format
		Pattern regexLongDate = Pattern.compile(".*?(\\d{12}).*");
		matcher = regexLongDate.matcher(fileName);
		if(matcher.matches()) {
			String result = matcher.group(1);
			return new SimpleDateFormat("yyyy").format(new Date()).substring(0, 2) + result.substring(10, 12) + "-" 
				+ result.substring(8, 10);
		}
		
		return fileName;
	}

}

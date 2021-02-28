package group.spart.fdr.attr;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.IllegalFormatException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/** 
 * 
 * @author megre
 * @email renhao.x@seu.edu.cn
 * @version created on: Jan 27, 2021 5:56:17 AM 
 */
public class AttributeFormatter extends AttributeResolver {
	private static Logger logger = LogManager.getFormatterLogger(AttributeFormatter.class);
	
	private String fFormatter;
	
	public AttributeFormatter(String formatter) {
		super(formatter);
		fFormatter = decode(getResolverText());
	}
	
	/**
	 * @see group.spart.fdr.attr.AttributeResolver#resolve()
	 */
	@Override
	public Object resolve(Object valueObject) {
		return format(valueObject);
	}
	
	private Object format(Object valueObject) {
		if(valueObject == null) return null;
		
		if(fFormatter.isEmpty()) {
			return valueObject;
		}
		
		if(valueObject instanceof Date) {
			Date date = (Date) valueObject;
			try {
				SimpleDateFormat dateFormat = new SimpleDateFormat(fFormatter);
			    return dateFormat.format(date);
			} catch (IllegalArgumentException e) {
				logger.error(e);
				return null;
			}
		}
		
		if(valueObject instanceof FileSize) {
			return ((FileSize) valueObject).getSize(fFormatter);
		}
		
		if(valueObject instanceof String || valueObject instanceof Float) {
			try {
				return String.format(fFormatter, valueObject);
			}
			catch (IllegalFormatException e) {
				logger.error(e);
				return null;
			}
		}
		
		return null;
	}

}

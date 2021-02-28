package group.spart.fdr.func;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import group.spart.fdr.FDRFilter;

/** 
 * 
 * @author megre
 * @email renhao.x@seu.edu.cn
 * @version created on: 2021-02-20 23:29:11
 */
public class SubstrFunction extends ModifierFunction {

	private static Logger logger = LogManager.getFormatterLogger(SubstrFunction.class);
	
	public SubstrFunction(String name, List<String> paramList) {
		super(name, paramList);
	}
	
	/**
	 * @see group.spart.fdr.func.ModifierFunction#modify(java.lang.String, group.spart.fdr.FDRFilter)
	 */
	@Override
	public String modify(String value, FDRFilter filter) {
		if(listParams().size() < 1) return null;
		
		try {
			int begin = Integer.parseInt(listParams().get(0));
			if(listParams().size() == 1) return value.substring(begin);
			
			int end = Integer.parseInt(listParams().get(1));
			return value.substring(begin, end);
		}
		catch (NumberFormatException | IndexOutOfBoundsException e) {
			logger.error(e);
		}
		
		return null;
	}
}

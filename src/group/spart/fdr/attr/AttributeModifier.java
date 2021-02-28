package group.spart.fdr.attr;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import group.spart.fdr.func.ModifierFunction;
import group.spart.fdr.func.ModifierFunctionFactory;

/** 
 * 
 * @author megre
 * @email renhao.x@seu.edu.cn
 * @version created on: 2021-02-06 8:37:33 pm 
 */
public class AttributeModifier extends AttributeResolver {

	@SuppressWarnings("unused")
	private static Logger logger = LogManager.getFormatterLogger(AttributeModifier.class);
	
	private ModifierFunction fFunction;
	
	public AttributeModifier(String modifier) {
		super(modifier);
		
		fFunction = ModifierFunctionFactory.create(getResolverText());
	}

	/**
	 * @see group.spart.fdr.attr.AttributeResolver#resolve(java.lang.Object)
	 */
	@Override
	public Object resolve(Object valueObject) {
		return modify(valueObject.toString());
	}
	
	private String modify(String value) {
		return fFunction == null
				? null
				: fFunction.modify(value, getFilter());
	}
	
	
}

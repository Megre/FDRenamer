package group.spart.fdr;

import static org.junit.Assert.*;

import org.junit.Test;

import group.spart.fdr.option.OptionValue;

/** 
 * 
 * @author megre
 * @email renhao.x@seu.edu.cn
 * @version created on: 2021年2月1日 下午3:40:04 
 */
public class FDROptionTest {

	@Test
	public void testFDROption() {
		{
			String[] args = new String[] {
					"-filter", "fileName=.+", 
					"-process", "outputDir=."};
			FDROption fdrOption  = new FDROption(args);
			assertEquals("preview", fdrOption.getTextOptionValue("mode"));
			assertEquals("false", fdrOption.getTextOptionValue("processSubdir"));
		}
		
		{
			String[] args = new String[] {
					// global
					"-input", "testData/source",
					"-mode", "exec",
					"-processSubdir", "true", 
					// local
					"-filter", "fileName=.+", 
					"-process", "outputDir=.;action=move"};
			FDROption fdrOption = new FDROption(args);
			assertEquals("exec", fdrOption.getTextOptionValue("mode"));
			assertEquals("true", fdrOption.getTextOptionValue("processSubdir"));
			assertEquals("testData/source", fdrOption.getTextOptionValue("input"));
			
			OptionValue optionValue = fdrOption.getOptionValue("process");
			assertEquals("%fileName%", optionValue.getValue("filePathName").getRawText());
			assertEquals(".", optionValue.getValue("outputDir").getRawText());
			assertEquals("move", optionValue.getValue("action").getRawText());
			assertEquals("false", optionValue.getValue("replaceExisting").getRawText());
			assertEquals("true", optionValue.getValue("remainDirStructure").getRawText());
			assertEquals("ignore", optionValue.getValue("onInvalidParam").getRawText());
		}
		
		{
			String[] args = new String[]{"-input", "testData/source", "-config", "testData/yearMonthSorter.cfg"};
			FDROption fdrOption = new FDROption(args);
			assertEquals("testData/source", fdrOption.getTextOptionValue("input"));
			assertEquals("exec", fdrOption.getTextOptionValue("mode"));
			assertEquals("false", fdrOption.getTextOptionValue("processSubdir"));
			
			OptionValue optionValue = fdrOption.getOptionValue("process"); // the first processor
			assertEquals("${mediaCreationDate, yyyy-MM}/${fileName}", optionValue.getValue("filePathName").getRawText());
			assertEquals(".", optionValue.getValue("outputDir").getRawText());
			assertEquals("move", optionValue.getValue("action").getRawText());
			assertEquals("false", optionValue.getValue("replaceExisting").getRawText());
			assertEquals("true", optionValue.getValue("remainDirStructure").getRawText());
			assertEquals("transfer", optionValue.getValue("onInvalidParam").getRawText());

		}
	}
	
}

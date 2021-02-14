package group.spart.fdr;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import org.junit.Test;

import group.spart.fdr.test.TestUtil;

/** 
 * 
 * @author megre
 * @email renhao.x@seu.edu.cn
 * @version created on: 2021年1月30日 下午7:47:36 
 */
public class FDRenamerTest {

	final static String testDir = TestUtil.TEST_DIR;

//	@Test
	public void testFDRenamerArgs() {
		new FDRenamer(null);
		new FDRenamer(new String[]{null});
		new FDRenamer(new String[]{null, null});
		new FDRenamer(new String[]{""});
		new FDRenamer(new String[]{"", ""});
		new FDRenamer(new String[]{"-"});
		new FDRenamer(new String[]{"-", "-"});
		new FDRenamer(new String[]{"-filter"});
		new FDRenamer(new String[]{"-filter", "-filter"});
		new FDRenamer(new String[]{"-filter", "-process"});
		new FDRenamer(new String[]{"-filter", "", "-process"});
		new FDRenamer(new String[]{"-filter", "", "-process", ""});
		new FDRenamer(new String[]{"-process"});
		new FDRenamer(new String[]{"-input"});
		new FDRenamer(new String[]{"-input", "testData/source", "-filter", "", "-process", ""});
		new FDRenamer(new String[]{"-mode"});
	}
	
	@Test
	public void testFDRenamer() throws IOException, InterruptedException {
		// yearMonthSorter
		{
			initDir();
			String[] args = new String[] {"-input", testDir + "/source", 
					"-config", testDir + "/yearMonthSorter.cfg"};
			joinThread(new FDRenamer(args));
			
			for(File file: new File(args[1]).listFiles()) {
				assertTrue(file.isDirectory());
			}
			assertTrue(new File(args[1] + "/2018-08/mmexport1533467816513.jpg").exists());
			assertTrue(new File(args[1] + "/2021-02/mmexport1533467816512.jpg").exists());
		}
		
		// fileNameSorter
		{
			initDir();
			String[] args = new String[]{"-input", testDir + "/source", 
					"-config", testDir + "/fileNameSorter.cfg"};
			joinThread(new FDRenamer(args));
			
			for(File file: new File(args[1]).listFiles()) {
				assertTrue(file.isDirectory());
			}
			assertTrue(new File(args[1] + "/2018-08/mmexport1533467816513.jpg").exists());
			assertTrue(new File(args[1] + "/2018-08/mmexport1533467816512.jpg").exists());
			assertTrue(new File(args[1] + "/2021-02/2021-02-07.gif").exists());
		}
		
		// -processSubdir true
		{
			initDir();
			String[] args = new String[]{"-input", testDir + "/source", 
					"-processSubdir", "true",
					"-config", testDir + "/yearMonthSorter.cfg"};
			joinThread(new FDRenamer(args));
			
			for(File file: new File(args[1]).listFiles()) {
				assertTrue(file.isDirectory());
			}
			assertTrue(new File(args[1] + "/2018-08/mmexport1533467816513.jpg").exists());
			assertTrue(new File(args[1] + "/2021-02/mmexport1533467816512.jpg").exists());
			assertTrue(new File(args[1] + "/directory0/2021-02/121301190818c8f0c7010755.mp4").exists());
			assertFalse(new File(args[1] + "/directory0/121301190818c8f0c7010755.mp4").exists());
		}
		
		// input directory is the same with output directory
		{
			initDir();
			String[] args = new String[]{"-mode", "exec", 
					"-input", testDir + "/source", 
					"-filter", "fileName=.+(jpg|jpeg|mp4|gif)",
					"-process", "action=move; outputDir=" + (testDir + "/source") 
								+ "; replaceExisting=true; onInvalidParam=transfer; filePathName=%fileName%;",
					};
			joinThread(new FDRenamer(args));
			
			assertTrue(new File(args[3] + "/mmexport1533467816512.jpg").exists());
			assertTrue(new File(args[3] + "/mmexport1533467816513.jpg").exists());
		}
		
		// output directory is the subdirectory of the input directory (remainDirStructure=true)
		{
			initDir();
			String[] args = new String[]{"-mode", "exec", 
					"-input", testDir + "/source", 
					"-processSubdir", "true",
					"-filter", "$fileName=/.+(jpg|jpeg|mp4|gif)/",
					"-process", "action=move; outputDir=" + (testDir + "/source/sub") 
								+ "; onInvalidParam=transfer; filePathName=$fileName;",
					};
			joinThread(new FDRenamer(args));
			
			assertTrue(new File(args[3] + "/mmexport1533467816512.jpg").exists());
			assertTrue(new File(args[3] + "/mmexport1533467816513.jpg").exists());
			assertTrue(new File(args[3] + "/directory0/121301190818c8f0c7010755.mp4").exists());
			assertFalse(new File(args[3] + "/sub/mmexport1533467816512.jpg").exists());
			assertFalse(new File(args[3] + "/sub/mmexport1533467816513.jpg").exists());
			assertFalse(new File(args[3] + "/sub/directory0/121301190818c8f0c7010755.mp4").exists());
			assertTrue(new File(args[3] + "/sub/sub/mmexport1533467816512.jpg").exists());
			assertTrue(new File(args[3] + "/sub/sub/mmexport1533467816513.jpg").exists());
			assertTrue(new File(args[3] + "/sub/sub/directory0/121301190818c8f0c7010755.mp4").exists());
		}
		
		// output directory is the subdirectory of the input directory (remainDirStructure false)
		{
			initDir();
			String[] args = new String[]{"-mode", "exec", 
					"-input", testDir + "/source", 
					"-processSubdir", "true",
					"-filter", "$fileName=/.+(jpg|jpeg|mp4|gif)/",
					"-process", "action=move; outputDir=" + (testDir + "/source/sub") 
								+ "; remainDirStructure=false"
								+ "; onInvalidParam=transfer; filePathName=$fileName;",
					};
			joinThread(new FDRenamer(args));
			
			assertTrue(new File(args[3] + "/mmexport1533467816512.jpg").exists());
			assertTrue(new File(args[3] + "/mmexport1533467816513.jpg").exists());
			assertFalse(new File(args[3] + "/directory0/121301190818c8f0c7010755.mp4").exists());
			assertTrue(new File(args[3] + "/sub/121301190818c8f0c7010755.mp4").exists());
			assertTrue(new File(args[3] + "/sub/mmexport1533467816512.jpg").exists());
			assertTrue(new File(args[3] + "/sub/mmexport1533467816513.jpg").exists());
			assertTrue(new File(args[3] + "/sub/directory0/121301190818c8f0c7010755.mp4").exists());
		}
		
		
	}
	
	private void joinThread(FDRenamer renamer) {
		while(renamer.getFilters() != null && renamer.getFilters().size() > 0) {
			if(renamer.getFilters().get(renamer.getFilters().size()-1).isStopped()) break;
		}
	}
	
	private void initDir() throws IOException {
		TestUtil.init();
	}
	

}

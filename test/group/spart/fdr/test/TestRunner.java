package group.spart.fdr.test;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

/** 
 * 
 * @author megre
 * @email renhao.x@seu.edu.cn
 * @version created on: 2021年1月30日 下午1:15:22 
 */
public class TestRunner {

	public static void main(String[] args) throws ClassNotFoundException {
		
		Result result = JUnitCore.runClasses(Class.forName("TestTimeStampRenamer"));
		
	    for (Failure failure : result.getFailures()) {
	       System.out.println(failure.toString());
	    }
	    System.out.println(result.wasSuccessful());
	}
}

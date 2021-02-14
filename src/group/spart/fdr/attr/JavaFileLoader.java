package group.spart.fdr.attr;

import java.io.File;
import java.io.FileWriter;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.Charset;
import java.util.concurrent.ConcurrentHashMap;

import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

/** 
 * 
 * @author megre
 * @email renhao.x@seu.edu.cn
 * @version created on: Jan 27, 2021 2:00:23 PM 
 */
public class JavaFileLoader {

	public static Logger logger = LogManager.getLogger(JavaFileLoader.class);
	
	public static ConcurrentHashMap<String, Class<?>> fLoadedClasses = new ConcurrentHashMap<>();
	
	public static Class<?> load(String javaFilePath) {
		if(fLoadedClasses.get(javaFilePath) != null) return fLoadedClasses.get(javaFilePath);
		
        File javaFile = new File(javaFilePath);
        if(!javaFile.isFile()) return null;
        
        File classFile = new File(getClassPath(javaFile));
		try {
			FileWriter fileWriter = new FileWriter(classFile);
			fileWriter.flush();
	        fileWriter.close();

	        // compile
	        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
	        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, Charset.forName("UTF-8"));
	        Iterable<? extends JavaFileObject> compilationUnits  = fileManager.getJavaFileObjects(javaFile);
	        CompilationTask task = compiler.getTask(fileWriter, fileManager, null, null, null, compilationUnits);
	        boolean compilationSuccess = task.call();
	        fileManager.close();
	        
	        if(!compilationSuccess) {
	        	logger.error("compilation failed: " + javaFile.getAbsolutePath());
	        	return null;
	        }

	        // load class
	        URLClassLoader classLoader = new URLClassLoader(new URL[] {classFile.getParentFile().toURI().toURL()});
	        Class<?> clazz = classLoader.loadClass(getClassName(javaFile));
	        classLoader.close();
	        
	        fLoadedClasses.put(javaFilePath, clazz);
	        
	        return clazz;
	        
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		} 
        
		return null;
	}
	
	private static String getClassPath(File javaFile) {
		return javaFile.getParentFile().getAbsolutePath() + "/" + getClassName(javaFile) + ".class";
	}
	
	private static String getClassName(File javaFile) {
		int index = javaFile.getName().indexOf('.');
		if(index < 0) return javaFile.getName();
		
		return javaFile.getName().substring(0, index);
	}
	
}

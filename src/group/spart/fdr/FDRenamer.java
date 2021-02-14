package group.spart.fdr;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import group.spart.fdr.attr.FileAttribute;
import group.spart.fdr.option.ListValue;
import group.spart.fdr.option.OptionItem;
import group.spart.fdr.option.OptionValue;

/** 
 * 
 * @author megre
 * @email renhao.x@seu.edu.cn
 * @version created on: Jan 26, 2021 8:18:17 PM 
 */
public class FDRenamer {

	private static Logger logger = LogManager.getLogger(FDRenamer.class);
	
	private static FDROption fOption;
	private static final String fUsage =  "Usage: \n"
			+ "-input <path1> \n"
			+ "       [;<path2>]... \n"
			+ "-mode [preview | exec] \n"
			+ "-processSubdir [true | false] \n"
			+ "-filter [fileName=<regex1>] \n"
			+ "        [;filePath=<regex2>]...] \n"
			+ "-process [action=[move | copy]] \n"
			+ "         [;remainDirStructure=[true | false]] \n"
			+ "         [;onInvalidParam=[ignore | transfer]] \n"
			+ "         [;replaceExisting=[true | false]] \n"
			+ "         [;outputDir=<output_directory>] \n"
			+ "         [;filePathName=<file_path_and_name>] \n"
			+ "         [;renamer=<java_file_path>] \n"
			+ "-config <configuration_file_path> \n\n"
			+ "https://github.com/megre/FDRenamer";
	
	private List<FDRFilter> fFilters;
	
	public static void main(String[] args) {
		new FDRenamer(args);
	}
	
	public FDRenamer(String[] args) {
		if((fOption = buildFDROption(args)) == null) {
			printUsage();
			return;
		}
		
		fFilters = connectFilters(buildFilters(fOption));
		if(fFilters.size() == 0) {
			logger.error("no filter was specified");
			return;
		}
		
		try {
			processInput(fOption);
		} catch (Exception e) {
			logger.error(e);
		} finally {
			getSourceFilter().closeInput();
		}
	}
	
	public List<FDRFilter> getFilters() {
		return fFilters;
	}
	
	private FDRFilter getSourceFilter() {
		return fFilters.get(0);
	}
	
	private FDROption buildFDROption(String[] args) {
		if(args == null || args.length < 1 || args[0] == null) return null;
		
		if(args.length == 1 && new File(args[0]).isFile()) { // -config <configuration_file_path>
			return new FDROption(new String[]{"-config", args[0]});
		}
		
		return new FDROption(args);
	}
	
	private void printUsage() {
		System.out.println(fUsage);
	}
	
	private void processInput(FDROption option) {
		final ExecutorService executor = Executors.newFixedThreadPool(fFilters.size());
		for(FDRFilter filter: fFilters) {
			filter.startProcess(executor);
		}
		
		FDRFilter sourceFilter = getSourceFilter();
		Queue<File> files = browseInputFiles(option);
		while(!files.isEmpty()) {
			File curFile = files.remove();
			logger.trace("input file: " + curFile.getAbsolutePath());
			sourceFilter.input(new FileAttribute(curFile));
		}
	}
	
	private Queue<File> browseInputFiles(FDROption option) {
		final Queue<File> fileQueue = new LinkedList<>();
		final OptionValue inputOptionValue = option.getOptionValue("input");
		final boolean processSubdir = "true".equals(option.getTextOptionValue("processSubdir"));
		if(inputOptionValue == null) {
			logger.error("-input option was not specified");
			return fileQueue;
		}
		
		ListValue inputs = inputOptionValue.asListValue();	
		for(OptionValue value: inputs.getValues()) {
			File file = new File(value.getRawText());
			if(!file.exists()) {
				logger.warn(file.getAbsolutePath() + " does not exist");
				continue;
			}
			
			if(file.isFile()) {
				fileQueue.add(file);
			}
			else if(file.isDirectory()) { 
				for(File subFile: file.listFiles()) { // files in the directory (depth one)
					includeFile(subFile, fileQueue, processSubdir);
				}
			}
		}
		
		if(fileQueue.isEmpty()) {
			logger.warn("input is empty: " + inputOptionValue.getRawText());
		}
		
		return fileQueue;
	}
	
	private void includeFile(File file, Queue<File> fileQueue, boolean processSubdir) {
		if(file.isFile()) fileQueue.add(file);
		else if(file.isDirectory() && processSubdir) {
			includeDir(fileQueue, file);
		}
	}
	
	private void includeDir(Queue<File> fileQueue, File dir) {
		for(File file: dir.listFiles()) {
			if(file.isFile()) fileQueue.add(file);
			else if(file.isDirectory()) includeDir(fileQueue, file);
		}
	}
	
	private List<FDRFilter> buildFilters(FDROption option) {
		List<OptionItem> optionItems = option.getOptionItems();
		List<FDRFilter> filters = new ArrayList<>();
		
		int index = 0;
		for(OptionItem item: optionItems) {
			
			if("filter".equals(item.getItemName()) && item.getItemValue() != null) {
				filters.add(new FDRFilter(item.getItemValue(), option));
				filters.get(filters.size() - 1).setPosition(index++);
				continue;
			}
			
			if("process".equals(item.getItemName()) && item.getItemValue() != null) {
				if(filters.size() > 0) {
					filters.get(filters.size()-1).setProcessor(new FDRProcessor(item.getItemValue(), option));
					continue;
				}
				
				logger.error("The processor loses its filter: " + item.getItemValue());
			}
		}
		
		return filters;
	}
	
	private List<FDRFilter> connectFilters(List<FDRFilter> filters) {
		if(filters.size() < 1) return filters;
		
		FDRFilter source = filters.get(0), sink = null;
		for(int idx=0; idx<filters.size()-1; ++idx) {
			sink = filters.get(idx+1);
			
			new FDRPipe(source, sink);
			source = sink;
		}
		
		return filters;
	}
	
}

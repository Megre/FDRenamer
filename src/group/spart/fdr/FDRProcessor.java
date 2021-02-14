package group.spart.fdr;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import group.spart.fdr.attr.FileAttribute;
import group.spart.fdr.option.ListValue;
import group.spart.fdr.option.OptionValue;

/** 
 * 
 * @author megre
 * @email renhao.x@seu.edu.cn
 * @version created on: Jan 26, 2021 7:25:10 PM 
 */
public class FDRProcessor {

	private Logger logger = LogManager.getLogger(FDRProcessor.class);
	
	private OptionValue fProcessOption;
	private FDROption fGlobalOption;
	private FDRFilter fFilter;
	
	public FDRProcessor(OptionValue processoption, FDROption globalOption) {
		fProcessOption = processoption;
		fGlobalOption = globalOption;
	}
	
	/**
	 * @param fileWrapper
	 * @return true to consume, false to deliver to next filter
	 */
	public boolean process(FileAttribute fileWrapper) {
		final String action = getLocalOption("action");
		final String onInvalidParam = getLocalOption("onInvalidParam");
		final boolean consumeOnInvalidParam = "ignore".equals(onInvalidParam);
		final String outputPath = decideOutputPath(fileWrapper);
		
		if(outputPath == null) {
			logger.warn("[filter " + fFilter.getPosition() + "] [" + action + "] [" 
					+ onInvalidParam + " " + fileWrapper.getFile().getAbsolutePath() + "] on invalid outputDir: " + getLocalOption("outputDir"));
			return consumeOnInvalidParam;
		}
		
		final String filePathName = decideFilePathName(fileWrapper);
		if(filePathName == null) {
			logger.warn("[filter " + fFilter.getPosition() + "] [" + action + "] [" 
					+ onInvalidParam + " " + fileWrapper.getFile().getAbsolutePath() + "] on invalid filePathName: " + getLocalOption("filePathName"));
			return consumeOnInvalidParam;
		}
		
		final File outputFile = new File(outputPath + "/" + filePathName);
		final boolean replaceExisting = "true".equals(getLocalOption("replaceExisting"));
		
		if(!replaceExisting && outputFile.exists()) {
			logger.info("[filter " + fFilter.getPosition() + "] [" + action + " ignored on existing target] " + fileWrapper.getFile().getAbsolutePath() 
					+ " -> " + outputFile.getAbsolutePath());
			return true;
		}
		
		if(!"exec".equals(getGlobalOption("mode"))) {
			logger.info("[filter " + fFilter.getPosition() + "] ["
					+ action + "] [mode preview]: " + fileWrapper.getFile().getAbsolutePath() + " -> " + outputFile.getAbsolutePath());
			return true;
		}
		
		performAction(action, fileWrapper, outputFile);
		return true;
	}
	
	public void setFilter(FDRFilter filter) {
		fFilter = filter;
	}
	
	public OptionValue getProcessOption() {
		return fProcessOption;
	}
	
	private String decideOutputPath(FileAttribute fileWrapper) {
		String outputPath = getOutputDir(fileWrapper);
		if(outputPath == null) return null; 
		
		if(outputPath.startsWith(".")) {
			outputPath = fileWrapper.getFile().getParentFile().getAbsolutePath() 
					+ outputPath.substring(1);
		} 
		else { // dir -> dir
			outputPath = getRelativePath(fileWrapper, outputPath);
		}
		
		return outputPath;
	}
	
	private String decideFilePathName(FileAttribute fileAttribute) {
		return fileAttribute.inflate(getFilePathName(), fFilter);
	}
	
	/**
	 * If an input is a directory, return a path to remain a file's directory structure
	 * 		relative to output path
	 * @param fileWrapper
	 * @param outputPath
	 * @return
	 */
	private String getRelativePath(FileAttribute fileWrapper, String outputPath) {
		if(!"true".equals(getLocalOption("remainDirStructure"))) return outputPath;
		
		ListValue listValue = fGlobalOption.getOptionValue("input").asListValue();
		for(OptionValue value: listValue.getValues()) { // list input directory
			final File inputFile = new File(value.getRawText());
			if(!inputFile.isDirectory()) continue;
			
			final String inputDir = inputFile.getAbsolutePath();
			final String curFilePath = fileWrapper.getFile().getParentFile().getAbsolutePath();
			if(curFilePath.startsWith(inputDir)) { // current file is in the input directory
				return new File(outputPath).getAbsolutePath() + curFilePath.replace(inputDir, "");
			}
		}
		
		return outputPath;
	}
	
	private boolean performAction(String action, FileAttribute fileWrapper, File outputFile) {
		boolean success = false;
		
		if(action.equals("copy")) {
			success = copy(fileWrapper, outputFile);
		}
		else if(action.equals("move")) {
			success = move(fileWrapper, outputFile);
		}
		
		if(success) {
			logger.info("[filter " + fFilter.getPosition() + "] ["
					+ action + " succeeded] " + fileWrapper.getFile().getAbsolutePath() + " -> " + outputFile.getAbsolutePath());
		} 
		else {
			logger.warn("[filter " + fFilter.getPosition() + "] ["
					+ action + " failed] " + fileWrapper.getFile().getAbsolutePath() + " -> " + outputFile.getAbsolutePath());
		}
		
		return success;
	}
	
	private boolean move(FileAttribute fileWrapper, File outputFile) {
		try {
			createNewFile(outputFile);
			Files.move(fileWrapper.getFile().toPath(), outputFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
			return true;
		} catch (IOException e) {
			logger.error("[filter " + fFilter.getPosition() + "] [move failed] " 
					+ fileWrapper.getFile().getAbsolutePath() + " -> " + outputFile.getAbsolutePath()
					+ ": " + e.toString());
		}
		return false;
	}
	
	private boolean copy(FileAttribute fileWrapper, File outputFile) {
		BufferedOutputStream bos = null;
		try {
			createNewFile(outputFile);
			bos = new BufferedOutputStream(new FileOutputStream(outputFile));
			Files.copy(fileWrapper.getFile().toPath(), bos);
			return true;
		} catch (IOException e) {
			logger.error("[filter " + fFilter.getPosition() + "] [copy failed] " 
					+ fileWrapper.getFile().getAbsolutePath() + " -> " + outputFile.getAbsolutePath()
					+ ": " + e.toString());
		} finally {
			if(bos != null) {
				try {
					bos.close();
				} catch (IOException e) { }
			}
		}
		
		return false;
	}
	
	private void createNewFile(File outputFile) throws IOException {
		if(!outputFile.exists()) {
			outputFile.getParentFile().mkdirs();
			outputFile.createNewFile();
		}
	}
	
	private String getGlobalOption(String optionName) {
		return fGlobalOption.getTextOptionValue(optionName);
	}
	
	private String getLocalOption(String optionName) {
		return fProcessOption.asListValue().getText(optionName);
	}
	
	private String getOutputDir(FileAttribute fileWrapper) {
		return fileWrapper.inflate(getLocalOption("outputDir"), fFilter);
	}
	
	private String getFilePathName() {
		return getLocalOption("filePathName");
	}
	
}

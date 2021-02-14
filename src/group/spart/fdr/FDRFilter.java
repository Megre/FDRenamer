package group.spart.fdr;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import group.spart.fdr.attr.FileAttribute;
import group.spart.fdr.option.ListValue;
import group.spart.fdr.option.OptionValue;

/** 
 * 
 * @author megre
 * @email renhao.x@seu.edu.cn
 * @version created on: Jan 26, 2021 5:40:04 PM 
 */
public class FDRFilter {
	private Logger logger = LogManager.getLogger(FDRFilter.class);
	
	private FDRPipe fSourcePipe, fDestPipe;
	
	private OptionValue fFilterOption;
	@SuppressWarnings("unused")
	private FDROption fGlobalOption;
	private Queue<FileAttribute> fCachedFileWrappers;
	private FDRProcessor fProcessor;
	private volatile boolean fInputClosed = false;
	private volatile boolean fStoped = false;
	private int fPosition;
	private List<InflatedFilterOption> fInflatedOptionList;
	
	public FDRFilter(OptionValue filterOption, FDROption globalOption) {
		fFilterOption = filterOption;
		fGlobalOption = globalOption;
		fCachedFileWrappers = new ConcurrentLinkedQueue<>();
		fInflatedOptionList = new ArrayList<>();
	}
	
	public void input(FileAttribute fileWrapper) {
		try {
			fCachedFileWrappers.add(fileWrapper);
		}
		catch (Exception e) {
			logger.error(e);
		}
	}
	
	public void output(FileAttribute fileWrapper) {
		if(fDestPipe != null) {
			fDestPipe.push(fileWrapper);
		}
	}
	
	public synchronized void closeInput() {
		fInputClosed = true;
	}
	
	public synchronized boolean isStopped() {
		return fStoped;
	}
	
	public void setProcessor(FDRProcessor processor) {
		fProcessor = processor;
		if(processor != null) {
			processor.setFilter(this);
		}
	}
	
	public FDRProcessor getProcessor() {
		return fProcessor;
	}
	
	public void setPosition(int position) {
		fPosition = position;
	}
	
	public int getPosition() {
		return fPosition;
	}
	
	public void startProcess(ExecutorService executorService) {
		if(fProcessor == null) {
			logger.error("no processor was specified for the filter: " + toString());
			return;
		}
		
		executorService.submit(() -> {
			while(true) {
				try {
					if(!hasNextFile()) break;
					
					FileAttribute fileWrapper = nextFile();
					if(fileWrapper == null) continue; // wait for next input
					
					if(isFileMatched(fileWrapper) && fProcessor.process(fileWrapper)) { // process matched file
						continue;
					}
					
					output(fileWrapper); // transfer to next filter
				}  catch (Exception e) {
					logger.error(e);
				}
			}
			
			if(getDestPipe() != null) {
				getDestPipe().close();
			} 
			else { // the last filter
				shutDown(executorService);
			}
			
			stop();
		});
	}
	
	public FDRPipe getSourcePipe() {
		return fSourcePipe;
	}

	public void setSourcePipe(FDRPipe fSourcePipe) {
		this.fSourcePipe = fSourcePipe;
	}

	public FDRPipe getDestPipe() {
		return fDestPipe;
	}

	public void setDestPipe(FDRPipe fDestPipe) {
		this.fDestPipe = fDestPipe;
	}
	
	public OptionValue getFilterOption() {
		return fFilterOption;
	}
	
	public List<InflatedFilterOption> listInflatedOptions() {
		return fInflatedOptionList;
	}
	
	@Override
	public String toString() {
		return fFilterOption.toString();
	}
	
	private boolean isFileMatched(FileAttribute fileAttribute) {
		try {
			fInflatedOptionList.clear();
			ListValue listValue = fFilterOption.asListValue();
			for(OptionValue value: listValue.getValues()) {
				final InflatedFilterOption inflatedOption = new InflatedFilterOption(fileAttribute, this, value);
				
				fInflatedOptionList.add(inflatedOption);
				
				if(!inflatedOption.matches()) return false;
			}
		} catch (Exception e) {
			logger.error(e);
			return false;
		}
		
		return true;
	}
	
	private boolean hasNextFile() {
		return fCachedFileWrappers.size() > 0 || !isInputClosed();
	}
	
	private FileAttribute nextFile() {
		if(fCachedFileWrappers.size() > 0) return fCachedFileWrappers.remove();
		
		try {
			Thread.sleep(20);
		} catch (InterruptedException e) {
			logger.error(e);
			closeInput();
		}
		
		return null;
	}
	
	private synchronized void stop() {
		fStoped = true;
	}
	
	private synchronized boolean isInputClosed() {
		return fInputClosed;
	}
	
	private void shutDown(ExecutorService executorService) {
		executorService.shutdown();
		try {
			if(!executorService.awaitTermination(5, TimeUnit.MILLISECONDS)){ 
				executorService.shutdownNow(); 
			}
		} catch (InterruptedException | SecurityException e) {
			logger.warn(e);
		} 
	}

	
	
}

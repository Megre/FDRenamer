package group.spart.fdr;

import group.spart.fdr.attr.FileAttribute;

/** 
 * 
 * @author megre
 * @email renhao.x@seu.edu.cn
 * @version created on: Jan 26, 2021 5:41:24 PM 
 */
public class FDRPipe {
	private FDRFilter fSource, fSink;
	
	public FDRPipe(FDRFilter source, FDRFilter sink) {
		fSource = source;
		fSink = sink;
		
		if(source != null) {
			source.setDestPipe(this);
		}
		
		if(sink != null) {
			sink.setSourcePipe(this);
		}
	}

	public void push(FileAttribute fileWrapper) {
		if(fSink != null) {
			fSink.input(fileWrapper);
		}
	}
	
	public void close() {
		if(fSink != null) fSink.closeInput();
	}
	
	public FDRFilter getSource() {
		return fSource;
	}

	public FDRFilter getSink() {
		return fSink;
	}
	
}

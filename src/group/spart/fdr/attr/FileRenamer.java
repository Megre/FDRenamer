package group.spart.fdr.attr;

import group.spart.fdr.FDRFilter;

/** 
 * 
 * @author megre
 * @email renhao.x@seu.edu.cn
 * @version created on: Jan 27, 2021 1:52:24 PM 
 */
public interface FileRenamer {

	String rename(FileAttribute fileWrapper, FDRFilter filter);
}

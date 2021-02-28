package group.spart.fdr.attr;

/** 
 * 
 * @author megre
 * @email renhao.x@seu.edu.cn
 * @version created on: 2021-02-18 6:10:59 PM
 */
public class FileSize {

	private long fBytes;
	
	public FileSize(long bytes) {
		fBytes = bytes;
	}
	
	public float getSize(String unit) {
		if(unit == null) return -1;
		
		if(unit.equalsIgnoreCase("bit")) {
			return fBytes * 8f;
		}
		
		if(unit.equalsIgnoreCase("b")) {
			return fBytes;
		}
		
		if(unit.equalsIgnoreCase("kb")) {
			return fBytes / 1024f;
		}
		
		if(unit.equalsIgnoreCase("mb")) {
			return fBytes / (1024f * 1024f);
		}
		
		if(unit.equalsIgnoreCase("gb")) {
			return fBytes / (1024f * 1024f * 1024);
		}
		
		if(unit.equalsIgnoreCase("tb")) {
			return fBytes / (1024f * 1024f * 1024 * 1024);
		}
		
		return -1f;
	}
	
	@Override
	public String toString() {
		return String.valueOf(getSize("b"));
	}
}

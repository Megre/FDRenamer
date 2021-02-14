package group.spart.fdr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

/** 
 * 
 * @author megre
 * @email renhao.x@seu.edu.cn
 * @version created on: Jan 26, 2021 7:46:32 PM 
 */
public class MultipleMap<K, V> {

	private HashMap<K, List<V>> fMap;
	
	public MultipleMap() {
		fMap = new HashMap<>();
	}
	
	public V getFirst(Object key) {
		if(!fMap.containsKey(key)) return null;
		
		return fMap.get(key).get(0);
	}
	
	public List<V> getList(Object key) {
		return (List<V>) fMap.get(key);
	}
	
	public int put(K key, V value) {
		List<V> list = null;
		if(fMap.containsKey(key)) {
			list = fMap.get(key);
		}
		else {
			list = new ArrayList<V>();
			fMap.put(key, list);
		}
		
		list.add(value);
		return list.size() - 1;
	}
	
	@Override
	public String toString() {
		StringBuffer stringBuffer = new StringBuffer("{");
		for(Entry<K, List<V>> entry: fMap.entrySet()) {
			stringBuffer.append(entry.getKey()).append(" = ").append(entry.getValue()).append(", ");
		}
		stringBuffer.append("}");
		return stringBuffer.toString();
	}
	
}

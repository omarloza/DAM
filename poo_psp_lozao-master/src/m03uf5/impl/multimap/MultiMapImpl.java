package m03uf5.impl.multimap;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import m03uf5.prob.multimap.MultiMap;
import m03uf5.prob.multimap.MyString;

public abstract class MultiMapImpl<K, V> implements MultiMap<K, V> {

	protected Map<K, Collection<V>> mm2;


	
	private Collection<V> c;

	public MultiMapImpl() {
		mm2 = new HashMap<K, Collection<V>>();
	
	}

	public abstract Collection<V> crearCol();

	@Override
	public Iterator<K> iterator() {
		
		return mm2.keySet().iterator();
	}

	@Override
	public int size() {

		return mm2.size();
	}

	@Override
	public void put(K key, V value) {

	  Collection<V> col=mm2.get(key);
	  if(col==null) {
		  col=crearCol();
		  mm2.put(key, col);
	  }
		col.add(value);
		mm2.put(key, col);
	
	}

	@Override
	public Collection<V> get(K key) {

		  Collection<V> col=mm2.get(key);
		  if(col==null) {
			  col=crearCol();
			  mm2.put(key, col);
		  }
		return mm2.get(key);
	}

	@Override
	public boolean remove(K key, V value) {
		
		

		return mm2.get(key).remove(value);
	}



}

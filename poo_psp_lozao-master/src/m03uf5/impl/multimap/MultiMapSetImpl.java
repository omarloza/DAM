package m03uf5.impl.multimap;

import java.util.Collection;
import java.util.HashSet;

import m03uf5.prob.multimap.MyString;

public class MultiMapSetImpl<K,V> extends MultiMapImpl<K,V> {
	Collection<V> c;
	@Override
	public Collection<V> crearCol() {
	return c = new HashSet<V>();
		
	}

}

package m03uf5.impl.multimap;

import java.util.ArrayList;
import java.util.Collection;

import m03uf5.prob.multimap.MyString;

public class MultiMapListImpl<K,V> extends MultiMapImpl<K,V> {
	Collection<V> c;
	


	@Override
	
	public Collection<V> crearCol() {
		 return c = new ArrayList<V>();
		
	}

}

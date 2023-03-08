package m03uf5.impl.multimap;

import m03uf5.prob.multimap.MyString;
import m03uf5.prob.multimap.MyStringFactory;

public class MyStringFactoryImpl implements MyStringFactory{

	@Override
	public MyString create(String value) {

		return new MyStringImpl(value);
	}

	
}

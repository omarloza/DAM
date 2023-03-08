package m03uf5.impl.multimap;

import m03uf5.prob.multimap.MyString;

public class MyStringImpl implements MyString{

	private String value;
	
	
	public MyStringImpl(String value) {
		this.value = value;
	}

	@Override
	public String get() {

		return value;
	}

	@Override
	public void set(String value) {
		this.value = value;
		
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MyStringImpl other = (MyStringImpl) obj;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}
	
	

	@Override
	public String toString() {
		return value ;
	}

	
	
	
}

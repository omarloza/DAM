package m03uf5.impl.mascotes.run;

import m03uf5.prob.mascotes.Llar;
import m03uf5.prob.mascotes.Xip;

public class XipImpl implements Xip {

	private Long id;
	private Llar llar;

	public XipImpl(Long id, Llar llar) {
		this.id = id;
		this.llar = llar;
	}

	@Override
	public Long getId() {

		return id;
	}

	@Override
	public Llar getLlar() {

		return llar;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setLlar(Llar llar) {
		this.llar = llar;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((llar == null) ? 0 : llar.hashCode());
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
		XipImpl other = (XipImpl) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (llar == null) {
			if (other.llar != null)
				return false;
		} else if (!llar.equals(other.llar))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return id + " a " + llar;
	}

}

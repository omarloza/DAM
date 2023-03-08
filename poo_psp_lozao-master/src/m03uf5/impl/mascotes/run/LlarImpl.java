package m03uf5.impl.mascotes.run;

import m03uf5.prob.mascotes.Llar;

public class LlarImpl implements Llar {
	private String nom;

	public LlarImpl(String nom) {
		super();
		this.nom = nom;
	}

	@Override
	public String getNom() {
		return nom;
	}

	@Override
	public String toString() {
		return nom;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nom == null) ? 0 : nom.hashCode());
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
		LlarImpl other = (LlarImpl) obj;
		if (nom == null) {
			if (other.nom != null)
				return false;
		} else if (!nom.equals(other.nom))
			return false;
		return true;
	}

	
	
}

package m03uf5.impl.exam1b;

import m03uf5.prob.exam1b.Proveidor;

public class ProveidorImpl<T> implements Proveidor<T> {
	private T producte;
	private int estoc;

	public ProveidorImpl(T producte, int estoc) {
		super();
		this.producte = producte;
		this.estoc = estoc;
	}

	@Override
	public T getProducte() {

		return producte;
	}

	@Override
	public int getEstoc() {

		return estoc;
	}

	@Override
	public void produir(int quantitat) {
		estoc = estoc + quantitat;

	}

	@Override
	public int subministrar(int quantitat) {

		if (estoc < quantitat) {
			return 0;
		}
		estoc = estoc - quantitat;

		return estoc;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + estoc;
		result = prime * result + ((producte == null) ? 0 : producte.hashCode());
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
		ProveidorImpl other = (ProveidorImpl) obj;
		if (estoc != other.estoc)
			return false;
		if (producte == null) {
			if (other.producte != null)
				return false;
		} else if (!producte.equals(other.producte))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ProveidorImpl [producte=" + producte + ", estoc=" + estoc + "]";
	}
	
	

}

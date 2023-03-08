package m03uf5.impl.mascotes.run;

import m03uf5.prob.mascotes.Especie;
import m03uf5.prob.mascotes.Mascota;
import m03uf5.prob.mascotes.Xip;

public class MascotaImpl implements Mascota {

	private Xip xip;
	private String nom;
	private Especie especie;
	private int any;

	public MascotaImpl(Xip xip, String nom, int any, Especie especie) {
		this.xip = xip;
		this.nom = nom;
		this.any = any;
		this.especie = especie;
	}

	@Override
	public int compareTo(Mascota m) {

		return this.any - m.getAny();
	}

	@Override
	public Xip getXip() {

		return xip;
	}

	@Override
	public String getNom() {

		return nom;
	}

	@Override
	public int getAny() {

		return any;
	}

	@Override
	public Especie getEspecie() {

		return especie;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + any;
		result = prime * result + ((especie == null) ? 0 : especie.hashCode());
		result = prime * result + ((nom == null) ? 0 : nom.hashCode());
		result = prime * result + ((xip == null) ? 0 : xip.hashCode());
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
		MascotaImpl other = (MascotaImpl) obj;
		if (any != other.any)
			return false;
		if (especie != other.especie)
			return false;
		if (nom == null) {
			if (other.nom != null)
				return false;
		} else if (!nom.equals(other.nom))
			return false;
		if (xip == null) {
			if (other.xip != null)
				return false;
		} else if (!xip.equals(other.xip))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return nom + "(" + any + ") és un " + especie + " amb xip " + xip;
	}

}

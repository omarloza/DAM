package m03uf5.impl.mascotes.run;

import java.util.LinkedList;
import java.util.Queue;

import m03uf5.prob.mascotes.Mascota;
import m03uf5.prob.mascotes.Veterinari;

public class VeterinariImpl implements Veterinari {

	private String nom;
	private Queue<Mascota> cua;

	public VeterinariImpl(String nom) {
		this.nom = nom;
		this.cua = new LinkedList<>();
	}

	@Override
	public String getNom() {
		return nom;
	}

	@Override
	public int pendents() {
		return cua.size();
	}

	@Override
	public void visita(Mascota mascota) {
		cua.offer(mascota);
	}

	@Override
	public Mascota atendre() {
		return cua.poll();
	}

}

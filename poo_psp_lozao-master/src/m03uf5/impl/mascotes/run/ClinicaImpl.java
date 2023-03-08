package m03uf5.impl.mascotes.run;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import m03uf5.prob.mascotes.Clinica;
import m03uf5.prob.mascotes.Especie;
import m03uf5.prob.mascotes.Llar;
import m03uf5.prob.mascotes.Mascota;
import m03uf5.prob.mascotes.Veterinari;
import m03uf5.prob.mascotes.Xip;

public class ClinicaImpl implements Clinica {

	private Long IdNuevo;
	private Map<Long, Mascota> mascotes;

	public ClinicaImpl() {
		IdNuevo = 0L;
		mascotes = new HashMap<>();
	}

	@Override
	public Mascota trobaMascota(Long chipId) {
		return mascotes.get(chipId);
	}

	@Override
	public Llar creaLlar(String nom) {

		Llar llar = new LlarImpl(nom);

		return llar;
	}

	@Override
	public Veterinari creaVeterinari(String nom) {

		Veterinari vet = new VeterinariImpl(nom);

		return vet;
	}

	@Override
	public Mascota registraGos(String nom, int any, Llar llar) {
		Mascota gos = registraNuevo(Especie.GOS, nom, any, llar);
		return gos;
	}

	@Override
	public Mascota registraGat(String nom, int any, Llar llar) {
		Mascota gat = registraNuevo(Especie.GAT, nom, any, llar);
		return gat;
	}

	private Mascota registraNuevo(Especie especie, String nom, int any, Llar llar) {
		Long id = ++IdNuevo;
		Xip xip = new XipImpl(id, llar);
		Mascota m = new MascotaImpl(xip, nom, any, especie);
		mascotes.put(id, m);
		return m;
	}

	@Override
	public List<Mascota> llistaMascotes(Llar llar) {

		List<Mascota> result = new ArrayList<>();

		for (Mascota mascota : mascotes.values()) {
			
			if(llar.equals(mascota.getXip().getLlar())) {
				
				result.add(mascota);
			}
			
			
		}

		return result;
	}

}
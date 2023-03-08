package m03uf5.impl.mascotes.run;

import java.util.Collections;
import java.util.List;

import lib.Problems;
import lib.Reflections;
import m03uf5.prob.mascotes.Clinica;
import m03uf5.prob.mascotes.Llar;
import m03uf5.prob.mascotes.Mascota;
import m03uf5.prob.mascotes.Veterinari;

public class ClinicaRun {

	/*
	 * sortida esperada:
	 * 
	 * ====> localitzat: Tom(2016) és un GAT amb chip 4 a Casa meva ====> mascotes
	 * per any: [Garfield(2015) és un GAT amb chip 1 a Casa meva, Tom(2016) és un
	 * GAT amb chip 4 a Casa meva, Silvestre(2017) és un GAT amb chip 3 a Casa
	 * meva] ====> veterinari: atenent Doraemon(2013) és un GAT amb chip 2 a Refugi
	 * atenent Silvestre(2017) és un GAT amb chip 3 a Casa meva atenent
	 * Garfield(2015) és un GAT amb chip 1 a Casa meva
	 *
	 */

	public static void main(String[] args) {

		// demana una classe m03uf5.impl.mascotes.ClinicaImpl que implementi Clinica

		String packageName = Problems.getImplPackage(Clinica.class);
		Clinica clinica = new ClinicaImpl();

		run(clinica);
	}

	public static void run(Clinica clinica) {

		Llar h1 = clinica.creaLlar("Refugi");
		Llar h2 = clinica.creaLlar("Casa meva");

		Mascota garfield = clinica.registraGat("Garfield", 2015, h2);
		Mascota doraemon = clinica.registraGat("Doraemon", 2013, h1);
		Mascota silvestre = clinica.registraGat("Silvestre", 2017, h2);
		Mascota tom = clinica.registraGat("Tom", 2016, h2);

		Mascota tomTrobat = clinica.trobaMascota(tom.getXip().getId());
		System.out.println("====> localitzat:\n" + tomTrobat);

		Llar h3 = clinica.creaLlar("Casa meva");
		List<Mascota> mascotes = clinica.llistaMascotes(h3);
		Collections.sort(mascotes);
		System.out.println("====> mascotes per any:\n" + mascotes);

		Veterinari v1 = clinica.creaVeterinari("Vete");

		v1.visita(doraemon);
		v1.visita(silvestre);
		v1.visita(garfield);

		System.out.println("====> veterinari:");

		Mascota pacient;
		while ((pacient = v1.atendre()) != null) {
			System.out.println("atenent " + pacient);
		}
	}
}

package m03uf5.impl.arbreg.run;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import m03uf5.prob.arbreg.Parella;
import m03uf5.prob.arbreg.Persona;
import m03uf5.prob.arbreg.Sexe;

public class PersonaImpl implements Persona {

	private String nom;
	private Sexe sexe;
	private Parella<Persona, Persona> pares;
	private Collection<Persona> fills = new ArrayList<Persona>();

	public PersonaImpl(String nom, Sexe sexe, Parella<Persona, Persona> pares) {
		super();
		this.nom = nom;
		this.sexe = sexe;
		this.pares = pares;

	}

	

	public PersonaImpl(String nom2, Sexe sexe2, Parella<Persona, Persona> pares2, List<Persona> fills) {
		super();
		this.nom = nom2;
		this.sexe = sexe2;
		this.pares = pares2;
		this.fills=fills;
	}

	@Override
	public String getNom() {

		return nom;
	}

	@Override
	public Sexe getSexe() {

		return sexe;
	}

	@Override

	public Parella<Persona, Persona> getPares() {
		
	
		return pares;
	}

	@Override
	public Collection<Persona> getFills() {
		
		
	
		return fills;
	}
	



	@Override
	public String toString() {
		return " " + nom +"";
	}

}

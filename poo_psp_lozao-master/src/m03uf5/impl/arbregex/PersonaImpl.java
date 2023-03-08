package m03uf5.impl.arbregex;

import java.util.Collection;
import java.util.HashSet;

import m03uf5.prob.arbreg.Parella;
import m03uf5.prob.arbreg.Persona;
import m03uf5.prob.arbreg.Sexe;

public class PersonaImpl implements Persona {

	private String nom;
	private Sexe sexe;
	private Parella<Persona, Persona> pares;
	private Collection<Persona> fills;
	
	public PersonaImpl(String nom, Sexe sexe, Parella<Persona, Persona> pares) {
		this.nom = nom;
		this.sexe = sexe;
		this.pares = pares;
		this.fills = new HashSet<Persona>();
		
		Persona pare = pares.getEsquerra();
		if (pare != null) {
			pare.getFills().add(this);
		}
		Persona mare = pares.getDreta();
		if (mare != null) {
			mare.getFills().add(this);
		}
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
		return nom;
	}
}

package m03uf5.impl.arbreg.run;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import m03uf5.prob.arbreg.ArbreGen;
import m03uf5.prob.arbreg.Parella;
import m03uf5.prob.arbreg.Persona;
import m03uf5.prob.arbreg.Sexe;

public class ArbreGenImpl implements ArbreGen {

	private List<Persona> personas = new ArrayList<>();

	private List<Persona> hijos = new ArrayList<>();
	
	private Map<Persona,Parella> m = new HashMap();
	Parella<Persona, Persona> pareja;

	@Override
	public Iterator<Persona> iterator() {

		return this.personas.iterator();

	}

	@Override
	public Parella<Persona, Persona> createPares(Persona pare, Persona mare) {

		pareja = new ParellaImpl<Persona, Persona>(pare, mare);

		return pareja;
	}

	@Override
	public Persona addPersona(String nom, Sexe sexe, Parella<Persona, Persona> pares) {

		return addPersonaConHijos(nom, sexe, pares, hijos);
	}

	public Persona addPersonaConHijos(String nom, Sexe sexe, Parella<Persona, Persona> pares, List<Persona> fills2) {
		Persona nueva = new PersonaImpl(nom, sexe, pares, fills2);
		personas.add(nueva);
		m.put(nueva, pareja);
		if (nueva.getPares().getEsquerra() != null && nueva.getPares().getDreta() != null) {
			
			
			addHijo(nueva);
			for(Persona s:hijos) {
				
				if(m.containsValue(s.getPares().getEsquerra().getNom())||m.containsValue(s.getPares().getDreta().getNom())) {
					addHijo(nueva);
					
				}
				
				
				
				}
			}
		
			System.out.println(m);
			return nueva;
			
		}


	
	public List<Persona> addHijo(Persona persona) {
		
		hijos.add(persona);
		return hijos;

	}

	@Override
	public Persona get(String nom) {

		for (Persona s : personas) {
			if (s.getNom().equals(nom)) {
				return s;
			}

		}

		return null;
	}

	@Override
	public Collection<Persona> getParelles(Persona p) {

		return null;
	}

	@Override
	public Collection<Persona> getFillsEnComu(Persona pare, Persona mare) {

		return null;
	}

}

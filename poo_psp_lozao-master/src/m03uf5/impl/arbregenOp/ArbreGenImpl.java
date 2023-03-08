package m03uf5.impl.arbregenOp;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import m03uf5.prob.arbreg.ArbreGen;
import m03uf5.prob.arbreg.Parella;
import m03uf5.prob.arbreg.Persona;
import m03uf5.prob.arbreg.Sexe;
import m03uf5.prob.multimap.MultiMap;

public class ArbreGenImpl implements ArbreGen,MultiMapArbre {

	private Map<String, Persona> persones;

	public ArbreGenImpl() {
		persones = new LinkedHashMap<>(); // keep insert order
	}
	
	@Override
	public Iterator<Persona> iterator() {
		return persones.values().iterator();
	}

	@Override
	public Parella<Persona, Persona> createPares(Persona pare, Persona mare) {
		return new ParellaImpl<Persona, Persona>(pare, mare);
	}

	@Override
	public Persona addPersona(String nom, Sexe sexe, Parella<Persona, Persona> pares) {		
		Persona p = new PersonaImpl(nom, sexe, pares);
		persones.put(nom, p);
		return p;
	}

	@Override
	public Persona get(String nom) {
		return persones.get(nom);
	}

	@Override
	public String toString() {
		return persones.values().toString();
	}
	
	@Override
	public Collection<Persona> getParelles(Persona p) {
		
		Set<Persona> result = new HashSet<>();	
		Collection<Persona> fills = p.getFills();
		for (Persona fill: fills) {
			Parella<Persona, Persona> pares = fill.getPares();
			Persona pare = pares.getEsquerra();
			if (pare != null && !p.equals(pare)) {
				result.add(pare);
			}
			Persona mare = pares.getDreta();
			if (mare != null && !p.equals(mare)) {
				result.add(mare);
			}
		}		
		return result;
	}

	@Override
	public Collection<Persona> getFillsEnComu(Persona pare, Persona mare) {

		Collection<Persona> fillsPare = pare.getFills();
		Collection<Persona> fillsMare = mare.getFills();
		
		Set<Persona> fills = new HashSet<>();
		for (Persona p: fillsPare) {
			if (fillsMare.contains(p)) {
				fills.add(p);
			}
		}
		
		return fills;
	}

	@Override
	public MultiMap<Integer, Persona> getDescendents(Persona p) {

		return null;
	}

	@Override
	public MultiMap<Integer, Persona> getAvantpassats(Persona p) {

		return null;
	}

	@Override
	public String getParentiuDeRespecte(Persona p1, Persona p2) {
	
		return null;
	}	
}

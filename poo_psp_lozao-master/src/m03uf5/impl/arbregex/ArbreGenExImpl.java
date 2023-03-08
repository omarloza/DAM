package m03uf5.impl.arbregex;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.omg.PortableInterceptor.ORBInitInfoPackage.DuplicateName;

import lib.db.DuplicateException;
import lib.db.NotFoundException;
import m03uf5.prob.arbreg.Parella;
import m03uf5.prob.arbreg.Persona;
import m03uf5.prob.arbreg.Sexe;
import m03uf5.prob.arbregex.ArbreGenEx;

public class ArbreGenExImpl implements ArbreGenEx{
	private Map<String, Persona> persones;
	
	
	
	public ArbreGenExImpl() {
		persones = new LinkedHashMap<>();
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
	public Persona addPersona(String nom, Sexe sexe, Parella<Persona, Persona> pares) throws DuplicateException {
	
		if(persones.containsKey(nom)) {
			if(nom==null||sexe==null||pares==null) {
				
				throw new IllegalArgumentException(nom+","+sexe+","+pares);
			}
		  throw new DuplicateException(nom);
		  
		  
		}else {
			
			if(nom==null||sexe==null||pares==null) {
				
				throw new IllegalArgumentException(nom+","+sexe+","+pares);
			}
			
			Persona p = new PersonaImpl(nom, sexe, pares);
			persones.put(nom, p);
			return p;
		}
	

	}

	@Override
	public Persona get(String nom) throws NotFoundException {
		if(persones.get(nom)==null) {
			 throw new NotFoundException(nom);
		}else {
			return persones.get(nom);
		}
		}

		

	@Override
	public Collection<Persona> getParelles(Persona p) {
		
		if(p==null) {
			throw new IllegalArgumentException();
		}else {
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
		}
		
		

	@Override
	public Collection<Persona> getFillsEnComu(Persona pare, Persona mare) {
	
		
		if(pare==null||mare==null) {
			throw new IllegalArgumentException(pare+","+mare);

		}else {
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
		}


	@Override
	public String toString() {
		return "" + persones + "";
	}
	

}

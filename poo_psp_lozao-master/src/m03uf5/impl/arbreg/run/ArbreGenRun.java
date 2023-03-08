package m03uf5.impl.arbreg.run;

import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import lib.LoggingConfigurator;
import lib.Problems;
import lib.Reflections;
import m03uf5.prob.arbreg.ArbreGen;
import m03uf5.prob.arbreg.Parella;
import m03uf5.prob.arbreg.Persona;
import m03uf5.prob.arbreg.Sexe;

public class ArbreGenRun {
	
	static final Logger LOGGER = Logger.getLogger(ArbreGenRun.class.getName());

	public static void main(String[] args) {
		
		// mostra missatges de nivell INFO
		LoggingConfigurator.configure(Level.INFO);
		
		String packageName = Problems.getImplPackage(ArbreGen.class);		
		ArbreGen ag = new ArbreGenImpl();
		
		testFamilia(ag);
	}
	
	public static void testFamilia(ArbreGen ag) {
		
		Parella<Persona, Persona> paresDesconeguts = ag.createPares(null, null);
		
		Persona joan = ag.addPersona("Joan", Sexe.M, paresDesconeguts);
		Persona maria = ag.addPersona("Maria", Sexe.F, paresDesconeguts);
		Persona lluna = ag.addPersona("Lluna", Sexe.M, ag.createPares(joan, maria));
		Persona pere = ag.addPersona("Pere", Sexe.M, ag.createPares(joan, maria));
		Persona amalia = ag.addPersona("Amalia", Sexe.F, paresDesconeguts);
		Persona lluis = ag.addPersona("Lluis", Sexe.M, paresDesconeguts);		
		Persona rosa = ag.addPersona("Rosa", Sexe.F, ag.createPares(joan, amalia));
		Persona roger = ag.addPersona("Roger", Sexe.M, ag.createPares(lluis, amalia));
		
		for (Persona p: ag) {					
			String nom = p.getNom();
			
			LOGGER.info("pares de " + p.getNom() + ": " + p.getPares());
			
			Collection<Persona> fills = p.getFills();
			if (!fills.isEmpty()) {
				LOGGER.info("fills de " + nom + ": " + fills);
			}
			
			Collection<Persona> parelles = ag.getParelles(p);
			if (!parelles.isEmpty()) {
				LOGGER.info("parelles de " + nom + ": " + parelles);
			}
		}
		
		LOGGER.info("fills en comu de joan i amalia: " + ag.getFillsEnComu(joan, amalia));
		LOGGER.info("fills en comu de joan i maria: " + ag.getFillsEnComu(joan, maria));
	}
}

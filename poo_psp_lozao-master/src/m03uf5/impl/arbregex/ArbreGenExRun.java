package m03uf5.impl.arbregex;

import java.util.logging.Level;
import java.util.logging.Logger;

import lib.LoggingConfigurator;
import lib.Problems;
import lib.Reflections;
import lib.db.DuplicateException;
import lib.db.NotFoundException;
import m03uf5.prob.arbreg.Parella;
import m03uf5.prob.arbreg.Persona;
import m03uf5.prob.arbreg.Sexe;
import m03uf5.prob.arbregex.ArbreGenEx;

public class ArbreGenExRun {
	
	static final Logger LOGGER = Logger.getLogger(ArbreGenExRun.class.getName());

	public static void main(String[] args) {
		
		// mostra missatges de nivell INFO
		LoggingConfigurator.configure(Level.INFO);
		
		String packageName = Problems.getImplPackage(ArbreGenEx.class);		
		ArbreGenEx ag = Reflections.newInstanceOfType(ArbreGenEx.class, packageName + ".ArbreGenExImpl");
		
		testFamilia(ag);
	}
	
	public static void testFamilia(ArbreGenEx ag) {
		
		Parella<Persona, Persona> paresDesconeguts = ag.createPares(null, null);
		
		try {
			Persona joan = ag.addPersona("Joan", Sexe.M, paresDesconeguts);
			Persona maria = ag.addPersona("Maria", Sexe.F, paresDesconeguts);
			Persona lluna = ag.addPersona("Lluna", Sexe.M, ag.createPares(joan, maria));
			Persona pere = ag.addPersona("Pere", Sexe.M, ag.createPares(joan, maria));
			Persona amalia = ag.addPersona("Amalia", Sexe.F, paresDesconeguts);
			Persona lluis = ag.addPersona("Lluis", Sexe.M, paresDesconeguts);		
			Persona rosa = ag.addPersona("Rosa", Sexe.F, ag.createPares(joan, amalia));
			Persona roger = ag.addPersona("Roger", Sexe.M, ag.createPares(lluis, amalia));
			
		} catch (DuplicateException e) {
			LOGGER.log(Level.SEVERE, "no hauria d'executar-se", e);
		}
		
		try {
			ag.addPersona("Joan", Sexe.M, paresDesconeguts);
	
			LOGGER.severe("no hauria d'executar-se");
			
		} catch (DuplicateException e) {
			LOGGER.log(Level.INFO, e.getMessage());
		}
		
		try {
			ag.get("Joana");
			LOGGER.severe("no hauria d'executar-se");
			
		} catch (NotFoundException e) {
			LOGGER.log(Level.INFO, e.getMessage());
		}	
		
		try {
			ag.addPersona(null, Sexe.M, paresDesconeguts);
			LOGGER.severe("no hauria d'executar-se");
			
		} catch (DuplicateException e) {
			LOGGER.log(Level.SEVERE, "no hauria d'executar-se", e);
		} catch (IllegalArgumentException e) {
			LOGGER.log(Level.INFO, e.getMessage());
		}
		
		try {
			ag.addPersona("Joan", null, paresDesconeguts);
			LOGGER.severe("no hauria d'executar-se");
			
		} catch (DuplicateException e) {
			LOGGER.log(Level.SEVERE, "no hauria d'executar-se", e);
		} catch (IllegalArgumentException e) {
			LOGGER.log(Level.INFO, e.getMessage());
		}
		
		try {
			ag.addPersona("Joan", Sexe.M, null);
			LOGGER.severe("no hauria d'executar-se");
			
		} catch (DuplicateException e) {
			LOGGER.log(Level.SEVERE, "no hauria d'executar-se", e);
		} catch (IllegalArgumentException e) {
			LOGGER.log(Level.INFO, e.getMessage());
		}
		
		try {
			ag.getParelles(null);
			LOGGER.severe("no hauria d'executar-se");
			
		} catch (IllegalArgumentException e) {
			LOGGER.log(Level.INFO, e.getMessage());
		}		

		try {
			ag.getFillsEnComu(null, null);
			LOGGER.severe("no hauria d'executar-se");
			
		} catch (IllegalArgumentException e) {
			LOGGER.log(Level.INFO, e.getMessage());
		}
	}
}

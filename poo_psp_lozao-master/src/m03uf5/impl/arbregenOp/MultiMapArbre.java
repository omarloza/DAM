package m03uf5.impl.arbregenOp;

import m03uf5.prob.arbreg.Persona;
import m03uf5.prob.multimap.MultiMap;

public interface MultiMapArbre {
	
//	On Integer és el nivell a partir de la persona de parentesc:
//
//		Per a descendents, 1 és fill, 2 és net, etc.
//		Per a avantpassats, 1 és pare, 2 és avi, etc.

	MultiMap<Integer, Persona> getDescendents(Persona p);
	MultiMap<Integer, Persona> getAvantpassats(Persona p);
	
//	que permeti anomenar el parentiu per a persones amb sang comuna (no parentescs polítics). O sigui: què és p1 respecte de p2? Retornar null, si no hi ha cap, o un String amb el nom del parentesc (fill, net, cosí, etc.) amb el sexe correcte (és fill o filla?).
//
//			Et caldrà recòrrer els arbres a partir d'una persona. Una pista: hi ha dues formes de recòrrer un arbre:
//
//			Breadth First Search
//			Depth First Search
//			Opcionalment, implementa el parentiu polític.	
	
	String getParentiuDeRespecte(Persona p1, Persona p2);
	
}

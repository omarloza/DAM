package m03uf5.impl.arbregenOp;

import m03uf5.prob.arbreg.Persona;
import m03uf5.prob.multimap.MultiMap;

public interface MultiMapArbre {
	
//	On Integer �s el nivell a partir de la persona de parentesc:
//
//		Per a descendents, 1 �s fill, 2 �s net, etc.
//		Per a avantpassats, 1 �s pare, 2 �s avi, etc.

	MultiMap<Integer, Persona> getDescendents(Persona p);
	MultiMap<Integer, Persona> getAvantpassats(Persona p);
	
//	que permeti anomenar el parentiu per a persones amb sang comuna (no parentescs pol�tics). O sigui: qu� �s p1 respecte de p2? Retornar null, si no hi ha cap, o un String amb el nom del parentesc (fill, net, cos�, etc.) amb el sexe correcte (�s fill o filla?).
//
//			Et caldr� rec�rrer els arbres a partir d'una persona. Una pista: hi ha dues formes de rec�rrer un arbre:
//
//			Breadth First Search
//			Depth First Search
//			Opcionalment, implementa el parentiu pol�tic.	
	
	String getParentiuDeRespecte(Persona p1, Persona p2);
	
}

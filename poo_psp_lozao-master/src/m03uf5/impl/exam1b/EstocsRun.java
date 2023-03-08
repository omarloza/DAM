package m03uf5.impl.exam1b;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import lib.LoggingConfigurator;
import lib.Problems;
import lib.Reflections;
import m03uf5.prob.exam1b.Comanda;
import m03uf5.prob.exam1b.Combustible;
import m03uf5.prob.exam1b.Establiment;
import m03uf5.prob.exam1b.Proveidor;
import m03uf5.prob.exam1b.ProveidorFactory;

/* SORTIDA:
 * comandes: [Comanda [quantitat=300, producte=DIESEL], Comanda [quantitat=400, producte=BENZINA95], Comanda [quantitat=500, producte=BENZINA98], Comanda [quantitat=600, producte=BENZINA95], Comanda [quantitat=100, producte=DIESELPLUS]] 
 * pendents: [Comanda [quantitat=600, producte=BENZINA95], Comanda [quantitat=100, producte=DIESELPLUS]] 
 * endents2: [Comanda [quantitat=100, producte=DIESELPLUS]] 
 * establiment: DIESEL: 300, BENZINA95: 1000, BENZINA98: 500, DIESELPLUS: 0 
 * proveidors: DIESEL: 50, BENZINA95: 150, BENZINA98: 200 
 */	

public class EstocsRun {
		
	static final Logger LOGGER = Logger.getLogger(EstocsRun.class.getName());

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		
		LoggingConfigurator.configure(Level.INFO);
		
		String packageName = Problems.getImplPackage(Establiment.class);	
		
		Establiment<Combustible> est = Reflections.newInstanceOfType(
				Establiment.class, packageName + ".EstablimentImpl");		
		
		ProveidorFactory<Combustible> pf = Reflections.newInstanceOfType(
				ProveidorFactory.class, packageName + ".ProveidorFactoryImpl");
		
		test(est, pf);
	}

	private static void test(Establiment<Combustible> est, ProveidorFactory<Combustible> pf) {
				
		// crear proveidors
		
		Proveidor<Combustible> proDiesel = pf.create(Combustible.DIESEL, 50);
		Proveidor<Combustible> pro95 = pf.create(Combustible.BENZINA95, 800);
		Proveidor<Combustible> pro98 = pf.create(Combustible.BENZINA98, 700);
		
		// associar els proveidors a l'establiment
		
		est.setProveidor(proDiesel);
		est.setProveidor(pro95);
		est.setProveidor(pro98);
		
		// crear llista de comandes
		
		List<Comanda<Combustible>> comandes = new ArrayList<>();
		
		comandes.add(new Comanda<Combustible>(Combustible.DIESEL, 300));		
		comandes.add(new Comanda<Combustible>(Combustible.BENZINA95, 400));
		comandes.add(new Comanda<Combustible>(Combustible.BENZINA98, 500));
		comandes.add(new Comanda<Combustible>(Combustible.BENZINA95, 600));
		comandes.add(new Comanda<Combustible>(Combustible.DIESELPLUS, 100));
		
		LOGGER.info("comandes: " + comandes);

		// produir estoc addicional de diesel
		
		proDiesel.produir(300);
		
		// processar comandes (es retornen les que no s'han pogut processar)
		
		List<Comanda<Combustible>> pendents = est.processar(comandes);
		LOGGER.info("pendents: " + pendents);
		
		// produir estoc addicional de 95, una de les comandes en necessita
		
		pro95.produir(350);
		
		// processar comandes (ha de permetre processar la comanda de 95)
		
		List<Comanda<Combustible>> pendents2 = est.processar(pendents);
		LOGGER.info("pendents2: " + pendents2);
		
		// mostra estocs de l'establiment i dels proveidors
		
		LOGGER.info("establiment: " 
				+ Combustible.DIESEL + ": " + est.getEstoc(Combustible.DIESEL) + ", "
				+ Combustible.BENZINA95 + ": " + est.getEstoc(Combustible.BENZINA95) + ", "
				+ Combustible.BENZINA98 + ": " + est.getEstoc(Combustible.BENZINA98) + ", " 
				+ Combustible.DIESELPLUS + ": " + est.getEstoc(Combustible.DIESELPLUS));
		
		LOGGER.info("proveidors: " 
				+ proDiesel.getProducte() + ": " + proDiesel.getEstoc() + ", " 
				+ pro95.getProducte() + ": " + pro95.getEstoc() + ", "
				+ pro98.getProducte() + ": " + pro98.getEstoc());
	}
}

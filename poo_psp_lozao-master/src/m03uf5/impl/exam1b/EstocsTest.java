package m03uf5.impl.exam1b;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import lib.Problems;
import lib.Reflections;
import m03uf5.prob.exam1b.Comanda;
import m03uf5.prob.exam1b.Combustible;
import m03uf5.prob.exam1b.Establiment;
import m03uf5.prob.exam1b.Proveidor;
import m03uf5.prob.exam1b.ProveidorFactory;

public class EstocsTest {

	private Establiment<Combustible> est;
	private ProveidorFactory<Combustible> pf;
	
	@SuppressWarnings("unchecked")
	public EstocsTest() {
			
		String packageName = Problems.getImplPackage(Establiment.class);	
		
		est = Reflections.newInstanceOfType(
				Establiment.class, packageName + ".EstablimentImpl");		
		
		pf = Reflections.newInstanceOfType(
				ProveidorFactory.class, packageName + ".ProveidorFactoryImpl");
	}

	@Test
	public void test() {
		
		Proveidor<Combustible> proDiesel = pf.create(Combustible.DIESEL, 50);
		assertEquals(50, proDiesel.getEstoc());
		assertEquals(Combustible.DIESEL, proDiesel.getProducte());
		
		Proveidor<Combustible> pro95 = pf.create(Combustible.BENZINA95, 800);
		assertEquals(800, pro95.getEstoc());
		assertEquals(Combustible.BENZINA95, pro95.getProducte());
		
		Proveidor<Combustible> pro98 = pf.create(Combustible.BENZINA98, 700);
		assertEquals(700, pro98.getEstoc());
		assertEquals(Combustible.BENZINA98, pro98.getProducte());
		
		est.setProveidor(proDiesel);
		assertEquals(proDiesel, est.getProveidor(Combustible.DIESEL));
		
		est.setProveidor(pro95);
		assertEquals(pro95, est.getProveidor(Combustible.BENZINA95));
		
		est.setProveidor(pro98);
		assertEquals(pro98, est.getProveidor(Combustible.BENZINA98));
		
		List<Comanda<Combustible>> comandes = new ArrayList<>();
		
		comandes.add(new Comanda<Combustible>(Combustible.DIESEL, 300));		
		comandes.add(new Comanda<Combustible>(Combustible.BENZINA95, 400));
		comandes.add(new Comanda<Combustible>(Combustible.BENZINA98, 500));
		comandes.add(new Comanda<Combustible>(Combustible.BENZINA95, 600));
		comandes.add(new Comanda<Combustible>(Combustible.DIESELPLUS, 100));
		
		proDiesel.produir(300);
		assertEquals(350, proDiesel.getEstoc());
		
		List<Comanda<Combustible>> pendents = est.processar(comandes);
		assertEquals(2, pendents.size());

		assertEquals(pendents.get(0), comandes.get(3));
		assertEquals(pendents.get(1), comandes.get(4));
		
		pro95.produir(350);
		assertEquals(750, pro95.getEstoc());
		
		List<Comanda<Combustible>> pendents2 = est.processar(pendents);
		assertEquals(1, pendents2.size());
		
		assertEquals(pendents2.get(0), comandes.get(4));
		
		assertEquals(300, est.getEstoc(Combustible.DIESEL));
		assertEquals(1000, est.getEstoc(Combustible.BENZINA95));
		assertEquals(500, est.getEstoc(Combustible.BENZINA98));		
		assertEquals(0, est.getEstoc(Combustible.DIESELPLUS));
		
		assertEquals(50, proDiesel.getEstoc());
		assertEquals(150, pro95.getEstoc());
		assertEquals(200, pro98.getEstoc());
	}
}

package m09uf2.impl.votacio;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import lib.LoggingConfigurator;
import lib.Problems;
import lib.Reflections;
import m09uf2.prob.votacio.LocalElectoral;

public class VotacioTest {
	
	static final Logger LOGGER = Logger.getLogger(VotacioTest.class.getName());

	static final int NUMVOTANTS = 2000;
	static final int NUMMESES = 4;
	static final int NUMCANDIDATS = 5;
	static final long SEED = 237458237656324L;
	
	@BeforeAll
	static void beforeAll() {
		
		LoggingConfigurator.configure(Level.INFO);
	}

	private VotCalc calc;
	private LocalElectoral le;
	
	@BeforeEach
	void beforeEach() {		
		
		calc = new VotCalc(2);
		
		String packageName = Problems.getImplPackage(LocalElectoral.class);
		le = Reflections.newInstanceOfType(
				LocalElectoral.class, packageName + ".LocalElectoralImpl",
				new Class[] {VotCalc.class, int.class, int.class}, 
				new Object[] {calc, NUMMESES, NUMCANDIDATS});
	}

	@Test
	void votar() {
		
		Random random = new Random(SEED);
		
		Thread[] threads = new Thread[NUMVOTANTS];
		for (int i=0; i<NUMVOTANTS; i++) {				
			Votant votant = new Votant(i, random.nextInt(NUMCANDIDATS), le);				
			threads[i] = new Thread(votant);
			threads[i].start();
		}

		// esperar fils acaben
		
		for (int i=0; i<NUMVOTANTS; i++) {
			try {
				threads[i].join();
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}
		
		int[] resultats = le.resultats();				
		assertNotNull(resultats);
		assertEquals(NUMCANDIDATS, resultats.length);
		
		assertEquals(403, resultats[0]);
		assertEquals(388, resultats[1]);
		assertEquals(416, resultats[2]);
		assertEquals(407, resultats[3]);
		assertEquals(386, resultats[4]);
		
		int vots = 0;
		for (int i=0; i<NUMCANDIDATS; i++) {
			vots += resultats[i];
		}
		
		assertEquals(NUMVOTANTS, vots);
		assertEquals(0, le.presents());
	}
}

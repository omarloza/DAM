package m09uf2.impl.votacio;

import java.util.Arrays;
import java.util.Random;
import java.util.function.Consumer;
import java.util.logging.Logger;

import lib.LoggingConfigurator;
import lib.Problems;
import lib.Reflections;
import m09uf2.prob.votacio.LocalElectoral;

public class VotacioRun {
	
	static final Logger LOGGER = Logger.getLogger(VotacioRun.class.getName());
	
	static final int NUMVOTANTS = 2000;
	static final int NUMMESES = 4;
	static final int NUMCANDIDATS = 5;
	static final long SEED = 237458237656324L;
	
	public static void main(String[] args) {
		
		LoggingConfigurator.configure();
		
		long incMillis = 2;
		
		testSingleThread(incMillis);
		testMultipleThread(incMillis);
	}

	private static LocalElectoral newInstance(VotCalc votcalc, int numMeses, int numCandidats) {
		
		String packageName = Problems.getImplPackage(LocalElectoral.class);
		return Reflections.newInstanceOfType(
				LocalElectoral.class, packageName + ".LocalElectoralImpl", 
				new Class[] {VotCalc.class, int.class, int.class}, 
				new Object[] {votcalc, numMeses, numCandidats});
	}
	
	static void testSingleThread(long incMillis) {
		
		LOGGER.info("single thread");

		votar(incMillis, le -> {
			
			Random random = new Random(SEED);
			
			for (int i=0; i<NUMVOTANTS; i++) {
				le.entrada();
				int mesa = le.findMesa(i);
				le.votar(i, mesa, random.nextInt(NUMCANDIDATS));
				le.sortida();
			}
		});
	}
	
	static void testMultipleThread(long incMillis) {
			
		LOGGER.info("multiple thread");
		
		votar(incMillis, le -> {
			
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
		});
	}
	
	static void votar(long incMillis, Consumer<LocalElectoral> votacio) {
		
		VotCalc votcalc = new VotCalc(incMillis);
		LocalElectoral le = newInstance(votcalc, NUMMESES, NUMCANDIDATS);
		
		votacio.accept(le);
		
		int[] resultats = le.resultats();		
		LOGGER.info(Arrays.toString(resultats));
		
		int vots = 0;
		for (int i=0; i<NUMCANDIDATS; i++) {
			vots += resultats[i];
		}
		LOGGER.info("han votat " + vots);
		LOGGER.info("queden al local " + le.presents());
	}
}

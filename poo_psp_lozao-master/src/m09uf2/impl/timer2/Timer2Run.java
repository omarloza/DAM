package m09uf2.impl.timer2;

import java.util.logging.Level;
import java.util.logging.Logger;

import lib.LoggingConfigurator;
import lib.Problems;
import lib.Reflections;
import m09uf2.prob.timer2.Timer2;

public class Timer2Run {
	
	static final Logger LOGGER = Logger.getLogger(Timer2Run.class.getName());

	public static void main(String[] args) {
		
		LoggingConfigurator.configure(Level.INFO);
		
		test(newInstance());
	}
	
	private static Timer2 newInstance() {
		
		String packageName = Problems.getImplPackage(Timer2.class);
		return Reflections.newInstanceOfType(
				Timer2.class, packageName + ".Timer2Impl");
	}
	
	private static void test(Timer2 t2) {
		
		long nid;
		
		nid = t2.set(2000, id -> {
			LOGGER.info("primera alarma!");
		});		
		LOGGER.info("added " + nid);
		
		nid = t2.set(5000, id -> {
			LOGGER.info("segona alarma!");
			t2.shutdown();
		});
		LOGGER.info("added " + nid);
		
		nid = t2.set(1000, id -> {
			LOGGER.info("tercera alarma!");			
		});
		LOGGER.info("added " + nid);
	}
}

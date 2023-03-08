package m09uf2.impl.timer1;

import java.util.logging.Level;
import java.util.logging.Logger;

import lib.LoggingConfigurator;
import lib.Problems;
import lib.Reflections;
import lib.Threads;
import m09uf2.prob.timer1.Timer1;

public class Timer1Run {
	
	static final Logger LOGGER = Logger.getLogger(Timer1Run.class.getName());

	public static void main(String[] args) {
		
		LoggingConfigurator.configure(Level.INFO);
		
		// constructor of Timer1Impl must be Timer1Impl(long millis) 
		

		testHold(newInstance(3000));
		testCancel(newInstance(3000));
		testDone(newInstance(3000));
	}
	
	private static Timer1 newInstance(int millis) {
		
		String packageName = Problems.getImplPackage(Timer1.class);
		return Reflections.newInstanceOfType(
				Timer1.class, packageName + ".Timer1Impl", 
				new Class[] {long.class}, 
				new Object[] {millis});
	}
	
	private static void testHold(Timer1 t1) {
		
		LOGGER.info("go hold!");
		t1.go();
		LOGGER.info("wait 1500...");
		Threads.sleep(1500);
		LOGGER.info("isAlive 1: " + t1.getThread().isAlive());
		LOGGER.info("hold!");	
		t1.hold();		
		LOGGER.info("isAlive 2: " + t1.getThread().isAlive());
		LOGGER.info("done!");
	}
	
	private static void testCancel(Timer1 t1) {
		
		LOGGER.info("go cancel!");
		t1.go();		
		LOGGER.info("cancel!");
		t1.cancel();		
		t1.hold();		
		LOGGER.info("done!");
	}
	
	private static void testDone(Timer1 t1) {
		
		LOGGER.info("go done!");
		t1.go();		
		while (!t1.done()) {
			Thread.yield();
			// sleep???
		}		
		LOGGER.info("done!");
	}
}

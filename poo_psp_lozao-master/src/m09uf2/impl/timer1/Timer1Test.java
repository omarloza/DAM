package m09uf2.impl.timer1;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import lib.LoggingConfigurator;
import lib.Problems;
import lib.Reflections;
import lib.Threads;
import m09uf2.prob.timer1.Timer1;

public class Timer1Test {
	
	static final Logger LOGGER = Logger.getLogger(Timer1Test.class.getName());
	static final long MARGIN = 20; 
	static final long MILLIS = 500;
	
	private Timer1 t1;
	private long started;
	
	@BeforeAll
	static void beforeAll() {
		
		LoggingConfigurator.configure(Level.INFO);
	}
	
	@BeforeEach
	void beforeEach() {		
		
		String packageName = Problems.getImplPackage(Timer1.class);
		t1 = Reflections.newInstanceOfType(
				Timer1.class, packageName + ".Timer1Impl", 
				new Class[] {long.class}, 
				new Object[] {MILLIS});
	}	
	
	private void setStart() {
		
		started = System.currentTimeMillis();
	}
	
	private void checkInterval(long millis) {
		
		long diff = Math.abs(System.currentTimeMillis() - started - millis);		
		LOGGER.info("margin is " + diff);
		assertTrue(diff < MARGIN);
		setStart();
	}

	@Test
	void testHold() {
		
		setStart();
		
		t1.go();
		checkInterval(0);
		Threads.sleep(MILLIS/2);
		assertTrue(t1.getThread().isAlive());
		
		t1.hold();
		assertFalse(t1.getThread().isAlive());
		checkInterval(t1.millis());
	}
	
	@Test
	void testCancel() {
		
		setStart();
		
		t1.go();
		checkInterval(0);
		
		t1.cancel();
		checkInterval(0);
		
		t1.hold();		
		checkInterval(0);
	}
	
	@Test
	void testDone() {
		
		setStart();
		
		t1.go();		
		while (!t1.done()) {
			Thread.yield();
			// sleep???
		}		
		checkInterval(t1.millis());
	}
}

package m09uf2.impl.timer2;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import lib.LoggingConfigurator;
import lib.Problems;
import lib.Reflections;
import m09uf2.prob.timer2.Timer2;

public class Timer2Test {

	static final Logger LOGGER = Logger.getLogger(Timer2Test.class.getName());
	static final long MARGIN = 50; // precisio del temporitzador
	
	private Timer2 t2;
	private long started;
	
	@BeforeAll
	static void beforeAll() {
		
		LoggingConfigurator.configure(Level.INFO);
	}
	
	@BeforeEach
	void beforeEach() {		
		
		String packageName = Problems.getImplPackage(Timer2.class);
		t2 = Reflections.newInstanceOfType(
				Timer2.class, packageName + ".Timer2Impl");
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
	void testOne() {
		
		MBoolean b1 = new MBoolean();
		
		setStart();
		
		long nid = t2.set(1000, id -> {
			checkInterval(1000);
			b1.value = true;
			LOGGER.info("alarma!");
		});
		LOGGER.info("added " + nid);
		
		try {
			Thread.sleep(1500);			
			assertTrue(b1.value);		
			t2.shutdown();
			
		} catch (InterruptedException e) {
			fail(e);
		}
	}
	
	@Test
	void testThree() {
		
		long nid;
		
		MBoolean b1 = new MBoolean();
		MBoolean b2 = new MBoolean();
		MBoolean b3 = new MBoolean();
		
		setStart();
		
		nid = t2.set(2000, id -> {
			checkInterval(1000);
			b1.value = true;
			LOGGER.info("primera alarma!");
		});		
		LOGGER.info("added " + nid);
		
		nid = t2.set(5000, id -> {
			checkInterval(3000);
			b2.value = true;
			LOGGER.info("segona alarma!");
		});
		LOGGER.info("added " + nid);
		
		nid = t2.set(1000, id -> {
			checkInterval(1000);
			b3.value = true;
			LOGGER.info("tercera alarma!");
		});
		LOGGER.info("added " + nid);
		
		try {
			Thread.sleep(6000);
			
			assertTrue(b1.value);
			assertTrue(b2.value);
			assertTrue(b3.value);
			
			t2.shutdown();			
			
		} catch (InterruptedException e) {
			fail(e);
		}
	}
	
	static class MBoolean {
		public boolean value;
	}
}

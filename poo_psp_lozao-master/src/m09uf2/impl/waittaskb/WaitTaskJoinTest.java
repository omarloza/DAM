package m09uf2.impl.waittaskb;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import lib.LoggingConfigurator;
import lib.Problems;
import lib.Reflections;
import lib.Threads;
import lib.fils.Mutable;
import m09uf2.prob.waittaskb.TestDoTask;
import m09uf2.prob.waittaskb.WaitTaskJoin;

public class WaitTaskJoinTest {

	static final Logger LOGGER = Logger.getLogger(WaitTaskJoinTest.class.getName());
	static final long MARGIN = 50; // precisio del temporitzador

	private WaitTaskJoin<String> wtask;
	private long started;
	
	@BeforeAll
	static void beforeAll() {
		
		LoggingConfigurator.configure(Level.INFO);
	}
	
	@BeforeEach
	@SuppressWarnings("unchecked")
	void beforeEach() {		
		
		String packageName = Problems.getImplPackage(WaitTaskJoin.class);
		// WaitTaskJoin<String> wtask = new WaitTaskJoinImpl<>();
		wtask = Reflections.newInstanceOfType(
				WaitTaskJoin.class, packageName + ".WaitTaskJoinImpl",
				new Class[] {}, 
				new Object[] {});
	}	
	
	private void setStart() {
		
		started = System.currentTimeMillis();
	}
	
	private void checkInterval(long millis) {
		
		long diff = Math.abs(System.currentTimeMillis() - started - millis);
		if (diff >= MARGIN) {
			fail("margin is " + diff);
		}
		setStart();
	}
	
	@Test
	void testJoin() {
		
		String returnValue = new Date().toString();
		Mutable<Boolean> done = new Mutable<>(false);
		TestDoTask doTask = new TestDoTask(done, returnValue);
		
		setStart();
		wtask.submit(1500, doTask);
		checkInterval(0);
		String result = wtask.result();
		assertEquals(returnValue, result);
		checkInterval(1500);
	}
	
	@Test
	void testCancel() {
		
		String returnValue1 = new Date().toString();
		Mutable<Boolean> done = new Mutable<>(false);
		TestDoTask doTask = new TestDoTask(done, returnValue1);
		
		setStart();
		wtask.submit(1500, doTask);
		checkInterval(0);
		Threads.sleep(750);
		checkInterval(750);
		
		String returnValue2 = new Date().toString();
		wtask.cancel(returnValue2);
		checkInterval(0);
		assertEquals(returnValue2, wtask.result());
		checkInterval(0);
	}
}

package m09uf1.impl.speerb;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.function.Consumer;
import java.util.logging.Logger;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import lib.LoggingConfigurator;
import lib.Problems;
import lib.Reflections;
import lib.fils.Mutable;
import m09uf1.prob.speerb.SafePeer;

public class SafePeerTest {

	static final Logger LOGGER = Logger.getLogger(SafePeerTest.class.getName());
	
	static final String MSG1 = "quin temps!";
	static final String MSG2 = "vols dir?";
	static final String MSG3 = "força bé!";
	
	private static SafePeer getInstance(Consumer<String> consumer) {
		
		String packageName = Problems.getImplPackage(SafePeer.class);		
		return Reflections.newInstanceOfType(
				SafePeer.class, packageName + ".SafePeerImpl",
				new Class[] {Consumer.class}, 
				new Object[] {consumer});
	}	

	@BeforeAll
	static void beforeAll() {	
		LoggingConfigurator.configure();
	}
	
	@Test
	void testBase() {
		
		Mutable<String> m1 = new Mutable<>();		
		Mutable<String> m2 = new Mutable<>();
		
		SafePeer p1 = getInstance(msg -> m1.setValue(msg));
		SafePeer p2 = getInstance(msg -> m2.setValue(msg));
		
		p1.connectTo(p2);
		p1.send(MSG1);
		
		assertNull(m1.getValue());
		assertEquals(MSG1, m2.getValue());
		
		m2.setValue(null);
		p2.send(MSG2);
		
		assertNull(m2.getValue());
		assertEquals(MSG2, m1.getValue());
	}	
	
	@Test
	void testReconnect() {
		
		Mutable<String> m1 = new Mutable<>();		
		Mutable<String> m2 = new Mutable<>();
		Mutable<String> m3 = new Mutable<>();
		
		SafePeer p1 = getInstance(msg -> m1.setValue(msg));
		SafePeer p2 = getInstance(msg -> m2.setValue(msg));
		SafePeer p3 = getInstance(msg -> m3.setValue(msg));
		
		p1.connectTo(p2);
		p1.send(MSG1);		
		assertEquals(MSG1, m2.getValue());
		
		p2.connectTo(p3);
		p2.send(MSG2);
		assertEquals(MSG2, m3.getValue());
		
		p3.connectTo(p1);
		p3.send(MSG3);
		assertEquals(MSG3, m1.getValue());
	}
	
	@Test
	void testUnconnected() {
		
		Mutable<String> m1 = new Mutable<>();
		SafePeer p1 = getInstance(msg -> m1.setValue(msg));

		try {
			p1.send(MSG1);
			fail("hauria d'haver fallat el send");			
		} catch (RuntimeException e) {}	
	}
	
	@Test
	void testBadConnect() {
		
		Mutable<String> m1 = new Mutable<>();
		Mutable<String> m2 = new Mutable<>();
		
		SafePeer p1 = getInstance(msg -> m1.setValue(msg));
		SafePeer p2 = getInstance(msg -> m2.setValue(msg));
		
		try {
			p2.connectFrom(p1, new byte[] {1, 2, 3});
			fail("hauria d'haver fallat el connectFrom");			
		} catch (RuntimeException e) {}
	}
	
	@Test
	void testBadReceive() {
		
		Mutable<String> m1 = new Mutable<>();
		Mutable<String> m2 = new Mutable<>();
		
		SafePeer p1 = getInstance(msg -> m1.setValue(msg));
		SafePeer p2 = getInstance(msg -> m2.setValue(msg));
		
		p1.connectTo(p2);
		
		try {
			p2.receive(new byte[] {1, 2, 3});
			fail("hauria d'haver fallat el receive");			
		} catch (RuntimeException e) {}
	}
}

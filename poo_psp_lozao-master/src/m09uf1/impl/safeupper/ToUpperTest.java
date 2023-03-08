package m09uf1.impl.safeupper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.util.logging.Level;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import lib.LoggingConfigurator;
import lib.Problems;
import lib.Reflections;
import lib.Startable;
import lib.Threads;
import m09uf1.prob.safeupper.ToUpperClient;

public class ToUpperTest {

	static final String HOST = "localhost";
	static final int PORT = 5509;
	static final byte[] AESKEY = new byte[] { 33, 1, 25, 18, -22, 89, -101, 8, 55, 46, -77, 3, 96, 11, -38, 12 };
	static final long MARGIN = 50; // precisio del temporitzador

	private long started;

	private static ToUpperClient getClientInstance(String host) {

		String packageName = Problems.getImplPackage(ToUpperClient.class);
		return Reflections.newInstanceOfType(ToUpperClient.class, packageName + ".ToUpperClientImpl",
				new Class[] { String.class, int.class, byte[].class }, new Object[] { host, PORT, AESKEY });
	}

	private static Startable getServerInstance(String host) {

		String packageName = Problems.getImplPackage(ToUpperClient.class);
		return Reflections.newInstanceOfType(Startable.class, packageName + ".ToUpperServerImpl",
				new Class[] { String.class, int.class, byte[].class }, new Object[] { host, PORT, AESKEY });
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

	@BeforeAll
	static void beforeAll() {

		LoggingConfigurator.configure(Level.FINE);
	}

	@Test
	void testOne() {

		ToUpperClient tuc = getClientInstance(HOST);
		Startable tus = getServerInstance(HOST);

		setStart();
		tus.start();
		Threads.sleep(500); // some time to start server
		checkInterval(500);

		String input, result;

		try {
			tuc.connect();
			checkInterval(0);

			input = "Somewhere over the rainbow";
			result = tuc.toUpper(input);
			checkInterval(0);
			assertEquals(input.toUpperCase(), result);

			tuc.disconnect();
			checkInterval(0);

		} catch (IOException e) {
			fail(e);
		} finally {
			tus.stop();
		}
	}
}

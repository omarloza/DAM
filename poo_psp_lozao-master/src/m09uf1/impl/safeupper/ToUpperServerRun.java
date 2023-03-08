package m09uf1.impl.safeupper;

import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

import lib.LoggingConfigurator;
import lib.Problems;
import lib.Reflections;
import lib.Startable;
import m09uf1.prob.safeupper.ToUpperClient;

public class ToUpperServerRun {
	
	static final Logger LOGGER = Logger.getLogger(ToUpperServerRun.class.getName());

	static final String HOST = "localhost";
	static final int PORT = 5510;
	static final byte[] AESKEY = new byte[] {
			33, 1, 25, 18, -22, 89, -101, 8, 55, 46, -77, 3, 96, 11, -38, 12};
	
	private static Startable getServerInstance(String host) {
		
		String packageName = Problems.getImplPackage(ToUpperClient.class);		
		return Reflections.newInstanceOfType(
				Startable.class, packageName + ".ToUpperServerImpl",
				new Class[] {String.class, int.class, byte[].class}, 
				new Object[] {host, PORT, AESKEY});
	}
	
	public static void main(String[] args) {
		
		LoggingConfigurator.configure(Level.FINE);
		
		LOGGER.info("key is " + Base64.getEncoder().encodeToString(AESKEY));
				
		Startable tus = getServerInstance(HOST);
		tus.start();
	}
}

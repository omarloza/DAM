package m09uf1.impl.safeupper;

import java.io.IOException;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

import lib.LoggingConfigurator;
import lib.Problems;
import lib.Reflections;
import lib.Threads;
import m09uf1.prob.safeupper.ToUpperClient;

public class ToUpperClientRun {
	
	static final Logger LOGGER = Logger.getLogger(ToUpperClientRun.class.getName());
	//146.255.96.104
	//9001
	
//	static final String HOST = "146.255.96.104";
//	static final int PORT = 9001;
	static final String HOST = "localhost";
	static final int PORT = 5509;
	static final byte[] AESKEY = new byte[] {
			33, 1, 25, 18, -22, 89, -101, 8, 55, 46, -77, 3, 96, 11, -38, 12};
	
	private static ToUpperClient getClientInstance(String host) {
		
		String packageName = Problems.getImplPackage(ToUpperClient.class);		
		return Reflections.newInstanceOfType(
				ToUpperClient.class, packageName + ".ToUpperClientImpl",
				new Class[] {String.class, int.class, byte[].class}, 
				new Object[] {host, PORT, AESKEY});
	}
	
	public static void main(String[] args) {
		
		LoggingConfigurator.configure(Level.FINE);
		
		LOGGER.info("key is " + Base64.getEncoder().encodeToString(AESKEY));
				
		ToUpperClient tuc = getClientInstance(HOST);
		
		String result;
		long wait = 2500; // 2500
		try {
			tuc.connect();
			
			Threads.sleep(wait);
			result = tuc.toUpper("hola, mon!");
			if (!"HOLA, MON!".equals(result)) {
				throw new RuntimeException("unexpected " + result);
			}
			
			Threads.sleep(wait);
			result = tuc.toUpper("com va?");
			if (!"COM VA?".equals(result)) {
				throw new RuntimeException("unexpected " + result);
			}
			
			tuc.disconnect();
			
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, null, e);
		}
	}
}

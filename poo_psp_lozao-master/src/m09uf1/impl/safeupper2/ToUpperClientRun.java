package m09uf1.impl.safeupper2;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import lib.LoggingConfigurator;
import lib.Problems;
import lib.Reflections;
import lib.Threads;
import m09uf1.prob.safeupper2.ToUpperClient;

public class ToUpperClientRun {
	
	static final Logger LOGGER = Logger.getLogger(ToUpperClientRun.class.getName());

	static final String HOST = "146.255.96.104";
	static final int PORT = 9000;
	
//	static final String HOST = "localhost";
//	static final int PORT = 5510;
	private static ToUpperClient getClientInstance(String host) {
		
		String packageName = Problems.getImplPackage(ToUpperClient.class);		
		return Reflections.newInstanceOfType(
				ToUpperClient.class, packageName + ".ToUpperClientImpl",
				new Class[] {String.class, int.class}, 
				new Object[] {host, PORT});
	}
	
	public static void main(String[] args) {
		
		LoggingConfigurator.configure(Level.INFO);
		
		ToUpperClient tuc = getClientInstance(HOST);
		
		String result;
		long wait = 2500; // 2500
		try {
			tuc.connect();
			
			Threads.sleep(wait);
			result = tuc.toUpper("hola, món!");
			if (!"HOLA, MÓN!".equals(result)) {
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

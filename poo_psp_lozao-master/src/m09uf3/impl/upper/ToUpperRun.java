package m09uf3.impl.upper;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import lib.LoggingConfigurator;
import lib.Problems;
import lib.Reflections;
import lib.Threads;
import m09uf3.prob.upper.ToUpperClient;
import m09uf3.prob.upper.ToUpperServer;

public class ToUpperRun {
	
	static final Logger LOGGER = Logger.getLogger(ToUpperRun.class.getName());

	static final int PORT = 5509;
	
	private static ToUpperClient getClientInstance(String host) {
		
		String packageName = Problems.getImplPackage(ToUpperClient.class);		
		return Reflections.newInstanceOfType(
				ToUpperClient.class, packageName + ".ToUpperClientImpl",
				new Class[] {String.class, int.class}, 
				new Object[] {host, PORT});
	}
	
	private static ToUpperServer getServerInstance(String host) {
		
		String packageName = Problems.getImplPackage(ToUpperClient.class);		
		return Reflections.newInstanceOfType(
				ToUpperServer.class, packageName + ".ToUpperServerImpl",
				new Class[] {String.class, int.class}, 
				new Object[] {host, PORT});
	}
	
	public static void main(String[] args) {
		
		LoggingConfigurator.configure();
				
		ToUpperServer tus = getServerInstance("localhost");		
		tus.start();		
		
		ToUpperClient tuc = getClientInstance("localhost");
		
		String result;		
		try {
			tuc.connect();
			
			Threads.sleep(2500);
			result = tuc.toUpper("hola, món!");
			if (!"HOLA, MÓN!".equals(result)) {
				throw new RuntimeException("unexpected " + result);
			}
			
			Threads.sleep(2500);
			result = tuc.toUpper("com va?");
			if (!"COM VA?".equals(result)) {
				throw new RuntimeException("unexpected " + result);
			}
			
			tuc.disconnect();
			
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, null, e);
			
		} finally {
			tus.stop();
		}
	}
}

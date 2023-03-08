package m09uf3.impl.fwatch;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import lib.LoggingConfigurator;
import lib.Problems;
import lib.Reflections;
import lib.Threads;
import m09uf3.prob.fwatch.FWatchClient;

public class FWatchClientRun {

	static final Logger LOGGER = Logger.getLogger(FWatchClientRun.class.getName());
	
	static final int PORT = 5510;
	
	private static FWatchClient getClientInstance(String host) {
		
		String packageName = Problems.getImplPackage(FWatchClient.class);		
		return Reflections.newInstanceOfType(
				FWatchClient.class, packageName + ".FWatchClientImpl",
				new Class[] {String.class, int.class}, 
				new Object[] {host, PORT});
	}
		
	public static void main(String[] args) {
		
		LoggingConfigurator.configure(Level.INFO);
				
		Threads.sleep(500); // some time to start server
		
		String host16 = "10.1.5.10";
		String host17 = "10.1.6.40";
		FWatchClient client = getClientInstance(host17);
		
		// assumes the server is in the same machine/OS
		String folder = "C:/data/fwatch";
		
		try {
			LOGGER.info("connecting");
			client.connect(folder, (event, filepath) -> {
				LOGGER.info("received " + event + " on " + filepath);
			});					
			
			Threads.sleep(60000);
			
			client.disconnect();
			
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, null, e);
			
		}
	}}

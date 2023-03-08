package m09uf3.impl.guessb;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import lib.LoggingConfigurator;
import lib.Problems;
import lib.Reflections;
import lib.Startable;
import m09uf3.prob.guessb.GuessClient;
import m09uf3.prob.guessb.GuessSecret;

public class GuessRun {

	static final Logger LOGGER = Logger.getLogger(GuessRun.class.getName());

	static final String HOST = "localhost";
	static final int PORT = 5514;	
	
	private static GuessClient getClientInstance(String host) {
		
		String packageName = Problems.getImplPackage(GuessClient.class);		
		return Reflections.newInstanceOfType(
				GuessClient.class, packageName + ".GuessClientImpl",
				new Class[] {String.class, int.class}, 
				new Object[] {host, PORT});
	}
	
	private static Startable getServerInstance(String host, GuessSecret gs) {
		
		String packageName = Problems.getImplPackage(GuessClient.class);		
		return Reflections.newInstanceOfType(
				Startable.class, packageName + ".GuessServerImpl",
				new Class[] {String.class, int.class, GuessSecret.class}, 
				new Object[] {host, PORT, gs});
	}
	
	public static void main(String[] args) {
		
		LoggingConfigurator.configure(Level.FINE);	
		
		GuessSecret gs = new GuessSecret(new int[] {13}, 20);
		Startable server = getServerInstance(HOST, gs);
		server.start();	
		
		GuessClient client = getClientInstance(HOST);
		LOGGER.info("start: " + Arrays.toString(client.start()));
		
		try {
			LOGGER.info("played 8: " + client.play((byte) 8));
			LOGGER.info("played 15: " + client.play((byte) 15));
			LOGGER.info("played 13: " + client.play((byte) 13));
			
		} finally {
			server.stop();
		}
	}
}

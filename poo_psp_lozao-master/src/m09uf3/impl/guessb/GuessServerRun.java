package m09uf3.impl.guessb;

import java.util.logging.Level;
import java.util.logging.Logger;

import lib.LoggingConfigurator;
import lib.Problems;
import lib.Reflections;
import lib.Startable;
import m09uf3.prob.guessb.GuessClient;
import m09uf3.prob.guessb.GuessSecret;

public class GuessServerRun {

	static final Logger LOGGER = Logger.getLogger(GuessRun.class.getName());

	static final String HOST = "localhost";
	static final int PORT = 5514;	
	
	private static Startable getServerInstance(String host, GuessSecret gs) {
		
		String packageName = Problems.getImplPackage(GuessClient.class);		
		return Reflections.newInstanceOfType(
				Startable.class, packageName + ".GuessServerImpl",
				new Class[] {String.class, int.class, GuessSecret.class}, 
				new Object[] {host, PORT, gs});
	}
	
	public static void main(String[] args) {
		
		LoggingConfigurator.configure(Level.INFO);	
		
		GuessSecret gs = new GuessSecret(new int[] {13}, 20);
		Startable server = getServerInstance(HOST, gs);
		server.start();	
	}
}

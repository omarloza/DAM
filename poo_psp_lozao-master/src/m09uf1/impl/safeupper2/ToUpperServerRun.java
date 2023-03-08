package m09uf1.impl.safeupper2;

import java.util.logging.Level;
import java.util.logging.Logger;

import lib.LoggingConfigurator;
import lib.Problems;
import lib.Reflections;
import lib.Startable;
import m09uf1.prob.safeupper2.ToUpperClient;

public class ToUpperServerRun {
	
	static final Logger LOGGER = Logger.getLogger(ToUpperServerRun.class.getName());

	static final String HOST = "localhost";
	static final int PORT = 5510;
	
	private static Startable getServerInstance(String host) {
		
		String packageName = Problems.getImplPackage(ToUpperClient.class);		
		return Reflections.newInstanceOfType(
				Startable.class, packageName + ".ToUpperServerImpl",
				new Class[] {String.class, int.class}, 
				new Object[] {host, PORT});
	}
	
	public static void main(String[] args) {
		
		LoggingConfigurator.configure(Level.INFO);
				
		Startable tus = getServerInstance(HOST);
		tus.start();
	}
}

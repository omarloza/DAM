package m09uf1.impl.speerb;

import java.util.function.Consumer;
import java.util.logging.Logger;

import javax.crypto.BadPaddingException;

import lib.LoggingConfigurator;
import lib.Problems;
import lib.Reflections;
import m09uf1.prob.speerb.SafePeer;

public class SafePeerRun {
	
	static final Logger LOGGER = Logger.getLogger(SafePeerRun.class.getName());
	
	private static SafePeer getInstance(Consumer<String> consumer) {
		
		String packageName = Problems.getImplPackage(SafePeer.class);		
		return Reflections.newInstanceOfType(
				SafePeer.class, packageName + ".SafePeerImpl",
				new Class[] {Consumer.class}, 
				new Object[] {consumer});
	}	

	public static void main(String[] args) throws BadPaddingException {
		
		LoggingConfigurator.configure();
		
		SafePeer p1 = getInstance((msg) -> LOGGER.info("p1: " + msg));
		SafePeer p2 = getInstance((msg) -> LOGGER.info("p2: " + msg));
		SafePeer p3 = getInstance((msg) -> LOGGER.info("p3: " + msg));
		
		p1.connectTo(p2);
		p2.send("soc el p2, com et trobes, p1?");
		p1.send("hola p1, anem fent!");
		
		p1.connectTo(p3);
		p3.send("soc el p3, com et trobes, p1?");
		p1.send("hola p1, anar-hi anant!");
	}
}

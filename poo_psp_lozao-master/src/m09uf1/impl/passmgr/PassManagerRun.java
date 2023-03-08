package m09uf1.impl.passmgr;

import java.util.logging.Logger;

import org.json.JSONObject;

import lib.LoggingConfigurator;
import lib.Problems;
import lib.Reflections;
import m09uf1.prob.passmgr.PassManager;

public class PassManagerRun {
	
	static final Logger LOGGER = Logger.getLogger(PassManagerRun.class.getName());

	private static PassManager getInstance(JSONObject entries) {
		
		String packageName = Problems.getImplPackage(PassManager.class);
		return Reflections.newInstanceOfType(
				PassManager.class, packageName + ".PassManagerImpl",
				new Class[] {JSONObject.class},
				new Object[] {entries});
	}
	
	public static void main(String[] args) throws Exception {
		
		LoggingConfigurator.configure();
		
		// myuser,mypassword
		JSONObject entries = new JSONObject()
				.put("myuser", new JSONObject()
						.put("pass", "IrYoM94B6jd/VrU7q6JzvW8l5fKJcn/LYlywkQFp2KU=")
						.put("salt", "CB+ek0q3461xy+kGd2qdTQ=="));
		
		PassManager pm = getInstance(entries);
		pm.addEntry("one", "mypassword".toCharArray());
		LOGGER.info(pm.toString());
		
		boolean ok = pm.checkPassword("myuser", "mypassword".toCharArray());
		LOGGER.info("check: " + ok);
		
		pm.addEntry("youruser", "yourpassword".toCharArray());
		LOGGER.info(pm.toString());		
		
		ok = pm.checkPassword("youruser", "badpassword".toCharArray());
		LOGGER.info("check: " + ok);
	}
}

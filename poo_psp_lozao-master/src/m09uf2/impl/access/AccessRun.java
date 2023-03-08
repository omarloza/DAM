package m09uf2.impl.access;

import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;

import lib.LoggingConfigurator;
import lib.Problems;
import lib.Reflections;
import m09uf2.prob.access.SiteAccessService;

public class AccessRun {
	
	static final Logger LOGGER = Logger.getLogger(AccessRun.class.getName());

	public static void main(String[] args) {
		
		LoggingConfigurator.configure(Level.INFO);
		
		SiteAccessService sas = newInstance();
		
		String[] urls = new String[] {
			"http://localhost:8888",
			"https://www.google.com",
			"https://agora.xtec.cat/ies-sabadell/",
			"http://ensenyament.gencat.cat/ca/inici"
		};
		
		CountDownLatch cdl = new CountDownLatch(urls.length);
		
		try {
			for (String url: urls) {
				sas.request(url, ar -> {
					LOGGER.info(ar.toString());
					cdl.countDown();
				});	
			}

			cdl.await();
			sas.shutdown();		
			
		} catch (InterruptedException e) {
			LOGGER.log(Level.SEVERE, "requesting", e);
		}
	}
	
	private static SiteAccessService newInstance() {
		
		String packageName = Problems.getImplPackage(SiteAccessService.class);		
		return Reflections.newInstanceOfType(
				SiteAccessService.class, packageName + ".SiteAccessServiceImpl",
				new Class[] {int.class}, 
				new Object[] {10});
	}
}

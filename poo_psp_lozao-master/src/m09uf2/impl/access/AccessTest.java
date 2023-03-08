package m09uf2.impl.access;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.jupiter.api.Test;

import lib.LoggingConfigurator;
import lib.Problems;
import lib.Reflections;
import lib.Threads;
import m09uf2.prob.access.AccessReport;
import m09uf2.prob.access.SiteAccessService;

public class AccessTest {
	
	static final Logger LOGGER = Logger.getLogger(AccessTest.class.getName());
		
	@Test
	void testChecker() {
		
		LoggingConfigurator.configure(Level.INFO);
		
		String packageName = Problems.getImplPackage(SiteAccessService.class);
		SiteAccessService sas = Reflections.newInstanceOfType(
				SiteAccessService.class, packageName + ".SiteAccessServiceImpl",
				new Class[] {int.class}, 
				new Object[] {10});
		
		String[] urls = new String[] {
			"https://www.google.com",
			"https://agora.xtec.cat/ies-sabadell/",
			"http://ensenyament.gencat.cat/ca/inici"
		};

		CountDownLatch cdl = new CountDownLatch(urls.length);
		List<AccessReport> reps = new ArrayList<>();

		try {
			for (String url: urls) {
				sas.request(url, ar -> {
				
					reps.add(ar);
					cdl.countDown();
				});	
			}
								
			assertTrue(cdl.await(2500, TimeUnit.MILLISECONDS));
			
			assertEquals(urls.length, reps.size());
			for (AccessReport ar: reps) {
				assertNotNull(ar.getUrl());
				assertNotNull(ar.getServer());
				assertTrue(ar.getTook() > 0);
			}
			
			sas.shutdown();
			Threads.sleep(500);
			
		} catch (InterruptedException e) {
			fail(e);
		}
	}
}

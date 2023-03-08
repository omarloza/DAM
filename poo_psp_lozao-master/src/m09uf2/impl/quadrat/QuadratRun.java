package m09uf2.impl.quadrat;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

import lib.LoggingConfigurator;
import lib.Problems;
import lib.Reflections;
import m09uf2.prob.quadrat.QuadratService;

public class QuadratRun {

	static final Logger LOGGER = Logger.getLogger(QuadratRun.class.getName());
	
	public static void main(String[] args) {
	
		LoggingConfigurator.configure();
		test(12, 8, 4, 19);
	}
		
	public static void test(double... nums) {
		
		String packageName = Problems.getImplPackage(QuadratService.class);		
		QuadratService qs = Reflections.newInstanceOfType(
				QuadratService.class, packageName + ".QuadratServiceImpl",
				new Class[] {int.class}, 
				new Object[] {10});

		List<Future<Double>> futs = new ArrayList<>();
		
		try {
			for (double num: nums) {
				futs.add(qs.request(num));	
			}
			
			for (Future<Double> future: futs) {
				LOGGER.info("result is " + future.get());
			}
			
		} catch (InterruptedException e) {
			LOGGER.info("interrupted!");
			
		} catch (ExecutionException e) {
			LOGGER.log(Level.SEVERE, "failed!", e);
			
		} finally {
			qs.shutdown();
		}
	}
}

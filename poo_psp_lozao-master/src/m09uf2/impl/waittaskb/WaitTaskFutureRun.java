package m09uf2.impl.waittaskb;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Logger;

import lib.LoggingConfigurator;
import lib.Problems;
import lib.Reflections;
import lib.Threads;
import lib.fils.Mutable;
import m09uf2.prob.waittaskb.TestDoTask;
import m09uf2.prob.waittaskb.WaitTaskFuture;

public class WaitTaskFutureRun {

	static final Logger LOGGER = Logger.getLogger(WaitTaskFutureRun.class.getName());

	static final int RUNNING = 2500;
	
	public static void main(String[] args) throws Exception {
		
		LoggingConfigurator.configure();
		
		String packageName = Problems.getImplPackage(WaitTaskFuture.class);
		// WaitTaskFuture<String> wtask = new WaitTaskFutureImpl<>();
		@SuppressWarnings("unchecked")
		WaitTaskFuture<String> wtaskf = Reflections.newInstanceOfType(
				WaitTaskFuture.class, packageName + ".WaitTaskFutureImpl",
				new Class[] {}, 
				new Object[] {});
				
		Threads.sleep(750);
		
		LOGGER.info("start");		
		long start = System.currentTimeMillis();
		
		Mutable<Boolean> done = new Mutable<>(false);
		TestDoTask doTask = new TestDoTask(done, "futur acabat!");
		
		wtaskf.submit(RUNNING, doTask);
		
		Future<String> future = wtaskf.future();
		
		Threads.sleep(750);
		LOGGER.info("done? " + done.getValue());
		
		String result;
		try {
			result = future.get();
		} catch (InterruptedException | ExecutionException e) {
			throw new RuntimeException("getting future", e);
		}
		
		long end = System.currentTimeMillis();
		LOGGER.info("took " + (end - start) + " ==? " + RUNNING);
		LOGGER.info("done? " + done.getValue());
		LOGGER.info("result is " + result);
	}
}

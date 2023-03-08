package m09uf2.impl.waittaskb;

import java.util.logging.Logger;

import lib.LoggingConfigurator;
import lib.Problems;
import lib.Reflections;
import lib.Threads;
import lib.fils.Mutable;
import m09uf2.prob.waittaskb.TestDoTask;
import m09uf2.prob.waittaskb.WaitTaskJoin;

public class WaitTaskJoinRun {

	static final Logger LOGGER = Logger.getLogger(WaitTaskJoinRun.class.getName());

	static final int RUNNING = 2500;
	
	public static void main(String[] args) throws Exception {
		
		LoggingConfigurator.configure();
		
		String packageName = Problems.getImplPackage(WaitTaskJoin.class);
		// WaitTaskJoin<String> wtask = new WaitTaskJoinImpl<>();
		@SuppressWarnings("unchecked")
		WaitTaskJoin<String> wtaskj = Reflections.newInstanceOfType(
				WaitTaskJoin.class, packageName + ".WaitTaskJoinImpl",
				new Class[] {}, 
				new Object[] {});
						
		Threads.sleep(750);
		
		LOGGER.info("start");		
		long start = System.currentTimeMillis();
		
		Mutable<Boolean> done = new Mutable<>(false);
		TestDoTask doTask = new TestDoTask(done, "join acabat!");
		
		wtaskj.submit(RUNNING, doTask);
		
		String result = wtaskj.result();
		
		LOGGER.info("done? " + done.getValue());
		
		long end = System.currentTimeMillis();
		LOGGER.info("took " + (end - start) + " ==? " + RUNNING);
		LOGGER.info("done? " + done.getValue());
		LOGGER.info("result is " + result);
	}
}

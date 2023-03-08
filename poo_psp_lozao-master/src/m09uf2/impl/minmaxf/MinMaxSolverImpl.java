package m09uf2.impl.minmaxf;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Logger;
import m09uf2.prob.minmaxf.MinMaxSolver;


public class MinMaxSolverImpl implements MinMaxSolver {

	static final Logger LOGGER = Logger.getLogger(MinMaxSolverImpl.class.getName());

	private MinMaxCalc calc;

	public MinMaxSolverImpl(MinMaxCalc calc) {
		this.calc = calc;
	}

	@Override
	public double[] minMax(double[] nums) {

		int count = Runtime.getRuntime().availableProcessors();
		LOGGER.info("start " + count + " threads!");
		ExecutorService executor = Executors.newFixedThreadPool(count);
		
		MinMaxCallable[] runs = new MinMaxCallable[count];
		Future <double[]> futuros[] = new Future[count];
		
		int from = 0;
		int part = nums.length / count;
		
		

			for (int i = 0; i < count; i++) {
				int to = i + 1 == count ? nums.length : from + part;
				LOGGER.info("from " + from + " to " + to);
			
				runs[i] = new MinMaxCallable(calc, nums, from, to);
				futuros[i]=executor.submit(runs[i]);
				
				from = to;
			}
			
			double[] mins = new double[count];
			double[] maxs = new double[count];
			
			int n =0;
			double[] minMax=new double[2];
			try {
			
			for (int i = 0; i < count; i++) {
				
				try {
					minMax=futuros[i].get();
				} catch (ExecutionException e) {
					
					e.printStackTrace();
				}
				
//				System.out.println(minMax.length);
			
			mins[i]=minMax[0];
			maxs[i]=minMax[1];
//			
//				System.out.println(mins[i]);
				
			}
			executor.shutdown();
			
			double min = calc.min(mins, 0, count);
			double max = calc.max(maxs, 0, count);

			return new double[] { min, max };

		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}

	}

	static class MinMaxCallable implements Callable<double[]> {

		private MinMaxCalc calc;
		private double[] nums;
		private int from, to;
		private double min, max;

		public MinMaxCallable(MinMaxCalc calc, double[] nums, int from, int to) {
			this.calc = calc;
			this.nums = nums;
			this.from = from;
			this.to = to;
		}

		public double getMin() {
			return min;
		}

		public double getMax() {
			return max;
		}

		@Override
		public double[] call() throws Exception {
			LOGGER.info("start!");
			min = calc.min(nums, from, to);
			max = calc.max(nums, from, to);
			LOGGER.info("parcial min=" + min + ", max=" + max);

			return new double[] { min, max };
		}
	}
}

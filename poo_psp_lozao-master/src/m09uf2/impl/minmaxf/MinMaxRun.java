package m09uf2.impl.minmaxf;

import java.util.Arrays;
import java.util.logging.Logger;

import lib.LoggingConfigurator;
import lib.Problems;
import lib.Reflections;
import m09uf2.prob.minmaxf.MinMaxSolver;

public class MinMaxRun {
	
	static final Logger LOGGER = Logger.getLogger(MinMaxRun.class.getName());
	
	static final int MOSTRA = 5000; // mida de la mostra
	
	/*
	 * resultat per al SEED i una mostra de 5000:
	 * min = -403.1524998956356, max = 336.82882155765856
	 */
	
	public static void main(String[] args) {
		
		LoggingConfigurator.configure();
		
		test(false);
	}

	public static void test(boolean ambDelay) {
		
		MinMaxCalc calc = ambDelay? new MinMaxCalc() : new MinMaxCalc(0);
		double[] nums = calc.random(MOSTRA);
		
		MinMaxSolver single = new MinMaxSingleSolver(calc);
		LOGGER.info("==> SINGLE: " + Arrays.toString(single.minMax(nums)));
		
		MinMaxSolver multi = newInstance(calc);
		LOGGER.info("==> MULTI:  " + Arrays.toString(multi.minMax(nums)));
	}
	
	private static MinMaxSolver newInstance(MinMaxCalc calc) {
		
		String packageName = Problems.getImplPackage(MinMaxSolver.class);
		return Reflections.newInstanceOfType(
				MinMaxSolver.class, packageName + ".MinMaxSolverImpl",
				new Class[] {MinMaxCalc.class}, 
				new Object[] {calc});
	}
	
	static class MinMaxSingleSolver implements MinMaxSolver {
		
		private MinMaxCalc calc;

		public MinMaxSingleSolver(MinMaxCalc calc) {
			this.calc = calc;
		}

		@Override
		public double[] minMax(double[] nums) {
			return new double[] {
				calc.min(nums, 0, nums.length),
				calc.max(nums, 0, nums.length)
			};
		}
		
	}
}

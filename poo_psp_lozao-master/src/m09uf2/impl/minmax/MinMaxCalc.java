package m09uf2.impl.minmax;

import java.util.Random;

import lib.Threads;

public class MinMaxCalc {
	
	static final long DEF_SPENDMILLIS = 5;
	static final long SEED = 237458237656324L;
	
	private long spendMillis;
	
	public MinMaxCalc() {
		this(DEF_SPENDMILLIS);
	}
	
	public MinMaxCalc(long spendMillis) {
		this.spendMillis = spendMillis;
	}

	public double[] random(int num) {

		Random r = new Random(SEED);

		double[] nums = new double[num];
		for (int i = 0; i < num; i++) {
			nums[i] = r.nextGaussian() * 100;
		}

		return nums;
	}

	public double min(double[] nums, int from, int to) {

		double min = Double.MAX_VALUE;

		for (int i = from; i < to; i++) {

			if (compare(nums[i], min) < 0) {
				min = nums[i];
			}
		}

		return min;
	}

	public double max(double[] nums, int from, int to) {

		double max = Double.MIN_VALUE;

		for (int i = from; i < to; i++) {

			if (compare(nums[i], max) > 0) {
				max = nums[i];
			}
		}

		return max;
	}

	public int compare(double a, double b) {

		Threads.spend(spendMillis);
		return Double.compare(a, b);
	}
}

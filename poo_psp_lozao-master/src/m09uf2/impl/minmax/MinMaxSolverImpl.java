package m09uf2.impl.minmax;

import java.util.ArrayList;

import m09uf2.prob.minmax.MinMaxSolver;

public class MinMaxSolverImpl implements MinMaxSolver {
	private MinMaxCalc calc;
	private static int numHilos;
	private double[] fragmentoArray = {};
	private ArrayList<Double> listaFragmentos = new ArrayList<Double>();
	private Runnable runnable;

	public MinMaxSolverImpl(MinMaxCalc calc) {
		super();
		this.calc = calc;
		this.numHilos = Runtime.getRuntime().availableProcessors();

	}

	@Override
	public double[] minMax(double[] nums) {

		Thread[] threads = new Thread[numHilos];
		int medida = nums.length;

		if (medida == 1) {
			numHilos = 1;
		}
		if (medida % 2 != 0) {
			numHilos = 3;
		}

		int frag = medida / numHilos;

		for (int i = 0; i < numHilos; i++) {

			double fg = i * frag;
			double[] mm = new double[frag];

			for (int j = 0; j < frag; j++) {

				double n = nums[(int) fg + j];
				mm[j] = n;

			}
			fragmentoArray = mm;
			runnable = new MinMaxRunnable(this, fragmentoArray);
			threads[i] = new Thread(runnable, "fil-" + i);
			threads[i].start();
		}

		for (int i = 0; i < numHilos; i++) {
			try {
				threads[i].join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		double[] fin = new double[listaFragmentos.size()];

		for (int i = 0; i < listaFragmentos.size(); i++) {

			double nm = listaFragmentos.get(i);
			fin[i] = nm;

		}
//		System.out.println(fin.length);

		return new double[] { calc.min(fin, 0, fin.length), calc.max(fin, 0, fin.length) };
	}

	public synchronized void listaSemiFinal(double numero) {
		listaFragmentos.add(numero);
	}

}

package m09uf2.impl.minmax;

import java.util.ArrayList;

public class MinMaxRunnable implements Runnable {
	double[] fragmentoArray= {};
	double[] numerosFinales= {};
	private MinMaxCalc calc;
	private MinMaxSolverImpl minMaxSolverImpl;
	private ArrayList<Double> listaFragmentos = new ArrayList<Double>();
	public MinMaxRunnable(MinMaxSolverImpl minMaxSolverImpl, double[] fragmentoArray) {
		super();
		this.minMaxSolverImpl=minMaxSolverImpl;
		this.fragmentoArray = fragmentoArray;
		calc=new MinMaxCalc();
	
	}	


	@Override
	public void run() {
		

			double[] calculo = { 
					calc.min(fragmentoArray, 0, fragmentoArray.length),
					calc.max(fragmentoArray, 0, fragmentoArray.length) };
			
//			System.out.println(calculo[0]+" min ------ max "+calculo[1]+"------------thread");
//			listaFragmentos.add(calculo[0]);
//			listaFragmentos.add(calculo[1]);
//			
			minMaxSolverImpl.listaSemiFinal(calculo[0]);
			minMaxSolverImpl.listaSemiFinal(calculo[1]);
		
			

		

//	System.out.println(listaFragmentos);
		
	}

}

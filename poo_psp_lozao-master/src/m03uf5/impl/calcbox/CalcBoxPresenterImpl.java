package m03uf5.impl.calcbox;

import java.io.IOException;

import m03uf5.impl.calcbox.CalcBoxContract.CalcBoxPresenter;
import m03uf5.impl.calcbox.CalcBoxContract.CalcBoxView;
import m03uf5.prob.calcbox.ExprAvaluator;

public class CalcBoxPresenterImpl implements CalcBoxPresenter {

	private CalcBoxView view;
	private ExprAvaluator model;
	
	

	public CalcBoxPresenterImpl() {
		model = new ExprAvaluator();
	}

	@Override
	public void setView(CalcBoxView v) {

		this.view = v;

	}

	@Override
	public void calcula(String expressio) {
		try {

			char primerCaracter= expressio.charAt(0);
			char ultimoCaracter = expressio.charAt(expressio.length()-1);
			
			if(primerCaracter!=1||primerCaracter!=2||primerCaracter!=3||primerCaracter!=4||primerCaracter!=5||primerCaracter!=6||primerCaracter!=7||primerCaracter!=8||primerCaracter!=9||primerCaracter!=0) {
				view.mostra("Expressio incorrecta");
			}
			
			if(ultimoCaracter!=1||ultimoCaracter!=2||ultimoCaracter!=3||ultimoCaracter!=4||ultimoCaracter!=5||ultimoCaracter!=6||ultimoCaracter!=7||ultimoCaracter!=8||ultimoCaracter!=9||ultimoCaracter!=0) {
				view.mostra("Expressio incorrecta");
			}
			
			
			if(expressio.isEmpty()) {
				view.mostra("0");
			}
			
			if(expressio.matches(".*[a-z].*")) {
				view.mostra("La operacion contiene un caracter");	
			}
			
			double result = model.avalua(expressio);
			
			if (result == (long) result) {
				view.mostra(Long.toString((long) result));
			}
			else {
				
				view.mostra(Double.toString(result));
			}
			
			
			
		
			

		} catch (IOException e) {

			e.printStackTrace();
		}

	}

}

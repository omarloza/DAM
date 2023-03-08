package m03uf5.impl.exam1b;

import m03uf5.prob.exam1b.Proveidor;
import m03uf5.prob.exam1b.ProveidorFactory;

public class ProveidorFactoryImpl<T> implements ProveidorFactory<T>{

	@Override
	public Proveidor<T> create(T producte, int estocInicial) {
		
		return  new ProveidorImpl<T>(producte, estocInicial);
	}


}

package m03uf5.impl.exam1b;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import m03uf5.prob.exam1b.Comanda;
import m03uf5.prob.exam1b.Establiment;
import m03uf5.prob.exam1b.Proveidor;

public class EstablimentImpl<T> implements Establiment<T> {
	private int estoc = 0;
	private Map<T, Proveidor<T>> producteProveidor;
	private Map<Proveidor<T>, Integer> proveidorEstoc;
	private List<Comanda<T>> noProcesados;

	public EstablimentImpl() {
		producteProveidor = new HashMap<T, Proveidor<T>>();
		noProcesados = new ArrayList<Comanda<T>>();

		proveidorEstoc = new HashMap<Proveidor<T>, Integer>();
	}

	@Override
	public void setProveidor(Proveidor<T> p) {

		T producto = p.getProducte();
		producteProveidor.put(producto, p);

		Integer estocProv = p.getEstoc();
		proveidorEstoc.put(p, estocProv);
		


	}

	@Override
	public Proveidor<T> getProveidor(T t) {

		for (Proveidor<T> b : producteProveidor.values()) {

			if (b.equals(t)) {
				return b;
			}

		}

		return null;
	}

	@Override
	public int getEstoc(T t) {

		return estoc;
	}

	@Override
	public List<Comanda<T>> processar(List<Comanda<T>> comandes) {

		
		for (Comanda<T> c : comandes) {
		
			for (Proveidor<T> p : proveidorEstoc.keySet()) {
				
				if (c.getProducte().equals(p.getProducte())) {
					
					if(p.subministrar(c.getQuantitat())>0) {
						p.subministrar(c.getQuantitat());
						
						estoc=c.getQuantitat();
						
						int provEstoc=p.getEstoc();
						estoc=estoc+provEstoc;
						
					}else {
//						noProcesados.add(c);
					}
				}
	
			}
			
	

		}

		return noProcesados;
	}

}

package m03uf5.impl.arbreg.run;

import m03uf5.prob.arbreg.Parella;

public class ParellaImpl<E,D>  implements Parella<E,D> {
	
	private E esquerra;
	private D dreta;
	
	public ParellaImpl(E esquerra, D dreta) {
		this.esquerra = esquerra;
		this.dreta = dreta;
	}

	@Override
	public E getEsquerra() {
		
	
		return esquerra;
	}

	@Override
	public D getDreta() {

		return dreta;
	}

	

	@Override
	public String toString() {
		return "[E=" + esquerra+ ", D=" + dreta + "]";
	}

	
}


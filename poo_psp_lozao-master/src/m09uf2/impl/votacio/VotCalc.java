package m09uf2.impl.votacio;

import lib.Threads;

public class VotCalc {

	private long millis;

	public VotCalc(long millis) {
		this.millis = millis;
	}
	
	public int increment(int valor) {		
		Threads.spend(millis);
		return valor + 1;
	}
}

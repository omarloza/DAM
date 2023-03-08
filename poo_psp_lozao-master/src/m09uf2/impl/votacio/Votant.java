package m09uf2.impl.votacio;

import m09uf2.prob.votacio.LocalElectoral;

public class Votant implements Runnable {

	private long id;
	private LocalElectoral le;
	private int candidat;

	public Votant(long id, int candidat, LocalElectoral le) {
		this.id = id;
		this.candidat = candidat;
		this.le = le;
	}
	
	@Override
	public void run() {
		le.entrada();
		int mesa = le.findMesa(id);
		le.votar(id, mesa, candidat);
		le.sortida();
	}
}

package m09uf2.impl.votacio;

import java.util.Map;

import m09uf2.prob.votacio.LocalElectoral;

public class LocalElectoralImpl implements LocalElectoral {

	private VotCalc votcalc; /* una clculadora que permet incrementar el vot a una mesa. */
	private int numMeses; /* : nombre de taules de votació. */
	private int numCandidats; /*
								 * : nombre de candidats polítics que es poden votar (vot de zero a
								 * numCandidats-1).
								 */
	private int presents = 0;
	private int[][] votacion;
	private int[] resultado;

	public LocalElectoralImpl(VotCalc votcalc, int numMeses, int numCandidats) {
		super();
		this.votcalc = votcalc;
		this.numMeses = numMeses;
		this.numCandidats = numCandidats;

		votacion = new int[numMeses][numCandidats];
		resultado = new int[numCandidats];

	}

	@Override
	public int findMesa(long idVotant) {

		return (int) (idVotant % numMeses);
	}

	@Override
	public boolean votar(long idVotant, int mesa, int candidat) {

		synchronized (votacion[mesa]) {
			if (presents < numMeses || candidat <= numCandidats - 1 && candidat >= 0 && mesa == findMesa(idVotant)) {

				votacion[mesa][candidat] = votcalc.increment(votacion[mesa][candidat]);

				return true;
			} else {
				return false;
			}
		}

	}

	@Override
	public synchronized void entrada() {

		this.presents++;

	}

	@Override
	public synchronized void sortida() {
		this.presents--;

	}

	@Override
	public synchronized int presents() {

		return this.presents;
	}

	@Override
	public synchronized int[] resultats() {
		int suma = 0;
		for (int i = 0; i < numCandidats; i++) {
			for (int j = 0; j < votacion.length; j++) {
				suma += votacion[j][i];
			}
//	            System.out.println("Columna " + i + " =" + suma);
			resultado[i] = suma;

			suma = 0;
		}
		return resultado;
	}

}

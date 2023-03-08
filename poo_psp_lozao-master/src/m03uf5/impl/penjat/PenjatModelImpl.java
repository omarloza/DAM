package m03uf5.impl.penjat;

import java.util.ArrayList;
import m03uf5.impl.penjat.PenjatContract.PenjatModel;

public class PenjatModelImpl implements PenjatModel {
	private String fraseRandom = "";
	private String letraDescubiertas = "";
	private String fraseconguiones = "";
	private ArrayList<String> letrasParaEncontrar = new ArrayList<String>();

	public int numeroPartida = 0;
	private int turno = 1;
	private int jdr1 = 0;
	private int jdr2 = 0;
	private boolean enPartida = true;

	@Override
	public String obtenerPartidas() {

		return "Marcador: Jugador 1 [" + jdr1 + " , " + jdr2 + "] Jugador2";
	}

	@Override
	public String jugar(String palabra) {

		fraseRandom = palabra;

		fraseconguiones = palabra.replaceAll("[^ ,.!?¿:;¡]", "_");

		for (int i = 0; i < palabra.length(); i++) {

			char caracter = palabra.charAt(i);
			String letra = Character.toString(caracter);
			if (Character.isLetter(caracter)) {
				letrasParaEncontrar.add(letra);
			}

			letrasParaEncontrar.remove(" ");

		}

		return fraseconguiones;

	}

	@Override
	public String comprobar(String letra) {
//		System.out.println(letra);

		letraDescubiertas = letraDescubiertas + letra;
//		System.out.println(letraDescubiertas);

		System.out.println(turno + " turno");

//		System.out.println(letraDescubiertas);

		if (letrasParaEncontrar.contains(letra)) {
			if (letraDescubiertas.contains("A")) {
				letraDescubiertas += "AÁÀ";
			}

			if (letraDescubiertas.contains("E")) {
				letraDescubiertas += "EÈÉ";
			}

			if (letraDescubiertas.contains("I")) {
				letraDescubiertas += "IÌÍ";
			}

			if (letraDescubiertas.contains("O")) {
				letraDescubiertas += "OÓÒ";
			}

			if (letraDescubiertas.contains("U")) {
				letraDescubiertas += "UÚÙÜ";
			}

			fraseconguiones = fraseRandom.replaceAll("[^,.!?¿:;¡" + letraDescubiertas + " ]", "_");
			System.out.println(fraseconguiones);
//			System.out.println(letraDescubiertas);

			if (!fraseconguiones.contains("_")) {
				numeroPartida++;
				if (turno % 2 == 0) {

					System.out.println("GANO EL JUGADOR 2");
					jdr2++;
				} else {
					System.out.println("GANO EL JUGADOR 1");
					jdr1++;
				}
				System.out.println("[" + jdr1 + "," + jdr2 + "]");
				enPartida = false;
			}

		} else {
			turno++;
		}

		return fraseconguiones;

	}

	@Override
	public int getTurno() {
		return turno;
	}

	@Override
	public void setTurno(int turno) {
		this.turno = turno;
	}

	@Override
	public int getJdr1() {
		return jdr1;
	}

	@Override
	public void setJdr1(int jdr1) {
		this.jdr1 = jdr1;
	}

	@Override
	public int getJdr2() {
		return jdr2;
	}

	@Override
	public void setJdr2(int jdr2) {
		this.jdr2 = jdr2;
	}

	@Override
	public boolean isEnPartida() {
		return enPartida;
	}

	@Override
	public void setEnPartida(boolean enPartida) {
		this.enPartida = enPartida;
	}

	@Override
	public String getFraseRandom() {
		return fraseRandom;
	}

	@Override
	public void setFraseRandom(String fraseRandom) {
		this.fraseRandom = fraseRandom;
	}

	@Override
	public String getLetraDescubiertas() {
		return letraDescubiertas;
	}

	@Override
	public void setLetraDescubiertas(String letraDescubiertas) {
		this.letraDescubiertas = letraDescubiertas;
	}

	@Override
	public String getFraseconguiones() {
		return fraseconguiones;
	}

	@Override
	public void setFraseconguiones(String fraseconguiones) {
		this.fraseconguiones = fraseconguiones;
	}

	@Override
	public ArrayList<String> getLetrasParaEncontrar() {
		return letrasParaEncontrar;
	}

	@Override
	public void setLetrasParaEncontrar(ArrayList<String> letrasParaEncontrar) {
		this.letrasParaEncontrar = letrasParaEncontrar;
	}

}

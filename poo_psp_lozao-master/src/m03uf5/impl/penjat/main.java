package m03uf5.impl.penjat;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import m03uf5.impl.penjat.PenjatContract.PenjatModel;

public class main {

	private static String fraseRandom = "";
	static String letraDescubiertas = "";
	static String fraseconguiones = "";
	static ArrayList<String> letrasParaEncontrar = new ArrayList<String>();

	private static int turno = 1;
	private static int jdr1 = 0;
	private static int jdr2 = 0;

	private static boolean enPartida = true;

	private final static String[] TEXTS = new String[] { "�Quieres el bien? Haz el bien y todo vendr�.",
			"Habla menos y observa m�s.", "Toma este d�a para sonre�r.", "Qui�rete �Es gratis!",
			"Que tu fe sea mayor que tus problemas.", "S� feliz, no aceptes menos.", "Es bueno ser bueno para alguien.",
			"La medida del amor es amar sin medida.", "Las cosas buenas toman tiempo.", "Llora litros, sonr�e a mares.",
			"El cuerpo logra lo que la mente cree.", "Vivir es dibujar sin goma.",
			"La gente sue�a toda su vida y s�lo despierta al final.",
			"La sonrisa es la mejor respuesta para una mirada.", "Trae tu propio sol.",
			"La alegr�a es contagiosa. P�salo.", "La esperanza es lo �ltimo que se pierde.",
			"La energ�a que desprendes es todo lo que t� eres.", "Es elegante ser buena persona.",
			"S� la raz�n de que alguien sonr�a hoy.", "Escucha a tu alma.", "Todo lo que necesitas es amor." };

	public static void main(String[] args) {
		fraseRandom = seleccionarfraseAleatoria();
		System.out.println(fraseRandom);
		jugar(fraseRandom);
		System.out.println(fraseconguiones);
		Scanner sc = new Scanner(System.in);

		while (enPartida = true) {

			String letra = "";
			letra += sc.nextLine();

			fraseconguiones = comprobar(letra);
			System.out.println(fraseconguiones);

		}

	}

	public static String jugar(String palabra) {

		fraseconguiones = palabra.replaceAll("[^ ,.!?�:;�]", " _ ");

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

	public static String comprobar(String letra) {

		letraDescubiertas = letraDescubiertas + letra;
		System.out.println(turno + " turno");

//		System.out.println(letraDescubiertas);

		if (letrasParaEncontrar.contains(letra)) {
			if (letraDescubiertas.contains("A")) {
				letraDescubiertas += "��";
			}

			if (letraDescubiertas.contains("E")) {
				letraDescubiertas += "��";
			}

			if (letraDescubiertas.contains("I")) {
				letraDescubiertas += "��";
			}

			if (letraDescubiertas.contains("O")) {
				letraDescubiertas += "��";
			}

			if (letraDescubiertas.contains("U")) {
				letraDescubiertas += "��";
			}

			fraseconguiones = fraseRandom.replaceAll("[^,.!?�:;�" + letraDescubiertas + " ]", " _ ");

			System.out.println(letraDescubiertas);

			if (!fraseconguiones.contains("_")) {

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

	public static String seleccionarfraseAleatoria() {

		Random r = new Random();
		int valorDado = r.nextInt(TEXTS.length);
		return fraseRandom = TEXTS[valorDado].toUpperCase();

	}

}

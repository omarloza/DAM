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

	private final static String[] TEXTS = new String[] { "¿Quieres el bien? Haz el bien y todo vendrá.",
			"Habla menos y observa más.", "Toma este día para sonreír.", "Quiérete ¡Es gratis!",
			"Que tu fe sea mayor que tus problemas.", "Sé feliz, no aceptes menos.", "Es bueno ser bueno para alguien.",
			"La medida del amor es amar sin medida.", "Las cosas buenas toman tiempo.", "Llora litros, sonríe a mares.",
			"El cuerpo logra lo que la mente cree.", "Vivir es dibujar sin goma.",
			"La gente sueña toda su vida y sólo despierta al final.",
			"La sonrisa es la mejor respuesta para una mirada.", "Trae tu propio sol.",
			"La alegría es contagiosa. Pásalo.", "La esperanza es lo último que se pierde.",
			"La energía que desprendes es todo lo que tú eres.", "Es elegante ser buena persona.",
			"Sé la razón de que alguien sonría hoy.", "Escucha a tu alma.", "Todo lo que necesitas es amor." };

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

		fraseconguiones = palabra.replaceAll("[^ ,.!?¿:;¡]", " _ ");

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
				letraDescubiertas += "ÁÀ";
			}

			if (letraDescubiertas.contains("E")) {
				letraDescubiertas += "ÈÉ";
			}

			if (letraDescubiertas.contains("I")) {
				letraDescubiertas += "ÌÍ";
			}

			if (letraDescubiertas.contains("O")) {
				letraDescubiertas += "ÓÒ";
			}

			if (letraDescubiertas.contains("U")) {
				letraDescubiertas += "ÚÙ";
			}

			fraseconguiones = fraseRandom.replaceAll("[^,.!?¿:;¡" + letraDescubiertas + " ]", " _ ");

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

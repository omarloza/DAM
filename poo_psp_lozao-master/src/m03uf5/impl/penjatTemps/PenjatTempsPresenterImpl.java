package m03uf5.impl.penjatTemps;

import java.util.ArrayList;
import java.util.Random;


import m03uf5.impl.penjatTemps.PenjatTempsContract.PenjatTempsModel;
import m03uf5.impl.penjatTemps.PenjatTempsContract.PenjatTempsPresenter;
import m03uf5.impl.penjatTemps.PenjatTempsContract.PenjatTempsView;

public class PenjatTempsPresenterImpl implements  PenjatTempsPresenter{

	private PenjatTempsView view;
	private PenjatTempsModel model;

	private String fraseRandom = "";
	private String fraseconguiones = "";

	private int turno = 0;
	boolean enPartida;

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

	@Override
	public void setView(PenjatTempsView v) {

		this.view = v;
	}

	@Override
	public void setModel(PenjatTempsModel m) {
		this.model = m;
	}

	@Override
	public void buttonClicked(String letra) {


		enPartida = model.isEnPartida();

		if (enPartida = true) {
			fraseconguiones = model.comprobar(letra);
			System.out.println(fraseconguiones);
			view.mostraComprovaciones(fraseconguiones);

			turno = model.getTurno();
			if (turno % 2 == 0) {
				String turn = "Turno: Jugador 2";
				view.mostraTurno(turn);

			} else {
				String turn = "Turno: Jugador 1";
				view.mostraTurno(turn);

			}

			String resultados = model.obtenerPartidas();
			view.mostraResultado(resultados);

			
			if(!fraseconguiones.contains("_")) {
				if (turno % 2 == 0) {
					view.mostraTurno("Gana el jugador 2");

				} else {

					view.mostraTurno("Gana el jugador 1");

				}
			}
		}

	}

	public String seleccionarfraseAleatoria() {

		Random r = new Random();
		int valorDado = r.nextInt(TEXTS.length);
		return fraseRandom = TEXTS[valorDado].toUpperCase();

	}

	@Override
	public void comenzar() {

		fraseRandom = seleccionarfraseAleatoria();
		view.mostra(model.jugar(fraseRandom));

	}

	@Override
	public void reiniciar() {
		model.setFraseconguiones("");
		model.setFraseRandom("");
		model.setLetraDescubiertas("");
		model.setLetrasParaEncontrar(new ArrayList<String>());
		model.setTurno(1);

		comenzar();
		this.fraseRandom = "";
		this.fraseconguiones = "";
		this.turno = 0;

		view.mostra("Presiona Comenzar");
		view.mostraTurno("Turno : Jugador 1");
	}

	@Override
	public void pasarTurno() {
		turno = model.getTurno();
		turno++;
		if (turno % 2 == 0) {
			String turn = "Turno: Jugador 2";
			view.mostraTurno(turn);

		} else {
			String turn = "Turno: Jugador 1";
			view.mostraTurno(turn);

		}
		
	}

}

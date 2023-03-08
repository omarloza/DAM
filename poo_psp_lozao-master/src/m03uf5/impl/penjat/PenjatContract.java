package m03uf5.impl.penjat;

import java.util.ArrayList;

public interface PenjatContract {
	interface PenjatView {

		void setPresenter(PenjatPresenter p);

		void mostra(String resultat);

		void mostraTurno(String resultat);

		void mostraResultado(String resultat);

		void mostraComprovaciones(String resultat);

		void tornarAJugar();
	}

	interface PenjatPresenter {

		void setView(PenjatView v);

		void setModel(PenjatModel m);

		void buttonClicked(String letra);

		void comenzar();

		void reiniciar();

	}

	interface PenjatModel {

		String jugar(String palabra);

		String comprobar(String letra);

		int getTurno();

		void setTurno(int turno);

		int getJdr1();

		void setJdr1(int jdr1);

		int getJdr2();

		void setJdr2(int jdr2);

		boolean isEnPartida();

		void setEnPartida(boolean enPartida);

		String getFraseRandom();

		void setFraseRandom(String fraseRandom);

		String getLetraDescubiertas();

		void setLetraDescubiertas(String letraDescubiertas);

		String getFraseconguiones();

		void setFraseconguiones(String fraseconguiones);

		ArrayList<String> getLetrasParaEncontrar();

		void setLetrasParaEncontrar(ArrayList<String> letrasParaEncontrar);

		String obtenerPartidas();

	}

}

package m03uf5.impl.exam2b;

import java.util.ArrayList;


public interface CalcHistContract {

	interface CalcHistView {
		
		/**
		 * Indica a la view quin es el seu presenter
		 * @param p
		 */
		void setPresenter(CalcHistPresenter p);
		
		/**
		 * Cridat pel presenter, per mostrar el resultat al label
		 * @param resultat
		 */
		void mostra(String resultat);

		void mostraHistorial(String resultat);


	}
	
	interface CalcHistPresenter {
		
		/**
		 * Indica al presenter quina es la seva view
		 * @param v
		 */
		void setView(CalcHistView v);

		/**
		 * Cridat per la view, per avaluar l'expressió
		 * @param expressio
		 */
		void calcula(String expressio);

		void anteriorYsiguiente(String expr);



		ArrayList<String> getHistorial();
	}
}

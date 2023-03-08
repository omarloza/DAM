package m03uf5.impl.calcbox;

public interface CalcBoxContract {

	interface CalcBoxView {
		
		/**
		 * Indica a la view quin es el seu presenter
		 * @param p
		 */
		void setPresenter(CalcBoxPresenter p);
		
		/**
		 * Cridat pel presenter, per mostrar el resultat al label
		 * @param resultat
		 */
		void mostra(String resultat);
	}
	
	interface CalcBoxPresenter {
		
		/**
		 * Indica al presenter quina es la seva view
		 * @param v
		 */
		void setView(CalcBoxView v);

		/**
		 * Cridat per la view, per avaluar l'expressió
		 * @param expressio
		 */
		void calcula(String expressio);
	}
}

package m03uf5.impl.exam1b;

public class Comanda<T> {
	
	private int quantitat;
	private T producte;

	public Comanda(T producte, int quantitat) {
		this.producte = producte;
		this.quantitat = quantitat;
	}

	public int getQuantitat() {
		return quantitat;
	}

	public T getProducte() {
		return producte;
	}

	@Override
	public String toString() {
		return "Comanda [quantitat=" + quantitat + ", producte=" + producte + "]";
	}
}

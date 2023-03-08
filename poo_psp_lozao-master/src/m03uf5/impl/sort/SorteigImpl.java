package m03uf5.impl.sort;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import m03uf5.prob.sort.GeneradorBolets;
import m03uf5.prob.sort.GeneradorPremis;
import m03uf5.prob.sort.Sorteig;

public class SorteigImpl<K, V> implements Sorteig<K, V> {

	private Map<K, V> map;
	private Queue<K> cua;
	private Map<V, Boolean> pbolets;

	private int numPremi;
	private GeneradorPremis<V> genpremi;
	private GeneradorBolets<K, V> genbolet;

	public SorteigImpl() {

		this.map = new HashMap<>();
		this.cua = new LinkedList<>();
	}

	@Override
	public void setNumPremis(int numPremis) {
		numPremi = numPremis;
	}

	@Override
	public void setGenPremis(GeneradorPremis<V> genpremis) {
		genpremi = genpremis;
	}

	@Override
	public void setGenBolets(GeneradorBolets<K, V> genbolets) {
		genbolet = genbolets;
	}

	@Override
	public V afegir(K concursant) {

		V valor = genbolet.genera(concursant);
		map.put(concursant, valor);
		cua.add(concursant);
		return valor;
	}

	@Override
	public Queue<K> concursants() {
		return cua;
	}

	@Override
	public Set<K> sortejar() {

		Set<V> numerosPremiados = new HashSet<V>();
		Set<K> clauPremiat = new HashSet<K>();
		pbolets = new HashMap<V, Boolean>();

		if (numPremi > 10) {
			System.out.println("ERROR: Has intentat generar " + numPremi
					+ " numeros premiats i aquest sorteig esta disenyat per donar un maxim de 10 premis");
			System.exit(0);
		}

		for (int i = 0; i <= numPremi; i++) {
			V numeroPremiat = genpremi.genera();
			numerosPremiados.add(numeroPremiat);

		}

		while (numerosPremiados.size() != numPremi) {
			V numeroPremiat = genpremi.genera();
			numerosPremiados.add(numeroPremiat);
		}

		int cola = cua.size();
		for (int i = 0; i <= cola; i++) {

			if (!cua.isEmpty()) {
				K clau = cua.remove();
				V bolet = map.get(clau);

				if (numerosPremiados.remove(bolet)) {
					clauPremiat.add(clau);
					pbolets.put(bolet, true);
				}

			}

		}

		for (V bolet : numerosPremiados) {
			pbolets.put(bolet, false);
		}

		return clauPremiat;
	}

	@Override
	public Map<V, Boolean> premiats() {

		return pbolets;
	}

}

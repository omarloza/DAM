package m03uf5.impl.sort;

import java.util.Map;
import java.util.Random;
import java.util.Set;

import lib.Problems;
import lib.Reflections;
import m03uf5.prob.sort.GeneradorBolets;
import m03uf5.prob.sort.GeneradorPremis;
import m03uf5.prob.sort.Sorteig;

/*
 * Aquesta és una possible sortida del programa:
 * 
nombre de premis/participants: 10/20
12275460X participa amb 0
28856161W participa amb 1
11992127H participa amb 7
6225439E participa amb 9
63613575S participa amb 5
100020220X participa amb 0
64813414O participa amb 4
87788878F participa amb 8
91456395S participa amb 5
59039255O participa amb 5
47182554Q participa amb 4
25383011D participa amb 1
68680094R participa amb 4
19944647I participa amb 7
84423642W participa amb 2
60101897S participa amb 7
9896125I participa amb 5
34832094B participa amb 4
68463411C participa amb 1
11072718Q participa amb 8
concursants: [12275460X, 28856161W, 11992127H, 6225439E, 63613575S, 100020220X, 64813414O, 87788878F, 91456395S, 59039255O, 47182554Q, 25383011D, 68680094R, 19944647I, 84423642W, 60101897S, 9896125I, 34832094B, 68463411C, 11072718Q]
guanyadors: [12275460X, 28856161W, 11992127H, 6225439E, 64813414O, 63613575S, 87788878F, 84423642W]
bolets premiats: {0=true, 1=true, 2=true, 3=false, 4=true, 5=true, 6=false, 7=true, 8=true, 9=true}
 *
 */
public class SortRun {
	
	public static void main(String[] args) {
		
		test(8, 20);
	}

	public static void test(int numPremis, int numConcursants) {

		System.out.println("nombre de premis/participants: " + numPremis + "/" + numConcursants);
		
		Random r = new Random();

		GeneradorPremis<Integer> genpremis = new GeneradorPremis<Integer>() {

			@Override
			public Integer genera() {
				return r.nextInt(10);
			}
		};

		GeneradorBolets<String, Integer> genbolets = new GeneradorBolets<String, Integer>() {

			@Override
			public Integer genera(String clau) {

				int dni = Integer.parseInt(clau.substring(0, clau.length() - 1));
				return dni % 10;
			}
		};
		
		String packageName = Problems.getImplPackage(Sorteig.class);	
		
		@SuppressWarnings("unchecked")
		Sorteig<String, Integer> s = Reflections.newInstanceOfType(Sorteig.class, packageName + ".SorteigImpl");

		s.setNumPremis(numPremis);
		s.setGenPremis(genpremis);
		s.setGenBolets(genbolets);

		for (int i = 0; i < numConcursants; i++) {
			char c = (char) (r.nextInt(26) + 'A');
			String dni = Integer.toString(r.nextInt(100000000) + 1000000) + c;
			int bolet = s.afegir(dni);
			System.out.println(dni + " participa amb " + bolet);
		}

		System.out.println("concursants: " + s.concursants());

		Set<String> resultat = s.sortejar();
		System.out.println("guanyadors: " + resultat);

		Map<Integer, Boolean> bolets = s.premiats();
		System.out.println("bolets premiats: " + bolets);
	}

	
}

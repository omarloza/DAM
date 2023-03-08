package m03uf5.impl.multimap;

import lib.Problems;
import lib.Reflections;
import m03uf5.prob.multimap.MultiMap;
import m03uf5.prob.multimap.MyString;
import m03uf5.prob.multimap.MyStringFactory;

public class MultiMapRun {

	/*
	 * sortida esperada:
	 * 
	 * ===> class m03uf5.impl.multimap.MultiSetMapImpl first dos is Dos false true
	 * false true false 1: [U, Uno] 2: [Dos] 3: [Tres, Three] 4: [] 5: [Cinc] 6: []
	 * 2: [Two] ===> class m03uf5.impl.multimap.MultiListMapImpl first dos is Dos
	 * false true false true false 1: [U, Uno] 2: [Dos, Dos] 3: [Tres, Three] 4: []
	 * 5: [Cinc] 6: [] 2: [Two, Dos]
	 * 
	 */

	public static void main(String[] args) {

		String packageName = Problems.getImplPackage(MultiMap.class);

		MyStringFactory msf = Reflections.newInstanceOfType(MyStringFactory.class,
				packageName + ".MyStringFactoryImpl");

		@SuppressWarnings("unchecked")
		MultiMap<Integer, MyString> msm = Reflections.newInstanceOfType(MultiMap.class,
				packageName + ".MultiMapSetImpl");
		testMap(msm, msf);

		@SuppressWarnings("unchecked")
		MultiMap<Integer, MyString> mlm = Reflections.newInstanceOfType(MultiMap.class,
				packageName + ".MultiMapListImpl");
		testMap(mlm, msf);
	}

	public static void testMap(MultiMap<Integer, MyString> mmap, MyStringFactory msf) {

		System.out.println("===> " + mmap.getClass());

		mmap.put(1, msf.create("U"));
		mmap.put(1, msf.create("Uno"));
		mmap.put(2, msf.create("Dos"));
		mmap.put(2, msf.create("Zero"));
		mmap.put(2, msf.create("Dos"));
		mmap.put(3, msf.create("Tres"));

		mmap.get(3).add(msf.create("Three"));
		mmap.get(4).add(msf.create("Zero"));
		mmap.get(5).add(msf.create("Cinc"));

		MyString firstDos = mmap.get(2).iterator().next();
		System.out.println("first dos is " + firstDos);

		for (int key : mmap) {
			System.out.println(mmap.remove(key, msf.create("Zero")));
		}

		for (int key : mmap) {
			System.out.println(key + ": " + mmap.get(key));
		}
		System.out.println("6: " + mmap.get(6));

		firstDos.set("Two");
		System.out.println("2: " + mmap.get(2));
	}
}

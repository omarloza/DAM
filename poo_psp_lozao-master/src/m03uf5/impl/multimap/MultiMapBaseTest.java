package m03uf5.impl.multimap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumingThat;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import lib.Problems;
import lib.Reflections;
import m03uf5.prob.multimap.MultiMap;
import m03uf5.prob.multimap.MyString;
import m03uf5.prob.multimap.MyStringFactory;

@TestInstance(Lifecycle.PER_METHOD) // or PER_CLASS (PER_METHOD is default)
public abstract class MultiMapBaseTest {

	protected MyStringFactory msf;
	protected MultiMap<Integer, MyString> mmap;
	protected boolean isSet;
	
	@SuppressWarnings("unchecked")
	public MultiMapBaseTest(String className, boolean isSet) {

		String packageName = Problems.getImplPackage(MultiMap.class);
		
		msf = Reflections.newInstanceOfType(
				MyStringFactory.class, packageName + ".MyStringFactoryImpl");
		
		mmap = Reflections.newInstanceOfType(
				MultiMap.class, packageName + "." + className);
		
		this.isSet = isSet;
	}
	
	@Test
	public void testMyString() {
		
		MyString one1 = msf.create("One");
		MyString one2 = msf.create("One");
		MyString two = msf.create("Two");
		
		assertNotNull(one1);
		assertNotNull(one2);
		assertNotNull(two);
		
		assertTrue(one1.equals(one2));
		assertFalse(one1.equals(two));
	}
	
	@Test
	public void testOne() {
		
		mmap.put(1, msf.create("One"));
		assertEquals(1, mmap.size());
		
		Collection<MyString> one = mmap.get(1);
		assertNotNull(one);
		assertEquals(1, one.size());
		MyString ms = one.iterator().next();
		assertEquals("One", ms.get());
	}

	@Test
	public void testEmpty() {
		
		Collection<MyString> none = mmap.get(1);
		assertNotNull(none);
		assertEquals(0, none.size());
		assertEquals(1, mmap.size());
		
		none.add(msf.create("One"));
		assertEquals(1, mmap.get(1).size());		
		MyString ms = mmap.get(1).iterator().next();		
		assertEquals("One", ms.get());
	}
	
	@Test
	public void testSize() {
		
		mmap.put(1, msf.create("One"));
		mmap.put(1, msf.create("Two"));
		mmap.put(1, msf.create("Two"));
		
		Collection<MyString> one = mmap.get(1);
		assertTrue(one.contains(msf.create("One")));
		assertTrue(one.contains(msf.create("Two")));
		
		assumingThat(isSet, () -> {
			assertEquals(2, one.size());
		});
		
		assumingThat(!isSet, () -> {
			assertEquals(3, one.size());
		});
	}
	
	@Test
	public void testKeys() {
		
		mmap.put(1, msf.create("One"));
		mmap.put(2, msf.create("Two"));
		mmap.get(3);
		
		Set<Integer> keys = new HashSet<>();
		keys.add(1); keys.add(2); keys.add(3);
		
		for (int key: mmap) {
			assert(keys.remove(key));
		}
		
		assertTrue(keys.isEmpty());
	}
	
	@Test
	public void testRemove() {
		
		MyString one1 = msf.create("One");
		MyString one2 = msf.create("One");
		
		mmap.put(1, one1);
		mmap.put(2, msf.create("Two"));
		
		assertTrue(mmap.remove(1, one2));
		assertFalse(mmap.remove(1, one1));
		assertTrue(mmap.remove(2, msf.create("Two")));
		assertFalse(mmap.remove(2, msf.create("Two")));
	}
}

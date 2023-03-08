package m09uf1.impl.passmgr;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import lib.Problems;
import lib.Reflections;
import lib.db.DuplicateException;
import lib.db.NotFoundException;
import m09uf1.prob.passmgr.PassManager;

public class PassManagerTest {
	
	PassManager pm;

	static PassManager getInstance(JSONObject entries) {
		
		String packageName = Problems.getImplPackage(PassManager.class);
		return Reflections.newInstanceOfType(
				PassManager.class, packageName + ".PassManagerImpl",
				new Class[] {JSONObject.class},
				new Object[] {entries});
	}

	@BeforeEach
	void beforeEach() {
		
		// youruser,yourpassword
		JSONObject entries = new JSONObject()
				.put("youruser", new JSONObject()
						.put("pass", "H5/l8hunXM7ejp93u8X3hwop06pILqniKUR30Q/Oav4=")
						.put("salt", "pQa1lCEhsLu8umgyTlLKHQ=="));
		
		pm = getInstance(entries);
	}
	
	@Test
	void testOk() {		
		try {
			boolean check = pm.checkPassword("youruser", "yourpassword".toCharArray());
			assertTrue(check);
		} catch (NotFoundException e) {
			fail(e);
		}
	}
	
	@Test
	void testNotFound() {		
		try {
			pm.checkPassword("myuser", "mypassword".toCharArray());
			fail("it should fail!");			
		} catch (NotFoundException e) {}
	}
	
	@Test
	void testBadPass() {
		try {
			boolean check = pm.checkPassword("youruser", "badpassword".toCharArray());
			assertFalse(check);
		} catch (NotFoundException e) {
			fail(e);
		}
	}
	
	@Test
	void testAdd() {
		try {
			pm.addEntry("myuser", "mypassword".toCharArray());			
		} catch (DuplicateException e) {
			fail(e);
		}
		
		try {
			boolean check = pm.checkPassword("myuser", "mypassword".toCharArray());
			assertTrue(check);
		} catch (NotFoundException e) {
			fail(e);
		}
	}
	
	@Test
	void testAddExisting() {
		try {
			pm.addEntry("youruser", "newpass".toCharArray());
			fail("it should fail!");			
		} catch (DuplicateException e) {}
	}
	
	@Test
	void testDelete() {		
		try {
			pm.deleteEntry("youruser");			
		} catch (NotFoundException e) {
			fail(e);
		}
		
		try {
			pm.checkPassword("myuser", "mypassword".toCharArray());
			fail("it should fail!");
		} catch (NotFoundException e) {}
	}
	
	@Test
	void testDeleteNonExisting() {
		try {
			pm.deleteEntry("myuser");	
			fail("it should fail!");			
		} catch (NotFoundException e) {}
	}
	
	@Test
	void testSetPassword() {
		try {
			pm.setPassword("youruser", "yourpassword".toCharArray(), "mypassword".toCharArray());
		} catch (NotFoundException e) {
			fail(e);
		}
		
		try {
			boolean check = pm.checkPassword("youruser", "mypassword".toCharArray());
			assertTrue(check);
		} catch (NotFoundException e) {
			fail(e);
		}
	}
	
	@Test
	void testSetMissingUser() {
		try {
			pm.setPassword("myuser", "mypassword".toCharArray(), "yourpassword".toCharArray());
			fail("it should fail!");
		} catch (NotFoundException e) {}
	}
	
	@Test
	void testSetBadPassword() {
		try {
			boolean check = pm.setPassword("youruser", "somepassword".toCharArray(), "mypassword".toCharArray());	
			assertFalse(check);
		} catch (NotFoundException e) {
			fail(e);
		}
		
		try {
			boolean check = pm.checkPassword("youruser", "yourpassword".toCharArray());
			assertTrue(check);
		} catch (NotFoundException e) {
			fail(e);
		}
	}
}

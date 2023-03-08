package m09uf1.impl.safestore;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.UUID;
import java.util.logging.Logger;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import lib.LoggingConfigurator;
import lib.Problems;
import lib.Reflections;
import m09uf1.prob.safestore.SafeStore;
import m09uf1.prob.safestore.WrongKeyException;

public class SafeStoreTest {
	
	static final Logger LOGGER = Logger.getLogger(SafeStoreTest.class.getName());
	
	static final Charset CHARSET = StandardCharsets.UTF_8;
	static final String SS1 = "safestore.txt";
	static final String SS2 = "safestore2.txt";
	static final char[] PASSWORD = "abc123".toCharArray();
	static final char[] PASSWORD2 = "abc234".toCharArray();
	
	private static SafeStore getInstance(String storageName) {
		
		String packageName = Problems.getImplPackage(SafeStore.class);		
		return Reflections.newInstanceOfType(
				SafeStore.class, packageName + ".SafeStoreImpl",
				new Class[] {String.class}, 
				new Object[] {storageName});
	}
	
	@BeforeAll
	static void beforeAll() {		
		LoggingConfigurator.configure();
		getInstance(SS1).getPath().delete();
		getInstance(SS2).getPath().delete();
	}
	
	@Test
	void testInit() {
		SafeStore ss = getInstance(SS1);
		try {
			init(ss, PASSWORD);			
		} catch (IOException e) {
			fail("init storage", e);
		} finally {
			assertTrue(ss.getPath().delete());
		}
	}
	
	@Test
	void testRename() {
		String value = UUID.randomUUID().toString();
		SafeStore ss1 = getInstance(SS1);
		try {
			init(ss1, PASSWORD);	
			ss1.set("key", value);
		} catch (WrongKeyException | IOException e) {
			fail("first storage", e);
		}
		
		SafeStore ss2 = getInstance(SS2);
		assertTrue(ss1.getPath().renameTo(ss2.getPath()));
		try {
			open(ss2, PASSWORD);
			assertEquals(value, ss2.get("key"));
		} catch (WrongKeyException | IOException e) {
			fail("second storage", e);
		} finally {
			assertTrue(ss2.getPath().delete());
		}
	}
	
	@Test
	void testOpen() {
		SafeStore ss = getInstance(SS1);
		try {
			init(ss, PASSWORD);	
			close(ss);
			open(ss, PASSWORD);			
		} catch (IOException e) {
			fail("open storage", e);
		} finally {
			assertTrue(ss.getPath().delete());
		}
	}
	
	@Test
	void testNotFound() {
		SafeStore ss = getInstance(SS1);
		try {	
			ss.open(PASSWORD);
			fail("shoult not open");
		} catch (IOException e) {}
	}
	
	@Test
	void testPassword() {
		SafeStore ss = getInstance(SS1);
		try {
			init(ss, PASSWORD);
			String value = UUID.randomUUID().toString();
			ss.set("key", value);
			close(ss);
			open(ss, PASSWORD);
			ss.setPassword(PASSWORD2);
			close(ss);
			open(ss, PASSWORD2);
			assertEquals(value, ss.get("key"));
			close(ss);
			open(ss, PASSWORD);
			
			try {
				ss.get("key");
				fail("not a bad padding");
			} catch (WrongKeyException t) {}
			
		} catch (WrongKeyException | IOException e) {
			fail("test password", e);
		} finally {
			assertTrue(ss.getPath().delete());
		}
	}
	
	@Test
	void testValues() {
		SafeStore ss = getInstance(SS1);
		try {
			init(ss, PASSWORD);
			String value1 = UUID.randomUUID().toString();
			String value2 = UUID.randomUUID().toString();
			String value3 = UUID.randomUUID().toString();
			ss.set("key1", value1);
			ss.set("key2", value2);
			ss.set("key3", value3);
			ss.set("key3", null);
			assertEquals(ss.get("key1"), value1);
			assertEquals(ss.get("key2"), value2);
			assertEquals(ss.get("key3"), null);
			assertEquals(ss.get("key4"), null);
			
		} catch (WrongKeyException | IOException e) {
			fail("test values", e);
		} finally {
			ss.getPath().delete();
		}
	}
	
	@Test
	void testContent() {
		SafeStore ss = getInstance(SS1);
		try {
			init(ss, PASSWORD);
			String value1 = UUID.randomUUID().toString();
			String value2 = UUID.randomUUID().toString();
			ss.set("key1", value1);
			ss.set("key2", value2);			
			JSONObject json = readToJson(ss.getPath(), CHARSET);
			LOGGER.info(json.toString(4));
			assertEquals(value1, readValue(json, "key1"));
			assertEquals(value2, readValue(json, "key2"));
			
		} catch (WrongKeyException | IOException e) {
			fail("test content", e);
		} finally {
			ss.getPath().delete();
		}
	}
	
	// HELPERS
	
	static SecretKey createKEK(char[] pass, byte[] salt) {
		
		try {
			SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
			KeySpec ks = new PBEKeySpec(pass, salt, 16384, 256);
			SecretKey s = f.generateSecret(ks);
			return new SecretKeySpec(s.getEncoded(), "AES");
			
		} catch (GeneralSecurityException e) {
			return fail("creating kek", e);
		}
	}
	
	static SecretKey readDEK(JSONObject jo) {
		
		byte[] cipDek = b64dec(jo.getString("DEK"));
		byte[] salt = b64dec(jo.getString("SALT"));
		byte[] kiv = b64dec(jo.getString("KIV"));		
		SecretKey kek = createKEK(PASSWORD, salt);
		
		byte[] dekBytes = decipher(kek, kiv, cipDek);
		return new SecretKeySpec(dekBytes, "AES");
	}
	
	static String readValue(JSONObject jo, String key) {
		
		SecretKey dek = readDEK(jo);
		
		byte[] cipDek = b64dec(jo.getString(key));
		byte[] div = b64dec(jo.getString("DIV"));		
		
		byte[] dekBytes = decipher(dek, div, cipDek);
		return new String(dekBytes, CHARSET);
	}
	
	static byte[] decipher(SecretKey key, byte[] iv, byte[] bytes) {
		try {
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			IvParameterSpec ivspec = new IvParameterSpec(iv);
			cipher.init(Cipher.DECRYPT_MODE, key, ivspec);
			return cipher.doFinal(bytes);
		} catch (GeneralSecurityException e) {
			return fail("deciphering", e);
		}
	}
			
	static void init(SafeStore ss, char[] pass) throws IOException {		
		assertFalse(ss.getPath().exists());
		assertTrue(ss.isClosed());
		ss.init(pass);
		assertTrue(ss.getPath().exists());
		assertFalse(ss.isClosed());
	}
	
	static void open(SafeStore ss, char[] pass) throws IOException {		
		assertTrue(ss.getPath().exists());
		assertTrue(ss.isClosed());
		ss.open(pass);
		assertFalse(ss.isClosed());
	}
	
	static void close(SafeStore ss) {
		ss.close();
		assertTrue(ss.isClosed());
	}
	
	static JSONObject readToJson(File file, Charset charset) throws IOException {
		
		StringBuilder sb = new StringBuilder();
		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), charset))) {
			String cLine;
			while ((cLine = br.readLine()) != null) {
				sb.append(cLine).append(System.lineSeparator());
			}
		}
		
		return new JSONObject(sb.toString());
	}
	
	static String b64enc(byte[] bytes) {
		return Base64.getEncoder().encodeToString(bytes);
	}
	
	static byte[] b64dec(String b64) {
		return Base64.getDecoder().decode(b64);
	}
	
}

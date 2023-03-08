package m09uf1.impl.safestore;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.logging.Logger;

import org.json.JSONObject;

import lib.LoggingConfigurator;
import lib.Problems;
import lib.Reflections;
import m09uf1.prob.safestore.SafeStore;
import m09uf1.prob.safestore.WrongKeyException;

public class SafeStoreRun {
	
	static final Logger LOGGER = Logger.getLogger(SafeStoreRun.class.getName());

	static final String STORAGENAME = "safestore.txt";
	static final char[] PASSWORD = "abc123".toCharArray();
	static final char[] PASSWORD2 = "abc234".toCharArray();
	
	private static SafeStore getInstance(String storageName) {
		
		String packageName = Problems.getImplPackage(SafeStore.class);		
		return Reflections.newInstanceOfType(
				SafeStore.class, packageName + ".SafeStoreImpl",
				new Class[] {String.class}, 
				new Object[] {storageName});
	}
	
	public static void main(String[] args) throws IOException, WrongKeyException {
		
		LoggingConfigurator.configure();
		
		SafeStore ss = getInstance(STORAGENAME);
		LOGGER.info("storage in " + ss.getPath());
		if (ss.getPath().exists()) {
			ss.getPath().delete();
		}
		
		String value1 = UUID.randomUUID().toString();
		LOGGER.info("value1 is " + value1);
		String value2 = UUID.randomUUID().toString();
		LOGGER.info("value2 is " + value2);
		
		ss.init(PASSWORD);
		ss.set("key1", value1);
		ss.set("key2", value2);		
		ss.close();
		
		LOGGER.info(readToJson(ss.getPath(), StandardCharsets.UTF_8).toString(4));
		
		ss.open(PASSWORD);
		LOGGER.info("key1 is " + ss.get("key1"));
		LOGGER.info("key2 is " + ss.get("key2"));
//		
//		String value3 = UUID.randomUUID().toString();
//		LOGGER.info("value3 is " + value3);
//		ss.set("key2", null);
//		ss.set("key3", value3);
//		ss.close();
//		
//		LOGGER.info(readToJson(ss.getPath(), StandardCharsets.UTF_8).toString(4));
//		
//		ss.open(PASSWORD);
//		LOGGER.info("key2 is " + ss.get("key2"));
//		LOGGER.info("key3 is " + ss.get("key3"));
//		
//		ss.setPassword(PASSWORD2);
//		ss.close();
//		ss.open(PASSWORD2);
//		LOGGER.info("key1 is " + ss.get("key1"));
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
}

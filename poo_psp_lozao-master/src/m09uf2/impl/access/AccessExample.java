package m09uf2.impl.access;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import lib.LoggingConfigurator;

public class AccessExample {
	
	static final Logger LOGGER = Logger.getLogger(AccessExample.class.getName());

	public static void main(String[] args) {
		
		LoggingConfigurator.configure();
		
		access("https://www.google.com");
	}
	
	private static void access(String urlStr) {
		
		long took = -1;
		String server;
		
		try {
			URL url = new URL(urlStr);
			LOGGER.info("submitting " + url);
			long millis = System.currentTimeMillis();
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();				
		    conn.setConnectTimeout(6000);
		    conn.connect();
		    int code = conn.getResponseCode();
		    took = System.currentTimeMillis() - millis;
		    server = conn.getHeaderField("Server");
		    
		    LOGGER.info("code is " + code + ", server is " + server + ", in " + took + " ms");
						
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "submitting " + urlStr, e);
		}
	}
}

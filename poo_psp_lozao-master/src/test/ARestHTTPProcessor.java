package test;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONObject;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;

import lib.LoggingConfigurator;
import lib.Threads;
import lib.http.HTTPProcessor;
import lib.http.HTTPRequest;
import lib.http.HTTPResponse;
import lib.http.HTTPUtils;
import lib.http.SocketHTTPServer;

/*
 * ENDPOINT
 * 
 * Input:		POST /add
 * 				* value: string
 * 
 * Output:		201 - Created
 * 				* key: number
 * 
 * Fallback:	400 - Bad Request
 * 
 */
public class ARestHTTPProcessor implements HTTPProcessor {

	static final Logger LOGGER = Logger.getLogger(ARestHTTPProcessor.class.getName());
	
	static final String HOST = "localhost";
	static final int PORT = 8888;	
	static final String BASEURL = "http://" + HOST + ":" + PORT;
	
	private int id;
	private Map<Integer, String> values;
	
	public ARestHTTPProcessor() {
		values = new LinkedHashMap<>();
	}
	
	@Override
	public void process(HTTPRequest req, HTTPResponse res) {
		
		if ("/add".equals(req.getPath()) && "POST".equals(req.getMethod())) {
			
			String body = HTTPUtils.readToString(req.getReader());			
			
			JSONObject jin = new JSONObject(body);
			String value = jin.getString("value");			
			values.put(++id, value);
			
			LOGGER.info(values.toString());
			
			JSONObject jout = new JSONObject().put("key", id);			
			process(res, "201 Created", jout.toString());	
		}
		else {
			LOGGER.severe(req.getMethod() + " " + req.getPath());
			process(res, "400 Bad Request", null);
		}		
	}
	
	private void process(HTTPResponse res, String status, String jsonStr) {
		
		PrintWriter out = res.getWriter();
		
		out.println("HTTP/1.1 " + status);
		out.println("Server: jgregor5");
		out.println("Date: " + HTTPUtils.toHTTPDate(new Date()));
		if (jsonStr != null) {
			out.println("Content-type: application/json; charset=" + SocketHTTPServer.CHARSET);
			out.println("Content-length: " + jsonStr.getBytes(SocketHTTPServer.CHARSET).length);
		}
		out.println(); // blank line!
		if (jsonStr != null) {
			out.println(jsonStr);
		}
	}
	
	public static void main(String[] args) {
		
		LoggingConfigurator.configure(Level.INFO);
		
		HTTPProcessor processor = new ARestHTTPProcessor();		
		SocketHTTPServer server = new SocketHTTPServer(processor, HOST, PORT);
		server.start();
		
		Threads.sleep(500); // give server some time
		
		try {
			JSONObject json;
			int key;
			
			json = new JSONObject().put("value", "hola, món!");		
			key = request(BASEURL, json);
			LOGGER.info("got key " + key);
			
			json.put("value", "adeu!");
			key = request(BASEURL, json);
			LOGGER.info("got key " + key);
			
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "requesting", e);
			
		} finally {
			server.stop();
		}
	}
	
	private static int request(String baseUrl, JSONObject jsonBody) throws IOException {
		
		Response res = Jsoup.connect(baseUrl + "/add")
				.method(Method.POST)
				.requestBody(jsonBody.toString())
				.ignoreContentType(true).ignoreHttpErrors(true).execute();
		
		if (res.statusCode() != 201) {
			throw new IOException("something went bad! status code is " + res.statusCode());
		}
		
		return new JSONObject(res.body()).getInt("key");		
	}
}

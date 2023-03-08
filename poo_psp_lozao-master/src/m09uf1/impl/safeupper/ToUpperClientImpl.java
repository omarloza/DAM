package m09uf1.impl.safeupper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

import m09uf1.prob.safeupper.ToUpperClient;


public class ToUpperClientImpl implements ToUpperClient{
	static final Logger LOGGER = Logger.getLogger(ToUpperClientImpl.class.getName());
	
	static final Charset UTF8 = StandardCharsets.UTF_8;
	static final Charset CHARSET = UTF8;

	private Socket socket;
	private String host;
	private int port;

	private MyCoDec encDec;
	
	private PrintWriter out;
	private BufferedReader in;
	
	
	public ToUpperClientImpl(String host, int port , byte[] askey) throws IOException {		
		this.host = host;
		this.port = port;
		this.encDec = new MyCoDec(askey);
	}
	
	public void connect() throws IOException {
		
		socket = new Socket();
		socket.connect(new InetSocketAddress(host, port), 1500); 
		socket.setSoTimeout(1000); 
		
		out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), CHARSET), true);
		in = new BufferedReader(new InputStreamReader(socket.getInputStream(), CHARSET));
	}
	
	@Override
	public void disconnect() throws IOException {
		try {
			socket.close();
		} finally {
			socket = null;
		}
	}

	@Override
	public synchronized String toUpper(String input) throws IOException {
		
		
//		if (socket == null) {
//			throw new IllegalStateException("socket is not created");
//		}
//		out.println(encDec.encode(input));
//
//		String s = in.readLine();
//
//		if (s == null) {
//			socket = null;
//			throw new IOException("socket is not connected");
//		}
//
//		String received = encDec.decode(s);
//		LOGGER.info("received " + received);
//		return received;

		
		if (socket == null) {
			throw new IllegalStateException("socket is not created");
		}
		
		out.println(encDec.encode(input));
		String received = in.readLine();
		
		if (received == null) {
			socket = null;
			throw new IOException("socket is not connected");
		}
		
		
		String secret = encDec.decode(received); 
		LOGGER.info("received " + secret);
		return secret;
	}
}
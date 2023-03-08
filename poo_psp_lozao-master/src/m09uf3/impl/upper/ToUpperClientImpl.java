package m09uf3.impl.upper;

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


import m09uf3.prob.upper.ToUpperClient;

public class ToUpperClientImpl implements ToUpperClient {
	static final Charset UTF8 = StandardCharsets.UTF_8;
	static final int TIMEOUT_MILLIS = 6000;
	static final Charset CHARSET = UTF8; // it could be UTF8, ISO8859_1...
	static final Logger LOGGER = Logger.getLogger(ToUpperClientImpl.class.getName());
	private String host;
	private int PORT;
	private PrintWriter out;
	private BufferedReader in;
	private Socket socket;

	public ToUpperClientImpl(String host, int PORT) {
		super();
		this.host = host;
		this.PORT = PORT;

	}

	@Override
	public String toUpper(String input) throws IOException {
		String received;
		out.println(input);
		received = in.readLine().toUpperCase();
		LOGGER.info("response is " + received);

		return received;
	}

	@Override
	public void connect() throws IOException {
		socket = new Socket();
		socket.connect(new InetSocketAddress(host, PORT), TIMEOUT_MILLIS); // connect timeout
		socket.setSoTimeout(TIMEOUT_MILLIS); // response timeout
		out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), CHARSET), true);
		in = new BufferedReader(new InputStreamReader(socket.getInputStream(), CHARSET));
	}

	@Override
	public void disconnect() throws IOException {
		out.close();
		in.close();
		socket.close();
	}

}

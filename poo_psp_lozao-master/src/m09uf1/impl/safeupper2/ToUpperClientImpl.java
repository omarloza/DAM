package m09uf1.impl.safeupper2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;
import m09uf1.prob.safeupper2.ToUpperClient;

public class ToUpperClientImpl implements ToUpperClient {

	static final Logger LOGGER = Logger.getLogger(ToUpperClientImpl.class.getName());

	private Socket socket;
	private String host;
	private int port;
	private PrintWriter out;
	private BufferedReader in;
	private MyCoDecClient codDec;

	public ToUpperClientImpl(String host, int port) throws IOException {
		this.host = host;
		this.port = port;
		codDec = new MyCoDecClient();
	}

	public void connect() throws IOException {

		socket = new Socket();
		socket.connect(new InetSocketAddress(host, port), 1500); // connect timeout
		socket.setSoTimeout(1000); // response timeout
		out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);
		in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
		
		if (socket == null) {
			throw new IllegalStateException("socket is not created");
		}
		out.println(codDec.PublicKey());

		String s = in.readLine();

		if (s == null) {
			socket = null;
			throw new IOException("socket is not connected");
		}
		System.out.println(s);
		codDec.readSecret(s);
		
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

		if (socket == null) {
			throw new IllegalStateException("socket is not created");
		}
		out.println(codDec.encode(input));

		String s = in.readLine();

		if (s == null) {
			socket = null;
			throw new IOException("socket is not connected");
		}

		String received = codDec.decode(s);
		LOGGER.info("received " + received);
		return received;
	}
}

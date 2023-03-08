package m09uf1.impl.safeupper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.logging.Logger;
import m09uf1.prob.safeupper.ToUpperClient;

public class ToUpperClientImpl_aux implements ToUpperClient {

	static final Logger LOGGER = Logger.getLogger(ToUpperClientImpl_aux.class.getName());
	static final Charset CHARSET = ToUpperServerImpl_aux.CHARSET;

	private Socket socket;
	private String host;
	private int port;
	private PrintWriter out;
	private BufferedReader in;
	private MyCoDec codDec;

	public ToUpperClientImpl_aux(String host, int port, byte[] keyBytes) throws IOException {
		this.host = host;
		this.port = port;
		codDec = new MyCoDec(keyBytes);
	}

	public void connect() throws IOException {

		socket = new Socket();
		socket.connect(new InetSocketAddress(host, port), 1500); // connect timeout
		socket.setSoTimeout(1000); // response timeout

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

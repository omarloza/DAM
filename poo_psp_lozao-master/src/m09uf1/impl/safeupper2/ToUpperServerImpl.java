package m09uf1.impl.safeupper2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import lib.Startable;

public class ToUpperServerImpl implements Startable {
	static final Logger LOGGER = Logger.getLogger(ToUpperServerImpl.class.getName());

	static final int PORT = 5508;
	static final int NTHREADS = 10;

	static final Charset UTF8 = StandardCharsets.UTF_8;
	static final Charset ISO8859_1 = StandardCharsets.ISO_8859_1;
	static final Charset CHARSET = UTF8; // it could be UTF8, ISO8859_1...

	static final int TIMEOUT_MILLIS = 6000;
	private MyCoDecServer c;
	private String host;
	private int port;
	private volatile ServerSocket serverSocket;
	private Thread t;

	public ToUpperServerImpl(String host, int port) throws IOException {
		this.host = host;
		this.port = port;

	}

	@Override
	public void start() {
		c = new MyCoDecServer();
		t = new Thread(() -> server(), "toupper");
		t.start();

	}

	@Override
	public void stop() {
		try {
			serverSocket.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

	@Override
	public boolean isStarted() {

		return t != null && t.isAlive();
	}

	private void server() {

		ExecutorService executor = Executors.newCachedThreadPool();

		try {
			this.serverSocket = new ServerSocket(port, 10, InetAddress.getByName(host));
			serverSocket.setSoTimeout(TIMEOUT_MILLIS);

			while (true) {
				try {
					Socket clientSocket = serverSocket.accept();
					SocketAddress remote = clientSocket.getRemoteSocketAddress();
					LOGGER.info("accept from " + remote);

					if (clientSocket != null) {
						PrintWriter out = new PrintWriter(
								new OutputStreamWriter(clientSocket.getOutputStream(), CHARSET), true);
						BufferedReader in = new BufferedReader(
								new InputStreamReader(clientSocket.getInputStream(), CHARSET));
						String publicKey = in.readLine();
						String secretKey = c.MSGxifrat(publicKey);
						out.println(secretKey);
					}

					clientSocket.setSoTimeout(TIMEOUT_MILLIS);
					executor.submit(() -> handle(clientSocket, c));

				} catch (SocketTimeoutException e) {
					// timeout in the accept: ignore
					LOGGER.fine("timeout accepting!");
				}
			}
		} catch (IOException e) {
			if (serverSocket.isClosed()) {
				LOGGER.info("server was closed");
				return;
			}

			LOGGER.log(Level.SEVERE, "serving", e);

		} finally {
			executor.shutdown();
		}
	}

	static void handle(Socket clientSocket, MyCoDecServer c2) {

		try (PrintWriter out = new PrintWriter(
				new OutputStreamWriter(clientSocket.getOutputStream(), StandardCharsets.UTF_8), true);
				BufferedReader in = new BufferedReader(
						new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8))) {

			while (true) {
				try {
					String s = in.readLine();
					if (s == null) {
						LOGGER.warning("client disconnected!");
						return;
					}

					String input = c2.decode(s);
					LOGGER.info("request is " + input);

					out.println(c2.encode(input.toUpperCase()));

				} catch (SocketTimeoutException e) {
					// read timeout: do nothing
					LOGGER.fine("timeout reading!");
				}
			}

		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "handling", e);

		} finally {
			try {
				clientSocket.close();
			} catch (IOException e) {
				LOGGER.warning("closing: " + e.getMessage());
			}
		}
	}
}

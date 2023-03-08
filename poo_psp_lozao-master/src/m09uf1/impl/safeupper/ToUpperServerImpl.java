package m09uf1.impl.safeupper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
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

public class ToUpperServerImpl implements Startable, Runnable {
	static final Logger LOGGER = Logger.getLogger(ToUpperServerImpl_aux.class.getName());

	static final int PORT = 5508;
	static final int NTHREADS = 10;

	static final Charset UTF8 = StandardCharsets.UTF_8;
	static final Charset CHARSET = UTF8;

	static final int TIMEOUT_MILLIS = 1500;

	private static MyCoDec encDec;
	private String host;
	private int port;
	private volatile ServerSocket serverSocket;
	private Thread t;

	public ToUpperServerImpl(String host, int port , byte[] keyByte) throws IOException {
		this.host = host;
		this.port = port;
		encDec = new MyCoDec(keyByte);
	}

	@Override
	public void start() {
		t = new Thread(this, "Upper");
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

					clientSocket.setSoTimeout(TIMEOUT_MILLIS);
					executor.submit(() -> handle(clientSocket));

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

	static void handle(Socket clientSocket) {

		try (PrintWriter out = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream(), CHARSET), true);
				BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), CHARSET))) {

			while (true) {
				try {
					String input = in.readLine();
					if (input == null) {
						LOGGER.warning("client disconnected!");
						return;
					}

					LOGGER.info("request is " + input);
					String inputdec = encDec.decode(input); 
					String inputUpper = inputdec.toUpperCase(); 

					out.println(encDec.encode(inputUpper));

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

	@Override
	public void run() {

		server();

	}

}
package m09uf3.impl.upper;

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
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;


import m09uf3.prob.upper.ToUpperServer;

public class ToUpperServerImpl implements ToUpperServer, Runnable {
	static final Charset UTF8 = StandardCharsets.UTF_8;
	static final int TIMEOUT_MILLIS = 6000;
	static final Charset CHARSET = UTF8; // it could be UTF8, ISO8859_1...
	static final Logger LOGGER = Logger.getLogger(ToUpperServerImpl.class.getName());
	private String host;
	private int PORT;
	private ServerSocket serverSocket;
	private Socket clientSocket;
	private Thread t;

	public ToUpperServerImpl(String host, int PORT) {
		super();
		this.host = host;
		this.PORT = PORT;

	}

	@Override
	public void start() {

		try {
			serverSocket = new ServerSocket(PORT, 10, InetAddress.getByName(host));
			t = new Thread(this, "thread");
			t.start();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {

			while (true) {
				
				try {
					
					clientSocket = serverSocket.accept();
					SocketAddress remote = clientSocket.getRemoteSocketAddress();
					LOGGER.info("accept from " + remote);
					clientSocket.setSoTimeout(TIMEOUT_MILLIS);
					handle();
					
				} catch (SocketTimeoutException e) {
					// timeout in the accept: ignore
					LOGGER.fine("timeout accepting");
				}
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			LOGGER.fine("socket closed!");

		}
	}

	@Override
	public void stop() {

		try {
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void handle() {

		while (true) {
			try {
				PrintWriter out = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream(), CHARSET),true);
				BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), CHARSET));
				String input = in.readLine();
				if (input == null) {
					break;
				}
				LOGGER.info("request is " + input);
				out.println(input);

			} catch (SocketTimeoutException e) {
				// read timeout: do nothing
				LOGGER.fine("timeout reading");

			} catch (IOException e) {
				LOGGER.log(Level.SEVERE, "handling", e);
			}
		}
	}

}

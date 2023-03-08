package m09uf3.impl.guessb;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import lib.Startable;
import m09uf3.prob.guessb.GuessSecret;

public class GuessServerImpl implements Startable, Runnable {
	private volatile boolean closed;
	static final Logger LOGGER = Logger.getLogger(GuessServerImpl.class.getName());
	static final int ACCEPT_TIMEOUT_MILLIS = 1500;
	static final int READ_TIMEOUT_MILLIS = 1500;
	private GuessSecret gs;
	private String host;
	private int port;
	private Thread thread;
	private ServerSocket serverSocket;


	public GuessServerImpl(String host, int port, GuessSecret gs) {
		this.host = host;
		this.port = port;
		this.gs = gs;
	}

	@Override
	public void start() {
		this.thread = new Thread(this, "server");
		this.thread.start();
	}

	@Override
	public void stop() {
		try {
			if (serverSocket != null)
				serverSocket.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean isStarted() {
		return thread != null && thread.isAlive();
	}

	@Override
	public void run() {

		ExecutorService service = Executors.newCachedThreadPool();

		LOGGER.info("starting server at " + host + ":" + port);

		try {
			serverSocket = new ServerSocket(port, 10, InetAddress.getByName(host));
			serverSocket.setSoTimeout(ACCEPT_TIMEOUT_MILLIS);

			while (true) {
				try {
					Socket clientSocket = serverSocket.accept();
					service.submit(() -> handle(clientSocket));

				} catch (SocketTimeoutException e) {
					// timeout in the accept: ignore
					LOGGER.fine("timeout accepting");
				}
			}

		} catch (IOException e) {
			if (serverSocket != null && serverSocket.isClosed()) {
				LOGGER.info("server closed");
			} else {
				LOGGER.log(Level.SEVERE, "accepting", e);
			}

		} finally {
			service.shutdown();
		}
	}

	protected void handle(Socket clientSocket) {
		try {
			InputStream in = clientSocket.getInputStream();
			OutputStream out = clientSocket.getOutputStream();
			int secret = gs.next();
			int min = 1;
			byte[] baux = new byte[2];
			baux[0] = (byte) min;
			baux[1] = (byte) gs.upper();
			out.write(baux);

			while (!closed) {
				int num = in.read();
				if (num == -1) {
					return;
				}
				if (num > secret) {
					int n =1;
					byte[] resp = new byte[1];
					resp[0]=(byte) n;
					out.write(resp);
				}
				if (num < secret) {
					int n =2;
					byte[] resp = new byte[1];
					resp[0]=(byte) n;
					out.write(resp);
				}
				if (num == secret) {
					int n =3;
					byte[] resp = new byte[1];
					resp[0]=(byte) n;
					out.write(resp);
					clientSocket.close();
					
				}

			}
		} catch (IOException e) {

			e.printStackTrace();
		}

	}
}

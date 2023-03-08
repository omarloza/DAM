package m09uf3.impl.fwatch;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;
import lib.Startable;
import m09uf3.prob.fwatch.FWatchEvent;
import m09uf3.prob.fwatch.FWatchHandler;

public class FWatchServerImpl implements Startable, Runnable {
	static final int NTHREADS = 10;
	static final Logger LOGGER = Logger.getLogger(FWatchServerImpl.class.getName());
	static final Charset UTF8 = StandardCharsets.UTF_8;
	static final Charset ISO8859_1 = StandardCharsets.ISO_8859_1;
	static final Charset CHARSET = UTF8; // it could be UTF8, ISO8859_1...
	static final int TIMEOUT_MILLIS = 6000;
	private Thread t;
	private volatile ServerSocket serverSocket;
	private String host;
	private int port;
	private FolderWatcher fw;
	public FWatchEvent evento;
	public String filePatho;

	public FWatchServerImpl(String host, int port) {
		this.host = host;
		this.port = port;
	}

	@Override
	public void start() {
		t = new Thread(this, "watcher");
		t.start();

	}

	private void server() {

		try {
			this.serverSocket = new ServerSocket(port, 10, InetAddress.getByName(host));
			serverSocket.setSoTimeout(TIMEOUT_MILLIS);

			while (true) {
				try {
					Socket clientSocket = serverSocket.accept();

					SocketAddress remote = clientSocket.getRemoteSocketAddress();
					LOGGER.info("accept from " + remote);

					clientSocket.setSoTimeout(TIMEOUT_MILLIS);
					handle(clientSocket);

				} catch (SocketTimeoutException e) {
					LOGGER.fine("timeout accepting!");
				}
			}

		} catch (IOException e) {
			if (serverSocket.isClosed()) {
				LOGGER.info("server was closed");
				return;
			}

			LOGGER.log(Level.SEVERE, "serving", e);

		}
	}

	private void handle(Socket clientSocket) {
		try {
			InputStream in = clientSocket.getInputStream();
			OutputStream out = clientSocket.getOutputStream();

			while (true) {
				int path;
				while (true) {
					try {
						path = in.read();
						break;
					} catch (SocketTimeoutException e) {
						LOGGER.info("timeout");
					}

				}

				if (path == -1) {
					break;
				}

				byte[] nomarxiu = new byte[path];
				for (int i = 0; i < path; i++) {
					nomarxiu[i] = (byte) in.read();
				}
				String nomarxiuStr = new String(nomarxiu, StandardCharsets.UTF_8);

				 fw = new FolderWatcher(nomarxiuStr, new FWatchHandler() {

					@Override
					public void notify(FWatchEvent event, String filePath) {
						LOGGER.info("received " + event + " on " + filePath);

						String letraEvento = "";

						if (event == FWatchEvent.CREATE) {
							letraEvento = "C";
							responder(out,letraEvento,filePath);
						}

						if (event == FWatchEvent.MODIFY) {
							letraEvento = "M";
							responder(out,letraEvento,filePath);

						}
						if (event == FWatchEvent.DELETE) {
							letraEvento = "D";
							responder(out,letraEvento,filePath);
						}

					}
				});

				fw.start();

			}
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "handling", e);

		} finally {
			try {
				clientSocket.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

	}

	public void responder(OutputStream out, String letraEvento, String filePath) {

		try {
			byte[] tipoEvento = letraEvento.getBytes(StandardCharsets.UTF_8);

			byte baux = tipoEvento[0];

			byte[] filePathbyte = filePath.getBytes(StandardCharsets.UTF_8);
			byte[] arrayEnviament = new byte[tipoEvento.length + filePathbyte.length + 1];

			arrayEnviament[0] = baux;
			arrayEnviament[1] = (byte) filePathbyte.length;

			for (int i = 0; i < filePathbyte.length; i++) {
				arrayEnviament[i + 2] = filePathbyte[i];

			}

			
			out.write(arrayEnviament);
		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	@Override
	public void stop() {
		try {
			fw.stop();
			t.interrupt();
			serverSocket.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

	@Override
	public boolean isStarted() {
		return t.isAlive();
	}

	@Override
	public void run() {
		server();
	}

}

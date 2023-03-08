package m09uf3.impl.fwatch;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import m09uf3.prob.fwatch.FWatchClient;
import m09uf3.prob.fwatch.FWatchEvent;
import m09uf3.prob.fwatch.FWatchHandler;

public class FWatchClientImpl implements FWatchClient {
	static final Logger LOGGER = Logger.getLogger(FWatchClientImpl.class.getName());
	static final Charset CHARSET = FWatchClientImpl.CHARSET;
	private String host;
	private volatile Socket socket;
	private int port;
	private String path;
	private FWatchHandler handler;
	private InputStream in;
	private OutputStream ou;

	public FWatchClientImpl(String host, int port) {

		this.host = host;
		this.port = port;
	}

	@Override
	public void connect(String path, FWatchHandler handler) throws IOException {
		this.path = path;
		this.handler = handler;
		new Thread(() -> client(), "watcher").start();
	}

	private void client() {
		ExecutorService executor = Executors.newCachedThreadPool();
		socket = new Socket();
		try {
			socket.connect(new InetSocketAddress(host, port), 1500);
			socket.setSoTimeout(6000);
			executor.submit(() -> handle(socket));
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			executor.shutdown();
		}

	}

	@Override
	public void disconnect() throws IOException {
		try {
		
			socket.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}finally {
			socket=null;
		}

	}

	public void notificar(InputStream in, int num ,FWatchEvent event) throws IOException {
		byte[] nomarxiu = new byte[num];
		for (int i = 0; i < num; i++) {
			nomarxiu[i] = (byte) in.read();
		}
		String nomarxiuStr = new String(nomarxiu, StandardCharsets.UTF_8);
		handler.notify(event, nomarxiuStr);
	}
	
	
	public void handle(Socket socket) {

		try {
			in = socket.getInputStream();
			ou = socket.getOutputStream();
			byte[] fileNameArray = path.getBytes(StandardCharsets.UTF_8);
			byte[] arrayEnviament = new byte[fileNameArray.length + 1];
			arrayEnviament[0] = (byte) fileNameArray.length;
			for (int i = 0; i < fileNameArray.length; i++) {
				arrayEnviament[i + 1] = fileNameArray[i];
			}
			ou.write(arrayEnviament);

			while (true) {
				
				 int event = in.read();
				if (event == -1) {
					return;
				}

				if (event == 0x43) {// C -> crear
					int num = in.read();
					if (num == -1) {
						return;
					}
					FWatchEvent ev = FWatchEvent.CREATE;
					notificar(in, num ,ev);
		
				}

				if (event == 0x4d) {// M -> modificar
					int num = in.read();
					if (num == -1) {
						return;
					}
					FWatchEvent ev = FWatchEvent.MODIFY;
					notificar(in, num ,ev);
				}

				if (event == 0x44) {// D -> eliminar
					int num = in.read();
					if (num == -1) {
						return;
					}
					FWatchEvent ev = FWatchEvent.DELETE;
					notificar(in, num ,ev);
				}

			}

		} catch (IOException e) {
			LOGGER.info("socket closed");
			
		} 
	}

}

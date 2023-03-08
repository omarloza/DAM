package m09uf3.impl.guessb;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import m09uf3.prob.guessb.GuessClient;
import m09uf3.prob.guessb.GuessSecret;
import m09uf3.prob.guessb.GuessStatus;

public class GuessClientImpl implements GuessClient {
	private String host;
	private int port;
	private Socket socket;
	private GuessSecret gs;
	private byte secret;
	private volatile boolean closed;

	public GuessClientImpl(String host, int port) {
		this.host = host;
		this.port = port;
	}

	@Override
	public byte[] start() {
		try {
			socket = new Socket();
			socket.connect(new InetSocketAddress(host, port), 1500);
			socket.setSoTimeout(1000); // response timeout
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] rango = new byte[2];
		try {
			InputStream in = socket.getInputStream();
			byte min = (byte) in.read();
			int upper = in.read();
			rango[0]=min;
			rango[1]=(byte) upper;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return rango;
	}

	@Override
	public GuessStatus play(byte play) {

		try {
			InputStream in = socket.getInputStream();
			OutputStream out = socket.getOutputStream();
			GuessStatus status = null ;
			out.write(play);
			
			while (!closed) {
				int event = in.read();
				if (event == -1) {
					return null;
				}
				if (event == 1) {
					return status.SMALLER;
				}
				if (event == 2) {
					return status.LARGER;
				}
				if (event == 3) {
					socket.close();
					return status.EXACT;
				}

			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

}

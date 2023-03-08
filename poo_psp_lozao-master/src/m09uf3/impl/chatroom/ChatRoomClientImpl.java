package m09uf3.impl.chatroom;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.logging.Logger;
import m09uf3.prob.chatroom.ChatRoomClient;
import m09uf3.prob.chatroom.ChatRoomHandler;

public class ChatRoomClientImpl implements ChatRoomClient, Runnable {

	static final Logger LOGGER = Logger.getLogger(ChatRoomClientImpl.class.getName());
	static final Charset UTF8 = StandardCharsets.UTF_8;
	static final Charset ISO8859_1 = StandardCharsets.ISO_8859_1;
	static final Charset CHARSET = UTF8; // it could be UTF8, ISO8859_1...
	static final int TIMEOUT_MILLIS = 1500;
	static final int SIZE = 512;
	private String host;
	private int port;
	private ChatRoomHandler handler;
	private DatagramSocket socket;
	private InetAddress address;
	private Thread t;
	private DatagramPacket packet=new DatagramPacket(new byte[SIZE], SIZE);
	private String username;
	private boolean acabar =true;
	
	public ChatRoomClientImpl(String host, int port) {
		this.host = host;
		this.port = port;
		
	}

	@Override
	public void connect(String username, ChatRoomHandler handler) throws IOException {
		this.username=username;
		this.handler = handler;
		socket = new DatagramSocket();
		address = InetAddress.getByName(host);
		t = new Thread(this, "user: " + username);
		t.start();
		int codigo = 1;
		CodificarEnviar(packet,socket,codigo, username);
	
	}

	@Override
	public void message(String message) throws IOException {
		int codigo = 2;
		CodificarEnviar(packet,socket,codigo, message);

	}

	public void CodificarEnviar(DatagramPacket p,DatagramSocket s, int codigo, String text) throws IOException {

		byte[] mensaje = text.getBytes(CHARSET);
		byte[] arrayEnviament = new byte[ mensaje.length + 2];
		arrayEnviament[0] = (byte) codigo;
		arrayEnviament[1] = (byte) mensaje.length;
		for (int i = 0; i < mensaje.length; i++) {
			arrayEnviament[i + 2] = mensaje[i];
		}

//		System.out.println(	Arrays.toString(arrayEnviament));
	
		p = new DatagramPacket(arrayEnviament, arrayEnviament.length, address, port);
		s.send(p);
//		LOGGER.info("sent " + p.getLength());
	}

	@Override
	public void disconnect() throws IOException {
		
		int codigo =3;
		byte[] arrayEnviament = new byte[1];
		arrayEnviament[0] = (byte) codigo;
		packet = new DatagramPacket(arrayEnviament, arrayEnviament.length, address, port);
		socket.send(packet);
//		LOGGER.info("sent " + packet.getLength());
		acabar=false;
		socket.close();

	}

	@Override
	public void run() {
		
		while (acabar==true) {

			try {
				socket.setSoTimeout(6000);
				packet = new DatagramPacket(new byte[SIZE], SIZE);
				socket.receive(packet);
				
				int cod = packet.getData()[0];
				if(cod!=1) {
					LOGGER.info("descartado");
				}else {
				
				int longitudNombre = packet.getData()[1];
//				System.out.println(longitudNombre);
				String nombre = new String(packet.getData(), 2, longitudNombre, CHARSET);
				
				int longitudRespuesta = packet.getData()[longitudNombre+2];
				String mensaje = new String(packet.getData(), longitudNombre+3, longitudRespuesta, CHARSET);

				LOGGER.info("received " +nombre+": "+mensaje);

				handler.message(nombre, mensaje);
				}

			} catch (IOException e) {
			LOGGER.info("socket closed!");
			}

		}

	}

}

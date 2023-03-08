package m09uf3.impl.chatroom;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.logging.Logger;
import lib.Startable;

public class ChatRoomServerImpl implements Startable, Runnable {
	static final Logger LOGGER = Logger.getLogger(ChatRoomServerImpl.class.getName());
	static final Charset UTF8 = StandardCharsets.UTF_8;
	static final Charset ISO8859_1 = StandardCharsets.ISO_8859_1;
	static final Charset CHARSET = UTF8; // it could be UTF8, ISO8859_1...
	static final int SIZE = 512;
	private String host;
	private int port;
	private Thread t;
	private DatagramSocket socket;
	private DatagramPacket packet;
	private HashSet<UserChatRoom> listaUsuarios = new HashSet<UserChatRoom>();
	private HashSet<UserChatRoom> usuarioborrar = new HashSet<UserChatRoom>();
	private boolean acabar = true;

	public ChatRoomServerImpl(String host, int port) {
		this.host = host;
		this.port = port;
	}
	@Override
	public void run() {
		try {
			socket.setSoTimeout(6000);
			packet = new DatagramPacket(new byte[SIZE], SIZE);
			while (acabar == true) {
				try {
					socket.receive(packet);
					handle(socket, packet);

				} catch (SocketTimeoutException e) {
					LOGGER.fine("timeout receiving!");
				} catch (IOException e) {
					LOGGER.info("socket server closed!");

				}
			}

		} catch (SocketException e) {
			e.printStackTrace();
		}

	}

	private void handle(DatagramSocket socket2, DatagramPacket packet2) {

		int codigo = packet2.getData()[0];
		if (codigo != 1 && codigo != 2 && codigo != 3) {
			LOGGER.info("codigo no valido!");
		} else {

			String aux = new String(packet2.getData(), 0, packet2.getLength(), CHARSET);


			int longitud = packet2.getData()[1];
			InetAddress address = packet2.getAddress();
			int portU = packet2.getPort();

			
			switch (codigo) {
			case 1:
				String texto = new String(packet2.getData(), 2, longitud, CHARSET);
				UserChatRoom usuari = new UserChatRoom(portU, address, texto);
				listaUsuarios.add(usuari);
				LOGGER.info(texto + " s'ha conectat!");
				break;
			case 2:

				for (UserChatRoom u : listaUsuarios) {

					if(portU==u.getPort()&&address.equals(u.getHost())) {
						
						int cRespuesta = 1;
						String nomUser = u.getNombre();
						String text = new String(packet2.getData(), 2, longitud, CHARSET);

						byte[] name = nomUser.getBytes(CHARSET);
						byte[] msj = text.getBytes(CHARSET);
						byte[] result = new byte[name.length + msj.length + 3];
						
//						LOGGER.info(text);
//						LOGGER.info(Arrays.toString(name));
//						LOGGER.info(Arrays.toString(msj));

						result[0] = (byte) cRespuesta;
						result[1] = (byte) name.length;

						for (int i = 0; i < name.length; i++) {
							result[i + 2] = name[i];
						}
						int lonpalabra = name.length + 2;

						result[lonpalabra] = (byte) msj.length;
						for (int i = 0; i < msj.length; i++) {
							result[i + lonpalabra + 1] = msj[i];
						}

//						System.out.println(Arrays.toString(result));
						String str1 = new String(result);
						LOGGER.info(nomUser + ": " + text);
						for (UserChatRoom u2 : listaUsuarios) {

							packet2 = new DatagramPacket(new byte[SIZE], SIZE);
							try {
								packet2 = new DatagramPacket(result, result.length, u2.getHost(), u2.getPort());
								socket2.send(packet2);
							} catch (IOException e) {
								e.printStackTrace();
							}

						}
					}

				}

				break;
			case 3:

				UserChatRoom userRemove = null;
				for (UserChatRoom u : listaUsuarios) {
					if (u.getPort() == portU) {
						userRemove = new UserChatRoom(u.getPort(), u.getHost(), u.getNombre());
					}
				}
				for (UserChatRoom ub : listaUsuarios) {

					if (ub.getPort() == userRemove.getPort()) {
						usuarioborrar.add(ub);
					}
				}

				listaUsuarios.removeAll(usuarioborrar);
//				System.out.println(listaUsuarios.size());


				break;
			default:
				System.out.println("no match");
			}

		}

	}

	@Override
	public void start() {
		try {
			socket = new DatagramSocket(port, InetAddress.getByName(host));
			t = new Thread(this, "server");
			t.start();
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void stop() {
		acabar = false;
		t.interrupt();
		socket.close();

	}

	@Override
	public boolean isStarted() {

		return t.isAlive() && t != null;
	}

}

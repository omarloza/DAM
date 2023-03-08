package m09uf3.impl.chatroom;

import java.net.InetAddress;

public class UserChatRoom {
	private InetAddress host;
	private int port;
	private String nombre;
	public UserChatRoom(int port,InetAddress host,  String nombre) {
		this.host = host;
		this.port = port;
		this.nombre = nombre;
	}
	public InetAddress getHost() {
		return host;
	}
	public void setHost(InetAddress host) {
		this.host = host;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	
}

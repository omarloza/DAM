package m09uf3.impl.chatroom;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import lib.LoggingConfigurator;
import lib.Problems;
import lib.Reflections;
import lib.Startable;
import lib.Threads;
import m09uf3.prob.chatroom.ChatRoomClient;

public class ChatRoomRun {

	static final Logger LOGGER = Logger.getLogger(ChatRoomRun.class.getName());
	
	static final int PORT = 5511;
	
	private static ChatRoomClient getClientInstance(String host) {
		
		String packageName = Problems.getImplPackage(ChatRoomClient.class);		
		return Reflections.newInstanceOfType(
				ChatRoomClient.class, packageName + ".ChatRoomClientImpl",
				new Class[] {String.class, int.class}, 
				new Object[] {host, PORT});
	}
	
	private static Startable getServerInstance(String host) {
		
		String packageName = Problems.getImplPackage(ChatRoomClient.class);		
		return Reflections.newInstanceOfType(
				Startable.class, packageName + ".ChatRoomServerImpl",
				new Class[] {String.class, int.class}, 
				new Object[] {host, PORT});
	}
	
	public static void main(String[] args) {
		
		LoggingConfigurator.configure(Level.FINE);
				
		Startable server = getServerInstance("localhost");		
		server.start();		
		Threads.sleep(500); // some time to start server
		
		ChatRoomClient client1 = getClientInstance("localhost");
		ChatRoomClient client2 = getClientInstance("localhost");
		
		try {
			LOGGER.info("connecting 1");			
			client1.connect("julian", (username, message) -> {
				LOGGER.info("received " + message + " from " + username);
			});
			
			Threads.sleep(00);
			
			LOGGER.info("writing message 1");
			client1.message("bon dia tinguin!");
			
			Threads.sleep(500);
			
			LOGGER.info("connecting 2");			
			client2.connect("pere", (username, message) -> {
				LOGGER.info("received " + message + " from " + username);
			});

			Threads.sleep(500);
			
			LOGGER.info("writing messages 2&3");
			client2.message("voste tambe!");
			client1.message("genial...");
			
			Threads.sleep(500);
			
			LOGGER.info("disconnecting 2");
			client2.disconnect();
			
			Threads.sleep(500);
			
			LOGGER.info("writing message 4");
			client1.message("adeu!");
			
			Threads.sleep(500);
			
			LOGGER.info("disconnecting 1");
			client1.disconnect();
			
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, null, e);
		} finally {
			server.stop();
		}
	}
}

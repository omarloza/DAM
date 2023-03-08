package m09uf3.impl.chatroom;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.jupiter.api.Test;

import lib.LoggingConfigurator;
import lib.Problems;
import lib.Reflections;
import lib.Startable;
import lib.Threads;
import m09uf3.prob.chatroom.ChatRoomClient;

public class ChatRoomTest {

	static final Logger LOGGER = Logger.getLogger(ChatRoomTest.class.getName());
	
	static final int PORT = 5511;
	static final long WAIT = 500;
	
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
	
	@Test
	void test() {
		
		LoggingConfigurator.configure(Level.FINE);
				
		Startable server = getServerInstance("localhost");		
		server.start();		
		Threads.sleep(WAIT); // some time to start server
		
		ChatRoomClient client1 = getClientInstance("localhost");
		ChatRoomClient client2 = getClientInstance("localhost");
		
		TMutable<Integer> fase = new TMutable<>(0);
		TMutable<Boolean> failed = new TMutable<>(false);
		TMutable<ChatMsg> m11 = new TMutable<>();
		TMutable<ChatMsg> m13 = new TMutable<>();
		TMutable<ChatMsg> m14 = new TMutable<>();
		TMutable<ChatMsg> m16 = new TMutable<>();
		TMutable<ChatMsg> m23 = new TMutable<>();
		TMutable<ChatMsg> m24 = new TMutable<>();
		
		try {
			LOGGER.info("connecting 1");			
			client1.connect("julian", (username, message) -> {
				switch (fase.value) {
				case 1: m11.value = new ChatMsg(username, message); break;
				case 3: m13.value = new ChatMsg(username, message); break;
				case 4: m14.value = new ChatMsg(username, message); break;
				case 6: m16.value = new ChatMsg(username, message); break;
				default: failed.value = true;
				}
			});
			
			Threads.sleep(WAIT);
			assertNull(m11.value);
			fase.value = 1;
			
			LOGGER.info("writing message 1");
			client1.message("bon dia tinguin!");
			
			Threads.sleep(WAIT);
			assertNotNull(m11.value);
			assertEquals("julian", m11.value.username);
			assertEquals("bon dia tinguin!", m11.value.message);
			fase.value = 2;
			
			LOGGER.info("connecting 2");			
			client2.connect("pere", (username, message) -> {
				switch (fase.value) {
				case 3: m23.value = new ChatMsg(username, message); break;
				case 4: m24.value = new ChatMsg(username, message); break;
				default: failed.value = true;
				}
			});

			Threads.sleep(WAIT);
			assertNull(m13.value);
			assertNull(m23.value);
			fase.value = 3;
			
			LOGGER.info("writing message 2");
			client2.message("vosté també!");
			
			Threads.sleep(WAIT);
			assertNotNull(m13.value);
			assertEquals("pere", m13.value.username);
			assertEquals("vosté també!", m13.value.message);
			assertNotNull(m23.value);
			assertEquals("pere", m23.value.username);
			assertEquals("vosté també!", m23.value.message);
			assertNull(m14.value);
			assertNull(m24.value);
			fase.value = 4;
			
			LOGGER.info("writing message 3");
			client1.message("genial...");
			
			Threads.sleep(WAIT);			
			assertNotNull(m14.value);
			assertEquals("julian", m14.value.username);
			assertEquals("genial...", m14.value.message);
			assertNotNull(m24.value);
			assertEquals("julian", m24.value.username);
			assertEquals("genial...", m24.value.message);
			fase.value = 5;
			
			LOGGER.info("disconnecting second");
			client2.disconnect();
			
			Threads.sleep(WAIT);
			assertNull(m16.value);
			fase.value = 6;
			
			LOGGER.info("writing message 4");
			client1.message("adeu!");
			
			Threads.sleep(WAIT);
			assertNotNull(m16.value);
			assertEquals("julian", m16.value.username);
			assertEquals("adeu!", m16.value.message);
			fase.value = 7;
			
			LOGGER.info("disconnecting first");
			client1.disconnect();
			
			Threads.sleep(WAIT);
			fase.value = 8;
			
			assertFalse(failed.value);
			
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, null, e);
		} finally {
			server.stop();
		}
	}
	
	static class TMutable<T> {
		volatile T value;
		TMutable() {
			this(null);
		}
		TMutable(T value) {
			this.value = value;
		}
	}
	
	static class ChatMsg {
		String username, message;
		ChatMsg(String username, String message) {
			this.username = username;
			this.message = message;
		}
	}
}

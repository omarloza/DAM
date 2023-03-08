package m09uf3.impl.guessb;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import lib.LoggingConfigurator;
import lib.Problems;
import lib.Reflections;
import lib.Startable;
import lib.Threads;
import m09uf3.prob.guessb.GuessClient;
import m09uf3.prob.guessb.GuessSecret;
import m09uf3.prob.guessb.GuessStatus;

public class GuessTest {

	static final Logger LOGGER = Logger.getLogger(GuessTest.class.getName());

	static final String HOST = "localhost";
	static final int PORT = 5514;
	
	static final long SEED = 237458237656324L;
	static final int COUNT = 10;
	
	private static GuessSecret gs;
	private static Startable server;
	
	private static GuessClient getClientInstance(String host) {
		
		String packageName = Problems.getImplPackage(GuessClient.class);		
		return Reflections.newInstanceOfType(
				GuessClient.class, packageName + ".GuessClientImpl",
				new Class[] {String.class, int.class}, 
				new Object[] {host, PORT});
	}
	
	private static Startable getServerInstance(String host, GuessSecret gs) {
		
		String packageName = Problems.getImplPackage(GuessClient.class);		
		return Reflections.newInstanceOfType(
				Startable.class, packageName + ".GuessServerImpl",
				new Class[] {String.class, int.class, GuessSecret.class}, 
				new Object[] {host, PORT, gs});
	}
	
	@BeforeAll
	static void beforeAll() {
		
		LoggingConfigurator.configure(Level.FINE);
		
		gs = new GuessSecret(new int[] {13, 8, 6, 9, 16}, 20);
		server = getServerInstance(HOST, gs);
		server.start();
	}
	
	@AfterAll
	static void afterAll() {
		
		server.stop();		
	}
		
	@Test
	@Order(1)
	void testOne() {	
		
		oneGame(false);
	}
	
	@Test
	@Order(2)
	void testSleep() {	
		
		oneGame(true);
	}
	
	@RepeatedTest(COUNT)
	@Order(3)
	void testSequencial() {		
		
		oneGame(false);
	}
	
	private void oneGame(boolean sleep) {
		
		BotPlayer player = new BotPlayer();
		GuessClient client = getClientInstance(HOST);
		
		byte[] range = client.start();		
		assertEquals(range[0], 1);
		assertEquals(range[1], (byte) gs.upper());
		
		boolean done = false;
		while (!done) {
			if (sleep) {
				Threads.sleep(3000);
			}
			done = play(player, client, range, gs.current());
		}
	}
	
	@Test
	@Order(4)
	void testParallel() {
		
		byte upper = (byte) gs.upper();
		
		BotPlayer[] players = new BotPlayer[COUNT];
		Queue<Integer> games = new LinkedList<>();		
		GuessClient[] clients = new GuessClient[COUNT];
		byte[] secrets = new byte[COUNT];
		byte[][] ranges = new byte[COUNT][];
		
		for (int i=0; i<COUNT; i++) {
			clients[i] = getClientInstance(HOST);
			ranges[i] = clients[i].start();
			secrets[i] = gs.current();
			players[i] = new BotPlayer();
			assertEquals(ranges[i][0], 1);
			assertEquals(ranges[i][1], upper);
			games.add(i);
		}	
		
		while (!games.isEmpty()) {			
			int idx = games.poll();
			boolean done = play(players[idx], clients[idx], ranges[idx], secrets[idx]);
			if (!done) {
				games.add(idx);
			}
		}
	}	
	
	private static boolean play(BotPlayer player, GuessClient client, byte[] range, byte secret) {
		
		LOGGER.info("range: " + Arrays.toString(range));
		
		byte play = player.getPlay(range);
		GuessStatus status = client.play(play);
		boolean guessed = status == GuessStatus.EXACT;
		LOGGER.info("played " + play + ", " + (guessed? "YES" : "NO"));								
		assertEquals(play == secret, guessed);				
		if (guessed) {			
			return true;
		}
		
		if (play < secret) {
			assertEquals(GuessStatus.LARGER, status);
			range[0] = (byte) (play + 1);
		}
		else if (play > secret) {
			assertEquals(GuessStatus.SMALLER, status);
			range[1] = (byte) (play - 1);
		}
		
		return false;
	}
	
	static class BotPlayer {
		
		private Random random;
		private Set<Byte> plays;
		
		public BotPlayer() {
			random = new Random(SEED);
			plays = new HashSet<>();
		}
		
		/**
		 * Retorna una jugada dins de l'interval, sense repetir-la
		 * @param range
		 * @return
		 */
		public byte getPlay(byte[] range) {
			int bound = range[1] - range[0] + 1;
			int base = range[0];		
			while (true) {
				byte play = (byte) (random.nextInt(bound) + base);
				if (plays.add(play)) {
					return play;
				}
			}
		}		
	}
}

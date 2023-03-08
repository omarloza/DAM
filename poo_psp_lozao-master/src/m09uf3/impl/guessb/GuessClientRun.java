package m09uf3.impl.guessb;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import lib.LoggingConfigurator;
import lib.Problems;
import lib.Reflections;
import m09uf3.prob.guessb.GuessClient;
import m09uf3.prob.guessb.GuessStatus;

public class GuessClientRun {

	static final Logger LOGGER = Logger.getLogger(GuessClientRun.class.getName());

//	static final String HOST = "localhost";
//	static final int PORT = 5514;	
	static final String HOST = "10.1.5.10";
	static final int PORT = 5514;	
	
	private static GuessClient getClientInstance(String host) {
		
		String packageName = Problems.getImplPackage(GuessClient.class);		
		return Reflections.newInstanceOfType(
				GuessClient.class, packageName + ".GuessClientImpl",
				new Class[] {String.class, int.class}, 
				new Object[] {host, PORT});
	}
		
	public static void main(String[] args) {
		
		// disable INFO/FINE for interactive games
		LoggingConfigurator.configure(Level.WARNING);	
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		
		GuessClient client = getClientInstance(HOST);
		byte[] range = client.start();
		
		while (true) {
			byte play = getPlay(reader, range);
			GuessStatus status = client.play(play);
			if (status == GuessStatus.EXACT) {
				System.out.println("> well done!");
				break;
			}
			else {
				if (status == GuessStatus.LARGER) {
					range[0] = (byte) (play + 1);
					System.out.println("> larger!");	
				}
				else if (status == GuessStatus.SMALLER) {
					range[1] = (byte) (play - 1);
					System.out.println("> smaller!");
				}
			}
		}
	}
	
	/**
	 * Retorna una jugada llegida per la consola, dins de l'interval indicat
	 * @param reader
	 * @param range
	 * @return
	 */
	private static byte getPlay(BufferedReader reader, byte[] range) {
		
		int bound = range[1] - range[0] + 1;
		int base = range[0];
		
		while (true) {
			System.out.println("> enter " + Arrays.toString(range));
			int play;
			try {
				String line = reader.readLine();
				play = Integer.parseInt(line);
			} catch (IOException e) {
				throw new RuntimeException(e);
			} catch (NumberFormatException e) {
				System.out.println("> not a number");
				continue;
			}
			if (play < base || play >= base + bound) {
				System.out.println("> not in range " + Arrays.toString(range));
			}
			else {
				return (byte) play;
			}
		}
	}	
}

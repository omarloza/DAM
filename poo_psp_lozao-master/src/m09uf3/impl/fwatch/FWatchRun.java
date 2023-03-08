package m09uf3.impl.fwatch;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import lib.LoggingConfigurator;
import lib.Problems;
import lib.Reflections;
import lib.Startable;
import lib.Threads;
import m09uf3.prob.fwatch.FWatchClient;

public class FWatchRun {

	static final Logger LOGGER = Logger.getLogger(FWatchRun.class.getName());

	static final int PORT = 5510;

	private static FWatchClient getClientInstance(String host) {

		String packageName = Problems.getImplPackage(FWatchClient.class);
		return Reflections.newInstanceOfType(FWatchClient.class, packageName + ".FWatchClientImpl",
				new Class[] { String.class, int.class }, new Object[] { host, PORT });
	}

	private static Startable getServerInstance(String host) {

		String packageName = Problems.getImplPackage(FWatchClient.class);
		return Reflections.newInstanceOfType(Startable.class, packageName + ".FWatchServerImpl",
				new Class[] { String.class, int.class }, new Object[] { host, PORT });
	}

	public static void main(String[] args) {

		LoggingConfigurator.configure(Level.INFO);

		Startable server = getServerInstance("localhost");
		server.start();
		Threads.sleep(500); // some time to start server
//		
		FWatchClient client = getClientInstance("localhost");

//		 assumes the server is in the same machine/OS
		String folder = System.getProperty("user.home") + File.separator + "fwatch";
		File folderFile = new File(folder);
		if (!folderFile.exists()) {
			if (!folderFile.mkdir()) {
				throw new RuntimeException("failed to create folder " + folder);
			}
		}

		try {
			LOGGER.info("connecting");
			client.connect(folder, (event, filepath) -> {
				LOGGER.info("I received " + event + " on " + filepath);
			});

			Threads.sleep(2000);

			Path myFile = Paths.get(folder + File.separator + "test.txt");
			List<String> lines = Arrays.asList("1st line", "2nd line");
			LOGGER.info("creating file");
			Files.write(myFile, lines, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.CREATE);

			Threads.sleep(2000);

			lines = Arrays.asList("3rd line");
			LOGGER.info("appending file");
			Files.write(myFile, lines, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND);

			Threads.sleep(2000);

			LOGGER.info("deleting file");
			Files.delete(myFile);

			Threads.sleep(2000);

			LOGGER.info("disconnecting");
			client.disconnect();

		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, null, e);

		} finally {
			server.stop();
			folderFile.delete();
		}
	}
}

package m09uf3.impl.fwatch;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import lib.LoggingConfigurator;
import lib.Startable;
import lib.Threads;
import m09uf3.prob.fwatch.FWatchEvent;
import m09uf3.prob.fwatch.FWatchHandler;

public class FolderWatcher implements Startable, Runnable {

	static final Logger LOGGER = Logger.getLogger(FolderWatcher.class.getName());

	private String folder;
	private Thread thread;
	private FWatchHandler handler;

	public FolderWatcher(String folder, FWatchHandler handler) {
		this.folder = folder;
		this.handler = handler;
	}

	@Override
	public void start() {
		this.thread = new Thread(this, "fwatcher");
		this.thread.start();
	}

	@Override
	public void stop() {
		this.thread.interrupt();
	}

	@Override
	public boolean isStarted() {
		return this.thread.isAlive();
	}

	@Override
	public void run() {

		File file = new File(folder);
		if (!file.isDirectory()) {
			throw new IllegalArgumentException(folder + " is not a folder");
		}

		Path path = file.toPath();
		FileSystem fs = path.getFileSystem();
		LOGGER.info("watching path: " + path);

		try (WatchService service = fs.newWatchService()) {

			path.register(service, ENTRY_CREATE, ENTRY_MODIFY, ENTRY_DELETE);

			WatchKey key = null;
			while (true) {
				key = service.take(); // can be interrupted
				for (WatchEvent<?> watchEvent : key.pollEvents()) {
					Kind<?> kind = watchEvent.kind();
					if (OVERFLOW == kind) {
						continue; // loop
					} else if (ENTRY_CREATE == kind) {
						@SuppressWarnings("unchecked")
						Path newPath = ((WatchEvent<Path>) watchEvent).context();
						handler.notify(FWatchEvent.CREATE, newPath.toString());
					} else if (ENTRY_MODIFY == kind) {
						@SuppressWarnings("unchecked")
						Path newPath = ((WatchEvent<Path>) watchEvent).context();
						handler.notify(FWatchEvent.MODIFY, newPath.toString());
					} else if (ENTRY_DELETE == kind) {
						@SuppressWarnings("unchecked")
						Path newPath = ((WatchEvent<Path>) watchEvent).context();
						handler.notify(FWatchEvent.DELETE, newPath.toString());
					}
				}

				if (!key.reset()) {
					break;
				}
			}

		} catch (IOException e) {
			throw new RuntimeException("watching " + path, e);
		} catch (InterruptedException e) {
			LOGGER.info("interrupted!");
		}
	}

	public static void main(String[] args) throws IOException {

		LoggingConfigurator.configure();

		String folder = System.getProperty("java.io.tmpdir");
		LOGGER.info("carpeta " + folder);

		Startable server = new FolderWatcher(folder, (event, filepath) -> {
			LOGGER.info(event + " on " + filepath);
		});

		server.start();
		Threads.sleep(300000);
		server.stop();
	}
}

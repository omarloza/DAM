package m09uf2.impl.access;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.function.Consumer;
import java.util.logging.Logger;
import lib.fils.SafeQueue;
import m09uf2.prob.access.AccessExample;
import m09uf2.prob.access.AccessReport;
import m09uf2.prob.access.SiteAccessService;

public class SiteAccessServiceImpl implements SiteAccessService, Runnable {
	static final Logger LOGGER = Logger.getLogger(AccessExample.class.getName());
	private SafeQueue<PeticioServidor> cua;
	private int size;
	private Boolean acabar;
	private Thread thread;

	public SiteAccessServiceImpl(int size) {
		this.size = size;
		this.cua = new SafeQueue<SiteAccessServiceImpl.PeticioServidor>(size);
		this.acabar = false;
		this.thread = new Thread(this, "acces");
		this.thread.start();
	}

	@Override
	public void request(String t, Consumer<AccessReport> consumer) throws InterruptedException {

		PeticioServidor peticio = new PeticioServidor();
		peticio.url = t;
		peticio.consumer = consumer;
		cua.put(peticio);

	}

	@Override
	public void shutdown() {
		synchronized (acabar) {
			acabar = true;
			thread.interrupt();
		}
	}

	private boolean acabar() {

		synchronized (acabar) {
			return acabar;
		}

	}

	@Override
	public void run() {
		while (!acabar()) {
			long took = -1;
			String server;
			PeticioServidor peticio;

			try {
				peticio = cua.take();

				try {

					URL url = new URL(peticio.url);
					LOGGER.info("submitting " + url);

					long millis = System.currentTimeMillis();
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.setConnectTimeout(6000);
					conn.connect();

					int code = conn.getResponseCode();
					took = System.currentTimeMillis() - millis;
					server = conn.getHeaderField("Server");

					AccessReport a = new AccessReport(peticio.url, took, server);
					peticio.consumer.accept(a);

				} catch (MalformedURLException e) {
					LOGGER.info("malformedUrl!");
					e.printStackTrace();

				} catch (IOException e) {
					AccessReport a = new AccessReport(peticio.url, -1, null);
					peticio.consumer.accept(a);
					LOGGER.info("exeption!");

				}

			} catch (InterruptedException e1) {
				LOGGER.info("interrupted!");
			}

		}

	}

	static class PeticioServidor {
		public String url;
		public Consumer<AccessReport> consumer;

	}

}

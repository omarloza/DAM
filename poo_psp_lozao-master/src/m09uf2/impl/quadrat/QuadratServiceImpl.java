package m09uf2.impl.quadrat;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.logging.Logger;

import lib.fils.SafeQueue;
import m09uf2.prob.access.AccessExample;
import m09uf2.prob.quadrat.QuadratService;

public class QuadratServiceImpl implements QuadratService, Runnable {
	static final Logger LOGGER = Logger.getLogger(AccessExample.class.getName());
	private Boolean acabar;
	private SafeQueue<PeticioServidor> cua;
	private int size;
	private Thread t;

	public QuadratServiceImpl(int size) {
		this.size = size;
		this.acabar = false;
		this.cua = new SafeQueue<PeticioServidor>(size);
		this.t = new Thread(this, "quadrat");
		this.t.start();

	}

	@Override
	public Future<Double> request(double num) throws InterruptedException {
		PeticioServidor peticio = new PeticioServidor();
		peticio.cfuture = new CompletableFuture<>();
		peticio.num = num;
		cua.put(peticio);

		return peticio.cfuture;
	}

	@Override
	public void shutdown() {
		synchronized (acabar) {
			acabar = true;
			t.interrupt();
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
			PeticioServidor peticio;
			try {
				peticio = cua.take();
//				LOGGER.info("numero para operar " + peticio.num);
				double numero = peticio.num;
				double quadrat = numero * numero;

				Thread.sleep(1500);
				peticio.cfuture.complete(quadrat);

			} catch (InterruptedException e) {
				LOGGER.info("interrupted!");
			}

		}
	}

	static class PeticioServidor {
		public CompletableFuture<Double> cfuture;
		public double num;

	}

}

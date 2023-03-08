package m09uf2.impl.timer2;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.function.Consumer;
import java.util.logging.Logger;

import m09uf2.prob.timer2.Timer2;

public class Timer2Impl implements Timer2, Runnable {
	static final Logger LOGGER = Logger.getLogger(Timer2Test.class.getName());
	private int idObjeto;
	private ArrayList<Timer2Object> lista = new ArrayList<Timer2Object>();
	private Thread thread;
	private Timer2Object alarma;

	private volatile boolean acabar = false;

	public Timer2Impl() {
		this.idObjeto = 0;
		this.thread = new Thread(this, "timer");
		thread.start();
	}

	@Override
	public int set(long millis, Consumer<Integer> handler) {

		idObjeto++;
		alarma = new Timer2Object(idObjeto, handler, millis);
		lista.add(alarma);

		try {
			thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return idObjeto;

	}

	@Override
	public void run() {

		while (acabar == false) {
			int contador = 0;

			if (lista.isEmpty()) {

				System.out.println("esperando");
			} else {

				if (!lista.isEmpty()) {
					lista.sort(new Comparator<Timer2Object>() {
						@Override
						public int compare(Timer2Object o1, Timer2Object o2) {
							return o1.compareTo(o2);
						}

					});

					if (lista.get(contador).isPasado() == false) {

						try {
							Thread.sleep( lista.get(contador).getMillis());
							lista.get(contador).getHandler().accept(lista.get(contador).getId());
							lista.get(contador).setPasado(true);
					
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						
					}
					contador++;

				}
			}

		}

	}

	@Override
	public void shutdown() {
		this.acabar = true;
		thread.interrupt();

	}

}

package m09uf2.impl.waittaskb;

import m09uf2.prob.waittaskb.DoTask;
import m09uf2.prob.waittaskb.WaitTaskJoin;

public class WaitTaskJoinImpl<T> implements WaitTaskJoin<T>, Runnable {

	private Thread thread;
	private long tiempo;
	private DoTask<T> tarea;
	private T aux = null;
	private T cancelado = null;

	public WaitTaskJoinImpl() {
		this.thread = new Thread(this, "thread");
	}

	@Override
	public void submit(long millis, DoTask<T> task) {
		tiempo = millis;
		tarea = task;
		thread.start();
	}

	@Override
	public T result() {
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return aux;
	}

	@Override
	public void cancel(T t) {
		cancelado = t;
		thread.interrupt();

	}

	@Override
	public void run() {
		try {
			Thread.sleep(tiempo);
			aux = tarea.execute();
		} catch (InterruptedException e) {
			aux = cancelado;
		}

	}

}

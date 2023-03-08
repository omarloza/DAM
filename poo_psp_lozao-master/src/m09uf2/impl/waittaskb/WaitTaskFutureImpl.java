package m09uf2.impl.waittaskb;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import m09uf2.prob.waittaskb.DoTask;
import m09uf2.prob.waittaskb.WaitTaskFuture;

public class WaitTaskFutureImpl<T> implements WaitTaskFuture<T>, Runnable {

	private Thread thread;
	private long tiempo;
	private CompletableFuture<T> cf;
	private DoTask<T> tarea;
	private T aux = null;
	private T cancelado = null;

	public WaitTaskFutureImpl() {
		this.thread = new Thread(this, "thread");
		this.cf = new CompletableFuture<T>();
	}

	@Override
	public void submit(long millis, DoTask<T> task) {
		tiempo = millis;
		tarea = task;
		thread.start();

	}

	@Override
	public Future<T> future() {
		return cf;
	}

	@Override
	public void cancel(T t) {
		
		System.out.println(t);
		cancelado=t;
		System.out.println(cancelado);
		thread.interrupt();
	}

	@Override
	public void run() {
		try {
			Thread.sleep(tiempo);
			aux = tarea.execute();
			cf.complete(aux);

		} catch (InterruptedException e) {
			cf.complete(cancelado);
		}

	}

}

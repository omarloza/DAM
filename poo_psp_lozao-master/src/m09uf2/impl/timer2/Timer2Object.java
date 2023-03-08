package m09uf2.impl.timer2;

import java.util.function.Consumer;

public class Timer2Object implements Comparable<Timer2Object> {

	private int id;
	private Consumer<Integer> handler;
	private long millis;
	private boolean pasado;

	public Timer2Object(int id, Consumer<Integer> handler, long millis) {
		super();
		this.id = id;
		this.handler = handler;
		this.millis = millis;
		this.pasado = false;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Consumer<Integer> getHandler() {
		return handler;
	}

	public void setHandler(Consumer<Integer> handler) {
		this.handler = handler;
	}

	public long getMillis() {
		return millis;
	}

	public void setMillis(long millis) {
		this.millis = millis;
	}

	public boolean isPasado() {
		return pasado;
	}

	public void setPasado(boolean pasado) {
		this.pasado = pasado;
	}

	@Override
	public String toString() {
		return "[id=" + id + ", handler=" + handler + ", millis=" + millis + "]";
	}

	@Override
	public int compareTo(Timer2Object o) {
		int resultado = 0;

		if (this.millis < o.millis) {
			resultado = -1;
		} else if (this.millis > o.millis) {
			resultado = 1;
		} else {
			resultado = 0;
		}

		return resultado;
	}

}

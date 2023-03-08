package m09uf2.impl.timer1;


import m09uf2.prob.timer1.Timer1;

public class Timer1Impl implements Timer1 {

	private long millis  ;
	private Runnable runnable;
	
	private Thread thread ;
	
	
	public Timer1Impl(long millis) {
		this.millis = millis;
		this.runnable = new Timer1Runnable(millis);
		this.thread=new Thread(runnable, "timer2"); 
	}

	@Override
	public long millis() {
		return millis;
	}

	@Override
	public void go() {
	thread.start();
		
	}

	@Override
	public void cancel() {
	thread.interrupt();

	}

	@Override
	public void hold() {

	try {

	thread.join(millis);
		
	} catch (InterruptedException e) {

		System.out.println("Temporizador cancelado");
	}

	}

	@Override
	public boolean done() {
		if((thread.isAlive()==false)) {
			return true;
		}
			return false;

		
	}

	@Override
	public Thread getThread() {

		return thread;
	}

}

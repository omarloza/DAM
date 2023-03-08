package m09uf2.impl.timer1;


import java.util.logging.Logger;

import lib.Threads;
import m09uf2.play.PrimerFilTest;

public class Timer1Runnable implements Runnable {
	static final Logger LOGGER = Logger.getLogger(PrimerFilTest.class.getName());
	private long millis;


	
	public Timer1Runnable(long millis) {
		this.millis=millis;
	}

	@Override
	public void run() {
		
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			System.out.println("timer cancelado");
		}
		
		

}
}
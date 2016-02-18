package area51.turboRocketWars.regeneration;


public abstract class Regenerator implements Runnable {

	protected long frequency;
	private boolean stop = false;

	public Regenerator(long frequency){
		this.frequency = frequency;
		new Thread(this).start();
	}
	
	public abstract void regen();

	public void run(){
		while(!stop){
			regen();
			try {
				Thread.sleep(frequency);
			} catch (InterruptedException e) {}
		}
	}
	
	public boolean hasStopped(){
		return stop;
	}

	public void stop(){
		stop = true;
	}


}

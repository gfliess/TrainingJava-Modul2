package at.edu.hti.concurrency;

public interface StoreEventListener {

	public void notifyStart();
	public void notifyDone();
}

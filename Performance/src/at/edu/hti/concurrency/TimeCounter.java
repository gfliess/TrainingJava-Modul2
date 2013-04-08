package at.edu.hti.concurrency;

public class TimeCounter implements StoreEventListener {

	private long start;
	private long duration;

	@Override
	public void notifyStart() {
		start = System.currentTimeMillis();

	}

	@Override
	public void notifyDone() {
		duration = System.currentTimeMillis() - start;

	}

	public long getDuration() {
		return duration;
	}

}

package at.edu.hti.concurrency;

import java.util.concurrent.atomic.AtomicInteger;

public class Consumer implements Runnable {

	private Store store;
	public static AtomicInteger read = new AtomicInteger();

	public Consumer(Store store) {
		super();
		this.store = store;
	}

	@Override
	public void run() {

		while (store.active()) {
			store.remove();
			synchronized (read) {				
			if (store.active() && read.incrementAndGet() == ProducerConsumerTestApp.ITEMS*ProducerConsumerTestApp.maxProducers)
				store.shutDown();
			}
		}
	}
}

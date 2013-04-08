package at.edu.hti.concurrency;

import java.util.concurrent.atomic.AtomicInteger;

public class Producer implements Runnable {

	private Store store;

	public static AtomicInteger written = new AtomicInteger();
	
	public Producer(Store store) {
		super();
		this.store = store;
		this.store.incProducer();
	}

	@Override
	public void run() {

		for (int count = 0; count < ProducerConsumerTestApp.ITEMS; count++) {

			String string = Thread.currentThread().getName() + " data" + count;

			store.add(string);
			synchronized (written) {
				
				written.incrementAndGet();
			}
		}
		store.decProducer();
		
	}

}

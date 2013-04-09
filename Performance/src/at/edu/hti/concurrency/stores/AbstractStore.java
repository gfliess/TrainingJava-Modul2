package at.edu.hti.concurrency.stores;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import at.edu.hti.concurrency.Store;
import at.edu.hti.concurrency.StoreEventListener;

public abstract class AbstractStore<T extends Collection<String>> implements
		Store {

	AtomicInteger producer = new AtomicInteger();

	T elements;
	// AtomicInteger maxItems = new AtomicInteger(1000000);
	List<StoreEventListener> listener = new ArrayList<StoreEventListener>();
	private boolean notified;

	private boolean active =true;

	public void clear() {
		elements.clear();
	}

	@Override
	public synchronized boolean active() {

		return active;
	}

	private void notifyListenerDone() {
		if (notified)
			return;
		for (StoreEventListener list : listener) {
			list.notifyDone();
		}
		notified = true;
	}

	@Override
	public synchronized void incProducer() {
		producer.incrementAndGet();

	}

	@Override
	public synchronized void decProducer() {
		producer.decrementAndGet();

	}

	@Override
	public void registerListener(StoreEventListener listener) {
		this.listener.add(listener);
	}

	@Override
	public int size() {

		return elements.size();
	}
	
	public void shutDown()
	{
		active=false;
		notifyListenerDone();
	}

}

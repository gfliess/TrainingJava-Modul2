package at.edu.hti.concurrency.stores;

import at.edu.hti.concurrency.Store;
import at.edu.hti.concurrency.StoreEventListener;

public class EmptyStore implements Store{

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void initMaxSize(int maxSize) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void add(String data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String remove() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String removeItem(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void registerListener(StoreEventListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean active() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void shutDown() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void incProducer() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void decProducer() {
		// TODO Auto-generated method stub
		
	}

}

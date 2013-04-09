package at.edu.hti.concurrency.stores;

import java.util.ArrayList;
import java.util.List;

import at.edu.hti.concurrency.Store;
import at.edu.hti.concurrency.StoreEventListener;

public class Store57 implements Store {

	private List<String> list;
	
	@Override
	public String getName() {
		return "Store57";
	}

	@Override
	public void initMaxSize(int maxSize) {
		if(maxSize > 0){
			list = new ArrayList<String>(maxSize);
		}
	}

	@Override
	public void add(String data) {
		if(data != null){
			list.add(data);
		}
	}

	@Override
	public String remove() {
		return list.remove(list.size()-1);
	}

	@Override
	public String removeItem(int index) {
		if(index < list.size()) {
			return list.remove(index);
		}
		return null;
	}

	@Override
	public int size() {
		return list.size();
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
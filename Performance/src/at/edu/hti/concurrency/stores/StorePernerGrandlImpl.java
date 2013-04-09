package at.edu.hti.concurrency.stores;

import java.util.ArrayList;
import java.util.List;

import at.edu.hti.concurrency.Store;
import at.edu.hti.concurrency.StoreEventListener;

public class StorePernerGrandlImpl implements Store {

	final static int DEFAULT_MAXSIZE = 100;

	Integer maxSize;
	
	List<String> storeItems;
	
	private List<String> getStoreItems() {
		if( null == storeItems ) {
			storeItems = new ArrayList<String>(maxSize != null ? maxSize : DEFAULT_MAXSIZE );
		}
		return storeItems;
	}
	
	@Override
	public String getName() {
		return this.getClass().getSimpleName();
	}

	@Override
	public void initMaxSize(int maxSize) {
		this.maxSize = new Integer(maxSize);
	}

	@Override
	public void add(String data) {
		getStoreItems().add(data);
	}

	@Override
	public String remove() {
		return this.removeItem( 0 );
	}

	@Override
	public String removeItem(int index) {
		List<String> list = getStoreItems();
		String removeItem = list.get( index );
		list.remove( index );
		return removeItem;
	}

	@Override
	public int size() {
		return storeItems.size();
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

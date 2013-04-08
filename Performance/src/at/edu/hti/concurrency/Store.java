package at.edu.hti.concurrency;

public interface Store {

	String getName();

	void initMaxSize(int maxSize);

	void add(String data);

	String remove();

	String removeItem(int index);

	int size();

	
	void registerListener(StoreEventListener listener);

	boolean active();

	void shutDown();

	
	
	void incProducer();

	void decProducer();

}

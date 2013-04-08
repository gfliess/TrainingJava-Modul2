package at.edu.hti.concurrency;

public interface Store {

	//Methoden fuer Beispiel 2
	String getName();

	void initMaxSize(int maxSize);

	void add(String data);

	String remove();

	String removeItem(int index);

	int size();

	//Methoden fuer Beispiel 3
	void registerListener(StoreEventListener listener);

	boolean active();

	void shutDown();

	
	//Methoden fuer Beispiel 4
	void incProducer();

	void decProducer();

}

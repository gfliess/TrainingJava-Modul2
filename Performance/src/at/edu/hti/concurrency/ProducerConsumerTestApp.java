package at.edu.hti.concurrency;

import java.util.ArrayList;
import java.util.List;

import at.edu.hti.concurrency.util.InstanceUtil;

public class ProducerConsumerTestApp {
	public static final int ITEMS = 2000;
	public static int maxProducers = 100;

	public static void main(String[] args) throws Exception {

		List<Store> stores = InstanceUtil.returnAvailableStoreImplementations();
		 for (Store st : stores)
		{

			int maxConsumer = 100;
			TimeCounter counter = new TimeCounter();
			st.registerListener(counter);

			st.initMaxSize(ITEMS);
			List<Producer> producers = new ArrayList<Producer>(maxProducers);
			List<Consumer> consumer = new ArrayList<Consumer>(maxConsumer);

			for (int count = 0; count < maxProducers; count++) {
				producers.add(new Producer(st));
			}

			for (int count = 0; count < maxConsumer; count++) {
				consumer.add(new Consumer(st));
			}
		
			counter.notifyStart();

			//TODO add instance
			Starter starter = null; 
			starter.run(st, producers, consumer);
			System.out.println(st.size() + "  " + Consumer.read.intValue()
					+ " " + Producer.written.intValue());
			System.out.println(st.getName() + ":" + counter.getDuration());

		}

	}

}

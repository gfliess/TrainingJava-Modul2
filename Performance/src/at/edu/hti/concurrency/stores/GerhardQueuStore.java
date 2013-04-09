package at.edu.hti.concurrency.stores;

import java.util.concurrent.LinkedBlockingQueue;

public class GerhardQueuStore extends
		AbstractStore<LinkedBlockingQueue<String>> {

	private int maxSize = 0;

	@Override
	public void initMaxSize(int maxSize) {
		this.maxSize = maxSize;

		elements = new LinkedBlockingQueue<String>(maxSize);
	}

	@Override
	public void add(String data) {

		try {
			elements.put(data);

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public String remove() {
		String elem = null;
		try {
			elem = elements.take();

		} catch (InterruptedException e) {
			if (active())
				e.printStackTrace();
		}
		return elem;
	}

	@Override
	public String getName() {

		return "GerhardQueue";
	}

	@Override
	public String removeItem(int index) {

		if (index > elements.size())
			return null;

		String result = null;
		try {

			LinkedBlockingQueue<String> tempElements = new LinkedBlockingQueue<String>(
					maxSize);
			for (int searchIndex = 0; index < elements.size(); index++) {
				if (searchIndex == index) {
					result = tempElements.remove();
				} else {
					tempElements.put(elements.take());
				}
			}
			elements = tempElements;

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return result;
	}

}

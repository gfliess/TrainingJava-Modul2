package at.edu.hti.concurrency;

import java.util.List;

public interface Starter  {
	
	public void run(Store store , List<Producer> p , List<Consumer> c);
}

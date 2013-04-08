package at.edu.hti.concurrency.server;

import java.net.InetSocketAddress;

import at.edu.hti.concurrency.Store;

public interface ServiceHandler extends Runnable {

	 void init(InetSocketAddress socket, Store store);

}
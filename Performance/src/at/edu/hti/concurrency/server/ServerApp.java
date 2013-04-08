package at.edu.hti.concurrency.server;

import java.net.InetSocketAddress;

import at.edu.hti.concurrency.Store;

public class ServerApp {

	public static void main(String[] args) {
		// TODO add instance
		Store store = null;
		store.initMaxSize(10000);
		InetSocketAddress addr = new InetSocketAddress(8000);

		// TODO add instance
		ServiceHandler server = null;
		server.init(addr, store);

		// TODO add instance;
		ServerStarter starter = null;
		starter.startService(server);

	}

}

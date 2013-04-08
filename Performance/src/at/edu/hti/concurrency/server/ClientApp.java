package at.edu.hti.concurrency.server;

import java.net.InetSocketAddress;
import java.util.ArrayList;

import at.edu.hti.concurrency.server.Client.Type;

public class ClientApp {

	public static void main(String[] args) {

		InetSocketAddress addr = new InetSocketAddress(8000);

		ArrayList<Client> clients = new ArrayList<Client>();

		for (int count = 0; count < 10; count++) {
			clients.add(createProducer(addr, count));
			clients.add(createConsumer(addr, count));
		}
		// TODO Add instance
		ClientStarter starter = null;//
		starter.startClients(clients);

	}

	protected static Client createProducer(InetSocketAddress addr, int count) {
		// TODO add instance
		Client nioClient = null;
		nioClient.init(""+count, Type.Producer, addr, 100);
		return nioClient;
	}

	protected static Client createConsumer(InetSocketAddress addr, int count) {
		// TODO add instance¯
		Client nioClient = null;
		nioClient.init(""+count, Type.Consumer, addr, 100);
		return nioClient;
	}

}

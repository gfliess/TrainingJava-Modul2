package at.edu.hti.concurrency.server;

import java.net.InetSocketAddress;

public interface Client extends Runnable {
	public enum Type {
		Producer("p"), Consumer("c");

		private String prefix;

		Type(String prefix) {
			this.prefix = prefix;
		}

		public String getPrefix() {
			return prefix;
		}
	};

	String EXIT_REQUEST = "exit";

	void init(String id, Type type, InetSocketAddress socketAddress,
			int maxIterations);

}

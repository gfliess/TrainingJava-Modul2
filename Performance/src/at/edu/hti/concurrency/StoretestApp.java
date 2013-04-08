package at.edu.hti.concurrency;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import at.edu.hti.concurrency.util.InstanceUtil;

public class StoretestApp {

	public static void main(String[] args) throws Exception {

		int[] synTestValues = new int[] { 1000, 10000, 100000, 200000 };

		int[] ranTestValues = new int[] { 1000, 10000, 100000 };

		StringBuilder builder = new StringBuilder();
		printHeaderLine(synTestValues, ranTestValues, builder);
		List<Store> stores = InstanceUtil.returnAvailableStoreImplementations();

		for (Store store : stores) {
			System.out.println(store.getName());
			builder.append(store.getName() + ";");

			for (int testCount : synTestValues) {
				System.out.print(testCount + "..");
				builder.append(testSeq(store, testCount) + ";");
				System.out.println();
			}

			for (int testCount : ranTestValues) {
				System.out.print(testCount + "..");
				builder.append(testRna(store, testCount) + ";");
				System.out.println();
			}
			builder.append("\n");

		}

		System.out.println(builder);

		FileWriter test1Csv = new FileWriter("test1.csv");
		test1Csv.append(builder);
		test1Csv.close();
	}

	protected static void printHeaderLine(int[] synTestValues,
			int[] ranTestValues, Appendable builder) throws IOException {

		builder.append("Store;");
		for (int i : synTestValues) {
			builder.append("syn " + i + ";");
		}
		for (int i : ranTestValues) {
			builder.append("ran " + i + ";");
		}
		builder.append("\n");
	}

	private static long testSeq(Store store, int i) {
		store.initMaxSize(i);
		long start = System.currentTimeMillis();
		for (int count = 0; count < i; count++) {
			store.add("data" + count);
		}

		for (int count = 0; count < i; count++) {
			store.remove();
		}
		
		if (store.size() > 0) {
			throw new RuntimeException("store not empty");
		}
	
		return System.currentTimeMillis() - start;

	}

	private static long testRna(Store store, int i) {
		store.initMaxSize(i);
		long start = System.currentTimeMillis();
		for (int count = 0; count < i; count++) {
			store.add("data" + count);
		}

		for (int count = i; count > 0; count--) {
			store.removeItem(count / 2);
		}

		if (store.size() > 0) {
			throw new RuntimeException("store not empty");
		}
	
		return System.currentTimeMillis() - start;

	}
}

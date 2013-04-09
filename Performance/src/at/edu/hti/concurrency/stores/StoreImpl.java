package at.edu.hti.concurrency.stores;

import java.util.LinkedList;

import at.edu.hti.concurrency.Store;
import at.edu.hti.concurrency.StoreEventListener;

/**
 * Store implementation with LinkedList
 */
public class StoreImpl implements Store {

    private LinkedList<String> queue = new LinkedList<String>();

    @Override
    public String getName() {
        return "StoreImpl";
    }

    @Override
    public void initMaxSize(int maxSize) {
        // TODO Auto-generated method stub

    }

    @Override
    public void add(String data) {
        queue.addFirst(data);

    }

    @Override
    public String remove() {
        return queue.removeLast();

    }

    @Override
    public String removeItem(int index) {
        return queue.remove(index);
    }

    @Override
    public int size() {
        return queue.size();
    }

    @Override
    public void registerListener(StoreEventListener listener) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean active() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void shutDown() {
        // TODO Auto-generated method stub

    }

    @Override
    public void incProducer() {
        // TODO Auto-generated method stub

    }

    @Override
    public void decProducer() {
        // TODO Auto-generated method stub

    }

}

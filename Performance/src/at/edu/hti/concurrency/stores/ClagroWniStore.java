/** 
 * Copyright 2013 SSI Schaefer PEEM GmbH. All Rights reserved. 
 * <br /> <br />
 * 
 * $Id$
 * <br /> <br />
 *
 */

package at.edu.hti.concurrency.stores;

import java.util.Collections;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicInteger;

import at.edu.hti.concurrency.Store;
import at.edu.hti.concurrency.StoreEventListener;

/**
 * Simple implementation of {@link Store}
 * 
 * @author nickl
 * @version $Revision$
 */

public class ClagroWniStore implements Store {

  //some change 

  private static final String name = "[CLAGRO_WNI_STORE]";
  private final Queue<StoreEventListener> listeners = Collections.asLifoQueue(new ConcurrentLinkedDeque<StoreEventListener>());
  private final Queue<String> data = Collections.asLifoQueue(new ConcurrentLinkedDeque<String>());
  private final AtomicInteger producerCount = new AtomicInteger(0);
  private int maxSize;

  @Override
  public boolean active() {
    System.out.println("active(): true");
    return true;
  }

  @Override
  public void add(String str) {
    this.data.add(str);
  }

  @Override
  public void decProducer() {
    producerCount.decrementAndGet();
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public void incProducer() {
    producerCount.incrementAndGet();
  }

  @Override
  public void initMaxSize(int maxSize) {
    this.maxSize = maxSize;
  }

  @Override
  public void registerListener(StoreEventListener listener) {
    listeners.add(listener);
  }

  @Override
  public String remove() {
    return this.data.remove();
  }

  @Override
  public String removeItem(int index) {
    String str = (String) data.toArray()[index];
    data.remove(str);
    return str;
  }

  @Override
  public void shutDown() {
    System.out.println("shutdow()");
  }

  @Override
  public int size() {
    return data.size();
  }
}

//---------------------------- Revision History ----------------------------
//$Log$
//

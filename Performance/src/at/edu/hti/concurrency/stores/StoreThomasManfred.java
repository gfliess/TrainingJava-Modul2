/** 
 * Copyright 2013 SSI Schaefer PEEM GmbH. All Rights reserved. 
 * <br /> <br />
 * 
 * $Id$
 * <br /> <br />
 *
 */

package at.edu.hti.concurrency.stores;

import java.util.ArrayList;
import java.util.List;

import at.edu.hti.concurrency.Store;
import at.edu.hti.concurrency.StoreEventListener;

/**
 * This is the class header. The first sentence (ending with "."+SPACE) is important, because it is
 * used summary in the package overview pages.<br />
 * <br />
 * 
 * @author goedl
 * @version $Revision$
 */

public class StoreThomasManfred implements Store {
  List<String> dataList = new ArrayList<String>();

  @Override
  public String getName() {
    return this.getClass().getSimpleName();
  }

  @Override
  public void initMaxSize(int maxSize) {
  }

  @Override
  public void add(String data) {
    dataList.add(data);
  }

  @Override
  public String remove() {
    return removeItem(0);
  }

  @Override
  public String removeItem(int index) {
    if (!dataList.isEmpty()) {
      return dataList.remove(index);
    }
    return null;
  }

  @Override
  public int size() {
    return dataList.size();
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

//---------------------------- Revision History ----------------------------
//$Log$
//

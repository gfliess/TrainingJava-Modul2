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
 * @author ebe
 * @version $Revision$
 */

public class StoreNE implements Store {

  private List<String> list;

  public StoreNE() {
  }

  @Override
  public String getName() {
    return "Ewald/Nikolett";
  }

  @Override
  public void initMaxSize(int maxSize) {
    list = new ArrayList<String>(maxSize);

  }

  @Override
  public void add(String data) {
    list.add(data);
  }

  @Override
  public String remove() {
    return list.remove(0);
  }

  @Override
  public String removeItem(int index) {
    return list.remove(index);
  }

  @Override
  public int size() {
    return list.size();
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

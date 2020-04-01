package org.apache.commons.net.util;

import java.io.Serializable;
import java.util.EventListener;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public class ListenerList implements Serializable, Iterable<EventListener> {
  private static final long serialVersionUID = -1934227607974228213L;
  
  private final CopyOnWriteArrayList<EventListener> __listeners = new CopyOnWriteArrayList<>();
  
  public void addListener(EventListener listener) {
    this.__listeners.add(listener);
  }
  
  public void removeListener(EventListener listener) {
    this.__listeners.remove(listener);
  }
  
  public int getListenerCount() {
    return this.__listeners.size();
  }
  
  public Iterator<EventListener> iterator() {
    return this.__listeners.iterator();
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\apache\commons\ne\\util\ListenerList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
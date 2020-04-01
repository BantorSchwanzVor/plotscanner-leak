package org.apache.commons.net.nntp;

import java.util.Iterator;

class NewsgroupIterator implements Iterator<NewsgroupInfo>, Iterable<NewsgroupInfo> {
  private final Iterator<String> stringIterator;
  
  public NewsgroupIterator(Iterable<String> iterableString) {
    this.stringIterator = iterableString.iterator();
  }
  
  public boolean hasNext() {
    return this.stringIterator.hasNext();
  }
  
  public NewsgroupInfo next() {
    String line = this.stringIterator.next();
    return NNTPClient.__parseNewsgroupListEntry(line);
  }
  
  public void remove() {
    this.stringIterator.remove();
  }
  
  public Iterator<NewsgroupInfo> iterator() {
    return this;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\apache\commons\net\nntp\NewsgroupIterator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
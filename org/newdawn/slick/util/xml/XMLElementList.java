package org.newdawn.slick.util.xml;

import java.util.ArrayList;
import java.util.Collection;

public class XMLElementList {
  private ArrayList list = new ArrayList();
  
  public void add(XMLElement element) {
    this.list.add(element);
  }
  
  public int size() {
    return this.list.size();
  }
  
  public XMLElement get(int i) {
    return this.list.get(i);
  }
  
  public boolean contains(XMLElement element) {
    return this.list.contains(element);
  }
  
  public void addAllTo(Collection collection) {
    collection.addAll(this.list);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\newdawn\slic\\util\xml\XMLElementList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
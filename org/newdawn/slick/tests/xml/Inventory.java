package org.newdawn.slick.tests.xml;

import java.util.ArrayList;

public class Inventory {
  private ArrayList items = new ArrayList();
  
  private void add(Item item) {
    this.items.add(item);
  }
  
  public void dump(String prefix) {
    System.out.println(String.valueOf(prefix) + "Inventory");
    for (int i = 0; i < this.items.size(); i++)
      ((Item)this.items.get(i)).dump(String.valueOf(prefix) + "\t"); 
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\newdawn\slick\tests\xml\Inventory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
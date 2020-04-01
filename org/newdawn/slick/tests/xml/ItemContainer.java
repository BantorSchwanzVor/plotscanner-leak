package org.newdawn.slick.tests.xml;

import java.util.ArrayList;

public class ItemContainer extends Item {
  private ArrayList items = new ArrayList();
  
  private void add(Item item) {
    this.items.add(item);
  }
  
  private void setName(String name) {
    this.name = name;
  }
  
  private void setCondition(int condition) {
    this.condition = condition;
  }
  
  public void dump(String prefix) {
    System.out.println(String.valueOf(prefix) + "Item Container " + this.name + "," + this.condition);
    for (int i = 0; i < this.items.size(); i++)
      ((Item)this.items.get(i)).dump(String.valueOf(prefix) + "\t"); 
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\newdawn\slick\tests\xml\ItemContainer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
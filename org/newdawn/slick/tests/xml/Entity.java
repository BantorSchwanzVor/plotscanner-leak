package org.newdawn.slick.tests.xml;

public class Entity {
  private float x;
  
  private float y;
  
  private Inventory invent;
  
  private Stats stats;
  
  private void add(Inventory inventory) {
    this.invent = inventory;
  }
  
  private void add(Stats stats) {
    this.stats = stats;
  }
  
  public void dump(String prefix) {
    System.out.println(String.valueOf(prefix) + "Entity " + this.x + "," + this.y);
    this.invent.dump(String.valueOf(prefix) + "\t");
    this.stats.dump(String.valueOf(prefix) + "\t");
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\newdawn\slick\tests\xml\Entity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
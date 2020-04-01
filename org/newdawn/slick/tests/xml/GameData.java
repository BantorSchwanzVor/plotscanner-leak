package org.newdawn.slick.tests.xml;

import java.util.ArrayList;

public class GameData {
  private ArrayList entities = new ArrayList();
  
  private void add(Entity entity) {
    this.entities.add(entity);
  }
  
  public void dump(String prefix) {
    System.out.println(String.valueOf(prefix) + "GameData");
    for (int i = 0; i < this.entities.size(); i++)
      ((Entity)this.entities.get(i)).dump(String.valueOf(prefix) + "\t"); 
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\newdawn\slick\tests\xml\GameData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
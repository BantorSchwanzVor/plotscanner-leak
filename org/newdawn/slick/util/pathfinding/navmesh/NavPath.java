package org.newdawn.slick.util.pathfinding.navmesh;

import java.util.ArrayList;

public class NavPath {
  private ArrayList links = new ArrayList();
  
  public void push(Link link) {
    this.links.add(link);
  }
  
  public int length() {
    return this.links.size();
  }
  
  public float getX(int step) {
    return ((Link)this.links.get(step)).getX();
  }
  
  public float getY(int step) {
    return ((Link)this.links.get(step)).getY();
  }
  
  public String toString() {
    return "[Path length=" + length() + "]";
  }
  
  public void remove(int i) {
    this.links.remove(i);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\newdawn\slic\\util\pathfinding\navmesh\NavPath.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
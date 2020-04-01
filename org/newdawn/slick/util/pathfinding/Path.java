package org.newdawn.slick.util.pathfinding;

import java.io.Serializable;
import java.util.ArrayList;

public class Path implements Serializable {
  private static final long serialVersionUID = 1L;
  
  private ArrayList steps = new ArrayList();
  
  public int getLength() {
    return this.steps.size();
  }
  
  public Step getStep(int index) {
    return this.steps.get(index);
  }
  
  public int getX(int index) {
    return (getStep(index)).x;
  }
  
  public int getY(int index) {
    return (getStep(index)).y;
  }
  
  public void appendStep(int x, int y) {
    this.steps.add(new Step(x, y));
  }
  
  public void prependStep(int x, int y) {
    this.steps.add(0, new Step(x, y));
  }
  
  public boolean contains(int x, int y) {
    return this.steps.contains(new Step(x, y));
  }
  
  public class Step implements Serializable {
    private int x;
    
    private int y;
    
    public Step(int x, int y) {
      this.x = x;
      this.y = y;
    }
    
    public int getX() {
      return this.x;
    }
    
    public int getY() {
      return this.y;
    }
    
    public int hashCode() {
      return this.x * this.y;
    }
    
    public boolean equals(Object other) {
      if (other instanceof Step) {
        Step o = (Step)other;
        return (o.x == this.x && o.y == this.y);
      } 
      return false;
    }
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\newdawn\slic\\util\pathfinding\Path.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
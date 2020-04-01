package org.newdawn.slick.util.pathfinding.navmesh;

public class Link {
  private float px;
  
  private float py;
  
  private Space target;
  
  public Link(float px, float py, Space target) {
    this.px = px;
    this.py = py;
    this.target = target;
  }
  
  public float distance2(float tx, float ty) {
    float dx = tx - this.px;
    float dy = ty - this.py;
    return dx * dx + dy * dy;
  }
  
  public float getX() {
    return this.px;
  }
  
  public float getY() {
    return this.py;
  }
  
  public Space getTarget() {
    return this.target;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\newdawn\slic\\util\pathfinding\navmesh\Link.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
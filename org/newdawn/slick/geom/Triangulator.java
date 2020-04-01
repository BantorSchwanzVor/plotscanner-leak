package org.newdawn.slick.geom;

import java.io.Serializable;

public interface Triangulator extends Serializable {
  int getTriangleCount();
  
  float[] getTrianglePoint(int paramInt1, int paramInt2);
  
  void addPolyPoint(float paramFloat1, float paramFloat2);
  
  void startHole();
  
  boolean triangulate();
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\newdawn\slick\geom\Triangulator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
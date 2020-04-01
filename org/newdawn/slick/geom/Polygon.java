package org.newdawn.slick.geom;

import java.util.ArrayList;

public class Polygon extends Shape {
  private boolean allowDups = false;
  
  private boolean closed = true;
  
  public Polygon(float[] points) {
    int length = points.length;
    this.points = new float[length];
    this.maxX = -1.4E-45F;
    this.maxY = -1.4E-45F;
    this.minX = Float.MAX_VALUE;
    this.minY = Float.MAX_VALUE;
    this.x = Float.MAX_VALUE;
    this.y = Float.MAX_VALUE;
    for (int i = 0; i < length; i++) {
      this.points[i] = points[i];
      if (i % 2 == 0) {
        if (points[i] > this.maxX)
          this.maxX = points[i]; 
        if (points[i] < this.minX)
          this.minX = points[i]; 
        if (points[i] < this.x)
          this.x = points[i]; 
      } else {
        if (points[i] > this.maxY)
          this.maxY = points[i]; 
        if (points[i] < this.minY)
          this.minY = points[i]; 
        if (points[i] < this.y)
          this.y = points[i]; 
      } 
    } 
    findCenter();
    calculateRadius();
    this.pointsDirty = true;
  }
  
  public Polygon() {
    this.points = new float[0];
    this.maxX = -1.4E-45F;
    this.maxY = -1.4E-45F;
    this.minX = Float.MAX_VALUE;
    this.minY = Float.MAX_VALUE;
  }
  
  public void setAllowDuplicatePoints(boolean allowDups) {
    this.allowDups = allowDups;
  }
  
  public void addPoint(float x, float y) {
    if (hasVertex(x, y) && !this.allowDups)
      return; 
    ArrayList<Float> tempPoints = new ArrayList();
    for (int i = 0; i < this.points.length; i++)
      tempPoints.add(new Float(this.points[i])); 
    tempPoints.add(new Float(x));
    tempPoints.add(new Float(y));
    int length = tempPoints.size();
    this.points = new float[length];
    for (int j = 0; j < length; j++)
      this.points[j] = ((Float)tempPoints.get(j)).floatValue(); 
    if (x > this.maxX)
      this.maxX = x; 
    if (y > this.maxY)
      this.maxY = y; 
    if (x < this.minX)
      this.minX = x; 
    if (y < this.minY)
      this.minY = y; 
    findCenter();
    calculateRadius();
    this.pointsDirty = true;
  }
  
  public Shape transform(Transform transform) {
    checkPoints();
    Polygon resultPolygon = new Polygon();
    float[] result = new float[this.points.length];
    transform.transform(this.points, 0, result, 0, this.points.length / 2);
    resultPolygon.points = result;
    resultPolygon.findCenter();
    resultPolygon.closed = this.closed;
    return resultPolygon;
  }
  
  public void setX(float x) {
    super.setX(x);
    this.pointsDirty = false;
  }
  
  public void setY(float y) {
    super.setY(y);
    this.pointsDirty = false;
  }
  
  protected void createPoints() {}
  
  public boolean closed() {
    return this.closed;
  }
  
  public void setClosed(boolean closed) {
    this.closed = closed;
  }
  
  public Polygon copy() {
    float[] copyPoints = new float[this.points.length];
    System.arraycopy(this.points, 0, copyPoints, 0, copyPoints.length);
    return new Polygon(copyPoints);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\newdawn\slick\geom\Polygon.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
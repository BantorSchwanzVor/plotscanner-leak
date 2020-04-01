package org.newdawn.slick.geom;

import java.util.ArrayList;

public class MorphShape extends Shape {
  private ArrayList shapes = new ArrayList();
  
  private float offset;
  
  private Shape current;
  
  private Shape next;
  
  public MorphShape(Shape base) {
    this.shapes.add(base);
    float[] copy = base.points;
    this.points = new float[copy.length];
    this.current = base;
    this.next = base;
  }
  
  public void addShape(Shape shape) {
    if (shape.points.length != this.points.length)
      throw new RuntimeException("Attempt to morph between two shapes with different vertex counts"); 
    Shape prev = this.shapes.get(this.shapes.size() - 1);
    if (equalShapes(prev, shape)) {
      this.shapes.add(prev);
    } else {
      this.shapes.add(shape);
    } 
    if (this.shapes.size() == 2)
      this.next = this.shapes.get(1); 
  }
  
  private boolean equalShapes(Shape a, Shape b) {
    a.checkPoints();
    b.checkPoints();
    for (int i = 0; i < a.points.length; i++) {
      if (a.points[i] != b.points[i])
        return false; 
    } 
    return true;
  }
  
  public void setMorphTime(float time) {
    int p = (int)time;
    int n = p + 1;
    float offset = time - p;
    p = rational(p);
    n = rational(n);
    setFrame(p, n, offset);
  }
  
  public void updateMorphTime(float delta) {
    this.offset += delta;
    if (this.offset < 0.0F) {
      int index = this.shapes.indexOf(this.current);
      if (index < 0)
        index = this.shapes.size() - 1; 
      int nframe = rational(index + 1);
      setFrame(index, nframe, this.offset);
      this.offset++;
    } else if (this.offset > 1.0F) {
      int index = this.shapes.indexOf(this.next);
      if (index < 1)
        index = 0; 
      int nframe = rational(index + 1);
      setFrame(index, nframe, this.offset);
      this.offset--;
    } else {
      this.pointsDirty = true;
    } 
  }
  
  public void setExternalFrame(Shape current) {
    this.current = current;
    this.next = this.shapes.get(0);
    this.offset = 0.0F;
  }
  
  private int rational(int n) {
    while (n >= this.shapes.size())
      n -= this.shapes.size(); 
    while (n < 0)
      n += this.shapes.size(); 
    return n;
  }
  
  private void setFrame(int a, int b, float offset) {
    this.current = this.shapes.get(a);
    this.next = this.shapes.get(b);
    this.offset = offset;
    this.pointsDirty = true;
  }
  
  protected void createPoints() {
    if (this.current == this.next) {
      System.arraycopy(this.current.points, 0, this.points, 0, this.points.length);
      return;
    } 
    float[] apoints = this.current.points;
    float[] bpoints = this.next.points;
    for (int i = 0; i < this.points.length; i++) {
      this.points[i] = apoints[i] * (1.0F - this.offset);
      this.points[i] = this.points[i] + bpoints[i] * this.offset;
    } 
  }
  
  public Shape transform(Transform transform) {
    createPoints();
    Polygon poly = new Polygon(this.points);
    return poly;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\newdawn\slick\geom\MorphShape.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
package org.newdawn.slick.geom;

public class Rectangle extends Shape {
  protected float width;
  
  protected float height;
  
  public Rectangle(float x, float y, float width, float height) {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    this.maxX = x + width;
    this.maxY = y + height;
    checkPoints();
  }
  
  public boolean contains(float xp, float yp) {
    if (xp <= getX())
      return false; 
    if (yp <= getY())
      return false; 
    if (xp >= this.maxX)
      return false; 
    if (yp >= this.maxY)
      return false; 
    return true;
  }
  
  public void setBounds(Rectangle other) {
    setBounds(other.getX(), other.getY(), other.getWidth(), other.getHeight());
  }
  
  public void setBounds(float x, float y, float width, float height) {
    setX(x);
    setY(y);
    setSize(width, height);
  }
  
  public void setSize(float width, float height) {
    setWidth(width);
    setHeight(height);
  }
  
  public float getWidth() {
    return this.width;
  }
  
  public float getHeight() {
    return this.height;
  }
  
  public void grow(float h, float v) {
    setX(getX() - h);
    setY(getY() - v);
    setWidth(getWidth() + h * 2.0F);
    setHeight(getHeight() + v * 2.0F);
  }
  
  public void scaleGrow(float h, float v) {
    grow(getWidth() * (h - 1.0F), getHeight() * (v - 1.0F));
  }
  
  public void setWidth(float width) {
    if (width != this.width) {
      this.pointsDirty = true;
      this.width = width;
      this.maxX = this.x + width;
    } 
  }
  
  public void setHeight(float height) {
    if (height != this.height) {
      this.pointsDirty = true;
      this.height = height;
      this.maxY = this.y + height;
    } 
  }
  
  public boolean intersects(Shape shape) {
    if (shape instanceof Rectangle) {
      Rectangle other = (Rectangle)shape;
      if (this.x > other.x + other.width || this.x + this.width < other.x)
        return false; 
      if (this.y > other.y + other.height || this.y + this.height < other.y)
        return false; 
      return true;
    } 
    if (shape instanceof Circle)
      return intersects((Circle)shape); 
    return super.intersects(shape);
  }
  
  protected void createPoints() {
    float useWidth = this.width;
    float useHeight = this.height;
    this.points = new float[8];
    this.points[0] = this.x;
    this.points[1] = this.y;
    this.points[2] = this.x + useWidth;
    this.points[3] = this.y;
    this.points[4] = this.x + useWidth;
    this.points[5] = this.y + useHeight;
    this.points[6] = this.x;
    this.points[7] = this.y + useHeight;
    this.maxX = this.points[2];
    this.maxY = this.points[5];
    this.minX = this.points[0];
    this.minY = this.points[1];
    findCenter();
    calculateRadius();
  }
  
  private boolean intersects(Circle other) {
    return other.intersects(this);
  }
  
  public String toString() {
    return "[Rectangle " + this.width + "x" + this.height + "]";
  }
  
  public static boolean contains(float xp, float yp, float xr, float yr, float widthr, float heightr) {
    return (xp >= xr && yp >= yr && xp <= xr + widthr && 
      yp <= yr + heightr);
  }
  
  public Shape transform(Transform transform) {
    checkPoints();
    Polygon resultPolygon = new Polygon();
    float[] result = new float[this.points.length];
    transform.transform(this.points, 0, result, 0, this.points.length / 2);
    resultPolygon.points = result;
    resultPolygon.findCenter();
    resultPolygon.checkPoints();
    return resultPolygon;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\newdawn\slick\geom\Rectangle.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
package org.newdawn.slick.geom;

import java.util.ArrayList;

public class BasicTriangulator implements Triangulator {
  private static final float EPSILON = 1.0E-10F;
  
  private PointList poly = new PointList();
  
  private PointList tris = new PointList();
  
  private boolean tried;
  
  public void addPolyPoint(float x, float y) {
    Point p = new Point(x, y);
    if (!this.poly.contains(p))
      this.poly.add(p); 
  }
  
  public int getPolyPointCount() {
    return this.poly.size();
  }
  
  public float[] getPolyPoint(int index) {
    return new float[] { Point.access$0(this.poly.get(index)), Point.access$1(this.poly.get(index)) };
  }
  
  public boolean triangulate() {
    this.tried = true;
    boolean worked = process(this.poly, this.tris);
    return worked;
  }
  
  public int getTriangleCount() {
    if (!this.tried)
      throw new RuntimeException("Call triangulate() before accessing triangles"); 
    return this.tris.size() / 3;
  }
  
  public float[] getTrianglePoint(int tri, int i) {
    if (!this.tried)
      throw new RuntimeException("Call triangulate() before accessing triangles"); 
    return this.tris.get(tri * 3 + i).toArray();
  }
  
  private float area(PointList contour) {
    int n = contour.size();
    float A = 0.0F;
    for (int p = n - 1, q = 0; q < n; p = q++) {
      Point contourP = contour.get(p);
      Point contourQ = contour.get(q);
      A += contourP.getX() * contourQ.getY() - contourQ.getX() * 
        contourP.getY();
    } 
    return A * 0.5F;
  }
  
  private boolean insideTriangle(float Ax, float Ay, float Bx, float By, float Cx, float Cy, float Px, float Py) {
    float ax = Cx - Bx;
    float ay = Cy - By;
    float bx = Ax - Cx;
    float by = Ay - Cy;
    float cx = Bx - Ax;
    float cy = By - Ay;
    float apx = Px - Ax;
    float apy = Py - Ay;
    float bpx = Px - Bx;
    float bpy = Py - By;
    float cpx = Px - Cx;
    float cpy = Py - Cy;
    float aCROSSbp = ax * bpy - ay * bpx;
    float cCROSSap = cx * apy - cy * apx;
    float bCROSScp = bx * cpy - by * cpx;
    return (aCROSSbp >= 0.0F && bCROSScp >= 0.0F && cCROSSap >= 0.0F);
  }
  
  private boolean snip(PointList contour, int u, int v, int w, int n, int[] V) {
    float Ax = contour.get(V[u]).getX();
    float Ay = contour.get(V[u]).getY();
    float Bx = contour.get(V[v]).getX();
    float By = contour.get(V[v]).getY();
    float Cx = contour.get(V[w]).getX();
    float Cy = contour.get(V[w]).getY();
    if (1.0E-10F > (Bx - Ax) * (Cy - Ay) - (By - Ay) * (Cx - Ax))
      return false; 
    for (int p = 0; p < n; p++) {
      if (p != u && p != v && p != w) {
        float Px = contour.get(V[p]).getX();
        float Py = contour.get(V[p]).getY();
        if (insideTriangle(Ax, Ay, Bx, By, Cx, Cy, Px, Py))
          return false; 
      } 
    } 
    return true;
  }
  
  private boolean process(PointList contour, PointList result) {
    result.clear();
    int n = contour.size();
    if (n < 3)
      return false; 
    int[] V = new int[n];
    if (0.0F < area(contour)) {
      for (int i = 0; i < n; i++)
        V[i] = i; 
    } else {
      for (int i = 0; i < n; i++)
        V[i] = n - 1 - i; 
    } 
    int nv = n;
    int count = 2 * nv;
    for (int m = 0, v = nv - 1; nv > 2; ) {
      if (count-- <= 0)
        return false; 
      int u = v;
      if (nv <= u)
        u = 0; 
      v = u + 1;
      if (nv <= v)
        v = 0; 
      int w = v + 1;
      if (nv <= w)
        w = 0; 
      if (snip(contour, u, v, w, nv, V)) {
        int a = V[u];
        int b = V[v];
        int c = V[w];
        result.add(contour.get(a));
        result.add(contour.get(b));
        result.add(contour.get(c));
        m++;
        for (int s = v, t = v + 1; t < nv; s++, t++)
          V[s] = V[t]; 
        nv--;
        count = 2 * nv;
      } 
    } 
    return true;
  }
  
  private class Point {
    private float x;
    
    private float y;
    
    private float[] array;
    
    public Point(float x, float y) {
      this.x = x;
      this.y = y;
      this.array = new float[] { x, y };
    }
    
    public float getX() {
      return this.x;
    }
    
    public float getY() {
      return this.y;
    }
    
    public float[] toArray() {
      return this.array;
    }
    
    public int hashCode() {
      return (int)(this.x * this.y * 31.0F);
    }
    
    public boolean equals(Object other) {
      if (other instanceof Point) {
        Point p = (Point)other;
        return (p.x == this.x && p.y == this.y);
      } 
      return false;
    }
  }
  
  private class PointList {
    private ArrayList points = new ArrayList();
    
    public boolean contains(BasicTriangulator.Point p) {
      return this.points.contains(p);
    }
    
    public void add(BasicTriangulator.Point point) {
      this.points.add(point);
    }
    
    public void remove(BasicTriangulator.Point point) {
      this.points.remove(point);
    }
    
    public int size() {
      return this.points.size();
    }
    
    public BasicTriangulator.Point get(int i) {
      return this.points.get(i);
    }
    
    public void clear() {
      this.points.clear();
    }
  }
  
  public void startHole() {}
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\newdawn\slick\geom\BasicTriangulator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
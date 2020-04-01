package org.newdawn.slick.geom;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MannTriangulator implements Triangulator {
  private static final double EPSILON = 1.0E-5D;
  
  protected PointBag contour;
  
  protected PointBag holes;
  
  private PointBag nextFreePointBag;
  
  private Point nextFreePoint;
  
  private List triangles = new ArrayList();
  
  public MannTriangulator() {
    this.contour = getPointBag();
  }
  
  public void addPolyPoint(float x, float y) {
    addPoint(new Vector2f(x, y));
  }
  
  public void reset() {
    while (this.holes != null)
      this.holes = freePointBag(this.holes); 
    this.contour.clear();
    this.holes = null;
  }
  
  public void startHole() {
    PointBag newHole = getPointBag();
    newHole.next = this.holes;
    this.holes = newHole;
  }
  
  private void addPoint(Vector2f pt) {
    if (this.holes == null) {
      Point p = getPoint(pt);
      this.contour.add(p);
    } else {
      Point p = getPoint(pt);
      this.holes.add(p);
    } 
  }
  
  private Vector2f[] triangulate(Vector2f[] result) {
    this.contour.computeAngles();
    for (PointBag hole = this.holes; hole != null; hole = hole.next)
      hole.computeAngles(); 
    while (this.holes != null) {
      Point pHole = this.holes.first;
      label57: do {
        if (pHole.angle <= 0.0D) {
          Point pContour = this.contour.first;
          label54: while (true) {
            if (pHole.isInfront(pContour) && 
              pContour.isInfront(pHole) && 
              !this.contour.doesIntersectSegment(pHole.pt, 
                pContour.pt)) {
              PointBag pointBag = this.holes;
              while (!pointBag.doesIntersectSegment(pHole.pt, 
                  pContour.pt)) {
                if ((pointBag = pointBag.next) == null) {
                  Point newPtContour = getPoint(pContour.pt);
                  pContour.insertAfter(newPtContour);
                  Point newPtHole = getPoint(pHole.pt);
                  pHole.insertBefore(newPtHole);
                  pContour.next = pHole;
                  pHole.prev = pContour;
                  newPtHole.next = newPtContour;
                  newPtContour.prev = newPtHole;
                  pContour.computeAngle();
                  pHole.computeAngle();
                  newPtContour.computeAngle();
                  newPtHole.computeAngle();
                  this.holes.first = null;
                  break label57;
                } 
              } 
            } 
            if ((pContour = pContour.next) == this.contour.first)
              break label54; 
          } 
          break;
        } 
      } while ((pHole = pHole.next) != this.holes.first);
      this.holes = freePointBag(this.holes);
    } 
    int numTriangles = this.contour.countPoints() - 2;
    int neededSpace = numTriangles * 3 + 1;
    if (result.length < neededSpace)
      result = (Vector2f[])Array.newInstance(result.getClass()
          .getComponentType(), neededSpace); 
    int idx = 0;
    while (true) {
      Point pContour = this.contour.first;
      if (pContour == null)
        break; 
      if (pContour.next == pContour.prev)
        break; 
      do {
        if (pContour.angle <= 0.0D)
          continue; 
        Point point1 = pContour.prev;
        Point point2 = pContour.next;
        if ((point2.next == point1 || (point1.isInfront(point2) && point2.isInfront(point1))) && 
          !this.contour.doesIntersectSegment(point1.pt, point2.pt)) {
          result[idx++] = pContour.pt;
          result[idx++] = point2.pt;
          result[idx++] = point1.pt;
          break;
        } 
      } while ((pContour = pContour.next) != this.contour.first);
      Point prev = pContour.prev;
      Point next = pContour.next;
      this.contour.first = prev;
      pContour.unlink();
      freePoint(pContour);
      next.computeAngle();
      prev.computeAngle();
    } 
    result[idx] = null;
    this.contour.clear();
    return result;
  }
  
  private PointBag getPointBag() {
    PointBag pb = this.nextFreePointBag;
    if (pb != null) {
      this.nextFreePointBag = pb.next;
      pb.next = null;
      return pb;
    } 
    return new PointBag();
  }
  
  private PointBag freePointBag(PointBag pb) {
    PointBag next = pb.next;
    pb.clear();
    pb.next = this.nextFreePointBag;
    this.nextFreePointBag = pb;
    return next;
  }
  
  private Point getPoint(Vector2f pt) {
    Point p = this.nextFreePoint;
    if (p != null) {
      this.nextFreePoint = p.next;
      p.next = null;
      p.prev = null;
      p.pt = pt;
      return p;
    } 
    return new Point(pt);
  }
  
  private void freePoint(Point p) {
    p.next = this.nextFreePoint;
    this.nextFreePoint = p;
  }
  
  private void freePoints(Point head) {
    head.prev.next = this.nextFreePoint;
    head.prev = null;
    this.nextFreePoint = head;
  }
  
  private static class Point implements Serializable {
    protected Vector2f pt;
    
    protected Point prev;
    
    protected Point next;
    
    protected double nx;
    
    protected double ny;
    
    protected double angle;
    
    protected double dist;
    
    public Point(Vector2f pt) {
      this.pt = pt;
    }
    
    public void unlink() {
      this.prev.next = this.next;
      this.next.prev = this.prev;
      this.next = null;
      this.prev = null;
    }
    
    public void insertBefore(Point p) {
      this.prev.next = p;
      p.prev = this.prev;
      p.next = this;
      this.prev = p;
    }
    
    public void insertAfter(Point p) {
      this.next.prev = p;
      p.prev = this;
      p.next = this.next;
      this.next = p;
    }
    
    private double hypot(double x, double y) {
      return Math.sqrt(x * x + y * y);
    }
    
    public void computeAngle() {
      if (this.prev.pt.equals(this.pt))
        this.pt.x += 0.01F; 
      double dx1 = (this.pt.x - this.prev.pt.x);
      double dy1 = (this.pt.y - this.prev.pt.y);
      double len1 = hypot(dx1, dy1);
      dx1 /= len1;
      dy1 /= len1;
      if (this.next.pt.equals(this.pt))
        this.pt.y += 0.01F; 
      double dx2 = (this.next.pt.x - this.pt.x);
      double dy2 = (this.next.pt.y - this.pt.y);
      double len2 = hypot(dx2, dy2);
      dx2 /= len2;
      dy2 /= len2;
      double nx1 = -dy1;
      double ny1 = dx1;
      this.nx = (nx1 - dy2) * 0.5D;
      this.ny = (ny1 + dx2) * 0.5D;
      if (this.nx * this.nx + this.ny * this.ny < 1.0E-5D) {
        this.nx = dx1;
        this.ny = dy2;
        this.angle = 1.0D;
        if (dx1 * dx2 + dy1 * dy2 > 0.0D) {
          this.nx = -dx1;
          this.ny = -dy1;
        } 
      } else {
        this.angle = this.nx * dx2 + this.ny * dy2;
      } 
    }
    
    public double getAngle(Point p) {
      double dx = (p.pt.x - this.pt.x);
      double dy = (p.pt.y - this.pt.y);
      double dlen = hypot(dx, dy);
      return (this.nx * dx + this.ny * dy) / dlen;
    }
    
    public boolean isConcave() {
      return (this.angle < 0.0D);
    }
    
    public boolean isInfront(double dx, double dy) {
      boolean sidePrev = ((this.prev.pt.y - this.pt.y) * dx + (this.pt.x - this.prev.pt.x) * 
        dy >= 0.0D);
      boolean sideNext = ((this.pt.y - this.next.pt.y) * dx + (this.next.pt.x - this.pt.x) * 
        dy >= 0.0D);
      return (this.angle < 0.0D) ? (sidePrev | sideNext) : (sidePrev & sideNext);
    }
    
    public boolean isInfront(Point p) {
      return isInfront((p.pt.x - this.pt.x), (p.pt.y - this.pt.y));
    }
  }
  
  protected class PointBag implements Serializable {
    protected MannTriangulator.Point first;
    
    protected PointBag next;
    
    public void clear() {
      if (this.first != null) {
        MannTriangulator.this.freePoints(this.first);
        this.first = null;
      } 
    }
    
    public void add(MannTriangulator.Point p) {
      if (this.first != null) {
        this.first.insertBefore(p);
      } else {
        this.first = p;
        p.next = p;
        p.prev = p;
      } 
    }
    
    public void computeAngles() {
      if (this.first == null)
        return; 
      MannTriangulator.Point p = this.first;
      do {
        p.computeAngle();
      } while ((p = p.next) != this.first);
    }
    
    public boolean doesIntersectSegment(Vector2f v1, Vector2f v2) {
      double dxA = (v2.x - v1.x);
      double dyA = (v2.y - v1.y);
      MannTriangulator.Point p = this.first;
      while (true) {
        MannTriangulator.Point n = p.next;
        if (p.pt != v1 && n.pt != v1 && p.pt != v2 && n.pt != v2) {
          double dxB = (n.pt.x - p.pt.x);
          double dyB = (n.pt.y - p.pt.y);
          double d = dxA * dyB - dyA * dxB;
          if (Math.abs(d) > 1.0E-5D) {
            double tmp1 = (p.pt.x - v1.x);
            double tmp2 = (p.pt.y - v1.y);
            double tA = (dyB * tmp1 - dxB * tmp2) / d;
            double tB = (dyA * tmp1 - dxA * tmp2) / d;
            if (tA >= 0.0D && tA <= 1.0D && tB >= 0.0D && tB <= 1.0D)
              return true; 
          } 
        } 
        if (n == this.first)
          return false; 
        p = n;
      } 
    }
    
    public int countPoints() {
      if (this.first == null)
        return 0; 
      int count = 0;
      MannTriangulator.Point p = this.first;
      do {
        count++;
      } while ((p = p.next) != this.first);
      return count;
    }
    
    public boolean contains(Vector2f point) {
      if (this.first == null)
        return false; 
      if (this.first.prev.pt.equals(point))
        return true; 
      if (this.first.pt.equals(point))
        return true; 
      return false;
    }
  }
  
  public boolean triangulate() {
    Vector2f[] temp = triangulate(new Vector2f[0]);
    for (int i = 0; i < temp.length && 
      temp[i] != null; i++)
      this.triangles.add(temp[i]); 
    return true;
  }
  
  public int getTriangleCount() {
    return this.triangles.size() / 3;
  }
  
  public float[] getTrianglePoint(int tri, int i) {
    Vector2f pt = this.triangles.get(tri * 3 + i);
    return new float[] { pt.x, pt.y };
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\newdawn\slick\geom\MannTriangulator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
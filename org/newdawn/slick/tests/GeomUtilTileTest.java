package org.newdawn.slick.tests;

import java.util.ArrayList;
import java.util.HashSet;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.Game;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.GeomUtil;
import org.newdawn.slick.geom.GeomUtilListener;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

public class GeomUtilTileTest extends BasicGame implements GeomUtilListener {
  private Shape source;
  
  private Shape cut;
  
  private Shape[] result;
  
  private GeomUtil util = new GeomUtil();
  
  private ArrayList original = new ArrayList();
  
  private ArrayList combined = new ArrayList();
  
  private ArrayList intersections = new ArrayList();
  
  private ArrayList used = new ArrayList();
  
  private ArrayList[][] quadSpace;
  
  private Shape[][] quadSpaceShapes;
  
  public GeomUtilTileTest() {
    super("GeomUtilTileTest");
  }
  
  private void generateSpace(ArrayList<Shape> shapes, float minx, float miny, float maxx, float maxy, int segments) {
    this.quadSpace = new ArrayList[segments][segments];
    this.quadSpaceShapes = new Shape[segments][segments];
    float dx = (maxx - minx) / segments;
    float dy = (maxy - miny) / segments;
    for (int x = 0; x < segments; x++) {
      for (int y = 0; y < segments; y++) {
        this.quadSpace[x][y] = new ArrayList();
        Polygon segmentPolygon = new Polygon();
        segmentPolygon.addPoint(minx + dx * x, miny + dy * y);
        segmentPolygon.addPoint(minx + dx * x + dx, miny + dy * y);
        segmentPolygon.addPoint(minx + dx * x + dx, miny + dy * y + dy);
        segmentPolygon.addPoint(minx + dx * x, miny + dy * y + dy);
        for (int i = 0; i < shapes.size(); i++) {
          Shape shape = shapes.get(i);
          if (collides(shape, (Shape)segmentPolygon))
            this.quadSpace[x][y].add(shape); 
        } 
        this.quadSpaceShapes[x][y] = (Shape)segmentPolygon;
      } 
    } 
  }
  
  private void removeFromQuadSpace(Shape shape) {
    int segments = this.quadSpace.length;
    for (int x = 0; x < segments; x++) {
      for (int y = 0; y < segments; y++)
        this.quadSpace[x][y].remove(shape); 
    } 
  }
  
  private void addToQuadSpace(Shape shape) {
    int segments = this.quadSpace.length;
    for (int x = 0; x < segments; x++) {
      for (int y = 0; y < segments; y++) {
        if (collides(shape, this.quadSpaceShapes[x][y]))
          this.quadSpace[x][y].add(shape); 
      } 
    } 
  }
  
  public void init() {
    int size = 10;
    int[][] map = { { 0, 0, 0, 0, 0, 0, 0, 3 }, { 0, 1, 1, 1, 1, 1, 1 }, { 0, 1, 1, 5, 1, 6 }, { 0, 1, 2, 4, 1, 1 }, { 0, 1, 1, 1, 1 }, { 0, 0, 0, 0, 3, 1, 1 }, { 0, 0, 0, 1, 1, 1 }, { 0, 0, 0, 1, 1 }, new int[10], 
        new int[10] };
    for (int x = 0; x < (map[0]).length; x++) {
      for (int y = 0; y < map.length; y++) {
        if (map[y][x] != 0) {
          Polygon p2;
          Polygon poly;
          Circle ellipse;
          Polygon p;
          Polygon p3;
          Polygon p4;
          switch (map[y][x]) {
            case 1:
              p2 = new Polygon();
              p2.addPoint((x * 32), (y * 32));
              p2.addPoint((x * 32 + 32), (y * 32));
              p2.addPoint((x * 32 + 32), (y * 32 + 32));
              p2.addPoint((x * 32), (y * 32 + 32));
              this.original.add(p2);
              break;
            case 2:
              poly = new Polygon();
              poly.addPoint((x * 32), (y * 32));
              poly.addPoint((x * 32 + 32), (y * 32));
              poly.addPoint((x * 32), (y * 32 + 32));
              this.original.add(poly);
              break;
            case 3:
              ellipse = new Circle((x * 32 + 16), (y * 32 + 32), 16.0F, 16);
              this.original.add(ellipse);
              break;
            case 4:
              p = new Polygon();
              p.addPoint((x * 32 + 32), (y * 32));
              p.addPoint((x * 32 + 32), (y * 32 + 32));
              p.addPoint((x * 32), (y * 32 + 32));
              this.original.add(p);
              break;
            case 5:
              p3 = new Polygon();
              p3.addPoint((x * 32), (y * 32));
              p3.addPoint((x * 32 + 32), (y * 32));
              p3.addPoint((x * 32 + 32), (y * 32 + 32));
              this.original.add(p3);
              break;
            case 6:
              p4 = new Polygon();
              p4.addPoint((x * 32), (y * 32));
              p4.addPoint((x * 32 + 32), (y * 32));
              p4.addPoint((x * 32), (y * 32 + 32));
              this.original.add(p4);
              break;
          } 
        } 
      } 
    } 
    long before = System.currentTimeMillis();
    generateSpace(this.original, 0.0F, 0.0F, ((size + 1) * 32), ((size + 1) * 32), 8);
    this.combined = combineQuadSpace();
    long after = System.currentTimeMillis();
    System.out.println("Combine took: " + (after - before));
    System.out.println("Combine result: " + this.combined.size());
  }
  
  private ArrayList combineQuadSpace() {
    boolean updated = true;
    while (updated) {
      updated = false;
      for (int i = 0; i < this.quadSpace.length; i++) {
        for (int y = 0; y < this.quadSpace.length; y++) {
          ArrayList shapes = this.quadSpace[i][y];
          int before = shapes.size();
          combine(shapes);
          int after = shapes.size();
          int j = updated | ((before != after) ? 1 : 0);
        } 
      } 
    } 
    HashSet<?> result = new HashSet();
    for (int x = 0; x < this.quadSpace.length; x++) {
      for (int y = 0; y < this.quadSpace.length; y++)
        result.addAll(this.quadSpace[x][y]); 
    } 
    return new ArrayList(result);
  }
  
  private ArrayList combine(ArrayList<Shape> shapes) {
    ArrayList<Shape> last = shapes;
    ArrayList<Shape> current = shapes;
    boolean first = true;
    while (current.size() != last.size() || first) {
      first = false;
      last = current;
      current = combineImpl(current);
    } 
    ArrayList<Shape> pruned = new ArrayList();
    for (int i = 0; i < current.size(); i++)
      pruned.add(((Shape)current.get(i)).prune()); 
    return pruned;
  }
  
  private ArrayList combineImpl(ArrayList<?> shapes) {
    ArrayList<?> result = new ArrayList(shapes);
    if (this.quadSpace != null)
      result = shapes; 
    for (int i = 0; i < shapes.size(); i++) {
      Shape first = (Shape)shapes.get(i);
      for (int j = i + 1; j < shapes.size(); j++) {
        Shape second = (Shape)shapes.get(j);
        if (first.intersects(second)) {
          Shape[] joined = this.util.union(first, second);
          if (joined.length == 1) {
            if (this.quadSpace != null) {
              removeFromQuadSpace(first);
              removeFromQuadSpace(second);
              addToQuadSpace(joined[0]);
            } else {
              result.remove(first);
              result.remove(second);
              result.add(joined[0]);
            } 
            return result;
          } 
        } 
      } 
    } 
    return result;
  }
  
  public boolean collides(Shape shape1, Shape shape2) {
    if (shape1.intersects(shape2))
      return true; 
    int i;
    for (i = 0; i < shape1.getPointCount(); i++) {
      float[] pt = shape1.getPoint(i);
      if (shape2.contains(pt[0], pt[1]))
        return true; 
    } 
    for (i = 0; i < shape2.getPointCount(); i++) {
      float[] pt = shape2.getPoint(i);
      if (shape1.contains(pt[0], pt[1]))
        return true; 
    } 
    return false;
  }
  
  public void init(GameContainer container) throws SlickException {
    this.util.setListener(this);
    init();
  }
  
  public void update(GameContainer container, int delta) throws SlickException {}
  
  public void render(GameContainer container, Graphics g) throws SlickException {
    g.setColor(Color.green);
    int i;
    for (i = 0; i < this.original.size(); i++) {
      Shape shape = this.original.get(i);
      g.draw(shape);
    } 
    g.setColor(Color.white);
    if (this.quadSpaceShapes != null)
      g.draw(this.quadSpaceShapes[0][0]); 
    g.translate(0.0F, 320.0F);
    for (i = 0; i < this.combined.size(); i++) {
      g.setColor(Color.white);
      Shape shape = this.combined.get(i);
      g.draw(shape);
      for (int j = 0; j < shape.getPointCount(); j++) {
        g.setColor(Color.yellow);
        float[] pt = shape.getPoint(j);
        g.fillOval(pt[0] - 1.0F, pt[1] - 1.0F, 3.0F, 3.0F);
      } 
    } 
  }
  
  public static void main(String[] argv) {
    try {
      AppGameContainer container = new AppGameContainer(
          (Game)new GeomUtilTileTest());
      container.setDisplayMode(800, 600, false);
      container.start();
    } catch (SlickException e) {
      e.printStackTrace();
    } 
  }
  
  public void pointExcluded(float x, float y) {}
  
  public void pointIntersected(float x, float y) {
    this.intersections.add(new Vector2f(x, y));
  }
  
  public void pointUsed(float x, float y) {
    this.used.add(new Vector2f(x, y));
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\newdawn\slick\tests\GeomUtilTileTest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
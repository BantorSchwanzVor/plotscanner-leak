package org.newdawn.slick.tests;

import java.util.ArrayList;
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
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Transform;
import org.newdawn.slick.geom.Vector2f;

public class GeomUtilTest extends BasicGame implements GeomUtilListener {
  private Shape source;
  
  private Shape cut;
  
  private Shape[] result;
  
  private ArrayList points = new ArrayList();
  
  private ArrayList marks = new ArrayList();
  
  private ArrayList exclude = new ArrayList();
  
  private boolean dynamic;
  
  private GeomUtil util = new GeomUtil();
  
  private int xp;
  
  private int yp;
  
  private Circle circle;
  
  private Shape rect;
  
  private Polygon star;
  
  private boolean union;
  
  public GeomUtilTest() {
    super("GeomUtilTest");
  }
  
  public void init() {
    Polygon source = new Polygon();
    source.addPoint(100.0F, 100.0F);
    source.addPoint(150.0F, 80.0F);
    source.addPoint(210.0F, 120.0F);
    source.addPoint(340.0F, 150.0F);
    source.addPoint(150.0F, 200.0F);
    source.addPoint(120.0F, 250.0F);
    this.source = (Shape)source;
    this.circle = new Circle(0.0F, 0.0F, 50.0F);
    this.rect = (Shape)new Rectangle(-100.0F, -40.0F, 200.0F, 80.0F);
    this.star = new Polygon();
    float dis = 40.0F;
    for (int i = 0; i < 360; i += 30) {
      dis = ((dis == 40.0F) ? 60 : 40);
      double x = Math.cos(Math.toRadians(i)) * dis;
      double y = Math.sin(Math.toRadians(i)) * dis;
      this.star.addPoint((float)x, (float)y);
    } 
    this.cut = (Shape)this.circle;
    this.cut.setLocation(203.0F, 78.0F);
    this.xp = (int)this.cut.getCenterX();
    this.yp = (int)this.cut.getCenterY();
    makeBoolean();
  }
  
  public void init(GameContainer container) throws SlickException {
    this.util.setListener(this);
    init();
    container.setVSync(true);
  }
  
  public void update(GameContainer container, int delta) throws SlickException {
    if (container.getInput().isKeyPressed(57))
      this.dynamic = !this.dynamic; 
    if (container.getInput().isKeyPressed(28)) {
      this.union = !this.union;
      makeBoolean();
    } 
    if (container.getInput().isKeyPressed(2)) {
      this.cut = (Shape)this.circle;
      this.circle.setCenterX(this.xp);
      this.circle.setCenterY(this.yp);
      makeBoolean();
    } 
    if (container.getInput().isKeyPressed(3)) {
      this.cut = this.rect;
      this.rect.setCenterX(this.xp);
      this.rect.setCenterY(this.yp);
      makeBoolean();
    } 
    if (container.getInput().isKeyPressed(4)) {
      this.cut = (Shape)this.star;
      this.star.setCenterX(this.xp);
      this.star.setCenterY(this.yp);
      makeBoolean();
    } 
    if (this.dynamic) {
      this.xp = container.getInput().getMouseX();
      this.yp = container.getInput().getMouseY();
      makeBoolean();
    } 
  }
  
  private void makeBoolean() {
    this.marks.clear();
    this.points.clear();
    this.exclude.clear();
    this.cut.setCenterX(this.xp);
    this.cut.setCenterY(this.yp);
    if (this.union) {
      this.result = this.util.union(this.source, this.cut);
    } else {
      this.result = this.util.subtract(this.source, this.cut);
    } 
  }
  
  public void render(GameContainer container, Graphics g) throws SlickException {
    g.drawString("Space - toggle movement of cutting shape", 530.0F, 10.0F);
    g.drawString("1,2,3 - select cutting shape", 530.0F, 30.0F);
    g.drawString("Mouse wheel - rotate shape", 530.0F, 50.0F);
    g.drawString("Enter - toggle union/subtract", 530.0F, 70.0F);
    g.drawString("MODE: " + (this.union ? "Union" : "Cut"), 530.0F, 200.0F);
    g.setColor(Color.green);
    g.draw(this.source);
    g.setColor(Color.red);
    g.draw(this.cut);
    g.setColor(Color.white);
    int i;
    for (i = 0; i < this.exclude.size(); i++) {
      Vector2f pt = this.exclude.get(i);
      g.drawOval(pt.x - 3.0F, pt.y - 3.0F, 7.0F, 7.0F);
    } 
    g.setColor(Color.yellow);
    for (i = 0; i < this.points.size(); i++) {
      Vector2f pt = this.points.get(i);
      g.fillOval(pt.x - 1.0F, pt.y - 1.0F, 3.0F, 3.0F);
    } 
    g.setColor(Color.white);
    for (i = 0; i < this.marks.size(); i++) {
      Vector2f pt = this.marks.get(i);
      g.fillOval(pt.x - 1.0F, pt.y - 1.0F, 3.0F, 3.0F);
    } 
    g.translate(0.0F, 300.0F);
    g.setColor(Color.white);
    if (this.result != null) {
      for (i = 0; i < this.result.length; i++)
        g.draw(this.result[i]); 
      g.drawString("Polys:" + this.result.length, 10.0F, 100.0F);
      g.drawString("X:" + this.xp, 10.0F, 120.0F);
      g.drawString("Y:" + this.yp, 10.0F, 130.0F);
    } 
  }
  
  public static void main(String[] argv) {
    try {
      AppGameContainer container = new AppGameContainer((Game)new GeomUtilTest());
      container.setDisplayMode(800, 600, false);
      container.start();
    } catch (SlickException e) {
      e.printStackTrace();
    } 
  }
  
  public void pointExcluded(float x, float y) {
    this.exclude.add(new Vector2f(x, y));
  }
  
  public void pointIntersected(float x, float y) {
    this.marks.add(new Vector2f(x, y));
  }
  
  public void pointUsed(float x, float y) {
    this.points.add(new Vector2f(x, y));
  }
  
  public void mouseWheelMoved(int change) {
    if (this.dynamic)
      if (change < 0) {
        this.cut = this.cut.transform(Transform.createRotateTransform((float)Math.toRadians(10.0D), this.cut.getCenterX(), this.cut.getCenterY()));
      } else {
        this.cut = this.cut.transform(Transform.createRotateTransform((float)Math.toRadians(-10.0D), this.cut.getCenterX(), this.cut.getCenterY()));
      }  
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\newdawn\slick\tests\GeomUtilTest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
package org.newdawn.slick.tests;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.Game;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.ShapeFill;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.fills.GradientFill;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

public class GradientImageTest extends BasicGame {
  private Image image1;
  
  private Image image2;
  
  private GradientFill fill;
  
  private Shape shape;
  
  private Polygon poly;
  
  private GameContainer container;
  
  private float ang;
  
  private boolean rotating = false;
  
  public GradientImageTest() {
    super("Gradient Image Test");
  }
  
  public void init(GameContainer container) throws SlickException {
    this.container = container;
    this.image1 = new Image("testdata/grass.png");
    this.image2 = new Image("testdata/rocks.png");
    this.fill = new GradientFill(-64.0F, 0.0F, new Color(1.0F, 1.0F, 1.0F, 1.0F), 64.0F, 0.0F, new Color(0, 0, 0, 0));
    this.shape = (Shape)new Rectangle(336.0F, 236.0F, 128.0F, 128.0F);
    this.poly = new Polygon();
    this.poly.addPoint(320.0F, 220.0F);
    this.poly.addPoint(350.0F, 200.0F);
    this.poly.addPoint(450.0F, 200.0F);
    this.poly.addPoint(480.0F, 220.0F);
    this.poly.addPoint(420.0F, 400.0F);
    this.poly.addPoint(400.0F, 390.0F);
  }
  
  public void render(GameContainer container, Graphics g) {
    g.drawString("R - Toggle Rotationg", 10.0F, 50.0F);
    g.drawImage(this.image1, 100.0F, 236.0F);
    g.drawImage(this.image2, 600.0F, 236.0F);
    g.translate(0.0F, -150.0F);
    g.rotate(400.0F, 300.0F, this.ang);
    g.texture(this.shape, this.image2);
    g.texture(this.shape, this.image1, (ShapeFill)this.fill);
    g.resetTransform();
    g.translate(0.0F, 150.0F);
    g.rotate(400.0F, 300.0F, this.ang);
    g.texture((Shape)this.poly, this.image2);
    g.texture((Shape)this.poly, this.image1, (ShapeFill)this.fill);
    g.resetTransform();
  }
  
  public void update(GameContainer container, int delta) {
    if (this.rotating)
      this.ang += delta * 0.1F; 
  }
  
  public static void main(String[] argv) {
    try {
      AppGameContainer container = new AppGameContainer((Game)new GradientImageTest());
      container.setDisplayMode(800, 600, false);
      container.start();
    } catch (SlickException e) {
      e.printStackTrace();
    } 
  }
  
  public void keyPressed(int key, char c) {
    if (key == 19)
      this.rotating = !this.rotating; 
    if (key == 1)
      this.container.exit(); 
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\newdawn\slick\tests\GradientImageTest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
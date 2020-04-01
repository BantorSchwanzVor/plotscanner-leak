package org.newdawn.slick.tests;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.Game;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Shape;

public class LameTest extends BasicGame {
  private Polygon poly = new Polygon();
  
  private Image image;
  
  public LameTest() {
    super("Lame Test");
  }
  
  public void init(GameContainer container) throws SlickException {
    this.poly.addPoint(100.0F, 100.0F);
    this.poly.addPoint(120.0F, 100.0F);
    this.poly.addPoint(120.0F, 120.0F);
    this.poly.addPoint(100.0F, 120.0F);
    this.image = new Image("testdata/rocks.png");
  }
  
  public void update(GameContainer container, int delta) throws SlickException {}
  
  public void render(GameContainer container, Graphics g) throws SlickException {
    g.setColor(Color.white);
    g.texture((Shape)this.poly, this.image);
  }
  
  public static void main(String[] argv) {
    try {
      AppGameContainer container = new AppGameContainer((Game)new LameTest());
      container.setDisplayMode(800, 600, false);
      container.start();
    } catch (SlickException e) {
      e.printStackTrace();
    } 
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\newdawn\slick\tests\LameTest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
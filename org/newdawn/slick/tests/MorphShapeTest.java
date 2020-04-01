package org.newdawn.slick.tests;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.Game;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.MorphShape;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Transform;

public class MorphShapeTest extends BasicGame {
  private Shape a;
  
  private Shape b;
  
  private Shape c;
  
  private MorphShape morph;
  
  private float time;
  
  public MorphShapeTest() {
    super("MorphShapeTest");
  }
  
  public void init(GameContainer container) throws SlickException {
    this.a = (Shape)new Rectangle(100.0F, 100.0F, 50.0F, 200.0F);
    this.a = this.a.transform(Transform.createRotateTransform(0.1F, 100.0F, 100.0F));
    this.b = (Shape)new Rectangle(200.0F, 100.0F, 50.0F, 200.0F);
    this.b = this.b.transform(Transform.createRotateTransform(-0.6F, 100.0F, 100.0F));
    this.c = (Shape)new Rectangle(300.0F, 100.0F, 50.0F, 200.0F);
    this.c = this.c.transform(Transform.createRotateTransform(-0.2F, 100.0F, 100.0F));
    this.morph = new MorphShape(this.a);
    this.morph.addShape(this.b);
    this.morph.addShape(this.c);
    container.setVSync(true);
  }
  
  public void update(GameContainer container, int delta) throws SlickException {
    this.time += delta * 0.001F;
    this.morph.setMorphTime(this.time);
  }
  
  public void render(GameContainer container, Graphics g) throws SlickException {
    g.setColor(Color.green);
    g.draw(this.a);
    g.setColor(Color.red);
    g.draw(this.b);
    g.setColor(Color.blue);
    g.draw(this.c);
    g.setColor(Color.white);
    g.draw((Shape)this.morph);
  }
  
  public static void main(String[] argv) {
    try {
      AppGameContainer container = new AppGameContainer(
          (Game)new MorphShapeTest());
      container.setDisplayMode(800, 600, false);
      container.start();
    } catch (SlickException e) {
      e.printStackTrace();
    } 
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\newdawn\slick\tests\MorphShapeTest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
package org.newdawn.slick.tests;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.Game;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class ClipTest extends BasicGame {
  private float ang = 0.0F;
  
  private boolean world;
  
  private boolean clip;
  
  public ClipTest() {
    super("Clip Test");
  }
  
  public void init(GameContainer container) throws SlickException {}
  
  public void update(GameContainer container, int delta) throws SlickException {
    this.ang += delta * 0.01F;
  }
  
  public void render(GameContainer container, Graphics g) throws SlickException {
    g.setColor(Color.white);
    g.drawString("1 - No Clipping", 100.0F, 10.0F);
    g.drawString("2 - Screen Clipping", 100.0F, 30.0F);
    g.drawString("3 - World Clipping", 100.0F, 50.0F);
    if (this.world)
      g.drawString("WORLD CLIPPING ENABLED", 200.0F, 80.0F); 
    if (this.clip)
      g.drawString("SCREEN CLIPPING ENABLED", 200.0F, 80.0F); 
    g.rotate(400.0F, 400.0F, this.ang);
    if (this.world)
      g.setWorldClip(350.0F, 302.0F, 100.0F, 196.0F); 
    if (this.clip)
      g.setClip(350, 302, 100, 196); 
    g.setColor(Color.red);
    g.fillOval(300.0F, 300.0F, 200.0F, 200.0F);
    g.setColor(Color.blue);
    g.fillRect(390.0F, 200.0F, 20.0F, 400.0F);
    g.clearClip();
    g.clearWorldClip();
  }
  
  public void keyPressed(int key, char c) {
    if (key == 2) {
      this.world = false;
      this.clip = false;
    } 
    if (key == 3) {
      this.world = false;
      this.clip = true;
    } 
    if (key == 4) {
      this.world = true;
      this.clip = false;
    } 
  }
  
  public static void main(String[] argv) {
    try {
      AppGameContainer container = new AppGameContainer((Game)new ClipTest());
      container.setDisplayMode(800, 600, false);
      container.start();
    } catch (SlickException e) {
      e.printStackTrace();
    } 
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\newdawn\slick\tests\ClipTest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
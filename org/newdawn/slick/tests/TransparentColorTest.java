package org.newdawn.slick.tests;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.Game;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class TransparentColorTest extends BasicGame {
  private Image image;
  
  private Image timage;
  
  public TransparentColorTest() {
    super("Transparent Color Test");
  }
  
  public void init(GameContainer container) throws SlickException {
    this.image = new Image("testdata/transtest.png");
    this.timage = new Image("testdata/transtest.png", new Color(94, 66, 41, 255));
  }
  
  public void render(GameContainer container, Graphics g) {
    g.setBackground(Color.red);
    this.image.draw(10.0F, 10.0F);
    this.timage.draw(10.0F, 310.0F);
  }
  
  public void update(GameContainer container, int delta) {}
  
  public static void main(String[] argv) {
    try {
      AppGameContainer container = new AppGameContainer((Game)new TransparentColorTest());
      container.setDisplayMode(800, 600, false);
      container.start();
    } catch (SlickException e) {
      e.printStackTrace();
    } 
  }
  
  public void keyPressed(int key, char c) {}
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\newdawn\slick\tests\TransparentColorTest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
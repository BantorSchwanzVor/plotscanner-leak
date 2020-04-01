package org.newdawn.slick.tests;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.Game;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class CopyAreaAlphaTest extends BasicGame {
  private Image textureMap;
  
  private Image copy;
  
  public CopyAreaAlphaTest() {
    super("CopyArea Alpha Test");
  }
  
  public void init(GameContainer container) throws SlickException {
    this.textureMap = new Image("testdata/grass.png");
    container.getGraphics().setBackground(Color.black);
    this.copy = new Image(100, 100);
  }
  
  public void update(GameContainer container, int delta) throws SlickException {}
  
  public void render(GameContainer container, Graphics g) throws SlickException {
    g.clearAlphaMap();
    g.setDrawMode(Graphics.MODE_NORMAL);
    g.setColor(Color.white);
    g.fillOval(100.0F, 100.0F, 150.0F, 150.0F);
    this.textureMap.draw(10.0F, 50.0F);
    g.copyArea(this.copy, 100, 100);
    g.setColor(Color.red);
    g.fillRect(300.0F, 100.0F, 200.0F, 200.0F);
    this.copy.draw(350.0F, 150.0F);
  }
  
  public void keyPressed(int key, char c) {}
  
  public static void main(String[] argv) {
    try {
      AppGameContainer container = new AppGameContainer((Game)new CopyAreaAlphaTest());
      container.setDisplayMode(800, 600, false);
      container.start();
    } catch (SlickException e) {
      e.printStackTrace();
    } 
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\newdawn\slick\tests\CopyAreaAlphaTest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
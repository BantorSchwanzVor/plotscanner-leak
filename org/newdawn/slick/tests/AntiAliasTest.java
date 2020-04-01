package org.newdawn.slick.tests;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.Game;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class AntiAliasTest extends BasicGame {
  public AntiAliasTest() {
    super("AntiAlias Test");
  }
  
  public void init(GameContainer container) throws SlickException {
    container.getGraphics().setBackground(Color.green);
  }
  
  public void update(GameContainer container, int delta) throws SlickException {}
  
  public void render(GameContainer container, Graphics g) throws SlickException {
    g.setAntiAlias(true);
    g.setColor(Color.red);
    g.drawOval(100.0F, 100.0F, 100.0F, 100.0F);
    g.fillOval(300.0F, 100.0F, 100.0F, 100.0F);
    g.setAntiAlias(false);
    g.setColor(Color.red);
    g.drawOval(100.0F, 300.0F, 100.0F, 100.0F);
    g.fillOval(300.0F, 300.0F, 100.0F, 100.0F);
  }
  
  public static void main(String[] argv) {
    try {
      AppGameContainer container = new AppGameContainer((Game)new AntiAliasTest());
      container.setDisplayMode(800, 600, false);
      container.start();
    } catch (SlickException e) {
      e.printStackTrace();
    } 
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\newdawn\slick\tests\AntiAliasTest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
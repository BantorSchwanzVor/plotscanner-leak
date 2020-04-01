package org.newdawn.slick.tests;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Game;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class DoubleClickTest extends BasicGame {
  private String message;
  
  public DoubleClickTest() {
    super("Double Click Test");
    this.message = "Click or Double Click";
  }
  
  public void init(GameContainer container) throws SlickException {}
  
  public void update(GameContainer container, int delta) throws SlickException {}
  
  public void render(GameContainer container, Graphics g) throws SlickException {
    g.drawString(this.message, 100.0F, 100.0F);
  }
  
  public static void main(String[] argv) {
    try {
      AppGameContainer container = new AppGameContainer((Game)new DoubleClickTest());
      container.setDisplayMode(800, 600, false);
      container.start();
    } catch (SlickException e) {
      e.printStackTrace();
    } 
  }
  
  public void mouseClicked(int button, int x, int y, int clickCount) {
    if (clickCount == 1)
      this.message = "Single Click: " + button + " " + x + "," + y; 
    if (clickCount == 2)
      this.message = "Double Click: " + button + " " + x + "," + y; 
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\newdawn\slick\tests\DoubleClickTest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
package org.newdawn.slick.tests;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.Game;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class ImageReadTest extends BasicGame {
  private Image image;
  
  private Color[] read = new Color[6];
  
  private Graphics g;
  
  public ImageReadTest() {
    super("Image Read Test");
  }
  
  public void init(GameContainer container) throws SlickException {
    this.image = new Image("testdata/testcard.png");
    this.read[0] = this.image.getColor(0, 0);
    this.read[1] = this.image.getColor(30, 40);
    this.read[2] = this.image.getColor(55, 70);
    this.read[3] = this.image.getColor(80, 90);
  }
  
  public void render(GameContainer container, Graphics g) {
    this.g = g;
    this.image.draw(100.0F, 100.0F);
    g.setColor(Color.white);
    g.drawString("Move mouse over test image", 200.0F, 20.0F);
    g.setColor(this.read[0]);
    g.drawString(this.read[0].toString(), 100.0F, 300.0F);
    g.setColor(this.read[1]);
    g.drawString(this.read[1].toString(), 150.0F, 320.0F);
    g.setColor(this.read[2]);
    g.drawString(this.read[2].toString(), 200.0F, 340.0F);
    g.setColor(this.read[3]);
    g.drawString(this.read[3].toString(), 250.0F, 360.0F);
    if (this.read[4] != null) {
      g.setColor(this.read[4]);
      g.drawString("On image: " + this.read[4].toString(), 100.0F, 250.0F);
    } 
    if (this.read[5] != null) {
      g.setColor(Color.white);
      g.drawString("On screen: " + this.read[5].toString(), 100.0F, 270.0F);
    } 
  }
  
  public void update(GameContainer container, int delta) {
    int mx = container.getInput().getMouseX();
    int my = container.getInput().getMouseY();
    if (mx >= 100 && my >= 100 && mx < 200 && my < 200) {
      this.read[4] = this.image.getColor(mx - 100, my - 100);
    } else {
      this.read[4] = Color.black;
    } 
    this.read[5] = this.g.getPixel(mx, my);
  }
  
  public static void main(String[] argv) {
    try {
      AppGameContainer container = new AppGameContainer((Game)new ImageReadTest());
      container.setDisplayMode(800, 600, false);
      container.start();
    } catch (SlickException e) {
      e.printStackTrace();
    } 
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\newdawn\slick\tests\ImageReadTest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
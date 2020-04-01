package org.newdawn.slick.tests;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.BigImage;
import org.newdawn.slick.Color;
import org.newdawn.slick.Game;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

public class BigImageTest extends BasicGame {
  private Image original;
  
  private Image image;
  
  private Image imageX;
  
  private Image imageY;
  
  private Image sub;
  
  private Image scaledSub;
  
  private float x;
  
  private float y;
  
  private float ang = 30.0F;
  
  private SpriteSheet bigSheet;
  
  public BigImageTest() {
    super("Big Image Test");
  }
  
  public void init(GameContainer container) throws SlickException {
    this.original = this.image = (Image)new BigImage("testdata/bigimage.tga", 2, 512);
    this.sub = this.image.getSubImage(210, 210, 200, 130);
    this.scaledSub = this.sub.getScaledCopy(2.0F);
    this.image = this.image.getScaledCopy(0.3F);
    this.imageX = this.image.getFlippedCopy(true, false);
    this.imageY = this.imageX.getFlippedCopy(true, true);
    this.bigSheet = new SpriteSheet(this.original, 16, 16);
  }
  
  public void render(GameContainer container, Graphics g) {
    this.original.draw(0.0F, 0.0F, new Color(1.0F, 1.0F, 1.0F, 0.4F));
    this.image.draw(this.x, this.y);
    this.imageX.draw(this.x + 400.0F, this.y);
    this.imageY.draw(this.x, this.y + 300.0F);
    this.scaledSub.draw(this.x + 300.0F, this.y + 300.0F);
    this.bigSheet.getSprite(7, 5).draw(50.0F, 10.0F);
    g.setColor(Color.white);
    g.drawRect(50.0F, 10.0F, 64.0F, 64.0F);
    g.rotate(this.x + 400.0F, this.y + 165.0F, this.ang);
    g.drawImage(this.sub, this.x + 300.0F, this.y + 100.0F);
  }
  
  public static void main(String[] argv) {
    try {
      AppGameContainer container = new AppGameContainer((Game)new BigImageTest());
      container.setDisplayMode(800, 600, false);
      container.start();
    } catch (SlickException e) {
      e.printStackTrace();
    } 
  }
  
  public void update(GameContainer container, int delta) throws SlickException {
    this.ang += delta * 0.1F;
    if (container.getInput().isKeyDown(203))
      this.x -= delta * 0.1F; 
    if (container.getInput().isKeyDown(205))
      this.x += delta * 0.1F; 
    if (container.getInput().isKeyDown(200))
      this.y -= delta * 0.1F; 
    if (container.getInput().isKeyDown(208))
      this.y += delta * 0.1F; 
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\newdawn\slick\tests\BigImageTest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
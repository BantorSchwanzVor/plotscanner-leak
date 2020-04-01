package org.newdawn.slick.tests;

import java.nio.ByteOrder;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.Game;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.ImageBuffer;
import org.newdawn.slick.SlickException;

public class ImageBufferEndianTest extends BasicGame {
  private ImageBuffer redImageBuffer;
  
  private ImageBuffer blueImageBuffer;
  
  private Image fromRed;
  
  private Image fromBlue;
  
  private String endian;
  
  public ImageBufferEndianTest() {
    super("ImageBuffer Endian Test");
  }
  
  public static void main(String[] args) {
    try {
      AppGameContainer container = new AppGameContainer((Game)new ImageBufferEndianTest());
      container.setDisplayMode(800, 600, false);
      container.start();
    } catch (SlickException e) {
      e.printStackTrace();
    } 
  }
  
  public void render(GameContainer container, Graphics g) throws SlickException {
    g.setColor(Color.white);
    g.drawString("Endianness is " + this.endian, 10.0F, 100.0F);
    g.drawString("Image below should be red", 10.0F, 200.0F);
    g.drawImage(this.fromRed, 10.0F, 220.0F);
    g.drawString("Image below should be blue", 410.0F, 200.0F);
    g.drawImage(this.fromBlue, 410.0F, 220.0F);
  }
  
  public void init(GameContainer container) throws SlickException {
    if (ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN) {
      this.endian = "Big endian";
    } else if (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN) {
      this.endian = "Little endian";
    } else {
      this.endian = "no idea";
    } 
    this.redImageBuffer = new ImageBuffer(100, 100);
    fillImageBufferWithColor(this.redImageBuffer, Color.red, 100, 100);
    this.blueImageBuffer = new ImageBuffer(100, 100);
    fillImageBufferWithColor(this.blueImageBuffer, Color.blue, 100, 100);
    this.fromRed = this.redImageBuffer.getImage();
    this.fromBlue = this.blueImageBuffer.getImage();
  }
  
  private void fillImageBufferWithColor(ImageBuffer buffer, Color c, int width, int height) {
    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++)
        buffer.setRGBA(x, y, c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()); 
    } 
  }
  
  public void update(GameContainer container, int delta) throws SlickException {}
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\newdawn\slick\tests\ImageBufferEndianTest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
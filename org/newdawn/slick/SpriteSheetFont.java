package org.newdawn.slick;

import java.io.UnsupportedEncodingException;
import org.newdawn.slick.util.Log;

public class SpriteSheetFont implements Font {
  private SpriteSheet font;
  
  private char startingCharacter;
  
  private int charWidth;
  
  private int charHeight;
  
  private int horizontalCount;
  
  private int numChars;
  
  public SpriteSheetFont(SpriteSheet font, char startingCharacter) {
    this.font = font;
    this.startingCharacter = startingCharacter;
    this.horizontalCount = font.getHorizontalCount();
    int verticalCount = font.getVerticalCount();
    this.charWidth = font.getWidth() / this.horizontalCount;
    this.charHeight = font.getHeight() / verticalCount;
    this.numChars = this.horizontalCount * verticalCount;
  }
  
  public void drawString(float x, float y, String text) {
    drawString(x, y, text, Color.white);
  }
  
  public void drawString(float x, float y, String text, Color col) {
    drawString(x, y, text, col, 0, text.length() - 1);
  }
  
  public void drawString(float x, float y, String text, Color col, int startIndex, int endIndex) {
    try {
      byte[] data = text.getBytes("US-ASCII");
      for (int i = 0; i < data.length; i++) {
        int index = data[i] - this.startingCharacter;
        if (index < this.numChars) {
          int xPos = index % this.horizontalCount;
          int yPos = index / this.horizontalCount;
          if (i >= startIndex || i <= endIndex)
            this.font.getSprite(xPos, yPos)
              .draw(x + (i * this.charWidth), y, col); 
        } 
      } 
    } catch (UnsupportedEncodingException e) {
      Log.error(e);
    } 
  }
  
  public int getHeight(String text) {
    return this.charHeight;
  }
  
  public int getWidth(String text) {
    return this.charWidth * text.length();
  }
  
  public int getLineHeight() {
    return this.charHeight;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\newdawn\slick\SpriteSheetFont.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
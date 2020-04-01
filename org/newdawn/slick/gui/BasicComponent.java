package org.newdawn.slick.gui;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public abstract class BasicComponent extends AbstractComponent {
  protected int x;
  
  protected int y;
  
  protected int width;
  
  protected int height;
  
  public BasicComponent(GUIContext container) {
    super(container);
  }
  
  public int getHeight() {
    return this.height;
  }
  
  public int getWidth() {
    return this.width;
  }
  
  public int getX() {
    return this.x;
  }
  
  public int getY() {
    return this.y;
  }
  
  public abstract void renderImpl(GUIContext paramGUIContext, Graphics paramGraphics);
  
  public void render(GUIContext container, Graphics g) throws SlickException {
    renderImpl(container, g);
  }
  
  public void setLocation(int x, int y) {
    this.x = x;
    this.y = y;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\newdawn\slick\gui\BasicComponent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
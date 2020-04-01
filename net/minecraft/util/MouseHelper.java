package net.minecraft.util;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

public class MouseHelper {
  public int deltaX;
  
  public int deltaY;
  
  public void grabMouseCursor() {
    Mouse.setGrabbed(true);
    this.deltaX = 0;
    this.deltaY = 0;
  }
  
  public void ungrabMouseCursor() {
    Mouse.setCursorPosition(Display.getWidth() / 2, Display.getHeight() / 2);
    Mouse.setGrabbed(false);
  }
  
  public void mouseXYChange() {
    this.deltaX = Mouse.getDX();
    this.deltaY = Mouse.getDY();
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraf\\util\MouseHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
package org.newdawn.slick.command;

public class KeyControl implements Control {
  private int keycode;
  
  public KeyControl(int keycode) {
    this.keycode = keycode;
  }
  
  public boolean equals(Object o) {
    if (o instanceof KeyControl)
      return (((KeyControl)o).keycode == this.keycode); 
    return false;
  }
  
  public int hashCode() {
    return this.keycode;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\newdawn\slick\command\KeyControl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
package org.newdawn.slick.command;

public class MouseButtonControl implements Control {
  private int button;
  
  public MouseButtonControl(int button) {
    this.button = button;
  }
  
  public boolean equals(Object o) {
    if (o instanceof MouseButtonControl)
      return (((MouseButtonControl)o).button == this.button); 
    return false;
  }
  
  public int hashCode() {
    return this.button;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\newdawn\slick\command\MouseButtonControl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
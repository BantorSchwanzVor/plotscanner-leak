package org.newdawn.slick.opengl;

import java.io.IOException;
import java.util.ArrayList;

public class CompositeIOException extends IOException {
  private ArrayList exceptions = new ArrayList();
  
  public void addException(Exception e) {
    this.exceptions.add(e);
  }
  
  public String getMessage() {
    String msg = "Composite Exception: \n";
    for (int i = 0; i < this.exceptions.size(); i++)
      msg = String.valueOf(msg) + "\t" + ((IOException)this.exceptions.get(i)).getMessage() + "\n"; 
    return msg;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\newdawn\slick\opengl\CompositeIOException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
package org.newdawn.slick.util;

import java.io.PrintStream;
import java.util.Date;

public class DefaultLogSystem implements LogSystem {
  public static PrintStream out = System.out;
  
  public void error(String message, Throwable e) {
    error(message);
    error(e);
  }
  
  public void error(Throwable e) {
    out.println(new Date() + " ERROR:" + e.getMessage());
    e.printStackTrace(out);
  }
  
  public void error(String message) {
    out.println(new Date() + " ERROR:" + message);
  }
  
  public void warn(String message) {
    out.println(new Date() + " WARN:" + message);
  }
  
  public void info(String message) {
    out.println(new Date() + " INFO:" + message);
  }
  
  public void debug(String message) {
    out.println(new Date() + " DEBUG:" + message);
  }
  
  public void warn(String message, Throwable e) {
    warn(message);
    e.printStackTrace(out);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\newdawn\slic\\util\DefaultLogSystem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
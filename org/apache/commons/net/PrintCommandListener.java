package org.apache.commons.net;

import java.io.PrintStream;
import java.io.PrintWriter;

public class PrintCommandListener implements ProtocolCommandListener {
  private final PrintWriter __writer;
  
  private final boolean __nologin;
  
  private final char __eolMarker;
  
  private final boolean __directionMarker;
  
  public PrintCommandListener(PrintStream stream) {
    this(new PrintWriter(stream));
  }
  
  public PrintCommandListener(PrintStream stream, boolean suppressLogin) {
    this(new PrintWriter(stream), suppressLogin);
  }
  
  public PrintCommandListener(PrintStream stream, boolean suppressLogin, char eolMarker) {
    this(new PrintWriter(stream), suppressLogin, eolMarker);
  }
  
  public PrintCommandListener(PrintStream stream, boolean suppressLogin, char eolMarker, boolean showDirection) {
    this(new PrintWriter(stream), suppressLogin, eolMarker, showDirection);
  }
  
  public PrintCommandListener(PrintWriter writer) {
    this(writer, false);
  }
  
  public PrintCommandListener(PrintWriter writer, boolean suppressLogin) {
    this(writer, suppressLogin, false);
  }
  
  public PrintCommandListener(PrintWriter writer, boolean suppressLogin, char eolMarker) {
    this(writer, suppressLogin, eolMarker, false);
  }
  
  public PrintCommandListener(PrintWriter writer, boolean suppressLogin, char eolMarker, boolean showDirection) {
    this.__writer = writer;
    this.__nologin = suppressLogin;
    this.__eolMarker = eolMarker;
    this.__directionMarker = showDirection;
  }
  
  public void protocolCommandSent(ProtocolCommandEvent event) {
    if (this.__directionMarker)
      this.__writer.print("> "); 
    if (this.__nologin) {
      String cmd = event.getCommand();
      if ("PASS".equalsIgnoreCase(cmd) || "USER".equalsIgnoreCase(cmd)) {
        this.__writer.print(cmd);
        this.__writer.println(" *******");
      } else {
        String IMAP_LOGIN = "LOGIN";
        if ("LOGIN".equalsIgnoreCase(cmd)) {
          String msg = event.getMessage();
          msg = msg.substring(0, msg.indexOf("LOGIN") + "LOGIN".length());
          this.__writer.print(msg);
          this.__writer.println(" *******");
        } else {
          this.__writer.print(getPrintableString(event.getMessage()));
        } 
      } 
    } else {
      this.__writer.print(getPrintableString(event.getMessage()));
    } 
    this.__writer.flush();
  }
  
  private String getPrintableString(String msg) {
    if (this.__eolMarker == '\000')
      return msg; 
    int pos = msg.indexOf("\r\n");
    if (pos > 0) {
      StringBuilder sb = new StringBuilder();
      sb.append(msg.substring(0, pos));
      sb.append(this.__eolMarker);
      sb.append(msg.substring(pos));
      return sb.toString();
    } 
    return msg;
  }
  
  public void protocolReplyReceived(ProtocolCommandEvent event) {
    if (this.__directionMarker)
      this.__writer.print("< "); 
    this.__writer.print(event.getMessage());
    this.__writer.flush();
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\apache\commons\net\PrintCommandListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
package org.apache.commons.net.telnet;

public class SuppressGAOptionHandler extends TelnetOptionHandler {
  public SuppressGAOptionHandler(boolean initlocal, boolean initremote, boolean acceptlocal, boolean acceptremote) {
    super(3, initlocal, initremote, acceptlocal, acceptremote);
  }
  
  public SuppressGAOptionHandler() {
    super(3, false, false, false, false);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\apache\commons\net\telnet\SuppressGAOptionHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
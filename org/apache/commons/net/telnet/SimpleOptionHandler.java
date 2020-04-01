package org.apache.commons.net.telnet;

public class SimpleOptionHandler extends TelnetOptionHandler {
  public SimpleOptionHandler(int optcode, boolean initlocal, boolean initremote, boolean acceptlocal, boolean acceptremote) {
    super(optcode, initlocal, initremote, acceptlocal, acceptremote);
  }
  
  public SimpleOptionHandler(int optcode) {
    super(optcode, false, false, false, false);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\apache\commons\net\telnet\SimpleOptionHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
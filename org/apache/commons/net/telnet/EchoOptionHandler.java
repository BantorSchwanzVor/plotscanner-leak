package org.apache.commons.net.telnet;

public class EchoOptionHandler extends TelnetOptionHandler {
  public EchoOptionHandler(boolean initlocal, boolean initremote, boolean acceptlocal, boolean acceptremote) {
    super(1, initlocal, initremote, acceptlocal, acceptremote);
  }
  
  public EchoOptionHandler() {
    super(1, false, false, false, false);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\apache\commons\net\telnet\EchoOptionHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
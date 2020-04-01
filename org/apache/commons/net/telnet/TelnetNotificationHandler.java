package org.apache.commons.net.telnet;

public interface TelnetNotificationHandler {
  public static final int RECEIVED_DO = 1;
  
  public static final int RECEIVED_DONT = 2;
  
  public static final int RECEIVED_WILL = 3;
  
  public static final int RECEIVED_WONT = 4;
  
  public static final int RECEIVED_COMMAND = 5;
  
  void receivedNegotiation(int paramInt1, int paramInt2);
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\apache\commons\net\telnet\TelnetNotificationHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
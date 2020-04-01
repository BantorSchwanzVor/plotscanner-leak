package org.apache.commons.net.telnet;

public class InvalidTelnetOptionException extends Exception {
  private static final long serialVersionUID = -2516777155928793597L;
  
  private final int optionCode;
  
  private final String msg;
  
  public InvalidTelnetOptionException(String message, int optcode) {
    this.optionCode = optcode;
    this.msg = message;
  }
  
  public String getMessage() {
    return String.valueOf(this.msg) + ": " + this.optionCode;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\apache\commons\net\telnet\InvalidTelnetOptionException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
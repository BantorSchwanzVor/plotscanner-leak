package com.mysql.cj.exceptions;

import com.mysql.cj.Messages;

public class CJTimeoutException extends CJException {
  private static final long serialVersionUID = -7440108828056331100L;
  
  public CJTimeoutException() {
    super(Messages.getString("MySQLTimeoutException.0"));
  }
  
  public CJTimeoutException(String message) {
    super(message);
  }
  
  public CJTimeoutException(Throwable cause) {
    super(cause);
  }
  
  public CJTimeoutException(String message, Throwable cause) {
    super(message, cause);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\exceptions\CJTimeoutException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
package com.mysql.cj.exceptions;

public class ConnectionIsClosedException extends CJException {
  private static final long serialVersionUID = -8001652264426656450L;
  
  public ConnectionIsClosedException() {
    setSQLState("08003");
  }
  
  public ConnectionIsClosedException(String message) {
    super(message);
    setSQLState("08003");
  }
  
  public ConnectionIsClosedException(String message, Throwable cause) {
    super(message, cause);
    setSQLState("08003");
  }
  
  public ConnectionIsClosedException(Throwable cause) {
    super(cause);
    setSQLState("08003");
  }
  
  protected ConnectionIsClosedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
    setSQLState("08003");
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\exceptions\ConnectionIsClosedException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
package com.mysql.cj.exceptions;

public class PropertyNotModifiableException extends CJException {
  private static final long serialVersionUID = -8001652264426656450L;
  
  public PropertyNotModifiableException() {}
  
  public PropertyNotModifiableException(String message) {
    super(message);
  }
  
  public PropertyNotModifiableException(String message, Throwable cause) {
    super(message, cause);
  }
  
  public PropertyNotModifiableException(Throwable cause) {
    super(cause);
  }
  
  protected PropertyNotModifiableException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\exceptions\PropertyNotModifiableException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
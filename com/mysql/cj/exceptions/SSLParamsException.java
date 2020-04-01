package com.mysql.cj.exceptions;

public class SSLParamsException extends CJException {
  private static final long serialVersionUID = -6597843374954727858L;
  
  public SSLParamsException() {
    setSQLState("08000");
  }
  
  public SSLParamsException(String message) {
    super(message);
    setSQLState("08000");
  }
  
  public SSLParamsException(String message, Throwable cause) {
    super(message, cause);
    setSQLState("08000");
  }
  
  public SSLParamsException(Throwable cause) {
    super(cause);
    setSQLState("08000");
  }
  
  public SSLParamsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
    setSQLState("08000");
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\exceptions\SSLParamsException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
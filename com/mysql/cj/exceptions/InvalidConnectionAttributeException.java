package com.mysql.cj.exceptions;

public class InvalidConnectionAttributeException extends CJException {
  private static final long serialVersionUID = -4814924499233623016L;
  
  public InvalidConnectionAttributeException() {
    setSQLState("01S00");
  }
  
  public InvalidConnectionAttributeException(String message) {
    super(message);
    setSQLState("01S00");
  }
  
  public InvalidConnectionAttributeException(String message, Throwable cause) {
    super(message, cause);
    setSQLState("01S00");
  }
  
  public InvalidConnectionAttributeException(Throwable cause) {
    super(cause);
    setSQLState("01S00");
  }
  
  public InvalidConnectionAttributeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
    setSQLState("01S00");
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\exceptions\InvalidConnectionAttributeException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
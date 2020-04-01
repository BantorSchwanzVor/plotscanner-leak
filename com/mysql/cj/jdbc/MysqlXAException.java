package com.mysql.cj.jdbc;

import javax.transaction.xa.XAException;

class MysqlXAException extends XAException {
  private static final long serialVersionUID = -9075817535836563004L;
  
  private String message;
  
  protected String xidAsString;
  
  public MysqlXAException(int errorCode, String message, String xidAsString) {
    super(errorCode);
    this.message = message;
    this.xidAsString = xidAsString;
  }
  
  public MysqlXAException(String message, String xidAsString) {
    this.message = message;
    this.xidAsString = xidAsString;
  }
  
  public String getMessage() {
    String superMessage = super.getMessage();
    StringBuilder returnedMessage = new StringBuilder();
    if (superMessage != null) {
      returnedMessage.append(superMessage);
      returnedMessage.append(":");
    } 
    if (this.message != null)
      returnedMessage.append(this.message); 
    return returnedMessage.toString();
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\jdbc\MysqlXAException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
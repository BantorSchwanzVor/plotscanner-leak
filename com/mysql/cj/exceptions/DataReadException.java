package com.mysql.cj.exceptions;

public class DataReadException extends CJException {
  private static final long serialVersionUID = 1684265521187171525L;
  
  public DataReadException(Exception cause) {
    super(cause);
    setSQLState("S1009");
  }
  
  public DataReadException(String msg) {
    super(msg);
    setSQLState("S1009");
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\exceptions\DataReadException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
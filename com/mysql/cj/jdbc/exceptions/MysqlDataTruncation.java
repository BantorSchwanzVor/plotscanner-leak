package com.mysql.cj.jdbc.exceptions;

import java.sql.DataTruncation;

public class MysqlDataTruncation extends DataTruncation {
  static final long serialVersionUID = 3263928195256986226L;
  
  private String message;
  
  private int vendorErrorCode;
  
  public MysqlDataTruncation(String message, int index, boolean parameter, boolean read, int dataSize, int transferSize, int vendorErrorCode) {
    super(index, parameter, read, dataSize, transferSize);
    this.message = message;
    this.vendorErrorCode = vendorErrorCode;
  }
  
  public int getErrorCode() {
    return this.vendorErrorCode;
  }
  
  public String getMessage() {
    return super.getMessage() + ": " + this.message;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\jdbc\exceptions\MysqlDataTruncation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
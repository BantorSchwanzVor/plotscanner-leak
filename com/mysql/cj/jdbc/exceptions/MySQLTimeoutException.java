package com.mysql.cj.jdbc.exceptions;

import com.mysql.cj.Messages;
import java.sql.SQLTimeoutException;

public class MySQLTimeoutException extends SQLTimeoutException {
  static final long serialVersionUID = -789621240523239939L;
  
  public MySQLTimeoutException(String reason, String SQLState, int vendorCode) {
    super(reason, SQLState, vendorCode);
  }
  
  public MySQLTimeoutException(String reason, String SQLState) {
    super(reason, SQLState);
  }
  
  public MySQLTimeoutException(String reason) {
    super(reason);
  }
  
  public MySQLTimeoutException() {
    super(Messages.getString("MySQLTimeoutException.0"));
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\jdbc\exceptions\MySQLTimeoutException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
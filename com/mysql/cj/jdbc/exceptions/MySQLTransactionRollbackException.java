package com.mysql.cj.jdbc.exceptions;

import com.mysql.cj.exceptions.DeadlockTimeoutRollbackMarker;
import java.sql.SQLTransactionRollbackException;

public class MySQLTransactionRollbackException extends SQLTransactionRollbackException implements DeadlockTimeoutRollbackMarker {
  static final long serialVersionUID = 6034999468737899730L;
  
  public MySQLTransactionRollbackException(String reason, String SQLState, int vendorCode) {
    super(reason, SQLState, vendorCode);
  }
  
  public MySQLTransactionRollbackException(String reason, String SQLState) {
    super(reason, SQLState);
  }
  
  public MySQLTransactionRollbackException(String reason) {
    super(reason);
  }
  
  public MySQLTransactionRollbackException() {}
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\jdbc\exceptions\MySQLTransactionRollbackException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
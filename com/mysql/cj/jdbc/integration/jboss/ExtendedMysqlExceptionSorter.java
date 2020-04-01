package com.mysql.cj.jdbc.integration.jboss;

import java.sql.SQLException;
import org.jboss.resource.adapter.jdbc.vendor.MySQLExceptionSorter;

public final class ExtendedMysqlExceptionSorter extends MySQLExceptionSorter {
  static final long serialVersionUID = -2454582336945931069L;
  
  public boolean isExceptionFatal(SQLException ex) {
    String sqlState = ex.getSQLState();
    if (sqlState != null && sqlState.startsWith("08"))
      return true; 
    return super.isExceptionFatal(ex);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\jdbc\integration\jboss\ExtendedMysqlExceptionSorter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
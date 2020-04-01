package com.mysql.cj.jdbc.integration.c3p0;

import com.mchange.v2.c3p0.C3P0ProxyConnection;
import com.mchange.v2.c3p0.QueryConnectionTester;
import com.mysql.cj.jdbc.JdbcConnection;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public final class MysqlConnectionTester implements QueryConnectionTester {
  private static final long serialVersionUID = 3256444690067896368L;
  
  private static final Object[] NO_ARGS_ARRAY = new Object[0];
  
  private transient Method pingMethod;
  
  public MysqlConnectionTester() {
    try {
      this.pingMethod = JdbcConnection.class.getMethod("ping", (Class[])null);
    } catch (Exception exception) {}
  }
  
  public int activeCheckConnection(Connection con) {
    try {
      if (this.pingMethod != null) {
        if (con instanceof JdbcConnection) {
          ((JdbcConnection)con).ping();
        } else {
          C3P0ProxyConnection castCon = (C3P0ProxyConnection)con;
          castCon.rawConnectionOperation(this.pingMethod, C3P0ProxyConnection.RAW_CONNECTION, NO_ARGS_ARRAY);
        } 
      } else {
        Statement pingStatement = null;
        try {
          pingStatement = con.createStatement();
          pingStatement.executeQuery("SELECT 1").close();
        } finally {
          if (pingStatement != null)
            pingStatement.close(); 
        } 
      } 
      return 0;
    } catch (Exception ex) {
      return -1;
    } 
  }
  
  public int statusOnException(Connection arg0, Throwable throwable) {
    if (throwable instanceof com.mysql.cj.jdbc.exceptions.CommunicationsException || throwable instanceof com.mysql.cj.exceptions.CJCommunicationsException)
      return -1; 
    if (throwable instanceof SQLException) {
      String sqlState = ((SQLException)throwable).getSQLState();
      if (sqlState != null && sqlState.startsWith("08"))
        return -1; 
      return 0;
    } 
    return -1;
  }
  
  public int activeCheckConnection(Connection arg0, String arg1) {
    return 0;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\jdbc\integration\c3p0\MysqlConnectionTester.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
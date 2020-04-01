package com.mysql.cj.jdbc;

import com.mysql.cj.PingTarget;
import com.mysql.cj.Query;
import com.mysql.cj.exceptions.ExceptionInterceptor;
import com.mysql.cj.jdbc.result.ResultSetInternalMethods;
import java.io.InputStream;
import java.sql.SQLException;
import java.sql.Statement;

public interface JdbcStatement extends Statement, Query {
  public static final int MAX_ROWS = 50000000;
  
  void enableStreamingResults() throws SQLException;
  
  void disableStreamingResults() throws SQLException;
  
  void setLocalInfileInputStream(InputStream paramInputStream);
  
  InputStream getLocalInfileInputStream();
  
  void setPingTarget(PingTarget paramPingTarget);
  
  ExceptionInterceptor getExceptionInterceptor();
  
  void removeOpenResultSet(ResultSetInternalMethods paramResultSetInternalMethods);
  
  int getOpenResultSetCount();
  
  void setHoldResultsOpenOverClose(boolean paramBoolean);
  
  Query getQuery();
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\jdbc\JdbcStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
package com.mysql.cj.jdbc.interceptors;

import com.mysql.cj.MysqlConnection;
import com.mysql.cj.Query;
import com.mysql.cj.exceptions.ExceptionFactory;
import com.mysql.cj.interceptors.QueryInterceptor;
import com.mysql.cj.jdbc.JdbcConnection;
import com.mysql.cj.jdbc.util.ResultSetUtil;
import com.mysql.cj.log.Log;
import com.mysql.cj.protocol.ServerSession;
import com.mysql.cj.util.Util;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.function.Supplier;

public class ServerStatusDiffInterceptor implements QueryInterceptor {
  private Map<String, String> preExecuteValues = new HashMap<>();
  
  private Map<String, String> postExecuteValues = new HashMap<>();
  
  private JdbcConnection connection;
  
  private Log log;
  
  public QueryInterceptor init(MysqlConnection conn, Properties props, Log l) {
    this.connection = (JdbcConnection)conn;
    this.log = l;
    return this;
  }
  
  public <T extends com.mysql.cj.protocol.Resultset> T postProcess(Supplier<String> sql, Query interceptedQuery, T originalResultSet, ServerSession serverSession) {
    populateMapWithSessionStatusValues(this.postExecuteValues);
    this.log.logInfo("Server status change for query:\n" + Util.calculateDifferences(this.preExecuteValues, this.postExecuteValues));
    return null;
  }
  
  private void populateMapWithSessionStatusValues(Map<String, String> toPopulate) {
    Statement stmt = null;
    ResultSet rs = null;
    try {
      try {
        toPopulate.clear();
        stmt = this.connection.createStatement();
        rs = stmt.executeQuery("SHOW SESSION STATUS");
        ResultSetUtil.resultSetToMap(toPopulate, rs);
      } finally {
        if (rs != null)
          rs.close(); 
        if (stmt != null)
          stmt.close(); 
      } 
    } catch (SQLException ex) {
      throw ExceptionFactory.createException(ex.getMessage(), ex);
    } 
  }
  
  public <T extends com.mysql.cj.protocol.Resultset> T preProcess(Supplier<String> sql, Query interceptedQuery) {
    populateMapWithSessionStatusValues(this.preExecuteValues);
    return null;
  }
  
  public boolean executeTopLevelOnly() {
    return true;
  }
  
  public void destroy() {
    this.connection = null;
    this.log = null;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\jdbc\interceptors\ServerStatusDiffInterceptor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
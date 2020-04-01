package com.mysql.cj.jdbc.util;

import com.mysql.cj.jdbc.Driver;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public abstract class BaseBugReport {
  private Connection conn;
  
  private Driver driver;
  
  public BaseBugReport() {
    try {
      this.driver = new Driver();
    } catch (SQLException ex) {
      throw new RuntimeException(ex.toString());
    } 
  }
  
  public abstract void setUp() throws Exception;
  
  public abstract void tearDown() throws Exception;
  
  public abstract void runTest() throws Exception;
  
  public final void run() throws Exception {
    try {
      setUp();
      runTest();
    } finally {
      tearDown();
    } 
  }
  
  protected final void assertTrue(String message, boolean condition) throws Exception {
    if (!condition)
      throw new Exception("Assertion failed: " + message); 
  }
  
  protected final void assertTrue(boolean condition) throws Exception {
    assertTrue("(no message given)", condition);
  }
  
  public String getUrl() {
    return "jdbc:mysql:///test";
  }
  
  public final synchronized Connection getConnection() throws SQLException {
    if (this.conn == null || this.conn.isClosed())
      this.conn = getNewConnection(); 
    return this.conn;
  }
  
  public final synchronized Connection getNewConnection() throws SQLException {
    return getConnection(getUrl());
  }
  
  public final synchronized Connection getConnection(String url) throws SQLException {
    return getConnection(url, null);
  }
  
  public final synchronized Connection getConnection(String url, Properties props) throws SQLException {
    return this.driver.connect(url, props);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\jdb\\util\BaseBugReport.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
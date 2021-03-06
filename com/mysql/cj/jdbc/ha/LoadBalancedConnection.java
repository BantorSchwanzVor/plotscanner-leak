package com.mysql.cj.jdbc.ha;

import com.mysql.cj.jdbc.JdbcConnection;
import java.sql.SQLException;

public interface LoadBalancedConnection extends JdbcConnection {
  boolean addHost(String paramString) throws SQLException;
  
  void removeHost(String paramString) throws SQLException;
  
  void removeHostWhenNotInUse(String paramString) throws SQLException;
  
  void ping(boolean paramBoolean) throws SQLException;
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\jdbc\ha\LoadBalancedConnection.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
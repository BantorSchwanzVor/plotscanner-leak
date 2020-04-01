package com.mysql.cj.jdbc.ha;

import com.mysql.cj.jdbc.JdbcConnection;
import java.sql.SQLException;

public interface ReplicationConnection extends JdbcConnection {
  long getConnectionGroupId();
  
  JdbcConnection getCurrentConnection();
  
  JdbcConnection getMasterConnection();
  
  void promoteSlaveToMaster(String paramString) throws SQLException;
  
  void removeMasterHost(String paramString) throws SQLException;
  
  void removeMasterHost(String paramString, boolean paramBoolean) throws SQLException;
  
  boolean isHostMaster(String paramString);
  
  JdbcConnection getSlavesConnection();
  
  void addSlaveHost(String paramString) throws SQLException;
  
  void removeSlave(String paramString) throws SQLException;
  
  void removeSlave(String paramString, boolean paramBoolean) throws SQLException;
  
  boolean isHostSlave(String paramString);
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\jdbc\ha\ReplicationConnection.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
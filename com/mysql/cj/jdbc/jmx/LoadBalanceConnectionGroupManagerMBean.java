package com.mysql.cj.jdbc.jmx;

import java.sql.SQLException;

public interface LoadBalanceConnectionGroupManagerMBean {
  int getActiveHostCount(String paramString);
  
  int getTotalHostCount(String paramString);
  
  long getTotalLogicalConnectionCount(String paramString);
  
  long getActiveLogicalConnectionCount(String paramString);
  
  long getActivePhysicalConnectionCount(String paramString);
  
  long getTotalPhysicalConnectionCount(String paramString);
  
  long getTotalTransactionCount(String paramString);
  
  void removeHost(String paramString1, String paramString2) throws SQLException;
  
  void stopNewConnectionsToHost(String paramString1, String paramString2) throws SQLException;
  
  void addHost(String paramString1, String paramString2, boolean paramBoolean);
  
  String getActiveHostsList(String paramString);
  
  String getRegisteredConnectionGroups();
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\jdbc\jmx\LoadBalanceConnectionGroupManagerMBean.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
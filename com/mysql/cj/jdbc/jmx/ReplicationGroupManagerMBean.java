package com.mysql.cj.jdbc.jmx;

import java.sql.SQLException;

public interface ReplicationGroupManagerMBean {
  void addSlaveHost(String paramString1, String paramString2) throws SQLException;
  
  void removeSlaveHost(String paramString1, String paramString2) throws SQLException;
  
  void promoteSlaveToMaster(String paramString1, String paramString2) throws SQLException;
  
  void removeMasterHost(String paramString1, String paramString2) throws SQLException;
  
  String getMasterHostsList(String paramString);
  
  String getSlaveHostsList(String paramString);
  
  String getRegisteredConnectionGroups();
  
  int getActiveMasterHostCount(String paramString);
  
  int getActiveSlaveHostCount(String paramString);
  
  int getSlavePromotionCount(String paramString);
  
  long getTotalLogicalConnectionCount(String paramString);
  
  long getActiveLogicalConnectionCount(String paramString);
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\jdbc\jmx\ReplicationGroupManagerMBean.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
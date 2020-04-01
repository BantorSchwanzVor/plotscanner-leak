package com.mysql.cj;

import com.mysql.cj.conf.PropertySet;
import com.mysql.cj.exceptions.ExceptionInterceptor;
import java.util.Properties;

public interface MysqlConnection {
  PropertySet getPropertySet();
  
  void createNewIO(boolean paramBoolean);
  
  long getId();
  
  Properties getProperties();
  
  Object getConnectionMutex();
  
  Session getSession();
  
  String getURL();
  
  String getUser();
  
  ExceptionInterceptor getExceptionInterceptor();
  
  void checkClosed();
  
  void normalClose();
  
  void cleanup(Throwable paramThrowable);
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\MysqlConnection.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
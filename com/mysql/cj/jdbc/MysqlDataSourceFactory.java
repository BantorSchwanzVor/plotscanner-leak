package com.mysql.cj.jdbc;

import com.mysql.cj.Messages;
import com.mysql.cj.conf.PropertyKey;
import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.RefAddr;
import javax.naming.Reference;
import javax.naming.spi.ObjectFactory;

public class MysqlDataSourceFactory implements ObjectFactory {
  protected static final String DATA_SOURCE_CLASS_NAME = MysqlDataSource.class.getName();
  
  protected static final String POOL_DATA_SOURCE_CLASS_NAME = MysqlConnectionPoolDataSource.class.getName();
  
  protected static final String XA_DATA_SOURCE_CLASS_NAME = MysqlXADataSource.class.getName();
  
  public Object getObjectInstance(Object refObj, Name nm, Context ctx, Hashtable<?, ?> env) throws Exception {
    Reference ref = (Reference)refObj;
    String className = ref.getClassName();
    if (className != null && (className
      .equals(DATA_SOURCE_CLASS_NAME) || className.equals(POOL_DATA_SOURCE_CLASS_NAME) || className.equals(XA_DATA_SOURCE_CLASS_NAME))) {
      MysqlDataSource dataSource = null;
      try {
        dataSource = (MysqlDataSource)Class.forName(className).newInstance();
      } catch (Exception ex) {
        throw new RuntimeException(Messages.getString("MysqlDataSourceFactory.0", new Object[] { className, ex.toString() }));
      } 
      int portNumber = 3306;
      String portNumberAsString = nullSafeRefAddrStringGet("port", ref);
      if (portNumberAsString != null)
        portNumber = Integer.parseInt(portNumberAsString); 
      dataSource.setPort(portNumber);
      String user = nullSafeRefAddrStringGet(PropertyKey.USER.getKeyName(), ref);
      if (user != null)
        dataSource.setUser(user); 
      String password = nullSafeRefAddrStringGet(PropertyKey.PASSWORD.getKeyName(), ref);
      if (password != null)
        dataSource.setPassword(password); 
      String serverName = nullSafeRefAddrStringGet("serverName", ref);
      if (serverName != null)
        dataSource.setServerName(serverName); 
      String databaseName = nullSafeRefAddrStringGet("databaseName", ref);
      if (databaseName != null)
        dataSource.setDatabaseName(databaseName); 
      String explicitUrlAsString = nullSafeRefAddrStringGet("explicitUrl", ref);
      if (explicitUrlAsString != null && 
        Boolean.valueOf(explicitUrlAsString).booleanValue())
        dataSource.setUrl(nullSafeRefAddrStringGet("url", ref)); 
      dataSource.setPropertiesViaRef(ref);
      return dataSource;
    } 
    return null;
  }
  
  private String nullSafeRefAddrStringGet(String referenceName, Reference ref) {
    RefAddr refAddr = ref.get(referenceName);
    String asString = (refAddr != null) ? (String)refAddr.getContent() : null;
    return asString;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\jdbc\MysqlDataSourceFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
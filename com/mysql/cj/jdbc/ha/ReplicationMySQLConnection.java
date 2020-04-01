package com.mysql.cj.jdbc.ha;

import com.mysql.cj.Messages;
import com.mysql.cj.exceptions.CJException;
import com.mysql.cj.jdbc.JdbcConnection;
import com.mysql.cj.jdbc.exceptions.SQLError;
import com.mysql.cj.jdbc.exceptions.SQLExceptionsMapping;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.Executor;

public class ReplicationMySQLConnection extends MultiHostMySQLConnection implements ReplicationConnection {
  public ReplicationMySQLConnection(MultiHostConnectionProxy proxy) {
    super(proxy);
  }
  
  public ReplicationConnectionProxy getThisAsProxy() {
    return (ReplicationConnectionProxy)super.getThisAsProxy();
  }
  
  public JdbcConnection getActiveMySQLConnection() {
    return getCurrentConnection();
  }
  
  public synchronized JdbcConnection getCurrentConnection() {
    return getThisAsProxy().getCurrentConnection();
  }
  
  public long getConnectionGroupId() {
    return getThisAsProxy().getConnectionGroupId();
  }
  
  public synchronized JdbcConnection getMasterConnection() {
    return getThisAsProxy().getMasterConnection();
  }
  
  private JdbcConnection getValidatedMasterConnection() {
    JdbcConnection conn = (getThisAsProxy()).masterConnection;
    try {
      return (conn == null || conn.isClosed()) ? null : conn;
    } catch (SQLException e) {
      return null;
    } 
  }
  
  public void promoteSlaveToMaster(String host) throws SQLException {
    try {
      getThisAsProxy().promoteSlaveToMaster(host);
      return;
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  public void removeMasterHost(String host) throws SQLException {
    try {
      getThisAsProxy().removeMasterHost(host);
      return;
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  public void removeMasterHost(String host, boolean waitUntilNotInUse) throws SQLException {
    try {
      getThisAsProxy().removeMasterHost(host, waitUntilNotInUse);
      return;
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  public boolean isHostMaster(String host) {
    return getThisAsProxy().isHostMaster(host);
  }
  
  public synchronized JdbcConnection getSlavesConnection() {
    return getThisAsProxy().getSlavesConnection();
  }
  
  private JdbcConnection getValidatedSlavesConnection() {
    JdbcConnection conn = (getThisAsProxy()).slavesConnection;
    try {
      return (conn == null || conn.isClosed()) ? null : conn;
    } catch (SQLException e) {
      return null;
    } 
  }
  
  public void addSlaveHost(String host) throws SQLException {
    try {
      getThisAsProxy().addSlaveHost(host);
      return;
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  public void removeSlave(String host) throws SQLException {
    try {
      getThisAsProxy().removeSlave(host);
      return;
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  public void removeSlave(String host, boolean closeGently) throws SQLException {
    try {
      getThisAsProxy().removeSlave(host, closeGently);
      return;
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  public boolean isHostSlave(String host) {
    return getThisAsProxy().isHostSlave(host);
  }
  
  public void setReadOnly(boolean readOnlyFlag) throws SQLException {
    try {
      getThisAsProxy().setReadOnly(readOnlyFlag);
      return;
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  public boolean isReadOnly() throws SQLException {
    try {
      return getThisAsProxy().isReadOnly();
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  public synchronized void ping() throws SQLException {
    try {
      try {
        JdbcConnection conn;
        if ((conn = getValidatedMasterConnection()) != null)
          conn.ping(); 
      } catch (SQLException e) {
        if (isMasterConnection())
          throw e; 
      } 
      try {
        JdbcConnection conn;
        if ((conn = getValidatedSlavesConnection()) != null)
          conn.ping(); 
      } catch (SQLException e) {
        if (!isMasterConnection())
          throw e; 
      } 
      return;
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  public synchronized void changeUser(String userName, String newPassword) throws SQLException {
    try {
      JdbcConnection conn;
      if ((conn = getValidatedMasterConnection()) != null)
        conn.changeUser(userName, newPassword); 
      if ((conn = getValidatedSlavesConnection()) != null)
        conn.changeUser(userName, newPassword); 
      return;
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  public synchronized void setStatementComment(String comment) {
    JdbcConnection conn;
    if ((conn = getValidatedMasterConnection()) != null)
      conn.setStatementComment(comment); 
    if ((conn = getValidatedSlavesConnection()) != null)
      conn.setStatementComment(comment); 
  }
  
  public boolean hasSameProperties(JdbcConnection c) {
    JdbcConnection connM = getValidatedMasterConnection();
    JdbcConnection connS = getValidatedSlavesConnection();
    if (connM == null && connS == null)
      return false; 
    return ((connM == null || connM.hasSameProperties(c)) && (connS == null || connS.hasSameProperties(c)));
  }
  
  public Properties getProperties() {
    Properties props = new Properties();
    JdbcConnection conn;
    if ((conn = getValidatedMasterConnection()) != null)
      props.putAll(conn.getProperties()); 
    if ((conn = getValidatedSlavesConnection()) != null)
      props.putAll(conn.getProperties()); 
    return props;
  }
  
  public void abort(Executor executor) throws SQLException {
    try {
      getThisAsProxy().doAbort(executor);
      return;
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  public void abortInternal() throws SQLException {
    try {
      getThisAsProxy().doAbortInternal();
      return;
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  public void setProxy(JdbcConnection proxy) {
    getThisAsProxy().setProxy(proxy);
  }
  
  public boolean isWrapperFor(Class<?> iface) throws SQLException {
    try {
      return iface.isInstance(this);
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  public <T> T unwrap(Class<T> iface) throws SQLException {
    try {
      try {
        return iface.cast(this);
      } catch (ClassCastException cce) {
        throw SQLError.createSQLException(Messages.getString("Common.UnableToUnwrap", new Object[] { iface.toString() }), "S1009", 
            getExceptionInterceptor());
      } 
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  @Deprecated
  public synchronized void clearHasTriedMaster() {
    (getThisAsProxy()).masterConnection.clearHasTriedMaster();
    (getThisAsProxy()).slavesConnection.clearHasTriedMaster();
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\jdbc\ha\ReplicationMySQLConnection.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
package com.mysql.cj.jdbc.ha;

import com.mysql.cj.Messages;
import com.mysql.cj.PingTarget;
import com.mysql.cj.conf.ConnectionUrl;
import com.mysql.cj.conf.HostInfo;
import com.mysql.cj.conf.HostsListView;
import com.mysql.cj.conf.PropertyKey;
import com.mysql.cj.conf.url.LoadBalanceConnectionUrl;
import com.mysql.cj.conf.url.ReplicationConnectionUrl;
import com.mysql.cj.jdbc.JdbcConnection;
import com.mysql.cj.jdbc.JdbcStatement;
import com.mysql.cj.jdbc.exceptions.SQLError;
import com.mysql.cj.util.StringUtils;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executor;

public class ReplicationConnectionProxy extends MultiHostConnectionProxy implements PingTarget {
  private ReplicationConnection thisAsReplicationConnection;
  
  protected boolean enableJMX = false;
  
  protected boolean allowMasterDownConnections = false;
  
  protected boolean allowSlaveDownConnections = false;
  
  protected boolean readFromMasterWhenNoSlaves = false;
  
  protected boolean readFromMasterWhenNoSlavesOriginal = false;
  
  protected boolean readOnly = false;
  
  ReplicationConnectionGroup connectionGroup;
  
  private long connectionGroupID = -1L;
  
  private List<HostInfo> masterHosts;
  
  protected LoadBalancedConnection masterConnection;
  
  private List<HostInfo> slaveHosts;
  
  protected LoadBalancedConnection slavesConnection;
  
  public static ReplicationConnection createProxyInstance(ConnectionUrl connectionUrl) throws SQLException {
    ReplicationConnectionProxy connProxy = new ReplicationConnectionProxy(connectionUrl);
    return (ReplicationConnection)Proxy.newProxyInstance(ReplicationConnection.class.getClassLoader(), new Class[] { ReplicationConnection.class, JdbcConnection.class }, connProxy);
  }
  
  private ReplicationConnectionProxy(ConnectionUrl connectionUrl) throws SQLException {
    Properties props = connectionUrl.getConnectionArgumentsAsProperties();
    this.thisAsReplicationConnection = (ReplicationConnection)this.thisAsConnection;
    this.connectionUrl = connectionUrl;
    String enableJMXAsString = props.getProperty(PropertyKey.ha_enableJMX.getKeyName(), "false");
    try {
      this.enableJMX = Boolean.parseBoolean(enableJMXAsString);
    } catch (Exception e) {
      throw SQLError.createSQLException(Messages.getString("MultihostConnection.badValueForHaEnableJMX", new Object[] { enableJMXAsString }), "S1009", null);
    } 
    String allowMasterDownConnectionsAsString = props.getProperty(PropertyKey.allowMasterDownConnections.getKeyName(), "false");
    try {
      this.allowMasterDownConnections = Boolean.parseBoolean(allowMasterDownConnectionsAsString);
    } catch (Exception e) {
      throw SQLError.createSQLException(
          Messages.getString("ReplicationConnectionProxy.badValueForAllowMasterDownConnections", new Object[] { enableJMXAsString }), "S1009", null);
    } 
    String allowSlaveDownConnectionsAsString = props.getProperty(PropertyKey.allowSlaveDownConnections.getKeyName(), "false");
    try {
      this.allowSlaveDownConnections = Boolean.parseBoolean(allowSlaveDownConnectionsAsString);
    } catch (Exception e) {
      throw SQLError.createSQLException(
          Messages.getString("ReplicationConnectionProxy.badValueForAllowSlaveDownConnections", new Object[] { allowSlaveDownConnectionsAsString }), "S1009", null);
    } 
    String readFromMasterWhenNoSlavesAsString = props.getProperty(PropertyKey.readFromMasterWhenNoSlaves.getKeyName());
    try {
      this.readFromMasterWhenNoSlavesOriginal = Boolean.parseBoolean(readFromMasterWhenNoSlavesAsString);
    } catch (Exception e) {
      throw SQLError.createSQLException(
          Messages.getString("ReplicationConnectionProxy.badValueForReadFromMasterWhenNoSlaves", new Object[] { readFromMasterWhenNoSlavesAsString }), "S1009", null);
    } 
    String group = props.getProperty(PropertyKey.replicationConnectionGroup.getKeyName(), null);
    if (!StringUtils.isNullOrEmpty(group) && ReplicationConnectionUrl.class.isAssignableFrom(connectionUrl.getClass())) {
      this.connectionGroup = ReplicationConnectionGroupManager.getConnectionGroupInstance(group);
      if (this.enableJMX)
        ReplicationConnectionGroupManager.registerJmx(); 
      this.connectionGroupID = this.connectionGroup.registerReplicationConnection(this.thisAsReplicationConnection, ((ReplicationConnectionUrl)connectionUrl)
          .getMastersListAsHostPortPairs(), ((ReplicationConnectionUrl)connectionUrl)
          .getSlavesListAsHostPortPairs());
      this.masterHosts = ((ReplicationConnectionUrl)connectionUrl).getMasterHostsListFromHostPortPairs(this.connectionGroup.getMasterHosts());
      this.slaveHosts = ((ReplicationConnectionUrl)connectionUrl).getSlaveHostsListFromHostPortPairs(this.connectionGroup.getSlaveHosts());
    } else {
      this.masterHosts = new ArrayList<>(connectionUrl.getHostsList(HostsListView.MASTERS));
      this.slaveHosts = new ArrayList<>(connectionUrl.getHostsList(HostsListView.SLAVES));
    } 
    resetReadFromMasterWhenNoSlaves();
    try {
      initializeSlavesConnection();
    } catch (SQLException e) {
      if (!this.allowSlaveDownConnections) {
        if (this.connectionGroup != null)
          this.connectionGroup.handleCloseConnection(this.thisAsReplicationConnection); 
        throw e;
      } 
    } 
    SQLException exCaught = null;
    try {
      this.currentConnection = initializeMasterConnection();
    } catch (SQLException e) {
      exCaught = e;
    } 
    if (this.currentConnection == null)
      if (this.allowMasterDownConnections && this.slavesConnection != null) {
        this.readOnly = true;
        this.currentConnection = this.slavesConnection;
      } else {
        if (this.connectionGroup != null)
          this.connectionGroup.handleCloseConnection(this.thisAsReplicationConnection); 
        if (exCaught != null)
          throw exCaught; 
        throw SQLError.createSQLException(Messages.getString("ReplicationConnectionProxy.initializationWithEmptyHostsLists"), "S1009", null);
      }  
  }
  
  JdbcConnection getNewWrapperForThisAsConnection() throws SQLException {
    return new ReplicationMySQLConnection(this);
  }
  
  protected void propagateProxyDown(JdbcConnection proxyConn) {
    if (this.masterConnection != null)
      this.masterConnection.setProxy(proxyConn); 
    if (this.slavesConnection != null)
      this.slavesConnection.setProxy(proxyConn); 
  }
  
  boolean shouldExceptionTriggerConnectionSwitch(Throwable t) {
    return false;
  }
  
  public boolean isMasterConnection() {
    return (this.currentConnection != null && this.currentConnection == this.masterConnection);
  }
  
  public boolean isSlavesConnection() {
    return (this.currentConnection != null && this.currentConnection == this.slavesConnection);
  }
  
  void pickNewConnection() throws SQLException {}
  
  void syncSessionState(JdbcConnection source, JdbcConnection target, boolean readonly) throws SQLException {
    try {
      super.syncSessionState(source, target, readonly);
    } catch (SQLException e1) {
      try {
        super.syncSessionState(source, target, readonly);
      } catch (SQLException sQLException) {}
    } 
  }
  
  void doClose() throws SQLException {
    if (this.masterConnection != null)
      this.masterConnection.close(); 
    if (this.slavesConnection != null)
      this.slavesConnection.close(); 
    if (this.connectionGroup != null)
      this.connectionGroup.handleCloseConnection(this.thisAsReplicationConnection); 
  }
  
  void doAbortInternal() throws SQLException {
    this.masterConnection.abortInternal();
    this.slavesConnection.abortInternal();
    if (this.connectionGroup != null)
      this.connectionGroup.handleCloseConnection(this.thisAsReplicationConnection); 
  }
  
  void doAbort(Executor executor) throws SQLException {
    this.masterConnection.abort(executor);
    this.slavesConnection.abort(executor);
    if (this.connectionGroup != null)
      this.connectionGroup.handleCloseConnection(this.thisAsReplicationConnection); 
  }
  
  Object invokeMore(Object proxy, Method method, Object[] args) throws Throwable {
    checkConnectionCapabilityForMethod(method);
    boolean invokeAgain = false;
    while (true) {
      try {
        Object result = method.invoke(this.thisAsConnection, args);
        if (result != null && result instanceof JdbcStatement)
          ((JdbcStatement)result).setPingTarget(this); 
        return result;
      } catch (InvocationTargetException e) {
        if (invokeAgain) {
          invokeAgain = false;
        } else if (e.getCause() != null && e.getCause() instanceof SQLException && ((SQLException)e
          .getCause()).getSQLState() == "25000" && ((SQLException)e
          .getCause()).getErrorCode() == 1000001) {
          try {
            setReadOnly(this.readOnly);
            invokeAgain = true;
          } catch (SQLException sQLException) {}
        } 
        if (!invokeAgain)
          throw e; 
      } 
    } 
  }
  
  private void checkConnectionCapabilityForMethod(Method method) throws Throwable {
    if (this.masterHosts.isEmpty() && this.slaveHosts.isEmpty() && !ReplicationConnection.class.isAssignableFrom(method.getDeclaringClass()))
      throw SQLError.createSQLException(Messages.getString("ReplicationConnectionProxy.noHostsInconsistentState"), "25000", 1000002, true, null); 
  }
  
  public void doPing() throws SQLException {
    boolean isMasterConn = isMasterConnection();
    SQLException mastersPingException = null;
    SQLException slavesPingException = null;
    if (this.masterConnection != null) {
      try {
        this.masterConnection.ping();
      } catch (SQLException e) {
        mastersPingException = e;
      } 
    } else {
      initializeMasterConnection();
    } 
    if (this.slavesConnection != null) {
      try {
        this.slavesConnection.ping();
      } catch (SQLException e) {
        slavesPingException = e;
      } 
    } else {
      try {
        initializeSlavesConnection();
        if (switchToSlavesConnectionIfNecessary())
          isMasterConn = false; 
      } catch (SQLException e) {
        if (this.masterConnection == null || !this.readFromMasterWhenNoSlaves)
          throw e; 
      } 
    } 
    if (isMasterConn && mastersPingException != null) {
      if (this.slavesConnection != null && slavesPingException == null) {
        this.masterConnection = null;
        this.currentConnection = this.slavesConnection;
        this.readOnly = true;
      } 
      throw mastersPingException;
    } 
    if (!isMasterConn && (slavesPingException != null || this.slavesConnection == null)) {
      if (this.masterConnection != null && this.readFromMasterWhenNoSlaves && mastersPingException == null) {
        this.slavesConnection = null;
        this.currentConnection = this.masterConnection;
        this.readOnly = true;
        this.currentConnection.setReadOnly(true);
      } 
      if (slavesPingException != null)
        throw slavesPingException; 
    } 
  }
  
  private JdbcConnection initializeMasterConnection() throws SQLException {
    this.masterConnection = null;
    if (this.masterHosts.size() == 0)
      return null; 
    LoadBalancedConnection newMasterConn = LoadBalancedConnectionProxy.createProxyInstance((ConnectionUrl)new LoadBalanceConnectionUrl(this.masterHosts, this.connectionUrl.getOriginalProperties()));
    newMasterConn.setProxy(getProxy());
    this.masterConnection = newMasterConn;
    return this.masterConnection;
  }
  
  private JdbcConnection initializeSlavesConnection() throws SQLException {
    this.slavesConnection = null;
    if (this.slaveHosts.size() == 0)
      return null; 
    LoadBalancedConnection newSlavesConn = LoadBalancedConnectionProxy.createProxyInstance((ConnectionUrl)new LoadBalanceConnectionUrl(this.slaveHosts, this.connectionUrl.getOriginalProperties()));
    newSlavesConn.setProxy(getProxy());
    newSlavesConn.setReadOnly(true);
    this.slavesConnection = newSlavesConn;
    return this.slavesConnection;
  }
  
  private synchronized boolean switchToMasterConnection() throws SQLException {
    if (this.masterConnection == null || this.masterConnection.isClosed())
      try {
        if (initializeMasterConnection() == null)
          return false; 
      } catch (SQLException e) {
        this.currentConnection = null;
        throw e;
      }  
    if (!isMasterConnection() && this.masterConnection != null) {
      syncSessionState(this.currentConnection, this.masterConnection, false);
      this.currentConnection = this.masterConnection;
    } 
    return true;
  }
  
  private synchronized boolean switchToSlavesConnection() throws SQLException {
    if (this.slavesConnection == null || this.slavesConnection.isClosed())
      try {
        if (initializeSlavesConnection() == null)
          return false; 
      } catch (SQLException e) {
        this.currentConnection = null;
        throw e;
      }  
    if (!isSlavesConnection() && this.slavesConnection != null) {
      syncSessionState(this.currentConnection, this.slavesConnection, true);
      this.currentConnection = this.slavesConnection;
    } 
    return true;
  }
  
  private boolean switchToSlavesConnectionIfNecessary() throws SQLException {
    if (this.currentConnection == null || (isMasterConnection() && (this.readOnly || (this.masterHosts.isEmpty() && this.currentConnection.isClosed()))) || (
      !isMasterConnection() && this.currentConnection.isClosed()))
      return switchToSlavesConnection(); 
    return false;
  }
  
  public synchronized JdbcConnection getCurrentConnection() {
    return (this.currentConnection == null) ? LoadBalancedConnectionProxy.getNullLoadBalancedConnectionInstance() : this.currentConnection;
  }
  
  public long getConnectionGroupId() {
    return this.connectionGroupID;
  }
  
  public synchronized JdbcConnection getMasterConnection() {
    return this.masterConnection;
  }
  
  public synchronized void promoteSlaveToMaster(String hostPortPair) throws SQLException {
    HostInfo host = getSlaveHost(hostPortPair);
    if (host == null)
      return; 
    this.masterHosts.add(host);
    removeSlave(hostPortPair);
    if (this.masterConnection != null)
      this.masterConnection.addHost(hostPortPair); 
    if (!this.readOnly && !isMasterConnection())
      switchToMasterConnection(); 
  }
  
  public synchronized void removeMasterHost(String hostPortPair) throws SQLException {
    removeMasterHost(hostPortPair, true);
  }
  
  public synchronized void removeMasterHost(String hostPortPair, boolean waitUntilNotInUse) throws SQLException {
    removeMasterHost(hostPortPair, waitUntilNotInUse, false);
  }
  
  public synchronized void removeMasterHost(String hostPortPair, boolean waitUntilNotInUse, boolean isNowSlave) throws SQLException {
    HostInfo host = getMasterHost(hostPortPair);
    if (host == null)
      return; 
    if (isNowSlave) {
      this.slaveHosts.add(host);
      resetReadFromMasterWhenNoSlaves();
    } 
    this.masterHosts.remove(host);
    if (this.masterConnection == null || this.masterConnection.isClosed()) {
      this.masterConnection = null;
      return;
    } 
    if (waitUntilNotInUse) {
      this.masterConnection.removeHostWhenNotInUse(hostPortPair);
    } else {
      this.masterConnection.removeHost(hostPortPair);
    } 
    if (this.masterHosts.isEmpty()) {
      this.masterConnection.close();
      this.masterConnection = null;
      switchToSlavesConnectionIfNecessary();
    } 
  }
  
  public boolean isHostMaster(String hostPortPair) {
    if (hostPortPair == null)
      return false; 
    return this.masterHosts.stream().anyMatch(hi -> hostPortPair.equalsIgnoreCase(hi.getHostPortPair()));
  }
  
  public synchronized JdbcConnection getSlavesConnection() {
    return this.slavesConnection;
  }
  
  public synchronized void addSlaveHost(String hostPortPair) throws SQLException {
    if (isHostSlave(hostPortPair))
      return; 
    this.slaveHosts.add(getConnectionUrl().getSlaveHostOrSpawnIsolated(hostPortPair));
    resetReadFromMasterWhenNoSlaves();
    if (this.slavesConnection == null) {
      initializeSlavesConnection();
      switchToSlavesConnectionIfNecessary();
    } else {
      this.slavesConnection.addHost(hostPortPair);
    } 
  }
  
  public synchronized void removeSlave(String hostPortPair) throws SQLException {
    removeSlave(hostPortPair, true);
  }
  
  public synchronized void removeSlave(String hostPortPair, boolean closeGently) throws SQLException {
    HostInfo host = getSlaveHost(hostPortPair);
    if (host == null)
      return; 
    this.slaveHosts.remove(host);
    resetReadFromMasterWhenNoSlaves();
    if (this.slavesConnection == null || this.slavesConnection.isClosed()) {
      this.slavesConnection = null;
      return;
    } 
    if (closeGently) {
      this.slavesConnection.removeHostWhenNotInUse(hostPortPair);
    } else {
      this.slavesConnection.removeHost(hostPortPair);
    } 
    if (this.slaveHosts.isEmpty()) {
      this.slavesConnection.close();
      this.slavesConnection = null;
      switchToMasterConnection();
      if (isMasterConnection())
        this.currentConnection.setReadOnly(this.readOnly); 
    } 
  }
  
  public boolean isHostSlave(String hostPortPair) {
    if (hostPortPair == null)
      return false; 
    return this.slaveHosts.stream().anyMatch(hi -> hostPortPair.equalsIgnoreCase(hi.getHostPortPair()));
  }
  
  public synchronized void setReadOnly(boolean readOnly) throws SQLException {
    if (readOnly) {
      if (!isSlavesConnection() || this.currentConnection.isClosed()) {
        boolean switched = true;
        SQLException exceptionCaught = null;
        try {
          switched = switchToSlavesConnection();
        } catch (SQLException e) {
          switched = false;
          exceptionCaught = e;
        } 
        if (!switched && this.readFromMasterWhenNoSlaves && switchToMasterConnection())
          exceptionCaught = null; 
        if (exceptionCaught != null)
          throw exceptionCaught; 
      } 
    } else if (!isMasterConnection() || this.currentConnection.isClosed()) {
      boolean switched = true;
      SQLException exceptionCaught = null;
      try {
        switched = switchToMasterConnection();
      } catch (SQLException e) {
        switched = false;
        exceptionCaught = e;
      } 
      if (!switched && switchToSlavesConnectionIfNecessary())
        exceptionCaught = null; 
      if (exceptionCaught != null)
        throw exceptionCaught; 
    } 
    this.readOnly = readOnly;
    if (this.readFromMasterWhenNoSlaves && isMasterConnection())
      this.currentConnection.setReadOnly(this.readOnly); 
  }
  
  public boolean isReadOnly() throws SQLException {
    return (!isMasterConnection() || this.readOnly);
  }
  
  private void resetReadFromMasterWhenNoSlaves() {
    this.readFromMasterWhenNoSlaves = (this.slaveHosts.isEmpty() || this.readFromMasterWhenNoSlavesOriginal);
  }
  
  private HostInfo getMasterHost(String hostPortPair) {
    return this.masterHosts.stream().filter(hi -> hostPortPair.equalsIgnoreCase(hi.getHostPortPair())).findFirst().orElse(null);
  }
  
  private HostInfo getSlaveHost(String hostPortPair) {
    return this.slaveHosts.stream().filter(hi -> hostPortPair.equalsIgnoreCase(hi.getHostPortPair())).findFirst().orElse(null);
  }
  
  private ReplicationConnectionUrl getConnectionUrl() {
    return (ReplicationConnectionUrl)this.connectionUrl;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\jdbc\ha\ReplicationConnectionProxy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
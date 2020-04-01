package com.mysql.cj.jdbc;

import com.mysql.cj.MysqlConnection;
import com.mysql.cj.ServerVersion;
import com.mysql.cj.TransactionEventHandler;
import com.mysql.cj.conf.PropertySet;
import com.mysql.cj.interceptors.QueryInterceptor;
import com.mysql.cj.jdbc.result.CachedResultSetMetaData;
import com.mysql.cj.jdbc.result.ResultSetInternalMethods;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public interface JdbcConnection extends Connection, MysqlConnection, TransactionEventHandler {
  JdbcPropertySet getPropertySet();
  
  void changeUser(String paramString1, String paramString2) throws SQLException;
  
  @Deprecated
  void clearHasTriedMaster();
  
  PreparedStatement clientPrepareStatement(String paramString) throws SQLException;
  
  PreparedStatement clientPrepareStatement(String paramString, int paramInt) throws SQLException;
  
  PreparedStatement clientPrepareStatement(String paramString, int paramInt1, int paramInt2) throws SQLException;
  
  PreparedStatement clientPrepareStatement(String paramString, int[] paramArrayOfint) throws SQLException;
  
  PreparedStatement clientPrepareStatement(String paramString, int paramInt1, int paramInt2, int paramInt3) throws SQLException;
  
  PreparedStatement clientPrepareStatement(String paramString, String[] paramArrayOfString) throws SQLException;
  
  int getActiveStatementCount();
  
  long getIdleFor();
  
  String getStatementComment();
  
  @Deprecated
  boolean hasTriedMaster();
  
  boolean isInGlobalTx();
  
  void setInGlobalTx(boolean paramBoolean);
  
  boolean isMasterConnection();
  
  boolean isSameResource(JdbcConnection paramJdbcConnection);
  
  boolean lowerCaseTableNames();
  
  void ping() throws SQLException;
  
  void resetServerState() throws SQLException;
  
  PreparedStatement serverPrepareStatement(String paramString) throws SQLException;
  
  PreparedStatement serverPrepareStatement(String paramString, int paramInt) throws SQLException;
  
  PreparedStatement serverPrepareStatement(String paramString, int paramInt1, int paramInt2) throws SQLException;
  
  PreparedStatement serverPrepareStatement(String paramString, int paramInt1, int paramInt2, int paramInt3) throws SQLException;
  
  PreparedStatement serverPrepareStatement(String paramString, int[] paramArrayOfint) throws SQLException;
  
  PreparedStatement serverPrepareStatement(String paramString, String[] paramArrayOfString) throws SQLException;
  
  void setFailedOver(boolean paramBoolean);
  
  void setStatementComment(String paramString);
  
  void shutdownServer() throws SQLException;
  
  int getAutoIncrementIncrement();
  
  boolean hasSameProperties(JdbcConnection paramJdbcConnection);
  
  String getHost();
  
  String getHostPortPair();
  
  void setProxy(JdbcConnection paramJdbcConnection);
  
  boolean isServerLocal() throws SQLException;
  
  int getSessionMaxRows();
  
  void setSessionMaxRows(int paramInt) throws SQLException;
  
  void abortInternal() throws SQLException;
  
  boolean isProxySet();
  
  CachedResultSetMetaData getCachedMetaData(String paramString);
  
  String getCharacterSetMetadata();
  
  Statement getMetadataSafeStatement() throws SQLException;
  
  ServerVersion getServerVersion();
  
  List<QueryInterceptor> getQueryInterceptorsInstances();
  
  void initializeResultsMetadataFromCache(String paramString, CachedResultSetMetaData paramCachedResultSetMetaData, ResultSetInternalMethods paramResultSetInternalMethods) throws SQLException;
  
  void initializeSafeQueryInterceptors() throws SQLException;
  
  boolean isReadOnly(boolean paramBoolean) throws SQLException;
  
  void pingInternal(boolean paramBoolean, int paramInt) throws SQLException;
  
  void realClose(boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, Throwable paramThrowable) throws SQLException;
  
  void recachePreparedStatement(JdbcPreparedStatement paramJdbcPreparedStatement) throws SQLException;
  
  void decachePreparedStatement(JdbcPreparedStatement paramJdbcPreparedStatement) throws SQLException;
  
  void registerStatement(JdbcStatement paramJdbcStatement);
  
  void setReadOnlyInternal(boolean paramBoolean) throws SQLException;
  
  boolean storesLowerCaseTableName();
  
  void throwConnectionClosedException() throws SQLException;
  
  void unregisterStatement(JdbcStatement paramJdbcStatement);
  
  void unSafeQueryInterceptors() throws SQLException;
  
  JdbcConnection getMultiHostSafeProxy();
  
  JdbcConnection getMultiHostParentProxy();
  
  JdbcConnection getActiveMySQLConnection();
  
  ClientInfoProvider getClientInfoProviderImpl() throws SQLException;
  
  void setDatabase(String paramString) throws SQLException;
  
  String getDatabase() throws SQLException;
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\jdbc\JdbcConnection.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
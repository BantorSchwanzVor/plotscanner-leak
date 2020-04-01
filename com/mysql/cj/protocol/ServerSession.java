package com.mysql.cj.protocol;

import com.mysql.cj.ServerVersion;
import java.util.Map;
import java.util.TimeZone;

public interface ServerSession {
  public static final int TRANSACTION_NOT_STARTED = 0;
  
  public static final int TRANSACTION_IN_PROGRESS = 1;
  
  public static final int TRANSACTION_STARTED = 2;
  
  public static final int TRANSACTION_COMPLETED = 3;
  
  public static final String LOCAL_CHARACTER_SET_RESULTS = "local.character_set_results";
  
  ServerCapabilities getCapabilities();
  
  void setCapabilities(ServerCapabilities paramServerCapabilities);
  
  int getStatusFlags();
  
  void setStatusFlags(int paramInt);
  
  void setStatusFlags(int paramInt, boolean paramBoolean);
  
  int getOldStatusFlags();
  
  void setOldStatusFlags(int paramInt);
  
  int getServerDefaultCollationIndex();
  
  void setServerDefaultCollationIndex(int paramInt);
  
  int getTransactionState();
  
  boolean inTransactionOnServer();
  
  boolean cursorExists();
  
  boolean isAutocommit();
  
  boolean hasMoreResults();
  
  boolean isLastRowSent();
  
  boolean noGoodIndexUsed();
  
  boolean noIndexUsed();
  
  boolean queryWasSlow();
  
  long getClientParam();
  
  void setClientParam(long paramLong);
  
  boolean useMultiResults();
  
  boolean isEOFDeprecated();
  
  boolean hasLongColumnInfo();
  
  void setHasLongColumnInfo(boolean paramBoolean);
  
  Map<String, String> getServerVariables();
  
  String getServerVariable(String paramString);
  
  int getServerVariable(String paramString, int paramInt);
  
  void setServerVariables(Map<String, String> paramMap);
  
  boolean characterSetNamesMatches(String paramString);
  
  ServerVersion getServerVersion();
  
  boolean isVersion(ServerVersion paramServerVersion);
  
  String getServerDefaultCharset();
  
  String getErrorMessageEncoding();
  
  void setErrorMessageEncoding(String paramString);
  
  int getMaxBytesPerChar(String paramString);
  
  int getMaxBytesPerChar(Integer paramInteger, String paramString);
  
  String getEncodingForIndex(int paramInt);
  
  void configureCharacterSets();
  
  String getCharacterSetMetadata();
  
  void setCharacterSetMetadata(String paramString);
  
  int getMetadataCollationIndex();
  
  void setMetadataCollationIndex(int paramInt);
  
  String getCharacterSetResultsOnServer();
  
  void setCharacterSetResultsOnServer(String paramString);
  
  boolean isLowerCaseTableNames();
  
  boolean storesLowerCaseTableNames();
  
  boolean isQueryCacheEnabled();
  
  boolean isNoBackslashEscapesSet();
  
  boolean useAnsiQuotedIdentifiers();
  
  boolean isServerTruncatesFracSecs();
  
  long getThreadId();
  
  void setThreadId(long paramLong);
  
  boolean isAutoCommit();
  
  void setAutoCommit(boolean paramBoolean);
  
  TimeZone getServerTimeZone();
  
  void setServerTimeZone(TimeZone paramTimeZone);
  
  TimeZone getDefaultTimeZone();
  
  void setDefaultTimeZone(TimeZone paramTimeZone);
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\protocol\ServerSession.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
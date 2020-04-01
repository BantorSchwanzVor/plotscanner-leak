package com.mysql.cj.protocol.a;

import com.mysql.cj.CharsetMapping;
import com.mysql.cj.Messages;
import com.mysql.cj.ServerVersion;
import com.mysql.cj.conf.PropertyKey;
import com.mysql.cj.conf.PropertySet;
import com.mysql.cj.exceptions.ExceptionFactory;
import com.mysql.cj.exceptions.WrongArgumentException;
import com.mysql.cj.protocol.ServerCapabilities;
import com.mysql.cj.protocol.ServerSession;
import com.mysql.cj.util.StringUtils;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class NativeServerSession implements ServerSession {
  public static final int SERVER_STATUS_IN_TRANS = 1;
  
  public static final int SERVER_STATUS_AUTOCOMMIT = 2;
  
  public static final int SERVER_MORE_RESULTS_EXISTS = 8;
  
  public static final int SERVER_QUERY_NO_GOOD_INDEX_USED = 16;
  
  public static final int SERVER_QUERY_NO_INDEX_USED = 32;
  
  public static final int SERVER_STATUS_CURSOR_EXISTS = 64;
  
  public static final int SERVER_STATUS_LAST_ROW_SENT = 128;
  
  public static final int SERVER_QUERY_WAS_SLOW = 2048;
  
  public static final int CLIENT_LONG_PASSWORD = 1;
  
  public static final int CLIENT_FOUND_ROWS = 2;
  
  public static final int CLIENT_LONG_FLAG = 4;
  
  public static final int CLIENT_CONNECT_WITH_DB = 8;
  
  public static final int CLIENT_COMPRESS = 32;
  
  public static final int CLIENT_LOCAL_FILES = 128;
  
  public static final int CLIENT_PROTOCOL_41 = 512;
  
  public static final int CLIENT_INTERACTIVE = 1024;
  
  public static final int CLIENT_SSL = 2048;
  
  public static final int CLIENT_TRANSACTIONS = 8192;
  
  public static final int CLIENT_RESERVED = 16384;
  
  public static final int CLIENT_SECURE_CONNECTION = 32768;
  
  public static final int CLIENT_MULTI_STATEMENTS = 65536;
  
  public static final int CLIENT_MULTI_RESULTS = 131072;
  
  public static final int CLIENT_PS_MULTI_RESULTS = 262144;
  
  public static final int CLIENT_PLUGIN_AUTH = 524288;
  
  public static final int CLIENT_CONNECT_ATTRS = 1048576;
  
  public static final int CLIENT_PLUGIN_AUTH_LENENC_CLIENT_DATA = 2097152;
  
  public static final int CLIENT_CAN_HANDLE_EXPIRED_PASSWORD = 4194304;
  
  public static final int CLIENT_SESSION_TRACK = 8388608;
  
  public static final int CLIENT_DEPRECATE_EOF = 16777216;
  
  private PropertySet propertySet;
  
  private NativeCapabilities capabilities;
  
  private int oldStatusFlags = 0;
  
  private int statusFlags = 0;
  
  private int serverDefaultCollationIndex;
  
  private long clientParam = 0L;
  
  private boolean hasLongColumnInfo = false;
  
  private Map<String, String> serverVariables = new HashMap<>();
  
  public Map<Integer, String> indexToCustomMysqlCharset = null;
  
  public Map<String, Integer> mysqlCharsetToCustomMblen = null;
  
  private String characterSetMetadata = null;
  
  private int metadataCollationIndex;
  
  private String characterSetResultsOnServer = null;
  
  private String errorMessageEncoding = "Cp1252";
  
  private boolean autoCommit = true;
  
  private TimeZone serverTimeZone = null;
  
  private TimeZone defaultTimeZone = TimeZone.getDefault();
  
  public NativeServerSession(PropertySet propertySet) {
    this.propertySet = propertySet;
    this.serverVariables.put("character_set_server", "utf8");
  }
  
  public NativeCapabilities getCapabilities() {
    return this.capabilities;
  }
  
  public void setCapabilities(ServerCapabilities capabilities) {
    this.capabilities = (NativeCapabilities)capabilities;
  }
  
  public int getStatusFlags() {
    return this.statusFlags;
  }
  
  public void setStatusFlags(int statusFlags) {
    setStatusFlags(statusFlags, false);
  }
  
  public void setStatusFlags(int statusFlags, boolean saveOldStatus) {
    if (saveOldStatus)
      this.oldStatusFlags = this.statusFlags; 
    this.statusFlags = statusFlags;
  }
  
  public int getOldStatusFlags() {
    return this.oldStatusFlags;
  }
  
  public void setOldStatusFlags(int oldStatusFlags) {
    this.oldStatusFlags = oldStatusFlags;
  }
  
  public int getTransactionState() {
    if ((this.oldStatusFlags & 0x1) == 0) {
      if ((this.statusFlags & 0x1) == 0)
        return 0; 
      return 2;
    } 
    if ((this.statusFlags & 0x1) == 0)
      return 3; 
    return 1;
  }
  
  public boolean inTransactionOnServer() {
    return ((this.statusFlags & 0x1) != 0);
  }
  
  public boolean cursorExists() {
    return ((this.statusFlags & 0x40) != 0);
  }
  
  public boolean isAutocommit() {
    return ((this.statusFlags & 0x2) != 0);
  }
  
  public boolean hasMoreResults() {
    return ((this.statusFlags & 0x8) != 0);
  }
  
  public boolean noGoodIndexUsed() {
    return ((this.statusFlags & 0x10) != 0);
  }
  
  public boolean noIndexUsed() {
    return ((this.statusFlags & 0x20) != 0);
  }
  
  public boolean queryWasSlow() {
    return ((this.statusFlags & 0x800) != 0);
  }
  
  public boolean isLastRowSent() {
    return ((this.statusFlags & 0x80) != 0);
  }
  
  public long getClientParam() {
    return this.clientParam;
  }
  
  public void setClientParam(long clientParam) {
    this.clientParam = clientParam;
  }
  
  public boolean useMultiResults() {
    return ((this.clientParam & 0x20000L) != 0L || (this.clientParam & 0x40000L) != 0L);
  }
  
  public boolean isEOFDeprecated() {
    return ((this.clientParam & 0x1000000L) != 0L);
  }
  
  public int getServerDefaultCollationIndex() {
    return this.serverDefaultCollationIndex;
  }
  
  public void setServerDefaultCollationIndex(int serverDefaultCollationIndex) {
    this.serverDefaultCollationIndex = serverDefaultCollationIndex;
  }
  
  public boolean hasLongColumnInfo() {
    return this.hasLongColumnInfo;
  }
  
  public void setHasLongColumnInfo(boolean hasLongColumnInfo) {
    this.hasLongColumnInfo = hasLongColumnInfo;
  }
  
  public Map<String, String> getServerVariables() {
    return this.serverVariables;
  }
  
  public String getServerVariable(String name) {
    return this.serverVariables.get(name);
  }
  
  public int getServerVariable(String variableName, int fallbackValue) {
    try {
      return Integer.valueOf(getServerVariable(variableName)).intValue();
    } catch (NumberFormatException numberFormatException) {
      return fallbackValue;
    } 
  }
  
  public void setServerVariables(Map<String, String> serverVariables) {
    this.serverVariables = serverVariables;
  }
  
  public boolean characterSetNamesMatches(String mysqlEncodingName) {
    return (mysqlEncodingName != null && mysqlEncodingName.equalsIgnoreCase(getServerVariable("character_set_client")) && mysqlEncodingName
      .equalsIgnoreCase(getServerVariable("character_set_connection")));
  }
  
  public final ServerVersion getServerVersion() {
    return this.capabilities.getServerVersion();
  }
  
  public boolean isVersion(ServerVersion version) {
    return getServerVersion().equals(version);
  }
  
  public boolean isSetNeededForAutoCommitMode(boolean autoCommitFlag, boolean elideSetAutoCommitsFlag) {
    if (elideSetAutoCommitsFlag) {
      boolean autoCommitModeOnServer = isAutocommit();
      if (autoCommitModeOnServer && !autoCommitFlag)
        return !inTransactionOnServer(); 
      return (autoCommitModeOnServer != autoCommitFlag);
    } 
    return true;
  }
  
  public String getErrorMessageEncoding() {
    return this.errorMessageEncoding;
  }
  
  public void setErrorMessageEncoding(String errorMessageEncoding) {
    this.errorMessageEncoding = errorMessageEncoding;
  }
  
  public String getServerDefaultCharset() {
    String charset = null;
    if (this.indexToCustomMysqlCharset != null)
      charset = this.indexToCustomMysqlCharset.get(Integer.valueOf(getServerDefaultCollationIndex())); 
    if (charset == null)
      charset = CharsetMapping.getMysqlCharsetNameForCollationIndex(Integer.valueOf(getServerDefaultCollationIndex())); 
    return (charset != null) ? charset : getServerVariable("character_set_server");
  }
  
  public int getMaxBytesPerChar(String javaCharsetName) {
    return getMaxBytesPerChar(null, javaCharsetName);
  }
  
  public int getMaxBytesPerChar(Integer charsetIndex, String javaCharsetName) {
    String charset = null;
    int res = 1;
    if (this.indexToCustomMysqlCharset != null)
      charset = this.indexToCustomMysqlCharset.get(charsetIndex); 
    if (charset == null)
      charset = CharsetMapping.getMysqlCharsetNameForCollationIndex(charsetIndex); 
    if (charset == null)
      charset = CharsetMapping.getMysqlCharsetForJavaEncoding(javaCharsetName, getServerVersion()); 
    Integer mblen = null;
    if (this.mysqlCharsetToCustomMblen != null)
      mblen = this.mysqlCharsetToCustomMblen.get(charset); 
    if (mblen == null)
      mblen = Integer.valueOf(CharsetMapping.getMblen(charset)); 
    if (mblen != null)
      res = mblen.intValue(); 
    return res;
  }
  
  public String getEncodingForIndex(int charsetIndex) {
    String javaEncoding = null;
    String characterEncoding = (String)this.propertySet.getStringProperty(PropertyKey.characterEncoding).getValue();
    if (charsetIndex != -1) {
      try {
        if (this.indexToCustomMysqlCharset != null) {
          String cs = this.indexToCustomMysqlCharset.get(Integer.valueOf(charsetIndex));
          if (cs != null)
            javaEncoding = CharsetMapping.getJavaEncodingForMysqlCharset(cs, characterEncoding); 
        } 
        if (javaEncoding == null)
          javaEncoding = CharsetMapping.getJavaEncodingForCollationIndex(Integer.valueOf(charsetIndex), characterEncoding); 
      } catch (ArrayIndexOutOfBoundsException outOfBoundsEx) {
        throw (WrongArgumentException)ExceptionFactory.createException(WrongArgumentException.class, Messages.getString("Connection.11", new Object[] { Integer.valueOf(charsetIndex) }));
      } 
      if (javaEncoding == null)
        javaEncoding = characterEncoding; 
    } else {
      javaEncoding = characterEncoding;
    } 
    return javaEncoding;
  }
  
  public void configureCharacterSets() {
    String characterSetResultsOnServerMysql = getServerVariable("local.character_set_results");
    if (characterSetResultsOnServerMysql == null || StringUtils.startsWithIgnoreCaseAndWs(characterSetResultsOnServerMysql, "NULL") || characterSetResultsOnServerMysql
      .length() == 0) {
      String defaultMetadataCharsetMysql = getServerVariable("character_set_system");
      String defaultMetadataCharset = null;
      if (defaultMetadataCharsetMysql != null) {
        defaultMetadataCharset = CharsetMapping.getJavaEncodingForMysqlCharset(defaultMetadataCharsetMysql);
      } else {
        defaultMetadataCharset = "UTF-8";
      } 
      this.characterSetMetadata = defaultMetadataCharset;
      setErrorMessageEncoding("UTF-8");
    } else {
      this.characterSetResultsOnServer = CharsetMapping.getJavaEncodingForMysqlCharset(characterSetResultsOnServerMysql);
      this.characterSetMetadata = this.characterSetResultsOnServer;
      setErrorMessageEncoding(this.characterSetResultsOnServer);
    } 
    this.metadataCollationIndex = CharsetMapping.getCollationIndexForJavaEncoding(this.characterSetMetadata, getServerVersion());
  }
  
  public String getCharacterSetMetadata() {
    return this.characterSetMetadata;
  }
  
  public void setCharacterSetMetadata(String characterSetMetadata) {
    this.characterSetMetadata = characterSetMetadata;
  }
  
  public int getMetadataCollationIndex() {
    return this.metadataCollationIndex;
  }
  
  public void setMetadataCollationIndex(int metadataCollationIndex) {
    this.metadataCollationIndex = metadataCollationIndex;
  }
  
  public String getCharacterSetResultsOnServer() {
    return this.characterSetResultsOnServer;
  }
  
  public void setCharacterSetResultsOnServer(String characterSetResultsOnServer) {
    this.characterSetResultsOnServer = characterSetResultsOnServer;
  }
  
  public void preserveOldTransactionState() {
    this.statusFlags |= this.oldStatusFlags & 0x1;
  }
  
  public boolean isLowerCaseTableNames() {
    String lowerCaseTables = this.serverVariables.get("lower_case_table_names");
    return ("on".equalsIgnoreCase(lowerCaseTables) || "1".equalsIgnoreCase(lowerCaseTables) || "2".equalsIgnoreCase(lowerCaseTables));
  }
  
  public boolean storesLowerCaseTableNames() {
    String lowerCaseTables = this.serverVariables.get("lower_case_table_names");
    return ("1".equalsIgnoreCase(lowerCaseTables) || "on".equalsIgnoreCase(lowerCaseTables));
  }
  
  public boolean isQueryCacheEnabled() {
    return ("ON".equalsIgnoreCase(this.serverVariables.get("query_cache_type")) && !"0".equalsIgnoreCase(this.serverVariables.get("query_cache_size")));
  }
  
  public boolean isNoBackslashEscapesSet() {
    String sqlModeAsString = this.serverVariables.get("sql_mode");
    return (sqlModeAsString != null && sqlModeAsString.indexOf("NO_BACKSLASH_ESCAPES") != -1);
  }
  
  public boolean useAnsiQuotedIdentifiers() {
    String sqlModeAsString = this.serverVariables.get("sql_mode");
    return (sqlModeAsString != null && sqlModeAsString.indexOf("ANSI_QUOTES") != -1);
  }
  
  public boolean isServerTruncatesFracSecs() {
    String sqlModeAsString = this.serverVariables.get("sql_mode");
    return (sqlModeAsString != null && sqlModeAsString.indexOf("TIME_TRUNCATE_FRACTIONAL") != -1);
  }
  
  public long getThreadId() {
    return this.capabilities.getThreadId();
  }
  
  public void setThreadId(long threadId) {
    this.capabilities.setThreadId(threadId);
  }
  
  public boolean isAutoCommit() {
    return this.autoCommit;
  }
  
  public void setAutoCommit(boolean autoCommit) {
    this.autoCommit = autoCommit;
  }
  
  public TimeZone getServerTimeZone() {
    return this.serverTimeZone;
  }
  
  public void setServerTimeZone(TimeZone serverTimeZone) {
    this.serverTimeZone = serverTimeZone;
  }
  
  public TimeZone getDefaultTimeZone() {
    return this.defaultTimeZone;
  }
  
  public void setDefaultTimeZone(TimeZone defaultTimeZone) {
    this.defaultTimeZone = defaultTimeZone;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\protocol\a\NativeServerSession.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
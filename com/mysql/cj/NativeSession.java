package com.mysql.cj;

import com.mysql.cj.conf.HostInfo;
import com.mysql.cj.conf.PropertyKey;
import com.mysql.cj.conf.PropertySet;
import com.mysql.cj.conf.RuntimeProperty;
import com.mysql.cj.exceptions.CJCommunicationsException;
import com.mysql.cj.exceptions.CJException;
import com.mysql.cj.exceptions.ConnectionIsClosedException;
import com.mysql.cj.exceptions.ExceptionFactory;
import com.mysql.cj.exceptions.ExceptionInterceptor;
import com.mysql.cj.exceptions.ExceptionInterceptorChain;
import com.mysql.cj.exceptions.OperationCancelledException;
import com.mysql.cj.exceptions.WrongArgumentException;
import com.mysql.cj.interceptors.QueryInterceptor;
import com.mysql.cj.log.Log;
import com.mysql.cj.protocol.ColumnDefinition;
import com.mysql.cj.protocol.Message;
import com.mysql.cj.protocol.NetworkResources;
import com.mysql.cj.protocol.Protocol;
import com.mysql.cj.protocol.ProtocolEntityFactory;
import com.mysql.cj.protocol.Resultset;
import com.mysql.cj.protocol.SocketConnection;
import com.mysql.cj.protocol.SocketFactory;
import com.mysql.cj.protocol.a.NativeMessageBuilder;
import com.mysql.cj.protocol.a.NativePacketPayload;
import com.mysql.cj.protocol.a.NativeProtocol;
import com.mysql.cj.protocol.a.NativeServerSession;
import com.mysql.cj.protocol.a.NativeSocketConnection;
import com.mysql.cj.protocol.a.ResultsetFactory;
import com.mysql.cj.result.Field;
import com.mysql.cj.result.IntegerValueFactory;
import com.mysql.cj.result.LongValueFactory;
import com.mysql.cj.result.Row;
import com.mysql.cj.result.StringValueFactory;
import com.mysql.cj.result.ValueFactory;
import com.mysql.cj.util.StringUtils;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.UnsupportedCharsetException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Timer;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Supplier;

public class NativeSession extends CoreSession implements Serializable {
  private static final long serialVersionUID = 5323638898749073419L;
  
  private CacheAdapter<String, Map<String, String>> serverConfigCache;
  
  private static final Map<String, Map<Integer, String>> customIndexToCharsetMapByUrl = new HashMap<>();
  
  private static final Map<String, Map<String, Integer>> customCharsetToMblenMapByUrl = new HashMap<>();
  
  private boolean requiresEscapingEncoder;
  
  private long lastQueryFinishedTime = 0L;
  
  private boolean needsPing = false;
  
  private NativeMessageBuilder commandBuilder = new NativeMessageBuilder();
  
  private boolean isClosed = true;
  
  private Throwable forceClosedReason;
  
  private CopyOnWriteArrayList<WeakReference<Session.SessionEventListener>> listeners = new CopyOnWriteArrayList<>();
  
  private transient Timer cancelTimer;
  
  private static final String SERVER_VERSION_STRING_VAR_NAME = "server_version_string";
  
  public NativeSession(HostInfo hostInfo, PropertySet propSet) {
    super(hostInfo, propSet);
  }
  
  public void connect(HostInfo hi, String user, String password, String database, int loginTimeout, TransactionEventHandler transactionManager) throws IOException {
    this.hostInfo = hi;
    setSessionMaxRows(-1);
    NativeSocketConnection nativeSocketConnection = new NativeSocketConnection();
    nativeSocketConnection.connect(this.hostInfo.getHost(), this.hostInfo.getPort(), this.propertySet, getExceptionInterceptor(), this.log, loginTimeout);
    if (this.protocol == null) {
      this.protocol = (Protocol<? extends Message>)NativeProtocol.getInstance(this, (SocketConnection)nativeSocketConnection, this.propertySet, this.log, transactionManager);
    } else {
      this.protocol.init(this, (SocketConnection)nativeSocketConnection, this.propertySet, transactionManager);
    } 
    this.protocol.connect(user, password, database);
    this.protocol.getServerSession().setErrorMessageEncoding(this.protocol.getAuthenticationProvider().getEncodingForHandshake());
    this.isClosed = false;
  }
  
  public NativeProtocol getProtocol() {
    return (NativeProtocol)this.protocol;
  }
  
  public void quit() {
    if (this.protocol != null)
      try {
        ((NativeProtocol)this.protocol).quit();
      } catch (Exception exception) {} 
    synchronized (this) {
      if (this.cancelTimer != null) {
        this.cancelTimer.cancel();
        this.cancelTimer = null;
      } 
    } 
    this.isClosed = true;
    super.quit();
  }
  
  public void forceClose() {
    if (this.protocol != null)
      try {
        this.protocol.getSocketConnection().forceClose();
        ((NativeProtocol)this.protocol).releaseResources();
      } catch (Throwable throwable) {} 
    synchronized (this) {
      if (this.cancelTimer != null) {
        this.cancelTimer.cancel();
        this.cancelTimer = null;
      } 
    } 
    this.isClosed = true;
    super.forceClose();
  }
  
  public void enableMultiQueries() {
    sendCommand(this.commandBuilder.buildComSetOption(((NativeProtocol)this.protocol).getSharedSendPacket(), 0), false, 0);
    ((NativeServerSession)getServerSession()).preserveOldTransactionState();
  }
  
  public void disableMultiQueries() {
    sendCommand(this.commandBuilder.buildComSetOption(((NativeProtocol)this.protocol).getSharedSendPacket(), 1), false, 0);
    ((NativeServerSession)getServerSession()).preserveOldTransactionState();
  }
  
  public boolean isSetNeededForAutoCommitMode(boolean autoCommitFlag) {
    return ((NativeServerSession)this.protocol.getServerSession()).isSetNeededForAutoCommitMode(autoCommitFlag, false);
  }
  
  public int getSessionMaxRows() {
    return this.sessionMaxRows;
  }
  
  public void setSessionMaxRows(int sessionMaxRows) {
    this.sessionMaxRows = sessionMaxRows;
  }
  
  public void setQueryInterceptors(List<QueryInterceptor> queryInterceptors) {
    ((NativeProtocol)this.protocol).setQueryInterceptors(queryInterceptors);
  }
  
  public boolean isServerLocal(Session sess) {
    SocketFactory factory = this.protocol.getSocketConnection().getSocketFactory();
    return factory.isLocallyConnected(sess);
  }
  
  public void shutdownServer() {
    if (versionMeetsMinimum(5, 7, 9)) {
      sendCommand(this.commandBuilder.buildComQuery(getSharedSendPacket(), "SHUTDOWN"), false, 0);
    } else {
      sendCommand(this.commandBuilder.buildComShutdown(getSharedSendPacket()), false, 0);
    } 
  }
  
  public void setSocketTimeout(int milliseconds) {
    getPropertySet().getProperty(PropertyKey.socketTimeout).setValue(Integer.valueOf(milliseconds));
    ((NativeProtocol)this.protocol).setSocketTimeout(milliseconds);
  }
  
  public int getSocketTimeout() {
    RuntimeProperty<Integer> sto = getPropertySet().getProperty(PropertyKey.socketTimeout);
    return ((Integer)sto.getValue()).intValue();
  }
  
  public void checkForCharsetMismatch() {
    ((NativeProtocol)this.protocol).checkForCharsetMismatch();
  }
  
  public NativePacketPayload getSharedSendPacket() {
    return ((NativeProtocol)this.protocol).getSharedSendPacket();
  }
  
  public void dumpPacketRingBuffer() {
    ((NativeProtocol)this.protocol).dumpPacketRingBuffer();
  }
  
  public <T extends Resultset> T invokeQueryInterceptorsPre(Supplier<String> sql, Query interceptedQuery, boolean forceExecute) {
    return (T)((NativeProtocol)this.protocol).invokeQueryInterceptorsPre(sql, interceptedQuery, forceExecute);
  }
  
  public <T extends Resultset> T invokeQueryInterceptorsPost(Supplier<String> sql, Query interceptedQuery, T originalResultSet, boolean forceExecute) {
    return (T)((NativeProtocol)this.protocol).invokeQueryInterceptorsPost(sql, interceptedQuery, (Resultset)originalResultSet, forceExecute);
  }
  
  public boolean shouldIntercept() {
    return (((NativeProtocol)this.protocol).getQueryInterceptors() != null);
  }
  
  public long getCurrentTimeNanosOrMillis() {
    return ((NativeProtocol)this.protocol).getCurrentTimeNanosOrMillis();
  }
  
  public final NativePacketPayload sendCommand(NativePacketPayload queryPacket, boolean skipCheck, int timeoutMillis) {
    return (NativePacketPayload)this.protocol.sendCommand((Message)queryPacket, skipCheck, timeoutMillis);
  }
  
  public long getSlowQueryThreshold() {
    return ((NativeProtocol)this.protocol).getSlowQueryThreshold();
  }
  
  public boolean hadWarnings() {
    return ((NativeProtocol)this.protocol).hadWarnings();
  }
  
  public void clearInputStream() {
    ((NativeProtocol)this.protocol).clearInputStream();
  }
  
  public NetworkResources getNetworkResources() {
    return this.protocol.getSocketConnection().getNetworkResources();
  }
  
  public boolean isSSLEstablished() {
    return this.protocol.getSocketConnection().isSSLEstablished();
  }
  
  public int getCommandCount() {
    return ((NativeProtocol)this.protocol).getCommandCount();
  }
  
  public SocketAddress getRemoteSocketAddress() {
    try {
      return this.protocol.getSocketConnection().getMysqlSocket().getRemoteSocketAddress();
    } catch (IOException e) {
      throw new CJCommunicationsException(e);
    } 
  }
  
  public InputStream getLocalInfileInputStream() {
    return this.protocol.getLocalInfileInputStream();
  }
  
  public void setLocalInfileInputStream(InputStream stream) {
    this.protocol.setLocalInfileInputStream(stream);
  }
  
  private void configureCharsetProperties() {
    if (this.characterEncoding.getValue() != null)
      try {
        String testString = "abc";
        StringUtils.getBytes(testString, (String)this.characterEncoding.getValue());
      } catch (WrongArgumentException waEx) {
        String oldEncoding = (String)this.characterEncoding.getValue();
        this.characterEncoding.setValue(CharsetMapping.getJavaEncodingForMysqlCharset(oldEncoding));
        if (this.characterEncoding.getValue() == null)
          throw (WrongArgumentException)ExceptionFactory.createException(WrongArgumentException.class, Messages.getString("Connection.5", new Object[] { oldEncoding }), getExceptionInterceptor()); 
        String testString = "abc";
        StringUtils.getBytes(testString, (String)this.characterEncoding.getValue());
      }  
  }
  
  public boolean configureClientCharacterSet(boolean dontCheckServerMatch) {
    String realJavaEncoding = (String)this.characterEncoding.getValue();
    RuntimeProperty<String> characterSetResults = getPropertySet().getProperty(PropertyKey.characterSetResults);
    boolean characterSetAlreadyConfigured = false;
    try {
      characterSetAlreadyConfigured = true;
      configureCharsetProperties();
      realJavaEncoding = (String)this.characterEncoding.getValue();
      String connectionCollationSuffix = "";
      String connectionCollationCharset = null;
      String connectionCollation = getPropertySet().getStringProperty(PropertyKey.connectionCollation).getStringValue();
      if (connectionCollation != null)
        for (int i = 1; i < CharsetMapping.COLLATION_INDEX_TO_COLLATION_NAME.length; i++) {
          if (CharsetMapping.COLLATION_INDEX_TO_COLLATION_NAME[i].equals(connectionCollation)) {
            connectionCollationSuffix = " COLLATE " + CharsetMapping.COLLATION_INDEX_TO_COLLATION_NAME[i];
            connectionCollationCharset = (CharsetMapping.COLLATION_INDEX_TO_CHARSET[i]).charsetName;
            realJavaEncoding = CharsetMapping.getJavaEncodingForCollationIndex(Integer.valueOf(i));
          } 
        }  
      try {
        String serverEncodingToSet = CharsetMapping.getJavaEncodingForCollationIndex(Integer.valueOf(this.protocol.getServerSession().getServerDefaultCollationIndex()));
        if (serverEncodingToSet == null || serverEncodingToSet.length() == 0)
          if (realJavaEncoding != null) {
            this.characterEncoding.setValue(realJavaEncoding);
          } else {
            throw ExceptionFactory.createException(
                Messages.getString("Connection.6", new Object[] { Integer.valueOf(this.protocol.getServerSession().getServerDefaultCollationIndex()) }), getExceptionInterceptor());
          }  
        if ("ISO8859_1".equalsIgnoreCase(serverEncodingToSet))
          serverEncodingToSet = "Cp1252"; 
        if ("UnicodeBig".equalsIgnoreCase(serverEncodingToSet) || "UTF-16".equalsIgnoreCase(serverEncodingToSet) || "UTF-16LE"
          .equalsIgnoreCase(serverEncodingToSet) || "UTF-32".equalsIgnoreCase(serverEncodingToSet))
          serverEncodingToSet = "UTF-8"; 
        this.characterEncoding.setValue(serverEncodingToSet);
      } catch (ArrayIndexOutOfBoundsException outOfBoundsEx) {
        if (realJavaEncoding != null) {
          this.characterEncoding.setValue(realJavaEncoding);
        } else {
          throw ExceptionFactory.createException(
              Messages.getString("Connection.6", new Object[] { Integer.valueOf(this.protocol.getServerSession().getServerDefaultCollationIndex()) }), getExceptionInterceptor());
        } 
      } 
      if (this.characterEncoding.getValue() == null)
        this.characterEncoding.setValue("ISO8859_1"); 
      if (realJavaEncoding != null) {
        if (realJavaEncoding.equalsIgnoreCase("UTF-8") || realJavaEncoding.equalsIgnoreCase("UTF8")) {
          String utf8CharsetName = (connectionCollationSuffix.length() > 0) ? connectionCollationCharset : "utf8mb4";
          if (dontCheckServerMatch || !this.protocol.getServerSession().characterSetNamesMatches("utf8") || 
            !this.protocol.getServerSession().characterSetNamesMatches("utf8mb4") || (connectionCollationSuffix.length() > 0 && 
            !connectionCollation.equalsIgnoreCase(this.protocol.getServerSession().getServerVariable("collation_server")))) {
            sendCommand(this.commandBuilder.buildComQuery(null, "SET NAMES " + utf8CharsetName + connectionCollationSuffix), false, 0);
            this.protocol.getServerSession().getServerVariables().put("character_set_client", utf8CharsetName);
            this.protocol.getServerSession().getServerVariables().put("character_set_connection", utf8CharsetName);
          } 
          this.characterEncoding.setValue(realJavaEncoding);
        } else {
          String mysqlCharsetName = (connectionCollationSuffix.length() > 0) ? connectionCollationCharset : CharsetMapping.getMysqlCharsetForJavaEncoding(realJavaEncoding.toUpperCase(Locale.ENGLISH), 
              getServerSession().getServerVersion());
          if (mysqlCharsetName != null)
            if (dontCheckServerMatch || !this.protocol.getServerSession().characterSetNamesMatches(mysqlCharsetName)) {
              sendCommand(this.commandBuilder.buildComQuery(null, "SET NAMES " + mysqlCharsetName + connectionCollationSuffix), false, 0);
              this.protocol.getServerSession().getServerVariables().put("character_set_client", mysqlCharsetName);
              this.protocol.getServerSession().getServerVariables().put("character_set_connection", mysqlCharsetName);
            }  
          this.characterEncoding.setValue(realJavaEncoding);
        } 
      } else if (this.characterEncoding.getValue() != null) {
        String mysqlCharsetName = (connectionCollationSuffix.length() > 0) ? connectionCollationCharset : getServerSession().getServerDefaultCharset();
        boolean ucs2 = false;
        if ("ucs2".equalsIgnoreCase(mysqlCharsetName) || "utf16".equalsIgnoreCase(mysqlCharsetName) || "utf16le".equalsIgnoreCase(mysqlCharsetName) || "utf32"
          .equalsIgnoreCase(mysqlCharsetName)) {
          mysqlCharsetName = "utf8";
          ucs2 = true;
          if (characterSetResults.getValue() == null)
            characterSetResults.setValue("UTF-8"); 
        } 
        if (dontCheckServerMatch || !this.protocol.getServerSession().characterSetNamesMatches(mysqlCharsetName) || ucs2) {
          sendCommand(this.commandBuilder.buildComQuery(null, "SET NAMES " + mysqlCharsetName + connectionCollationSuffix), false, 0);
          this.protocol.getServerSession().getServerVariables().put("character_set_client", mysqlCharsetName);
          this.protocol.getServerSession().getServerVariables().put("character_set_connection", mysqlCharsetName);
        } 
        realJavaEncoding = (String)this.characterEncoding.getValue();
      } 
      String onServer = this.protocol.getServerSession().getServerVariable("character_set_results");
      if (characterSetResults.getValue() == null) {
        if (onServer != null && onServer.length() > 0 && !"NULL".equalsIgnoreCase(onServer)) {
          sendCommand(this.commandBuilder.buildComQuery(null, "SET character_set_results = NULL"), false, 0);
          this.protocol.getServerSession().getServerVariables().put("local.character_set_results", null);
        } else {
          this.protocol.getServerSession().getServerVariables().put("local.character_set_results", onServer);
        } 
      } else {
        String charsetResults = (String)characterSetResults.getValue();
        String mysqlEncodingName = null;
        if ("UTF-8".equalsIgnoreCase(charsetResults) || "UTF8".equalsIgnoreCase(charsetResults)) {
          mysqlEncodingName = "utf8";
        } else if ("null".equalsIgnoreCase(charsetResults)) {
          mysqlEncodingName = "NULL";
        } else {
          mysqlEncodingName = CharsetMapping.getMysqlCharsetForJavaEncoding(charsetResults.toUpperCase(Locale.ENGLISH), 
              getServerSession().getServerVersion());
        } 
        if (mysqlEncodingName == null)
          throw (WrongArgumentException)ExceptionFactory.createException(WrongArgumentException.class, Messages.getString("Connection.7", new Object[] { charsetResults }), getExceptionInterceptor()); 
        if (!mysqlEncodingName.equalsIgnoreCase(this.protocol.getServerSession().getServerVariable("character_set_results"))) {
          StringBuilder setBuf = new StringBuilder("SET character_set_results = ".length() + mysqlEncodingName.length());
          setBuf.append("SET character_set_results = ").append(mysqlEncodingName);
          sendCommand(this.commandBuilder.buildComQuery(null, setBuf.toString()), false, 0);
          this.protocol.getServerSession().getServerVariables().put("local.character_set_results", mysqlEncodingName);
          this.protocol.getServerSession().setErrorMessageEncoding(charsetResults);
        } else {
          this.protocol.getServerSession().getServerVariables().put("local.character_set_results", onServer);
        } 
      } 
    } finally {
      this.characterEncoding.setValue(realJavaEncoding);
    } 
    try {
      CharsetEncoder enc = Charset.forName((String)this.characterEncoding.getValue()).newEncoder();
      CharBuffer cbuf = CharBuffer.allocate(1);
      ByteBuffer bbuf = ByteBuffer.allocate(1);
      cbuf.put("¥");
      cbuf.position(0);
      enc.encode(cbuf, bbuf, true);
      if (bbuf.get(0) == 92) {
        this.requiresEscapingEncoder = true;
      } else {
        cbuf.clear();
        bbuf.clear();
        cbuf.put("₩");
        cbuf.position(0);
        enc.encode(cbuf, bbuf, true);
        if (bbuf.get(0) == 92)
          this.requiresEscapingEncoder = true; 
      } 
    } catch (UnsupportedCharsetException ucex) {
      byte[] bbuf = StringUtils.getBytes("¥", (String)this.characterEncoding.getValue());
      if (bbuf[0] == 92) {
        this.requiresEscapingEncoder = true;
      } else {
        bbuf = StringUtils.getBytes("₩", (String)this.characterEncoding.getValue());
        if (bbuf[0] == 92)
          this.requiresEscapingEncoder = true; 
      } 
    } 
    return characterSetAlreadyConfigured;
  }
  
  public boolean getRequiresEscapingEncoder() {
    return this.requiresEscapingEncoder;
  }
  
  private void createConfigCacheIfNeeded(Object syncMutex) {
    synchronized (syncMutex) {
      if (this.serverConfigCache != null)
        return; 
      try {
        Class<?> factoryClass = Class.forName(getPropertySet().getStringProperty(PropertyKey.serverConfigCacheFactory).getStringValue());
        CacheAdapterFactory<String, Map<String, String>> cacheFactory = (CacheAdapterFactory<String, Map<String, String>>)factoryClass.newInstance();
        this.serverConfigCache = cacheFactory.getInstance(syncMutex, this.hostInfo.getDatabaseUrl(), 2147483647, 2147483647);
        ExceptionInterceptor evictOnCommsError = new ExceptionInterceptor() {
            public ExceptionInterceptor init(Properties config, Log log1) {
              return this;
            }
            
            public void destroy() {}
            
            public Exception interceptException(Exception sqlEx) {
              if (sqlEx instanceof SQLException && ((SQLException)sqlEx).getSQLState() != null && ((SQLException)sqlEx)
                .getSQLState().startsWith("08"))
                NativeSession.this.serverConfigCache.invalidate(NativeSession.this.hostInfo.getDatabaseUrl()); 
              return null;
            }
          };
        if (this.exceptionInterceptor == null) {
          this.exceptionInterceptor = evictOnCommsError;
        } else {
          ((ExceptionInterceptorChain)this.exceptionInterceptor).addRingZero(evictOnCommsError);
        } 
      } catch (ClassNotFoundException e) {
        throw ExceptionFactory.createException(Messages.getString("Connection.CantFindCacheFactory", new Object[] { getPropertySet().getStringProperty(PropertyKey.parseInfoCacheFactory).getValue(), PropertyKey.parseInfoCacheFactory }), e, getExceptionInterceptor());
      } catch (InstantiationException|IllegalAccessException|CJException e) {
        throw ExceptionFactory.createException(Messages.getString("Connection.CantLoadCacheFactory", new Object[] { getPropertySet().getStringProperty(PropertyKey.parseInfoCacheFactory).getValue(), PropertyKey.parseInfoCacheFactory }), e, getExceptionInterceptor());
      } 
    } 
  }
  
  public void loadServerVariables(Object syncMutex, String version) {
    if (((Boolean)this.cacheServerConfiguration.getValue()).booleanValue()) {
      createConfigCacheIfNeeded(syncMutex);
      Map<String, String> cachedVariableMap = this.serverConfigCache.get(this.hostInfo.getDatabaseUrl());
      if (cachedVariableMap != null) {
        String cachedServerVersion = cachedVariableMap.get("server_version_string");
        if (cachedServerVersion != null && getServerSession().getServerVersion() != null && cachedServerVersion
          .equals(getServerSession().getServerVersion().toString())) {
          this.protocol.getServerSession().setServerVariables(cachedVariableMap);
          return;
        } 
        this.serverConfigCache.invalidate(this.hostInfo.getDatabaseUrl());
      } 
    } 
    try {
      if (version != null && version.indexOf('*') != -1) {
        StringBuilder buf = new StringBuilder(version.length() + 10);
        for (int i = 0; i < version.length(); i++) {
          char c = version.charAt(i);
          buf.append((c == '*') ? "[star]" : Character.valueOf(c));
        } 
        version = buf.toString();
      } 
      String versionComment = (((Boolean)this.propertySet.getBooleanProperty(PropertyKey.paranoid).getValue()).booleanValue() || version == null) ? "" : ("/* " + version + " */");
      this.protocol.getServerSession().setServerVariables(new HashMap<>());
      if (versionMeetsMinimum(5, 1, 0)) {
        StringBuilder queryBuf = (new StringBuilder(versionComment)).append("SELECT");
        queryBuf.append("  @@session.auto_increment_increment AS auto_increment_increment");
        queryBuf.append(", @@character_set_client AS character_set_client");
        queryBuf.append(", @@character_set_connection AS character_set_connection");
        queryBuf.append(", @@character_set_results AS character_set_results");
        queryBuf.append(", @@character_set_server AS character_set_server");
        queryBuf.append(", @@collation_server AS collation_server");
        queryBuf.append(", @@collation_connection AS collation_connection");
        queryBuf.append(", @@init_connect AS init_connect");
        queryBuf.append(", @@interactive_timeout AS interactive_timeout");
        if (!versionMeetsMinimum(5, 5, 0))
          queryBuf.append(", @@language AS language"); 
        queryBuf.append(", @@license AS license");
        queryBuf.append(", @@lower_case_table_names AS lower_case_table_names");
        queryBuf.append(", @@max_allowed_packet AS max_allowed_packet");
        queryBuf.append(", @@net_write_timeout AS net_write_timeout");
        queryBuf.append(", @@performance_schema AS performance_schema");
        if (!versionMeetsMinimum(8, 0, 3)) {
          queryBuf.append(", @@query_cache_size AS query_cache_size");
          queryBuf.append(", @@query_cache_type AS query_cache_type");
        } 
        queryBuf.append(", @@sql_mode AS sql_mode");
        queryBuf.append(", @@system_time_zone AS system_time_zone");
        queryBuf.append(", @@time_zone AS time_zone");
        if (versionMeetsMinimum(8, 0, 3) || (versionMeetsMinimum(5, 7, 20) && !versionMeetsMinimum(8, 0, 0))) {
          queryBuf.append(", @@transaction_isolation AS transaction_isolation");
        } else {
          queryBuf.append(", @@tx_isolation AS transaction_isolation");
        } 
        queryBuf.append(", @@wait_timeout AS wait_timeout");
        NativePacketPayload resultPacket = sendCommand(this.commandBuilder.buildComQuery(null, queryBuf.toString()), false, 0);
        Resultset rs = ((NativeProtocol)this.protocol).readAllResults(-1, false, resultPacket, false, null, (ProtocolEntityFactory)new ResultsetFactory(Resultset.Type.FORWARD_ONLY, null));
        Field[] f = rs.getColumnDefinition().getFields();
        StringValueFactory stringValueFactory = new StringValueFactory(this.propertySet);
        Row r;
        if (f.length > 0 && (r = (Row)rs.getRows().next()) != null)
          for (int i = 0; i < f.length; i++)
            this.protocol.getServerSession().getServerVariables().put(f[i].getColumnLabel(), r.getValue(i, (ValueFactory)stringValueFactory));  
      } else {
        NativePacketPayload resultPacket = sendCommand(this.commandBuilder.buildComQuery(null, versionComment + "SHOW VARIABLES"), false, 0);
        Resultset rs = ((NativeProtocol)this.protocol).readAllResults(-1, false, resultPacket, false, null, (ProtocolEntityFactory)new ResultsetFactory(Resultset.Type.FORWARD_ONLY, null));
        StringValueFactory stringValueFactory = new StringValueFactory(this.propertySet);
        Row r;
        while ((r = (Row)rs.getRows().next()) != null)
          this.protocol.getServerSession().getServerVariables().put(r.getValue(0, (ValueFactory)stringValueFactory), r.getValue(1, (ValueFactory)stringValueFactory)); 
      } 
    } catch (IOException e) {
      throw ExceptionFactory.createException(e.getMessage(), e);
    } 
    if (((Boolean)this.cacheServerConfiguration.getValue()).booleanValue()) {
      this.protocol.getServerSession().getServerVariables().put("server_version_string", getServerSession().getServerVersion().toString());
      this.serverConfigCache.put(this.hostInfo.getDatabaseUrl(), this.protocol.getServerSession().getServerVariables());
    } 
  }
  
  public void setSessionVariables() {
    String sessionVariables = (String)getPropertySet().getStringProperty(PropertyKey.sessionVariables).getValue();
    if (sessionVariables != null) {
      List<String> variablesToSet = new ArrayList<>();
      for (String part : StringUtils.split(sessionVariables, ",", "\"'(", "\"')", "\"'", true))
        variablesToSet.addAll(StringUtils.split(part, ";", "\"'(", "\"')", "\"'", true)); 
      if (!variablesToSet.isEmpty()) {
        StringBuilder query = new StringBuilder("SET ");
        String separator = "";
        for (String variableToSet : variablesToSet) {
          if (variableToSet.length() > 0) {
            query.append(separator);
            if (!variableToSet.startsWith("@"))
              query.append("SESSION "); 
            query.append(variableToSet);
            separator = ",";
          } 
        } 
        sendCommand(this.commandBuilder.buildComQuery(null, query.toString()), false, 0);
      } 
    } 
  }
  
  public void buildCollationMapping() {
    Map<Integer, String> customCharset = null;
    Map<String, Integer> customMblen = null;
    String databaseURL = this.hostInfo.getDatabaseUrl();
    if (((Boolean)this.cacheServerConfiguration.getValue()).booleanValue())
      synchronized (customIndexToCharsetMapByUrl) {
        customCharset = customIndexToCharsetMapByUrl.get(databaseURL);
        customMblen = customCharsetToMblenMapByUrl.get(databaseURL);
      }  
    if (customCharset == null && ((Boolean)getPropertySet().getBooleanProperty(PropertyKey.detectCustomCollations).getValue()).booleanValue()) {
      customCharset = new HashMap<>();
      customMblen = new HashMap<>();
      IntegerValueFactory integerValueFactory = new IntegerValueFactory(getPropertySet());
      try {
        NativePacketPayload resultPacket = sendCommand(this.commandBuilder.buildComQuery(null, "SHOW COLLATION"), false, 0);
        Resultset rs = ((NativeProtocol)this.protocol).readAllResults(-1, false, resultPacket, false, null, (ProtocolEntityFactory)new ResultsetFactory(Resultset.Type.FORWARD_ONLY, null));
        StringValueFactory stringValueFactory = new StringValueFactory(this.propertySet);
        Row r;
        while ((r = (Row)rs.getRows().next()) != null) {
          int collationIndex = ((Number)r.getValue(2, (ValueFactory)integerValueFactory)).intValue();
          String charsetName = (String)r.getValue(1, (ValueFactory)stringValueFactory);
          if (collationIndex >= 2048 || !charsetName.equals(CharsetMapping.getMysqlCharsetNameForCollationIndex(Integer.valueOf(collationIndex))))
            customCharset.put(Integer.valueOf(collationIndex), charsetName); 
          if (!CharsetMapping.CHARSET_NAME_TO_CHARSET.containsKey(charsetName))
            customMblen.put(charsetName, null); 
        } 
      } catch (IOException e) {
        throw ExceptionFactory.createException(e.getMessage(), e, this.exceptionInterceptor);
      } 
      if (customMblen.size() > 0)
        try {
          NativePacketPayload resultPacket = sendCommand(this.commandBuilder.buildComQuery(null, "SHOW CHARACTER SET"), false, 0);
          Resultset rs = ((NativeProtocol)this.protocol).readAllResults(-1, false, resultPacket, false, null, (ProtocolEntityFactory)new ResultsetFactory(Resultset.Type.FORWARD_ONLY, null));
          int charsetColumn = ((Integer)rs.getColumnDefinition().getColumnNameToIndex().get("Charset")).intValue();
          int maxlenColumn = ((Integer)rs.getColumnDefinition().getColumnNameToIndex().get("Maxlen")).intValue();
          StringValueFactory stringValueFactory = new StringValueFactory(this.propertySet);
          Row r;
          while ((r = (Row)rs.getRows().next()) != null) {
            String charsetName = (String)r.getValue(charsetColumn, (ValueFactory)stringValueFactory);
            if (customMblen.containsKey(charsetName))
              customMblen.put(charsetName, r.getValue(maxlenColumn, (ValueFactory)integerValueFactory)); 
          } 
        } catch (IOException e) {
          throw ExceptionFactory.createException(e.getMessage(), e, this.exceptionInterceptor);
        }  
      if (((Boolean)this.cacheServerConfiguration.getValue()).booleanValue())
        synchronized (customIndexToCharsetMapByUrl) {
          customIndexToCharsetMapByUrl.put(databaseURL, customCharset);
          customCharsetToMblenMapByUrl.put(databaseURL, customMblen);
        }  
    } 
    if (customCharset != null)
      ((NativeServerSession)this.protocol.getServerSession()).indexToCustomMysqlCharset = Collections.unmodifiableMap(customCharset); 
    if (customMblen != null)
      ((NativeServerSession)this.protocol.getServerSession()).mysqlCharsetToCustomMblen = Collections.unmodifiableMap(customMblen); 
    if (this.protocol.getServerSession().getServerDefaultCollationIndex() == 0) {
      String collationServer = this.protocol.getServerSession().getServerVariable("collation_server");
      if (collationServer != null) {
        for (int i = 1; i < CharsetMapping.COLLATION_INDEX_TO_COLLATION_NAME.length; i++) {
          if (CharsetMapping.COLLATION_INDEX_TO_COLLATION_NAME[i].equals(collationServer)) {
            this.protocol.getServerSession().setServerDefaultCollationIndex(i);
            break;
          } 
        } 
      } else {
        this.protocol.getServerSession().setServerDefaultCollationIndex(45);
      } 
    } 
  }
  
  public String getProcessHost() {
    try {
      long threadId = getThreadId();
      String processHost = findProcessHost(threadId);
      if (processHost == null) {
        this.log.logWarn(String.format("Connection id %d not found in \"SHOW PROCESSLIST\", assuming 32-bit overflow, using SELECT CONNECTION_ID() instead", new Object[] { Long.valueOf(threadId) }));
        NativePacketPayload resultPacket = sendCommand(this.commandBuilder.buildComQuery(null, "SELECT CONNECTION_ID()"), false, 0);
        Resultset rs = ((NativeProtocol)this.protocol).readAllResults(-1, false, resultPacket, false, null, (ProtocolEntityFactory)new ResultsetFactory(Resultset.Type.FORWARD_ONLY, null));
        LongValueFactory longValueFactory = new LongValueFactory(getPropertySet());
        Row r;
        if ((r = (Row)rs.getRows().next()) != null) {
          threadId = ((Long)r.getValue(0, (ValueFactory)longValueFactory)).longValue();
          processHost = findProcessHost(threadId);
        } else {
          this.log.logError("No rows returned for statement \"SELECT CONNECTION_ID()\", local connection check will most likely be incorrect");
        } 
      } 
      if (processHost == null)
        this.log.logWarn(String.format("Cannot find process listing for connection %d in SHOW PROCESSLIST output, unable to determine if locally connected", new Object[] { Long.valueOf(threadId) })); 
      return processHost;
    } catch (IOException e) {
      throw ExceptionFactory.createException(e.getMessage(), e);
    } 
  }
  
  private String findProcessHost(long threadId) {
    try {
      String processHost = null;
      String ps = this.protocol.getServerSession().getServerVariable("performance_schema");
      NativePacketPayload resultPacket = (versionMeetsMinimum(5, 6, 0) && ps != null && ("1".contentEquals(ps) || "ON".contentEquals(ps))) ? sendCommand(this.commandBuilder.buildComQuery(null, "select PROCESSLIST_ID, PROCESSLIST_USER, PROCESSLIST_HOST from performance_schema.threads where PROCESSLIST_ID=" + threadId), false, 0) : sendCommand(this.commandBuilder.buildComQuery(null, "SHOW PROCESSLIST"), false, 0);
      Resultset rs = ((NativeProtocol)this.protocol).readAllResults(-1, false, resultPacket, false, null, (ProtocolEntityFactory)new ResultsetFactory(Resultset.Type.FORWARD_ONLY, null));
      LongValueFactory longValueFactory = new LongValueFactory(getPropertySet());
      StringValueFactory stringValueFactory = new StringValueFactory(this.propertySet);
      Row r;
      while ((r = (Row)rs.getRows().next()) != null) {
        long id = ((Long)r.getValue(0, (ValueFactory)longValueFactory)).longValue();
        if (threadId == id) {
          processHost = (String)r.getValue(2, (ValueFactory)stringValueFactory);
          break;
        } 
      } 
      return processHost;
    } catch (IOException e) {
      throw ExceptionFactory.createException(e.getMessage(), e);
    } 
  }
  
  public String queryServerVariable(String varName) {
    try {
      NativePacketPayload resultPacket = sendCommand(this.commandBuilder.buildComQuery(null, "SELECT " + varName), false, 0);
      Resultset rs = ((NativeProtocol)this.protocol).readAllResults(-1, false, resultPacket, false, null, (ProtocolEntityFactory)new ResultsetFactory(Resultset.Type.FORWARD_ONLY, null));
      StringValueFactory stringValueFactory = new StringValueFactory(this.propertySet);
      Row r;
      if ((r = (Row)rs.getRows().next()) != null) {
        String s = (String)r.getValue(0, (ValueFactory)stringValueFactory);
        if (s != null)
          return s; 
      } 
      return null;
    } catch (IOException e) {
      throw ExceptionFactory.createException(e.getMessage(), e);
    } 
  }
  
  public <T extends Resultset> T execSQL(Query callingQuery, String query, int maxRows, NativePacketPayload packet, boolean streamResults, ProtocolEntityFactory<T, NativePacketPayload> resultSetFactory, ColumnDefinition cachedMetadata, boolean isBatch) {
    long queryStartTime = ((Boolean)this.gatherPerfMetrics.getValue()).booleanValue() ? System.currentTimeMillis() : 0L;
    int endOfQueryPacketPosition = (packet != null) ? packet.getPosition() : 0;
    this.lastQueryFinishedTime = 0L;
    if (((Boolean)this.autoReconnect.getValue()).booleanValue() && (getServerSession().isAutoCommit() || ((Boolean)this.autoReconnectForPools.getValue()).booleanValue()) && this.needsPing && !isBatch)
      try {
        ping(false, 0);
        this.needsPing = false;
      } catch (Exception Ex) {
        invokeReconnectListeners();
      }  
    try {
      return (T)((packet == null) ? ((NativeProtocol)this.protocol)
        .sendQueryString(callingQuery, query, (String)this.characterEncoding.getValue(), maxRows, streamResults, cachedMetadata, resultSetFactory) : ((NativeProtocol)this.protocol)
        
        .sendQueryPacket(callingQuery, packet, maxRows, streamResults, cachedMetadata, resultSetFactory));
    } catch (CJException sqlE) {
      if (((Boolean)getPropertySet().getBooleanProperty(PropertyKey.dumpQueriesOnException).getValue()).booleanValue()) {
        String extractedSql = NativePacketPayload.extractSqlFromPacket(query, packet, endOfQueryPacketPosition, ((Integer)
            getPropertySet().getIntegerProperty(PropertyKey.maxQuerySizeToLog).getValue()).intValue());
        StringBuilder messageBuf = new StringBuilder(extractedSql.length() + 32);
        messageBuf.append("\n\nQuery being executed when exception was thrown:\n");
        messageBuf.append(extractedSql);
        messageBuf.append("\n\n");
        sqlE.appendMessage(messageBuf.toString());
      } 
      if (((Boolean)this.autoReconnect.getValue()).booleanValue()) {
        if (sqlE instanceof CJCommunicationsException)
          this.protocol.getSocketConnection().forceClose(); 
        this.needsPing = true;
      } else if (sqlE instanceof CJCommunicationsException) {
        invokeCleanupListeners((Throwable)sqlE);
      } 
      throw sqlE;
    } catch (Throwable ex) {
      if (((Boolean)this.autoReconnect.getValue()).booleanValue()) {
        if (ex instanceof IOException) {
          this.protocol.getSocketConnection().forceClose();
        } else if (ex instanceof IOException) {
          invokeCleanupListeners(ex);
        } 
        this.needsPing = true;
      } 
      throw ExceptionFactory.createException(ex.getMessage(), ex, this.exceptionInterceptor);
    } finally {
      if (((Boolean)this.maintainTimeStats.getValue()).booleanValue())
        this.lastQueryFinishedTime = System.currentTimeMillis(); 
      if (((Boolean)this.gatherPerfMetrics.getValue()).booleanValue())
        ((NativeProtocol)this.protocol).getMetricsHolder().registerQueryExecutionTime(System.currentTimeMillis() - queryStartTime); 
    } 
  }
  
  public long getIdleFor() {
    return (this.lastQueryFinishedTime == 0L) ? 0L : (System.currentTimeMillis() - this.lastQueryFinishedTime);
  }
  
  public boolean isNeedsPing() {
    return this.needsPing;
  }
  
  public void setNeedsPing(boolean needsPing) {
    this.needsPing = needsPing;
  }
  
  public void ping(boolean checkForClosedConnection, int timeoutMillis) {
    if (checkForClosedConnection)
      checkClosed(); 
    long pingMillisLifetime = ((Integer)getPropertySet().getIntegerProperty(PropertyKey.selfDestructOnPingSecondsLifetime).getValue()).intValue();
    int pingMaxOperations = ((Integer)getPropertySet().getIntegerProperty(PropertyKey.selfDestructOnPingMaxOperations).getValue()).intValue();
    if ((pingMillisLifetime > 0L && System.currentTimeMillis() - this.connectionCreationTimeMillis > pingMillisLifetime) || (pingMaxOperations > 0 && pingMaxOperations <= 
      getCommandCount())) {
      invokeNormalCloseListeners();
      throw ExceptionFactory.createException(Messages.getString("Connection.exceededConnectionLifetime"), "08S01", 0, false, null, this.exceptionInterceptor);
    } 
    sendCommand(this.commandBuilder.buildComPing(null), false, timeoutMillis);
  }
  
  public long getConnectionCreationTimeMillis() {
    return this.connectionCreationTimeMillis;
  }
  
  public void setConnectionCreationTimeMillis(long connectionCreationTimeMillis) {
    this.connectionCreationTimeMillis = connectionCreationTimeMillis;
  }
  
  public boolean isClosed() {
    return this.isClosed;
  }
  
  public void checkClosed() {
    if (this.isClosed) {
      if (this.forceClosedReason != null && this.forceClosedReason.getClass().equals(OperationCancelledException.class))
        throw (OperationCancelledException)this.forceClosedReason; 
      throw (ConnectionIsClosedException)ExceptionFactory.createException(ConnectionIsClosedException.class, Messages.getString("Connection.2"), this.forceClosedReason, 
          getExceptionInterceptor());
    } 
  }
  
  public Throwable getForceClosedReason() {
    return this.forceClosedReason;
  }
  
  public void setForceClosedReason(Throwable forceClosedReason) {
    this.forceClosedReason = forceClosedReason;
  }
  
  public void addListener(Session.SessionEventListener l) {
    this.listeners.addIfAbsent(new WeakReference<>(l));
  }
  
  public void removeListener(Session.SessionEventListener listener) {
    for (WeakReference<Session.SessionEventListener> wr : this.listeners) {
      Session.SessionEventListener l = wr.get();
      if (l == listener) {
        this.listeners.remove(wr);
        break;
      } 
    } 
  }
  
  protected void invokeNormalCloseListeners() {
    for (WeakReference<Session.SessionEventListener> wr : this.listeners) {
      Session.SessionEventListener l = wr.get();
      if (l != null) {
        l.handleNormalClose();
        continue;
      } 
      this.listeners.remove(wr);
    } 
  }
  
  protected void invokeReconnectListeners() {
    for (WeakReference<Session.SessionEventListener> wr : this.listeners) {
      Session.SessionEventListener l = wr.get();
      if (l != null) {
        l.handleReconnect();
        continue;
      } 
      this.listeners.remove(wr);
    } 
  }
  
  public void invokeCleanupListeners(Throwable whyCleanedUp) {
    for (WeakReference<Session.SessionEventListener> wr : this.listeners) {
      Session.SessionEventListener l = wr.get();
      if (l != null) {
        l.handleCleanup(whyCleanedUp);
        continue;
      } 
      this.listeners.remove(wr);
    } 
  }
  
  public String getIdentifierQuoteString() {
    return (this.protocol != null && this.protocol.getServerSession().useAnsiQuotedIdentifiers()) ? "\"" : "`";
  }
  
  public synchronized Timer getCancelTimer() {
    if (this.cancelTimer == null)
      this.cancelTimer = new Timer("MySQL Statement Cancellation Timer", Boolean.TRUE.booleanValue()); 
    return this.cancelTimer;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\NativeSession.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
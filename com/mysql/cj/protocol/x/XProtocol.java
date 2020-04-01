package com.mysql.cj.protocol.x;

import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.Message;
import com.mysql.cj.CharsetMapping;
import com.mysql.cj.Constants;
import com.mysql.cj.Messages;
import com.mysql.cj.Session;
import com.mysql.cj.TransactionEventHandler;
import com.mysql.cj.conf.HostInfo;
import com.mysql.cj.conf.PropertyDefinitions;
import com.mysql.cj.conf.PropertyKey;
import com.mysql.cj.conf.PropertySet;
import com.mysql.cj.conf.RuntimeProperty;
import com.mysql.cj.exceptions.AssertionFailedException;
import com.mysql.cj.exceptions.CJCommunicationsException;
import com.mysql.cj.exceptions.CJConnectionFeatureNotAvailableException;
import com.mysql.cj.exceptions.CJOperationNotSupportedException;
import com.mysql.cj.exceptions.ConnectionIsClosedException;
import com.mysql.cj.exceptions.ExceptionFactory;
import com.mysql.cj.exceptions.ExceptionInterceptor;
import com.mysql.cj.exceptions.SSLParamsException;
import com.mysql.cj.exceptions.WrongArgumentException;
import com.mysql.cj.protocol.AbstractProtocol;
import com.mysql.cj.protocol.ColumnDefinition;
import com.mysql.cj.protocol.ExportControlled;
import com.mysql.cj.protocol.Message;
import com.mysql.cj.protocol.MessageListener;
import com.mysql.cj.protocol.MessageReader;
import com.mysql.cj.protocol.MessageSender;
import com.mysql.cj.protocol.Protocol;
import com.mysql.cj.protocol.ProtocolEntity;
import com.mysql.cj.protocol.ProtocolEntityFactory;
import com.mysql.cj.protocol.ResultBuilder;
import com.mysql.cj.protocol.ResultStreamer;
import com.mysql.cj.protocol.Resultset;
import com.mysql.cj.protocol.ServerCapabilities;
import com.mysql.cj.protocol.ServerSession;
import com.mysql.cj.protocol.SocketConnection;
import com.mysql.cj.protocol.a.NativeSocketConnection;
import com.mysql.cj.result.DefaultColumnDefinition;
import com.mysql.cj.result.Field;
import com.mysql.cj.result.LongValueFactory;
import com.mysql.cj.result.ValueFactory;
import com.mysql.cj.util.SequentialIdLease;
import com.mysql.cj.util.StringUtils;
import com.mysql.cj.x.protobuf.Mysqlx;
import com.mysql.cj.x.protobuf.MysqlxConnection;
import com.mysql.cj.x.protobuf.MysqlxDatatypes;
import com.mysql.cj.x.protobuf.MysqlxNotice;
import com.mysql.cj.x.protobuf.MysqlxResultset;
import com.mysql.cj.x.protobuf.MysqlxSession;
import com.mysql.cj.x.protobuf.MysqlxSql;
import com.mysql.cj.xdevapi.PreparableStatement;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class XProtocol extends AbstractProtocol<XMessage> implements Protocol<XMessage> {
  private static int RETRY_PREPARE_STATEMENT_COUNTDOWN = 100;
  
  private MessageReader<XMessageHeader, XMessage> reader;
  
  private MessageSender<XMessage> sender;
  
  private Closeable managedResource;
  
  private ResultStreamer currentResultStreamer;
  
  XServerSession serverSession = null;
  
  Boolean useSessionResetKeepOpen = null;
  
  public String defaultSchemaName;
  
  private Map<String, Object> clientCapabilities = new HashMap<>();
  
  private boolean supportsPreparedStatements = true;
  
  private int retryPrepareStatementCountdown = 0;
  
  private SequentialIdLease preparedStatementIds = new SequentialIdLease();
  
  private ReferenceQueue<PreparableStatement<?>> preparableStatementRefQueue = new ReferenceQueue<>();
  
  private Map<Integer, PreparableStatement.PreparableStatementFinalizer> preparableStatementFinalizerReferences = new TreeMap<>();
  
  private Map<Class<? extends GeneratedMessageV3>, ProtocolEntityFactory<? extends ProtocolEntity, XMessage>> messageToProtocolEntityFactory = new HashMap<>();
  
  private String currUser;
  
  private String currPassword;
  
  private String currDatabase;
  
  public void init(Session sess, SocketConnection socketConn, PropertySet propSet, TransactionEventHandler trManager) {
    super.init(sess, socketConn, propSet, trManager);
    this.messageBuilder = new XMessageBuilder();
    this.authProvider = new XAuthenticationProvider();
    this.authProvider.init(this, propSet, null);
    this.useSessionResetKeepOpen = null;
    this.messageToProtocolEntityFactory.put(MysqlxResultset.ColumnMetaData.class, new FieldFactory("latin1"));
    this.messageToProtocolEntityFactory.put(MysqlxNotice.Frame.class, new NoticeFactory());
    this.messageToProtocolEntityFactory.put(MysqlxResultset.Row.class, new XProtocolRowFactory());
    this.messageToProtocolEntityFactory.put(MysqlxResultset.FetchDoneMoreResultsets.class, new FetchDoneMoreResultsFactory());
    this.messageToProtocolEntityFactory.put(MysqlxResultset.FetchDone.class, new FetchDoneEntityFactory());
    this.messageToProtocolEntityFactory.put(MysqlxSql.StmtExecuteOk.class, new StatementExecuteOkFactory());
    this.messageToProtocolEntityFactory.put(Mysqlx.Ok.class, new OkFactory());
  }
  
  public ServerSession getServerSession() {
    return this.serverSession;
  }
  
  public void sendCapabilities(Map<String, Object> keyValuePair) {
    keyValuePair.forEach((k, v) -> ((XServerCapabilities)getServerSession().getCapabilities()).setCapability(k, v));
    this.sender.send(((XMessageBuilder)this.messageBuilder).buildCapabilitiesSet(keyValuePair));
    readQueryResult(new OkBuilder());
  }
  
  public void negotiateSSLConnection(int packLength) {
    if (!ExportControlled.enabled())
      throw new CJConnectionFeatureNotAvailableException(); 
    if (!((XServerCapabilities)this.serverSession.getCapabilities()).hasCapability(XServerCapabilities.KEY_TLS))
      throw new CJCommunicationsException("A secure connection is required but the server is not configured with SSL."); 
    this.reader.stopAfterNextMessage();
    Map<String, Object> tlsCapabilities = new HashMap<>();
    tlsCapabilities.put(XServerCapabilities.KEY_TLS, Boolean.valueOf(true));
    sendCapabilities(tlsCapabilities);
    try {
      this.socketConnection.performTlsHandshake(null);
    } catch (SSLParamsException|com.mysql.cj.exceptions.FeatureNotAvailableException|IOException e) {
      throw new CJCommunicationsException(e);
    } 
    try {
      if (this.socketConnection.isSynchronous()) {
        this.sender = new SyncMessageSender(this.socketConnection.getMysqlOutput());
        this.reader = new SyncMessageReader(this.socketConnection.getMysqlInput());
      } else {
        ((AsyncMessageSender)this.sender).setChannel(this.socketConnection.getAsynchronousSocketChannel());
        this.reader.start();
      } 
    } catch (IOException e) {
      throw new XProtocolError(e.getMessage(), e);
    } 
  }
  
  public void beforeHandshake() {
    this.serverSession = new XServerSession();
    try {
      if (this.socketConnection.isSynchronous()) {
        this.sender = new SyncMessageSender(this.socketConnection.getMysqlOutput());
        this.reader = new SyncMessageReader(this.socketConnection.getMysqlInput());
        this.managedResource = this.socketConnection.getMysqlSocket();
      } else {
        this.sender = new AsyncMessageSender(this.socketConnection.getAsynchronousSocketChannel());
        this.reader = new AsyncMessageReader(this.propertySet, this.socketConnection);
        this.reader.start();
        this.managedResource = this.socketConnection.getAsynchronousSocketChannel();
      } 
    } catch (IOException e) {
      throw new XProtocolError(e.getMessage(), e);
    } 
    this.serverSession.setCapabilities(readServerCapabilities());
    String attributes = (String)this.propertySet.getStringProperty(PropertyKey.xdevapiConnectionAttributes).getValue();
    if (attributes == null || !attributes.equalsIgnoreCase("false")) {
      Map<String, String> attMap = getConnectionAttributesMap("true".equalsIgnoreCase(attributes) ? "" : attributes);
      this.clientCapabilities.put(XServerCapabilities.KEY_SESSION_CONNECT_ATTRS, attMap);
    } 
    RuntimeProperty<PropertyDefinitions.XdevapiSslMode> xdevapiSslMode = this.propertySet.getEnumProperty(PropertyKey.xdevapiSSLMode);
    if (xdevapiSslMode.isExplicitlySet())
      this.propertySet.getEnumProperty(PropertyKey.sslMode).setValue(PropertyDefinitions.SslMode.valueOf(((PropertyDefinitions.XdevapiSslMode)xdevapiSslMode.getValue()).toString())); 
    RuntimeProperty<String> sslTrustStoreUrl = this.propertySet.getStringProperty(PropertyKey.xdevapiSSLTrustStoreUrl);
    if (sslTrustStoreUrl.isExplicitlySet())
      this.propertySet.getStringProperty(PropertyKey.trustCertificateKeyStoreUrl).setValue(sslTrustStoreUrl.getValue()); 
    RuntimeProperty<String> sslTrustStoreType = this.propertySet.getStringProperty(PropertyKey.xdevapiSSLTrustStoreType);
    if (sslTrustStoreType.isExplicitlySet())
      this.propertySet.getStringProperty(PropertyKey.trustCertificateKeyStoreType).setValue(sslTrustStoreType.getValue()); 
    RuntimeProperty<String> sslTrustStorePassword = this.propertySet.getStringProperty(PropertyKey.xdevapiSSLTrustStorePassword);
    if (sslTrustStorePassword.isExplicitlySet())
      this.propertySet.getStringProperty(PropertyKey.trustCertificateKeyStorePassword).setValue(sslTrustStorePassword.getValue()); 
    RuntimeProperty<PropertyDefinitions.SslMode> sslMode = this.propertySet.getEnumProperty(PropertyKey.sslMode);
    if (sslMode.getValue() == PropertyDefinitions.SslMode.PREFERRED)
      sslMode.setValue(PropertyDefinitions.SslMode.REQUIRED); 
    boolean verifyServerCert = (sslMode.getValue() == PropertyDefinitions.SslMode.VERIFY_CA || sslMode.getValue() == PropertyDefinitions.SslMode.VERIFY_IDENTITY);
    String trustStoreUrl = (String)this.propertySet.getStringProperty(PropertyKey.trustCertificateKeyStoreUrl).getValue();
    RuntimeProperty<String> xdevapiTlsVersions = this.propertySet.getStringProperty(PropertyKey.xdevapiTlsVersions);
    if (xdevapiTlsVersions.isExplicitlySet()) {
      if (sslMode.getValue() == PropertyDefinitions.SslMode.DISABLED)
        throw (WrongArgumentException)ExceptionFactory.createException(WrongArgumentException.class, "Option '" + PropertyKey.xdevapiTlsVersions
            .getKeyName() + "' can not be specified when SSL connections are disabled."); 
      if (((String)xdevapiTlsVersions.getValue()).trim().isEmpty())
        throw (WrongArgumentException)ExceptionFactory.createException(WrongArgumentException.class, "At least one TLS protocol version must be specified in '" + PropertyKey.xdevapiTlsVersions
            .getKeyName() + "' list."); 
      String[] tlsVersions = ((String)xdevapiTlsVersions.getValue()).split("\\s*,\\s*");
      List<String> tryProtocols = Arrays.asList(tlsVersions);
      ExportControlled.checkValidProtocols(tryProtocols);
      this.propertySet.getStringProperty(PropertyKey.enabledTLSProtocols).setValue(xdevapiTlsVersions.getValue());
    } 
    RuntimeProperty<String> xdevapiTlsCiphersuites = this.propertySet.getStringProperty(PropertyKey.xdevapiTlsCiphersuites);
    if (xdevapiTlsCiphersuites.isExplicitlySet()) {
      if (sslMode.getValue() == PropertyDefinitions.SslMode.DISABLED)
        throw (WrongArgumentException)ExceptionFactory.createException(WrongArgumentException.class, "Option '" + PropertyKey.xdevapiTlsCiphersuites
            .getKeyName() + "' can not be specified when SSL connections are disabled."); 
      this.propertySet.getStringProperty(PropertyKey.enabledSSLCipherSuites).setValue(xdevapiTlsCiphersuites.getValue());
    } 
    if (!verifyServerCert && !StringUtils.isNullOrEmpty(trustStoreUrl)) {
      StringBuilder msg = new StringBuilder("Incompatible security settings. The property '");
      msg.append(PropertyKey.xdevapiSSLTrustStoreUrl.getKeyName()).append("' requires '");
      msg.append(PropertyKey.xdevapiSSLMode.getKeyName()).append("' as '");
      msg.append(PropertyDefinitions.SslMode.VERIFY_CA).append("' or '");
      msg.append(PropertyDefinitions.SslMode.VERIFY_IDENTITY).append("'.");
      throw new CJCommunicationsException(msg.toString());
    } 
    if (this.clientCapabilities.size() > 0)
      try {
        sendCapabilities(this.clientCapabilities);
      } catch (XProtocolError e) {
        if (e.getErrorCode() != 5002 && !e.getMessage().contains(XServerCapabilities.KEY_SESSION_CONNECT_ATTRS))
          throw e; 
        this.clientCapabilities.remove(XServerCapabilities.KEY_SESSION_CONNECT_ATTRS);
      }  
    if (xdevapiSslMode.getValue() != PropertyDefinitions.XdevapiSslMode.DISABLED)
      negotiateSSLConnection(0); 
  }
  
  private Map<String, String> getConnectionAttributesMap(String attStr) {
    Map<String, String> attMap = new HashMap<>();
    if (attStr != null) {
      if (attStr.startsWith("[") && attStr.endsWith("]"))
        attStr = attStr.substring(1, attStr.length() - 1); 
      if (!StringUtils.isNullOrEmpty(attStr)) {
        String[] pairs = attStr.split(",");
        for (String pair : pairs) {
          String[] kv = pair.split("=");
          String key = kv[0].trim();
          String value = (kv.length > 1) ? kv[1].trim() : "";
          if (key.startsWith("_"))
            throw (WrongArgumentException)ExceptionFactory.createException(WrongArgumentException.class, Messages.getString("Protocol.WrongAttributeName")); 
          if (attMap.put(key, value) != null)
            throw (WrongArgumentException)ExceptionFactory.createException(WrongArgumentException.class, 
                Messages.getString("Protocol.DuplicateAttribute", new Object[] { key })); 
        } 
      } 
    } 
    attMap.put("_platform", Constants.OS_ARCH);
    attMap.put("_os", Constants.OS_NAME + "-" + Constants.OS_VERSION);
    attMap.put("_client_name", "MySQL Connector/J");
    attMap.put("_client_version", "8.0.19");
    attMap.put("_client_license", "GPL");
    attMap.put("_runtime_version", Constants.JVM_VERSION);
    attMap.put("_runtime_vendor", Constants.JVM_VENDOR);
    return attMap;
  }
  
  public XProtocol(String host, int port, String defaultSchema, PropertySet propertySet) {
    this.currUser = null;
    this.currPassword = null;
    this.currDatabase = null;
    this.defaultSchemaName = defaultSchema;
    RuntimeProperty<Integer> connectTimeout = propertySet.getIntegerProperty(PropertyKey.connectTimeout);
    RuntimeProperty<Integer> xdevapiConnectTimeout = propertySet.getIntegerProperty(PropertyKey.xdevapiConnectTimeout);
    if (xdevapiConnectTimeout.isExplicitlySet() || !connectTimeout.isExplicitlySet())
      connectTimeout.setValue(xdevapiConnectTimeout.getValue()); 
    SocketConnection socketConn = ((Boolean)propertySet.getBooleanProperty(PropertyKey.xdevapiUseAsyncProtocol).getValue()).booleanValue() ? new XAsyncSocketConnection() : (SocketConnection)new NativeSocketConnection();
    socketConn.connect(host, port, propertySet, null, null, 0);
    init((Session)null, socketConn, propertySet, (TransactionEventHandler)null);
  }
  
  public XProtocol(HostInfo hostInfo, PropertySet propertySet) {
    this.currUser = null;
    this.currPassword = null;
    this.currDatabase = null;
    String host = hostInfo.getHost();
    if (host == null || StringUtils.isEmptyOrWhitespaceOnly(host))
      host = "localhost"; 
    int port = hostInfo.getPort();
    if (port < 0)
      port = 33060; 
    this.defaultSchemaName = hostInfo.getDatabase();
    RuntimeProperty<Integer> connectTimeout = propertySet.getIntegerProperty(PropertyKey.connectTimeout);
    RuntimeProperty<Integer> xdevapiConnectTimeout = propertySet.getIntegerProperty(PropertyKey.xdevapiConnectTimeout);
    if (xdevapiConnectTimeout.isExplicitlySet() || !connectTimeout.isExplicitlySet())
      connectTimeout.setValue(xdevapiConnectTimeout.getValue()); 
    SocketConnection socketConn = ((Boolean)propertySet.getBooleanProperty(PropertyKey.xdevapiUseAsyncProtocol).getValue()).booleanValue() ? new XAsyncSocketConnection() : (SocketConnection)new NativeSocketConnection();
    socketConn.connect(host, port, propertySet, null, null, 0);
    init((Session)null, socketConn, propertySet, (TransactionEventHandler)null);
  }
  
  public void connect(String user, String password, String database) {
    this.currUser = user;
    this.currPassword = password;
    this.currDatabase = database;
    beforeHandshake();
    this.authProvider.connect(null, user, password, database);
  }
  
  public void changeUser(String user, String password, String database) {
    this.currUser = user;
    this.currPassword = password;
    this.currDatabase = database;
    this.authProvider.changeUser(null, user, password, database);
  }
  
  public void afterHandshake() {
    initServerSession();
  }
  
  public void configureTimezone() {}
  
  public void initServerSession() {
    configureTimezone();
    send(this.messageBuilder.buildSqlStatement("select @@mysqlx_max_allowed_packet"), 0);
    ColumnDefinition metadata = readMetadata();
    long count = ((Long)(new XProtocolRowInputStream(metadata, this, null)).next().getValue(0, (ValueFactory)new LongValueFactory(this.propertySet))).longValue();
    readQueryResult(new StatementExecuteOkBuilder());
    setMaxAllowedPacket((int)count);
  }
  
  public void readAuthenticateOk() {
    try {
      XMessage mess = (XMessage)this.reader.readMessage(null, 4);
      if (mess != null && mess.getNotices() != null)
        for (Notice notice : mess.getNotices()) {
          if (notice instanceof Notice.XSessionStateChanged)
            switch (((Notice.XSessionStateChanged)notice).getParamType().intValue()) {
              case 11:
                getServerSession().setThreadId(((Notice.XSessionStateChanged)notice).getValue().getVUnsignedInt());
            }  
        }  
    } catch (IOException e) {
      throw new XProtocolError(e.getMessage(), e);
    } 
  }
  
  public byte[] readAuthenticateContinue() {
    try {
      MysqlxSession.AuthenticateContinue msg = (MysqlxSession.AuthenticateContinue)((XMessage)this.reader.readMessage(null, 3)).getMessage();
      byte[] data = msg.getAuthData().toByteArray();
      if (data.length != 20)
        throw AssertionFailedException.shouldNotHappen("Salt length should be 20, but is " + data.length); 
      return data;
    } catch (IOException e) {
      throw new XProtocolError(e.getMessage(), e);
    } 
  }
  
  public boolean hasMoreResults() {
    try {
      XMessageHeader header;
      if ((header = (XMessageHeader)this.reader.readHeader()).getMessageType() == 16) {
        this.reader.readMessage(null, header);
        if (((XMessageHeader)this.reader.readHeader()).getMessageType() == 14)
          return false; 
        return true;
      } 
      return false;
    } catch (IOException e) {
      throw new XProtocolError(e.getMessage(), e);
    } 
  }
  
  public <T extends com.mysql.cj.QueryResult> T readQueryResult(ResultBuilder<T> resultBuilder) {
    try {
      boolean done = false;
      while (!done) {
        XMessageHeader header = (XMessageHeader)this.reader.readHeader();
        XMessage mess = (XMessage)this.reader.readMessage(null, header);
        Class<? extends GeneratedMessageV3> msgClass = (Class)mess.getMessage().getClass();
        if (Mysqlx.Error.class.equals(msgClass))
          throw new XProtocolError((Mysqlx.Error)Mysqlx.Error.class.cast(mess.getMessage())); 
        if (!this.messageToProtocolEntityFactory.containsKey(msgClass))
          throw new WrongArgumentException("Unhandled msg class (" + msgClass + ") + msg=" + mess.getMessage()); 
        List<Notice> notices;
        if ((notices = mess.getNotices()) != null)
          notices.stream().forEach(resultBuilder::addProtocolEntity); 
        done = resultBuilder.addProtocolEntity((ProtocolEntity)((ProtocolEntityFactory)this.messageToProtocolEntityFactory.get(msgClass)).createFromMessage(mess));
      } 
      return (T)resultBuilder.build();
    } catch (IOException e) {
      throw new XProtocolError(e.getMessage(), e);
    } 
  }
  
  public boolean hasResults() {
    try {
      return (((XMessageHeader)this.reader.readHeader()).getMessageType() == 12);
    } catch (IOException e) {
      throw new XProtocolError(e.getMessage(), e);
    } 
  }
  
  public void drainRows() {
    try {
      XMessageHeader header;
      while ((header = (XMessageHeader)this.reader.readHeader()).getMessageType() == 13)
        this.reader.readMessage(null, header); 
    } catch (XProtocolError e) {
      this.currentResultStreamer = null;
      throw e;
    } catch (IOException e) {
      this.currentResultStreamer = null;
      throw new XProtocolError(e.getMessage(), e);
    } 
  }
  
  public static Map<String, Integer> COLLATION_NAME_TO_COLLATION_INDEX = new HashMap<>();
  
  static {
    for (int i = 0; i < CharsetMapping.COLLATION_INDEX_TO_COLLATION_NAME.length; i++)
      COLLATION_NAME_TO_COLLATION_INDEX.put(CharsetMapping.COLLATION_INDEX_TO_COLLATION_NAME[i], Integer.valueOf(i)); 
  }
  
  public ColumnDefinition readMetadata() {
    try {
      List<MysqlxResultset.ColumnMetaData> fromServer = new LinkedList<>();
      while (true) {
        fromServer.add((MysqlxResultset.ColumnMetaData)((XMessage)this.reader.readMessage(null, 12)).getMessage());
        if (((XMessageHeader)this.reader.readHeader()).getMessageType() != 12) {
          ArrayList<Field> metadata = new ArrayList<>(fromServer.size());
          ProtocolEntityFactory<Field, XMessage> fieldFactory = (ProtocolEntityFactory<Field, XMessage>)this.messageToProtocolEntityFactory.get(MysqlxResultset.ColumnMetaData.class);
          fromServer.forEach(col -> metadata.add(fieldFactory.createFromMessage(new XMessage((Message)col))));
          return (ColumnDefinition)new DefaultColumnDefinition(metadata.<Field>toArray(new Field[0]));
        } 
      } 
    } catch (IOException e) {
      throw new XProtocolError(e.getMessage(), e);
    } 
  }
  
  public ColumnDefinition readMetadata(Field f, Consumer<Notice> noticeConsumer) {
    try {
      List<MysqlxResultset.ColumnMetaData> fromServer = new LinkedList<>();
      while (((XMessageHeader)this.reader.readHeader()).getMessageType() == 12) {
        XMessage mess = (XMessage)this.reader.readMessage(null, 12);
        List<Notice> notices;
        if (noticeConsumer != null && (notices = mess.getNotices()) != null)
          notices.stream().forEach(noticeConsumer::accept); 
        fromServer.add((MysqlxResultset.ColumnMetaData)mess.getMessage());
      } 
      ArrayList<Field> metadata = new ArrayList<>(fromServer.size());
      metadata.add(f);
      ProtocolEntityFactory<Field, XMessage> fieldFactory = (ProtocolEntityFactory<Field, XMessage>)this.messageToProtocolEntityFactory.get(MysqlxResultset.ColumnMetaData.class);
      fromServer.forEach(col -> metadata.add(fieldFactory.createFromMessage(new XMessage((Message)col))));
      return (ColumnDefinition)new DefaultColumnDefinition(metadata.<Field>toArray(new Field[0]));
    } catch (IOException e) {
      throw new XProtocolError(e.getMessage(), e);
    } 
  }
  
  public XProtocolRow readRowOrNull(ColumnDefinition metadata, Consumer<Notice> noticeConsumer) {
    try {
      XMessageHeader header;
      if ((header = (XMessageHeader)this.reader.readHeader()).getMessageType() == 13) {
        XMessage mess = (XMessage)this.reader.readMessage(null, header);
        List<Notice> notices;
        if (noticeConsumer != null && (notices = mess.getNotices()) != null)
          notices.stream().forEach(noticeConsumer::accept); 
        XProtocolRow res = new XProtocolRow((MysqlxResultset.Row)mess.getMessage());
        res.setMetadata(metadata);
        return res;
      } 
      return null;
    } catch (XProtocolError e) {
      this.currentResultStreamer = null;
      throw e;
    } catch (IOException e) {
      this.currentResultStreamer = null;
      throw new XProtocolError(e.getMessage(), e);
    } 
  }
  
  public boolean supportsPreparedStatements() {
    return this.supportsPreparedStatements;
  }
  
  public boolean readyForPreparingStatements() {
    if (this.retryPrepareStatementCountdown == 0)
      return true; 
    this.retryPrepareStatementCountdown--;
    return false;
  }
  
  public int getNewPreparedStatementId(PreparableStatement<?> preparableStatement) {
    if (!this.supportsPreparedStatements)
      throw new XProtocolError("The connected MySQL server does not support prepared statements."); 
    int preparedStatementId = this.preparedStatementIds.allocateSequentialId();
    this.preparableStatementFinalizerReferences.put(Integer.valueOf(preparedStatementId), new PreparableStatement.PreparableStatementFinalizer(preparableStatement, this.preparableStatementRefQueue, preparedStatementId));
    return preparedStatementId;
  }
  
  public void freePreparedStatementId(int preparedStatementId) {
    if (!this.supportsPreparedStatements)
      throw new XProtocolError("The connected MySQL server does not support prepared statements."); 
    this.preparedStatementIds.releaseSequentialId(preparedStatementId);
    this.preparableStatementFinalizerReferences.remove(Integer.valueOf(preparedStatementId));
  }
  
  public boolean failedPreparingStatement(int preparedStatementId, XProtocolError e) {
    freePreparedStatementId(preparedStatementId);
    if (e.getErrorCode() == 1461) {
      this.retryPrepareStatementCountdown = RETRY_PREPARE_STATEMENT_COUNTDOWN;
      return true;
    } 
    if (e.getErrorCode() == 1047 && this.preparableStatementFinalizerReferences.isEmpty()) {
      this.supportsPreparedStatements = false;
      this.retryPrepareStatementCountdown = 0;
      this.preparedStatementIds = null;
      this.preparableStatementRefQueue = null;
      this.preparableStatementFinalizerReferences = null;
      return true;
    } 
    return false;
  }
  
  protected void newCommand() {
    if (this.currentResultStreamer != null)
      try {
        this.currentResultStreamer.finishStreaming();
      } finally {
        this.currentResultStreamer = null;
      }  
    if (this.supportsPreparedStatements) {
      Reference<? extends PreparableStatement<?>> ref;
      while ((ref = this.preparableStatementRefQueue.poll()) != null) {
        PreparableStatement.PreparableStatementFinalizer psf = (PreparableStatement.PreparableStatementFinalizer)ref;
        psf.clear();
        try {
          this.sender.send(((XMessageBuilder)this.messageBuilder).buildPrepareDeallocate(psf.getPreparedStatementId()));
          readQueryResult(new OkBuilder());
        } catch (XProtocolError e) {
          if (e.getErrorCode() != 5110)
            throw e; 
        } finally {
          freePreparedStatementId(psf.getPreparedStatementId());
        } 
      } 
    } 
  }
  
  public <M extends Message, R extends com.mysql.cj.QueryResult> R query(M message, ResultBuilder<R> resultBuilder) {
    send((Message)message, 0);
    R res = readQueryResult(resultBuilder);
    if (ResultStreamer.class.isAssignableFrom(res.getClass()))
      this.currentResultStreamer = (ResultStreamer)res; 
    return res;
  }
  
  public <M extends Message, R extends com.mysql.cj.QueryResult> CompletableFuture<R> queryAsync(M message, ResultBuilder<R> resultBuilder) {
    newCommand();
    CompletableFuture<R> f = new CompletableFuture<>();
    MessageListener<XMessage> l = new ResultMessageListener<>(this.messageToProtocolEntityFactory, resultBuilder, f);
    this.sender.send((XMessage)message, f, () -> this.reader.pushMessageListener(l));
    return f;
  }
  
  public boolean isOpen() {
    return (this.managedResource != null);
  }
  
  public void close() throws IOException {
    try {
      send(this.messageBuilder.buildClose(), 0);
      readQueryResult(new OkBuilder());
    } catch (Exception exception) {
      try {
        if (this.managedResource == null)
          throw new ConnectionIsClosedException(); 
        this.managedResource.close();
        this.managedResource = null;
      } catch (IOException ex) {
        throw new CJCommunicationsException(ex);
      } 
    } finally {
      try {
        if (this.managedResource == null)
          throw new ConnectionIsClosedException(); 
        this.managedResource.close();
        this.managedResource = null;
      } catch (IOException ex) {
        throw new CJCommunicationsException(ex);
      } 
    } 
  }
  
  public boolean isSqlResultPending() {
    try {
      XMessageHeader header;
      switch ((header = (XMessageHeader)this.reader.readHeader()).getMessageType()) {
        case 12:
          return true;
        case 16:
          this.reader.readMessage(null, header);
          break;
      } 
      return false;
    } catch (IOException e) {
      throw new XProtocolError(e.getMessage(), e);
    } 
  }
  
  public void setMaxAllowedPacket(int maxAllowedPacket) {
    this.sender.setMaxAllowedPacket(maxAllowedPacket);
  }
  
  public void send(Message message, int packetLen) {
    newCommand();
    this.sender.send(message);
  }
  
  public ServerCapabilities readServerCapabilities() {
    try {
      this.sender.send(((XMessageBuilder)this.messageBuilder).buildCapabilitiesGet());
      return new XServerCapabilities((Map<String, MysqlxDatatypes.Any>)((MysqlxConnection.Capabilities)((XMessage)this.reader.readMessage(null, 2)).getMessage())
          .getCapabilitiesList().stream().collect(Collectors.toMap(MysqlxConnection.Capability::getName, MysqlxConnection.Capability::getValue)));
    } catch (IOException|AssertionFailedException e) {
      throw new XProtocolError(e.getMessage(), e);
    } 
  }
  
  public void reset() {
    newCommand();
    this.propertySet.reset();
    if (this.useSessionResetKeepOpen == null)
      try {
        send(((XMessageBuilder)this.messageBuilder).buildExpectOpen(), 0);
        readQueryResult(new OkBuilder());
        this.useSessionResetKeepOpen = Boolean.valueOf(true);
      } catch (XProtocolError e) {
        if (e.getErrorCode() != 5168 && e.getErrorCode() != 5160)
          throw e; 
        this.useSessionResetKeepOpen = Boolean.valueOf(false);
      }  
    if (this.useSessionResetKeepOpen.booleanValue()) {
      send(((XMessageBuilder)this.messageBuilder).buildSessionResetKeepOpen(), 0);
      readQueryResult(new OkBuilder());
    } else {
      send(((XMessageBuilder)this.messageBuilder).buildSessionResetAndClose(), 0);
      readQueryResult(new OkBuilder());
      if (this.clientCapabilities.containsKey(XServerCapabilities.KEY_SESSION_CONNECT_ATTRS)) {
        Map<String, Object> reducedClientCapabilities = new HashMap<>();
        reducedClientCapabilities.put(XServerCapabilities.KEY_SESSION_CONNECT_ATTRS, this.clientCapabilities
            .get(XServerCapabilities.KEY_SESSION_CONNECT_ATTRS));
        if (reducedClientCapabilities.size() > 0)
          sendCapabilities(reducedClientCapabilities); 
      } 
      this.authProvider.changeUser(null, this.currUser, this.currPassword, this.currDatabase);
    } 
    if (this.supportsPreparedStatements) {
      this.retryPrepareStatementCountdown = 0;
      this.preparedStatementIds = new SequentialIdLease();
      this.preparableStatementRefQueue = new ReferenceQueue<>();
      this.preparableStatementFinalizerReferences = new TreeMap<>();
    } 
  }
  
  public ExceptionInterceptor getExceptionInterceptor() {
    throw (CJOperationNotSupportedException)ExceptionFactory.createException(CJOperationNotSupportedException.class, "Not supported");
  }
  
  public void changeDatabase(String database) {
    throw (CJOperationNotSupportedException)ExceptionFactory.createException(CJOperationNotSupportedException.class, "Not supported");
  }
  
  public String getPasswordCharacterEncoding() {
    throw (CJOperationNotSupportedException)ExceptionFactory.createException(CJOperationNotSupportedException.class, "Not supported");
  }
  
  public boolean versionMeetsMinimum(int major, int minor, int subminor) {
    throw (CJOperationNotSupportedException)ExceptionFactory.createException(CJOperationNotSupportedException.class, "Not supported");
  }
  
  public XMessage readMessage(XMessage reuse) {
    throw (CJOperationNotSupportedException)ExceptionFactory.createException(CJOperationNotSupportedException.class, "Not supported");
  }
  
  public XMessage checkErrorMessage() {
    throw (CJOperationNotSupportedException)ExceptionFactory.createException(CJOperationNotSupportedException.class, "Not supported");
  }
  
  public XMessage sendCommand(Message queryPacket, boolean skipCheck, int timeoutMillis) {
    throw (CJOperationNotSupportedException)ExceptionFactory.createException(CJOperationNotSupportedException.class, "Not supported");
  }
  
  public <T extends ProtocolEntity> T read(Class<T> requiredClass, ProtocolEntityFactory<T, XMessage> protocolEntityFactory) throws IOException {
    throw (CJOperationNotSupportedException)ExceptionFactory.createException(CJOperationNotSupportedException.class, "Not supported");
  }
  
  public <T extends ProtocolEntity> T read(Class<Resultset> requiredClass, int maxRows, boolean streamResults, XMessage resultPacket, boolean isBinaryEncoded, ColumnDefinition metadata, ProtocolEntityFactory<T, XMessage> protocolEntityFactory) throws IOException {
    throw (CJOperationNotSupportedException)ExceptionFactory.createException(CJOperationNotSupportedException.class, "Not supported");
  }
  
  public void setLocalInfileInputStream(InputStream stream) {
    throw (CJOperationNotSupportedException)ExceptionFactory.createException(CJOperationNotSupportedException.class, "Not supported");
  }
  
  public InputStream getLocalInfileInputStream() {
    throw (CJOperationNotSupportedException)ExceptionFactory.createException(CJOperationNotSupportedException.class, "Not supported");
  }
  
  public String getQueryComment() {
    throw (CJOperationNotSupportedException)ExceptionFactory.createException(CJOperationNotSupportedException.class, "Not supported");
  }
  
  public void setQueryComment(String comment) {
    throw (CJOperationNotSupportedException)ExceptionFactory.createException(CJOperationNotSupportedException.class, "Not supported");
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\protocol\x\XProtocol.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
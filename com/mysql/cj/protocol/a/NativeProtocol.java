package com.mysql.cj.protocol.a;

import com.mysql.cj.Constants;
import com.mysql.cj.MessageBuilder;
import com.mysql.cj.Messages;
import com.mysql.cj.MysqlType;
import com.mysql.cj.Query;
import com.mysql.cj.ServerVersion;
import com.mysql.cj.Session;
import com.mysql.cj.TransactionEventHandler;
import com.mysql.cj.conf.PropertyKey;
import com.mysql.cj.conf.PropertySet;
import com.mysql.cj.conf.RuntimeProperty;
import com.mysql.cj.exceptions.CJConnectionFeatureNotAvailableException;
import com.mysql.cj.exceptions.CJException;
import com.mysql.cj.exceptions.CJOperationNotSupportedException;
import com.mysql.cj.exceptions.CJPacketTooBigException;
import com.mysql.cj.exceptions.ClosedOnExpiredPasswordException;
import com.mysql.cj.exceptions.DataTruncationException;
import com.mysql.cj.exceptions.ExceptionFactory;
import com.mysql.cj.exceptions.FeatureNotAvailableException;
import com.mysql.cj.exceptions.MysqlErrorNumbers;
import com.mysql.cj.exceptions.PasswordExpiredException;
import com.mysql.cj.exceptions.WrongArgumentException;
import com.mysql.cj.interceptors.QueryInterceptor;
import com.mysql.cj.jdbc.exceptions.MysqlDataTruncation;
import com.mysql.cj.log.BaseMetricsHolder;
import com.mysql.cj.log.Log;
import com.mysql.cj.log.ProfilerEventHandler;
import com.mysql.cj.protocol.AbstractProtocol;
import com.mysql.cj.protocol.AuthenticationProvider;
import com.mysql.cj.protocol.ColumnDefinition;
import com.mysql.cj.protocol.ExportControlled;
import com.mysql.cj.protocol.FullReadInputStream;
import com.mysql.cj.protocol.Message;
import com.mysql.cj.protocol.MessageReader;
import com.mysql.cj.protocol.MessageSender;
import com.mysql.cj.protocol.PacketReceivedTimeHolder;
import com.mysql.cj.protocol.PacketSentTimeHolder;
import com.mysql.cj.protocol.Protocol;
import com.mysql.cj.protocol.ProtocolEntity;
import com.mysql.cj.protocol.ProtocolEntityFactory;
import com.mysql.cj.protocol.ProtocolEntityReader;
import com.mysql.cj.protocol.ResultBuilder;
import com.mysql.cj.protocol.Resultset;
import com.mysql.cj.protocol.ResultsetRow;
import com.mysql.cj.protocol.ResultsetRows;
import com.mysql.cj.protocol.ServerCapabilities;
import com.mysql.cj.protocol.ServerSession;
import com.mysql.cj.protocol.SocketConnection;
import com.mysql.cj.protocol.a.result.OkPacket;
import com.mysql.cj.result.Field;
import com.mysql.cj.result.IntegerValueFactory;
import com.mysql.cj.result.Row;
import com.mysql.cj.result.StringValueFactory;
import com.mysql.cj.result.ValueFactory;
import com.mysql.cj.util.LazyString;
import com.mysql.cj.util.StringUtils;
import com.mysql.cj.util.TestUtils;
import com.mysql.cj.util.TimeUtil;
import com.mysql.cj.util.Util;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.lang.ref.SoftReference;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.sql.SQLWarning;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TimeZone;
import java.util.function.Supplier;

public class NativeProtocol extends AbstractProtocol<NativePacketPayload> implements Protocol<NativePacketPayload>, RuntimeProperty.RuntimePropertyListener {
  protected static final int INITIAL_PACKET_SIZE = 1024;
  
  protected static final int COMP_HEADER_LENGTH = 3;
  
  protected static final int MAX_QUERY_SIZE_TO_EXPLAIN = 1048576;
  
  private static final String EXPLAINABLE_STATEMENT = "SELECT";
  
  private static final String[] EXPLAINABLE_STATEMENT_EXTENSION = new String[] { "INSERT", "UPDATE", "REPLACE", "DELETE" };
  
  protected MessageSender<NativePacketPayload> packetSender;
  
  protected MessageReader<NativePacketHeader, NativePacketPayload> packetReader;
  
  protected NativeServerSession serverSession;
  
  protected CompressedPacketSender compressedPacketSender;
  
  protected NativePacketPayload sharedSendPacket = null;
  
  protected NativePacketPayload reusablePacket = null;
  
  private SoftReference<NativePacketPayload> loadFileBufRef;
  
  protected byte packetSequence = 0;
  
  protected boolean useCompression = false;
  
  private RuntimeProperty<Integer> maxAllowedPacket;
  
  private RuntimeProperty<Boolean> useServerPrepStmts;
  
  private boolean autoGenerateTestcaseScript;
  
  private boolean logSlowQueries = false;
  
  private boolean useAutoSlowLog;
  
  private boolean profileSQL = false;
  
  private long slowQueryThreshold;
  
  private int commandCount = 0;
  
  protected boolean hadWarnings = false;
  
  private int warningCount = 0;
  
  protected Map<Class<? extends ProtocolEntity>, ProtocolEntityReader<? extends ProtocolEntity, ? extends Message>> PROTOCOL_ENTITY_CLASS_TO_TEXT_READER;
  
  protected Map<Class<? extends ProtocolEntity>, ProtocolEntityReader<? extends ProtocolEntity, ? extends Message>> PROTOCOL_ENTITY_CLASS_TO_BINARY_READER;
  
  protected boolean platformDbCharsetMatches = true;
  
  private int statementExecutionDepth = 0;
  
  private List<QueryInterceptor> queryInterceptors;
  
  private RuntimeProperty<Boolean> maintainTimeStats;
  
  private RuntimeProperty<Integer> maxQuerySizeToLog;
  
  private InputStream localInfileInputStream;
  
  private BaseMetricsHolder metricsHolder;
  
  private String queryComment = null;
  
  private static String jvmPlatformCharset = null;
  
  private NativeMessageBuilder commandBuilder = new NativeMessageBuilder();
  
  private ResultsetRows streamingData;
  
  static {
    OutputStreamWriter outWriter = null;
    try {
      outWriter = new OutputStreamWriter(new ByteArrayOutputStream());
      jvmPlatformCharset = outWriter.getEncoding();
    } finally {
      try {
        if (outWriter != null)
          outWriter.close(); 
      } catch (IOException iOException) {}
    } 
  }
  
  public static NativeProtocol getInstance(Session session, SocketConnection socketConnection, PropertySet propertySet, Log log, TransactionEventHandler transactionManager) {
    NativeProtocol protocol = new NativeProtocol(log);
    protocol.init(session, socketConnection, propertySet, transactionManager);
    return protocol;
  }
  
  public void init(Session sess, SocketConnection phConnection, PropertySet propSet, TransactionEventHandler trManager) {
    super.init(sess, phConnection, propSet, trManager);
    this.maintainTimeStats = this.propertySet.getBooleanProperty(PropertyKey.maintainTimeStats);
    this.maxQuerySizeToLog = this.propertySet.getIntegerProperty(PropertyKey.maxQuerySizeToLog);
    this.useAutoSlowLog = ((Boolean)this.propertySet.getBooleanProperty(PropertyKey.autoSlowLog).getValue()).booleanValue();
    this.logSlowQueries = ((Boolean)this.propertySet.getBooleanProperty(PropertyKey.logSlowQueries).getValue()).booleanValue();
    this.maxAllowedPacket = this.propertySet.getIntegerProperty(PropertyKey.maxAllowedPacket);
    this.profileSQL = ((Boolean)this.propertySet.getBooleanProperty(PropertyKey.profileSQL).getValue()).booleanValue();
    this.autoGenerateTestcaseScript = ((Boolean)this.propertySet.getBooleanProperty(PropertyKey.autoGenerateTestcaseScript).getValue()).booleanValue();
    this.useServerPrepStmts = this.propertySet.getBooleanProperty(PropertyKey.useServerPrepStmts);
    this.reusablePacket = new NativePacketPayload(1024);
    try {
      this.packetSender = new SimplePacketSender(this.socketConnection.getMysqlOutput());
      this.packetReader = new SimplePacketReader(this.socketConnection, this.maxAllowedPacket);
    } catch (IOException ioEx) {
      throw ExceptionFactory.createCommunicationsException(this.propertySet, this.serverSession, getPacketSentTimeHolder(), 
          getPacketReceivedTimeHolder(), ioEx, getExceptionInterceptor());
    } 
    if (((Boolean)this.propertySet.getBooleanProperty(PropertyKey.logSlowQueries).getValue()).booleanValue())
      calculateSlowQueryThreshold(); 
    this.authProvider = new NativeAuthenticationProvider();
    this.authProvider.init(this, getPropertySet(), this.socketConnection.getExceptionInterceptor());
    Map<Class<? extends ProtocolEntity>, ProtocolEntityReader<? extends ProtocolEntity, NativePacketPayload>> protocolEntityClassToTextReader = new HashMap<>();
    protocolEntityClassToTextReader.put(ColumnDefinition.class, new ColumnDefinitionReader(this));
    protocolEntityClassToTextReader.put(ResultsetRow.class, new ResultsetRowReader(this));
    protocolEntityClassToTextReader.put(Resultset.class, new TextResultsetReader(this));
    this.PROTOCOL_ENTITY_CLASS_TO_TEXT_READER = Collections.unmodifiableMap(protocolEntityClassToTextReader);
    Map<Class<? extends ProtocolEntity>, ProtocolEntityReader<? extends ProtocolEntity, NativePacketPayload>> protocolEntityClassToBinaryReader = new HashMap<>();
    protocolEntityClassToBinaryReader.put(ColumnDefinition.class, new ColumnDefinitionReader(this));
    protocolEntityClassToBinaryReader.put(Resultset.class, new BinaryResultsetReader(this));
    this.PROTOCOL_ENTITY_CLASS_TO_BINARY_READER = Collections.unmodifiableMap(protocolEntityClassToBinaryReader);
  }
  
  public MessageBuilder<NativePacketPayload> getMessageBuilder() {
    throw (CJOperationNotSupportedException)ExceptionFactory.createException(CJOperationNotSupportedException.class, "Not supported");
  }
  
  public MessageSender<NativePacketPayload> getPacketSender() {
    return this.packetSender;
  }
  
  public MessageReader<NativePacketHeader, NativePacketPayload> getPacketReader() {
    return this.packetReader;
  }
  
  public void negotiateSSLConnection(int packLength) {
    if (!ExportControlled.enabled())
      throw new CJConnectionFeatureNotAvailableException(getPropertySet(), this.serverSession, getPacketSentTimeHolder(), null); 
    long clientParam = this.serverSession.getClientParam();
    clientParam |= 0x800L;
    this.serverSession.setClientParam(clientParam);
    NativePacketPayload packet = new NativePacketPayload(packLength);
    packet.writeInteger(NativeConstants.IntegerDataType.INT4, clientParam);
    packet.writeInteger(NativeConstants.IntegerDataType.INT4, 16777215L);
    packet.writeInteger(NativeConstants.IntegerDataType.INT1, AuthenticationProvider.getCharsetForHandshake(this.authProvider.getEncodingForHandshake(), this.serverSession
          .getCapabilities().getServerVersion()));
    packet.writeBytes(NativeConstants.StringLengthDataType.STRING_FIXED, new byte[23]);
    send(packet, packet.getPosition());
    try {
      this.socketConnection.performTlsHandshake(this.serverSession);
      this.packetSender = new SimplePacketSender(this.socketConnection.getMysqlOutput());
      this.packetReader = new SimplePacketReader(this.socketConnection, this.maxAllowedPacket);
    } catch (FeatureNotAvailableException nae) {
      throw new CJConnectionFeatureNotAvailableException(getPropertySet(), this.serverSession, getPacketSentTimeHolder(), nae);
    } catch (IOException ioEx) {
      throw ExceptionFactory.createCommunicationsException(this.propertySet, this.serverSession, getPacketSentTimeHolder(), 
          getPacketReceivedTimeHolder(), ioEx, getExceptionInterceptor());
    } 
  }
  
  public void rejectProtocol(NativePacketPayload msg) {
    try {
      this.socketConnection.getMysqlSocket().close();
    } catch (Exception exception) {}
    int errno = 2000;
    NativePacketPayload buf = msg;
    buf.setPosition(1);
    errno = (int)buf.readInteger(NativeConstants.IntegerDataType.INT2);
    String serverErrorMessage = "";
    try {
      serverErrorMessage = buf.readString(NativeConstants.StringSelfDataType.STRING_TERM, "ASCII");
    } catch (Exception exception) {}
    StringBuilder errorBuf = new StringBuilder(Messages.getString("Protocol.0"));
    errorBuf.append(serverErrorMessage);
    errorBuf.append("\"");
    String xOpen = MysqlErrorNumbers.mysqlToSqlState(errno);
    throw ExceptionFactory.createException(MysqlErrorNumbers.get(xOpen) + ", " + errorBuf.toString(), xOpen, errno, false, null, getExceptionInterceptor());
  }
  
  public void beforeHandshake() {
    this.packetReader.resetMessageSequence();
    this.serverSession = new NativeServerSession(this.propertySet);
    NativeCapabilities capabilities = readServerCapabilities();
    this.serverSession.setCapabilities(capabilities);
  }
  
  public void afterHandshake() {
    checkTransactionState();
    PropertySet pset = getPropertySet();
    try {
      if ((this.serverSession.getCapabilities().getCapabilityFlags() & 0x20) != 0 && ((Boolean)pset
        .getBooleanProperty(PropertyKey.useCompression).getValue()).booleanValue() && 
        !(this.socketConnection.getMysqlInput().getUnderlyingStream() instanceof CompressedInputStream)) {
        this.useCompression = true;
        this.socketConnection.setMysqlInput(new FullReadInputStream(new CompressedInputStream((InputStream)this.socketConnection
                .getMysqlInput(), pset.getBooleanProperty(PropertyKey.traceProtocol), this.log)));
        this.compressedPacketSender = new CompressedPacketSender(this.socketConnection.getMysqlOutput());
        this.packetSender = this.compressedPacketSender;
      } 
      applyPacketDecorators(this.packetSender, this.packetReader);
      this.socketConnection.getSocketFactory().afterHandshake();
    } catch (IOException ioEx) {
      throw ExceptionFactory.createCommunicationsException(getPropertySet(), this.serverSession, getPacketSentTimeHolder(), 
          getPacketReceivedTimeHolder(), ioEx, getExceptionInterceptor());
    } 
    this.maintainTimeStats.addListener(this);
    pset.getBooleanProperty(PropertyKey.traceProtocol).addListener(this);
    pset.getBooleanProperty(PropertyKey.enablePacketDebug).addListener(this);
  }
  
  public void handlePropertyChange(RuntimeProperty<?> prop) {
    switch (prop.getPropertyDefinition().getPropertyKey()) {
      case maintainTimeStats:
      case traceProtocol:
      case enablePacketDebug:
        applyPacketDecorators(this.packetSender.undecorateAll(), this.packetReader.undecorateAll());
        break;
    } 
  }
  
  public void applyPacketDecorators(MessageSender<NativePacketPayload> sender, MessageReader<NativePacketHeader, NativePacketPayload> messageReader) {
    TimeTrackingPacketSender ttSender = null;
    TimeTrackingPacketReader ttReader = null;
    LinkedList<StringBuilder> debugRingBuffer = null;
    if (((Boolean)this.maintainTimeStats.getValue()).booleanValue()) {
      ttSender = new TimeTrackingPacketSender(sender);
      sender = ttSender;
      ttReader = new TimeTrackingPacketReader(messageReader);
      messageReader = ttReader;
    } 
    if (((Boolean)this.propertySet.getBooleanProperty(PropertyKey.traceProtocol).getValue()).booleanValue()) {
      sender = new TracingPacketSender(sender, this.log, this.socketConnection.getHost(), getServerSession().getCapabilities().getThreadId());
      messageReader = new TracingPacketReader(messageReader, this.log);
    } 
    if (((Boolean)getPropertySet().getBooleanProperty(PropertyKey.enablePacketDebug).getValue()).booleanValue()) {
      debugRingBuffer = new LinkedList<>();
      sender = new DebugBufferingPacketSender(sender, debugRingBuffer, this.propertySet.getIntegerProperty(PropertyKey.packetDebugBufferSize));
      messageReader = new DebugBufferingPacketReader(messageReader, debugRingBuffer, this.propertySet.getIntegerProperty(PropertyKey.packetDebugBufferSize));
    } 
    messageReader = new MultiPacketReader(messageReader);
    synchronized (this.packetReader) {
      this.packetReader = messageReader;
      this.packetDebugRingBuffer = debugRingBuffer;
      setPacketSentTimeHolder((ttSender != null) ? ttSender : new PacketSentTimeHolder() {
          
          });
    } 
    synchronized (this.packetSender) {
      this.packetSender = sender;
      setPacketReceivedTimeHolder((ttReader != null) ? ttReader : new PacketReceivedTimeHolder() {
          
          });
    } 
  }
  
  public NativeCapabilities readServerCapabilities() {
    NativePacketPayload buf = readMessage((NativePacketPayload)null);
    if (buf.isErrorPacket())
      rejectProtocol(buf); 
    NativeCapabilities serverCapabilities = new NativeCapabilities();
    serverCapabilities.setInitialHandshakePacket(buf);
    return serverCapabilities;
  }
  
  public NativeServerSession getServerSession() {
    return this.serverSession;
  }
  
  public void changeDatabase(String database) {
    if (database == null || database.length() == 0)
      return; 
    try {
      sendCommand(this.commandBuilder.buildComInitDb(getSharedSendPacket(), database), false, 0);
    } catch (CJException ex) {
      if (((Boolean)getPropertySet().getBooleanProperty(PropertyKey.createDatabaseIfNotExist).getValue()).booleanValue()) {
        sendCommand(this.commandBuilder.buildComQuery(getSharedSendPacket(), "CREATE DATABASE IF NOT EXISTS " + database), false, 0);
        sendCommand(this.commandBuilder.buildComInitDb(getSharedSendPacket(), database), false, 0);
      } else {
        throw ExceptionFactory.createCommunicationsException(getPropertySet(), this.serverSession, getPacketSentTimeHolder(), 
            getPacketReceivedTimeHolder(), ex, getExceptionInterceptor());
      } 
    } 
  }
  
  public final NativePacketPayload readMessage(NativePacketPayload reuse) {
    try {
      NativePacketHeader header = (NativePacketHeader)this.packetReader.readHeader();
      NativePacketPayload buf = (NativePacketPayload)this.packetReader.readMessage(Optional.ofNullable(reuse), header);
      this.packetSequence = header.getMessageSequence();
      return buf;
    } catch (IOException ioEx) {
      throw ExceptionFactory.createCommunicationsException(this.propertySet, this.serverSession, getPacketSentTimeHolder(), 
          getPacketReceivedTimeHolder(), ioEx, getExceptionInterceptor());
    } catch (OutOfMemoryError oom) {
      throw ExceptionFactory.createException(oom.getMessage(), "HY001", 0, false, oom, this.exceptionInterceptor);
    } 
  }
  
  public final void send(Message packet, int packetLen) {
    try {
      if (((Integer)this.maxAllowedPacket.getValue()).intValue() > 0 && packetLen > ((Integer)this.maxAllowedPacket.getValue()).intValue())
        throw new CJPacketTooBigException(packetLen, ((Integer)this.maxAllowedPacket.getValue()).intValue()); 
      this.packetSequence = (byte)(this.packetSequence + 1);
      this.packetSender.send(packet.getByteBuffer(), packetLen, this.packetSequence);
      if (packet == this.sharedSendPacket)
        reclaimLargeSharedSendPacket(); 
    } catch (IOException ioEx) {
      throw ExceptionFactory.createCommunicationsException(getPropertySet(), this.serverSession, getPacketSentTimeHolder(), 
          getPacketReceivedTimeHolder(), ioEx, getExceptionInterceptor());
    } 
  }
  
  public final NativePacketPayload sendCommand(Message queryPacket, boolean skipCheck, int timeoutMillis) {
    int command = queryPacket.getByteBuffer()[0];
    this.commandCount++;
    if (this.queryInterceptors != null) {
      NativePacketPayload interceptedPacketPayload = (NativePacketPayload)invokeQueryInterceptorsPre(queryPacket, false);
      if (interceptedPacketPayload != null)
        return interceptedPacketPayload; 
    } 
    this.packetReader.resetMessageSequence();
    int oldTimeout = 0;
    if (timeoutMillis != 0)
      try {
        oldTimeout = this.socketConnection.getMysqlSocket().getSoTimeout();
        this.socketConnection.getMysqlSocket().setSoTimeout(timeoutMillis);
      } catch (IOException e) {
        throw ExceptionFactory.createCommunicationsException(this.propertySet, this.serverSession, getPacketSentTimeHolder(), 
            getPacketReceivedTimeHolder(), e, getExceptionInterceptor());
      }  
    try {
      checkForOutstandingStreamingData();
      this.serverSession.setStatusFlags(0, true);
      this.hadWarnings = false;
      setWarningCount(0);
      if (this.useCompression) {
        int bytesLeft = this.socketConnection.getMysqlInput().available();
        if (bytesLeft > 0)
          this.socketConnection.getMysqlInput().skip(bytesLeft); 
      } 
      try {
        clearInputStream();
        this.packetSequence = -1;
        send(queryPacket, queryPacket.getPosition());
      } catch (CJException ex) {
        throw ex;
      } catch (Exception ex) {
        throw ExceptionFactory.createCommunicationsException(this.propertySet, this.serverSession, getPacketSentTimeHolder(), 
            getPacketReceivedTimeHolder(), ex, getExceptionInterceptor());
      } 
      NativePacketPayload returnPacket = null;
      if (!skipCheck) {
        if (command == 23 || command == 26)
          this.packetReader.resetMessageSequence(); 
        returnPacket = checkErrorMessage(command);
        if (this.queryInterceptors != null)
          returnPacket = (NativePacketPayload)invokeQueryInterceptorsPost(queryPacket, returnPacket, false); 
      } 
      return returnPacket;
    } catch (IOException ioEx) {
      this.serverSession.preserveOldTransactionState();
      throw ExceptionFactory.createCommunicationsException(this.propertySet, this.serverSession, getPacketSentTimeHolder(), 
          getPacketReceivedTimeHolder(), ioEx, getExceptionInterceptor());
    } catch (CJException e) {
      this.serverSession.preserveOldTransactionState();
      throw e;
    } finally {
      if (timeoutMillis != 0)
        try {
          this.socketConnection.getMysqlSocket().setSoTimeout(oldTimeout);
        } catch (IOException e) {
          throw ExceptionFactory.createCommunicationsException(this.propertySet, this.serverSession, getPacketSentTimeHolder(), 
              getPacketReceivedTimeHolder(), e, getExceptionInterceptor());
        }  
    } 
  }
  
  public void checkTransactionState() {
    int transState = this.serverSession.getTransactionState();
    if (transState == 3) {
      this.transactionManager.transactionCompleted();
    } else if (transState == 2) {
      this.transactionManager.transactionBegun();
    } 
  }
  
  public NativePacketPayload checkErrorMessage() {
    return checkErrorMessage(-1);
  }
  
  private NativePacketPayload checkErrorMessage(int command) {
    NativePacketPayload resultPacket = null;
    this.serverSession.setStatusFlags(0);
    try {
      resultPacket = readMessage(this.reusablePacket);
    } catch (CJException ex) {
      throw ex;
    } catch (Exception fallThru) {
      throw ExceptionFactory.createCommunicationsException(this.propertySet, this.serverSession, getPacketSentTimeHolder(), 
          getPacketReceivedTimeHolder(), fallThru, getExceptionInterceptor());
    } 
    checkErrorMessage(resultPacket);
    return resultPacket;
  }
  
  public void checkErrorMessage(NativePacketPayload resultPacket) {
    resultPacket.setPosition(0);
    byte statusCode = (byte)(int)resultPacket.readInteger(NativeConstants.IntegerDataType.INT1);
    if (statusCode == -1) {
      int errno = 2000;
      errno = (int)resultPacket.readInteger(NativeConstants.IntegerDataType.INT2);
      String xOpen = null;
      String serverErrorMessage = resultPacket.readString(NativeConstants.StringSelfDataType.STRING_TERM, this.serverSession.getErrorMessageEncoding());
      if (serverErrorMessage.charAt(0) == '#') {
        if (serverErrorMessage.length() > 6) {
          xOpen = serverErrorMessage.substring(1, 6);
          serverErrorMessage = serverErrorMessage.substring(6);
          if (xOpen.equals("HY000"))
            xOpen = MysqlErrorNumbers.mysqlToSqlState(errno); 
        } else {
          xOpen = MysqlErrorNumbers.mysqlToSqlState(errno);
        } 
      } else {
        xOpen = MysqlErrorNumbers.mysqlToSqlState(errno);
      } 
      clearInputStream();
      StringBuilder errorBuf = new StringBuilder();
      String xOpenErrorMessage = MysqlErrorNumbers.get(xOpen);
      boolean useOnlyServerErrorMessages = ((Boolean)this.propertySet.getBooleanProperty(PropertyKey.useOnlyServerErrorMessages).getValue()).booleanValue();
      if (!useOnlyServerErrorMessages && 
        xOpenErrorMessage != null) {
        errorBuf.append(xOpenErrorMessage);
        errorBuf.append(Messages.getString("Protocol.0"));
      } 
      errorBuf.append(serverErrorMessage);
      if (!useOnlyServerErrorMessages && 
        xOpenErrorMessage != null)
        errorBuf.append("\""); 
      appendDeadlockStatusInformation(this.session, xOpen, errorBuf);
      if (xOpen != null) {
        if (xOpen.startsWith("22"))
          throw new DataTruncationException(errorBuf.toString(), 0, true, false, 0, 0, errno); 
        if (errno == 1820)
          throw (PasswordExpiredException)ExceptionFactory.createException(PasswordExpiredException.class, errorBuf.toString(), getExceptionInterceptor()); 
        if (errno == 1862)
          throw (ClosedOnExpiredPasswordException)ExceptionFactory.createException(ClosedOnExpiredPasswordException.class, errorBuf.toString(), getExceptionInterceptor()); 
      } 
      throw ExceptionFactory.createException(errorBuf.toString(), xOpen, errno, false, null, getExceptionInterceptor());
    } 
  }
  
  private void reclaimLargeSharedSendPacket() {
    if (this.sharedSendPacket != null && this.sharedSendPacket.getCapacity() > 1048576)
      this.sharedSendPacket = new NativePacketPayload(1024); 
  }
  
  public void clearInputStream() {
    try {
      int len;
      while ((len = this.socketConnection.getMysqlInput().available()) > 0 && this.socketConnection.getMysqlInput().skip(len) > 0L);
    } catch (IOException ioEx) {
      throw ExceptionFactory.createCommunicationsException(this.propertySet, this.serverSession, getPacketSentTimeHolder(), 
          getPacketReceivedTimeHolder(), ioEx, getExceptionInterceptor());
    } 
  }
  
  public void reclaimLargeReusablePacket() {
    if (this.reusablePacket != null && this.reusablePacket.getCapacity() > 1048576)
      this.reusablePacket = new NativePacketPayload(1024); 
  }
  
  public final <T extends Resultset> T sendQueryString(Query callingQuery, String query, String characterEncoding, int maxRows, boolean streamResults, ColumnDefinition cachedMetadata, ProtocolEntityFactory<T, NativePacketPayload> resultSetFactory) throws IOException {
    String statementComment = this.queryComment;
    if (((Boolean)this.propertySet.getBooleanProperty(PropertyKey.includeThreadNamesAsStatementComment).getValue()).booleanValue())
      statementComment = ((statementComment != null) ? (statementComment + ", ") : "") + "java thread: " + Thread.currentThread().getName(); 
    int packLength = 1 + query.length() * 4 + 2;
    byte[] commentAsBytes = null;
    if (statementComment != null) {
      commentAsBytes = StringUtils.getBytes(statementComment, characterEncoding);
      packLength += commentAsBytes.length;
      packLength += 6;
    } 
    NativePacketPayload sendPacket = new NativePacketPayload(packLength);
    sendPacket.setPosition(0);
    sendPacket.writeInteger(NativeConstants.IntegerDataType.INT1, 3L);
    if (commentAsBytes != null) {
      sendPacket.writeBytes(NativeConstants.StringLengthDataType.STRING_FIXED, Constants.SLASH_STAR_SPACE_AS_BYTES);
      sendPacket.writeBytes(NativeConstants.StringLengthDataType.STRING_FIXED, commentAsBytes);
      sendPacket.writeBytes(NativeConstants.StringLengthDataType.STRING_FIXED, Constants.SPACE_STAR_SLASH_SPACE_AS_BYTES);
    } 
    if (!this.platformDbCharsetMatches && StringUtils.startsWithIgnoreCaseAndWs(query, "LOAD DATA")) {
      sendPacket.writeBytes(NativeConstants.StringLengthDataType.STRING_FIXED, StringUtils.getBytes(query));
    } else {
      sendPacket.writeBytes(NativeConstants.StringLengthDataType.STRING_FIXED, StringUtils.getBytes(query, characterEncoding));
    } 
    return sendQueryPacket(callingQuery, sendPacket, maxRows, streamResults, cachedMetadata, resultSetFactory);
  }
  
  public final <T extends Resultset> T sendQueryPacket(Query callingQuery, NativePacketPayload queryPacket, int maxRows, boolean streamResults, ColumnDefinition cachedMetadata, ProtocolEntityFactory<T, NativePacketPayload> resultSetFactory) throws IOException {
    long queryStartTime = (this.profileSQL || this.logSlowQueries) ? getCurrentTimeNanosOrMillis() : 0L;
    this.statementExecutionDepth++;
    byte[] queryBuf = queryPacket.getByteBuffer();
    int oldPacketPosition = queryPacket.getPosition();
    LazyString query = new LazyString(queryBuf, 1, oldPacketPosition - 1);
    try {
      if (this.queryInterceptors != null) {
        T interceptedResults = invokeQueryInterceptorsPre((Supplier<String>)query, callingQuery, false);
        if (interceptedResults != null)
          return interceptedResults; 
      } 
      if (this.autoGenerateTestcaseScript) {
        StringBuilder debugBuf = new StringBuilder(query.length() + 32);
        generateQueryCommentBlock(debugBuf);
        debugBuf.append(query);
        debugBuf.append(';');
        TestUtils.dumpTestcaseQuery(debugBuf.toString());
      } 
      NativePacketPayload resultPacket = sendCommand(queryPacket, false, 0);
      long queryEndTime = (this.profileSQL || this.logSlowQueries) ? getCurrentTimeNanosOrMillis() : 0L;
      long queryDuration = (this.profileSQL || this.logSlowQueries) ? (queryEndTime - queryStartTime) : 0L;
      boolean queryWasSlow = (this.logSlowQueries && (this.useAutoSlowLog ? this.metricsHolder.checkAbonormallyLongQuery(queryDuration) : (queryDuration > ((Integer)this.propertySet.getIntegerProperty(PropertyKey.slowQueryThresholdMillis).getValue()).intValue())));
      long fetchBeginTime = this.profileSQL ? getCurrentTimeNanosOrMillis() : 0L;
      T rs = readAllResults(maxRows, streamResults, resultPacket, false, cachedMetadata, resultSetFactory);
      if (this.profileSQL || queryWasSlow) {
        long fetchEndTime = this.profileSQL ? getCurrentTimeNanosOrMillis() : 0L;
        boolean truncated = (oldPacketPosition > ((Integer)this.maxQuerySizeToLog.getValue()).intValue());
        int extractPosition = truncated ? (((Integer)this.maxQuerySizeToLog.getValue()).intValue() + 1) : oldPacketPosition;
        String extractedQuery = StringUtils.toString(queryBuf, 1, extractPosition - 1);
        if (truncated)
          extractedQuery = extractedQuery + Messages.getString("Protocol.2"); 
        ProfilerEventHandler eventSink = this.session.getProfilerEventHandler();
        if (this.logSlowQueries) {
          if (queryWasSlow) {
            eventSink.processEvent((byte)6, this.session, callingQuery, (Resultset)rs, queryDuration, new Throwable(), 
                Messages.getString("Protocol.SlowQuery", new Object[] { this.useAutoSlowLog ? " 95% of all queries " : String.valueOf(this.slowQueryThreshold), this.queryTimingUnits, 
                    Long.valueOf(queryDuration), extractedQuery }));
            if (((Boolean)this.propertySet.getBooleanProperty(PropertyKey.explainSlowQueries).getValue()).booleanValue())
              if (oldPacketPosition < 1048576) {
                queryPacket.setPosition(1);
                explainSlowQuery(query.toString(), extractedQuery);
              } else {
                this.log.logWarn(Messages.getString("Protocol.3", new Object[] { Integer.valueOf(1048576) }));
              }  
          } 
          if (this.serverSession.noGoodIndexUsed())
            eventSink.processEvent((byte)6, this.session, callingQuery, (Resultset)rs, queryDuration, new Throwable(), 
                Messages.getString("Protocol.4") + extractedQuery); 
          if (this.serverSession.noIndexUsed())
            eventSink.processEvent((byte)6, this.session, callingQuery, (Resultset)rs, queryDuration, new Throwable(), 
                Messages.getString("Protocol.5") + extractedQuery); 
          if (this.serverSession.queryWasSlow())
            eventSink.processEvent((byte)6, this.session, callingQuery, (Resultset)rs, queryDuration, new Throwable(), 
                Messages.getString("Protocol.ServerSlowQuery") + extractedQuery); 
        } 
        if (this.profileSQL) {
          eventSink.processEvent((byte)3, this.session, callingQuery, (Resultset)rs, queryDuration, new Throwable(), extractedQuery);
          eventSink.processEvent((byte)5, this.session, callingQuery, (Resultset)rs, fetchEndTime - fetchBeginTime, new Throwable(), null);
        } 
      } 
      if (this.hadWarnings)
        scanForAndThrowDataTruncation(); 
      if (this.queryInterceptors != null)
        rs = invokeQueryInterceptorsPost((Supplier<String>)query, callingQuery, rs, false); 
      return rs;
    } catch (CJException sqlEx) {
      if (this.queryInterceptors != null)
        invokeQueryInterceptorsPost((Supplier<String>)query, callingQuery, (Resultset)null, false); 
      if (callingQuery != null)
        callingQuery.checkCancelTimeout(); 
      throw sqlEx;
    } finally {
      this.statementExecutionDepth--;
    } 
  }
  
  public <T extends Resultset> T invokeQueryInterceptorsPre(Supplier<String> sql, Query interceptedQuery, boolean forceExecute) {
    Resultset resultset;
    T previousResultSet = null;
    for (int i = 0, s = this.queryInterceptors.size(); i < s; i++) {
      QueryInterceptor interceptor = this.queryInterceptors.get(i);
      boolean executeTopLevelOnly = interceptor.executeTopLevelOnly();
      boolean shouldExecute = ((executeTopLevelOnly && (this.statementExecutionDepth == 1 || forceExecute)) || !executeTopLevelOnly);
      if (shouldExecute) {
        Resultset resultset1 = interceptor.preProcess(sql, interceptedQuery);
        if (resultset1 != null)
          resultset = resultset1; 
      } 
    } 
    return (T)resultset;
  }
  
  public <M extends Message> M invokeQueryInterceptorsPre(M queryPacket, boolean forceExecute) {
    Message message;
    M previousPacketPayload = null;
    for (int i = 0, s = this.queryInterceptors.size(); i < s; i++) {
      QueryInterceptor interceptor = this.queryInterceptors.get(i);
      Message message1 = interceptor.preProcess((Message)queryPacket);
      if (message1 != null)
        message = message1; 
    } 
    return (M)message;
  }
  
  public <T extends Resultset> T invokeQueryInterceptorsPost(Supplier<String> sql, Query interceptedQuery, T originalResultSet, boolean forceExecute) {
    Resultset resultset;
    for (int i = 0, s = this.queryInterceptors.size(); i < s; i++) {
      QueryInterceptor interceptor = this.queryInterceptors.get(i);
      boolean executeTopLevelOnly = interceptor.executeTopLevelOnly();
      boolean shouldExecute = ((executeTopLevelOnly && (this.statementExecutionDepth == 1 || forceExecute)) || !executeTopLevelOnly);
      if (shouldExecute) {
        Resultset resultset1 = interceptor.postProcess(sql, interceptedQuery, (Resultset)originalResultSet, this.serverSession);
        if (resultset1 != null)
          resultset = resultset1; 
      } 
    } 
    return (T)resultset;
  }
  
  public <M extends Message> M invokeQueryInterceptorsPost(M queryPacket, M originalResponsePacket, boolean forceExecute) {
    Message message;
    for (int i = 0, s = this.queryInterceptors.size(); i < s; i++) {
      QueryInterceptor interceptor = this.queryInterceptors.get(i);
      Message message1 = interceptor.postProcess((Message)queryPacket, (Message)originalResponsePacket);
      if (message1 != null)
        message = message1; 
    } 
    return (M)message;
  }
  
  public long getCurrentTimeNanosOrMillis() {
    return this.useNanosForElapsedTime ? TimeUtil.getCurrentTimeNanosOrMillis() : System.currentTimeMillis();
  }
  
  public boolean hadWarnings() {
    return this.hadWarnings;
  }
  
  public void setHadWarnings(boolean hadWarnings) {
    this.hadWarnings = hadWarnings;
  }
  
  public void explainSlowQuery(String query, String truncatedQuery) {
    if (StringUtils.startsWithIgnoreCaseAndWs(truncatedQuery, "SELECT") || (
      versionMeetsMinimum(5, 6, 3) && StringUtils.startsWithIgnoreCaseAndWs(truncatedQuery, EXPLAINABLE_STATEMENT_EXTENSION) != -1))
      try {
        NativePacketPayload resultPacket = sendCommand(this.commandBuilder.buildComQuery(getSharedSendPacket(), "EXPLAIN " + query), false, 0);
        Resultset rs = readAllResults(-1, false, resultPacket, false, (ColumnDefinition)null, new ResultsetFactory(Resultset.Type.FORWARD_ONLY, null));
        StringBuilder explainResults = new StringBuilder(Messages.getString("Protocol.6"));
        explainResults.append(truncatedQuery);
        explainResults.append(Messages.getString("Protocol.7"));
        appendResultSetSlashGStyle(explainResults, rs);
        this.log.logWarn(explainResults.toString());
      } catch (CJException sqlEx) {
        throw sqlEx;
      } catch (Exception ex) {
        throw ExceptionFactory.createException(ex.getMessage(), ex, getExceptionInterceptor());
      }  
  }
  
  public final void skipPacket() {
    try {
      int packetLength = ((NativePacketHeader)this.packetReader.readHeader()).getMessageSize();
      this.socketConnection.getMysqlInput().skipFully(packetLength);
    } catch (IOException ioEx) {
      throw ExceptionFactory.createCommunicationsException(this.propertySet, this.serverSession, getPacketSentTimeHolder(), 
          getPacketReceivedTimeHolder(), ioEx, getExceptionInterceptor());
    } 
  }
  
  public final void quit() {
    try {
      try {
        if (!ExportControlled.isSSLEstablished(this.socketConnection.getMysqlSocket()) && 
          !this.socketConnection.getMysqlSocket().isClosed())
          try {
            this.socketConnection.getMysqlSocket().shutdownInput();
          } catch (UnsupportedOperationException unsupportedOperationException) {} 
      } catch (IOException iOException) {}
      this.packetSequence = -1;
      NativePacketPayload packet = new NativePacketPayload(1);
      send(this.commandBuilder.buildComQuit(packet), packet.getPosition());
    } finally {
      this.socketConnection.forceClose();
      this.localInfileInputStream = null;
    } 
  }
  
  public NativePacketPayload getSharedSendPacket() {
    if (this.sharedSendPacket == null)
      this.sharedSendPacket = new NativePacketPayload(1024); 
    this.sharedSendPacket.setPosition(0);
    return this.sharedSendPacket;
  }
  
  private void calculateSlowQueryThreshold() {
    this.slowQueryThreshold = ((Integer)this.propertySet.getIntegerProperty(PropertyKey.slowQueryThresholdMillis).getValue()).intValue();
    if (((Boolean)this.propertySet.getBooleanProperty(PropertyKey.useNanosForElapsedTime).getValue()).booleanValue()) {
      long nanosThreshold = ((Long)this.propertySet.getLongProperty(PropertyKey.slowQueryThresholdNanos).getValue()).longValue();
      if (nanosThreshold != 0L) {
        this.slowQueryThreshold = nanosThreshold;
      } else {
        this.slowQueryThreshold *= 1000000L;
      } 
    } 
  }
  
  public void changeUser(String user, String password, String database) {
    this.packetSequence = -1;
    this.packetSender = this.packetSender.undecorateAll();
    this.packetReader = this.packetReader.undecorateAll();
    this.authProvider.changeUser(this.serverSession, user, password, database);
  }
  
  public void checkForCharsetMismatch() {
    String characterEncoding = (String)this.propertySet.getStringProperty(PropertyKey.characterEncoding).getValue();
    if (characterEncoding != null) {
      String encodingToCheck = jvmPlatformCharset;
      if (encodingToCheck == null)
        encodingToCheck = Constants.PLATFORM_ENCODING; 
      if (encodingToCheck == null) {
        this.platformDbCharsetMatches = false;
      } else {
        this.platformDbCharsetMatches = encodingToCheck.equals(characterEncoding);
      } 
    } 
  }
  
  protected boolean useNanosForElapsedTime() {
    return this.useNanosForElapsedTime;
  }
  
  public long getSlowQueryThreshold() {
    return this.slowQueryThreshold;
  }
  
  public int getCommandCount() {
    return this.commandCount;
  }
  
  public void setQueryInterceptors(List<QueryInterceptor> queryInterceptors) {
    this.queryInterceptors = queryInterceptors.isEmpty() ? null : queryInterceptors;
  }
  
  public List<QueryInterceptor> getQueryInterceptors() {
    return this.queryInterceptors;
  }
  
  public void setSocketTimeout(int milliseconds) {
    try {
      Socket soc = this.socketConnection.getMysqlSocket();
      if (soc != null)
        soc.setSoTimeout(milliseconds); 
    } catch (IOException e) {
      throw (WrongArgumentException)ExceptionFactory.createException(WrongArgumentException.class, Messages.getString("Protocol.8"), e, getExceptionInterceptor());
    } 
  }
  
  public void releaseResources() {
    if (this.compressedPacketSender != null)
      this.compressedPacketSender.stop(); 
  }
  
  public void connect(String user, String password, String database) {
    beforeHandshake();
    this.authProvider.connect(this.serverSession, user, password, database);
  }
  
  protected boolean isDataAvailable() {
    try {
      return (this.socketConnection.getMysqlInput().available() > 0);
    } catch (IOException ioEx) {
      throw ExceptionFactory.createCommunicationsException(this.propertySet, this.serverSession, getPacketSentTimeHolder(), 
          getPacketReceivedTimeHolder(), ioEx, getExceptionInterceptor());
    } 
  }
  
  public NativePacketPayload getReusablePacket() {
    return this.reusablePacket;
  }
  
  public int getWarningCount() {
    return this.warningCount;
  }
  
  public void setWarningCount(int warningCount) {
    this.warningCount = warningCount;
  }
  
  public void dumpPacketRingBuffer() {
    LinkedList<StringBuilder> localPacketDebugRingBuffer = this.packetDebugRingBuffer;
    if (localPacketDebugRingBuffer != null) {
      StringBuilder dumpBuffer = new StringBuilder();
      dumpBuffer.append("Last " + localPacketDebugRingBuffer.size() + " packets received from server, from oldest->newest:\n");
      dumpBuffer.append("\n");
      for (Iterator<StringBuilder> ringBufIter = localPacketDebugRingBuffer.iterator(); ringBufIter.hasNext(); ) {
        dumpBuffer.append(ringBufIter.next());
        dumpBuffer.append("\n");
      } 
      this.log.logTrace(dumpBuffer.toString());
    } 
  }
  
  public boolean doesPlatformDbCharsetMatches() {
    return this.platformDbCharsetMatches;
  }
  
  public String getPasswordCharacterEncoding() {
    String encoding;
    if ((encoding = this.propertySet.getStringProperty(PropertyKey.passwordCharacterEncoding).getStringValue()) != null)
      return encoding; 
    if ((encoding = (String)this.propertySet.getStringProperty(PropertyKey.characterEncoding).getValue()) != null)
      return encoding; 
    return "UTF-8";
  }
  
  public boolean versionMeetsMinimum(int major, int minor, int subminor) {
    return this.serverSession.getServerVersion().meetsMinimum(new ServerVersion(major, minor, subminor));
  }
  
  public static MysqlType findMysqlType(PropertySet propertySet, int mysqlTypeId, short colFlag, long length, LazyString tableName, LazyString originalTableName, int collationIndex, String encoding) {
    int newMysqlTypeId;
    boolean isUnsigned = ((colFlag & 0x20) > 0);
    boolean isFromFunction = (originalTableName.length() == 0);
    boolean isBinary = ((colFlag & 0x80) > 0);
    boolean isImplicitTemporaryTable = (tableName.length() > 0 && tableName.toString().startsWith("#sql_"));
    boolean isOpaqueBinary = (isBinary && collationIndex == 63 && (mysqlTypeId == 254 || mysqlTypeId == 253 || mysqlTypeId == 15)) ? (!isImplicitTemporaryTable) : "binary".equalsIgnoreCase(encoding);
    switch (mysqlTypeId) {
      case 0:
      case 246:
        return isUnsigned ? MysqlType.DECIMAL_UNSIGNED : MysqlType.DECIMAL;
      case 1:
        if (!isUnsigned && length == 1L && ((Boolean)propertySet.getBooleanProperty(PropertyKey.tinyInt1isBit).getValue()).booleanValue()) {
          if (((Boolean)propertySet.getBooleanProperty(PropertyKey.transformedBitIsBoolean).getValue()).booleanValue())
            return MysqlType.BOOLEAN; 
          return MysqlType.BIT;
        } 
        return isUnsigned ? MysqlType.TINYINT_UNSIGNED : MysqlType.TINYINT;
      case 2:
        return isUnsigned ? MysqlType.SMALLINT_UNSIGNED : MysqlType.SMALLINT;
      case 3:
        return isUnsigned ? MysqlType.INT_UNSIGNED : MysqlType.INT;
      case 4:
        return isUnsigned ? MysqlType.FLOAT_UNSIGNED : MysqlType.FLOAT;
      case 5:
        return isUnsigned ? MysqlType.DOUBLE_UNSIGNED : MysqlType.DOUBLE;
      case 6:
        return MysqlType.NULL;
      case 7:
        return MysqlType.TIMESTAMP;
      case 8:
        return isUnsigned ? MysqlType.BIGINT_UNSIGNED : MysqlType.BIGINT;
      case 9:
        return isUnsigned ? MysqlType.MEDIUMINT_UNSIGNED : MysqlType.MEDIUMINT;
      case 10:
        return MysqlType.DATE;
      case 11:
        return MysqlType.TIME;
      case 12:
        return MysqlType.DATETIME;
      case 13:
        return MysqlType.YEAR;
      case 15:
      case 253:
        if (isOpaqueBinary && (!isFromFunction || !((Boolean)propertySet.getBooleanProperty(PropertyKey.functionsNeverReturnBlobs).getValue()).booleanValue()))
          return MysqlType.VARBINARY; 
        return MysqlType.VARCHAR;
      case 16:
        return MysqlType.BIT;
      case 245:
        return MysqlType.JSON;
      case 247:
        return MysqlType.ENUM;
      case 248:
        return MysqlType.SET;
      case 249:
        if (!isBinary || collationIndex != 63 || ((Boolean)propertySet
          .getBooleanProperty(PropertyKey.blobsAreStrings).getValue()).booleanValue() || (isFromFunction && ((Boolean)propertySet
          .getBooleanProperty(PropertyKey.functionsNeverReturnBlobs).getValue()).booleanValue()))
          return MysqlType.TINYTEXT; 
        return MysqlType.TINYBLOB;
      case 250:
        if (!isBinary || collationIndex != 63 || ((Boolean)propertySet
          .getBooleanProperty(PropertyKey.blobsAreStrings).getValue()).booleanValue() || (isFromFunction && ((Boolean)propertySet
          .getBooleanProperty(PropertyKey.functionsNeverReturnBlobs).getValue()).booleanValue()))
          return MysqlType.MEDIUMTEXT; 
        return MysqlType.MEDIUMBLOB;
      case 251:
        if (!isBinary || collationIndex != 63 || ((Boolean)propertySet
          .getBooleanProperty(PropertyKey.blobsAreStrings).getValue()).booleanValue() || (isFromFunction && ((Boolean)propertySet
          .getBooleanProperty(PropertyKey.functionsNeverReturnBlobs).getValue()).booleanValue()))
          return MysqlType.LONGTEXT; 
        return MysqlType.LONGBLOB;
      case 252:
        newMysqlTypeId = mysqlTypeId;
        if (length <= MysqlType.TINYBLOB.getPrecision().longValue()) {
          newMysqlTypeId = 249;
        } else {
          if (length <= MysqlType.BLOB.getPrecision().longValue()) {
            if (!isBinary || collationIndex != 63 || ((Boolean)propertySet
              .getBooleanProperty(PropertyKey.blobsAreStrings).getValue()).booleanValue() || (isFromFunction && ((Boolean)propertySet
              .getBooleanProperty(PropertyKey.functionsNeverReturnBlobs).getValue()).booleanValue())) {
              newMysqlTypeId = 15;
              return MysqlType.TEXT;
            } 
            return MysqlType.BLOB;
          } 
          if (length <= MysqlType.MEDIUMBLOB.getPrecision().longValue()) {
            newMysqlTypeId = 250;
          } else {
            newMysqlTypeId = 251;
          } 
        } 
        return findMysqlType(propertySet, newMysqlTypeId, colFlag, length, tableName, originalTableName, collationIndex, encoding);
      case 254:
        if (isOpaqueBinary && !((Boolean)propertySet.getBooleanProperty(PropertyKey.blobsAreStrings).getValue()).booleanValue())
          return MysqlType.BINARY; 
        return MysqlType.CHAR;
      case 255:
        return MysqlType.GEOMETRY;
    } 
    return MysqlType.UNKNOWN;
  }
  
  public <T extends ProtocolEntity> T read(Class<T> requiredClass, ProtocolEntityFactory<T, NativePacketPayload> protocolEntityFactory) throws IOException {
    ProtocolEntityReader<T, NativePacketPayload> sr = (ProtocolEntityReader<T, NativePacketPayload>)this.PROTOCOL_ENTITY_CLASS_TO_TEXT_READER.get(requiredClass);
    if (sr == null)
      throw (FeatureNotAvailableException)ExceptionFactory.createException(FeatureNotAvailableException.class, "ProtocolEntityReader isn't available for class " + requiredClass); 
    return (T)sr.read(protocolEntityFactory);
  }
  
  public <T extends ProtocolEntity> T read(Class<Resultset> requiredClass, int maxRows, boolean streamResults, NativePacketPayload resultPacket, boolean isBinaryEncoded, ColumnDefinition metadata, ProtocolEntityFactory<T, NativePacketPayload> protocolEntityFactory) throws IOException {
    ProtocolEntityReader<T, NativePacketPayload> sr = isBinaryEncoded ? (ProtocolEntityReader<T, NativePacketPayload>)this.PROTOCOL_ENTITY_CLASS_TO_BINARY_READER.get(requiredClass) : (ProtocolEntityReader<T, NativePacketPayload>)this.PROTOCOL_ENTITY_CLASS_TO_TEXT_READER.get(requiredClass);
    if (sr == null)
      throw (FeatureNotAvailableException)ExceptionFactory.createException(FeatureNotAvailableException.class, "ProtocolEntityReader isn't available for class " + requiredClass); 
    return (T)sr.read(maxRows, streamResults, resultPacket, metadata, protocolEntityFactory);
  }
  
  public <T extends ProtocolEntity> T readNextResultset(T currentProtocolEntity, int maxRows, boolean streamResults, boolean isBinaryEncoded, ProtocolEntityFactory<T, NativePacketPayload> resultSetFactory) throws IOException {
    T result = null;
    if (Resultset.class.isAssignableFrom(currentProtocolEntity.getClass()) && this.serverSession.useMultiResults() && 
      this.serverSession.hasMoreResults()) {
      T currentResultSet = currentProtocolEntity;
      do {
        NativePacketPayload fieldPacket = checkErrorMessage();
        fieldPacket.setPosition(0);
        T newResultSet = read(Resultset.class, maxRows, streamResults, fieldPacket, isBinaryEncoded, (ColumnDefinition)null, resultSetFactory);
        ((Resultset)currentResultSet).setNextResultset((Resultset)newResultSet);
        currentResultSet = newResultSet;
        if (result != null)
          continue; 
        result = currentResultSet;
      } while (streamResults && this.serverSession.hasMoreResults() && 
        !((Resultset)currentResultSet).hasRows());
    } 
    return result;
  }
  
  public <T extends Resultset> T readAllResults(int maxRows, boolean streamResults, NativePacketPayload resultPacket, boolean isBinaryEncoded, ColumnDefinition metadata, ProtocolEntityFactory<T, NativePacketPayload> resultSetFactory) throws IOException {
    resultPacket.setPosition(0);
    Resultset resultset = read(Resultset.class, maxRows, streamResults, resultPacket, isBinaryEncoded, metadata, resultSetFactory);
    if (this.serverSession.hasMoreResults()) {
      Resultset resultset1 = resultset;
      if (streamResults) {
        resultset1 = readNextResultset(resultset1, maxRows, true, isBinaryEncoded, resultSetFactory);
      } else {
        while (this.serverSession.hasMoreResults())
          resultset1 = readNextResultset(resultset1, maxRows, false, isBinaryEncoded, resultSetFactory); 
        clearInputStream();
      } 
    } 
    if (this.hadWarnings)
      scanForAndThrowDataTruncation(); 
    reclaimLargeReusablePacket();
    return (T)resultset;
  }
  
  public final <T> T readServerStatusForResultSets(NativePacketPayload rowPacket, boolean saveOldStatus) {
    OkPacket okPacket;
    T result = null;
    if (rowPacket.isEOFPacket()) {
      rowPacket.readInteger(NativeConstants.IntegerDataType.INT1);
      this.warningCount = (int)rowPacket.readInteger(NativeConstants.IntegerDataType.INT2);
      if (this.warningCount > 0)
        this.hadWarnings = true; 
      this.serverSession.setStatusFlags((int)rowPacket.readInteger(NativeConstants.IntegerDataType.INT2), saveOldStatus);
      checkTransactionState();
    } else {
      OkPacket ok = OkPacket.parse(rowPacket, this.serverSession.getErrorMessageEncoding());
      okPacket = ok;
      this.serverSession.setStatusFlags(ok.getStatusFlags(), saveOldStatus);
      checkTransactionState();
      this.warningCount = ok.getWarningCount();
      if (this.warningCount > 0)
        this.hadWarnings = true; 
    } 
    return (T)okPacket;
  }
  
  public <T extends com.mysql.cj.QueryResult> T readQueryResult(ResultBuilder<T> resultBuilder) {
    throw (CJOperationNotSupportedException)ExceptionFactory.createException(CJOperationNotSupportedException.class, "Not supported");
  }
  
  public InputStream getLocalInfileInputStream() {
    return this.localInfileInputStream;
  }
  
  public void setLocalInfileInputStream(InputStream stream) {
    this.localInfileInputStream = stream;
  }
  
  public final NativePacketPayload sendFileToServer(String fileName) {
    NativePacketPayload filePacket = (this.loadFileBufRef == null) ? null : this.loadFileBufRef.get();
    int bigPacketLength = Math.min(((Integer)this.maxAllowedPacket.getValue()).intValue() - 12, 
        alignPacketSize(((Integer)this.maxAllowedPacket.getValue()).intValue() - 16, 4096) - 12);
    int oneMeg = 1048576;
    int smallerPacketSizeAligned = Math.min(oneMeg - 12, 
        alignPacketSize(oneMeg - 16, 4096) - 12);
    int packetLength = Math.min(smallerPacketSizeAligned, bigPacketLength);
    if (filePacket == null)
      try {
        filePacket = new NativePacketPayload(packetLength);
        this.loadFileBufRef = new SoftReference<>(filePacket);
      } catch (OutOfMemoryError oom) {
        throw ExceptionFactory.createException(Messages.getString("MysqlIO.111", new Object[] { Integer.valueOf(packetLength) }), "HY001", 0, false, oom, this.exceptionInterceptor);
      }  
    filePacket.setPosition(0);
    byte[] fileBuf = new byte[packetLength];
    BufferedInputStream fileIn = null;
    try {
      if (!((Boolean)this.propertySet.getBooleanProperty(PropertyKey.allowLoadLocalInfile).getValue()).booleanValue())
        throw ExceptionFactory.createException(Messages.getString("MysqlIO.LoadDataLocalNotAllowed"), this.exceptionInterceptor); 
      InputStream hookedStream = null;
      hookedStream = getLocalInfileInputStream();
      if (hookedStream != null) {
        fileIn = new BufferedInputStream(hookedStream);
      } else if (!((Boolean)this.propertySet.getBooleanProperty(PropertyKey.allowUrlInLocalInfile).getValue()).booleanValue()) {
        fileIn = new BufferedInputStream(new FileInputStream(fileName));
      } else if (fileName.indexOf(':') != -1) {
        try {
          URL urlFromFileName = new URL(fileName);
          fileIn = new BufferedInputStream(urlFromFileName.openStream());
        } catch (MalformedURLException badUrlEx) {
          fileIn = new BufferedInputStream(new FileInputStream(fileName));
        } 
      } else {
        fileIn = new BufferedInputStream(new FileInputStream(fileName));
      } 
      int bytesRead = 0;
      while ((bytesRead = fileIn.read(fileBuf)) != -1) {
        filePacket.setPosition(0);
        filePacket.writeBytes(NativeConstants.StringLengthDataType.STRING_FIXED, fileBuf, 0, bytesRead);
        send(filePacket, filePacket.getPosition());
      } 
    } catch (IOException ioEx) {
      StringBuilder messageBuf = new StringBuilder(Messages.getString("MysqlIO.60"));
      boolean isParanoid = ((Boolean)this.propertySet.getBooleanProperty(PropertyKey.paranoid).getValue()).booleanValue();
      if (fileName != null && !isParanoid) {
        messageBuf.append("'");
        messageBuf.append(fileName);
        messageBuf.append("'");
      } 
      messageBuf.append(Messages.getString("MysqlIO.63"));
      if (!isParanoid) {
        messageBuf.append(Messages.getString("MysqlIO.64"));
        messageBuf.append(Util.stackTraceToString(ioEx));
      } 
      throw ExceptionFactory.createException(messageBuf.toString(), ioEx, this.exceptionInterceptor);
    } finally {
      if (fileIn != null) {
        try {
          fileIn.close();
        } catch (Exception ex) {
          throw ExceptionFactory.createException(Messages.getString("MysqlIO.65"), ex, this.exceptionInterceptor);
        } 
        fileIn = null;
      } else {
        filePacket.setPosition(0);
        send(filePacket, filePacket.getPosition());
        checkErrorMessage();
      } 
    } 
    filePacket.setPosition(0);
    send(filePacket, filePacket.getPosition());
    return checkErrorMessage();
  }
  
  private int alignPacketSize(int a, int l) {
    return a + l - 1 & (l - 1 ^ 0xFFFFFFFF);
  }
  
  public NativeProtocol(Log logger) {
    this.streamingData = null;
    this.log = logger;
    this.metricsHolder = new BaseMetricsHolder();
  }
  
  public ResultsetRows getStreamingData() {
    return this.streamingData;
  }
  
  public void setStreamingData(ResultsetRows streamingData) {
    this.streamingData = streamingData;
  }
  
  public void checkForOutstandingStreamingData() {
    if (this.streamingData != null) {
      boolean shouldClobber = ((Boolean)this.propertySet.getBooleanProperty(PropertyKey.clobberStreamingResults).getValue()).booleanValue();
      if (!shouldClobber)
        throw ExceptionFactory.createException(Messages.getString("MysqlIO.39") + this.streamingData + Messages.getString("MysqlIO.40") + 
            Messages.getString("MysqlIO.41") + Messages.getString("MysqlIO.42"), this.exceptionInterceptor); 
      this.streamingData.getOwner().closeOwner(false);
      clearInputStream();
    } 
  }
  
  public void unsetStreamingData(ResultsetRows streamer) {
    if (this.streamingData == null)
      throw ExceptionFactory.createException(Messages.getString("MysqlIO.17") + streamer + Messages.getString("MysqlIO.18"), this.exceptionInterceptor); 
    if (streamer == this.streamingData)
      this.streamingData = null; 
  }
  
  public void scanForAndThrowDataTruncation() {
    if (this.streamingData == null && ((Boolean)this.propertySet.getBooleanProperty(PropertyKey.jdbcCompliantTruncation).getValue()).booleanValue() && getWarningCount() > 0) {
      int warningCountOld = getWarningCount();
      convertShowWarningsToSQLWarnings(getWarningCount(), true);
      setWarningCount(warningCountOld);
    } 
  }
  
  public StringBuilder generateQueryCommentBlock(StringBuilder buf) {
    buf.append("/* conn id ");
    buf.append(getServerSession().getCapabilities().getThreadId());
    buf.append(" clock: ");
    buf.append(System.currentTimeMillis());
    buf.append(" */ ");
    return buf;
  }
  
  public BaseMetricsHolder getMetricsHolder() {
    return this.metricsHolder;
  }
  
  public String getQueryComment() {
    return this.queryComment;
  }
  
  public void setQueryComment(String comment) {
    this.queryComment = comment;
  }
  
  private void appendDeadlockStatusInformation(Session sess, String xOpen, StringBuilder errorBuf) {
    if (((Boolean)sess.getPropertySet().getBooleanProperty(PropertyKey.includeInnodbStatusInDeadlockExceptions).getValue()).booleanValue() && xOpen != null && (xOpen
      .startsWith("40") || xOpen.startsWith("41")) && getStreamingData() == null)
      try {
        NativePacketPayload resultPacket = sendCommand(this.commandBuilder.buildComQuery(getSharedSendPacket(), "SHOW ENGINE INNODB STATUS"), false, 0);
        Resultset rs = readAllResults(-1, false, resultPacket, false, (ColumnDefinition)null, new ResultsetFactory(Resultset.Type.FORWARD_ONLY, null));
        int colIndex = 0;
        Field f = null;
        for (int i = 0; i < (rs.getColumnDefinition().getFields()).length; i++) {
          f = rs.getColumnDefinition().getFields()[i];
          if ("Status".equals(f.getName())) {
            colIndex = i;
            break;
          } 
        } 
        StringValueFactory stringValueFactory = new StringValueFactory(this.propertySet);
        Row r;
        if ((r = (Row)rs.getRows().next()) != null) {
          errorBuf.append("\n\n").append((String)r.getValue(colIndex, (ValueFactory)stringValueFactory));
        } else {
          errorBuf.append("\n\n").append(Messages.getString("MysqlIO.NoInnoDBStatusFound"));
        } 
      } catch (IOException|CJException ex) {
        errorBuf.append("\n\n").append(Messages.getString("MysqlIO.InnoDBStatusFailed")).append("\n\n").append(Util.stackTraceToString(ex));
      }  
    if (((Boolean)sess.getPropertySet().getBooleanProperty(PropertyKey.includeThreadDumpInDeadlockExceptions).getValue()).booleanValue()) {
      errorBuf.append("\n\n*** Java threads running at time of deadlock ***\n\n");
      ThreadMXBean threadMBean = ManagementFactory.getThreadMXBean();
      long[] threadIds = threadMBean.getAllThreadIds();
      ThreadInfo[] threads = threadMBean.getThreadInfo(threadIds, 2147483647);
      List<ThreadInfo> activeThreads = new ArrayList<>();
      for (ThreadInfo info : threads) {
        if (info != null)
          activeThreads.add(info); 
      } 
      for (ThreadInfo threadInfo : activeThreads) {
        errorBuf.append('"').append(threadInfo.getThreadName()).append("\" tid=").append(threadInfo.getThreadId()).append(" ")
          .append(threadInfo.getThreadState());
        if (threadInfo.getLockName() != null)
          errorBuf.append(" on lock=").append(threadInfo.getLockName()); 
        if (threadInfo.isSuspended())
          errorBuf.append(" (suspended)"); 
        if (threadInfo.isInNative())
          errorBuf.append(" (running in native)"); 
        StackTraceElement[] stackTrace = threadInfo.getStackTrace();
        if (stackTrace.length > 0) {
          errorBuf.append(" in ");
          errorBuf.append(stackTrace[0].getClassName()).append(".");
          errorBuf.append(stackTrace[0].getMethodName()).append("()");
        } 
        errorBuf.append("\n");
        if (threadInfo.getLockOwnerName() != null)
          errorBuf.append("\t owned by ").append(threadInfo.getLockOwnerName()).append(" Id=").append(threadInfo.getLockOwnerId()).append("\n"); 
        for (int j = 0; j < stackTrace.length; j++) {
          StackTraceElement ste = stackTrace[j];
          errorBuf.append("\tat ").append(ste.toString()).append("\n");
        } 
      } 
    } 
  }
  
  private StringBuilder appendResultSetSlashGStyle(StringBuilder appendTo, Resultset rs) {
    Field[] fields = rs.getColumnDefinition().getFields();
    int maxWidth = 0;
    for (int i = 0; i < fields.length; i++) {
      if (fields[i].getColumnLabel().length() > maxWidth)
        maxWidth = fields[i].getColumnLabel().length(); 
    } 
    int rowCount = 1;
    Row r;
    while ((r = (Row)rs.getRows().next()) != null) {
      appendTo.append("*************************** ");
      appendTo.append(rowCount++);
      appendTo.append(". row ***************************\n");
      for (int j = 0; j < fields.length; j++) {
        int leftPad = maxWidth - fields[j].getColumnLabel().length();
        for (int k = 0; k < leftPad; k++)
          appendTo.append(" "); 
        appendTo.append(fields[j].getColumnLabel()).append(": ");
        String stringVal = (String)r.getValue(j, (ValueFactory)new StringValueFactory(this.propertySet));
        appendTo.append((stringVal != null) ? stringVal : "NULL").append("\n");
      } 
      appendTo.append("\n");
    } 
    return appendTo;
  }
  
  public SQLWarning convertShowWarningsToSQLWarnings(int warningCountIfKnown, boolean forTruncationOnly) {
    SQLWarning currentWarning = null;
    ResultsetRows rows = null;
    try {
      SQLWarning sQLWarning;
      NativePacketPayload resultPacket = sendCommand(this.commandBuilder.buildComQuery(getSharedSendPacket(), "SHOW WARNINGS"), false, 0);
      Resultset warnRs = readAllResults(-1, (warningCountIfKnown > 99), resultPacket, false, (ColumnDefinition)null, new ResultsetFactory(Resultset.Type.FORWARD_ONLY, Resultset.Concurrency.READ_ONLY));
      int codeFieldIndex = warnRs.getColumnDefinition().findColumn("Code", false, 1) - 1;
      int messageFieldIndex = warnRs.getColumnDefinition().findColumn("Message", false, 1) - 1;
      StringValueFactory stringValueFactory = new StringValueFactory(this.propertySet);
      IntegerValueFactory integerValueFactory = new IntegerValueFactory(this.propertySet);
      rows = warnRs.getRows();
      Row r;
      while ((r = (Row)rows.next()) != null) {
        MysqlDataTruncation mysqlDataTruncation;
        int code = ((Integer)r.getValue(codeFieldIndex, (ValueFactory)integerValueFactory)).intValue();
        if (forTruncationOnly) {
          if (code == 1265 || code == 1264) {
            MysqlDataTruncation mysqlDataTruncation1 = new MysqlDataTruncation((String)r.getValue(messageFieldIndex, (ValueFactory)stringValueFactory), 0, false, false, 0, 0, code);
            if (currentWarning == null) {
              mysqlDataTruncation = mysqlDataTruncation1;
              continue;
            } 
            mysqlDataTruncation.setNextWarning((SQLWarning)mysqlDataTruncation1);
          } 
          continue;
        } 
        String message = (String)r.getValue(messageFieldIndex, (ValueFactory)stringValueFactory);
        SQLWarning newWarning = new SQLWarning(message, MysqlErrorNumbers.mysqlToSqlState(code), code);
        if (mysqlDataTruncation == null) {
          sQLWarning = newWarning;
          continue;
        } 
        sQLWarning.setNextWarning(newWarning);
      } 
      if (forTruncationOnly && sQLWarning != null)
        throw ExceptionFactory.createException(sQLWarning.getMessage(), sQLWarning); 
      return sQLWarning;
    } catch (IOException ex) {
      throw ExceptionFactory.createException(ex.getMessage(), ex);
    } finally {
      if (rows != null)
        rows.close(); 
    } 
  }
  
  public ColumnDefinition readMetadata() {
    throw (CJOperationNotSupportedException)ExceptionFactory.createException(CJOperationNotSupportedException.class, "Not supported");
  }
  
  public void close() throws IOException {
    throw (CJOperationNotSupportedException)ExceptionFactory.createException(CJOperationNotSupportedException.class, "Not supported");
  }
  
  public void configureTimezone() {
    String configuredTimeZoneOnServer = this.serverSession.getServerVariable("time_zone");
    if ("SYSTEM".equalsIgnoreCase(configuredTimeZoneOnServer))
      configuredTimeZoneOnServer = this.serverSession.getServerVariable("system_time_zone"); 
    String canonicalTimezone = (String)getPropertySet().getStringProperty(PropertyKey.serverTimezone).getValue();
    if (configuredTimeZoneOnServer != null)
      if (canonicalTimezone == null || StringUtils.isEmptyOrWhitespaceOnly(canonicalTimezone))
        try {
          canonicalTimezone = TimeUtil.getCanonicalTimezone(configuredTimeZoneOnServer, getExceptionInterceptor());
        } catch (IllegalArgumentException iae) {
          throw (WrongArgumentException)ExceptionFactory.createException(WrongArgumentException.class, iae.getMessage(), getExceptionInterceptor());
        }   
    if (canonicalTimezone != null && canonicalTimezone.length() > 0) {
      this.serverSession.setServerTimeZone(TimeZone.getTimeZone(canonicalTimezone));
      if (!canonicalTimezone.equalsIgnoreCase("GMT") && this.serverSession.getServerTimeZone().getID().equals("GMT"))
        throw (WrongArgumentException)ExceptionFactory.createException(WrongArgumentException.class, Messages.getString("Connection.9", new Object[] { canonicalTimezone }), getExceptionInterceptor()); 
    } 
    this.serverSession.setDefaultTimeZone(this.serverSession.getServerTimeZone());
  }
  
  public void initServerSession() {
    configureTimezone();
    if (this.session.getServerSession().getServerVariables().containsKey("max_allowed_packet")) {
      int serverMaxAllowedPacket = this.session.getServerSession().getServerVariable("max_allowed_packet", -1);
      if (serverMaxAllowedPacket != -1 && (!this.maxAllowedPacket.isExplicitlySet() || serverMaxAllowedPacket < ((Integer)this.maxAllowedPacket.getValue()).intValue()))
        this.maxAllowedPacket.setValue(Integer.valueOf(serverMaxAllowedPacket)); 
      if (((Boolean)this.useServerPrepStmts.getValue()).booleanValue()) {
        RuntimeProperty<Integer> blobSendChunkSize = this.propertySet.getProperty(PropertyKey.blobSendChunkSize);
        int preferredBlobSendChunkSize = ((Integer)blobSendChunkSize.getValue()).intValue();
        int packetHeaderSize = 8203;
        int allowedBlobSendChunkSize = Math.min(preferredBlobSendChunkSize, ((Integer)this.maxAllowedPacket.getValue()).intValue()) - packetHeaderSize;
        if (allowedBlobSendChunkSize <= 0)
          throw ExceptionFactory.createException(Messages.getString("Connection.15", new Object[] { Integer.valueOf(packetHeaderSize) }), "01S00", 0, false, null, this.exceptionInterceptor); 
        blobSendChunkSize.setValue(Integer.valueOf(allowedBlobSendChunkSize));
      } 
    } 
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\protocol\a\NativeProtocol.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
package com.mysql.cj;

import com.mysql.cj.conf.PropertyKey;
import com.mysql.cj.conf.RuntimeProperty;
import com.mysql.cj.exceptions.CJException;
import com.mysql.cj.exceptions.ExceptionFactory;
import com.mysql.cj.exceptions.WrongArgumentException;
import com.mysql.cj.protocol.ColumnDefinition;
import com.mysql.cj.protocol.ProtocolEntityFactory;
import com.mysql.cj.protocol.Resultset;
import com.mysql.cj.protocol.a.ColumnDefinitionFactory;
import com.mysql.cj.protocol.a.NativeConstants;
import com.mysql.cj.protocol.a.NativeMessageBuilder;
import com.mysql.cj.protocol.a.NativePacketPayload;
import com.mysql.cj.result.Field;
import com.mysql.cj.util.StringUtils;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.sql.Blob;

public class ServerPreparedQuery extends AbstractPreparedQuery<ServerPreparedQueryBindings> {
  public static final int BLOB_STREAM_READ_BUF_SIZE = 8192;
  
  public static final byte OPEN_CURSOR_FLAG = 1;
  
  private long serverStatementId;
  
  private Field[] parameterFields;
  
  private ColumnDefinition resultFields;
  
  protected boolean profileSQL = false;
  
  protected boolean gatherPerfMetrics;
  
  protected boolean logSlowQueries = false;
  
  private boolean useAutoSlowLog;
  
  protected RuntimeProperty<Integer> slowQueryThresholdMillis;
  
  protected RuntimeProperty<Boolean> explainSlowQueries;
  
  protected boolean useCursorFetch = false;
  
  protected boolean queryWasSlow = false;
  
  protected NativeMessageBuilder commandBuilder = new NativeMessageBuilder();
  
  public static ServerPreparedQuery getInstance(NativeSession sess) {
    if (((Boolean)sess.getPropertySet().getBooleanProperty(PropertyKey.autoGenerateTestcaseScript).getValue()).booleanValue())
      return new ServerPreparedQueryTestcaseGenerator(sess); 
    return new ServerPreparedQuery(sess);
  }
  
  protected ServerPreparedQuery(NativeSession sess) {
    super(sess);
    this.profileSQL = ((Boolean)sess.getPropertySet().getBooleanProperty(PropertyKey.profileSQL).getValue()).booleanValue();
    this.gatherPerfMetrics = ((Boolean)sess.getPropertySet().getBooleanProperty(PropertyKey.gatherPerfMetrics).getValue()).booleanValue();
    this.logSlowQueries = ((Boolean)sess.getPropertySet().getBooleanProperty(PropertyKey.logSlowQueries).getValue()).booleanValue();
    this.useAutoSlowLog = ((Boolean)sess.getPropertySet().getBooleanProperty(PropertyKey.autoSlowLog).getValue()).booleanValue();
    this.slowQueryThresholdMillis = sess.getPropertySet().getIntegerProperty(PropertyKey.slowQueryThresholdMillis);
    this.explainSlowQueries = sess.getPropertySet().getBooleanProperty(PropertyKey.explainSlowQueries);
    this.useCursorFetch = ((Boolean)sess.getPropertySet().getBooleanProperty(PropertyKey.useCursorFetch).getValue()).booleanValue();
  }
  
  public void serverPrepare(String sql) throws IOException {
    this.session.checkClosed();
    synchronized (this.session) {
      long begin = this.profileSQL ? System.currentTimeMillis() : 0L;
      boolean loadDataQuery = StringUtils.startsWithIgnoreCaseAndWs(sql, "LOAD DATA");
      String characterEncoding = null;
      String connectionEncoding = (String)this.session.getPropertySet().getStringProperty(PropertyKey.characterEncoding).getValue();
      if (!loadDataQuery && connectionEncoding != null)
        characterEncoding = connectionEncoding; 
      NativePacketPayload prepareResultPacket = this.session.sendCommand(this.commandBuilder.buildComStmtPrepare(this.session.getSharedSendPacket(), sql, characterEncoding), false, 0);
      prepareResultPacket.setPosition(1);
      this.serverStatementId = prepareResultPacket.readInteger(NativeConstants.IntegerDataType.INT4);
      int fieldCount = (int)prepareResultPacket.readInteger(NativeConstants.IntegerDataType.INT2);
      setParameterCount((int)prepareResultPacket.readInteger(NativeConstants.IntegerDataType.INT2));
      this.queryBindings = new ServerPreparedQueryBindings(this.parameterCount, this.session);
      this.queryBindings.setLoadDataQuery(loadDataQuery);
      if (this.gatherPerfMetrics)
        this.session.getProtocol().getMetricsHolder().incrementNumberOfPrepares(); 
      if (this.profileSQL)
        this.session.getProfilerEventHandler().processEvent((byte)2, this.session, this, null, this.session
            .getCurrentTimeNanosOrMillis() - begin, new Throwable(), truncateQueryToLog(sql)); 
      boolean checkEOF = !this.session.getServerSession().isEOFDeprecated();
      if (this.parameterCount > 0) {
        if (checkEOF)
          this.session.getProtocol().skipPacket(); 
        this
          .parameterFields = ((ColumnDefinition)this.session.getProtocol().read(ColumnDefinition.class, (ProtocolEntityFactory)new ColumnDefinitionFactory(this.parameterCount, null))).getFields();
      } 
      if (fieldCount > 0)
        this.resultFields = (ColumnDefinition)this.session.getProtocol().read(ColumnDefinition.class, (ProtocolEntityFactory)new ColumnDefinitionFactory(fieldCount, null)); 
    } 
  }
  
  public void statementBegins() {
    super.statementBegins();
    this.queryWasSlow = false;
  }
  
  public <T extends Resultset> T serverExecute(int maxRowsToRetrieve, boolean createStreamingResultSet, ColumnDefinition metadata, ProtocolEntityFactory<T, NativePacketPayload> resultSetFactory) {
    if (this.session.shouldIntercept()) {
      T interceptedResults = this.session.invokeQueryInterceptorsPre(() -> getOriginalSql(), this, true);
      if (interceptedResults != null)
        return interceptedResults; 
    } 
    String queryAsString = (this.profileSQL || this.logSlowQueries || this.gatherPerfMetrics) ? asSql(true) : "";
    NativePacketPayload packet = prepareExecutePacket();
    NativePacketPayload resPacket = sendExecutePacket(packet, queryAsString);
    T rs = readExecuteResult(resPacket, maxRowsToRetrieve, createStreamingResultSet, metadata, resultSetFactory, queryAsString);
    return rs;
  }
  
  public NativePacketPayload prepareExecutePacket() {
    ServerPreparedQueryBindValue[] parameterBindings = this.queryBindings.getBindValues();
    if (this.queryBindings.isLongParameterSwitchDetected()) {
      boolean firstFound = false;
      long boundTimeToCheck = 0L;
      for (int m = 0; m < this.parameterCount - 1; m++) {
        if (parameterBindings[m].isStream()) {
          if (firstFound && boundTimeToCheck != (parameterBindings[m]).boundBeforeExecutionNum)
            throw ExceptionFactory.createException(
                Messages.getString("ServerPreparedStatement.11") + Messages.getString("ServerPreparedStatement.12"), "S1C00", 0, true, null, this.session
                .getExceptionInterceptor()); 
          firstFound = true;
          boundTimeToCheck = (parameterBindings[m]).boundBeforeExecutionNum;
        } 
      } 
      serverResetStatement();
    } 
    this.queryBindings.checkAllParametersSet();
    for (int i = 0; i < this.parameterCount; i++) {
      if (parameterBindings[i].isStream())
        serverLongData(i, parameterBindings[i]); 
    } 
    NativePacketPayload packet = this.session.getSharedSendPacket();
    packet.writeInteger(NativeConstants.IntegerDataType.INT1, 23L);
    packet.writeInteger(NativeConstants.IntegerDataType.INT4, this.serverStatementId);
    if (this.resultFields != null && this.resultFields.getFields() != null && this.useCursorFetch && this.resultSetType == Resultset.Type.FORWARD_ONLY && this.fetchSize > 0) {
      packet.writeInteger(NativeConstants.IntegerDataType.INT1, 1L);
    } else {
      packet.writeInteger(NativeConstants.IntegerDataType.INT1, 0L);
    } 
    packet.writeInteger(NativeConstants.IntegerDataType.INT4, 1L);
    int nullCount = (this.parameterCount + 7) / 8;
    int nullBitsPosition = packet.getPosition();
    for (int j = 0; j < nullCount; j++)
      packet.writeInteger(NativeConstants.IntegerDataType.INT1, 0L); 
    byte[] nullBitsBuffer = new byte[nullCount];
    packet.writeInteger(NativeConstants.IntegerDataType.INT1, this.queryBindings.getSendTypesToServer().get() ? 1L : 0L);
    if (this.queryBindings.getSendTypesToServer().get())
      for (int m = 0; m < this.parameterCount; m++)
        packet.writeInteger(NativeConstants.IntegerDataType.INT2, (parameterBindings[m]).bufferType);  
    for (int k = 0; k < this.parameterCount; k++) {
      if (!parameterBindings[k].isStream())
        if (!parameterBindings[k].isNull()) {
          parameterBindings[k].storeBinding(packet, this.queryBindings.isLoadDataQuery(), this.charEncoding, this.session.getExceptionInterceptor());
        } else {
          nullBitsBuffer[k / 8] = (byte)(nullBitsBuffer[k / 8] | 1 << (k & 0x7));
        }  
    } 
    int endPosition = packet.getPosition();
    packet.setPosition(nullBitsPosition);
    packet.writeBytes(NativeConstants.StringLengthDataType.STRING_FIXED, nullBitsBuffer);
    packet.setPosition(endPosition);
    return packet;
  }
  
  public NativePacketPayload sendExecutePacket(NativePacketPayload packet, String queryAsString) {
    boolean countDuration = (this.profileSQL || this.logSlowQueries || this.gatherPerfMetrics);
    long begin = countDuration ? this.session.getCurrentTimeNanosOrMillis() : 0L;
    resetCancelledState();
    CancelQueryTask timeoutTask = null;
    try {
      timeoutTask = startQueryTimer(this, this.timeoutInMillis);
      statementBegins();
      NativePacketPayload resultPacket = this.session.sendCommand(packet, false, 0);
      long queryEndTime = countDuration ? this.session.getCurrentTimeNanosOrMillis() : 0L;
      if (timeoutTask != null) {
        stopQueryTimer(timeoutTask, true, true);
        timeoutTask = null;
      } 
      long elapsedTime = countDuration ? (queryEndTime - begin) : 0L;
      if (this.logSlowQueries) {
        this
          
          .queryWasSlow = this.useAutoSlowLog ? this.session.getProtocol().getMetricsHolder().checkAbonormallyLongQuery(elapsedTime) : ((elapsedTime > ((Integer)this.slowQueryThresholdMillis.getValue()).intValue()));
        if (this.queryWasSlow)
          this.session.getProfilerEventHandler().processEvent((byte)6, this.session, this, null, elapsedTime, new Throwable(), 
              Messages.getString("ServerPreparedStatement.15", (Object[])new String[] { String.valueOf(this.session.getSlowQueryThreshold()), 
                  String.valueOf(elapsedTime), this.originalSql, queryAsString })); 
      } 
      if (this.gatherPerfMetrics) {
        this.session.getProtocol().getMetricsHolder().registerQueryExecutionTime(elapsedTime);
        this.session.getProtocol().getMetricsHolder().incrementNumberOfPreparedExecutes();
      } 
      if (this.profileSQL)
        this.session.getProfilerEventHandler().processEvent((byte)4, this.session, this, null, elapsedTime, new Throwable(), 
            truncateQueryToLog(queryAsString)); 
      return resultPacket;
    } catch (CJException sqlEx) {
      if (this.session.shouldIntercept())
        this.session.invokeQueryInterceptorsPost(() -> getOriginalSql(), this, (Resultset)null, true); 
      throw sqlEx;
    } finally {
      this.statementExecuting.set(false);
      stopQueryTimer(timeoutTask, false, false);
    } 
  }
  
  public <T extends Resultset> T readExecuteResult(NativePacketPayload resultPacket, int maxRowsToRetrieve, boolean createStreamingResultSet, ColumnDefinition metadata, ProtocolEntityFactory<T, NativePacketPayload> resultSetFactory, String queryAsString) {
    try {
      T t;
      long fetchStartTime = this.profileSQL ? this.session.getCurrentTimeNanosOrMillis() : 0L;
      Resultset resultset = this.session.getProtocol().readAllResults(maxRowsToRetrieve, createStreamingResultSet, resultPacket, true, (metadata != null) ? metadata : this.resultFields, resultSetFactory);
      if (this.session.shouldIntercept()) {
        T interceptedResults = this.session.invokeQueryInterceptorsPost(() -> getOriginalSql(), this, (T)resultset, true);
        if (interceptedResults != null)
          t = interceptedResults; 
      } 
      if (this.profileSQL)
        this.session.getProfilerEventHandler().processEvent((byte)5, this.session, this, (Resultset)t, this.session
            .getCurrentTimeNanosOrMillis() - fetchStartTime, new Throwable(), null); 
      if (this.queryWasSlow && ((Boolean)this.explainSlowQueries.getValue()).booleanValue())
        this.session.getProtocol().explainSlowQuery(queryAsString, queryAsString); 
      this.queryBindings.getSendTypesToServer().set(false);
      if (this.session.hadWarnings())
        this.session.getProtocol().scanForAndThrowDataTruncation(); 
      return t;
    } catch (IOException ioEx) {
      throw ExceptionFactory.createCommunicationsException(this.session.getPropertySet(), this.session.getServerSession(), this.session
          .getProtocol().getPacketSentTimeHolder(), this.session.getProtocol().getPacketReceivedTimeHolder(), ioEx, this.session
          .getExceptionInterceptor());
    } catch (CJException sqlEx) {
      if (this.session.shouldIntercept())
        this.session.invokeQueryInterceptorsPost(() -> getOriginalSql(), this, (Resultset)null, true); 
      throw sqlEx;
    } 
  }
  
  private void serverLongData(int parameterIndex, ServerPreparedQueryBindValue longData) {
    synchronized (this) {
      NativePacketPayload packet = this.session.getSharedSendPacket();
      Object value = longData.value;
      if (value instanceof byte[]) {
        this.session.sendCommand(this.commandBuilder.buildComStmtSendLongData(packet, this.serverStatementId, parameterIndex, (byte[])value), true, 0);
      } else if (value instanceof InputStream) {
        storeStream(parameterIndex, packet, (InputStream)value);
      } else if (value instanceof Blob) {
        try {
          storeStream(parameterIndex, packet, ((Blob)value).getBinaryStream());
        } catch (Throwable t) {
          throw ExceptionFactory.createException(t.getMessage(), this.session.getExceptionInterceptor());
        } 
      } else if (value instanceof Reader) {
        storeReader(parameterIndex, packet, (Reader)value);
      } else {
        throw (WrongArgumentException)ExceptionFactory.createException(WrongArgumentException.class, 
            Messages.getString("ServerPreparedStatement.18") + value.getClass().getName() + "'", this.session.getExceptionInterceptor());
      } 
    } 
  }
  
  public void closeQuery() {
    this.queryBindings = null;
    this.parameterFields = null;
    this.resultFields = null;
    super.closeQuery();
  }
  
  public long getServerStatementId() {
    return this.serverStatementId;
  }
  
  public void setServerStatementId(long serverStatementId) {
    this.serverStatementId = serverStatementId;
  }
  
  public Field[] getParameterFields() {
    return this.parameterFields;
  }
  
  public void setParameterFields(Field[] parameterFields) {
    this.parameterFields = parameterFields;
  }
  
  public ColumnDefinition getResultFields() {
    return this.resultFields;
  }
  
  public void setResultFields(ColumnDefinition resultFields) {
    this.resultFields = resultFields;
  }
  
  public void storeStream(int parameterIndex, NativePacketPayload packet, InputStream inStream) {
    this.session.checkClosed();
    synchronized (this.session) {
      byte[] buf = new byte[8192];
      int numRead = 0;
      try {
        int bytesInPacket = 0;
        int totalBytesRead = 0;
        int bytesReadAtLastSend = 0;
        int packetIsFullAt = ((Integer)this.session.getPropertySet().getMemorySizeProperty(PropertyKey.blobSendChunkSize).getValue()).intValue();
        packet.setPosition(0);
        packet.writeInteger(NativeConstants.IntegerDataType.INT1, 24L);
        packet.writeInteger(NativeConstants.IntegerDataType.INT4, this.serverStatementId);
        packet.writeInteger(NativeConstants.IntegerDataType.INT2, parameterIndex);
        boolean readAny = false;
        while ((numRead = inStream.read(buf)) != -1) {
          readAny = true;
          packet.writeBytes(NativeConstants.StringLengthDataType.STRING_FIXED, buf, 0, numRead);
          bytesInPacket += numRead;
          totalBytesRead += numRead;
          if (bytesInPacket >= packetIsFullAt) {
            bytesReadAtLastSend = totalBytesRead;
            this.session.sendCommand(packet, true, 0);
            bytesInPacket = 0;
            packet.setPosition(0);
            packet.writeInteger(NativeConstants.IntegerDataType.INT1, 24L);
            packet.writeInteger(NativeConstants.IntegerDataType.INT4, this.serverStatementId);
            packet.writeInteger(NativeConstants.IntegerDataType.INT2, parameterIndex);
          } 
        } 
        if (totalBytesRead != bytesReadAtLastSend)
          this.session.sendCommand(packet, true, 0); 
        if (!readAny)
          this.session.sendCommand(packet, true, 0); 
      } catch (IOException ioEx) {
        throw ExceptionFactory.createException(Messages.getString("ServerPreparedStatement.25") + ioEx.toString(), ioEx, this.session
            .getExceptionInterceptor());
      } finally {
        if (((Boolean)this.autoClosePStmtStreams.getValue()).booleanValue() && 
          inStream != null)
          try {
            inStream.close();
          } catch (IOException iOException) {} 
      } 
    } 
  }
  
  public void storeReader(int parameterIndex, NativePacketPayload packet, Reader inStream) {
    this.session.checkClosed();
    synchronized (this.session) {
      String forcedEncoding = this.session.getPropertySet().getStringProperty(PropertyKey.clobCharacterEncoding).getStringValue();
      String clobEncoding = (forcedEncoding == null) ? (String)this.session.getPropertySet().getStringProperty(PropertyKey.characterEncoding).getValue() : forcedEncoding;
      int maxBytesChar = 2;
      if (clobEncoding != null)
        if (!clobEncoding.equals("UTF-16")) {
          maxBytesChar = this.session.getServerSession().getMaxBytesPerChar(clobEncoding);
          if (maxBytesChar == 1)
            maxBytesChar = 2; 
        } else {
          maxBytesChar = 4;
        }  
      char[] buf = new char[8192 / maxBytesChar];
      int numRead = 0;
      int bytesInPacket = 0;
      int totalBytesRead = 0;
      int bytesReadAtLastSend = 0;
      int packetIsFullAt = ((Integer)this.session.getPropertySet().getMemorySizeProperty(PropertyKey.blobSendChunkSize).getValue()).intValue();
      try {
        packet.setPosition(0);
        packet.writeInteger(NativeConstants.IntegerDataType.INT1, 24L);
        packet.writeInteger(NativeConstants.IntegerDataType.INT4, this.serverStatementId);
        packet.writeInteger(NativeConstants.IntegerDataType.INT2, parameterIndex);
        boolean readAny = false;
        while ((numRead = inStream.read(buf)) != -1) {
          readAny = true;
          byte[] valueAsBytes = StringUtils.getBytes(buf, 0, numRead, clobEncoding);
          packet.writeBytes(NativeConstants.StringSelfDataType.STRING_EOF, valueAsBytes);
          bytesInPacket += valueAsBytes.length;
          totalBytesRead += valueAsBytes.length;
          if (bytesInPacket >= packetIsFullAt) {
            bytesReadAtLastSend = totalBytesRead;
            this.session.sendCommand(packet, true, 0);
            bytesInPacket = 0;
            packet.setPosition(0);
            packet.writeInteger(NativeConstants.IntegerDataType.INT1, 24L);
            packet.writeInteger(NativeConstants.IntegerDataType.INT4, this.serverStatementId);
            packet.writeInteger(NativeConstants.IntegerDataType.INT2, parameterIndex);
          } 
        } 
        if (totalBytesRead != bytesReadAtLastSend)
          this.session.sendCommand(packet, true, 0); 
        if (!readAny)
          this.session.sendCommand(packet, true, 0); 
      } catch (IOException ioEx) {
        throw ExceptionFactory.createException(Messages.getString("ServerPreparedStatement.24") + ioEx.toString(), ioEx, this.session
            .getExceptionInterceptor());
      } finally {
        if (((Boolean)this.autoClosePStmtStreams.getValue()).booleanValue() && 
          inStream != null)
          try {
            inStream.close();
          } catch (IOException iOException) {} 
      } 
    } 
  }
  
  public void clearParameters(boolean clearServerParameters) {
    boolean hadLongData = false;
    if (this.queryBindings != null) {
      hadLongData = this.queryBindings.clearBindValues();
      this.queryBindings.setLongParameterSwitchDetected(!(clearServerParameters && hadLongData));
    } 
    if (clearServerParameters && hadLongData)
      serverResetStatement(); 
  }
  
  public void serverResetStatement() {
    this.session.checkClosed();
    synchronized (this.session) {
      try {
        this.session.sendCommand(this.commandBuilder.buildComStmtReset(this.session.getSharedSendPacket(), this.serverStatementId), false, 0);
      } finally {
        this.session.clearInputStream();
      } 
    } 
  }
  
  protected long[] computeMaxParameterSetSizeAndBatchSize(int numBatchedArgs) {
    long sizeOfEntireBatch = 10L;
    long maxSizeOfParameterSet = 0L;
    for (int i = 0; i < numBatchedArgs; i++) {
      ServerPreparedQueryBindValue[] paramArg = ((ServerPreparedQueryBindings)this.batchedArgs.get(i)).getBindValues();
      long sizeOfParameterSet = ((this.parameterCount + 7) / 8);
      sizeOfParameterSet += (this.parameterCount * 2);
      ServerPreparedQueryBindValue[] parameterBindings = this.queryBindings.getBindValues();
      for (int j = 0; j < parameterBindings.length; j++) {
        if (!paramArg[j].isNull()) {
          long size = paramArg[j].getBoundLength();
          if (paramArg[j].isStream()) {
            if (size != -1L)
              sizeOfParameterSet += size; 
          } else {
            sizeOfParameterSet += size;
          } 
        } 
      } 
      sizeOfEntireBatch += sizeOfParameterSet;
      if (sizeOfParameterSet > maxSizeOfParameterSet)
        maxSizeOfParameterSet = sizeOfParameterSet; 
    } 
    return new long[] { maxSizeOfParameterSet, sizeOfEntireBatch };
  }
  
  private String truncateQueryToLog(String sql) {
    String queryStr = null;
    int maxQuerySizeToLog = ((Integer)this.session.getPropertySet().getIntegerProperty(PropertyKey.maxQuerySizeToLog).getValue()).intValue();
    if (sql.length() > maxQuerySizeToLog) {
      StringBuilder queryBuf = new StringBuilder(maxQuerySizeToLog + 12);
      queryBuf.append(sql.substring(0, maxQuerySizeToLog));
      queryBuf.append(Messages.getString("MysqlIO.25"));
      queryStr = queryBuf.toString();
    } else {
      queryStr = sql;
    } 
    return queryStr;
  }
  
  public <M extends com.mysql.cj.protocol.Message> M fillSendPacket() {
    return null;
  }
  
  public <M extends com.mysql.cj.protocol.Message> M fillSendPacket(QueryBindings<?> bindings) {
    return null;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\ServerPreparedQuery.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
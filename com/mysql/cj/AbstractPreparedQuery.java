package com.mysql.cj;

import com.mysql.cj.conf.PropertyKey;
import com.mysql.cj.conf.RuntimeProperty;
import com.mysql.cj.exceptions.ExceptionFactory;
import com.mysql.cj.exceptions.WrongArgumentException;
import com.mysql.cj.protocol.a.NativeConstants;
import com.mysql.cj.protocol.a.NativePacketPayload;
import com.mysql.cj.util.StringUtils;
import com.mysql.cj.util.Util;
import java.io.IOException;
import java.io.InputStream;

public abstract class AbstractPreparedQuery<T extends QueryBindings<?>> extends AbstractQuery implements PreparedQuery<T> {
  protected ParseInfo parseInfo;
  
  protected T queryBindings = null;
  
  protected String originalSql = null;
  
  protected int parameterCount;
  
  protected RuntimeProperty<Boolean> autoClosePStmtStreams;
  
  protected int batchCommandIndex = -1;
  
  protected RuntimeProperty<Boolean> useStreamLengthsInPrepStmts;
  
  private byte[] streamConvertBuf = null;
  
  private boolean usingAnsiMode;
  
  public AbstractPreparedQuery(NativeSession sess) {
    super(sess);
    this.autoClosePStmtStreams = this.session.getPropertySet().getBooleanProperty(PropertyKey.autoClosePStmtStreams);
    this.useStreamLengthsInPrepStmts = this.session.getPropertySet().getBooleanProperty(PropertyKey.useStreamLengthsInPrepStmts);
    this.usingAnsiMode = !this.session.getServerSession().useAnsiQuotedIdentifiers();
  }
  
  public void closeQuery() {
    this.streamConvertBuf = null;
    super.closeQuery();
  }
  
  public ParseInfo getParseInfo() {
    return this.parseInfo;
  }
  
  public void setParseInfo(ParseInfo parseInfo) {
    this.parseInfo = parseInfo;
  }
  
  public String getOriginalSql() {
    return this.originalSql;
  }
  
  public void setOriginalSql(String originalSql) {
    this.originalSql = originalSql;
  }
  
  public int getParameterCount() {
    return this.parameterCount;
  }
  
  public void setParameterCount(int parameterCount) {
    this.parameterCount = parameterCount;
  }
  
  public T getQueryBindings() {
    return this.queryBindings;
  }
  
  public void setQueryBindings(T queryBindings) {
    this.queryBindings = queryBindings;
  }
  
  public int getBatchCommandIndex() {
    return this.batchCommandIndex;
  }
  
  public void setBatchCommandIndex(int batchCommandIndex) {
    this.batchCommandIndex = batchCommandIndex;
  }
  
  public int computeBatchSize(int numBatchedArgs) {
    long[] combinedValues = computeMaxParameterSetSizeAndBatchSize(numBatchedArgs);
    long maxSizeOfParameterSet = combinedValues[0];
    long sizeOfEntireBatch = combinedValues[1];
    if (sizeOfEntireBatch < (((Integer)this.maxAllowedPacket.getValue()).intValue() - this.originalSql.length()))
      return numBatchedArgs; 
    return (int)Math.max(1L, (((Integer)this.maxAllowedPacket.getValue()).intValue() - this.originalSql.length()) / maxSizeOfParameterSet);
  }
  
  public void checkNullOrEmptyQuery(String sql) {
    if (sql == null)
      throw (WrongArgumentException)ExceptionFactory.createException(WrongArgumentException.class, Messages.getString("PreparedQuery.0"), this.session.getExceptionInterceptor()); 
    if (sql.length() == 0)
      throw (WrongArgumentException)ExceptionFactory.createException(WrongArgumentException.class, Messages.getString("PreparedQuery.1"), this.session.getExceptionInterceptor()); 
  }
  
  public String asSql() {
    return asSql(false);
  }
  
  public String asSql(boolean quoteStreamsAndUnknowns) {
    StringBuilder buf = new StringBuilder();
    Object batchArg = null;
    if (this.batchCommandIndex != -1)
      batchArg = this.batchedArgs.get(this.batchCommandIndex); 
    byte[][] staticSqlStrings = this.parseInfo.getStaticSql();
    for (int i = 0; i < this.parameterCount; i++) {
      buf.append((this.charEncoding != null) ? StringUtils.toString(staticSqlStrings[i], this.charEncoding) : StringUtils.toString(staticSqlStrings[i]));
      byte[] val = null;
      if (batchArg != null && batchArg instanceof String) {
        buf.append((String)batchArg);
      } else {
        val = (this.batchCommandIndex == -1) ? ((this.queryBindings == null) ? null : this.queryBindings.getBindValues()[i].getByteValue()) : ((QueryBindings)batchArg).getBindValues()[i].getByteValue();
        boolean isStreamParam = (this.batchCommandIndex == -1) ? ((this.queryBindings == null) ? false : this.queryBindings.getBindValues()[i].isStream()) : ((QueryBindings)batchArg).getBindValues()[i].isStream();
        if (val == null && !isStreamParam) {
          buf.append(quoteStreamsAndUnknowns ? "'** NOT SPECIFIED **'" : "** NOT SPECIFIED **");
        } else if (isStreamParam) {
          buf.append(quoteStreamsAndUnknowns ? "'** STREAM DATA **'" : "** STREAM DATA **");
        } else {
          buf.append(StringUtils.toString(val, this.charEncoding));
        } 
      } 
    } 
    buf.append((this.charEncoding != null) ? StringUtils.toString(staticSqlStrings[this.parameterCount], this.charEncoding) : 
        StringUtils.toAsciiString(staticSqlStrings[this.parameterCount]));
    return buf.toString();
  }
  
  protected abstract long[] computeMaxParameterSetSizeAndBatchSize(int paramInt);
  
  public <M extends com.mysql.cj.protocol.Message> M fillSendPacket() {
    synchronized (this) {
      return fillSendPacket((QueryBindings<?>)this.queryBindings);
    } 
  }
  
  public <M extends com.mysql.cj.protocol.Message> M fillSendPacket(QueryBindings<?> bindings) {
    synchronized (this) {
      BindValue[] bindValues = (BindValue[])bindings.getBindValues();
      NativePacketPayload sendPacket = this.session.getSharedSendPacket();
      sendPacket.writeInteger(NativeConstants.IntegerDataType.INT1, 3L);
      boolean useStreamLengths = ((Boolean)this.useStreamLengthsInPrepStmts.getValue()).booleanValue();
      int ensurePacketSize = 0;
      String statementComment = this.session.getProtocol().getQueryComment();
      byte[] commentAsBytes = null;
      if (statementComment != null) {
        commentAsBytes = StringUtils.getBytes(statementComment, this.charEncoding);
        ensurePacketSize += commentAsBytes.length;
        ensurePacketSize += 6;
      } 
      for (int i = 0; i < bindValues.length; i++) {
        if (bindValues[i].isStream() && useStreamLengths)
          ensurePacketSize = (int)(ensurePacketSize + bindValues[i].getStreamLength()); 
      } 
      if (ensurePacketSize != 0)
        sendPacket.ensureCapacity(ensurePacketSize); 
      if (commentAsBytes != null) {
        sendPacket.writeBytes(NativeConstants.StringLengthDataType.STRING_FIXED, Constants.SLASH_STAR_SPACE_AS_BYTES);
        sendPacket.writeBytes(NativeConstants.StringLengthDataType.STRING_FIXED, commentAsBytes);
        sendPacket.writeBytes(NativeConstants.StringLengthDataType.STRING_FIXED, Constants.SPACE_STAR_SLASH_SPACE_AS_BYTES);
      } 
      byte[][] staticSqlStrings = this.parseInfo.getStaticSql();
      for (int j = 0; j < bindValues.length; j++) {
        bindings.checkParameterSet(j);
        sendPacket.writeBytes(NativeConstants.StringLengthDataType.STRING_FIXED, staticSqlStrings[j]);
        if (bindValues[j].isStream()) {
          streamToBytes(sendPacket, bindValues[j].getStreamValue(), true, bindValues[j].getStreamLength(), useStreamLengths);
        } else {
          sendPacket.writeBytes(NativeConstants.StringLengthDataType.STRING_FIXED, bindValues[j].getByteValue());
        } 
      } 
      sendPacket.writeBytes(NativeConstants.StringLengthDataType.STRING_FIXED, staticSqlStrings[bindValues.length]);
      return (M)sendPacket;
    } 
  }
  
  private final void streamToBytes(NativePacketPayload packet, InputStream in, boolean escape, long streamLength, boolean useLength) {
    try {
      if (this.streamConvertBuf == null)
        this.streamConvertBuf = new byte[4096]; 
      boolean hexEscape = this.session.getServerSession().isNoBackslashEscapesSet();
      if (streamLength == -1L)
        useLength = false; 
      int bc = useLength ? Util.readBlock(in, this.streamConvertBuf, (int)streamLength, this.session.getExceptionInterceptor()) : Util.readBlock(in, this.streamConvertBuf, this.session.getExceptionInterceptor());
      int lengthLeftToRead = (int)streamLength - bc;
      packet.writeBytes(NativeConstants.StringLengthDataType.STRING_FIXED, StringUtils.getBytes(hexEscape ? "x" : "_binary"));
      if (escape)
        packet.writeInteger(NativeConstants.IntegerDataType.INT1, 39L); 
      while (bc > 0) {
        if (hexEscape) {
          ((AbstractQueryBindings)this.queryBindings).hexEscapeBlock(this.streamConvertBuf, packet, bc);
        } else if (escape) {
          escapeblockFast(this.streamConvertBuf, packet, bc);
        } else {
          packet.writeBytes(NativeConstants.StringLengthDataType.STRING_FIXED, this.streamConvertBuf, 0, bc);
        } 
        if (useLength) {
          bc = Util.readBlock(in, this.streamConvertBuf, lengthLeftToRead, this.session.getExceptionInterceptor());
          if (bc > 0)
            lengthLeftToRead -= bc; 
          continue;
        } 
        bc = Util.readBlock(in, this.streamConvertBuf, this.session.getExceptionInterceptor());
      } 
      if (escape)
        packet.writeInteger(NativeConstants.IntegerDataType.INT1, 39L); 
    } finally {
      if (((Boolean)this.autoClosePStmtStreams.getValue()).booleanValue()) {
        try {
          in.close();
        } catch (IOException iOException) {}
        in = null;
      } 
    } 
  }
  
  private final void escapeblockFast(byte[] buf, NativePacketPayload packet, int size) {
    int lastwritten = 0;
    for (int i = 0; i < size; i++) {
      byte b = buf[i];
      if (b == 0) {
        if (i > lastwritten)
          packet.writeBytes(NativeConstants.StringLengthDataType.STRING_FIXED, buf, lastwritten, i - lastwritten); 
        packet.writeInteger(NativeConstants.IntegerDataType.INT1, 92L);
        packet.writeInteger(NativeConstants.IntegerDataType.INT1, 48L);
        lastwritten = i + 1;
      } else if (b == 92 || b == 39 || (!this.usingAnsiMode && b == 34)) {
        if (i > lastwritten)
          packet.writeBytes(NativeConstants.StringLengthDataType.STRING_FIXED, buf, lastwritten, i - lastwritten); 
        packet.writeInteger(NativeConstants.IntegerDataType.INT1, 92L);
        lastwritten = i;
      } 
    } 
    if (lastwritten < size)
      packet.writeBytes(NativeConstants.StringLengthDataType.STRING_FIXED, buf, lastwritten, size - lastwritten); 
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\AbstractPreparedQuery.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
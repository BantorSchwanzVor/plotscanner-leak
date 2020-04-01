package com.mysql.cj.jdbc;

import com.mysql.cj.Constants;
import com.mysql.cj.Messages;
import com.mysql.cj.exceptions.CJException;
import com.mysql.cj.exceptions.ExceptionInterceptor;
import com.mysql.cj.jdbc.exceptions.SQLError;
import com.mysql.cj.jdbc.exceptions.SQLExceptionsMapping;
import com.mysql.cj.jdbc.result.ResultSetInternalMethods;
import com.mysql.cj.protocol.OutputStreamWatcher;
import com.mysql.cj.protocol.WatchableOutputStream;
import com.mysql.cj.protocol.WatchableStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.SQLException;

public class Blob implements Blob, OutputStreamWatcher {
  private byte[] binaryData = null;
  
  private boolean isClosed = false;
  
  private ExceptionInterceptor exceptionInterceptor;
  
  Blob(ExceptionInterceptor exceptionInterceptor) {
    setBinaryData(Constants.EMPTY_BYTE_ARRAY);
    this.exceptionInterceptor = exceptionInterceptor;
  }
  
  public Blob(byte[] data, ExceptionInterceptor exceptionInterceptor) {
    setBinaryData(data);
    this.exceptionInterceptor = exceptionInterceptor;
  }
  
  Blob(byte[] data, ResultSetInternalMethods creatorResultSetToSet, int columnIndexToSet) {
    setBinaryData(data);
  }
  
  private synchronized byte[] getBinaryData() {
    return this.binaryData;
  }
  
  public synchronized InputStream getBinaryStream() throws SQLException {
    try {
      checkClosed();
      return new ByteArrayInputStream(getBinaryData());
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, this.exceptionInterceptor);
    } 
  }
  
  public synchronized byte[] getBytes(long pos, int length) throws SQLException {
    try {
      checkClosed();
      if (pos < 1L)
        throw SQLError.createSQLException(Messages.getString("Blob.2"), "S1009", this.exceptionInterceptor); 
      pos--;
      if (pos > this.binaryData.length)
        throw SQLError.createSQLException(Messages.getString("Blob.3"), "S1009", this.exceptionInterceptor); 
      if (pos + length > this.binaryData.length)
        throw SQLError.createSQLException(Messages.getString("Blob.4"), "S1009", this.exceptionInterceptor); 
      byte[] newData = new byte[length];
      System.arraycopy(getBinaryData(), (int)pos, newData, 0, length);
      return newData;
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, this.exceptionInterceptor);
    } 
  }
  
  public synchronized long length() throws SQLException {
    try {
      checkClosed();
      return (getBinaryData()).length;
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, this.exceptionInterceptor);
    } 
  }
  
  public synchronized long position(byte[] pattern, long start) throws SQLException {
    try {
      throw SQLError.createSQLFeatureNotSupportedException();
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, this.exceptionInterceptor);
    } 
  }
  
  public synchronized long position(Blob pattern, long start) throws SQLException {
    try {
      checkClosed();
      return position(pattern.getBytes(0L, (int)pattern.length()), start);
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, this.exceptionInterceptor);
    } 
  }
  
  private synchronized void setBinaryData(byte[] newBinaryData) {
    this.binaryData = newBinaryData;
  }
  
  public synchronized OutputStream setBinaryStream(long indexToWriteAt) throws SQLException {
    try {
      checkClosed();
      if (indexToWriteAt < 1L)
        throw SQLError.createSQLException(Messages.getString("Blob.0"), "S1009", this.exceptionInterceptor); 
      WatchableOutputStream bytesOut = new WatchableOutputStream();
      bytesOut.setWatcher(this);
      if (indexToWriteAt > 0L)
        bytesOut.write(this.binaryData, 0, (int)(indexToWriteAt - 1L)); 
      return (OutputStream)bytesOut;
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, this.exceptionInterceptor);
    } 
  }
  
  public synchronized int setBytes(long writeAt, byte[] bytes) throws SQLException {
    try {
      checkClosed();
      return setBytes(writeAt, bytes, 0, bytes.length);
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, this.exceptionInterceptor);
    } 
  }
  
  public synchronized int setBytes(long writeAt, byte[] bytes, int offset, int length) throws SQLException {
    try {
      checkClosed();
      OutputStream bytesOut = setBinaryStream(writeAt);
      try {
        bytesOut.write(bytes, offset, length);
      } catch (IOException ioEx) {
        SQLException sqlEx = SQLError.createSQLException(Messages.getString("Blob.1"), "S1000", this.exceptionInterceptor);
        sqlEx.initCause(ioEx);
        throw sqlEx;
      } finally {
        try {
          bytesOut.close();
        } catch (IOException iOException) {}
      } 
      return length;
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, this.exceptionInterceptor);
    } 
  }
  
  public synchronized void streamClosed(byte[] byteData) {
    this.binaryData = byteData;
  }
  
  public synchronized void streamClosed(WatchableStream out) {
    int streamSize = out.size();
    if (streamSize < this.binaryData.length)
      out.write(this.binaryData, streamSize, this.binaryData.length - streamSize); 
    this.binaryData = out.toByteArray();
  }
  
  public synchronized void truncate(long len) throws SQLException {
    try {
      checkClosed();
      if (len < 0L)
        throw SQLError.createSQLException(Messages.getString("Blob.5"), "S1009", this.exceptionInterceptor); 
      if (len > this.binaryData.length)
        throw SQLError.createSQLException(Messages.getString("Blob.6"), "S1009", this.exceptionInterceptor); 
      byte[] newData = new byte[(int)len];
      System.arraycopy(getBinaryData(), 0, newData, 0, (int)len);
      this.binaryData = newData;
      return;
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, this.exceptionInterceptor);
    } 
  }
  
  public synchronized void free() throws SQLException {
    try {
      this.binaryData = null;
      this.isClosed = true;
      return;
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, this.exceptionInterceptor);
    } 
  }
  
  public synchronized InputStream getBinaryStream(long pos, long length) throws SQLException {
    try {
      checkClosed();
      if (pos < 1L)
        throw SQLError.createSQLException(Messages.getString("Blob.2"), "S1009", this.exceptionInterceptor); 
      pos--;
      if (pos > this.binaryData.length)
        throw SQLError.createSQLException(Messages.getString("Blob.6"), "S1009", this.exceptionInterceptor); 
      if (pos + length > this.binaryData.length)
        throw SQLError.createSQLException(Messages.getString("Blob.4"), "S1009", this.exceptionInterceptor); 
      return new ByteArrayInputStream(getBinaryData(), (int)pos, (int)length);
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, this.exceptionInterceptor);
    } 
  }
  
  private synchronized void checkClosed() throws SQLException {
    if (this.isClosed)
      throw SQLError.createSQLException(Messages.getString("Blob.7"), "S1009", this.exceptionInterceptor); 
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\jdbc\Blob.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
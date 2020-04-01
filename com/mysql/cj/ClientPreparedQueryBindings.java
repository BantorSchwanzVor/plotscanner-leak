package com.mysql.cj;

import com.mysql.cj.conf.PropertyKey;
import com.mysql.cj.exceptions.ExceptionFactory;
import com.mysql.cj.exceptions.WrongArgumentException;
import com.mysql.cj.util.StringUtils;
import com.mysql.cj.util.TimeUtil;
import com.mysql.cj.util.Util;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ClientPreparedQueryBindings extends AbstractQueryBindings<ClientPreparedQueryBindValue> {
  private CharsetEncoder charsetEncoder;
  
  private SimpleDateFormat ddf;
  
  private SimpleDateFormat tdf;
  
  private SimpleDateFormat tsdf = null;
  
  public ClientPreparedQueryBindings(int parameterCount, Session sess) {
    super(parameterCount, sess);
    if (((NativeSession)this.session).getRequiresEscapingEncoder())
      this.charsetEncoder = Charset.forName(this.charEncoding).newEncoder(); 
  }
  
  protected void initBindValues(int parameterCount) {
    this.bindValues = new ClientPreparedQueryBindValue[parameterCount];
    for (int i = 0; i < parameterCount; i++)
      this.bindValues[i] = new ClientPreparedQueryBindValue(); 
  }
  
  public ClientPreparedQueryBindings clone() {
    ClientPreparedQueryBindings newBindings = new ClientPreparedQueryBindings(this.bindValues.length, this.session);
    ClientPreparedQueryBindValue[] bvs = new ClientPreparedQueryBindValue[this.bindValues.length];
    for (int i = 0; i < this.bindValues.length; i++)
      bvs[i] = this.bindValues[i].clone(); 
    newBindings.setBindValues(bvs);
    newBindings.isLoadDataQuery = this.isLoadDataQuery;
    return newBindings;
  }
  
  public void checkParameterSet(int columnIndex) {
    if (!this.bindValues[columnIndex].isSet())
      throw ExceptionFactory.createException(Messages.getString("PreparedStatement.40") + (columnIndex + 1), "07001", 0, true, null, this.session
          .getExceptionInterceptor()); 
  }
  
  public void setAsciiStream(int parameterIndex, InputStream x) {
    setAsciiStream(parameterIndex, x, -1);
  }
  
  public void setAsciiStream(int parameterIndex, InputStream x, int length) {
    if (x == null) {
      setNull(parameterIndex);
    } else {
      setBinaryStream(parameterIndex, x, length);
    } 
  }
  
  public void setAsciiStream(int parameterIndex, InputStream x, long length) {
    setAsciiStream(parameterIndex, x, (int)length);
    this.bindValues[parameterIndex].setMysqlType(MysqlType.TEXT);
  }
  
  public void setBigDecimal(int parameterIndex, BigDecimal x) {
    if (x == null) {
      setNull(parameterIndex);
    } else {
      setValue(parameterIndex, StringUtils.fixDecimalExponent(x.toPlainString()), MysqlType.DECIMAL);
    } 
  }
  
  public void setBigInteger(int parameterIndex, BigInteger x) {
    setValue(parameterIndex, x.toString(), MysqlType.BIGINT_UNSIGNED);
  }
  
  public void setBinaryStream(int parameterIndex, InputStream x) {
    setBinaryStream(parameterIndex, x, -1);
  }
  
  public void setBinaryStream(int parameterIndex, InputStream x, int length) {
    if (x == null) {
      setNull(parameterIndex);
    } else {
      this.bindValues[parameterIndex].setNull(false);
      this.bindValues[parameterIndex].setIsStream(true);
      this.bindValues[parameterIndex].setMysqlType(MysqlType.BLOB);
      this.bindValues[parameterIndex].setStreamValue(x, length);
    } 
  }
  
  public void setBinaryStream(int parameterIndex, InputStream x, long length) {
    setBinaryStream(parameterIndex, x, (int)length);
  }
  
  public void setBlob(int parameterIndex, InputStream inputStream) {
    setBinaryStream(parameterIndex, inputStream);
  }
  
  public void setBlob(int parameterIndex, InputStream inputStream, long length) {
    setBinaryStream(parameterIndex, inputStream, (int)length);
  }
  
  public void setBlob(int parameterIndex, Blob x) {
    if (x == null) {
      setNull(parameterIndex);
    } else {
      try {
        setBinaryStream(parameterIndex, x.getBinaryStream());
      } catch (Throwable t) {
        throw ExceptionFactory.createException(t.getMessage(), t, this.session.getExceptionInterceptor());
      } 
    } 
  }
  
  public void setBoolean(int parameterIndex, boolean x) {
    setValue(parameterIndex, x ? "1" : "0", MysqlType.BOOLEAN);
  }
  
  public void setByte(int parameterIndex, byte x) {
    setValue(parameterIndex, String.valueOf(x), MysqlType.TINYINT);
  }
  
  public void setBytes(int parameterIndex, byte[] x) {
    setBytes(parameterIndex, x, true, true);
    if (x != null)
      this.bindValues[parameterIndex].setMysqlType(MysqlType.BINARY); 
  }
  
  public synchronized void setBytes(int parameterIndex, byte[] x, boolean checkForIntroducer, boolean escapeForMBChars) {
    if (x == null) {
      setNull(parameterIndex);
    } else {
      if (this.session.getServerSession().isNoBackslashEscapesSet() || (escapeForMBChars && CharsetMapping.isMultibyteCharset(this.charEncoding))) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(x.length * 2 + 3);
        byteArrayOutputStream.write(120);
        byteArrayOutputStream.write(39);
        for (int j = 0; j < x.length; j++) {
          int lowBits = (x[j] & 0xFF) / 16;
          int highBits = (x[j] & 0xFF) % 16;
          byteArrayOutputStream.write(HEX_DIGITS[lowBits]);
          byteArrayOutputStream.write(HEX_DIGITS[highBits]);
        } 
        byteArrayOutputStream.write(39);
        setValue(parameterIndex, byteArrayOutputStream.toByteArray(), MysqlType.BINARY);
        setOrigValue(parameterIndex, x);
        return;
      } 
      int numBytes = x.length;
      int pad = 2;
      if (checkForIntroducer)
        pad += 7; 
      ByteArrayOutputStream bOut = new ByteArrayOutputStream(numBytes + pad);
      if (checkForIntroducer) {
        bOut.write(95);
        bOut.write(98);
        bOut.write(105);
        bOut.write(110);
        bOut.write(97);
        bOut.write(114);
        bOut.write(121);
      } 
      bOut.write(39);
      for (int i = 0; i < numBytes; i++) {
        byte b = x[i];
        switch (b) {
          case 0:
            bOut.write(92);
            bOut.write(48);
            break;
          case 10:
            bOut.write(92);
            bOut.write(110);
            break;
          case 13:
            bOut.write(92);
            bOut.write(114);
            break;
          case 92:
            bOut.write(92);
            bOut.write(92);
            break;
          case 39:
            bOut.write(92);
            bOut.write(39);
            break;
          case 34:
            bOut.write(92);
            bOut.write(34);
            break;
          case 26:
            bOut.write(92);
            bOut.write(90);
            break;
          default:
            bOut.write(b);
            break;
        } 
      } 
      bOut.write(39);
      setValue(parameterIndex, bOut.toByteArray(), MysqlType.BINARY);
    } 
  }
  
  public void setBytesNoEscape(int parameterIndex, byte[] parameterAsBytes) {
    byte[] parameterWithQuotes = StringUtils.quoteBytes(parameterAsBytes);
    setValue(parameterIndex, parameterWithQuotes, MysqlType.BINARY);
  }
  
  public void setBytesNoEscapeNoQuotes(int parameterIndex, byte[] parameterAsBytes) {
    setValue(parameterIndex, parameterAsBytes, MysqlType.BINARY);
  }
  
  public void setCharacterStream(int parameterIndex, Reader reader) {
    setCharacterStream(parameterIndex, reader, -1);
  }
  
  public void setCharacterStream(int parameterIndex, Reader reader, int length) {
    try {
      if (reader == null) {
        setNull(parameterIndex);
      } else {
        char[] c = null;
        int len = 0;
        boolean useLength = ((Boolean)this.useStreamLengthsInPrepStmts.getValue()).booleanValue();
        String forcedEncoding = this.session.getPropertySet().getStringProperty(PropertyKey.clobCharacterEncoding).getStringValue();
        if (useLength && length != -1) {
          c = new char[length];
          int numCharsRead = Util.readFully(reader, c, length);
          if (forcedEncoding == null) {
            setString(parameterIndex, new String(c, 0, numCharsRead));
          } else {
            setBytes(parameterIndex, StringUtils.getBytes(new String(c, 0, numCharsRead), forcedEncoding));
          } 
        } else {
          c = new char[4096];
          StringBuilder buf = new StringBuilder();
          while ((len = reader.read(c)) != -1)
            buf.append(c, 0, len); 
          if (forcedEncoding == null) {
            setString(parameterIndex, buf.toString());
          } else {
            setBytes(parameterIndex, StringUtils.getBytes(buf.toString(), forcedEncoding));
          } 
        } 
        this.bindValues[parameterIndex].setMysqlType(MysqlType.TEXT);
      } 
    } catch (UnsupportedEncodingException uec) {
      throw (WrongArgumentException)ExceptionFactory.createException(WrongArgumentException.class, uec.toString(), uec, this.session.getExceptionInterceptor());
    } catch (IOException ioEx) {
      throw ExceptionFactory.createException(ioEx.toString(), ioEx, this.session.getExceptionInterceptor());
    } 
  }
  
  public void setCharacterStream(int parameterIndex, Reader reader, long length) {
    setCharacterStream(parameterIndex, reader, (int)length);
  }
  
  public void setClob(int parameterIndex, Reader reader) {
    setCharacterStream(parameterIndex, reader);
  }
  
  public void setClob(int parameterIndex, Reader reader, long length) {
    setCharacterStream(parameterIndex, reader, length);
  }
  
  public void setClob(int i, Clob x) {
    if (x == null) {
      setNull(i);
    } else {
      try {
        String forcedEncoding = this.session.getPropertySet().getStringProperty(PropertyKey.clobCharacterEncoding).getStringValue();
        if (forcedEncoding == null) {
          setString(i, x.getSubString(1L, (int)x.length()));
        } else {
          setBytes(i, StringUtils.getBytes(x.getSubString(1L, (int)x.length()), forcedEncoding));
        } 
        this.bindValues[i].setMysqlType(MysqlType.TEXT);
      } catch (Throwable t) {
        throw ExceptionFactory.createException(t.getMessage(), t);
      } 
    } 
  }
  
  public void setDate(int parameterIndex, Date x) {
    setDate(parameterIndex, x, (Calendar)null);
  }
  
  public void setDate(int parameterIndex, Date x, Calendar cal) {
    if (x == null) {
      setNull(parameterIndex);
    } else {
      this.ddf = TimeUtil.getSimpleDateFormat(this.ddf, "''yyyy-MM-dd''", cal, (cal != null) ? null : this.session.getServerSession().getDefaultTimeZone());
      setValue(parameterIndex, this.ddf.format(x), MysqlType.DATE);
    } 
  }
  
  public void setDouble(int parameterIndex, double x) {
    if (!((Boolean)this.session.getPropertySet().getBooleanProperty(PropertyKey.allowNanAndInf).getValue()).booleanValue() && (x == Double.POSITIVE_INFINITY || x == Double.NEGATIVE_INFINITY || 
      Double.isNaN(x)))
      throw (WrongArgumentException)ExceptionFactory.createException(WrongArgumentException.class, Messages.getString("PreparedStatement.64", new Object[] { Double.valueOf(x) }), this.session
          .getExceptionInterceptor()); 
    setValue(parameterIndex, StringUtils.fixDecimalExponent(String.valueOf(x)), MysqlType.DOUBLE);
  }
  
  public void setFloat(int parameterIndex, float x) {
    setValue(parameterIndex, StringUtils.fixDecimalExponent(String.valueOf(x)), MysqlType.FLOAT);
  }
  
  public void setInt(int parameterIndex, int x) {
    setValue(parameterIndex, String.valueOf(x), MysqlType.INT);
  }
  
  public void setLong(int parameterIndex, long x) {
    setValue(parameterIndex, String.valueOf(x), MysqlType.BIGINT);
  }
  
  public void setNCharacterStream(int parameterIndex, Reader value) {
    setNCharacterStream(parameterIndex, value, -1L);
  }
  
  public void setNCharacterStream(int parameterIndex, Reader reader, long length) {
    if (reader == null) {
      setNull(parameterIndex);
    } else {
      try {
        char[] c = null;
        int len = 0;
        boolean useLength = ((Boolean)this.useStreamLengthsInPrepStmts.getValue()).booleanValue();
        if (useLength && length != -1L) {
          c = new char[(int)length];
          int numCharsRead = Util.readFully(reader, c, (int)length);
          setNString(parameterIndex, new String(c, 0, numCharsRead));
        } else {
          c = new char[4096];
          StringBuilder buf = new StringBuilder();
          while ((len = reader.read(c)) != -1)
            buf.append(c, 0, len); 
          setNString(parameterIndex, buf.toString());
        } 
        this.bindValues[parameterIndex].setMysqlType(MysqlType.TEXT);
      } catch (Throwable t) {
        throw ExceptionFactory.createException(t.getMessage(), t, this.session.getExceptionInterceptor());
      } 
    } 
  }
  
  public void setNClob(int parameterIndex, Reader reader) {
    setNCharacterStream(parameterIndex, reader);
  }
  
  public void setNClob(int parameterIndex, Reader reader, long length) {
    if (reader == null) {
      setNull(parameterIndex);
    } else {
      setNCharacterStream(parameterIndex, reader, length);
    } 
  }
  
  public void setNClob(int parameterIndex, NClob value) {
    if (value == null) {
      setNull(parameterIndex);
    } else {
      try {
        setNCharacterStream(parameterIndex, value.getCharacterStream(), value.length());
      } catch (Throwable t) {
        throw ExceptionFactory.createException(t.getMessage(), t, this.session.getExceptionInterceptor());
      } 
    } 
  }
  
  public void setNString(int parameterIndex, String x) {
    if (x == null) {
      setNull(parameterIndex);
    } else {
      if (this.charEncoding.equalsIgnoreCase("UTF-8") || this.charEncoding.equalsIgnoreCase("utf8")) {
        setString(parameterIndex, x);
        return;
      } 
      int stringLength = x.length();
      StringBuilder buf = new StringBuilder((int)(x.length() * 1.1D + 4.0D));
      buf.append("_utf8");
      buf.append('\'');
      for (int i = 0; i < stringLength; i++) {
        char c = x.charAt(i);
        switch (c) {
          case '\000':
            buf.append('\\');
            buf.append('0');
            break;
          case '\n':
            buf.append('\\');
            buf.append('n');
            break;
          case '\r':
            buf.append('\\');
            buf.append('r');
            break;
          case '\\':
            buf.append('\\');
            buf.append('\\');
            break;
          case '\'':
            buf.append('\\');
            buf.append('\'');
            break;
          case '"':
            if (this.session.getServerSession().useAnsiQuotedIdentifiers())
              buf.append('\\'); 
            buf.append('"');
            break;
          case '\032':
            buf.append('\\');
            buf.append('Z');
            break;
          default:
            buf.append(c);
            break;
        } 
      } 
      buf.append('\'');
      byte[] parameterAsBytes = this.isLoadDataQuery ? StringUtils.getBytes(buf.toString()) : StringUtils.getBytes(buf.toString(), "UTF-8");
      setValue(parameterIndex, parameterAsBytes, MysqlType.VARCHAR);
    } 
  }
  
  public synchronized void setNull(int parameterIndex) {
    setValue(parameterIndex, "null", MysqlType.NULL);
    this.bindValues[parameterIndex].setNull(true);
  }
  
  public void setShort(int parameterIndex, short x) {
    setValue(parameterIndex, String.valueOf(x), MysqlType.SMALLINT);
  }
  
  public void setString(int parameterIndex, String x) {
    if (x == null) {
      setNull(parameterIndex);
    } else {
      int stringLength = x.length();
      if (this.session.getServerSession().isNoBackslashEscapesSet()) {
        boolean needsHexEscape = isEscapeNeededForString(x, stringLength);
        if (!needsHexEscape) {
          StringBuilder quotedString = new StringBuilder(x.length() + 2);
          quotedString.append('\'');
          quotedString.append(x);
          quotedString.append('\'');
          byte[] arrayOfByte = this.isLoadDataQuery ? StringUtils.getBytes(quotedString.toString()) : StringUtils.getBytes(quotedString.toString(), this.charEncoding);
          setValue(parameterIndex, arrayOfByte, MysqlType.VARCHAR);
        } else {
          byte[] arrayOfByte = this.isLoadDataQuery ? StringUtils.getBytes(x) : StringUtils.getBytes(x, this.charEncoding);
          setBytes(parameterIndex, arrayOfByte);
        } 
        return;
      } 
      String parameterAsString = x;
      boolean needsQuoted = true;
      if (this.isLoadDataQuery || isEscapeNeededForString(x, stringLength)) {
        needsQuoted = false;
        StringBuilder buf = new StringBuilder((int)(x.length() * 1.1D));
        buf.append('\'');
        for (int i = 0; i < stringLength; i++) {
          char c = x.charAt(i);
          switch (c) {
            case '\000':
              buf.append('\\');
              buf.append('0');
              break;
            case '\n':
              buf.append('\\');
              buf.append('n');
              break;
            case '\r':
              buf.append('\\');
              buf.append('r');
              break;
            case '\\':
              buf.append('\\');
              buf.append('\\');
              break;
            case '\'':
              buf.append('\'');
              buf.append('\'');
              break;
            case '"':
              if (this.session.getServerSession().useAnsiQuotedIdentifiers())
                buf.append('\\'); 
              buf.append('"');
              break;
            case '\032':
              buf.append('\\');
              buf.append('Z');
              break;
            case '¥':
            case '₩':
              if (this.charsetEncoder != null) {
                CharBuffer cbuf = CharBuffer.allocate(1);
                ByteBuffer bbuf = ByteBuffer.allocate(1);
                cbuf.put(c);
                cbuf.position(0);
                this.charsetEncoder.encode(cbuf, bbuf, true);
                if (bbuf.get(0) == 92)
                  buf.append('\\'); 
              } 
              buf.append(c);
              break;
            default:
              buf.append(c);
              break;
          } 
        } 
        buf.append('\'');
        parameterAsString = buf.toString();
      } 
      byte[] parameterAsBytes = this.isLoadDataQuery ? StringUtils.getBytes(parameterAsString) : (needsQuoted ? StringUtils.getBytesWrapped(parameterAsString, '\'', '\'', this.charEncoding) : StringUtils.getBytes(parameterAsString, this.charEncoding));
      setValue(parameterIndex, parameterAsBytes, MysqlType.VARCHAR);
    } 
  }
  
  private boolean isEscapeNeededForString(String x, int stringLength) {
    boolean needsHexEscape = false;
    for (int i = 0; i < stringLength; i++) {
      char c = x.charAt(i);
      switch (c) {
        case '\000':
        case '\n':
        case '\r':
        case '\032':
        case '"':
        case '\'':
        case '\\':
          needsHexEscape = true;
          break;
      } 
      if (needsHexEscape)
        break; 
    } 
    return needsHexEscape;
  }
  
  public void setTime(int parameterIndex, Time x, Calendar cal) {
    if (x == null) {
      setNull(parameterIndex);
    } else {
      this.tdf = TimeUtil.getSimpleDateFormat(this.tdf, "''HH:mm:ss''", cal, (cal != null) ? null : this.session.getServerSession().getDefaultTimeZone());
      setValue(parameterIndex, this.tdf.format(x), MysqlType.TIME);
    } 
  }
  
  public void setTime(int parameterIndex, Time x) {
    setTime(parameterIndex, x, (Calendar)null);
  }
  
  public void setTimestamp(int parameterIndex, Timestamp x) {
    int fractLen = -1;
    if (!((Boolean)this.sendFractionalSeconds.getValue()).booleanValue() || !this.session.getServerSession().getCapabilities().serverSupportsFracSecs()) {
      fractLen = 0;
    } else if (this.columnDefinition != null && parameterIndex <= (this.columnDefinition.getFields()).length && parameterIndex >= 0) {
      fractLen = this.columnDefinition.getFields()[parameterIndex].getDecimals();
    } 
    setTimestamp(parameterIndex, x, (Calendar)null, fractLen);
  }
  
  public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal) {
    int fractLen = -1;
    if (!((Boolean)this.sendFractionalSeconds.getValue()).booleanValue() || !this.session.getServerSession().getCapabilities().serverSupportsFracSecs()) {
      fractLen = 0;
    } else if (this.columnDefinition != null && parameterIndex <= (this.columnDefinition.getFields()).length && parameterIndex >= 0 && this.columnDefinition
      .getFields()[parameterIndex].getDecimals() > 0) {
      fractLen = this.columnDefinition.getFields()[parameterIndex].getDecimals();
    } 
    setTimestamp(parameterIndex, x, cal, fractLen);
  }
  
  public void setTimestamp(int parameterIndex, Timestamp x, Calendar targetCalendar, int fractionalLength) {
    if (x == null) {
      setNull(parameterIndex);
    } else {
      x = (Timestamp)x.clone();
      if (!this.session.getServerSession().getCapabilities().serverSupportsFracSecs() || (
        !((Boolean)this.sendFractionalSeconds.getValue()).booleanValue() && fractionalLength == 0))
        x = TimeUtil.truncateFractionalSeconds(x); 
      if (fractionalLength < 0)
        fractionalLength = 6; 
      x = TimeUtil.adjustTimestampNanosPrecision(x, fractionalLength, !this.session.getServerSession().isServerTruncatesFracSecs());
      this.tsdf = TimeUtil.getSimpleDateFormat(this.tsdf, "''yyyy-MM-dd HH:mm:ss", targetCalendar, (targetCalendar != null) ? null : this.session
          .getServerSession().getDefaultTimeZone());
      StringBuffer buf = new StringBuffer();
      buf.append(this.tsdf.format(x));
      if (this.session.getServerSession().getCapabilities().serverSupportsFracSecs()) {
        buf.append('.');
        buf.append(TimeUtil.formatNanos(x.getNanos(), 6));
      } 
      buf.append('\'');
      setValue(parameterIndex, buf.toString(), MysqlType.TIMESTAMP);
    } 
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\ClientPreparedQueryBindings.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
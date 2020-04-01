package com.mysql.cj;

import com.mysql.cj.exceptions.CJException;
import com.mysql.cj.exceptions.ExceptionFactory;
import com.mysql.cj.exceptions.ExceptionInterceptor;
import com.mysql.cj.protocol.a.NativeConstants;
import com.mysql.cj.protocol.a.NativePacketPayload;
import com.mysql.cj.util.StringUtils;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class ServerPreparedQueryBindValue extends ClientPreparedQueryBindValue implements BindValue {
  public long boundBeforeExecutionNum = 0L;
  
  public int bufferType;
  
  public Calendar calendar;
  
  private TimeZone defaultTimeZone;
  
  protected String charEncoding = null;
  
  public ServerPreparedQueryBindValue(TimeZone defaultTZ) {
    this.defaultTimeZone = defaultTZ;
  }
  
  public ServerPreparedQueryBindValue clone() {
    return new ServerPreparedQueryBindValue(this);
  }
  
  private ServerPreparedQueryBindValue(ServerPreparedQueryBindValue copyMe) {
    super(copyMe);
    this.defaultTimeZone = copyMe.defaultTimeZone;
    this.bufferType = copyMe.bufferType;
    this.calendar = copyMe.calendar;
    this.charEncoding = copyMe.charEncoding;
  }
  
  public void reset() {
    super.reset();
    this.calendar = null;
    this.charEncoding = null;
  }
  
  public boolean resetToType(int bufType, long numberOfExecutions) {
    boolean sendTypesToServer = false;
    reset();
    if (bufType != 6 || this.bufferType == 0)
      if (this.bufferType != bufType) {
        sendTypesToServer = true;
        this.bufferType = bufType;
      }  
    this.isSet = true;
    this.boundBeforeExecutionNum = numberOfExecutions;
    return sendTypesToServer;
  }
  
  public String toString() {
    return toString(false);
  }
  
  public String toString(boolean quoteIfNeeded) {
    if (this.isStream)
      return "' STREAM DATA '"; 
    if (this.isNull)
      return "NULL"; 
    switch (this.bufferType) {
      case 1:
      case 2:
      case 3:
      case 8:
        return String.valueOf(((Long)this.value).longValue());
      case 4:
        return String.valueOf(((Float)this.value).floatValue());
      case 5:
        return String.valueOf(((Double)this.value).doubleValue());
      case 7:
      case 10:
      case 11:
      case 12:
      case 15:
      case 253:
      case 254:
        if (quoteIfNeeded)
          return "'" + String.valueOf(this.value) + "'"; 
        return String.valueOf(this.value);
    } 
    if (this.value instanceof byte[])
      return "byte data"; 
    if (quoteIfNeeded)
      return "'" + String.valueOf(this.value) + "'"; 
    return String.valueOf(this.value);
  }
  
  public long getBoundLength() {
    if (this.isNull)
      return 0L; 
    if (this.isStream)
      return this.streamLength; 
    switch (this.bufferType) {
      case 1:
        return 1L;
      case 2:
        return 2L;
      case 3:
        return 4L;
      case 8:
        return 8L;
      case 4:
        return 4L;
      case 5:
        return 8L;
      case 11:
        return 9L;
      case 10:
        return 7L;
      case 7:
      case 12:
        return 11L;
      case 0:
      case 15:
      case 246:
      case 253:
      case 254:
        if (this.value instanceof byte[])
          return ((byte[])this.value).length; 
        return ((String)this.value).length();
    } 
    return 0L;
  }
  
  public void storeBinding(NativePacketPayload intoPacket, boolean isLoadDataQuery, String characterEncoding, ExceptionInterceptor interceptor) {
    synchronized (this) {
      try {
        switch (this.bufferType) {
          case 1:
            intoPacket.writeInteger(NativeConstants.IntegerDataType.INT1, ((Long)this.value).longValue());
            return;
          case 2:
            intoPacket.writeInteger(NativeConstants.IntegerDataType.INT2, ((Long)this.value).longValue());
            return;
          case 3:
            intoPacket.writeInteger(NativeConstants.IntegerDataType.INT4, ((Long)this.value).longValue());
            return;
          case 8:
            intoPacket.writeInteger(NativeConstants.IntegerDataType.INT8, ((Long)this.value).longValue());
            return;
          case 4:
            intoPacket.writeInteger(NativeConstants.IntegerDataType.INT4, Float.floatToIntBits(((Float)this.value).floatValue()));
            return;
          case 5:
            intoPacket.writeInteger(NativeConstants.IntegerDataType.INT8, Double.doubleToLongBits(((Double)this.value).doubleValue()));
            return;
          case 11:
            storeTime(intoPacket);
            return;
          case 7:
          case 10:
          case 12:
            storeDateTime(intoPacket);
            return;
          case 0:
          case 15:
          case 246:
          case 253:
          case 254:
            if (this.value instanceof byte[]) {
              intoPacket.writeBytes(NativeConstants.StringSelfDataType.STRING_LENENC, (byte[])this.value);
            } else if (!isLoadDataQuery) {
              intoPacket.writeBytes(NativeConstants.StringSelfDataType.STRING_LENENC, StringUtils.getBytes((String)this.value, characterEncoding));
            } else {
              intoPacket.writeBytes(NativeConstants.StringSelfDataType.STRING_LENENC, StringUtils.getBytes((String)this.value));
            } 
            return;
        } 
      } catch (CJException uEE) {
        throw ExceptionFactory.createException(Messages.getString("ServerPreparedStatement.22") + characterEncoding + "'", uEE, interceptor);
      } 
    } 
  }
  
  private void storeTime(NativePacketPayload intoPacket) {
    intoPacket.ensureCapacity(9);
    intoPacket.writeInteger(NativeConstants.IntegerDataType.INT1, 8L);
    intoPacket.writeInteger(NativeConstants.IntegerDataType.INT1, 0L);
    intoPacket.writeInteger(NativeConstants.IntegerDataType.INT4, 0L);
    if (this.calendar == null)
      this.calendar = Calendar.getInstance(this.defaultTimeZone, Locale.US); 
    this.calendar.setTime((Date)this.value);
    intoPacket.writeInteger(NativeConstants.IntegerDataType.INT1, this.calendar.get(11));
    intoPacket.writeInteger(NativeConstants.IntegerDataType.INT1, this.calendar.get(12));
    intoPacket.writeInteger(NativeConstants.IntegerDataType.INT1, this.calendar.get(13));
  }
  
  private void storeDateTime(NativePacketPayload intoPacket) {
    synchronized (this) {
      if (this.calendar == null)
        this.calendar = Calendar.getInstance(this.defaultTimeZone, Locale.US); 
      this.calendar.setTime((Date)this.value);
      if (this.value instanceof java.sql.Date) {
        this.calendar.set(11, 0);
        this.calendar.set(12, 0);
        this.calendar.set(13, 0);
      } 
      byte length = 7;
      if (this.value instanceof Timestamp)
        length = 11; 
      intoPacket.ensureCapacity(length);
      intoPacket.writeInteger(NativeConstants.IntegerDataType.INT1, length);
      int year = this.calendar.get(1);
      int month = this.calendar.get(2) + 1;
      int date = this.calendar.get(5);
      intoPacket.writeInteger(NativeConstants.IntegerDataType.INT2, year);
      intoPacket.writeInteger(NativeConstants.IntegerDataType.INT1, month);
      intoPacket.writeInteger(NativeConstants.IntegerDataType.INT1, date);
      if (this.value instanceof java.sql.Date) {
        intoPacket.writeInteger(NativeConstants.IntegerDataType.INT1, 0L);
        intoPacket.writeInteger(NativeConstants.IntegerDataType.INT1, 0L);
        intoPacket.writeInteger(NativeConstants.IntegerDataType.INT1, 0L);
      } else {
        intoPacket.writeInteger(NativeConstants.IntegerDataType.INT1, this.calendar.get(11));
        intoPacket.writeInteger(NativeConstants.IntegerDataType.INT1, this.calendar.get(12));
        intoPacket.writeInteger(NativeConstants.IntegerDataType.INT1, this.calendar.get(13));
      } 
      if (length == 11)
        intoPacket.writeInteger(NativeConstants.IntegerDataType.INT4, (((Timestamp)this.value).getNanos() / 1000)); 
    } 
  }
  
  public byte[] getByteValue() {
    if (!this.isStream)
      return (this.charEncoding != null) ? StringUtils.getBytes(toString(), this.charEncoding) : toString().getBytes(); 
    return null;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\ServerPreparedQueryBindValue.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
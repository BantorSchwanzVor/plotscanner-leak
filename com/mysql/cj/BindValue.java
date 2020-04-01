package com.mysql.cj;

import java.io.InputStream;

public interface BindValue {
  BindValue clone();
  
  void reset();
  
  boolean isNull();
  
  void setNull(boolean paramBoolean);
  
  boolean isStream();
  
  void setIsStream(boolean paramBoolean);
  
  MysqlType getMysqlType();
  
  void setMysqlType(MysqlType paramMysqlType);
  
  byte[] getByteValue();
  
  void setByteValue(byte[] paramArrayOfbyte);
  
  void setOrigByteValue(byte[] paramArrayOfbyte);
  
  byte[] getOrigByteValue();
  
  InputStream getStreamValue();
  
  void setStreamValue(InputStream paramInputStream, long paramLong);
  
  long getStreamLength();
  
  boolean isSet();
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\BindValue.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
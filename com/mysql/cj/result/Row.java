package com.mysql.cj.result;

import com.mysql.cj.Messages;
import com.mysql.cj.exceptions.CJOperationNotSupportedException;
import com.mysql.cj.exceptions.ExceptionFactory;
import com.mysql.cj.protocol.ColumnDefinition;
import com.mysql.cj.protocol.ProtocolEntity;

public interface Row extends ProtocolEntity {
  <T> T getValue(int paramInt, ValueFactory<T> paramValueFactory);
  
  default Row setMetadata(ColumnDefinition columnDefinition) {
    throw (CJOperationNotSupportedException)ExceptionFactory.createException(CJOperationNotSupportedException.class, Messages.getString("OperationNotSupportedException.0"));
  }
  
  default byte[] getBytes(int columnIndex) {
    throw (CJOperationNotSupportedException)ExceptionFactory.createException(CJOperationNotSupportedException.class, Messages.getString("OperationNotSupportedException.0"));
  }
  
  default void setBytes(int columnIndex, byte[] value) {
    throw (CJOperationNotSupportedException)ExceptionFactory.createException(CJOperationNotSupportedException.class, Messages.getString("OperationNotSupportedException.0"));
  }
  
  boolean getNull(int paramInt);
  
  boolean wasNull();
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\result\Row.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
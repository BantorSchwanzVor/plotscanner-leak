package com.mysql.cj.protocol;

public interface ResultBuilder<T> {
  boolean addProtocolEntity(ProtocolEntity paramProtocolEntity);
  
  T build();
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\protocol\ResultBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
package com.mysql.cj.protocol.a;

import com.mysql.cj.protocol.ColumnDefinition;
import com.mysql.cj.protocol.Message;
import com.mysql.cj.protocol.ProtocolEntityFactory;
import com.mysql.cj.result.DefaultColumnDefinition;
import com.mysql.cj.result.Field;

public class ColumnDefinitionFactory implements ProtocolEntityFactory<ColumnDefinition, NativePacketPayload> {
  protected long columnCount;
  
  protected ColumnDefinition columnDefinitionFromCache;
  
  public ColumnDefinitionFactory(long columnCount, ColumnDefinition columnDefinitionFromCache) {
    this.columnCount = columnCount;
    this.columnDefinitionFromCache = columnDefinitionFromCache;
  }
  
  public long getColumnCount() {
    return this.columnCount;
  }
  
  public ColumnDefinition getColumnDefinitionFromCache() {
    return this.columnDefinitionFromCache;
  }
  
  public ColumnDefinition createFromMessage(NativePacketPayload packetPayload) {
    return null;
  }
  
  public boolean mergeColumnDefinitions() {
    return false;
  }
  
  public ColumnDefinition createFromFields(Field[] fields) {
    return (ColumnDefinition)new DefaultColumnDefinition(fields);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\protocol\a\ColumnDefinitionFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
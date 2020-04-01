package com.mysql.cj.xdevapi;

import com.mysql.cj.conf.PropertySet;
import com.mysql.cj.protocol.ColumnDefinition;
import com.mysql.cj.protocol.ProtocolEntity;
import com.mysql.cj.protocol.ProtocolEntityFactory;
import com.mysql.cj.protocol.x.XMessage;
import com.mysql.cj.result.Row;
import java.util.TimeZone;

public class RowFactory implements ProtocolEntityFactory<Row, XMessage> {
  private ColumnDefinition metadata;
  
  private TimeZone defaultTimeZone;
  
  private PropertySet pset;
  
  public RowFactory(ColumnDefinition metadata, TimeZone defaultTimeZone, PropertySet pset) {
    this.metadata = metadata;
    this.defaultTimeZone = defaultTimeZone;
    this.pset = pset;
  }
  
  public Row createFromProtocolEntity(ProtocolEntity internalRow) {
    return new RowImpl((Row)internalRow, this.metadata, this.defaultTimeZone, this.pset);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\xdevapi\RowFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
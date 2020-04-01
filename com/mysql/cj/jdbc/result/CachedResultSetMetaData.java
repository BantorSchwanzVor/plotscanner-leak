package com.mysql.cj.jdbc.result;

import com.mysql.cj.protocol.ColumnDefinition;
import java.sql.ResultSetMetaData;

public interface CachedResultSetMetaData extends ColumnDefinition {
  ResultSetMetaData getMetadata();
  
  void setMetadata(ResultSetMetaData paramResultSetMetaData);
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\jdbc\result\CachedResultSetMetaData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
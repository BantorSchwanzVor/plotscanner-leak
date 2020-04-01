package com.mysql.cj.jdbc.result;

import com.mysql.cj.result.DefaultColumnDefinition;
import java.sql.ResultSetMetaData;

public class CachedResultSetMetaDataImpl extends DefaultColumnDefinition implements CachedResultSetMetaData {
  ResultSetMetaData metadata;
  
  public ResultSetMetaData getMetadata() {
    return this.metadata;
  }
  
  public void setMetadata(ResultSetMetaData metadata) {
    this.metadata = metadata;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\jdbc\result\CachedResultSetMetaDataImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
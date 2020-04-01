package com.mysql.cj.xdevapi;

import com.mysql.cj.exceptions.FeatureNotAvailableException;
import com.mysql.cj.protocol.x.StatementExecuteOk;
import java.util.List;

public class SqlUpdateResult extends UpdateResult implements SqlResult {
  public SqlUpdateResult(StatementExecuteOk ok) {
    super(ok);
  }
  
  public boolean hasData() {
    return false;
  }
  
  public boolean nextResult() {
    throw new FeatureNotAvailableException("Not a multi-result");
  }
  
  public List<Row> fetchAll() {
    throw new FeatureNotAvailableException("No data");
  }
  
  public Row next() {
    throw new FeatureNotAvailableException("No data");
  }
  
  public boolean hasNext() {
    throw new FeatureNotAvailableException("No data");
  }
  
  public int getColumnCount() {
    throw new FeatureNotAvailableException("No data");
  }
  
  public List<Column> getColumns() {
    throw new FeatureNotAvailableException("No data");
  }
  
  public List<String> getColumnNames() {
    throw new FeatureNotAvailableException("No data");
  }
  
  public long count() {
    throw new FeatureNotAvailableException("No data");
  }
  
  public Long getAutoIncrementValue() {
    return this.ok.getLastInsertId();
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\xdevapi\SqlUpdateResult.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
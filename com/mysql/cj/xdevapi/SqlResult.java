package com.mysql.cj.xdevapi;

public interface SqlResult extends Result, InsertResult, RowResult {
  default boolean nextResult() {
    return false;
  }
  
  default Long getAutoIncrementValue() {
    throw new XDevAPIError("Method getAutoIncrementValue() is allowed only for insert statements.");
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\xdevapi\SqlResult.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
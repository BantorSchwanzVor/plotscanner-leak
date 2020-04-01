package com.mysql.cj.xdevapi;

import java.util.Arrays;
import java.util.List;

public interface InsertStatement extends Statement<InsertStatement, InsertResult> {
  InsertStatement values(List<Object> paramList);
  
  InsertStatement values(Object... values) {
    return values(Arrays.asList(values));
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\xdevapi\InsertStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
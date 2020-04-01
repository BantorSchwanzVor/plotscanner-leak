package com.mysql.cj.xdevapi;

import com.mysql.cj.QueryResult;
import java.util.Iterator;

public interface Result extends QueryResult {
  long getAffectedItemsCount();
  
  int getWarningsCount();
  
  Iterator<Warning> getWarnings();
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\xdevapi\Result.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
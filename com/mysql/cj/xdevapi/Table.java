package com.mysql.cj.xdevapi;

import java.util.Map;

public interface Table extends DatabaseObject {
  InsertStatement insert();
  
  InsertStatement insert(String... paramVarArgs);
  
  InsertStatement insert(Map<String, Object> paramMap);
  
  SelectStatement select(String... paramVarArgs);
  
  UpdateStatement update();
  
  DeleteStatement delete();
  
  long count();
  
  boolean isView();
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\xdevapi\Table.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
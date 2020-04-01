package com.mysql.cj.xdevapi;

public interface DatabaseObject {
  Session getSession();
  
  Schema getSchema();
  
  String getName();
  
  DbObjectStatus existsInDatabase();
  
  public enum DbObjectType {
    COLLECTION, TABLE, VIEW, COLLECTION_VIEW;
  }
  
  public enum DbObjectStatus {
    EXISTS, NOT_EXISTS, UNKNOWN;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\xdevapi\DatabaseObject.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
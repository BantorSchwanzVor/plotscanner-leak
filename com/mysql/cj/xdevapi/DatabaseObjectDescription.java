package com.mysql.cj.xdevapi;

public class DatabaseObjectDescription {
  private String objectName;
  
  private DatabaseObject.DbObjectType objectType;
  
  public DatabaseObjectDescription(String name, String type) {
    this.objectName = name;
    this.objectType = DatabaseObject.DbObjectType.valueOf(type);
  }
  
  public String getObjectName() {
    return this.objectName;
  }
  
  public DatabaseObject.DbObjectType getObjectType() {
    return this.objectType;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\xdevapi\DatabaseObjectDescription.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
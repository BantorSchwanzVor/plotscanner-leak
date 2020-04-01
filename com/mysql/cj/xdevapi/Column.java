package com.mysql.cj.xdevapi;

public interface Column {
  String getSchemaName();
  
  String getTableName();
  
  String getTableLabel();
  
  String getColumnName();
  
  String getColumnLabel();
  
  Type getType();
  
  long getLength();
  
  int getFractionalDigits();
  
  boolean isNumberSigned();
  
  String getCollationName();
  
  String getCharacterSetName();
  
  boolean isPadded();
  
  boolean isNullable();
  
  boolean isAutoIncrement();
  
  boolean isPrimaryKey();
  
  boolean isUniqueKey();
  
  boolean isPartKey();
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\xdevapi\Column.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
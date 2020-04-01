package com.mysql.cj.xdevapi;

import java.util.List;

public interface Schema extends DatabaseObject {
  List<Collection> getCollections();
  
  List<Collection> getCollections(String paramString);
  
  List<Table> getTables();
  
  List<Table> getTables(String paramString);
  
  Collection getCollection(String paramString);
  
  Collection getCollection(String paramString, boolean paramBoolean);
  
  Table getCollectionAsTable(String paramString);
  
  Table getTable(String paramString);
  
  Table getTable(String paramString, boolean paramBoolean);
  
  Collection createCollection(String paramString);
  
  Collection createCollection(String paramString, boolean paramBoolean);
  
  void dropCollection(String paramString);
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\xdevapi\Schema.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
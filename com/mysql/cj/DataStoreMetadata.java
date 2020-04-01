package com.mysql.cj;

public interface DataStoreMetadata {
  boolean schemaExists(String paramString);
  
  boolean tableExists(String paramString1, String paramString2);
  
  long getTableRowCount(String paramString1, String paramString2);
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\DataStoreMetadata.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
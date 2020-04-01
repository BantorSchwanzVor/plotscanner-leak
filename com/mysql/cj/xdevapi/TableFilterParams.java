package com.mysql.cj.xdevapi;

import java.util.Arrays;
import java.util.stream.Collectors;

public class TableFilterParams extends AbstractFilterParams {
  public TableFilterParams(String schemaName, String collectionName) {
    this(schemaName, collectionName, true);
  }
  
  public TableFilterParams(String schemaName, String collectionName, boolean supportsOffset) {
    super(schemaName, collectionName, supportsOffset, true);
  }
  
  public void setFields(String... projection) {
    this.projection = projection;
    this.fields = (new ExprParser(Arrays.<CharSequence>stream((CharSequence[])projection).collect(Collectors.joining(", ")), true)).parseTableSelectProjection();
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\xdevapi\TableFilterParams.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
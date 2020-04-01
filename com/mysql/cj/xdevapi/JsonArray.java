package com.mysql.cj.xdevapi;

import java.util.ArrayList;

public class JsonArray extends ArrayList<JsonValue> implements JsonValue {
  private static final long serialVersionUID = 6557406141541247905L;
  
  public String toString() {
    StringBuilder sb = new StringBuilder("[");
    for (JsonValue val : this) {
      if (sb.length() > 1)
        sb.append(","); 
      sb.append(val.toString());
    } 
    sb.append("]");
    return sb.toString();
  }
  
  public String toFormattedString() {
    StringBuilder sb = new StringBuilder("[");
    for (JsonValue val : this) {
      if (sb.length() > 1)
        sb.append(", "); 
      sb.append(val.toFormattedString());
    } 
    sb.append("]");
    return sb.toString();
  }
  
  public JsonArray addValue(JsonValue val) {
    add(val);
    return this;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\xdevapi\JsonArray.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
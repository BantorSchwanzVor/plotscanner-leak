package com.mysql.cj.xdevapi;

import java.util.TreeMap;

public class DbDocImpl extends TreeMap<String, JsonValue> implements DbDoc {
  private static final long serialVersionUID = 6557406141541247905L;
  
  public String toString() {
    StringBuilder sb = new StringBuilder("{");
    for (String key : keySet()) {
      if (sb.length() > 1)
        sb.append(","); 
      sb.append("\"").append(key).append("\":").append(get(key).toString());
    } 
    sb.append("}");
    return sb.toString();
  }
  
  public String toFormattedString() {
    StringBuilder sb = new StringBuilder("{");
    for (String key : keySet()) {
      if (sb.length() > 1)
        sb.append(","); 
      sb.append("\n\"").append(key).append("\" : ").append(get(key).toFormattedString());
    } 
    if (size() > 0)
      sb.append("\n"); 
    sb.append("}");
    return sb.toString();
  }
  
  public DbDoc add(String key, JsonValue val) {
    put(key, val);
    return this;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\xdevapi\DbDocImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
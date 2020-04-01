package com.mysql.cj;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

class MysqlCharset {
  public final String charsetName;
  
  public final int mblen;
  
  public final int priority;
  
  public final List<String> javaEncodingsUc = new ArrayList<>();
  
  public final ServerVersion minimumVersion;
  
  public MysqlCharset(String charsetName, int mblen, int priority, String[] javaEncodings) {
    this(charsetName, mblen, priority, javaEncodings, new ServerVersion(0, 0, 0));
  }
  
  private void addEncodingMapping(String encoding) {
    String encodingUc = encoding.toUpperCase(Locale.ENGLISH);
    if (!this.javaEncodingsUc.contains(encodingUc))
      this.javaEncodingsUc.add(encodingUc); 
  }
  
  public MysqlCharset(String charsetName, int mblen, int priority, String[] javaEncodings, ServerVersion minimumVersion) {
    this.charsetName = charsetName;
    this.mblen = mblen;
    this.priority = priority;
    for (int i = 0; i < javaEncodings.length; i++) {
      String encoding = javaEncodings[i];
      try {
        Charset cs = Charset.forName(encoding);
        addEncodingMapping(cs.name());
        Set<String> als = cs.aliases();
        Iterator<String> ali = als.iterator();
        while (ali.hasNext())
          addEncodingMapping(ali.next()); 
      } catch (Exception e) {
        if (mblen == 1)
          addEncodingMapping(encoding); 
      } 
    } 
    if (this.javaEncodingsUc.size() == 0)
      if (mblen > 1) {
        addEncodingMapping("UTF-8");
      } else {
        addEncodingMapping("Cp1252");
      }  
    this.minimumVersion = minimumVersion;
  }
  
  public String toString() {
    StringBuilder asString = new StringBuilder();
    asString.append("[");
    asString.append("charsetName=");
    asString.append(this.charsetName);
    asString.append(",mblen=");
    asString.append(this.mblen);
    asString.append("]");
    return asString.toString();
  }
  
  boolean isOkayForVersion(ServerVersion version) {
    return version.meetsMinimum(this.minimumVersion);
  }
  
  String getMatchingJavaEncoding(String javaEncoding) {
    if (javaEncoding != null && this.javaEncodingsUc.contains(javaEncoding.toUpperCase(Locale.ENGLISH)))
      return javaEncoding; 
    return this.javaEncodingsUc.get(0);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\MysqlCharset.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
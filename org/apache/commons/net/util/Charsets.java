package org.apache.commons.net.util;

import java.nio.charset.Charset;

public class Charsets {
  public static Charset toCharset(String charsetName) {
    return (charsetName == null) ? Charset.defaultCharset() : Charset.forName(charsetName);
  }
  
  public static Charset toCharset(String charsetName, String defaultCharsetName) {
    return (charsetName == null) ? Charset.forName(defaultCharsetName) : Charset.forName(charsetName);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\apache\commons\ne\\util\Charsets.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
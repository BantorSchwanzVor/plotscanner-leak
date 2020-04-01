package org.apache.commons.net.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

public final class CRLFLineReader extends BufferedReader {
  private static final char LF = '\n';
  
  private static final char CR = '\r';
  
  public CRLFLineReader(Reader reader) {
    super(reader);
  }
  
  public String readLine() throws IOException {
    StringBuilder sb = new StringBuilder();
    boolean prevWasCR = false;
    synchronized (this.lock) {
      int intch;
      while ((intch = read()) != -1) {
        if (prevWasCR && intch == 10)
          return sb.substring(0, sb.length() - 1); 
        if (intch == 13) {
          prevWasCR = true;
        } else {
          prevWasCR = false;
        } 
        sb.append((char)intch);
      } 
    } 
    String string = sb.toString();
    if (string.length() == 0)
      return null; 
    return string;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\apache\commons\net\io\CRLFLineReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
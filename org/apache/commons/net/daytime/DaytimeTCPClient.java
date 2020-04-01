package org.apache.commons.net.daytime;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.apache.commons.net.SocketClient;

public final class DaytimeTCPClient extends SocketClient {
  public static final int DEFAULT_PORT = 13;
  
  private final char[] __buffer = new char[64];
  
  public DaytimeTCPClient() {
    setDefaultPort(13);
  }
  
  public String getTime() throws IOException {
    StringBuilder result = new StringBuilder(this.__buffer.length);
    BufferedReader reader = new BufferedReader(new InputStreamReader(this._input_, getCharset()));
    while (true) {
      int read = reader.read(this.__buffer, 0, this.__buffer.length);
      if (read <= 0)
        break; 
      result.append(this.__buffer, 0, read);
    } 
    return result.toString();
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\apache\commons\net\daytime\DaytimeTCPClient.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
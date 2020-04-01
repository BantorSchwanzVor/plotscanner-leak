package org.apache.commons.net.discard;

import java.io.OutputStream;
import org.apache.commons.net.SocketClient;

public class DiscardTCPClient extends SocketClient {
  public static final int DEFAULT_PORT = 9;
  
  public DiscardTCPClient() {
    setDefaultPort(9);
  }
  
  public OutputStream getOutputStream() {
    return this._output_;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\apache\commons\net\discard\DiscardTCPClient.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
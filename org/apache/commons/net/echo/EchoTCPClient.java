package org.apache.commons.net.echo;

import java.io.InputStream;
import org.apache.commons.net.discard.DiscardTCPClient;

public final class EchoTCPClient extends DiscardTCPClient {
  public static final int DEFAULT_PORT = 7;
  
  public EchoTCPClient() {
    setDefaultPort(7);
  }
  
  public InputStream getInputStream() {
    return this._input_;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\apache\commons\net\echo\EchoTCPClient.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
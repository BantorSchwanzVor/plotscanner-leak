package org.apache.commons.net.bsd;

import java.io.IOException;

public class RLoginClient extends RCommandClient {
  public static final int DEFAULT_PORT = 513;
  
  public RLoginClient() {
    setDefaultPort(513);
  }
  
  public void rlogin(String localUsername, String remoteUsername, String terminalType, int terminalSpeed) throws IOException {
    rexec(localUsername, remoteUsername, String.valueOf(terminalType) + "/" + terminalSpeed, 
        false);
  }
  
  public void rlogin(String localUsername, String remoteUsername, String terminalType) throws IOException {
    rexec(localUsername, remoteUsername, terminalType, false);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\apache\commons\net\bsd\RLoginClient.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
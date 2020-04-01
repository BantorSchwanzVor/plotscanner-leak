package org.apache.commons.net.nntp;

import java.io.IOException;

public final class NNTPConnectionClosedException extends IOException {
  private static final long serialVersionUID = 1029785635891040770L;
  
  public NNTPConnectionClosedException() {}
  
  public NNTPConnectionClosedException(String message) {
    super(message);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\apache\commons\net\nntp\NNTPConnectionClosedException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
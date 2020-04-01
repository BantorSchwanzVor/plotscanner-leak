package org.apache.commons.net.ftp;

import java.io.IOException;

public class FTPConnectionClosedException extends IOException {
  private static final long serialVersionUID = 3500547241659379952L;
  
  public FTPConnectionClosedException() {}
  
  public FTPConnectionClosedException(String message) {
    super(message);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\apache\commons\net\ftp\FTPConnectionClosedException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
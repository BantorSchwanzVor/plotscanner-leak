package org.apache.commons.net.smtp;

import java.io.IOException;

public final class SMTPConnectionClosedException extends IOException {
  private static final long serialVersionUID = 626520434326660627L;
  
  public SMTPConnectionClosedException() {}
  
  public SMTPConnectionClosedException(String message) {
    super(message);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\apache\commons\net\smtp\SMTPConnectionClosedException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
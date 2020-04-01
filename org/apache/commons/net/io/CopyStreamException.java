package org.apache.commons.net.io;

import java.io.IOException;

public class CopyStreamException extends IOException {
  private static final long serialVersionUID = -2602899129433221532L;
  
  private final long totalBytesTransferred;
  
  public CopyStreamException(String message, long bytesTransferred, IOException exception) {
    super(message);
    initCause(exception);
    this.totalBytesTransferred = bytesTransferred;
  }
  
  public long getTotalBytesTransferred() {
    return this.totalBytesTransferred;
  }
  
  public IOException getIOException() {
    return (IOException)getCause();
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\apache\commons\net\io\CopyStreamException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
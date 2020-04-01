package org.apache.commons.net.io;

import java.util.EventObject;

public class CopyStreamEvent extends EventObject {
  private static final long serialVersionUID = -964927635655051867L;
  
  public static final long UNKNOWN_STREAM_SIZE = -1L;
  
  private final int bytesTransferred;
  
  private final long totalBytesTransferred;
  
  private final long streamSize;
  
  public CopyStreamEvent(Object source, long totalBytesTransferred, int bytesTransferred, long streamSize) {
    super(source);
    this.bytesTransferred = bytesTransferred;
    this.totalBytesTransferred = totalBytesTransferred;
    this.streamSize = streamSize;
  }
  
  public int getBytesTransferred() {
    return this.bytesTransferred;
  }
  
  public long getTotalBytesTransferred() {
    return this.totalBytesTransferred;
  }
  
  public long getStreamSize() {
    return this.streamSize;
  }
  
  public String toString() {
    return String.valueOf(getClass().getName()) + "[source=" + this.source + 
      ", total=" + this.totalBytesTransferred + 
      ", bytes=" + this.bytesTransferred + 
      ", size=" + this.streamSize + 
      "]";
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\apache\commons\net\io\CopyStreamEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
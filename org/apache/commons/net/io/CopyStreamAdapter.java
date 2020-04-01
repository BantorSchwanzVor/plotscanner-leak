package org.apache.commons.net.io;

import java.util.EventListener;
import org.apache.commons.net.util.ListenerList;

public class CopyStreamAdapter implements CopyStreamListener {
  private final ListenerList internalListeners = new ListenerList();
  
  public void bytesTransferred(CopyStreamEvent event) {
    for (EventListener listener : this.internalListeners)
      ((CopyStreamListener)listener).bytesTransferred(event); 
  }
  
  public void bytesTransferred(long totalBytesTransferred, int bytesTransferred, long streamSize) {
    for (EventListener listener : this.internalListeners)
      ((CopyStreamListener)listener).bytesTransferred(
          totalBytesTransferred, bytesTransferred, streamSize); 
  }
  
  public void addCopyStreamListener(CopyStreamListener listener) {
    this.internalListeners.addListener(listener);
  }
  
  public void removeCopyStreamListener(CopyStreamListener listener) {
    this.internalListeners.removeListener(listener);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\apache\commons\net\io\CopyStreamAdapter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
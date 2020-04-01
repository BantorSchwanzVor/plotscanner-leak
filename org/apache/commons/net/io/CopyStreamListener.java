package org.apache.commons.net.io;

import java.util.EventListener;

public interface CopyStreamListener extends EventListener {
  void bytesTransferred(CopyStreamEvent paramCopyStreamEvent);
  
  void bytesTransferred(long paramLong1, int paramInt, long paramLong2);
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\apache\commons\net\io\CopyStreamListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
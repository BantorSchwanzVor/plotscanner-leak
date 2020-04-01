package com.mysql.cj.protocol;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class WatchableOutputStream extends ByteArrayOutputStream implements WatchableStream {
  private OutputStreamWatcher watcher;
  
  public void close() throws IOException {
    super.close();
    if (this.watcher != null)
      this.watcher.streamClosed(this); 
  }
  
  public void setWatcher(OutputStreamWatcher watcher) {
    this.watcher = watcher;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\protocol\WatchableOutputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
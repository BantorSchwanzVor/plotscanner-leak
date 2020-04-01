package com.mysql.cj.protocol;

public interface WatchableStream {
  void setWatcher(OutputStreamWatcher paramOutputStreamWatcher);
  
  int size();
  
  byte[] toByteArray();
  
  void write(byte[] paramArrayOfbyte, int paramInt1, int paramInt2);
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\protocol\WatchableStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
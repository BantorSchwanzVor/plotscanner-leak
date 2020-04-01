package com.mysql.cj.protocol;

public interface PacketReceivedTimeHolder {
  default long getLastPacketReceivedTime() {
    return 0L;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\protocol\PacketReceivedTimeHolder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
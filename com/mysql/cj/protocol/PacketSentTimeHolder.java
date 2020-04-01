package com.mysql.cj.protocol;

public interface PacketSentTimeHolder {
  default long getLastPacketSentTime() {
    return 0L;
  }
  
  default long getPreviousPacketSentTime() {
    return 0L;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\protocol\PacketSentTimeHolder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
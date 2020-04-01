package com.mysql.cj.protocol.a;

public class PacketSplitter {
  private int totalSize;
  
  private int currentPacketLen = 0;
  
  private int offset = 0;
  
  public PacketSplitter(int totalSize) {
    this.totalSize = totalSize;
  }
  
  public int getPacketLen() {
    return this.currentPacketLen;
  }
  
  public int getOffset() {
    return this.offset;
  }
  
  public boolean nextPacket() {
    this.offset += this.currentPacketLen;
    if (this.currentPacketLen == 16777215 && this.offset == this.totalSize) {
      this.currentPacketLen = 0;
      return true;
    } 
    if (this.totalSize == 0) {
      this.totalSize = -1;
      return true;
    } 
    this.currentPacketLen = this.totalSize - this.offset;
    if (this.currentPacketLen > 16777215)
      this.currentPacketLen = 16777215; 
    return (this.offset < this.totalSize);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\protocol\a\PacketSplitter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
package com.mysql.cj.protocol.x;

import com.mysql.cj.protocol.MessageHeader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class XMessageHeader implements MessageHeader {
  private ByteBuffer headerBuf;
  
  private int messageType = -1;
  
  private int messageSize = -1;
  
  public XMessageHeader() {
    this.headerBuf = ByteBuffer.allocate(5).order(ByteOrder.LITTLE_ENDIAN);
  }
  
  public XMessageHeader(byte[] buf) {
    this.headerBuf = ByteBuffer.wrap(buf).order(ByteOrder.LITTLE_ENDIAN);
  }
  
  private void parseBuffer() {
    if (this.messageSize == -1) {
      this.headerBuf.position(0);
      this.messageSize = this.headerBuf.getInt() - 1;
      this.messageType = this.headerBuf.get();
    } 
  }
  
  public ByteBuffer getBuffer() {
    return this.headerBuf;
  }
  
  public int getMessageSize() {
    parseBuffer();
    return this.messageSize;
  }
  
  public byte getMessageSequence() {
    return 0;
  }
  
  public int getMessageType() {
    parseBuffer();
    return this.messageType;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\protocol\x\XMessageHeader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
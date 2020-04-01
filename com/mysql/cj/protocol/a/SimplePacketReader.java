package com.mysql.cj.protocol.a;

import com.mysql.cj.Messages;
import com.mysql.cj.conf.RuntimeProperty;
import com.mysql.cj.exceptions.CJPacketTooBigException;
import com.mysql.cj.protocol.Message;
import com.mysql.cj.protocol.MessageHeader;
import com.mysql.cj.protocol.MessageReader;
import com.mysql.cj.protocol.SocketConnection;
import java.io.IOException;
import java.util.Optional;

public class SimplePacketReader implements MessageReader<NativePacketHeader, NativePacketPayload> {
  protected SocketConnection socketConnection;
  
  protected RuntimeProperty<Integer> maxAllowedPacket;
  
  private byte readPacketSequence = -1;
  
  public SimplePacketReader(SocketConnection socketConnection, RuntimeProperty<Integer> maxAllowedPacket) {
    this.socketConnection = socketConnection;
    this.maxAllowedPacket = maxAllowedPacket;
  }
  
  public NativePacketHeader readHeader() throws IOException {
    NativePacketHeader hdr = new NativePacketHeader();
    try {
      this.socketConnection.getMysqlInput().readFully(hdr.getBuffer().array(), 0, 4);
      int packetLength = hdr.getMessageSize();
      if (packetLength > ((Integer)this.maxAllowedPacket.getValue()).intValue())
        throw new CJPacketTooBigException(packetLength, ((Integer)this.maxAllowedPacket.getValue()).intValue()); 
    } catch (IOException|CJPacketTooBigException e) {
      try {
        this.socketConnection.forceClose();
      } catch (Exception exception) {}
      throw e;
    } 
    this.readPacketSequence = hdr.getMessageSequence();
    return hdr;
  }
  
  public NativePacketPayload readMessage(Optional<NativePacketPayload> reuse, NativePacketHeader header) throws IOException {
    try {
      NativePacketPayload buf;
      int packetLength = header.getMessageSize();
      if (reuse.isPresent()) {
        buf = reuse.get();
        buf.setPosition(0);
        if ((buf.getByteBuffer()).length < packetLength)
          buf.setByteBuffer(new byte[packetLength]); 
        buf.setPayloadLength(packetLength);
      } else {
        buf = new NativePacketPayload(new byte[packetLength]);
      } 
      int numBytesRead = this.socketConnection.getMysqlInput().readFully(buf.getByteBuffer(), 0, packetLength);
      if (numBytesRead != packetLength)
        throw new IOException(Messages.getString("PacketReader.1", new Object[] { Integer.valueOf(packetLength), Integer.valueOf(numBytesRead) })); 
      return buf;
    } catch (IOException e) {
      try {
        this.socketConnection.forceClose();
      } catch (Exception exception) {}
      throw e;
    } 
  }
  
  public byte getMessageSequence() {
    return this.readPacketSequence;
  }
  
  public void resetMessageSequence() {
    this.readPacketSequence = 0;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\protocol\a\SimplePacketReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
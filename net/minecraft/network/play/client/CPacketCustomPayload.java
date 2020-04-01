package net.minecraft.network.play.client;

import io.netty.buffer.ByteBuf;
import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class CPacketCustomPayload implements Packet<INetHandlerPlayServer> {
  private String channel;
  
  private PacketBuffer data;
  
  public CPacketCustomPayload() {}
  
  public CPacketCustomPayload(String channelIn, PacketBuffer bufIn) {
    this.channel = channelIn;
    this.data = bufIn;
    if (bufIn.writerIndex() > 32767)
      throw new IllegalArgumentException("Payload may not be larger than 32767 bytes"); 
  }
  
  public void readPacketData(PacketBuffer buf) throws IOException {
    this.channel = buf.readStringFromBuffer(20);
    int i = buf.readableBytes();
    if (i >= 0 && i <= 32767) {
      this.data = new PacketBuffer(buf.readBytes(i));
    } else {
      throw new IOException("Payload may not be larger than 32767 bytes");
    } 
  }
  
  public void writePacketData(PacketBuffer buf) throws IOException {
    buf.writeString(this.channel);
    buf.writeBytes((ByteBuf)this.data);
  }
  
  public void processPacket(INetHandlerPlayServer handler) {
    handler.processCustomPayload(this);
    if (this.data != null)
      this.data.release(); 
  }
  
  public String getChannelName() {
    return this.channel;
  }
  
  public PacketBuffer getBufferData() {
    return this.data;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\network\play\client\CPacketCustomPayload.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
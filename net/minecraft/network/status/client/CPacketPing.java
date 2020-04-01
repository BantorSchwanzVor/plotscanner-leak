package net.minecraft.network.status.client;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.status.INetHandlerStatusServer;

public class CPacketPing implements Packet<INetHandlerStatusServer> {
  private long clientTime;
  
  public CPacketPing() {}
  
  public CPacketPing(long clientTimeIn) {
    this.clientTime = clientTimeIn;
  }
  
  public void readPacketData(PacketBuffer buf) throws IOException {
    this.clientTime = buf.readLong();
  }
  
  public void writePacketData(PacketBuffer buf) throws IOException {
    buf.writeLong(this.clientTime);
  }
  
  public void processPacket(INetHandlerStatusServer handler) {
    handler.processPing(this);
  }
  
  public long getClientTime() {
    return this.clientTime;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\network\status\client\CPacketPing.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
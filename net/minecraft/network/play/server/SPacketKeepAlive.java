package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketKeepAlive implements Packet<INetHandlerPlayClient> {
  private long id;
  
  public SPacketKeepAlive() {}
  
  public SPacketKeepAlive(long idIn) {
    this.id = idIn;
  }
  
  public void processPacket(INetHandlerPlayClient handler) {
    handler.handleKeepAlive(this);
  }
  
  public void readPacketData(PacketBuffer buf) throws IOException {
    this.id = buf.readLong();
  }
  
  public void writePacketData(PacketBuffer buf) throws IOException {
    buf.writeLong(this.id);
  }
  
  public long getId() {
    return this.id;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\network\play\server\SPacketKeepAlive.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
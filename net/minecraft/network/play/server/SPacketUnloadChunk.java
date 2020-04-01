package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketUnloadChunk implements Packet<INetHandlerPlayClient> {
  private int x;
  
  private int z;
  
  public SPacketUnloadChunk() {}
  
  public SPacketUnloadChunk(int xIn, int zIn) {
    this.x = xIn;
    this.z = zIn;
  }
  
  public void readPacketData(PacketBuffer buf) throws IOException {
    this.x = buf.readInt();
    this.z = buf.readInt();
  }
  
  public void writePacketData(PacketBuffer buf) throws IOException {
    buf.writeInt(this.x);
    buf.writeInt(this.z);
  }
  
  public void processPacket(INetHandlerPlayClient handler) {
    handler.processChunkUnload(this);
  }
  
  public int getX() {
    return this.x;
  }
  
  public int getZ() {
    return this.z;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\network\play\server\SPacketUnloadChunk.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketDestroyEntities implements Packet<INetHandlerPlayClient> {
  private int[] entityIDs;
  
  public SPacketDestroyEntities() {}
  
  public SPacketDestroyEntities(int... entityIdsIn) {
    this.entityIDs = entityIdsIn;
  }
  
  public void readPacketData(PacketBuffer buf) throws IOException {
    this.entityIDs = new int[buf.readVarIntFromBuffer()];
    for (int i = 0; i < this.entityIDs.length; i++)
      this.entityIDs[i] = buf.readVarIntFromBuffer(); 
  }
  
  public void writePacketData(PacketBuffer buf) throws IOException {
    buf.writeVarIntToBuffer(this.entityIDs.length);
    byte b;
    int i, arrayOfInt[];
    for (i = (arrayOfInt = this.entityIDs).length, b = 0; b < i; ) {
      int j = arrayOfInt[b];
      buf.writeVarIntToBuffer(j);
      b++;
    } 
  }
  
  public void processPacket(INetHandlerPlayClient handler) {
    handler.handleDestroyEntities(this);
  }
  
  public int[] getEntityIDs() {
    return this.entityIDs;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\network\play\server\SPacketDestroyEntities.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
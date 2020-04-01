package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketHeldItemChange implements Packet<INetHandlerPlayClient> {
  private int heldItemHotbarIndex;
  
  public SPacketHeldItemChange() {}
  
  public SPacketHeldItemChange(int hotbarIndexIn) {
    this.heldItemHotbarIndex = hotbarIndexIn;
  }
  
  public void readPacketData(PacketBuffer buf) throws IOException {
    this.heldItemHotbarIndex = buf.readByte();
  }
  
  public void writePacketData(PacketBuffer buf) throws IOException {
    buf.writeByte(this.heldItemHotbarIndex);
  }
  
  public void processPacket(INetHandlerPlayClient handler) {
    handler.handleHeldItemChange(this);
  }
  
  public int getHeldItemHotbarIndex() {
    return this.heldItemHotbarIndex;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\network\play\server\SPacketHeldItemChange.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
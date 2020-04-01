package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.util.EnumHand;

public class CPacketPlayerTryUseItem implements Packet<INetHandlerPlayServer> {
  private EnumHand hand;
  
  public CPacketPlayerTryUseItem() {}
  
  public CPacketPlayerTryUseItem(EnumHand handIn) {
    this.hand = handIn;
  }
  
  public void readPacketData(PacketBuffer buf) throws IOException {
    this.hand = (EnumHand)buf.readEnumValue(EnumHand.class);
  }
  
  public void writePacketData(PacketBuffer buf) throws IOException {
    buf.writeEnumValue((Enum)this.hand);
  }
  
  public void processPacket(INetHandlerPlayServer handler) {
    handler.processPlayerBlockPlacement(this);
  }
  
  public EnumHand getHand() {
    return this.hand;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\network\play\client\CPacketPlayerTryUseItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
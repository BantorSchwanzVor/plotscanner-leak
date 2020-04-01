package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.text.ITextComponent;

public class SPacketDisconnect implements Packet<INetHandlerPlayClient> {
  private ITextComponent reason;
  
  public SPacketDisconnect() {}
  
  public SPacketDisconnect(ITextComponent messageIn) {
    this.reason = messageIn;
  }
  
  public void readPacketData(PacketBuffer buf) throws IOException {
    this.reason = buf.readTextComponent();
  }
  
  public void writePacketData(PacketBuffer buf) throws IOException {
    buf.writeTextComponent(this.reason);
  }
  
  public void processPacket(INetHandlerPlayClient handler) {
    handler.handleDisconnect(this);
  }
  
  public ITextComponent getReason() {
    return this.reason;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\network\play\server\SPacketDisconnect.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
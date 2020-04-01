package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.text.ITextComponent;

public class SPacketPlayerListHeaderFooter implements Packet<INetHandlerPlayClient> {
  private ITextComponent header;
  
  private ITextComponent footer;
  
  public void readPacketData(PacketBuffer buf) throws IOException {
    this.header = buf.readTextComponent();
    this.footer = buf.readTextComponent();
  }
  
  public void writePacketData(PacketBuffer buf) throws IOException {
    buf.writeTextComponent(this.header);
    buf.writeTextComponent(this.footer);
  }
  
  public void processPacket(INetHandlerPlayClient handler) {
    handler.handlePlayerListHeaderFooter(this);
  }
  
  public ITextComponent getHeader() {
    return this.header;
  }
  
  public ITextComponent getFooter() {
    return this.footer;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\network\play\server\SPacketPlayerListHeaderFooter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
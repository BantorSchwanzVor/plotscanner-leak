package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class CPacketResourcePackStatus implements Packet<INetHandlerPlayServer> {
  private Action action;
  
  public CPacketResourcePackStatus() {}
  
  public CPacketResourcePackStatus(Action p_i47156_1_) {
    this.action = p_i47156_1_;
  }
  
  public void readPacketData(PacketBuffer buf) throws IOException {
    this.action = (Action)buf.readEnumValue(Action.class);
  }
  
  public void writePacketData(PacketBuffer buf) throws IOException {
    buf.writeEnumValue(this.action);
  }
  
  public void processPacket(INetHandlerPlayServer handler) {
    handler.handleResourcePackStatus(this);
  }
  
  public enum Action {
    SUCCESSFULLY_LOADED, DECLINED, FAILED_DOWNLOAD, ACCEPTED;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\network\play\client\CPacketResourcePackStatus.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
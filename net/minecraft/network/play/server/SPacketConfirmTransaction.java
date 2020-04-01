package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketConfirmTransaction implements Packet<INetHandlerPlayClient> {
  private int windowId;
  
  private short actionNumber;
  
  private boolean accepted;
  
  public SPacketConfirmTransaction() {}
  
  public SPacketConfirmTransaction(int windowIdIn, short actionNumberIn, boolean acceptedIn) {
    this.windowId = windowIdIn;
    this.actionNumber = actionNumberIn;
    this.accepted = acceptedIn;
  }
  
  public void processPacket(INetHandlerPlayClient handler) {
    handler.handleConfirmTransaction(this);
  }
  
  public void readPacketData(PacketBuffer buf) throws IOException {
    this.windowId = buf.readUnsignedByte();
    this.actionNumber = buf.readShort();
    this.accepted = buf.readBoolean();
  }
  
  public void writePacketData(PacketBuffer buf) throws IOException {
    buf.writeByte(this.windowId);
    buf.writeShort(this.actionNumber);
    buf.writeBoolean(this.accepted);
  }
  
  public int getWindowId() {
    return this.windowId;
  }
  
  public short getActionNumber() {
    return this.actionNumber;
  }
  
  public boolean wasAccepted() {
    return this.accepted;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\network\play\server\SPacketConfirmTransaction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
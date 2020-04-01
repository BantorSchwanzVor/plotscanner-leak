package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class CPacketConfirmTransaction implements Packet<INetHandlerPlayServer> {
  private int windowId;
  
  private short uid;
  
  private boolean accepted;
  
  public CPacketConfirmTransaction() {}
  
  public CPacketConfirmTransaction(int windowIdIn, short uidIn, boolean acceptedIn) {
    this.windowId = windowIdIn;
    this.uid = uidIn;
    this.accepted = acceptedIn;
  }
  
  public void processPacket(INetHandlerPlayServer handler) {
    handler.processConfirmTransaction(this);
  }
  
  public void readPacketData(PacketBuffer buf) throws IOException {
    this.windowId = buf.readByte();
    this.uid = buf.readShort();
    this.accepted = (buf.readByte() != 0);
  }
  
  public void writePacketData(PacketBuffer buf) throws IOException {
    buf.writeByte(this.windowId);
    buf.writeShort(this.uid);
    buf.writeByte(this.accepted ? 1 : 0);
  }
  
  public int getWindowId() {
    return this.windowId;
  }
  
  public short getUid() {
    return this.uid;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\network\play\client\CPacketConfirmTransaction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
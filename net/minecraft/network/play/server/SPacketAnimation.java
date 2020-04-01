package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.entity.Entity;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketAnimation implements Packet<INetHandlerPlayClient> {
  private int entityId;
  
  private int type;
  
  public SPacketAnimation() {}
  
  public SPacketAnimation(Entity entityIn, int typeIn) {
    this.entityId = entityIn.getEntityId();
    this.type = typeIn;
  }
  
  public void readPacketData(PacketBuffer buf) throws IOException {
    this.entityId = buf.readVarIntFromBuffer();
    this.type = buf.readUnsignedByte();
  }
  
  public void writePacketData(PacketBuffer buf) throws IOException {
    buf.writeVarIntToBuffer(this.entityId);
    buf.writeByte(this.type);
  }
  
  public void processPacket(INetHandlerPlayClient handler) {
    handler.handleAnimation(this);
  }
  
  public int getEntityID() {
    return this.entityId;
  }
  
  public int getAnimationType() {
    return this.type;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\network\play\server\SPacketAnimation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
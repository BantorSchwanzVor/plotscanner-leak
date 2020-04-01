package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.entity.Entity;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.world.World;

public class SPacketEntityStatus implements Packet<INetHandlerPlayClient> {
  private int entityId;
  
  private byte logicOpcode;
  
  public SPacketEntityStatus() {}
  
  public SPacketEntityStatus(Entity entityIn, byte opcodeIn) {
    this.entityId = entityIn.getEntityId();
    this.logicOpcode = opcodeIn;
  }
  
  public void readPacketData(PacketBuffer buf) throws IOException {
    this.entityId = buf.readInt();
    this.logicOpcode = buf.readByte();
  }
  
  public void writePacketData(PacketBuffer buf) throws IOException {
    buf.writeInt(this.entityId);
    buf.writeByte(this.logicOpcode);
  }
  
  public void processPacket(INetHandlerPlayClient handler) {
    handler.handleEntityStatus(this);
  }
  
  public Entity getEntity(World worldIn) {
    return worldIn.getEntityByID(this.entityId);
  }
  
  public byte getOpCode() {
    return this.logicOpcode;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\network\play\server\SPacketEntityStatus.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
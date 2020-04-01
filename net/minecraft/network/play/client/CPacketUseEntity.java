package net.minecraft.network.play.client;

import java.io.IOException;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class CPacketUseEntity implements Packet<INetHandlerPlayServer> {
  private int entityId;
  
  private Action action;
  
  private Vec3d hitVec;
  
  private EnumHand hand;
  
  public CPacketUseEntity() {}
  
  public CPacketUseEntity(Entity entityIn) {
    this.entityId = entityIn.getEntityId();
    this.action = Action.ATTACK;
  }
  
  public CPacketUseEntity(Entity entityIn, EnumHand handIn) {
    this.entityId = entityIn.getEntityId();
    this.action = Action.INTERACT;
    this.hand = handIn;
  }
  
  public CPacketUseEntity(Entity entityIn, EnumHand handIn, Vec3d hitVecIn) {
    this.entityId = entityIn.getEntityId();
    this.action = Action.INTERACT_AT;
    this.hand = handIn;
    this.hitVec = hitVecIn;
  }
  
  public void readPacketData(PacketBuffer buf) throws IOException {
    this.entityId = buf.readVarIntFromBuffer();
    this.action = (Action)buf.readEnumValue(Action.class);
    if (this.action == Action.INTERACT_AT)
      this.hitVec = new Vec3d(buf.readFloat(), buf.readFloat(), buf.readFloat()); 
    if (this.action == Action.INTERACT || this.action == Action.INTERACT_AT)
      this.hand = (EnumHand)buf.readEnumValue(EnumHand.class); 
  }
  
  public void writePacketData(PacketBuffer buf) throws IOException {
    buf.writeVarIntToBuffer(this.entityId);
    buf.writeEnumValue(this.action);
    if (this.action == Action.INTERACT_AT) {
      buf.writeFloat((float)this.hitVec.xCoord);
      buf.writeFloat((float)this.hitVec.yCoord);
      buf.writeFloat((float)this.hitVec.zCoord);
    } 
    if (this.action == Action.INTERACT || this.action == Action.INTERACT_AT)
      buf.writeEnumValue((Enum)this.hand); 
  }
  
  public void processPacket(INetHandlerPlayServer handler) {
    handler.processUseEntity(this);
  }
  
  @Nullable
  public Entity getEntityFromWorld(World worldIn) {
    return worldIn.getEntityByID(this.entityId);
  }
  
  public Action getAction() {
    return this.action;
  }
  
  public EnumHand getHand() {
    return this.hand;
  }
  
  public Vec3d getHitVec() {
    return this.hitVec;
  }
  
  public enum Action {
    INTERACT, ATTACK, INTERACT_AT;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\network\play\client\CPacketUseEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
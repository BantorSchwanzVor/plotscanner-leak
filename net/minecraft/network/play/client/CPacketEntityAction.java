package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.entity.Entity;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class CPacketEntityAction implements Packet<INetHandlerPlayServer> {
  private int entityID;
  
  private Action action;
  
  private int auxData;
  
  public CPacketEntityAction() {}
  
  public CPacketEntityAction(Entity entityIn, Action actionIn) {
    this(entityIn, actionIn, 0);
  }
  
  public CPacketEntityAction(Entity entityIn, Action actionIn, int auxDataIn) {
    this.entityID = entityIn.getEntityId();
    this.action = actionIn;
    this.auxData = auxDataIn;
  }
  
  public void readPacketData(PacketBuffer buf) throws IOException {
    this.entityID = buf.readVarIntFromBuffer();
    this.action = (Action)buf.readEnumValue(Action.class);
    this.auxData = buf.readVarIntFromBuffer();
  }
  
  public void writePacketData(PacketBuffer buf) throws IOException {
    buf.writeVarIntToBuffer(this.entityID);
    buf.writeEnumValue(this.action);
    buf.writeVarIntToBuffer(this.auxData);
  }
  
  public void processPacket(INetHandlerPlayServer handler) {
    handler.processEntityAction(this);
  }
  
  public Action getAction() {
    return this.action;
  }
  
  public int getAuxData() {
    return this.auxData;
  }
  
  public enum Action {
    START_SNEAKING, STOP_SNEAKING, STOP_SLEEPING, START_SPRINTING, STOP_SPRINTING, START_RIDING_JUMP, STOP_RIDING_JUMP, OPEN_INVENTORY, START_FALL_FLYING;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\network\play\client\CPacketEntityAction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
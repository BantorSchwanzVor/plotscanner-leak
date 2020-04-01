package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class CPacketPlayerDigging implements Packet<INetHandlerPlayServer> {
  private BlockPos position;
  
  private EnumFacing facing;
  
  private Action action;
  
  public CPacketPlayerDigging() {}
  
  public CPacketPlayerDigging(Action actionIn, BlockPos posIn, EnumFacing facingIn) {
    this.action = actionIn;
    this.position = posIn;
    this.facing = facingIn;
  }
  
  public void readPacketData(PacketBuffer buf) throws IOException {
    this.action = (Action)buf.readEnumValue(Action.class);
    this.position = buf.readBlockPos();
    this.facing = EnumFacing.getFront(buf.readUnsignedByte());
  }
  
  public void writePacketData(PacketBuffer buf) throws IOException {
    buf.writeEnumValue(this.action);
    buf.writeBlockPos(this.position);
    buf.writeByte(this.facing.getIndex());
  }
  
  public void processPacket(INetHandlerPlayServer handler) {
    handler.processPlayerDigging(this);
  }
  
  public BlockPos getPosition() {
    return this.position;
  }
  
  public EnumFacing getFacing() {
    return this.facing;
  }
  
  public Action getAction() {
    return this.action;
  }
  
  public enum Action {
    START_DESTROY_BLOCK, ABORT_DESTROY_BLOCK, STOP_DESTROY_BLOCK, DROP_ALL_ITEMS, DROP_ITEM, RELEASE_USE_ITEM, SWAP_HELD_ITEMS;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\network\play\client\CPacketPlayerDigging.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
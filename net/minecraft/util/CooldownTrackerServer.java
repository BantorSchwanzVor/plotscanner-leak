package net.minecraft.util;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketCooldown;

public class CooldownTrackerServer extends CooldownTracker {
  private final EntityPlayerMP player;
  
  public CooldownTrackerServer(EntityPlayerMP playerIn) {
    this.player = playerIn;
  }
  
  protected void notifyOnSet(Item itemIn, int ticksIn) {
    super.notifyOnSet(itemIn, ticksIn);
    this.player.connection.sendPacket((Packet)new SPacketCooldown(itemIn, ticksIn));
  }
  
  protected void notifyOnRemove(Item itemIn) {
    super.notifyOnRemove(itemIn);
    this.player.connection.sendPacket((Packet)new SPacketCooldown(itemIn, 0));
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraf\\util\CooldownTrackerServer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
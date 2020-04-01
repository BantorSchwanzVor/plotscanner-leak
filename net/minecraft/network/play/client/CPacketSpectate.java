package net.minecraft.network.play.client;

import java.io.IOException;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.world.WorldServer;

public class CPacketSpectate implements Packet<INetHandlerPlayServer> {
  private UUID id;
  
  public CPacketSpectate() {}
  
  public CPacketSpectate(UUID uniqueIdIn) {
    this.id = uniqueIdIn;
  }
  
  public void readPacketData(PacketBuffer buf) throws IOException {
    this.id = buf.readUuid();
  }
  
  public void writePacketData(PacketBuffer buf) throws IOException {
    buf.writeUuid(this.id);
  }
  
  public void processPacket(INetHandlerPlayServer handler) {
    handler.handleSpectate(this);
  }
  
  @Nullable
  public Entity getEntity(WorldServer worldIn) {
    return worldIn.getEntityFromUuid(this.id);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\network\play\client\CPacketSpectate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
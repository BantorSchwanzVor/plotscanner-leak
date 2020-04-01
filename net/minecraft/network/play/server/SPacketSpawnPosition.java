package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.math.BlockPos;

public class SPacketSpawnPosition implements Packet<INetHandlerPlayClient> {
  private BlockPos spawnBlockPos;
  
  public SPacketSpawnPosition() {}
  
  public SPacketSpawnPosition(BlockPos posIn) {
    this.spawnBlockPos = posIn;
  }
  
  public void readPacketData(PacketBuffer buf) throws IOException {
    this.spawnBlockPos = buf.readBlockPos();
  }
  
  public void writePacketData(PacketBuffer buf) throws IOException {
    buf.writeBlockPos(this.spawnBlockPos);
  }
  
  public void processPacket(INetHandlerPlayClient handler) {
    handler.handleSpawnPosition(this);
  }
  
  public BlockPos getSpawnPos() {
    return this.spawnBlockPos;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\network\play\server\SPacketSpawnPosition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
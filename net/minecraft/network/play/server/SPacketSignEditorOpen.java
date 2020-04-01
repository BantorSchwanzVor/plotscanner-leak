package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.math.BlockPos;

public class SPacketSignEditorOpen implements Packet<INetHandlerPlayClient> {
  private BlockPos signPosition;
  
  public SPacketSignEditorOpen() {}
  
  public SPacketSignEditorOpen(BlockPos posIn) {
    this.signPosition = posIn;
  }
  
  public void processPacket(INetHandlerPlayClient handler) {
    handler.handleSignEditorOpen(this);
  }
  
  public void readPacketData(PacketBuffer buf) throws IOException {
    this.signPosition = buf.readBlockPos();
  }
  
  public void writePacketData(PacketBuffer buf) throws IOException {
    buf.writeBlockPos(this.signPosition);
  }
  
  public BlockPos getSignPosition() {
    return this.signPosition;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\network\play\server\SPacketSignEditorOpen.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
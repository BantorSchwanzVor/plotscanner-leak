package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.math.BlockPos;

public class SPacketBlockBreakAnim implements Packet<INetHandlerPlayClient> {
  private int breakerId;
  
  private BlockPos position;
  
  private int progress;
  
  public SPacketBlockBreakAnim() {}
  
  public SPacketBlockBreakAnim(int breakerIdIn, BlockPos positionIn, int progressIn) {
    this.breakerId = breakerIdIn;
    this.position = positionIn;
    this.progress = progressIn;
  }
  
  public void readPacketData(PacketBuffer buf) throws IOException {
    this.breakerId = buf.readVarIntFromBuffer();
    this.position = buf.readBlockPos();
    this.progress = buf.readUnsignedByte();
  }
  
  public void writePacketData(PacketBuffer buf) throws IOException {
    buf.writeVarIntToBuffer(this.breakerId);
    buf.writeBlockPos(this.position);
    buf.writeByte(this.progress);
  }
  
  public void processPacket(INetHandlerPlayClient handler) {
    handler.handleBlockBreakAnim(this);
  }
  
  public int getBreakerId() {
    return this.breakerId;
  }
  
  public BlockPos getPosition() {
    return this.position;
  }
  
  public int getProgress() {
    return this.progress;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\network\play\server\SPacketBlockBreakAnim.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
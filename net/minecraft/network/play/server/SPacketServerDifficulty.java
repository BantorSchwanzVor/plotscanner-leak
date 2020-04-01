package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.world.EnumDifficulty;

public class SPacketServerDifficulty implements Packet<INetHandlerPlayClient> {
  private EnumDifficulty difficulty;
  
  private boolean difficultyLocked;
  
  public SPacketServerDifficulty() {}
  
  public SPacketServerDifficulty(EnumDifficulty difficultyIn, boolean difficultyLockedIn) {
    this.difficulty = difficultyIn;
    this.difficultyLocked = difficultyLockedIn;
  }
  
  public void processPacket(INetHandlerPlayClient handler) {
    handler.handleServerDifficulty(this);
  }
  
  public void readPacketData(PacketBuffer buf) throws IOException {
    this.difficulty = EnumDifficulty.getDifficultyEnum(buf.readUnsignedByte());
  }
  
  public void writePacketData(PacketBuffer buf) throws IOException {
    buf.writeByte(this.difficulty.getDifficultyId());
  }
  
  public boolean isDifficultyLocked() {
    return this.difficultyLocked;
  }
  
  public EnumDifficulty getDifficulty() {
    return this.difficulty;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\network\play\server\SPacketServerDifficulty.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
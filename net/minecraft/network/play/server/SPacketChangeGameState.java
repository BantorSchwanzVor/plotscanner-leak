package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketChangeGameState implements Packet<INetHandlerPlayClient> {
  public static final String[] MESSAGE_NAMES = new String[] { "tile.bed.notValid" };
  
  private int state;
  
  private float value;
  
  public SPacketChangeGameState() {}
  
  public SPacketChangeGameState(int stateIn, float valueIn) {
    this.state = stateIn;
    this.value = valueIn;
  }
  
  public void readPacketData(PacketBuffer buf) throws IOException {
    this.state = buf.readUnsignedByte();
    this.value = buf.readFloat();
  }
  
  public void writePacketData(PacketBuffer buf) throws IOException {
    buf.writeByte(this.state);
    buf.writeFloat(this.value);
  }
  
  public void processPacket(INetHandlerPlayClient handler) {
    handler.handleChangeGameState(this);
  }
  
  public int getGameState() {
    return this.state;
  }
  
  public float getValue() {
    return this.value;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\network\play\server\SPacketChangeGameState.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
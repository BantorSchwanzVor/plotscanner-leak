package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketUpdateHealth implements Packet<INetHandlerPlayClient> {
  private float health;
  
  private int foodLevel;
  
  private float saturationLevel;
  
  public SPacketUpdateHealth() {}
  
  public SPacketUpdateHealth(float healthIn, int foodLevelIn, float saturationLevelIn) {
    this.health = healthIn;
    this.foodLevel = foodLevelIn;
    this.saturationLevel = saturationLevelIn;
  }
  
  public void readPacketData(PacketBuffer buf) throws IOException {
    this.health = buf.readFloat();
    this.foodLevel = buf.readVarIntFromBuffer();
    this.saturationLevel = buf.readFloat();
  }
  
  public void writePacketData(PacketBuffer buf) throws IOException {
    buf.writeFloat(this.health);
    buf.writeVarIntToBuffer(this.foodLevel);
    buf.writeFloat(this.saturationLevel);
  }
  
  public void processPacket(INetHandlerPlayClient handler) {
    handler.handleUpdateHealth(this);
  }
  
  public float getHealth() {
    return this.health;
  }
  
  public int getFoodLevel() {
    return this.foodLevel;
  }
  
  public float getSaturationLevel() {
    return this.saturationLevel;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\network\play\server\SPacketUpdateHealth.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
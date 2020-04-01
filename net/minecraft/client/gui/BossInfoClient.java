package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.server.SPacketUpdateBossInfo;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.BossInfo;

public class BossInfoClient extends BossInfo {
  protected float rawPercent;
  
  protected long percentSetTime;
  
  public BossInfoClient(SPacketUpdateBossInfo packetIn) {
    super(packetIn.getUniqueId(), packetIn.getName(), packetIn.getColor(), packetIn.getOverlay());
    this.rawPercent = packetIn.getPercent();
    this.percent = packetIn.getPercent();
    this.percentSetTime = Minecraft.getSystemTime();
    setDarkenSky(packetIn.shouldDarkenSky());
    setPlayEndBossMusic(packetIn.shouldPlayEndBossMusic());
    setCreateFog(packetIn.shouldCreateFog());
  }
  
  public void setPercent(float percentIn) {
    this.percent = getPercent();
    this.rawPercent = percentIn;
    this.percentSetTime = Minecraft.getSystemTime();
  }
  
  public float getPercent() {
    long i = Minecraft.getSystemTime() - this.percentSetTime;
    float f = MathHelper.clamp((float)i / 100.0F, 0.0F, 1.0F);
    return this.percent + (this.rawPercent - this.percent) * f;
  }
  
  public void updateFromPacket(SPacketUpdateBossInfo packetIn) {
    switch (packetIn.getOperation()) {
      case UPDATE_NAME:
        setName(packetIn.getName());
        break;
      case UPDATE_PCT:
        setPercent(packetIn.getPercent());
        break;
      case UPDATE_STYLE:
        setColor(packetIn.getColor());
        setOverlay(packetIn.getOverlay());
        break;
      case UPDATE_PROPERTIES:
        setDarkenSky(packetIn.shouldDarkenSky());
        setPlayEndBossMusic(packetIn.shouldPlayEndBossMusic());
        break;
    } 
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\gui\BossInfoClient.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
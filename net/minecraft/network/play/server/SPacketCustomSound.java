package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.SoundCategory;
import org.apache.commons.lang3.Validate;

public class SPacketCustomSound implements Packet<INetHandlerPlayClient> {
  private String soundName;
  
  private SoundCategory category;
  
  private int x;
  
  private int y = Integer.MAX_VALUE;
  
  private int z;
  
  private float volume;
  
  private float pitch;
  
  public SPacketCustomSound(String soundNameIn, SoundCategory categoryIn, double xIn, double yIn, double zIn, float volumeIn, float pitchIn) {
    Validate.notNull(soundNameIn, "name", new Object[0]);
    this.soundName = soundNameIn;
    this.category = categoryIn;
    this.x = (int)(xIn * 8.0D);
    this.y = (int)(yIn * 8.0D);
    this.z = (int)(zIn * 8.0D);
    this.volume = volumeIn;
    this.pitch = pitchIn;
  }
  
  public void readPacketData(PacketBuffer buf) throws IOException {
    this.soundName = buf.readStringFromBuffer(256);
    this.category = (SoundCategory)buf.readEnumValue(SoundCategory.class);
    this.x = buf.readInt();
    this.y = buf.readInt();
    this.z = buf.readInt();
    this.volume = buf.readFloat();
    this.pitch = buf.readFloat();
  }
  
  public void writePacketData(PacketBuffer buf) throws IOException {
    buf.writeString(this.soundName);
    buf.writeEnumValue((Enum)this.category);
    buf.writeInt(this.x);
    buf.writeInt(this.y);
    buf.writeInt(this.z);
    buf.writeFloat(this.volume);
    buf.writeFloat(this.pitch);
  }
  
  public String getSoundName() {
    return this.soundName;
  }
  
  public SoundCategory getCategory() {
    return this.category;
  }
  
  public double getX() {
    return (this.x / 8.0F);
  }
  
  public double getY() {
    return (this.y / 8.0F);
  }
  
  public double getZ() {
    return (this.z / 8.0F);
  }
  
  public float getVolume() {
    return this.volume;
  }
  
  public float getPitch() {
    return this.pitch;
  }
  
  public void processPacket(INetHandlerPlayClient handler) {
    handler.handleCustomSound(this);
  }
  
  public SPacketCustomSound() {}
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\network\play\server\SPacketCustomSound.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
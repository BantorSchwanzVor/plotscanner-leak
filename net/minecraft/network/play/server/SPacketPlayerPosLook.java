package net.minecraft.network.play.server;

import java.io.IOException;
import java.util.EnumSet;
import java.util.Set;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketPlayerPosLook implements Packet<INetHandlerPlayClient> {
  private double x;
  
  private double y;
  
  private double z;
  
  private float yaw;
  
  private float pitch;
  
  private Set<EnumFlags> flags;
  
  private int teleportId;
  
  public SPacketPlayerPosLook() {}
  
  public SPacketPlayerPosLook(double xIn, double yIn, double zIn, float yawIn, float pitchIn, Set<EnumFlags> flagsIn, int teleportIdIn) {
    this.x = xIn;
    this.y = yIn;
    this.z = zIn;
    this.yaw = yawIn;
    this.pitch = pitchIn;
    this.flags = flagsIn;
    this.teleportId = teleportIdIn;
  }
  
  public void readPacketData(PacketBuffer buf) throws IOException {
    this.x = buf.readDouble();
    this.y = buf.readDouble();
    this.z = buf.readDouble();
    this.yaw = buf.readFloat();
    this.pitch = buf.readFloat();
    this.flags = EnumFlags.unpack(buf.readUnsignedByte());
    this.teleportId = buf.readVarIntFromBuffer();
  }
  
  public void writePacketData(PacketBuffer buf) throws IOException {
    buf.writeDouble(this.x);
    buf.writeDouble(this.y);
    buf.writeDouble(this.z);
    buf.writeFloat(this.yaw);
    buf.writeFloat(this.pitch);
    buf.writeByte(EnumFlags.pack(this.flags));
    buf.writeVarIntToBuffer(this.teleportId);
  }
  
  public void processPacket(INetHandlerPlayClient handler) {
    handler.handlePlayerPosLook(this);
  }
  
  public double getX() {
    return this.x;
  }
  
  public double getY() {
    return this.y;
  }
  
  public double getZ() {
    return this.z;
  }
  
  public float getYaw() {
    return this.yaw;
  }
  
  public float getPitch() {
    return this.pitch;
  }
  
  public int getTeleportId() {
    return this.teleportId;
  }
  
  public Set<EnumFlags> getFlags() {
    return this.flags;
  }
  
  public enum EnumFlags {
    X(0),
    Y(1),
    Z(2),
    Y_ROT(3),
    X_ROT(4);
    
    private final int bit;
    
    EnumFlags(int p_i46690_3_) {
      this.bit = p_i46690_3_;
    }
    
    private int getMask() {
      return 1 << this.bit;
    }
    
    private boolean isSet(int p_187043_1_) {
      return ((p_187043_1_ & getMask()) == getMask());
    }
    
    public static Set<EnumFlags> unpack(int flags) {
      Set<EnumFlags> set = EnumSet.noneOf(EnumFlags.class);
      byte b;
      int i;
      EnumFlags[] arrayOfEnumFlags;
      for (i = (arrayOfEnumFlags = values()).length, b = 0; b < i; ) {
        EnumFlags spacketplayerposlook$enumflags = arrayOfEnumFlags[b];
        if (spacketplayerposlook$enumflags.isSet(flags))
          set.add(spacketplayerposlook$enumflags); 
        b++;
      } 
      return set;
    }
    
    public static int pack(Set<EnumFlags> flags) {
      int i = 0;
      for (EnumFlags spacketplayerposlook$enumflags : flags)
        i |= spacketplayerposlook$enumflags.getMask(); 
      return i;
    }
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\network\play\server\SPacketPlayerPosLook.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
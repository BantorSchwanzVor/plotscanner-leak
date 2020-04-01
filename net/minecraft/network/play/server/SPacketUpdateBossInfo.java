package net.minecraft.network.play.server;

import java.io.IOException;
import java.util.UUID;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.BossInfo;

public class SPacketUpdateBossInfo implements Packet<INetHandlerPlayClient> {
  private UUID uniqueId;
  
  private Operation operation;
  
  private ITextComponent name;
  
  private float percent;
  
  private BossInfo.Color color;
  
  private BossInfo.Overlay overlay;
  
  private boolean darkenSky;
  
  private boolean playEndBossMusic;
  
  private boolean createFog;
  
  public SPacketUpdateBossInfo() {}
  
  public SPacketUpdateBossInfo(Operation operationIn, BossInfo data) {
    this.operation = operationIn;
    this.uniqueId = data.getUniqueId();
    this.name = data.getName();
    this.percent = data.getPercent();
    this.color = data.getColor();
    this.overlay = data.getOverlay();
    this.darkenSky = data.shouldDarkenSky();
    this.playEndBossMusic = data.shouldPlayEndBossMusic();
    this.createFog = data.shouldCreateFog();
  }
  
  public void readPacketData(PacketBuffer buf) throws IOException {
    this.uniqueId = buf.readUuid();
    this.operation = (Operation)buf.readEnumValue(Operation.class);
    switch (this.operation) {
      case null:
        this.name = buf.readTextComponent();
        this.percent = buf.readFloat();
        this.color = (BossInfo.Color)buf.readEnumValue(BossInfo.Color.class);
        this.overlay = (BossInfo.Overlay)buf.readEnumValue(BossInfo.Overlay.class);
        setFlags(buf.readUnsignedByte());
      default:
        return;
      case UPDATE_PCT:
        this.percent = buf.readFloat();
      case UPDATE_NAME:
        this.name = buf.readTextComponent();
      case UPDATE_STYLE:
        this.color = (BossInfo.Color)buf.readEnumValue(BossInfo.Color.class);
        this.overlay = (BossInfo.Overlay)buf.readEnumValue(BossInfo.Overlay.class);
      case UPDATE_PROPERTIES:
        break;
    } 
    setFlags(buf.readUnsignedByte());
  }
  
  private void setFlags(int flags) {
    this.darkenSky = ((flags & 0x1) > 0);
    this.playEndBossMusic = ((flags & 0x2) > 0);
    this.createFog = ((flags & 0x2) > 0);
  }
  
  public void writePacketData(PacketBuffer buf) throws IOException {
    buf.writeUuid(this.uniqueId);
    buf.writeEnumValue(this.operation);
    switch (this.operation) {
      case null:
        buf.writeTextComponent(this.name);
        buf.writeFloat(this.percent);
        buf.writeEnumValue((Enum)this.color);
        buf.writeEnumValue((Enum)this.overlay);
        buf.writeByte(getFlags());
      default:
        return;
      case UPDATE_PCT:
        buf.writeFloat(this.percent);
      case UPDATE_NAME:
        buf.writeTextComponent(this.name);
      case UPDATE_STYLE:
        buf.writeEnumValue((Enum)this.color);
        buf.writeEnumValue((Enum)this.overlay);
      case UPDATE_PROPERTIES:
        break;
    } 
    buf.writeByte(getFlags());
  }
  
  private int getFlags() {
    int i = 0;
    if (this.darkenSky)
      i |= 0x1; 
    if (this.playEndBossMusic)
      i |= 0x2; 
    if (this.createFog)
      i |= 0x2; 
    return i;
  }
  
  public void processPacket(INetHandlerPlayClient handler) {
    handler.handleUpdateEntityNBT(this);
  }
  
  public UUID getUniqueId() {
    return this.uniqueId;
  }
  
  public Operation getOperation() {
    return this.operation;
  }
  
  public ITextComponent getName() {
    return this.name;
  }
  
  public float getPercent() {
    return this.percent;
  }
  
  public BossInfo.Color getColor() {
    return this.color;
  }
  
  public BossInfo.Overlay getOverlay() {
    return this.overlay;
  }
  
  public boolean shouldDarkenSky() {
    return this.darkenSky;
  }
  
  public boolean shouldPlayEndBossMusic() {
    return this.playEndBossMusic;
  }
  
  public boolean shouldCreateFog() {
    return this.createFog;
  }
  
  public enum Operation {
    ADD, REMOVE, UPDATE_PCT, UPDATE_NAME, UPDATE_STYLE, UPDATE_PROPERTIES;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\network\play\server\SPacketUpdateBossInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
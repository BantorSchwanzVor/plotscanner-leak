package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketWindowProperty implements Packet<INetHandlerPlayClient> {
  private int windowId;
  
  private int property;
  
  private int value;
  
  public SPacketWindowProperty() {}
  
  public SPacketWindowProperty(int windowIdIn, int propertyIn, int valueIn) {
    this.windowId = windowIdIn;
    this.property = propertyIn;
    this.value = valueIn;
  }
  
  public void processPacket(INetHandlerPlayClient handler) {
    handler.handleWindowProperty(this);
  }
  
  public void readPacketData(PacketBuffer buf) throws IOException {
    this.windowId = buf.readUnsignedByte();
    this.property = buf.readShort();
    this.value = buf.readShort();
  }
  
  public void writePacketData(PacketBuffer buf) throws IOException {
    buf.writeByte(this.windowId);
    buf.writeShort(this.property);
    buf.writeShort(this.value);
  }
  
  public int getWindowId() {
    return this.windowId;
  }
  
  public int getProperty() {
    return this.property;
  }
  
  public int getValue() {
    return this.value;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\network\play\server\SPacketWindowProperty.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.item.ItemStack;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketSetSlot implements Packet<INetHandlerPlayClient> {
  private int windowId;
  
  private int slot;
  
  private ItemStack item = ItemStack.field_190927_a;
  
  public SPacketSetSlot(int windowIdIn, int slotIn, ItemStack itemIn) {
    this.windowId = windowIdIn;
    this.slot = slotIn;
    this.item = itemIn.copy();
  }
  
  public void processPacket(INetHandlerPlayClient handler) {
    handler.handleSetSlot(this);
  }
  
  public void readPacketData(PacketBuffer buf) throws IOException {
    this.windowId = buf.readByte();
    this.slot = buf.readShort();
    this.item = buf.readItemStackFromBuffer();
  }
  
  public void writePacketData(PacketBuffer buf) throws IOException {
    buf.writeByte(this.windowId);
    buf.writeShort(this.slot);
    buf.writeItemStackToBuffer(this.item);
  }
  
  public int getWindowId() {
    return this.windowId;
  }
  
  public int getSlot() {
    return this.slot;
  }
  
  public ItemStack getStack() {
    return this.item;
  }
  
  public SPacketSetSlot() {}
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\network\play\server\SPacketSetSlot.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
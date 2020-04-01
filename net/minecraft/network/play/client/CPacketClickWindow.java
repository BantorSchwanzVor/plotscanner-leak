package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class CPacketClickWindow implements Packet<INetHandlerPlayServer> {
  private int windowId;
  
  private int slotId;
  
  private int usedButton;
  
  private short actionNumber;
  
  private ItemStack clickedItem = ItemStack.field_190927_a;
  
  private ClickType mode;
  
  public CPacketClickWindow(int windowIdIn, int slotIdIn, int usedButtonIn, ClickType modeIn, ItemStack clickedItemIn, short actionNumberIn) {
    this.windowId = windowIdIn;
    this.slotId = slotIdIn;
    this.usedButton = usedButtonIn;
    this.clickedItem = clickedItemIn.copy();
    this.actionNumber = actionNumberIn;
    this.mode = modeIn;
  }
  
  public void processPacket(INetHandlerPlayServer handler) {
    handler.processClickWindow(this);
  }
  
  public void readPacketData(PacketBuffer buf) throws IOException {
    this.windowId = buf.readByte();
    this.slotId = buf.readShort();
    this.usedButton = buf.readByte();
    this.actionNumber = buf.readShort();
    this.mode = (ClickType)buf.readEnumValue(ClickType.class);
    this.clickedItem = buf.readItemStackFromBuffer();
  }
  
  public void writePacketData(PacketBuffer buf) throws IOException {
    buf.writeByte(this.windowId);
    buf.writeShort(this.slotId);
    buf.writeByte(this.usedButton);
    buf.writeShort(this.actionNumber);
    buf.writeEnumValue((Enum)this.mode);
    buf.writeItemStackToBuffer(this.clickedItem);
  }
  
  public int getWindowId() {
    return this.windowId;
  }
  
  public int getSlotId() {
    return this.slotId;
  }
  
  public int getUsedButton() {
    return this.usedButton;
  }
  
  public short getActionNumber() {
    return this.actionNumber;
  }
  
  public ItemStack getClickedItem() {
    return this.clickedItem;
  }
  
  public ClickType getClickType() {
    return this.mode;
  }
  
  public CPacketClickWindow() {}
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\network\play\client\CPacketClickWindow.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
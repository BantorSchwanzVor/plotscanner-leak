package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketEntityEquipment implements Packet<INetHandlerPlayClient> {
  private int entityID;
  
  private EntityEquipmentSlot equipmentSlot;
  
  private ItemStack itemStack = ItemStack.field_190927_a;
  
  public SPacketEntityEquipment(int entityIdIn, EntityEquipmentSlot equipmentSlotIn, ItemStack itemStackIn) {
    this.entityID = entityIdIn;
    this.equipmentSlot = equipmentSlotIn;
    this.itemStack = itemStackIn.copy();
  }
  
  public void readPacketData(PacketBuffer buf) throws IOException {
    this.entityID = buf.readVarIntFromBuffer();
    this.equipmentSlot = (EntityEquipmentSlot)buf.readEnumValue(EntityEquipmentSlot.class);
    this.itemStack = buf.readItemStackFromBuffer();
  }
  
  public void writePacketData(PacketBuffer buf) throws IOException {
    buf.writeVarIntToBuffer(this.entityID);
    buf.writeEnumValue((Enum)this.equipmentSlot);
    buf.writeItemStackToBuffer(this.itemStack);
  }
  
  public void processPacket(INetHandlerPlayClient handler) {
    handler.handleEntityEquipment(this);
  }
  
  public ItemStack getItemStack() {
    return this.itemStack;
  }
  
  public int getEntityID() {
    return this.entityID;
  }
  
  public EntityEquipmentSlot getEquipmentSlot() {
    return this.equipmentSlot;
  }
  
  public SPacketEntityEquipment() {}
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\network\play\server\SPacketEntityEquipment.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
package net.minecraft.tileentity;

import javax.annotation.Nullable;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.DataFixer;

public class TileEntityFlowerPot extends TileEntity {
  private Item flowerPotItem;
  
  private int flowerPotData;
  
  public TileEntityFlowerPot() {}
  
  public TileEntityFlowerPot(Item potItem, int potData) {
    this.flowerPotItem = potItem;
    this.flowerPotData = potData;
  }
  
  public static void registerFixesFlowerPot(DataFixer fixer) {}
  
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {
    super.writeToNBT(compound);
    ResourceLocation resourcelocation = (ResourceLocation)Item.REGISTRY.getNameForObject(this.flowerPotItem);
    compound.setString("Item", (resourcelocation == null) ? "" : resourcelocation.toString());
    compound.setInteger("Data", this.flowerPotData);
    return compound;
  }
  
  public void readFromNBT(NBTTagCompound compound) {
    super.readFromNBT(compound);
    if (compound.hasKey("Item", 8)) {
      this.flowerPotItem = Item.getByNameOrId(compound.getString("Item"));
    } else {
      this.flowerPotItem = Item.getItemById(compound.getInteger("Item"));
    } 
    this.flowerPotData = compound.getInteger("Data");
  }
  
  @Nullable
  public SPacketUpdateTileEntity getUpdatePacket() {
    return new SPacketUpdateTileEntity(this.pos, 5, getUpdateTag());
  }
  
  public NBTTagCompound getUpdateTag() {
    return writeToNBT(new NBTTagCompound());
  }
  
  public void func_190614_a(ItemStack p_190614_1_) {
    this.flowerPotItem = p_190614_1_.getItem();
    this.flowerPotData = p_190614_1_.getMetadata();
  }
  
  public ItemStack getFlowerItemStack() {
    return (this.flowerPotItem == null) ? ItemStack.field_190927_a : new ItemStack(this.flowerPotItem, 1, this.flowerPotData);
  }
  
  @Nullable
  public Item getFlowerPotItem() {
    return this.flowerPotItem;
  }
  
  public int getFlowerPotData() {
    return this.flowerPotData;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\tileentity\TileEntityFlowerPot.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
package net.minecraft.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IWorldNameable;

public interface IInventory extends IWorldNameable {
  int getSizeInventory();
  
  boolean func_191420_l();
  
  ItemStack getStackInSlot(int paramInt);
  
  ItemStack decrStackSize(int paramInt1, int paramInt2);
  
  ItemStack removeStackFromSlot(int paramInt);
  
  void setInventorySlotContents(int paramInt, ItemStack paramItemStack);
  
  int getInventoryStackLimit();
  
  void markDirty();
  
  boolean isUsableByPlayer(EntityPlayer paramEntityPlayer);
  
  void openInventory(EntityPlayer paramEntityPlayer);
  
  void closeInventory(EntityPlayer paramEntityPlayer);
  
  boolean isItemValidForSlot(int paramInt, ItemStack paramItemStack);
  
  int getField(int paramInt);
  
  void setField(int paramInt1, int paramInt2);
  
  int getFieldCount();
  
  void clear();
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\inventory\IInventory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
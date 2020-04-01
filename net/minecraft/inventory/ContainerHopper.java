package net.minecraft.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

public class ContainerHopper extends Container {
  private final IInventory hopperInventory;
  
  public ContainerHopper(InventoryPlayer playerInventory, IInventory hopperInventoryIn, EntityPlayer player) {
    this.hopperInventory = hopperInventoryIn;
    hopperInventoryIn.openInventory(player);
    int i = 51;
    for (int j = 0; j < hopperInventoryIn.getSizeInventory(); j++)
      addSlotToContainer(new Slot(hopperInventoryIn, j, 44 + j * 18, 20)); 
    for (int l = 0; l < 3; l++) {
      for (int k = 0; k < 9; k++)
        addSlotToContainer(new Slot((IInventory)playerInventory, k + l * 9 + 9, 8 + k * 18, l * 18 + 51)); 
    } 
    for (int i1 = 0; i1 < 9; i1++)
      addSlotToContainer(new Slot((IInventory)playerInventory, i1, 8 + i1 * 18, 109)); 
  }
  
  public boolean canInteractWith(EntityPlayer playerIn) {
    return this.hopperInventory.isUsableByPlayer(playerIn);
  }
  
  public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
    ItemStack itemstack = ItemStack.field_190927_a;
    Slot slot = this.inventorySlots.get(index);
    if (slot != null && slot.getHasStack()) {
      ItemStack itemstack1 = slot.getStack();
      itemstack = itemstack1.copy();
      if (index < this.hopperInventory.getSizeInventory()) {
        if (!mergeItemStack(itemstack1, this.hopperInventory.getSizeInventory(), this.inventorySlots.size(), true))
          return ItemStack.field_190927_a; 
      } else if (!mergeItemStack(itemstack1, 0, this.hopperInventory.getSizeInventory(), false)) {
        return ItemStack.field_190927_a;
      } 
      if (itemstack1.func_190926_b()) {
        slot.putStack(ItemStack.field_190927_a);
      } else {
        slot.onSlotChanged();
      } 
    } 
    return itemstack;
  }
  
  public void onContainerClosed(EntityPlayer playerIn) {
    super.onContainerClosed(playerIn);
    this.hopperInventory.closeInventory(playerIn);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\inventory\ContainerHopper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
package net.minecraft.inventory;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;

public class SlotFurnaceFuel extends Slot {
  public SlotFurnaceFuel(IInventory inventoryIn, int slotIndex, int xPosition, int yPosition) {
    super(inventoryIn, slotIndex, xPosition, yPosition);
  }
  
  public boolean isItemValid(ItemStack stack) {
    return !(!TileEntityFurnace.isItemFuel(stack) && !isBucket(stack));
  }
  
  public int getItemStackLimit(ItemStack stack) {
    return isBucket(stack) ? 1 : super.getItemStackLimit(stack);
  }
  
  public static boolean isBucket(ItemStack stack) {
    return (stack.getItem() == Items.BUCKET);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\inventory\SlotFurnaceFuel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
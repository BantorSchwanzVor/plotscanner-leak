package net.minecraft.inventory;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class SlotShulkerBox extends Slot {
  public SlotShulkerBox(IInventory p_i47265_1_, int p_i47265_2_, int p_i47265_3_, int p_i47265_4_) {
    super(p_i47265_1_, p_i47265_2_, p_i47265_3_, p_i47265_4_);
  }
  
  public boolean isItemValid(ItemStack stack) {
    return !(Block.getBlockFromItem(stack.getItem()) instanceof net.minecraft.block.BlockShulkerBox);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\inventory\SlotShulkerBox.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
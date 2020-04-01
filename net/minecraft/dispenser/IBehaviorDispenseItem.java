package net.minecraft.dispenser;

import net.minecraft.item.ItemStack;

public interface IBehaviorDispenseItem {
  public static final IBehaviorDispenseItem DEFAULT_BEHAVIOR = new IBehaviorDispenseItem() {
      public ItemStack dispense(IBlockSource source, ItemStack stack) {
        return stack;
      }
    };
  
  ItemStack dispense(IBlockSource paramIBlockSource, ItemStack paramItemStack);
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\dispenser\IBehaviorDispenseItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
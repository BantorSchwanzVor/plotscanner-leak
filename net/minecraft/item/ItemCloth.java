package net.minecraft.item;

import net.minecraft.block.Block;

public class ItemCloth extends ItemBlock {
  public ItemCloth(Block block) {
    super(block);
    setMaxDamage(0);
    setHasSubtypes(true);
  }
  
  public int getMetadata(int damage) {
    return damage;
  }
  
  public String getUnlocalizedName(ItemStack stack) {
    return String.valueOf(getUnlocalizedName()) + "." + EnumDyeColor.byMetadata(stack.getMetadata()).getUnlocalizedName();
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\item\ItemCloth.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
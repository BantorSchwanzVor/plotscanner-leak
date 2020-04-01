package net.minecraft.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;

public class ItemLeaves extends ItemBlock {
  private final BlockLeaves leaves;
  
  public ItemLeaves(BlockLeaves block) {
    super((Block)block);
    this.leaves = block;
    setMaxDamage(0);
    setHasSubtypes(true);
  }
  
  public int getMetadata(int damage) {
    return damage | 0x4;
  }
  
  public String getUnlocalizedName(ItemStack stack) {
    return String.valueOf(getUnlocalizedName()) + "." + this.leaves.getWoodType(stack.getMetadata()).getUnlocalizedName();
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\item\ItemLeaves.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
package net.minecraft.item;

import net.minecraft.block.Block;

public class ItemAnvilBlock extends ItemMultiTexture {
  public ItemAnvilBlock(Block block) {
    super(block, block, new String[] { "intact", "slightlyDamaged", "veryDamaged" });
  }
  
  public int getMetadata(int damage) {
    return damage << 2;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\item\ItemAnvilBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
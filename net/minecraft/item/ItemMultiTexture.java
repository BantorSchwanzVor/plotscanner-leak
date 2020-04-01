package net.minecraft.item;

import net.minecraft.block.Block;

public class ItemMultiTexture extends ItemBlock {
  protected final Block theBlock;
  
  protected final Mapper nameFunction;
  
  public ItemMultiTexture(Block p_i47262_1_, Block p_i47262_2_, Mapper p_i47262_3_) {
    super(p_i47262_1_);
    this.theBlock = p_i47262_2_;
    this.nameFunction = p_i47262_3_;
    setMaxDamage(0);
    setHasSubtypes(true);
  }
  
  public ItemMultiTexture(Block block, Block block2, String[] namesByMeta) {
    this(block, block2, new Mapper(namesByMeta) {
          public String apply(ItemStack p_apply_1_) {
            int i = p_apply_1_.getMetadata();
            if (i < 0 || i >= namesByMeta.length)
              i = 0; 
            return namesByMeta[i];
          }
        });
  }
  
  public int getMetadata(int damage) {
    return damage;
  }
  
  public String getUnlocalizedName(ItemStack stack) {
    return String.valueOf(getUnlocalizedName()) + "." + this.nameFunction.apply(stack);
  }
  
  public static interface Mapper {
    String apply(ItemStack param1ItemStack);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\item\ItemMultiTexture.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
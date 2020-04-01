package net.minecraft.item;

import net.minecraft.block.Block;

public class ItemColored extends ItemBlock {
  private String[] subtypeNames;
  
  public ItemColored(Block block, boolean hasSubtypes) {
    super(block);
    if (hasSubtypes) {
      setMaxDamage(0);
      setHasSubtypes(true);
    } 
  }
  
  public int getMetadata(int damage) {
    return damage;
  }
  
  public ItemColored setSubtypeNames(String[] names) {
    this.subtypeNames = names;
    return this;
  }
  
  public String getUnlocalizedName(ItemStack stack) {
    if (this.subtypeNames == null)
      return super.getUnlocalizedName(stack); 
    int i = stack.getMetadata();
    return (i >= 0 && i < this.subtypeNames.length) ? (String.valueOf(super.getUnlocalizedName(stack)) + "." + this.subtypeNames[i]) : super.getUnlocalizedName(stack);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\item\ItemColored.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
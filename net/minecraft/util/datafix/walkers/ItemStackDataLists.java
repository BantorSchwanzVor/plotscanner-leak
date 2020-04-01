package net.minecraft.util.datafix.walkers;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.DataFixesManager;
import net.minecraft.util.datafix.IDataFixer;

public class ItemStackDataLists extends Filtered {
  private final String[] matchingTags;
  
  public ItemStackDataLists(Class<?> p_i47310_1_, String... p_i47310_2_) {
    super(p_i47310_1_);
    this.matchingTags = p_i47310_2_;
  }
  
  NBTTagCompound filteredProcess(IDataFixer fixer, NBTTagCompound compound, int versionIn) {
    byte b;
    int i;
    String[] arrayOfString;
    for (i = (arrayOfString = this.matchingTags).length, b = 0; b < i; ) {
      String s = arrayOfString[b];
      compound = DataFixesManager.processInventory(fixer, compound, versionIn, s);
      b++;
    } 
    return compound;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraf\\util\datafix\walkers\ItemStackDataLists.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
package net.minecraft.client.settings;

import java.util.ArrayList;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class HotbarSnapshot extends ArrayList<ItemStack> {
  public static final int field_192835_a = InventoryPlayer.getHotbarSize();
  
  public HotbarSnapshot() {
    ensureCapacity(field_192835_a);
    for (int i = 0; i < field_192835_a; i++)
      add(ItemStack.field_190927_a); 
  }
  
  public NBTTagList func_192834_a() {
    NBTTagList nbttaglist = new NBTTagList();
    for (int i = 0; i < field_192835_a; i++)
      nbttaglist.appendTag((NBTBase)get(i).writeToNBT(new NBTTagCompound())); 
    return nbttaglist;
  }
  
  public void func_192833_a(NBTTagList p_192833_1_) {
    for (int i = 0; i < field_192835_a; i++)
      set(i, new ItemStack(p_192833_1_.getCompoundTagAt(i))); 
  }
  
  public boolean isEmpty() {
    for (int i = 0; i < field_192835_a; i++) {
      if (!get(i).func_190926_b())
        return false; 
    } 
    return true;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\settings\HotbarSnapshot.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
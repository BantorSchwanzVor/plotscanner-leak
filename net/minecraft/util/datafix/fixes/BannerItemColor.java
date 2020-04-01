package net.minecraft.util.datafix.fixes;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.datafix.IFixableData;

public class BannerItemColor implements IFixableData {
  public int getFixVersion() {
    return 804;
  }
  
  public NBTTagCompound fixTagCompound(NBTTagCompound compound) {
    if ("minecraft:banner".equals(compound.getString("id")) && compound.hasKey("tag", 10)) {
      NBTTagCompound nbttagcompound = compound.getCompoundTag("tag");
      if (nbttagcompound.hasKey("BlockEntityTag", 10)) {
        NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("BlockEntityTag");
        if (nbttagcompound1.hasKey("Base", 99)) {
          compound.setShort("Damage", (short)(nbttagcompound1.getShort("Base") & 0xF));
          if (nbttagcompound.hasKey("display", 10)) {
            NBTTagCompound nbttagcompound2 = nbttagcompound.getCompoundTag("display");
            if (nbttagcompound2.hasKey("Lore", 9)) {
              NBTTagList nbttaglist = nbttagcompound2.getTagList("Lore", 8);
              if (nbttaglist.tagCount() == 1 && "(+NBT)".equals(nbttaglist.getStringTagAt(0)))
                return compound; 
            } 
          } 
          nbttagcompound1.removeTag("Base");
          if (nbttagcompound1.hasNoTags())
            nbttagcompound.removeTag("BlockEntityTag"); 
          if (nbttagcompound.hasNoTags())
            compound.removeTag("tag"); 
        } 
      } 
    } 
    return compound;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraf\\util\datafix\fixes\BannerItemColor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
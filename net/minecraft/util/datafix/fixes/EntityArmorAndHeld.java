package net.minecraft.util.datafix.fixes;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.datafix.IFixableData;

public class EntityArmorAndHeld implements IFixableData {
  public int getFixVersion() {
    return 100;
  }
  
  public NBTTagCompound fixTagCompound(NBTTagCompound compound) {
    NBTTagList nbttaglist = compound.getTagList("Equipment", 10);
    if (!nbttaglist.hasNoTags() && !compound.hasKey("HandItems", 10)) {
      NBTTagList nbttaglist1 = new NBTTagList();
      nbttaglist1.appendTag(nbttaglist.get(0));
      nbttaglist1.appendTag((NBTBase)new NBTTagCompound());
      compound.setTag("HandItems", (NBTBase)nbttaglist1);
    } 
    if (nbttaglist.tagCount() > 1 && !compound.hasKey("ArmorItem", 10)) {
      NBTTagList nbttaglist3 = new NBTTagList();
      nbttaglist3.appendTag((NBTBase)nbttaglist.getCompoundTagAt(1));
      nbttaglist3.appendTag((NBTBase)nbttaglist.getCompoundTagAt(2));
      nbttaglist3.appendTag((NBTBase)nbttaglist.getCompoundTagAt(3));
      nbttaglist3.appendTag((NBTBase)nbttaglist.getCompoundTagAt(4));
      compound.setTag("ArmorItems", (NBTBase)nbttaglist3);
    } 
    compound.removeTag("Equipment");
    if (compound.hasKey("DropChances", 9)) {
      NBTTagList nbttaglist4 = compound.getTagList("DropChances", 5);
      if (!compound.hasKey("HandDropChances", 10)) {
        NBTTagList nbttaglist2 = new NBTTagList();
        nbttaglist2.appendTag((NBTBase)new NBTTagFloat(nbttaglist4.getFloatAt(0)));
        nbttaglist2.appendTag((NBTBase)new NBTTagFloat(0.0F));
        compound.setTag("HandDropChances", (NBTBase)nbttaglist2);
      } 
      if (!compound.hasKey("ArmorDropChances", 10)) {
        NBTTagList nbttaglist5 = new NBTTagList();
        nbttaglist5.appendTag((NBTBase)new NBTTagFloat(nbttaglist4.getFloatAt(1)));
        nbttaglist5.appendTag((NBTBase)new NBTTagFloat(nbttaglist4.getFloatAt(2)));
        nbttaglist5.appendTag((NBTBase)new NBTTagFloat(nbttaglist4.getFloatAt(3)));
        nbttaglist5.appendTag((NBTBase)new NBTTagFloat(nbttaglist4.getFloatAt(4)));
        compound.setTag("ArmorDropChances", (NBTBase)nbttaglist5);
      } 
      compound.removeTag("DropChances");
    } 
    return compound;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraf\\util\datafix\fixes\EntityArmorAndHeld.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
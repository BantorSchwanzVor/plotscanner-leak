package net.minecraft.util.datafix.fixes;

import net.minecraft.item.EnumDyeColor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;

public class BedItemColor implements IFixableData {
  public int getFixVersion() {
    return 1125;
  }
  
  public NBTTagCompound fixTagCompound(NBTTagCompound compound) {
    if ("minecraft:bed".equals(compound.getString("id")) && compound.getShort("Damage") == 0)
      compound.setShort("Damage", (short)EnumDyeColor.RED.getMetadata()); 
    return compound;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraf\\util\datafix\fixes\BedItemColor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
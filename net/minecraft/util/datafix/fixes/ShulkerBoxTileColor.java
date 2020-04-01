package net.minecraft.util.datafix.fixes;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;

public class ShulkerBoxTileColor implements IFixableData {
  public int getFixVersion() {
    return 813;
  }
  
  public NBTTagCompound fixTagCompound(NBTTagCompound compound) {
    if ("minecraft:shulker".equals(compound.getString("id")))
      compound.removeTag("Color"); 
    return compound;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraf\\util\datafix\fixes\ShulkerBoxTileColor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
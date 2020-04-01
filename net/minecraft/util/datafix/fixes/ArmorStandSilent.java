package net.minecraft.util.datafix.fixes;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;

public class ArmorStandSilent implements IFixableData {
  public int getFixVersion() {
    return 147;
  }
  
  public NBTTagCompound fixTagCompound(NBTTagCompound compound) {
    if ("ArmorStand".equals(compound.getString("id")) && compound.getBoolean("Silent") && !compound.getBoolean("Marker"))
      compound.removeTag("Silent"); 
    return compound;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraf\\util\datafix\fixes\ArmorStandSilent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
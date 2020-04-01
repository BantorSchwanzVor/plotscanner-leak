package net.minecraft.util.datafix.fixes;

import java.util.UUID;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;

public class StringToUUID implements IFixableData {
  public int getFixVersion() {
    return 108;
  }
  
  public NBTTagCompound fixTagCompound(NBTTagCompound compound) {
    if (compound.hasKey("UUID", 8))
      compound.setUniqueId("UUID", UUID.fromString(compound.getString("UUID"))); 
    return compound;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraf\\util\datafix\fixes\StringToUUID.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
package net.minecraft.util.datafix.fixes;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;

public class ForceVBOOn implements IFixableData {
  public int getFixVersion() {
    return 505;
  }
  
  public NBTTagCompound fixTagCompound(NBTTagCompound compound) {
    compound.setString("useVbo", "true");
    return compound;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraf\\util\datafix\fixes\ForceVBOOn.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
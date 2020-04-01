package net.minecraft.util.datafix.fixes;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;

public class ElderGuardianSplit implements IFixableData {
  public int getFixVersion() {
    return 700;
  }
  
  public NBTTagCompound fixTagCompound(NBTTagCompound compound) {
    if ("Guardian".equals(compound.getString("id"))) {
      if (compound.getBoolean("Elder"))
        compound.setString("id", "ElderGuardian"); 
      compound.removeTag("Elder");
    } 
    return compound;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraf\\util\datafix\fixes\ElderGuardianSplit.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
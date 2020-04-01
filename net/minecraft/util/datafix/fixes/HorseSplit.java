package net.minecraft.util.datafix.fixes;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;

public class HorseSplit implements IFixableData {
  public int getFixVersion() {
    return 703;
  }
  
  public NBTTagCompound fixTagCompound(NBTTagCompound compound) {
    if ("EntityHorse".equals(compound.getString("id"))) {
      int i = compound.getInteger("Type");
      switch (i) {
        default:
          compound.setString("id", "Horse");
          break;
        case 1:
          compound.setString("id", "Donkey");
          break;
        case 2:
          compound.setString("id", "Mule");
          break;
        case 3:
          compound.setString("id", "ZombieHorse");
          break;
        case 4:
          compound.setString("id", "SkeletonHorse");
          break;
      } 
      compound.removeTag("Type");
    } 
    return compound;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraf\\util\datafix\fixes\HorseSplit.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
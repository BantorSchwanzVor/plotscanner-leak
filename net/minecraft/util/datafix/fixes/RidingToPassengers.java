package net.minecraft.util.datafix.fixes;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.datafix.IFixableData;

public class RidingToPassengers implements IFixableData {
  public int getFixVersion() {
    return 135;
  }
  
  public NBTTagCompound fixTagCompound(NBTTagCompound compound) {
    while (compound.hasKey("Riding", 10)) {
      NBTTagCompound nbttagcompound = extractVehicle(compound);
      addPassengerToVehicle(compound, nbttagcompound);
      compound = nbttagcompound;
    } 
    return compound;
  }
  
  protected void addPassengerToVehicle(NBTTagCompound p_188219_1_, NBTTagCompound p_188219_2_) {
    NBTTagList nbttaglist = new NBTTagList();
    nbttaglist.appendTag((NBTBase)p_188219_1_);
    p_188219_2_.setTag("Passengers", (NBTBase)nbttaglist);
  }
  
  protected NBTTagCompound extractVehicle(NBTTagCompound p_188220_1_) {
    NBTTagCompound nbttagcompound = p_188220_1_.getCompoundTag("Riding");
    p_188220_1_.removeTag("Riding");
    return nbttagcompound;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraf\\util\datafix\fixes\RidingToPassengers.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
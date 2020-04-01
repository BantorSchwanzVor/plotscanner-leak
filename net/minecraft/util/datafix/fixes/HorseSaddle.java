package net.minecraft.util.datafix.fixes;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;

public class HorseSaddle implements IFixableData {
  public int getFixVersion() {
    return 110;
  }
  
  public NBTTagCompound fixTagCompound(NBTTagCompound compound) {
    if ("EntityHorse".equals(compound.getString("id")) && !compound.hasKey("SaddleItem", 10) && compound.getBoolean("Saddle")) {
      NBTTagCompound nbttagcompound = new NBTTagCompound();
      nbttagcompound.setString("id", "minecraft:saddle");
      nbttagcompound.setByte("Count", (byte)1);
      nbttagcompound.setShort("Damage", (short)0);
      compound.setTag("SaddleItem", (NBTBase)nbttagcompound);
      compound.removeTag("Saddle");
    } 
    return compound;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraf\\util\datafix\fixes\HorseSaddle.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
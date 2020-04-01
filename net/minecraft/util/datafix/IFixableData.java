package net.minecraft.util.datafix;

import net.minecraft.nbt.NBTTagCompound;

public interface IFixableData {
  int getFixVersion();
  
  NBTTagCompound fixTagCompound(NBTTagCompound paramNBTTagCompound);
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraf\\util\datafix\IFixableData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
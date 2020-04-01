package net.minecraft.util.datafix.fixes;

import java.util.Locale;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;

public class OptionsLowerCaseLanguage implements IFixableData {
  public int getFixVersion() {
    return 816;
  }
  
  public NBTTagCompound fixTagCompound(NBTTagCompound compound) {
    if (compound.hasKey("lang", 8))
      compound.setString("lang", compound.getString("lang").toLowerCase(Locale.ROOT)); 
    return compound;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraf\\util\datafix\fixes\OptionsLowerCaseLanguage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
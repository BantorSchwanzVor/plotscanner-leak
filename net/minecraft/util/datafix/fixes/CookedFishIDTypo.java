package net.minecraft.util.datafix.fixes;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.IFixableData;

public class CookedFishIDTypo implements IFixableData {
  private static final ResourceLocation WRONG = new ResourceLocation("cooked_fished");
  
  public int getFixVersion() {
    return 502;
  }
  
  public NBTTagCompound fixTagCompound(NBTTagCompound compound) {
    if (compound.hasKey("id", 8) && WRONG.equals(new ResourceLocation(compound.getString("id"))))
      compound.setString("id", "minecraft:cooked_fish"); 
    return compound;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraf\\util\datafix\fixes\CookedFishIDTypo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
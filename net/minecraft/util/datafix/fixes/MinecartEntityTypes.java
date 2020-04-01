package net.minecraft.util.datafix.fixes;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;

public class MinecartEntityTypes implements IFixableData {
  private static final List<String> MINECART_TYPE_LIST = Lists.newArrayList((Object[])new String[] { "MinecartRideable", "MinecartChest", "MinecartFurnace", "MinecartTNT", "MinecartSpawner", "MinecartHopper", "MinecartCommandBlock" });
  
  public int getFixVersion() {
    return 106;
  }
  
  public NBTTagCompound fixTagCompound(NBTTagCompound compound) {
    if ("Minecart".equals(compound.getString("id"))) {
      String s = "MinecartRideable";
      int i = compound.getInteger("Type");
      if (i > 0 && i < MINECART_TYPE_LIST.size())
        s = MINECART_TYPE_LIST.get(i); 
      compound.setString("id", s);
      compound.removeTag("Type");
    } 
    return compound;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraf\\util\datafix\fixes\MinecartEntityTypes.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
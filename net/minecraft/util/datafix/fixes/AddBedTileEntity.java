package net.minecraft.util.datafix.fixes;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.datafix.IFixableData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AddBedTileEntity implements IFixableData {
  private static final Logger field_193842_a = LogManager.getLogger();
  
  public int getFixVersion() {
    return 1125;
  }
  
  public NBTTagCompound fixTagCompound(NBTTagCompound compound) {
    int i = 416;
    try {
      NBTTagCompound nbttagcompound = compound.getCompoundTag("Level");
      int j = nbttagcompound.getInteger("xPos");
      int k = nbttagcompound.getInteger("zPos");
      NBTTagList nbttaglist = nbttagcompound.getTagList("TileEntities", 10);
      NBTTagList nbttaglist1 = nbttagcompound.getTagList("Sections", 10);
      for (int l = 0; l < nbttaglist1.tagCount(); l++) {
        NBTTagCompound nbttagcompound1 = nbttaglist1.getCompoundTagAt(l);
        int i1 = nbttagcompound1.getByte("Y");
        byte[] abyte = nbttagcompound1.getByteArray("Blocks");
        for (int j1 = 0; j1 < abyte.length; j1++) {
          if (416 == (abyte[j1] & 0xFF) << 4) {
            int k1 = j1 & 0xF;
            int l1 = j1 >> 8 & 0xF;
            int i2 = j1 >> 4 & 0xF;
            NBTTagCompound nbttagcompound2 = new NBTTagCompound();
            nbttagcompound2.setString("id", "bed");
            nbttagcompound2.setInteger("x", k1 + (j << 4));
            nbttagcompound2.setInteger("y", l1 + (i1 << 4));
            nbttagcompound2.setInteger("z", i2 + (k << 4));
            nbttaglist.appendTag((NBTBase)nbttagcompound2);
          } 
        } 
      } 
    } catch (Exception var17) {
      field_193842_a.warn("Unable to datafix Bed blocks, level format may be missing tags.");
    } 
    return compound;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraf\\util\datafix\fixes\AddBedTileEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
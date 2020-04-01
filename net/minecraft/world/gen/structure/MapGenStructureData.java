package net.minecraft.world.gen.structure;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.storage.WorldSavedData;

public class MapGenStructureData extends WorldSavedData {
  private NBTTagCompound tagCompound = new NBTTagCompound();
  
  public MapGenStructureData(String name) {
    super(name);
  }
  
  public void readFromNBT(NBTTagCompound nbt) {
    this.tagCompound = nbt.getCompoundTag("Features");
  }
  
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {
    compound.setTag("Features", (NBTBase)this.tagCompound);
    return compound;
  }
  
  public void writeInstance(NBTTagCompound tagCompoundIn, int chunkX, int chunkZ) {
    this.tagCompound.setTag(formatChunkCoords(chunkX, chunkZ), (NBTBase)tagCompoundIn);
  }
  
  public static String formatChunkCoords(int chunkX, int chunkZ) {
    return "[" + chunkX + "," + chunkZ + "]";
  }
  
  public NBTTagCompound getTagCompound() {
    return this.tagCompound;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\world\gen\structure\MapGenStructureData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
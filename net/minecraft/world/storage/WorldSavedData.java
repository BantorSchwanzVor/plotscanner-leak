package net.minecraft.world.storage;

import net.minecraft.nbt.NBTTagCompound;

public abstract class WorldSavedData {
  public final String mapName;
  
  private boolean dirty;
  
  public WorldSavedData(String name) {
    this.mapName = name;
  }
  
  public abstract void readFromNBT(NBTTagCompound paramNBTTagCompound);
  
  public abstract NBTTagCompound writeToNBT(NBTTagCompound paramNBTTagCompound);
  
  public void markDirty() {
    setDirty(true);
  }
  
  public void setDirty(boolean isDirty) {
    this.dirty = isDirty;
  }
  
  public boolean isDirty() {
    return this.dirty;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\world\storage\WorldSavedData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
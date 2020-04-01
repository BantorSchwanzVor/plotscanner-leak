package net.minecraft.util.datafix.walkers;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.IDataFixer;
import net.minecraft.util.datafix.IDataWalker;

public abstract class Filtered implements IDataWalker {
  private final ResourceLocation key;
  
  public Filtered(Class<?> p_i47309_1_) {
    if (Entity.class.isAssignableFrom(p_i47309_1_)) {
      this.key = EntityList.func_191306_a(p_i47309_1_);
    } else if (TileEntity.class.isAssignableFrom(p_i47309_1_)) {
      this.key = TileEntity.func_190559_a(p_i47309_1_);
    } else {
      this.key = null;
    } 
  }
  
  public NBTTagCompound process(IDataFixer fixer, NBTTagCompound compound, int versionIn) {
    if ((new ResourceLocation(compound.getString("id"))).equals(this.key))
      compound = filteredProcess(fixer, compound, versionIn); 
    return compound;
  }
  
  abstract NBTTagCompound filteredProcess(IDataFixer paramIDataFixer, NBTTagCompound paramNBTTagCompound, int paramInt);
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraf\\util\datafix\walkers\Filtered.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
package net.minecraft.util.math;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagList;

public class Rotations {
  protected final float x;
  
  protected final float y;
  
  protected final float z;
  
  public Rotations(float x, float y, float z) {
    this.x = (!Float.isInfinite(x) && !Float.isNaN(x)) ? (x % 360.0F) : 0.0F;
    this.y = (!Float.isInfinite(y) && !Float.isNaN(y)) ? (y % 360.0F) : 0.0F;
    this.z = (!Float.isInfinite(z) && !Float.isNaN(z)) ? (z % 360.0F) : 0.0F;
  }
  
  public Rotations(NBTTagList nbt) {
    this(nbt.getFloatAt(0), nbt.getFloatAt(1), nbt.getFloatAt(2));
  }
  
  public NBTTagList writeToNBT() {
    NBTTagList nbttaglist = new NBTTagList();
    nbttaglist.appendTag((NBTBase)new NBTTagFloat(this.x));
    nbttaglist.appendTag((NBTBase)new NBTTagFloat(this.y));
    nbttaglist.appendTag((NBTBase)new NBTTagFloat(this.z));
    return nbttaglist;
  }
  
  public boolean equals(Object p_equals_1_) {
    if (!(p_equals_1_ instanceof Rotations))
      return false; 
    Rotations rotations = (Rotations)p_equals_1_;
    return (this.x == rotations.x && this.y == rotations.y && this.z == rotations.z);
  }
  
  public float getX() {
    return this.x;
  }
  
  public float getY() {
    return this.y;
  }
  
  public float getZ() {
    return this.z;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraf\\util\math\Rotations.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
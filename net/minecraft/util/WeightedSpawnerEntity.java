package net.minecraft.util;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

public class WeightedSpawnerEntity extends WeightedRandom.Item {
  private final NBTTagCompound nbt;
  
  public WeightedSpawnerEntity() {
    super(1);
    this.nbt = new NBTTagCompound();
    this.nbt.setString("id", "minecraft:pig");
  }
  
  public WeightedSpawnerEntity(NBTTagCompound nbtIn) {
    this(nbtIn.hasKey("Weight", 99) ? nbtIn.getInteger("Weight") : 1, nbtIn.getCompoundTag("Entity"));
  }
  
  public WeightedSpawnerEntity(int itemWeightIn, NBTTagCompound nbtIn) {
    super(itemWeightIn);
    this.nbt = nbtIn;
  }
  
  public NBTTagCompound toCompoundTag() {
    NBTTagCompound nbttagcompound = new NBTTagCompound();
    if (!this.nbt.hasKey("id", 8)) {
      this.nbt.setString("id", "minecraft:pig");
    } else if (!this.nbt.getString("id").contains(":")) {
      this.nbt.setString("id", (new ResourceLocation(this.nbt.getString("id"))).toString());
    } 
    nbttagcompound.setTag("Entity", (NBTBase)this.nbt);
    nbttagcompound.setInteger("Weight", this.itemWeight);
    return nbttagcompound;
  }
  
  public NBTTagCompound getNbt() {
    return this.nbt;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraf\\util\WeightedSpawnerEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
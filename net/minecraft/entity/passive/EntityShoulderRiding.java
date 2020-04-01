package net.minecraft.entity.passive;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public abstract class EntityShoulderRiding extends EntityTameable {
  private int field_191996_bB;
  
  public EntityShoulderRiding(World p_i47410_1_) {
    super(p_i47410_1_);
  }
  
  public boolean func_191994_f(EntityPlayer p_191994_1_) {
    NBTTagCompound nbttagcompound = new NBTTagCompound();
    nbttagcompound.setString("id", getEntityString());
    writeToNBT(nbttagcompound);
    if (p_191994_1_.func_192027_g(nbttagcompound)) {
      this.world.removeEntity((Entity)this);
      return true;
    } 
    return false;
  }
  
  public void onUpdate() {
    this.field_191996_bB++;
    super.onUpdate();
  }
  
  public boolean func_191995_du() {
    return (this.field_191996_bB > 100);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\entity\passive\EntityShoulderRiding.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
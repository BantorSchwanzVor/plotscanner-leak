package net.minecraft.entity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;

public class MultiPartEntityPart extends Entity {
  public final IEntityMultiPart entityDragonObj;
  
  public final String partName;
  
  public MultiPartEntityPart(IEntityMultiPart parent, String partName, float base, float sizeHeight) {
    super(parent.getWorld());
    setSize(base, sizeHeight);
    this.entityDragonObj = parent;
    this.partName = partName;
  }
  
  protected void entityInit() {}
  
  protected void readEntityFromNBT(NBTTagCompound compound) {}
  
  protected void writeEntityToNBT(NBTTagCompound compound) {}
  
  public boolean canBeCollidedWith() {
    return true;
  }
  
  public boolean attackEntityFrom(DamageSource source, float amount) {
    return isEntityInvulnerable(source) ? false : this.entityDragonObj.attackEntityFromPart(this, source, amount);
  }
  
  public boolean isEntityEqual(Entity entityIn) {
    return !(this != entityIn && this.entityDragonObj != entityIn);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\entity\MultiPartEntityPart.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
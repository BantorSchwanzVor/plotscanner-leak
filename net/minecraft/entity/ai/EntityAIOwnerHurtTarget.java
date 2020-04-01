package net.minecraft.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityTameable;

public class EntityAIOwnerHurtTarget extends EntityAITarget {
  EntityTameable theEntityTameable;
  
  EntityLivingBase theTarget;
  
  private int timestamp;
  
  public EntityAIOwnerHurtTarget(EntityTameable theEntityTameableIn) {
    super((EntityCreature)theEntityTameableIn, false);
    this.theEntityTameable = theEntityTameableIn;
    setMutexBits(1);
  }
  
  public boolean shouldExecute() {
    if (!this.theEntityTameable.isTamed())
      return false; 
    EntityLivingBase entitylivingbase = this.theEntityTameable.getOwner();
    if (entitylivingbase == null)
      return false; 
    this.theTarget = entitylivingbase.getLastAttacker();
    int i = entitylivingbase.getLastAttackerTime();
    return (i != this.timestamp && isSuitableTarget(this.theTarget, false) && this.theEntityTameable.shouldAttackEntity(this.theTarget, entitylivingbase));
  }
  
  public void startExecuting() {
    this.taskOwner.setAttackTarget(this.theTarget);
    EntityLivingBase entitylivingbase = this.theEntityTameable.getOwner();
    if (entitylivingbase != null)
      this.timestamp = entitylivingbase.getLastAttackerTime(); 
    super.startExecuting();
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\entity\ai\EntityAIOwnerHurtTarget.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
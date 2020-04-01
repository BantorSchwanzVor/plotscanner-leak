package net.minecraft.entity.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.util.math.AxisAlignedBB;

public class EntityAIHurtByTarget extends EntityAITarget {
  private final boolean entityCallsForHelp;
  
  private int revengeTimerOld;
  
  private final Class<?>[] targetClasses;
  
  public EntityAIHurtByTarget(EntityCreature creatureIn, boolean entityCallsForHelpIn, Class... targetClassesIn) {
    super(creatureIn, true);
    this.entityCallsForHelp = entityCallsForHelpIn;
    this.targetClasses = targetClassesIn;
    setMutexBits(1);
  }
  
  public boolean shouldExecute() {
    int i = this.taskOwner.getRevengeTimer();
    EntityLivingBase entitylivingbase = this.taskOwner.getAITarget();
    return (i != this.revengeTimerOld && entitylivingbase != null && isSuitableTarget(entitylivingbase, false));
  }
  
  public void startExecuting() {
    this.taskOwner.setAttackTarget(this.taskOwner.getAITarget());
    this.target = this.taskOwner.getAttackTarget();
    this.revengeTimerOld = this.taskOwner.getRevengeTimer();
    this.unseenMemoryTicks = 300;
    if (this.entityCallsForHelp)
      alertOthers(); 
    super.startExecuting();
  }
  
  protected void alertOthers() {
    double d0 = getTargetDistance();
    for (EntityCreature entitycreature : this.taskOwner.world.getEntitiesWithinAABB(this.taskOwner.getClass(), (new AxisAlignedBB(this.taskOwner.posX, this.taskOwner.posY, this.taskOwner.posZ, this.taskOwner.posX + 1.0D, this.taskOwner.posY + 1.0D, this.taskOwner.posZ + 1.0D)).expand(d0, 10.0D, d0))) {
      if (this.taskOwner != entitycreature && entitycreature.getAttackTarget() == null && (!(this.taskOwner instanceof EntityTameable) || ((EntityTameable)this.taskOwner).getOwner() == ((EntityTameable)entitycreature).getOwner()) && !entitycreature.isOnSameTeam((Entity)this.taskOwner.getAITarget())) {
        boolean flag = false;
        byte b;
        int i;
        Class<?>[] arrayOfClass;
        for (i = (arrayOfClass = this.targetClasses).length, b = 0; b < i; ) {
          Class<?> oclass = arrayOfClass[b];
          if (entitycreature.getClass() == oclass) {
            flag = true;
            break;
          } 
          b++;
        } 
        if (!flag)
          setEntityAttackTarget(entitycreature, this.taskOwner.getAITarget()); 
      } 
    } 
  }
  
  protected void setEntityAttackTarget(EntityCreature creatureIn, EntityLivingBase entityLivingBaseIn) {
    creatureIn.setAttackTarget(entityLivingBaseIn);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\entity\ai\EntityAIHurtByTarget.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
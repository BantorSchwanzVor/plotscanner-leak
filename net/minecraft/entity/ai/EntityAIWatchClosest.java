package net.minecraft.entity.ai;

import com.google.common.base.Predicates;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EntitySelectors;

public class EntityAIWatchClosest extends EntityAIBase {
  protected EntityLiving theWatcher;
  
  protected Entity closestEntity;
  
  protected float maxDistanceForPlayer;
  
  private int lookTime;
  
  private final float chance;
  
  protected Class<? extends Entity> watchedClass;
  
  public EntityAIWatchClosest(EntityLiving entitylivingIn, Class<? extends Entity> watchTargetClass, float maxDistance) {
    this.theWatcher = entitylivingIn;
    this.watchedClass = watchTargetClass;
    this.maxDistanceForPlayer = maxDistance;
    this.chance = 0.02F;
    setMutexBits(2);
  }
  
  public EntityAIWatchClosest(EntityLiving entitylivingIn, Class<? extends Entity> watchTargetClass, float maxDistance, float chanceIn) {
    this.theWatcher = entitylivingIn;
    this.watchedClass = watchTargetClass;
    this.maxDistanceForPlayer = maxDistance;
    this.chance = chanceIn;
    setMutexBits(2);
  }
  
  public boolean shouldExecute() {
    if (this.theWatcher.getRNG().nextFloat() >= this.chance)
      return false; 
    if (this.theWatcher.getAttackTarget() != null)
      this.closestEntity = (Entity)this.theWatcher.getAttackTarget(); 
    if (this.watchedClass == EntityPlayer.class) {
      this.closestEntity = (Entity)this.theWatcher.world.func_190525_a(this.theWatcher.posX, this.theWatcher.posY, this.theWatcher.posZ, this.maxDistanceForPlayer, Predicates.and(EntitySelectors.NOT_SPECTATING, EntitySelectors.func_191324_b((Entity)this.theWatcher)));
    } else {
      this.closestEntity = this.theWatcher.world.findNearestEntityWithinAABB(this.watchedClass, this.theWatcher.getEntityBoundingBox().expand(this.maxDistanceForPlayer, 3.0D, this.maxDistanceForPlayer), (Entity)this.theWatcher);
    } 
    return (this.closestEntity != null);
  }
  
  public boolean continueExecuting() {
    if (!this.closestEntity.isEntityAlive())
      return false; 
    if (this.theWatcher.getDistanceSqToEntity(this.closestEntity) > (this.maxDistanceForPlayer * this.maxDistanceForPlayer))
      return false; 
    return (this.lookTime > 0);
  }
  
  public void startExecuting() {
    this.lookTime = 40 + this.theWatcher.getRNG().nextInt(40);
  }
  
  public void resetTask() {
    this.closestEntity = null;
  }
  
  public void updateTask() {
    this.theWatcher.getLookHelper().setLookPosition(this.closestEntity.posX, this.closestEntity.posY + this.closestEntity.getEyeHeight(), this.closestEntity.posZ, this.theWatcher.getHorizontalFaceSpeed(), this.theWatcher.getVerticalFaceSpeed());
    this.lookTime--;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\entity\ai\EntityAIWatchClosest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
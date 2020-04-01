package net.minecraft.entity.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.pathfinding.PathNavigateFlying;
import net.minecraft.pathfinding.PathNavigateGround;

public class EntityAISwimming extends EntityAIBase {
  private final EntityLiving theEntity;
  
  public EntityAISwimming(EntityLiving entitylivingIn) {
    this.theEntity = entitylivingIn;
    setMutexBits(4);
    if (entitylivingIn.getNavigator() instanceof PathNavigateGround) {
      ((PathNavigateGround)entitylivingIn.getNavigator()).setCanSwim(true);
    } else if (entitylivingIn.getNavigator() instanceof PathNavigateFlying) {
      ((PathNavigateFlying)entitylivingIn.getNavigator()).func_192877_c(true);
    } 
  }
  
  public boolean shouldExecute() {
    return !(!this.theEntity.isInWater() && !this.theEntity.isInLava());
  }
  
  public void updateTask() {
    if (this.theEntity.getRNG().nextFloat() < 0.8F)
      this.theEntity.getJumpHelper().setJumping(); 
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\entity\ai\EntityAISwimming.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
package net.minecraft.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class EntityAIMoveTowardsRestriction extends EntityAIBase {
  private final EntityCreature theEntity;
  
  private double movePosX;
  
  private double movePosY;
  
  private double movePosZ;
  
  private final double movementSpeed;
  
  public EntityAIMoveTowardsRestriction(EntityCreature creatureIn, double speedIn) {
    this.theEntity = creatureIn;
    this.movementSpeed = speedIn;
    setMutexBits(1);
  }
  
  public boolean shouldExecute() {
    if (this.theEntity.isWithinHomeDistanceCurrentPosition())
      return false; 
    BlockPos blockpos = this.theEntity.getHomePosition();
    Vec3d vec3d = RandomPositionGenerator.findRandomTargetBlockTowards(this.theEntity, 16, 7, new Vec3d(blockpos.getX(), blockpos.getY(), blockpos.getZ()));
    if (vec3d == null)
      return false; 
    this.movePosX = vec3d.xCoord;
    this.movePosY = vec3d.yCoord;
    this.movePosZ = vec3d.zCoord;
    return true;
  }
  
  public boolean continueExecuting() {
    return !this.theEntity.getNavigator().noPath();
  }
  
  public void startExecuting() {
    this.theEntity.getNavigator().tryMoveToXYZ(this.movePosX, this.movePosY, this.movePosZ, this.movementSpeed);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\entity\ai\EntityAIMoveTowardsRestriction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
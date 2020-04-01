package net.minecraft.pathfinding;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class PathNavigateClimber extends PathNavigateGround {
  private BlockPos targetPosition;
  
  public PathNavigateClimber(EntityLiving entityLivingIn, World worldIn) {
    super(entityLivingIn, worldIn);
  }
  
  public Path getPathToPos(BlockPos pos) {
    this.targetPosition = pos;
    return super.getPathToPos(pos);
  }
  
  public Path getPathToEntityLiving(Entity entityIn) {
    this.targetPosition = new BlockPos(entityIn);
    return super.getPathToEntityLiving(entityIn);
  }
  
  public boolean tryMoveToEntityLiving(Entity entityIn, double speedIn) {
    Path path = getPathToEntityLiving(entityIn);
    if (path != null)
      return setPath(path, speedIn); 
    this.targetPosition = new BlockPos(entityIn);
    this.speed = speedIn;
    return true;
  }
  
  public void onUpdateNavigation() {
    if (!noPath()) {
      super.onUpdateNavigation();
    } else if (this.targetPosition != null) {
      double d0 = (this.theEntity.width * this.theEntity.width);
      if (this.theEntity.getDistanceSqToCenter(this.targetPosition) >= d0 && (this.theEntity.posY <= this.targetPosition.getY() || this.theEntity.getDistanceSqToCenter(new BlockPos(this.targetPosition.getX(), MathHelper.floor(this.theEntity.posY), this.targetPosition.getZ())) >= d0)) {
        this.theEntity.getMoveHelper().setMoveTo(this.targetPosition.getX(), this.targetPosition.getY(), this.targetPosition.getZ(), this.speed);
      } else {
        this.targetPosition = null;
      } 
    } 
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\pathfinding\PathNavigateClimber.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
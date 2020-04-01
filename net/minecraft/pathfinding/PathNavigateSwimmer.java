package net.minecraft.pathfinding;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class PathNavigateSwimmer extends PathNavigate {
  public PathNavigateSwimmer(EntityLiving entitylivingIn, World worldIn) {
    super(entitylivingIn, worldIn);
  }
  
  protected PathFinder getPathFinder() {
    return new PathFinder(new SwimNodeProcessor());
  }
  
  protected boolean canNavigate() {
    return isInLiquid();
  }
  
  protected Vec3d getEntityPosition() {
    return new Vec3d(this.theEntity.posX, this.theEntity.posY + this.theEntity.height * 0.5D, this.theEntity.posZ);
  }
  
  protected void pathFollow() {
    Vec3d vec3d = getEntityPosition();
    float f = this.theEntity.width * this.theEntity.width;
    int i = 6;
    if (vec3d.squareDistanceTo(this.currentPath.getVectorFromIndex((Entity)this.theEntity, this.currentPath.getCurrentPathIndex())) < f)
      this.currentPath.incrementPathIndex(); 
    for (int j = Math.min(this.currentPath.getCurrentPathIndex() + 6, this.currentPath.getCurrentPathLength() - 1); j > this.currentPath.getCurrentPathIndex(); j--) {
      Vec3d vec3d1 = this.currentPath.getVectorFromIndex((Entity)this.theEntity, j);
      if (vec3d1.squareDistanceTo(vec3d) <= 36.0D && isDirectPathBetweenPoints(vec3d, vec3d1, 0, 0, 0)) {
        this.currentPath.setCurrentPathIndex(j);
        break;
      } 
    } 
    checkForStuck(vec3d);
  }
  
  protected boolean isDirectPathBetweenPoints(Vec3d posVec31, Vec3d posVec32, int sizeX, int sizeY, int sizeZ) {
    RayTraceResult raytraceresult = this.worldObj.rayTraceBlocks(posVec31, new Vec3d(posVec32.xCoord, posVec32.yCoord + this.theEntity.height * 0.5D, posVec32.zCoord), false, true, false);
    return !(raytraceresult != null && raytraceresult.typeOfHit != RayTraceResult.Type.MISS);
  }
  
  public boolean canEntityStandOnPos(BlockPos pos) {
    return !this.worldObj.getBlockState(pos).isFullBlock();
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\pathfinding\PathNavigateSwimmer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
package net.minecraft.pathfinding;

import javax.annotation.Nullable;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;

public class SwimNodeProcessor extends NodeProcessor {
  public PathPoint getStart() {
    return openPoint(MathHelper.floor((this.entity.getEntityBoundingBox()).minX), MathHelper.floor((this.entity.getEntityBoundingBox()).minY + 0.5D), MathHelper.floor((this.entity.getEntityBoundingBox()).minZ));
  }
  
  public PathPoint getPathPointToCoords(double x, double y, double z) {
    return openPoint(MathHelper.floor(x - (this.entity.width / 2.0F)), MathHelper.floor(y + 0.5D), MathHelper.floor(z - (this.entity.width / 2.0F)));
  }
  
  public int findPathOptions(PathPoint[] pathOptions, PathPoint currentPoint, PathPoint targetPoint, float maxDistance) {
    int i = 0;
    byte b;
    int j;
    EnumFacing[] arrayOfEnumFacing;
    for (j = (arrayOfEnumFacing = EnumFacing.values()).length, b = 0; b < j; ) {
      EnumFacing enumfacing = arrayOfEnumFacing[b];
      PathPoint pathpoint = getWaterNode(currentPoint.xCoord + enumfacing.getFrontOffsetX(), currentPoint.yCoord + enumfacing.getFrontOffsetY(), currentPoint.zCoord + enumfacing.getFrontOffsetZ());
      if (pathpoint != null && !pathpoint.visited && pathpoint.distanceTo(targetPoint) < maxDistance)
        pathOptions[i++] = pathpoint; 
      b++;
    } 
    return i;
  }
  
  public PathNodeType getPathNodeType(IBlockAccess blockaccessIn, int x, int y, int z, EntityLiving entitylivingIn, int xSize, int ySize, int zSize, boolean canBreakDoorsIn, boolean canEnterDoorsIn) {
    return PathNodeType.WATER;
  }
  
  public PathNodeType getPathNodeType(IBlockAccess blockaccessIn, int x, int y, int z) {
    return PathNodeType.WATER;
  }
  
  @Nullable
  private PathPoint getWaterNode(int p_186328_1_, int p_186328_2_, int p_186328_3_) {
    PathNodeType pathnodetype = isFree(p_186328_1_, p_186328_2_, p_186328_3_);
    return (pathnodetype == PathNodeType.WATER) ? openPoint(p_186328_1_, p_186328_2_, p_186328_3_) : null;
  }
  
  private PathNodeType isFree(int p_186327_1_, int p_186327_2_, int p_186327_3_) {
    BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
    for (int i = p_186327_1_; i < p_186327_1_ + this.entitySizeX; i++) {
      for (int j = p_186327_2_; j < p_186327_2_ + this.entitySizeY; j++) {
        for (int k = p_186327_3_; k < p_186327_3_ + this.entitySizeZ; k++) {
          IBlockState iblockstate = this.blockaccess.getBlockState((BlockPos)blockpos$mutableblockpos.setPos(i, j, k));
          if (iblockstate.getMaterial() != Material.WATER)
            return PathNodeType.BLOCKED; 
        } 
      } 
    } 
    return PathNodeType.WATER;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\pathfinding\SwimNodeProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
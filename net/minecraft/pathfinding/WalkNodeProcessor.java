package net.minecraft.pathfinding;

import com.google.common.collect.Sets;
import java.util.EnumSet;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;

public class WalkNodeProcessor extends NodeProcessor {
  protected float avoidsWater;
  
  public void initProcessor(IBlockAccess sourceIn, EntityLiving mob) {
    super.initProcessor(sourceIn, mob);
    this.avoidsWater = mob.getPathPriority(PathNodeType.WATER);
  }
  
  public void postProcess() {
    this.entity.setPathPriority(PathNodeType.WATER, this.avoidsWater);
    super.postProcess();
  }
  
  public PathPoint getStart() {
    int i;
    if (getCanSwim() && this.entity.isInWater()) {
      i = (int)(this.entity.getEntityBoundingBox()).minY;
      BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos(MathHelper.floor(this.entity.posX), i, MathHelper.floor(this.entity.posZ));
      for (Block block = this.blockaccess.getBlockState((BlockPos)blockpos$mutableblockpos).getBlock(); block == Blocks.FLOWING_WATER || block == Blocks.WATER; block = this.blockaccess.getBlockState((BlockPos)blockpos$mutableblockpos).getBlock()) {
        i++;
        blockpos$mutableblockpos.setPos(MathHelper.floor(this.entity.posX), i, MathHelper.floor(this.entity.posZ));
      } 
    } else if (this.entity.onGround) {
      i = MathHelper.floor((this.entity.getEntityBoundingBox()).minY + 0.5D);
    } else {
      for (BlockPos blockpos = new BlockPos((Entity)this.entity); (this.blockaccess.getBlockState(blockpos).getMaterial() == Material.AIR || this.blockaccess.getBlockState(blockpos).getBlock().isPassable(this.blockaccess, blockpos)) && blockpos.getY() > 0; blockpos = blockpos.down());
      i = blockpos.up().getY();
    } 
    BlockPos blockpos2 = new BlockPos((Entity)this.entity);
    PathNodeType pathnodetype1 = getPathNodeType(this.entity, blockpos2.getX(), i, blockpos2.getZ());
    if (this.entity.getPathPriority(pathnodetype1) < 0.0F) {
      Set<BlockPos> set = Sets.newHashSet();
      set.add(new BlockPos((this.entity.getEntityBoundingBox()).minX, i, (this.entity.getEntityBoundingBox()).minZ));
      set.add(new BlockPos((this.entity.getEntityBoundingBox()).minX, i, (this.entity.getEntityBoundingBox()).maxZ));
      set.add(new BlockPos((this.entity.getEntityBoundingBox()).maxX, i, (this.entity.getEntityBoundingBox()).minZ));
      set.add(new BlockPos((this.entity.getEntityBoundingBox()).maxX, i, (this.entity.getEntityBoundingBox()).maxZ));
      for (BlockPos blockpos1 : set) {
        PathNodeType pathnodetype = getPathNodeType(this.entity, blockpos1);
        if (this.entity.getPathPriority(pathnodetype) >= 0.0F)
          return openPoint(blockpos1.getX(), blockpos1.getY(), blockpos1.getZ()); 
      } 
    } 
    return openPoint(blockpos2.getX(), i, blockpos2.getZ());
  }
  
  public PathPoint getPathPointToCoords(double x, double y, double z) {
    return openPoint(MathHelper.floor(x), MathHelper.floor(y), MathHelper.floor(z));
  }
  
  public int findPathOptions(PathPoint[] pathOptions, PathPoint currentPoint, PathPoint targetPoint, float maxDistance) {
    int i = 0;
    int j = 0;
    PathNodeType pathnodetype = getPathNodeType(this.entity, currentPoint.xCoord, currentPoint.yCoord + 1, currentPoint.zCoord);
    if (this.entity.getPathPriority(pathnodetype) >= 0.0F)
      j = MathHelper.floor(Math.max(1.0F, this.entity.stepHeight)); 
    BlockPos blockpos = (new BlockPos(currentPoint.xCoord, currentPoint.yCoord, currentPoint.zCoord)).down();
    double d0 = currentPoint.yCoord - 1.0D - (this.blockaccess.getBlockState(blockpos).getBoundingBox(this.blockaccess, blockpos)).maxY;
    PathPoint pathpoint = getSafePoint(currentPoint.xCoord, currentPoint.yCoord, currentPoint.zCoord + 1, j, d0, EnumFacing.SOUTH);
    PathPoint pathpoint1 = getSafePoint(currentPoint.xCoord - 1, currentPoint.yCoord, currentPoint.zCoord, j, d0, EnumFacing.WEST);
    PathPoint pathpoint2 = getSafePoint(currentPoint.xCoord + 1, currentPoint.yCoord, currentPoint.zCoord, j, d0, EnumFacing.EAST);
    PathPoint pathpoint3 = getSafePoint(currentPoint.xCoord, currentPoint.yCoord, currentPoint.zCoord - 1, j, d0, EnumFacing.NORTH);
    if (pathpoint != null && !pathpoint.visited && pathpoint.distanceTo(targetPoint) < maxDistance)
      pathOptions[i++] = pathpoint; 
    if (pathpoint1 != null && !pathpoint1.visited && pathpoint1.distanceTo(targetPoint) < maxDistance)
      pathOptions[i++] = pathpoint1; 
    if (pathpoint2 != null && !pathpoint2.visited && pathpoint2.distanceTo(targetPoint) < maxDistance)
      pathOptions[i++] = pathpoint2; 
    if (pathpoint3 != null && !pathpoint3.visited && pathpoint3.distanceTo(targetPoint) < maxDistance)
      pathOptions[i++] = pathpoint3; 
    boolean flag = !(pathpoint3 != null && pathpoint3.nodeType != PathNodeType.OPEN && pathpoint3.costMalus == 0.0F);
    boolean flag1 = !(pathpoint != null && pathpoint.nodeType != PathNodeType.OPEN && pathpoint.costMalus == 0.0F);
    boolean flag2 = !(pathpoint2 != null && pathpoint2.nodeType != PathNodeType.OPEN && pathpoint2.costMalus == 0.0F);
    boolean flag3 = !(pathpoint1 != null && pathpoint1.nodeType != PathNodeType.OPEN && pathpoint1.costMalus == 0.0F);
    if (flag && flag3) {
      PathPoint pathpoint4 = getSafePoint(currentPoint.xCoord - 1, currentPoint.yCoord, currentPoint.zCoord - 1, j, d0, EnumFacing.NORTH);
      if (pathpoint4 != null && !pathpoint4.visited && pathpoint4.distanceTo(targetPoint) < maxDistance)
        pathOptions[i++] = pathpoint4; 
    } 
    if (flag && flag2) {
      PathPoint pathpoint5 = getSafePoint(currentPoint.xCoord + 1, currentPoint.yCoord, currentPoint.zCoord - 1, j, d0, EnumFacing.NORTH);
      if (pathpoint5 != null && !pathpoint5.visited && pathpoint5.distanceTo(targetPoint) < maxDistance)
        pathOptions[i++] = pathpoint5; 
    } 
    if (flag1 && flag3) {
      PathPoint pathpoint6 = getSafePoint(currentPoint.xCoord - 1, currentPoint.yCoord, currentPoint.zCoord + 1, j, d0, EnumFacing.SOUTH);
      if (pathpoint6 != null && !pathpoint6.visited && pathpoint6.distanceTo(targetPoint) < maxDistance)
        pathOptions[i++] = pathpoint6; 
    } 
    if (flag1 && flag2) {
      PathPoint pathpoint7 = getSafePoint(currentPoint.xCoord + 1, currentPoint.yCoord, currentPoint.zCoord + 1, j, d0, EnumFacing.SOUTH);
      if (pathpoint7 != null && !pathpoint7.visited && pathpoint7.distanceTo(targetPoint) < maxDistance)
        pathOptions[i++] = pathpoint7; 
    } 
    return i;
  }
  
  @Nullable
  private PathPoint getSafePoint(int x, int y, int z, int p_186332_4_, double p_186332_5_, EnumFacing facing) {
    PathPoint pathpoint = null;
    BlockPos blockpos = new BlockPos(x, y, z);
    BlockPos blockpos1 = blockpos.down();
    double d0 = y - 1.0D - (this.blockaccess.getBlockState(blockpos1).getBoundingBox(this.blockaccess, blockpos1)).maxY;
    if (d0 - p_186332_5_ > 1.125D)
      return null; 
    PathNodeType pathnodetype = getPathNodeType(this.entity, x, y, z);
    float f = this.entity.getPathPriority(pathnodetype);
    double d1 = this.entity.width / 2.0D;
    if (f >= 0.0F) {
      pathpoint = openPoint(x, y, z);
      pathpoint.nodeType = pathnodetype;
      pathpoint.costMalus = Math.max(pathpoint.costMalus, f);
    } 
    if (pathnodetype == PathNodeType.WALKABLE)
      return pathpoint; 
    if (pathpoint == null && p_186332_4_ > 0 && pathnodetype != PathNodeType.FENCE && pathnodetype != PathNodeType.TRAPDOOR) {
      pathpoint = getSafePoint(x, y + 1, z, p_186332_4_ - 1, p_186332_5_, facing);
      if (pathpoint != null && (pathpoint.nodeType == PathNodeType.OPEN || pathpoint.nodeType == PathNodeType.WALKABLE) && this.entity.width < 1.0F) {
        double d2 = (x - facing.getFrontOffsetX()) + 0.5D;
        double d3 = (z - facing.getFrontOffsetZ()) + 0.5D;
        AxisAlignedBB axisalignedbb = new AxisAlignedBB(d2 - d1, y + 0.001D, d3 - d1, d2 + d1, (y + this.entity.height), d3 + d1);
        AxisAlignedBB axisalignedbb1 = this.blockaccess.getBlockState(blockpos).getBoundingBox(this.blockaccess, blockpos);
        AxisAlignedBB axisalignedbb2 = axisalignedbb.addCoord(0.0D, axisalignedbb1.maxY - 0.002D, 0.0D);
        if (this.entity.world.collidesWithAnyBlock(axisalignedbb2))
          pathpoint = null; 
      } 
    } 
    if (pathnodetype == PathNodeType.OPEN) {
      AxisAlignedBB axisalignedbb3 = new AxisAlignedBB(x - d1 + 0.5D, y + 0.001D, z - d1 + 0.5D, x + d1 + 0.5D, (y + this.entity.height), z + d1 + 0.5D);
      if (this.entity.world.collidesWithAnyBlock(axisalignedbb3))
        return null; 
      if (this.entity.width >= 1.0F) {
        PathNodeType pathnodetype1 = getPathNodeType(this.entity, x, y - 1, z);
        if (pathnodetype1 == PathNodeType.BLOCKED) {
          pathpoint = openPoint(x, y, z);
          pathpoint.nodeType = PathNodeType.WALKABLE;
          pathpoint.costMalus = Math.max(pathpoint.costMalus, f);
          return pathpoint;
        } 
      } 
      int i = 0;
      while (y > 0 && pathnodetype == PathNodeType.OPEN) {
        y--;
        if (i++ >= this.entity.getMaxFallHeight())
          return null; 
        pathnodetype = getPathNodeType(this.entity, x, y, z);
        f = this.entity.getPathPriority(pathnodetype);
        if (pathnodetype != PathNodeType.OPEN && f >= 0.0F) {
          pathpoint = openPoint(x, y, z);
          pathpoint.nodeType = pathnodetype;
          pathpoint.costMalus = Math.max(pathpoint.costMalus, f);
          break;
        } 
        if (f < 0.0F)
          return null; 
      } 
    } 
    return pathpoint;
  }
  
  public PathNodeType getPathNodeType(IBlockAccess blockaccessIn, int x, int y, int z, EntityLiving entitylivingIn, int xSize, int ySize, int zSize, boolean canBreakDoorsIn, boolean canEnterDoorsIn) {
    EnumSet<PathNodeType> enumset = EnumSet.noneOf(PathNodeType.class);
    PathNodeType pathnodetype = PathNodeType.BLOCKED;
    double d0 = entitylivingIn.width / 2.0D;
    BlockPos blockpos = new BlockPos((Entity)entitylivingIn);
    pathnodetype = func_193577_a(blockaccessIn, x, y, z, xSize, ySize, zSize, canBreakDoorsIn, canEnterDoorsIn, enumset, pathnodetype, blockpos);
    if (enumset.contains(PathNodeType.FENCE))
      return PathNodeType.FENCE; 
    PathNodeType pathnodetype1 = PathNodeType.BLOCKED;
    for (PathNodeType pathnodetype2 : enumset) {
      if (entitylivingIn.getPathPriority(pathnodetype2) < 0.0F)
        return pathnodetype2; 
      if (entitylivingIn.getPathPriority(pathnodetype2) >= entitylivingIn.getPathPriority(pathnodetype1))
        pathnodetype1 = pathnodetype2; 
    } 
    if (pathnodetype == PathNodeType.OPEN && entitylivingIn.getPathPriority(pathnodetype1) == 0.0F)
      return PathNodeType.OPEN; 
    return pathnodetype1;
  }
  
  public PathNodeType func_193577_a(IBlockAccess p_193577_1_, int p_193577_2_, int p_193577_3_, int p_193577_4_, int p_193577_5_, int p_193577_6_, int p_193577_7_, boolean p_193577_8_, boolean p_193577_9_, EnumSet<PathNodeType> p_193577_10_, PathNodeType p_193577_11_, BlockPos p_193577_12_) {
    for (int i = 0; i < p_193577_5_; i++) {
      for (int j = 0; j < p_193577_6_; j++) {
        for (int k = 0; k < p_193577_7_; k++) {
          int l = i + p_193577_2_;
          int i1 = j + p_193577_3_;
          int j1 = k + p_193577_4_;
          PathNodeType pathnodetype = getPathNodeType(p_193577_1_, l, i1, j1);
          if (pathnodetype == PathNodeType.DOOR_WOOD_CLOSED && p_193577_8_ && p_193577_9_)
            pathnodetype = PathNodeType.WALKABLE; 
          if (pathnodetype == PathNodeType.DOOR_OPEN && !p_193577_9_)
            pathnodetype = PathNodeType.BLOCKED; 
          if (pathnodetype == PathNodeType.RAIL && !(p_193577_1_.getBlockState(p_193577_12_).getBlock() instanceof net.minecraft.block.BlockRailBase) && !(p_193577_1_.getBlockState(p_193577_12_.down()).getBlock() instanceof net.minecraft.block.BlockRailBase))
            pathnodetype = PathNodeType.FENCE; 
          if (i == 0 && j == 0 && k == 0)
            p_193577_11_ = pathnodetype; 
          p_193577_10_.add(pathnodetype);
        } 
      } 
    } 
    return p_193577_11_;
  }
  
  private PathNodeType getPathNodeType(EntityLiving entitylivingIn, BlockPos pos) {
    return getPathNodeType(entitylivingIn, pos.getX(), pos.getY(), pos.getZ());
  }
  
  private PathNodeType getPathNodeType(EntityLiving entitylivingIn, int x, int y, int z) {
    return getPathNodeType(this.blockaccess, x, y, z, entitylivingIn, this.entitySizeX, this.entitySizeY, this.entitySizeZ, getCanBreakDoors(), getCanEnterDoors());
  }
  
  public PathNodeType getPathNodeType(IBlockAccess blockaccessIn, int x, int y, int z) {
    PathNodeType pathnodetype = getPathNodeTypeRaw(blockaccessIn, x, y, z);
    if (pathnodetype == PathNodeType.OPEN && y >= 1) {
      Block block = blockaccessIn.getBlockState(new BlockPos(x, y - 1, z)).getBlock();
      PathNodeType pathnodetype1 = getPathNodeTypeRaw(blockaccessIn, x, y - 1, z);
      pathnodetype = (pathnodetype1 != PathNodeType.WALKABLE && pathnodetype1 != PathNodeType.OPEN && pathnodetype1 != PathNodeType.WATER && pathnodetype1 != PathNodeType.LAVA) ? PathNodeType.WALKABLE : PathNodeType.OPEN;
      if (pathnodetype1 == PathNodeType.DAMAGE_FIRE || block == Blocks.MAGMA)
        pathnodetype = PathNodeType.DAMAGE_FIRE; 
      if (pathnodetype1 == PathNodeType.DAMAGE_CACTUS)
        pathnodetype = PathNodeType.DAMAGE_CACTUS; 
    } 
    pathnodetype = func_193578_a(blockaccessIn, x, y, z, pathnodetype);
    return pathnodetype;
  }
  
  public PathNodeType func_193578_a(IBlockAccess p_193578_1_, int p_193578_2_, int p_193578_3_, int p_193578_4_, PathNodeType p_193578_5_) {
    BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain();
    if (p_193578_5_ == PathNodeType.WALKABLE)
      for (int i = -1; i <= 1; i++) {
        for (int j = -1; j <= 1; j++) {
          if (i != 0 || j != 0) {
            Block block = p_193578_1_.getBlockState((BlockPos)blockpos$pooledmutableblockpos.setPos(i + p_193578_2_, p_193578_3_, j + p_193578_4_)).getBlock();
            if (block == Blocks.CACTUS) {
              p_193578_5_ = PathNodeType.DANGER_CACTUS;
            } else if (block == Blocks.FIRE) {
              p_193578_5_ = PathNodeType.DANGER_FIRE;
            } 
          } 
        } 
      }  
    blockpos$pooledmutableblockpos.release();
    return p_193578_5_;
  }
  
  protected PathNodeType getPathNodeTypeRaw(IBlockAccess p_189553_1_, int p_189553_2_, int p_189553_3_, int p_189553_4_) {
    BlockPos blockpos = new BlockPos(p_189553_2_, p_189553_3_, p_189553_4_);
    IBlockState iblockstate = p_189553_1_.getBlockState(blockpos);
    Block block = iblockstate.getBlock();
    Material material = iblockstate.getMaterial();
    if (material == Material.AIR)
      return PathNodeType.OPEN; 
    if (block != Blocks.TRAPDOOR && block != Blocks.IRON_TRAPDOOR && block != Blocks.WATERLILY) {
      if (block == Blocks.FIRE)
        return PathNodeType.DAMAGE_FIRE; 
      if (block == Blocks.CACTUS)
        return PathNodeType.DAMAGE_CACTUS; 
      if (block instanceof BlockDoor && material == Material.WOOD && !((Boolean)iblockstate.getValue((IProperty)BlockDoor.OPEN)).booleanValue())
        return PathNodeType.DOOR_WOOD_CLOSED; 
      if (block instanceof BlockDoor && material == Material.IRON && !((Boolean)iblockstate.getValue((IProperty)BlockDoor.OPEN)).booleanValue())
        return PathNodeType.DOOR_IRON_CLOSED; 
      if (block instanceof BlockDoor && ((Boolean)iblockstate.getValue((IProperty)BlockDoor.OPEN)).booleanValue())
        return PathNodeType.DOOR_OPEN; 
      if (block instanceof net.minecraft.block.BlockRailBase)
        return PathNodeType.RAIL; 
      if (!(block instanceof net.minecraft.block.BlockFence) && !(block instanceof net.minecraft.block.BlockWall) && (!(block instanceof BlockFenceGate) || ((Boolean)iblockstate.getValue((IProperty)BlockFenceGate.OPEN)).booleanValue())) {
        if (material == Material.WATER)
          return PathNodeType.WATER; 
        if (material == Material.LAVA)
          return PathNodeType.LAVA; 
        return block.isPassable(p_189553_1_, blockpos) ? PathNodeType.OPEN : PathNodeType.BLOCKED;
      } 
      return PathNodeType.FENCE;
    } 
    return PathNodeType.TRAPDOOR;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\pathfinding\WalkNodeProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
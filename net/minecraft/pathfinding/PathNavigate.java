package net.minecraft.pathfinding;

import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class PathNavigate {
  protected EntityLiving theEntity;
  
  protected World worldObj;
  
  @Nullable
  protected Path currentPath;
  
  protected double speed;
  
  private final IAttributeInstance pathSearchRange;
  
  protected int totalTicks;
  
  private int ticksAtLastPos;
  
  private Vec3d lastPosCheck = Vec3d.ZERO;
  
  private Vec3d timeoutCachedNode = Vec3d.ZERO;
  
  private long timeoutTimer;
  
  private long lastTimeoutCheck;
  
  private double timeoutLimit;
  
  protected float maxDistanceToWaypoint = 0.5F;
  
  protected boolean tryUpdatePath;
  
  private long lastTimeUpdated;
  
  protected NodeProcessor nodeProcessor;
  
  private BlockPos targetPos;
  
  private final PathFinder pathFinder;
  
  public PathNavigate(EntityLiving entitylivingIn, World worldIn) {
    this.theEntity = entitylivingIn;
    this.worldObj = worldIn;
    this.pathSearchRange = entitylivingIn.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE);
    this.pathFinder = getPathFinder();
  }
  
  protected abstract PathFinder getPathFinder();
  
  public void setSpeed(double speedIn) {
    this.speed = speedIn;
  }
  
  public float getPathSearchRange() {
    return (float)this.pathSearchRange.getAttributeValue();
  }
  
  public boolean canUpdatePathOnTimeout() {
    return this.tryUpdatePath;
  }
  
  public void updatePath() {
    if (this.worldObj.getTotalWorldTime() - this.lastTimeUpdated > 20L) {
      if (this.targetPos != null) {
        this.currentPath = null;
        this.currentPath = getPathToPos(this.targetPos);
        this.lastTimeUpdated = this.worldObj.getTotalWorldTime();
        this.tryUpdatePath = false;
      } 
    } else {
      this.tryUpdatePath = true;
    } 
  }
  
  @Nullable
  public final Path getPathToXYZ(double x, double y, double z) {
    return getPathToPos(new BlockPos(x, y, z));
  }
  
  @Nullable
  public Path getPathToPos(BlockPos pos) {
    if (!canNavigate())
      return null; 
    if (this.currentPath != null && !this.currentPath.isFinished() && pos.equals(this.targetPos))
      return this.currentPath; 
    this.targetPos = pos;
    float f = getPathSearchRange();
    this.worldObj.theProfiler.startSection("pathfind");
    BlockPos blockpos = new BlockPos((Entity)this.theEntity);
    int i = (int)(f + 8.0F);
    ChunkCache chunkcache = new ChunkCache(this.worldObj, blockpos.add(-i, -i, -i), blockpos.add(i, i, i), 0);
    Path path = this.pathFinder.findPath((IBlockAccess)chunkcache, this.theEntity, this.targetPos, f);
    this.worldObj.theProfiler.endSection();
    return path;
  }
  
  @Nullable
  public Path getPathToEntityLiving(Entity entityIn) {
    if (!canNavigate())
      return null; 
    BlockPos blockpos = new BlockPos(entityIn);
    if (this.currentPath != null && !this.currentPath.isFinished() && blockpos.equals(this.targetPos))
      return this.currentPath; 
    this.targetPos = blockpos;
    float f = getPathSearchRange();
    this.worldObj.theProfiler.startSection("pathfind");
    BlockPos blockpos1 = (new BlockPos((Entity)this.theEntity)).up();
    int i = (int)(f + 16.0F);
    ChunkCache chunkcache = new ChunkCache(this.worldObj, blockpos1.add(-i, -i, -i), blockpos1.add(i, i, i), 0);
    Path path = this.pathFinder.findPath((IBlockAccess)chunkcache, this.theEntity, entityIn, f);
    this.worldObj.theProfiler.endSection();
    return path;
  }
  
  public boolean tryMoveToXYZ(double x, double y, double z, double speedIn) {
    return setPath(getPathToXYZ(x, y, z), speedIn);
  }
  
  public boolean tryMoveToEntityLiving(Entity entityIn, double speedIn) {
    Path path = getPathToEntityLiving(entityIn);
    return (path != null && setPath(path, speedIn));
  }
  
  public boolean setPath(@Nullable Path pathentityIn, double speedIn) {
    if (pathentityIn == null) {
      this.currentPath = null;
      return false;
    } 
    if (!pathentityIn.isSamePath(this.currentPath))
      this.currentPath = pathentityIn; 
    removeSunnyPath();
    if (this.currentPath.getCurrentPathLength() <= 0)
      return false; 
    this.speed = speedIn;
    Vec3d vec3d = getEntityPosition();
    this.ticksAtLastPos = this.totalTicks;
    this.lastPosCheck = vec3d;
    return true;
  }
  
  @Nullable
  public Path getPath() {
    return this.currentPath;
  }
  
  public void onUpdateNavigation() {
    this.totalTicks++;
    if (this.tryUpdatePath)
      updatePath(); 
    if (!noPath()) {
      if (canNavigate()) {
        pathFollow();
      } else if (this.currentPath != null && this.currentPath.getCurrentPathIndex() < this.currentPath.getCurrentPathLength()) {
        Vec3d vec3d = getEntityPosition();
        Vec3d vec3d1 = this.currentPath.getVectorFromIndex((Entity)this.theEntity, this.currentPath.getCurrentPathIndex());
        if (vec3d.yCoord > vec3d1.yCoord && !this.theEntity.onGround && MathHelper.floor(vec3d.xCoord) == MathHelper.floor(vec3d1.xCoord) && MathHelper.floor(vec3d.zCoord) == MathHelper.floor(vec3d1.zCoord))
          this.currentPath.setCurrentPathIndex(this.currentPath.getCurrentPathIndex() + 1); 
      } 
      func_192876_m();
      if (!noPath()) {
        Vec3d vec3d2 = this.currentPath.getPosition((Entity)this.theEntity);
        BlockPos blockpos = (new BlockPos(vec3d2)).down();
        AxisAlignedBB axisalignedbb = this.worldObj.getBlockState(blockpos).getBoundingBox((IBlockAccess)this.worldObj, blockpos);
        vec3d2 = vec3d2.subtract(0.0D, 1.0D - axisalignedbb.maxY, 0.0D);
        this.theEntity.getMoveHelper().setMoveTo(vec3d2.xCoord, vec3d2.yCoord, vec3d2.zCoord, this.speed);
      } 
    } 
  }
  
  protected void func_192876_m() {}
  
  protected void pathFollow() {
    Vec3d vec3d = getEntityPosition();
    int i = this.currentPath.getCurrentPathLength();
    for (int j = this.currentPath.getCurrentPathIndex(); j < this.currentPath.getCurrentPathLength(); j++) {
      if ((this.currentPath.getPathPointFromIndex(j)).yCoord != Math.floor(vec3d.yCoord)) {
        i = j;
        break;
      } 
    } 
    this.maxDistanceToWaypoint = (this.theEntity.width > 0.75F) ? (this.theEntity.width / 2.0F) : (0.75F - this.theEntity.width / 2.0F);
    Vec3d vec3d1 = this.currentPath.getCurrentPos();
    if (MathHelper.abs((float)(this.theEntity.posX - vec3d1.xCoord + 0.5D)) < this.maxDistanceToWaypoint && MathHelper.abs((float)(this.theEntity.posZ - vec3d1.zCoord + 0.5D)) < this.maxDistanceToWaypoint && Math.abs(this.theEntity.posY - vec3d1.yCoord) < 1.0D)
      this.currentPath.setCurrentPathIndex(this.currentPath.getCurrentPathIndex() + 1); 
    int k = MathHelper.ceil(this.theEntity.width);
    int l = MathHelper.ceil(this.theEntity.height);
    int i1 = k;
    for (int j1 = i - 1; j1 >= this.currentPath.getCurrentPathIndex(); j1--) {
      if (isDirectPathBetweenPoints(vec3d, this.currentPath.getVectorFromIndex((Entity)this.theEntity, j1), k, l, i1)) {
        this.currentPath.setCurrentPathIndex(j1);
        break;
      } 
    } 
    checkForStuck(vec3d);
  }
  
  protected void checkForStuck(Vec3d positionVec3) {
    if (this.totalTicks - this.ticksAtLastPos > 100) {
      if (positionVec3.squareDistanceTo(this.lastPosCheck) < 2.25D)
        clearPathEntity(); 
      this.ticksAtLastPos = this.totalTicks;
      this.lastPosCheck = positionVec3;
    } 
    if (this.currentPath != null && !this.currentPath.isFinished()) {
      Vec3d vec3d = this.currentPath.getCurrentPos();
      if (vec3d.equals(this.timeoutCachedNode)) {
        this.timeoutTimer += System.currentTimeMillis() - this.lastTimeoutCheck;
      } else {
        this.timeoutCachedNode = vec3d;
        double d0 = positionVec3.distanceTo(this.timeoutCachedNode);
        this.timeoutLimit = (this.theEntity.getAIMoveSpeed() > 0.0F) ? (d0 / this.theEntity.getAIMoveSpeed() * 1000.0D) : 0.0D;
      } 
      if (this.timeoutLimit > 0.0D && this.timeoutTimer > this.timeoutLimit * 3.0D) {
        this.timeoutCachedNode = Vec3d.ZERO;
        this.timeoutTimer = 0L;
        this.timeoutLimit = 0.0D;
        clearPathEntity();
      } 
      this.lastTimeoutCheck = System.currentTimeMillis();
    } 
  }
  
  public boolean noPath() {
    return !(this.currentPath != null && !this.currentPath.isFinished());
  }
  
  public void clearPathEntity() {
    this.currentPath = null;
  }
  
  protected abstract Vec3d getEntityPosition();
  
  protected abstract boolean canNavigate();
  
  protected boolean isInLiquid() {
    return !(!this.theEntity.isInWater() && !this.theEntity.isInLava());
  }
  
  protected void removeSunnyPath() {
    if (this.currentPath != null)
      for (int i = 0; i < this.currentPath.getCurrentPathLength(); i++) {
        PathPoint pathpoint = this.currentPath.getPathPointFromIndex(i);
        PathPoint pathpoint1 = (i + 1 < this.currentPath.getCurrentPathLength()) ? this.currentPath.getPathPointFromIndex(i + 1) : null;
        IBlockState iblockstate = this.worldObj.getBlockState(new BlockPos(pathpoint.xCoord, pathpoint.yCoord, pathpoint.zCoord));
        Block block = iblockstate.getBlock();
        if (block == Blocks.CAULDRON) {
          this.currentPath.setPoint(i, pathpoint.cloneMove(pathpoint.xCoord, pathpoint.yCoord + 1, pathpoint.zCoord));
          if (pathpoint1 != null && pathpoint.yCoord >= pathpoint1.yCoord)
            this.currentPath.setPoint(i + 1, pathpoint1.cloneMove(pathpoint1.xCoord, pathpoint.yCoord + 1, pathpoint1.zCoord)); 
        } 
      }  
  }
  
  protected abstract boolean isDirectPathBetweenPoints(Vec3d paramVec3d1, Vec3d paramVec3d2, int paramInt1, int paramInt2, int paramInt3);
  
  public boolean canEntityStandOnPos(BlockPos pos) {
    return this.worldObj.getBlockState(pos.down()).isFullBlock();
  }
  
  public NodeProcessor getNodeProcessor() {
    return this.nodeProcessor;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\pathfinding\PathNavigate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
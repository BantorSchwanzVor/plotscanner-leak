package net.minecraft.util.math;

import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;

public class RayTraceResult {
  private BlockPos blockPos;
  
  public Type typeOfHit;
  
  public EnumFacing sideHit;
  
  public Vec3d hitVec;
  
  public Entity entityHit;
  
  public RayTraceResult(Vec3d hitVecIn, EnumFacing sideHitIn, BlockPos blockPosIn) {
    this(Type.BLOCK, hitVecIn, sideHitIn, blockPosIn);
  }
  
  public RayTraceResult(Vec3d hitVecIn, EnumFacing sideHitIn) {
    this(Type.BLOCK, hitVecIn, sideHitIn, BlockPos.ORIGIN);
  }
  
  public RayTraceResult(Entity entityIn) {
    this(entityIn, new Vec3d(entityIn.posX, entityIn.posY, entityIn.posZ));
  }
  
  public RayTraceResult(Type typeIn, Vec3d hitVecIn, EnumFacing sideHitIn, BlockPos blockPosIn) {
    this.typeOfHit = typeIn;
    this.blockPos = blockPosIn;
    this.sideHit = sideHitIn;
    this.hitVec = new Vec3d(hitVecIn.xCoord, hitVecIn.yCoord, hitVecIn.zCoord);
  }
  
  public RayTraceResult(Entity entityHitIn, Vec3d hitVecIn) {
    this.typeOfHit = Type.ENTITY;
    this.entityHit = entityHitIn;
    this.hitVec = hitVecIn;
  }
  
  public BlockPos getBlockPos() {
    return this.blockPos;
  }
  
  public String toString() {
    return "HitResult{type=" + this.typeOfHit + ", blockpos=" + this.blockPos + ", f=" + this.sideHit + ", pos=" + this.hitVec + ", entity=" + this.entityHit + '}';
  }
  
  public enum Type {
    MISS, BLOCK, ENTITY;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraf\\util\math\RayTraceResult.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
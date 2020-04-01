package net.minecraft.util.math;

import com.google.common.annotations.VisibleForTesting;
import javax.annotation.Nullable;
import net.minecraft.util.EnumFacing;

public class AxisAlignedBB {
  public final double minX;
  
  public final double minY;
  
  public final double minZ;
  
  public final double maxX;
  
  public final double maxY;
  
  public final double maxZ;
  
  public AxisAlignedBB(double x1, double y1, double z1, double x2, double y2, double z2) {
    this.minX = Math.min(x1, x2);
    this.minY = Math.min(y1, y2);
    this.minZ = Math.min(z1, z2);
    this.maxX = Math.max(x1, x2);
    this.maxY = Math.max(y1, y2);
    this.maxZ = Math.max(z1, z2);
  }
  
  public AxisAlignedBB(BlockPos pos) {
    this(pos.getX(), pos.getY(), pos.getZ(), (pos.getX() + 1), (pos.getY() + 1), (pos.getZ() + 1));
  }
  
  public AxisAlignedBB(BlockPos pos1, BlockPos pos2) {
    this(pos1.getX(), pos1.getY(), pos1.getZ(), pos2.getX(), pos2.getY(), pos2.getZ());
  }
  
  public AxisAlignedBB(Vec3d min, Vec3d max) {
    this(min.xCoord, min.yCoord, min.zCoord, max.xCoord, max.yCoord, max.zCoord);
  }
  
  public AxisAlignedBB setMaxY(double y2) {
    return new AxisAlignedBB(this.minX, this.minY, this.minZ, this.maxX, y2, this.maxZ);
  }
  
  public boolean equals(Object p_equals_1_) {
    if (this == p_equals_1_)
      return true; 
    if (!(p_equals_1_ instanceof AxisAlignedBB))
      return false; 
    AxisAlignedBB axisalignedbb = (AxisAlignedBB)p_equals_1_;
    if (Double.compare(axisalignedbb.minX, this.minX) != 0)
      return false; 
    if (Double.compare(axisalignedbb.minY, this.minY) != 0)
      return false; 
    if (Double.compare(axisalignedbb.minZ, this.minZ) != 0)
      return false; 
    if (Double.compare(axisalignedbb.maxX, this.maxX) != 0)
      return false; 
    if (Double.compare(axisalignedbb.maxY, this.maxY) != 0)
      return false; 
    return (Double.compare(axisalignedbb.maxZ, this.maxZ) == 0);
  }
  
  public int hashCode() {
    long i = Double.doubleToLongBits(this.minX);
    int j = (int)(i ^ i >>> 32L);
    i = Double.doubleToLongBits(this.minY);
    j = 31 * j + (int)(i ^ i >>> 32L);
    i = Double.doubleToLongBits(this.minZ);
    j = 31 * j + (int)(i ^ i >>> 32L);
    i = Double.doubleToLongBits(this.maxX);
    j = 31 * j + (int)(i ^ i >>> 32L);
    i = Double.doubleToLongBits(this.maxY);
    j = 31 * j + (int)(i ^ i >>> 32L);
    i = Double.doubleToLongBits(this.maxZ);
    j = 31 * j + (int)(i ^ i >>> 32L);
    return j;
  }
  
  public AxisAlignedBB func_191195_a(double p_191195_1_, double p_191195_3_, double p_191195_5_) {
    double d0 = this.minX;
    double d1 = this.minY;
    double d2 = this.minZ;
    double d3 = this.maxX;
    double d4 = this.maxY;
    double d5 = this.maxZ;
    if (p_191195_1_ < 0.0D) {
      d0 -= p_191195_1_;
    } else if (p_191195_1_ > 0.0D) {
      d3 -= p_191195_1_;
    } 
    if (p_191195_3_ < 0.0D) {
      d1 -= p_191195_3_;
    } else if (p_191195_3_ > 0.0D) {
      d4 -= p_191195_3_;
    } 
    if (p_191195_5_ < 0.0D) {
      d2 -= p_191195_5_;
    } else if (p_191195_5_ > 0.0D) {
      d5 -= p_191195_5_;
    } 
    return new AxisAlignedBB(d0, d1, d2, d3, d4, d5);
  }
  
  public AxisAlignedBB addCoord(double x, double y, double z) {
    double d0 = this.minX;
    double d1 = this.minY;
    double d2 = this.minZ;
    double d3 = this.maxX;
    double d4 = this.maxY;
    double d5 = this.maxZ;
    if (x < 0.0D) {
      d0 += x;
    } else if (x > 0.0D) {
      d3 += x;
    } 
    if (y < 0.0D) {
      d1 += y;
    } else if (y > 0.0D) {
      d4 += y;
    } 
    if (z < 0.0D) {
      d2 += z;
    } else if (z > 0.0D) {
      d5 += z;
    } 
    return new AxisAlignedBB(d0, d1, d2, d3, d4, d5);
  }
  
  public AxisAlignedBB expand(double x, double y, double z) {
    double d0 = this.minX - x;
    double d1 = this.minY - y;
    double d2 = this.minZ - z;
    double d3 = this.maxX + x;
    double d4 = this.maxY + y;
    double d5 = this.maxZ + z;
    return new AxisAlignedBB(d0, d1, d2, d3, d4, d5);
  }
  
  public AxisAlignedBB expandXyz(double value) {
    return expand(value, value, value);
  }
  
  public AxisAlignedBB func_191500_a(AxisAlignedBB p_191500_1_) {
    double d0 = Math.max(this.minX, p_191500_1_.minX);
    double d1 = Math.max(this.minY, p_191500_1_.minY);
    double d2 = Math.max(this.minZ, p_191500_1_.minZ);
    double d3 = Math.min(this.maxX, p_191500_1_.maxX);
    double d4 = Math.min(this.maxY, p_191500_1_.maxY);
    double d5 = Math.min(this.maxZ, p_191500_1_.maxZ);
    return new AxisAlignedBB(d0, d1, d2, d3, d4, d5);
  }
  
  public AxisAlignedBB union(AxisAlignedBB other) {
    double d0 = Math.min(this.minX, other.minX);
    double d1 = Math.min(this.minY, other.minY);
    double d2 = Math.min(this.minZ, other.minZ);
    double d3 = Math.max(this.maxX, other.maxX);
    double d4 = Math.max(this.maxY, other.maxY);
    double d5 = Math.max(this.maxZ, other.maxZ);
    return new AxisAlignedBB(d0, d1, d2, d3, d4, d5);
  }
  
  public AxisAlignedBB offset(double x, double y, double z) {
    return new AxisAlignedBB(this.minX + x, this.minY + y, this.minZ + z, this.maxX + x, this.maxY + y, this.maxZ + z);
  }
  
  public AxisAlignedBB offset(BlockPos pos) {
    return new AxisAlignedBB(this.minX + pos.getX(), this.minY + pos.getY(), this.minZ + pos.getZ(), this.maxX + pos.getX(), this.maxY + pos.getY(), this.maxZ + pos.getZ());
  }
  
  public AxisAlignedBB func_191194_a(Vec3d p_191194_1_) {
    return offset(p_191194_1_.xCoord, p_191194_1_.yCoord, p_191194_1_.zCoord);
  }
  
  public double calculateXOffset(AxisAlignedBB other, double offsetX) {
    if (other.maxY > this.minY && other.minY < this.maxY && other.maxZ > this.minZ && other.minZ < this.maxZ) {
      if (offsetX > 0.0D && other.maxX <= this.minX) {
        double d1 = this.minX - other.maxX;
        if (d1 < offsetX)
          offsetX = d1; 
      } else if (offsetX < 0.0D && other.minX >= this.maxX) {
        double d0 = this.maxX - other.minX;
        if (d0 > offsetX)
          offsetX = d0; 
      } 
      return offsetX;
    } 
    return offsetX;
  }
  
  public double calculateYOffset(AxisAlignedBB other, double offsetY) {
    if (other.maxX > this.minX && other.minX < this.maxX && other.maxZ > this.minZ && other.minZ < this.maxZ) {
      if (offsetY > 0.0D && other.maxY <= this.minY) {
        double d1 = this.minY - other.maxY;
        if (d1 < offsetY)
          offsetY = d1; 
      } else if (offsetY < 0.0D && other.minY >= this.maxY) {
        double d0 = this.maxY - other.minY;
        if (d0 > offsetY)
          offsetY = d0; 
      } 
      return offsetY;
    } 
    return offsetY;
  }
  
  public double calculateZOffset(AxisAlignedBB other, double offsetZ) {
    if (other.maxX > this.minX && other.minX < this.maxX && other.maxY > this.minY && other.minY < this.maxY) {
      if (offsetZ > 0.0D && other.maxZ <= this.minZ) {
        double d1 = this.minZ - other.maxZ;
        if (d1 < offsetZ)
          offsetZ = d1; 
      } else if (offsetZ < 0.0D && other.minZ >= this.maxZ) {
        double d0 = this.maxZ - other.minZ;
        if (d0 > offsetZ)
          offsetZ = d0; 
      } 
      return offsetZ;
    } 
    return offsetZ;
  }
  
  public boolean intersectsWith(AxisAlignedBB other) {
    return intersects(other.minX, other.minY, other.minZ, other.maxX, other.maxY, other.maxZ);
  }
  
  public boolean intersects(double x1, double y1, double z1, double x2, double y2, double z2) {
    return (this.minX < x2 && this.maxX > x1 && this.minY < y2 && this.maxY > y1 && this.minZ < z2 && this.maxZ > z1);
  }
  
  public boolean intersects(Vec3d min, Vec3d max) {
    return intersects(Math.min(min.xCoord, max.xCoord), Math.min(min.yCoord, max.yCoord), Math.min(min.zCoord, max.zCoord), Math.max(min.xCoord, max.xCoord), Math.max(min.yCoord, max.yCoord), Math.max(min.zCoord, max.zCoord));
  }
  
  public boolean isVecInside(Vec3d vec) {
    if (vec.xCoord > this.minX && vec.xCoord < this.maxX) {
      if (vec.yCoord > this.minY && vec.yCoord < this.maxY)
        return (vec.zCoord > this.minZ && vec.zCoord < this.maxZ); 
      return false;
    } 
    return false;
  }
  
  public double getAverageEdgeLength() {
    double d0 = this.maxX - this.minX;
    double d1 = this.maxY - this.minY;
    double d2 = this.maxZ - this.minZ;
    return (d0 + d1 + d2) / 3.0D;
  }
  
  public AxisAlignedBB contract(double value) {
    return expandXyz(-value);
  }
  
  @Nullable
  public RayTraceResult calculateIntercept(Vec3d vecA, Vec3d vecB) {
    Vec3d vec3d = collideWithXPlane(this.minX, vecA, vecB);
    EnumFacing enumfacing = EnumFacing.WEST;
    Vec3d vec3d1 = collideWithXPlane(this.maxX, vecA, vecB);
    if (vec3d1 != null && isClosest(vecA, vec3d, vec3d1)) {
      vec3d = vec3d1;
      enumfacing = EnumFacing.EAST;
    } 
    vec3d1 = collideWithYPlane(this.minY, vecA, vecB);
    if (vec3d1 != null && isClosest(vecA, vec3d, vec3d1)) {
      vec3d = vec3d1;
      enumfacing = EnumFacing.DOWN;
    } 
    vec3d1 = collideWithYPlane(this.maxY, vecA, vecB);
    if (vec3d1 != null && isClosest(vecA, vec3d, vec3d1)) {
      vec3d = vec3d1;
      enumfacing = EnumFacing.UP;
    } 
    vec3d1 = collideWithZPlane(this.minZ, vecA, vecB);
    if (vec3d1 != null && isClosest(vecA, vec3d, vec3d1)) {
      vec3d = vec3d1;
      enumfacing = EnumFacing.NORTH;
    } 
    vec3d1 = collideWithZPlane(this.maxZ, vecA, vecB);
    if (vec3d1 != null && isClosest(vecA, vec3d, vec3d1)) {
      vec3d = vec3d1;
      enumfacing = EnumFacing.SOUTH;
    } 
    return (vec3d == null) ? null : new RayTraceResult(vec3d, enumfacing);
  }
  
  @VisibleForTesting
  boolean isClosest(Vec3d p_186661_1_, @Nullable Vec3d p_186661_2_, Vec3d p_186661_3_) {
    return !(p_186661_2_ != null && p_186661_1_.squareDistanceTo(p_186661_3_) >= p_186661_1_.squareDistanceTo(p_186661_2_));
  }
  
  @Nullable
  @VisibleForTesting
  Vec3d collideWithXPlane(double p_186671_1_, Vec3d p_186671_3_, Vec3d p_186671_4_) {
    Vec3d vec3d = p_186671_3_.getIntermediateWithXValue(p_186671_4_, p_186671_1_);
    return (vec3d != null && intersectsWithYZ(vec3d)) ? vec3d : null;
  }
  
  @Nullable
  @VisibleForTesting
  Vec3d collideWithYPlane(double p_186663_1_, Vec3d p_186663_3_, Vec3d p_186663_4_) {
    Vec3d vec3d = p_186663_3_.getIntermediateWithYValue(p_186663_4_, p_186663_1_);
    return (vec3d != null && intersectsWithXZ(vec3d)) ? vec3d : null;
  }
  
  @Nullable
  @VisibleForTesting
  Vec3d collideWithZPlane(double p_186665_1_, Vec3d p_186665_3_, Vec3d p_186665_4_) {
    Vec3d vec3d = p_186665_3_.getIntermediateWithZValue(p_186665_4_, p_186665_1_);
    return (vec3d != null && intersectsWithXY(vec3d)) ? vec3d : null;
  }
  
  @VisibleForTesting
  public boolean intersectsWithYZ(Vec3d vec) {
    return (vec.yCoord >= this.minY && vec.yCoord <= this.maxY && vec.zCoord >= this.minZ && vec.zCoord <= this.maxZ);
  }
  
  @VisibleForTesting
  public boolean intersectsWithXZ(Vec3d vec) {
    return (vec.xCoord >= this.minX && vec.xCoord <= this.maxX && vec.zCoord >= this.minZ && vec.zCoord <= this.maxZ);
  }
  
  @VisibleForTesting
  public boolean intersectsWithXY(Vec3d vec) {
    return (vec.xCoord >= this.minX && vec.xCoord <= this.maxX && vec.yCoord >= this.minY && vec.yCoord <= this.maxY);
  }
  
  public String toString() {
    return "box[" + this.minX + ", " + this.minY + ", " + this.minZ + " -> " + this.maxX + ", " + this.maxY + ", " + this.maxZ + "]";
  }
  
  public boolean hasNaN() {
    return !(!Double.isNaN(this.minX) && !Double.isNaN(this.minY) && !Double.isNaN(this.minZ) && !Double.isNaN(this.maxX) && !Double.isNaN(this.maxY) && !Double.isNaN(this.maxZ));
  }
  
  public Vec3d getCenter() {
    return new Vec3d(this.minX + (this.maxX - this.minX) * 0.5D, this.minY + (this.maxY - this.minY) * 0.5D, this.minZ + (this.maxZ - this.minZ) * 0.5D);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraf\\util\math\AxisAlignedBB.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
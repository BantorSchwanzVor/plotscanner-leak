package net.minecraft.entity;

import com.google.common.base.Predicate;
import javax.annotation.Nullable;
import net.minecraft.block.BlockRedstoneDiode;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import org.apache.commons.lang3.Validate;

public abstract class EntityHanging extends Entity {
  private static final Predicate<Entity> IS_HANGING_ENTITY = new Predicate<Entity>() {
      public boolean apply(@Nullable Entity p_apply_1_) {
        return p_apply_1_ instanceof EntityHanging;
      }
    };
  
  private int tickCounter1;
  
  protected BlockPos hangingPosition;
  
  @Nullable
  public EnumFacing facingDirection;
  
  public EntityHanging(World worldIn) {
    super(worldIn);
    setSize(0.5F, 0.5F);
  }
  
  public EntityHanging(World worldIn, BlockPos hangingPositionIn) {
    this(worldIn);
    this.hangingPosition = hangingPositionIn;
  }
  
  protected void entityInit() {}
  
  protected void updateFacingWithBoundingBox(EnumFacing facingDirectionIn) {
    Validate.notNull(facingDirectionIn);
    Validate.isTrue(facingDirectionIn.getAxis().isHorizontal());
    this.facingDirection = facingDirectionIn;
    this.rotationYaw = (this.facingDirection.getHorizontalIndex() * 90);
    this.prevRotationYaw = this.rotationYaw;
    updateBoundingBox();
  }
  
  protected void updateBoundingBox() {
    if (this.facingDirection != null) {
      double d0 = this.hangingPosition.getX() + 0.5D;
      double d1 = this.hangingPosition.getY() + 0.5D;
      double d2 = this.hangingPosition.getZ() + 0.5D;
      double d3 = 0.46875D;
      double d4 = offs(getWidthPixels());
      double d5 = offs(getHeightPixels());
      d0 -= this.facingDirection.getFrontOffsetX() * 0.46875D;
      d2 -= this.facingDirection.getFrontOffsetZ() * 0.46875D;
      d1 += d5;
      EnumFacing enumfacing = this.facingDirection.rotateYCCW();
      d0 += d4 * enumfacing.getFrontOffsetX();
      d2 += d4 * enumfacing.getFrontOffsetZ();
      this.posX = d0;
      this.posY = d1;
      this.posZ = d2;
      double d6 = getWidthPixels();
      double d7 = getHeightPixels();
      double d8 = getWidthPixels();
      if (this.facingDirection.getAxis() == EnumFacing.Axis.Z) {
        d8 = 1.0D;
      } else {
        d6 = 1.0D;
      } 
      d6 /= 32.0D;
      d7 /= 32.0D;
      d8 /= 32.0D;
      setEntityBoundingBox(new AxisAlignedBB(d0 - d6, d1 - d7, d2 - d8, d0 + d6, d1 + d7, d2 + d8));
    } 
  }
  
  private double offs(int p_190202_1_) {
    return (p_190202_1_ % 32 == 0) ? 0.5D : 0.0D;
  }
  
  public void onUpdate() {
    this.prevPosX = this.posX;
    this.prevPosY = this.posY;
    this.prevPosZ = this.posZ;
    if (this.tickCounter1++ == 100 && !this.world.isRemote) {
      this.tickCounter1 = 0;
      if (!this.isDead && !onValidSurface()) {
        setDead();
        onBroken((Entity)null);
      } 
    } 
  }
  
  public boolean onValidSurface() {
    if (!this.world.getCollisionBoxes(this, getEntityBoundingBox()).isEmpty())
      return false; 
    int i = Math.max(1, getWidthPixels() / 16);
    int j = Math.max(1, getHeightPixels() / 16);
    BlockPos blockpos = this.hangingPosition.offset(this.facingDirection.getOpposite());
    EnumFacing enumfacing = this.facingDirection.rotateYCCW();
    BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
    for (int k = 0; k < i; k++) {
      for (int l = 0; l < j; l++) {
        int i1 = (i - 1) / -2;
        int j1 = (j - 1) / -2;
        blockpos$mutableblockpos.setPos((Vec3i)blockpos).move(enumfacing, k + i1).move(EnumFacing.UP, l + j1);
        IBlockState iblockstate = this.world.getBlockState((BlockPos)blockpos$mutableblockpos);
        if (!iblockstate.getMaterial().isSolid() && !BlockRedstoneDiode.isDiode(iblockstate))
          return false; 
      } 
    } 
    return this.world.getEntitiesInAABBexcluding(this, getEntityBoundingBox(), IS_HANGING_ENTITY).isEmpty();
  }
  
  public boolean canBeCollidedWith() {
    return true;
  }
  
  public boolean hitByEntity(Entity entityIn) {
    return (entityIn instanceof EntityPlayer) ? attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer)entityIn), 0.0F) : false;
  }
  
  public EnumFacing getHorizontalFacing() {
    return this.facingDirection;
  }
  
  public boolean attackEntityFrom(DamageSource source, float amount) {
    if (isEntityInvulnerable(source))
      return false; 
    if (!this.isDead && !this.world.isRemote) {
      setDead();
      setBeenAttacked();
      onBroken(source.getEntity());
    } 
    return true;
  }
  
  public void moveEntity(MoverType x, double p_70091_2_, double p_70091_4_, double p_70091_6_) {
    if (!this.world.isRemote && !this.isDead && p_70091_2_ * p_70091_2_ + p_70091_4_ * p_70091_4_ + p_70091_6_ * p_70091_6_ > 0.0D) {
      setDead();
      onBroken((Entity)null);
    } 
  }
  
  public void addVelocity(double x, double y, double z) {
    if (!this.world.isRemote && !this.isDead && x * x + y * y + z * z > 0.0D) {
      setDead();
      onBroken((Entity)null);
    } 
  }
  
  public void writeEntityToNBT(NBTTagCompound compound) {
    compound.setByte("Facing", (byte)this.facingDirection.getHorizontalIndex());
    BlockPos blockpos = getHangingPosition();
    compound.setInteger("TileX", blockpos.getX());
    compound.setInteger("TileY", blockpos.getY());
    compound.setInteger("TileZ", blockpos.getZ());
  }
  
  public void readEntityFromNBT(NBTTagCompound compound) {
    this.hangingPosition = new BlockPos(compound.getInteger("TileX"), compound.getInteger("TileY"), compound.getInteger("TileZ"));
    updateFacingWithBoundingBox(EnumFacing.getHorizontal(compound.getByte("Facing")));
  }
  
  public EntityItem entityDropItem(ItemStack stack, float offsetY) {
    EntityItem entityitem = new EntityItem(this.world, this.posX + (this.facingDirection.getFrontOffsetX() * 0.15F), this.posY + offsetY, this.posZ + (this.facingDirection.getFrontOffsetZ() * 0.15F), stack);
    entityitem.setDefaultPickupDelay();
    this.world.spawnEntityInWorld((Entity)entityitem);
    return entityitem;
  }
  
  protected boolean shouldSetPosAfterLoading() {
    return false;
  }
  
  public void setPosition(double x, double y, double z) {
    this.hangingPosition = new BlockPos(x, y, z);
    updateBoundingBox();
    this.isAirBorne = true;
  }
  
  public BlockPos getHangingPosition() {
    return this.hangingPosition;
  }
  
  public float getRotatedYaw(Rotation transformRotation) {
    if (this.facingDirection != null && this.facingDirection.getAxis() != EnumFacing.Axis.Y)
      switch (transformRotation) {
        case null:
          this.facingDirection = this.facingDirection.getOpposite();
          break;
        case COUNTERCLOCKWISE_90:
          this.facingDirection = this.facingDirection.rotateYCCW();
          break;
        case CLOCKWISE_90:
          this.facingDirection = this.facingDirection.rotateY();
          break;
      }  
    float f = MathHelper.wrapDegrees(this.rotationYaw);
    switch (transformRotation) {
      case null:
        return f + 180.0F;
      case COUNTERCLOCKWISE_90:
        return f + 90.0F;
      case CLOCKWISE_90:
        return f + 270.0F;
    } 
    return f;
  }
  
  public float getMirroredYaw(Mirror transformMirror) {
    return getRotatedYaw(transformMirror.toRotation(this.facingDirection));
  }
  
  public void onStruckByLightning(EntityLightningBolt lightningBolt) {}
  
  public abstract int getWidthPixels();
  
  public abstract int getHeightPixels();
  
  public abstract void onBroken(Entity paramEntity);
  
  public abstract void playPlaceSound();
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\entity\EntityHanging.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
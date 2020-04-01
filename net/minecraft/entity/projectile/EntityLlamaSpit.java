package net.minecraft.entity.projectile;

import java.util.List;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.passive.EntityLlama;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityLlamaSpit extends Entity implements IProjectile {
  public EntityLlama field_190539_a;
  
  private NBTTagCompound field_190540_b;
  
  public EntityLlamaSpit(World p_i47272_1_) {
    super(p_i47272_1_);
  }
  
  public EntityLlamaSpit(World p_i47273_1_, EntityLlama p_i47273_2_) {
    super(p_i47273_1_);
    this.field_190539_a = p_i47273_2_;
    setPosition(p_i47273_2_.posX - (p_i47273_2_.width + 1.0F) * 0.5D * MathHelper.sin(p_i47273_2_.renderYawOffset * 0.017453292F), p_i47273_2_.posY + p_i47273_2_.getEyeHeight() - 0.10000000149011612D, p_i47273_2_.posZ + (p_i47273_2_.width + 1.0F) * 0.5D * MathHelper.cos(p_i47273_2_.renderYawOffset * 0.017453292F));
    setSize(0.25F, 0.25F);
  }
  
  public EntityLlamaSpit(World p_i47274_1_, double p_i47274_2_, double p_i47274_4_, double p_i47274_6_, double p_i47274_8_, double p_i47274_10_, double p_i47274_12_) {
    super(p_i47274_1_);
    setPosition(p_i47274_2_, p_i47274_4_, p_i47274_6_);
    for (int i = 0; i < 7; i++) {
      double d0 = 0.4D + 0.1D * i;
      p_i47274_1_.spawnParticle(EnumParticleTypes.SPIT, p_i47274_2_, p_i47274_4_, p_i47274_6_, p_i47274_8_ * d0, p_i47274_10_, p_i47274_12_ * d0, new int[0]);
    } 
    this.motionX = p_i47274_8_;
    this.motionY = p_i47274_10_;
    this.motionZ = p_i47274_12_;
  }
  
  public void onUpdate() {
    super.onUpdate();
    if (this.field_190540_b != null)
      func_190537_j(); 
    Vec3d vec3d = new Vec3d(this.posX, this.posY, this.posZ);
    Vec3d vec3d1 = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
    RayTraceResult raytraceresult = this.world.rayTraceBlocks(vec3d, vec3d1);
    vec3d = new Vec3d(this.posX, this.posY, this.posZ);
    vec3d1 = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
    if (raytraceresult != null)
      vec3d1 = new Vec3d(raytraceresult.hitVec.xCoord, raytraceresult.hitVec.yCoord, raytraceresult.hitVec.zCoord); 
    Entity entity = func_190538_a(vec3d, vec3d1);
    if (entity != null)
      raytraceresult = new RayTraceResult(entity); 
    if (raytraceresult != null)
      func_190536_a(raytraceresult); 
    this.posX += this.motionX;
    this.posY += this.motionY;
    this.posZ += this.motionZ;
    float f = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
    this.rotationYaw = (float)(MathHelper.atan2(this.motionX, this.motionZ) * 57.29577951308232D);
    for (this.rotationPitch = (float)(MathHelper.atan2(this.motionY, f) * 57.29577951308232D); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F);
    while (this.rotationPitch - this.prevRotationPitch >= 180.0F)
      this.prevRotationPitch += 360.0F; 
    while (this.rotationYaw - this.prevRotationYaw < -180.0F)
      this.prevRotationYaw -= 360.0F; 
    while (this.rotationYaw - this.prevRotationYaw >= 180.0F)
      this.prevRotationYaw += 360.0F; 
    this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
    this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;
    float f1 = 0.99F;
    float f2 = 0.06F;
    if (!this.world.isMaterialInBB(getEntityBoundingBox(), Material.AIR)) {
      setDead();
    } else if (isInWater()) {
      setDead();
    } else {
      this.motionX *= 0.9900000095367432D;
      this.motionY *= 0.9900000095367432D;
      this.motionZ *= 0.9900000095367432D;
      if (!hasNoGravity())
        this.motionY -= 0.05999999865889549D; 
      setPosition(this.posX, this.posY, this.posZ);
    } 
  }
  
  public void setVelocity(double x, double y, double z) {
    this.motionX = x;
    this.motionY = y;
    this.motionZ = z;
    if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
      float f = MathHelper.sqrt(x * x + z * z);
      this.rotationPitch = (float)(MathHelper.atan2(y, f) * 57.29577951308232D);
      this.rotationYaw = (float)(MathHelper.atan2(x, z) * 57.29577951308232D);
      this.prevRotationPitch = this.rotationPitch;
      this.prevRotationYaw = this.rotationYaw;
      setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
    } 
  }
  
  @Nullable
  private Entity func_190538_a(Vec3d p_190538_1_, Vec3d p_190538_2_) {
    Entity entity = null;
    List<Entity> list = this.world.getEntitiesWithinAABBExcludingEntity(this, getEntityBoundingBox().addCoord(this.motionX, this.motionY, this.motionZ).expandXyz(1.0D));
    double d0 = 0.0D;
    for (Entity entity1 : list) {
      if (entity1 != this.field_190539_a) {
        AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().expandXyz(0.30000001192092896D);
        RayTraceResult raytraceresult = axisalignedbb.calculateIntercept(p_190538_1_, p_190538_2_);
        if (raytraceresult != null) {
          double d1 = p_190538_1_.squareDistanceTo(raytraceresult.hitVec);
          if (d1 < d0 || d0 == 0.0D) {
            entity = entity1;
            d0 = d1;
          } 
        } 
      } 
    } 
    return entity;
  }
  
  public void setThrowableHeading(double x, double y, double z, float velocity, float inaccuracy) {
    float f = MathHelper.sqrt(x * x + y * y + z * z);
    x /= f;
    y /= f;
    z /= f;
    x += this.rand.nextGaussian() * 0.007499999832361937D * inaccuracy;
    y += this.rand.nextGaussian() * 0.007499999832361937D * inaccuracy;
    z += this.rand.nextGaussian() * 0.007499999832361937D * inaccuracy;
    x *= velocity;
    y *= velocity;
    z *= velocity;
    this.motionX = x;
    this.motionY = y;
    this.motionZ = z;
    float f1 = MathHelper.sqrt(x * x + z * z);
    this.rotationYaw = (float)(MathHelper.atan2(x, z) * 57.29577951308232D);
    this.rotationPitch = (float)(MathHelper.atan2(y, f1) * 57.29577951308232D);
    this.prevRotationYaw = this.rotationYaw;
    this.prevRotationPitch = this.rotationPitch;
  }
  
  public void func_190536_a(RayTraceResult p_190536_1_) {
    if (p_190536_1_.entityHit != null && this.field_190539_a != null)
      p_190536_1_.entityHit.attackEntityFrom(DamageSource.causeIndirectDamage(this, (EntityLivingBase)this.field_190539_a).setProjectile(), 1.0F); 
    if (!this.world.isRemote)
      setDead(); 
  }
  
  protected void entityInit() {}
  
  protected void readEntityFromNBT(NBTTagCompound compound) {
    if (compound.hasKey("Owner", 10))
      this.field_190540_b = compound.getCompoundTag("Owner"); 
  }
  
  protected void writeEntityToNBT(NBTTagCompound compound) {
    if (this.field_190539_a != null) {
      NBTTagCompound nbttagcompound = new NBTTagCompound();
      UUID uuid = this.field_190539_a.getUniqueID();
      nbttagcompound.setUniqueId("OwnerUUID", uuid);
      compound.setTag("Owner", (NBTBase)nbttagcompound);
    } 
  }
  
  private void func_190537_j() {
    if (this.field_190540_b != null && this.field_190540_b.hasUniqueId("OwnerUUID")) {
      UUID uuid = this.field_190540_b.getUniqueId("OwnerUUID");
      for (EntityLlama entityllama : this.world.getEntitiesWithinAABB(EntityLlama.class, getEntityBoundingBox().expandXyz(15.0D))) {
        if (entityllama.getUniqueID().equals(uuid)) {
          this.field_190539_a = entityllama;
          break;
        } 
      } 
    } 
    this.field_190540_b = null;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\entity\projectile\EntityLlamaSpit.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
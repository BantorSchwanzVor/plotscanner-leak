package net.minecraft.entity.projectile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public abstract class EntityFireball extends Entity {
  public EntityLivingBase shootingEntity;
  
  private int ticksAlive;
  
  private int ticksInAir;
  
  public double accelerationX;
  
  public double accelerationY;
  
  public double accelerationZ;
  
  public EntityFireball(World worldIn) {
    super(worldIn);
    setSize(1.0F, 1.0F);
  }
  
  protected void entityInit() {}
  
  public boolean isInRangeToRenderDist(double distance) {
    double d0 = getEntityBoundingBox().getAverageEdgeLength() * 4.0D;
    if (Double.isNaN(d0))
      d0 = 4.0D; 
    d0 *= 64.0D;
    return (distance < d0 * d0);
  }
  
  public EntityFireball(World worldIn, double x, double y, double z, double accelX, double accelY, double accelZ) {
    super(worldIn);
    setSize(1.0F, 1.0F);
    setLocationAndAngles(x, y, z, this.rotationYaw, this.rotationPitch);
    setPosition(x, y, z);
    double d0 = MathHelper.sqrt(accelX * accelX + accelY * accelY + accelZ * accelZ);
    this.accelerationX = accelX / d0 * 0.1D;
    this.accelerationY = accelY / d0 * 0.1D;
    this.accelerationZ = accelZ / d0 * 0.1D;
  }
  
  public EntityFireball(World worldIn, EntityLivingBase shooter, double accelX, double accelY, double accelZ) {
    super(worldIn);
    this.shootingEntity = shooter;
    setSize(1.0F, 1.0F);
    setLocationAndAngles(shooter.posX, shooter.posY, shooter.posZ, shooter.rotationYaw, shooter.rotationPitch);
    setPosition(this.posX, this.posY, this.posZ);
    this.motionX = 0.0D;
    this.motionY = 0.0D;
    this.motionZ = 0.0D;
    accelX += this.rand.nextGaussian() * 0.4D;
    accelY += this.rand.nextGaussian() * 0.4D;
    accelZ += this.rand.nextGaussian() * 0.4D;
    double d0 = MathHelper.sqrt(accelX * accelX + accelY * accelY + accelZ * accelZ);
    this.accelerationX = accelX / d0 * 0.1D;
    this.accelerationY = accelY / d0 * 0.1D;
    this.accelerationZ = accelZ / d0 * 0.1D;
  }
  
  public void onUpdate() {
    if (this.world.isRemote || ((this.shootingEntity == null || !this.shootingEntity.isDead) && this.world.isBlockLoaded(new BlockPos(this)))) {
      super.onUpdate();
      if (isFireballFiery())
        setFire(1); 
      this.ticksInAir++;
      RayTraceResult raytraceresult = ProjectileHelper.forwardsRaycast(this, true, (this.ticksInAir >= 25), (Entity)this.shootingEntity);
      if (raytraceresult != null)
        onImpact(raytraceresult); 
      this.posX += this.motionX;
      this.posY += this.motionY;
      this.posZ += this.motionZ;
      ProjectileHelper.rotateTowardsMovement(this, 0.2F);
      float f = getMotionFactor();
      if (isInWater()) {
        for (int i = 0; i < 4; i++) {
          float f1 = 0.25F;
          this.world.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX - this.motionX * 0.25D, this.posY - this.motionY * 0.25D, this.posZ - this.motionZ * 0.25D, this.motionX, this.motionY, this.motionZ, new int[0]);
        } 
        f = 0.8F;
      } 
      this.motionX += this.accelerationX;
      this.motionY += this.accelerationY;
      this.motionZ += this.accelerationZ;
      this.motionX *= f;
      this.motionY *= f;
      this.motionZ *= f;
      this.world.spawnParticle(getParticleType(), this.posX, this.posY + 0.5D, this.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
      setPosition(this.posX, this.posY, this.posZ);
    } else {
      setDead();
    } 
  }
  
  protected boolean isFireballFiery() {
    return true;
  }
  
  protected EnumParticleTypes getParticleType() {
    return EnumParticleTypes.SMOKE_NORMAL;
  }
  
  protected float getMotionFactor() {
    return 0.95F;
  }
  
  protected abstract void onImpact(RayTraceResult paramRayTraceResult);
  
  public static void registerFixesFireball(DataFixer fixer, String name) {}
  
  public void writeEntityToNBT(NBTTagCompound compound) {
    compound.setTag("direction", (NBTBase)newDoubleNBTList(new double[] { this.motionX, this.motionY, this.motionZ }));
    compound.setTag("power", (NBTBase)newDoubleNBTList(new double[] { this.accelerationX, this.accelerationY, this.accelerationZ }));
    compound.setInteger("life", this.ticksAlive);
  }
  
  public void readEntityFromNBT(NBTTagCompound compound) {
    if (compound.hasKey("power", 9)) {
      NBTTagList nbttaglist = compound.getTagList("power", 6);
      if (nbttaglist.tagCount() == 3) {
        this.accelerationX = nbttaglist.getDoubleAt(0);
        this.accelerationY = nbttaglist.getDoubleAt(1);
        this.accelerationZ = nbttaglist.getDoubleAt(2);
      } 
    } 
    this.ticksAlive = compound.getInteger("life");
    if (compound.hasKey("direction", 9) && compound.getTagList("direction", 6).tagCount() == 3) {
      NBTTagList nbttaglist1 = compound.getTagList("direction", 6);
      this.motionX = nbttaglist1.getDoubleAt(0);
      this.motionY = nbttaglist1.getDoubleAt(1);
      this.motionZ = nbttaglist1.getDoubleAt(2);
    } else {
      setDead();
    } 
  }
  
  public boolean canBeCollidedWith() {
    return true;
  }
  
  public float getCollisionBorderSize() {
    return 1.0F;
  }
  
  public boolean attackEntityFrom(DamageSource source, float amount) {
    if (isEntityInvulnerable(source))
      return false; 
    setBeenAttacked();
    if (source.getEntity() != null) {
      Vec3d vec3d = source.getEntity().getLookVec();
      if (vec3d != null) {
        this.motionX = vec3d.xCoord;
        this.motionY = vec3d.yCoord;
        this.motionZ = vec3d.zCoord;
        this.accelerationX = this.motionX * 0.1D;
        this.accelerationY = this.motionY * 0.1D;
        this.accelerationZ = this.motionZ * 0.1D;
      } 
      if (source.getEntity() instanceof EntityLivingBase)
        this.shootingEntity = (EntityLivingBase)source.getEntity(); 
      return true;
    } 
    return false;
  }
  
  public float getBrightness() {
    return 1.0F;
  }
  
  public int getBrightnessForRender() {
    return 15728880;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\entity\projectile\EntityFireball.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
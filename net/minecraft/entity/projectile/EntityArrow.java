package net.minecraft.entity.projectile;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Enchantments;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SPacketChangeGameState;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class EntityArrow extends Entity implements IProjectile {
  private static final Predicate<Entity> ARROW_TARGETS = Predicates.and(new Predicate[] { EntitySelectors.NOT_SPECTATING, EntitySelectors.IS_ALIVE, new Predicate<Entity>() {
          public boolean apply(@Nullable Entity p_apply_1_) {
            return p_apply_1_.canBeCollidedWith();
          }
        } });
  
  private static final DataParameter<Byte> CRITICAL = EntityDataManager.createKey(EntityArrow.class, DataSerializers.BYTE);
  
  private int xTile;
  
  private int yTile;
  
  private int zTile;
  
  private Block inTile;
  
  private int inData;
  
  protected boolean inGround;
  
  protected int timeInGround;
  
  public PickupStatus pickupStatus;
  
  public int arrowShake;
  
  public Entity shootingEntity;
  
  private int ticksInGround;
  
  private int ticksInAir;
  
  private double damage;
  
  private int knockbackStrength;
  
  public EntityArrow(World worldIn) {
    super(worldIn);
    this.xTile = -1;
    this.yTile = -1;
    this.zTile = -1;
    this.pickupStatus = PickupStatus.DISALLOWED;
    this.damage = 2.0D;
    setSize(0.5F, 0.5F);
  }
  
  public EntityArrow(World worldIn, double x, double y, double z) {
    this(worldIn);
    setPosition(x, y, z);
  }
  
  public EntityArrow(World worldIn, EntityLivingBase shooter) {
    this(worldIn, shooter.posX, shooter.posY + shooter.getEyeHeight() - 0.10000000149011612D, shooter.posZ);
    this.shootingEntity = (Entity)shooter;
    if (shooter instanceof EntityPlayer)
      this.pickupStatus = PickupStatus.ALLOWED; 
  }
  
  public boolean isInRangeToRenderDist(double distance) {
    double d0 = getEntityBoundingBox().getAverageEdgeLength() * 10.0D;
    if (Double.isNaN(d0))
      d0 = 1.0D; 
    d0 = d0 * 64.0D * getRenderDistanceWeight();
    return (distance < d0 * d0);
  }
  
  protected void entityInit() {
    this.dataManager.register(CRITICAL, Byte.valueOf((byte)0));
  }
  
  public void setAim(Entity shooter, float pitch, float yaw, float p_184547_4_, float velocity, float inaccuracy) {
    float f = -MathHelper.sin(yaw * 0.017453292F) * MathHelper.cos(pitch * 0.017453292F);
    float f1 = -MathHelper.sin(pitch * 0.017453292F);
    float f2 = MathHelper.cos(yaw * 0.017453292F) * MathHelper.cos(pitch * 0.017453292F);
    setThrowableHeading(f, f1, f2, velocity, inaccuracy);
    this.motionX += shooter.motionX;
    this.motionZ += shooter.motionZ;
    if (!shooter.onGround)
      this.motionY += shooter.motionY; 
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
    this.ticksInGround = 0;
  }
  
  public void setPositionAndRotationDirect(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean teleport) {
    setPosition(x, y, z);
    setRotation(yaw, pitch);
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
      this.ticksInGround = 0;
    } 
  }
  
  public void onUpdate() {
    super.onUpdate();
    if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
      float f = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
      this.rotationYaw = (float)(MathHelper.atan2(this.motionX, this.motionZ) * 57.29577951308232D);
      this.rotationPitch = (float)(MathHelper.atan2(this.motionY, f) * 57.29577951308232D);
      this.prevRotationYaw = this.rotationYaw;
      this.prevRotationPitch = this.rotationPitch;
    } 
    BlockPos blockpos = new BlockPos(this.xTile, this.yTile, this.zTile);
    IBlockState iblockstate = this.world.getBlockState(blockpos);
    Block block = iblockstate.getBlock();
    if (iblockstate.getMaterial() != Material.AIR) {
      AxisAlignedBB axisalignedbb = iblockstate.getCollisionBoundingBox((IBlockAccess)this.world, blockpos);
      if (axisalignedbb != Block.NULL_AABB && axisalignedbb.offset(blockpos).isVecInside(new Vec3d(this.posX, this.posY, this.posZ)))
        this.inGround = true; 
    } 
    if (this.arrowShake > 0)
      this.arrowShake--; 
    if (this.inGround) {
      int j = block.getMetaFromState(iblockstate);
      if ((block != this.inTile || j != this.inData) && !this.world.collidesWithAnyBlock(getEntityBoundingBox().expandXyz(0.05D))) {
        this.inGround = false;
        this.motionX *= (this.rand.nextFloat() * 0.2F);
        this.motionY *= (this.rand.nextFloat() * 0.2F);
        this.motionZ *= (this.rand.nextFloat() * 0.2F);
        this.ticksInGround = 0;
        this.ticksInAir = 0;
      } else {
        this.ticksInGround++;
        if (this.ticksInGround >= 1200)
          setDead(); 
      } 
      this.timeInGround++;
    } else {
      this.timeInGround = 0;
      this.ticksInAir++;
      Vec3d vec3d1 = new Vec3d(this.posX, this.posY, this.posZ);
      Vec3d vec3d = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
      RayTraceResult raytraceresult = this.world.rayTraceBlocks(vec3d1, vec3d, false, true, false);
      vec3d1 = new Vec3d(this.posX, this.posY, this.posZ);
      vec3d = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
      if (raytraceresult != null)
        vec3d = new Vec3d(raytraceresult.hitVec.xCoord, raytraceresult.hitVec.yCoord, raytraceresult.hitVec.zCoord); 
      Entity entity = findEntityOnPath(vec3d1, vec3d);
      if (entity != null)
        raytraceresult = new RayTraceResult(entity); 
      if (raytraceresult != null && raytraceresult.entityHit instanceof EntityPlayer) {
        EntityPlayer entityplayer = (EntityPlayer)raytraceresult.entityHit;
        if (this.shootingEntity instanceof EntityPlayer && !((EntityPlayer)this.shootingEntity).canAttackPlayer(entityplayer))
          raytraceresult = null; 
      } 
      if (raytraceresult != null)
        onHit(raytraceresult); 
      if (getIsCritical())
        for (int k = 0; k < 4; k++)
          this.world.spawnParticle(EnumParticleTypes.CRIT, this.posX + this.motionX * k / 4.0D, this.posY + this.motionY * k / 4.0D, this.posZ + this.motionZ * k / 4.0D, -this.motionX, -this.motionY + 0.2D, -this.motionZ, new int[0]);  
      this.posX += this.motionX;
      this.posY += this.motionY;
      this.posZ += this.motionZ;
      float f4 = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
      this.rotationYaw = (float)(MathHelper.atan2(this.motionX, this.motionZ) * 57.29577951308232D);
      for (this.rotationPitch = (float)(MathHelper.atan2(this.motionY, f4) * 57.29577951308232D); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F);
      while (this.rotationPitch - this.prevRotationPitch >= 180.0F)
        this.prevRotationPitch += 360.0F; 
      while (this.rotationYaw - this.prevRotationYaw < -180.0F)
        this.prevRotationYaw -= 360.0F; 
      while (this.rotationYaw - this.prevRotationYaw >= 180.0F)
        this.prevRotationYaw += 360.0F; 
      this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
      this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;
      float f1 = 0.99F;
      float f2 = 0.05F;
      if (isInWater()) {
        for (int i = 0; i < 4; i++) {
          float f3 = 0.25F;
          this.world.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX - this.motionX * 0.25D, this.posY - this.motionY * 0.25D, this.posZ - this.motionZ * 0.25D, this.motionX, this.motionY, this.motionZ, new int[0]);
        } 
        f1 = 0.6F;
      } 
      if (isWet())
        extinguish(); 
      this.motionX *= f1;
      this.motionY *= f1;
      this.motionZ *= f1;
      if (!hasNoGravity())
        this.motionY -= 0.05000000074505806D; 
      setPosition(this.posX, this.posY, this.posZ);
      doBlockCollisions();
    } 
  }
  
  protected void onHit(RayTraceResult raytraceResultIn) {
    Entity entity = raytraceResultIn.entityHit;
    if (entity != null) {
      DamageSource damagesource;
      float f = MathHelper.sqrt(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
      int i = MathHelper.ceil(f * this.damage);
      if (getIsCritical())
        i += this.rand.nextInt(i / 2 + 2); 
      if (this.shootingEntity == null) {
        damagesource = DamageSource.causeArrowDamage(this, this);
      } else {
        damagesource = DamageSource.causeArrowDamage(this, this.shootingEntity);
      } 
      if (isBurning() && !(entity instanceof net.minecraft.entity.monster.EntityEnderman))
        entity.setFire(5); 
      if (entity.attackEntityFrom(damagesource, i)) {
        if (entity instanceof EntityLivingBase) {
          EntityLivingBase entitylivingbase = (EntityLivingBase)entity;
          if (!this.world.isRemote)
            entitylivingbase.setArrowCountInEntity(entitylivingbase.getArrowCountInEntity() + 1); 
          if (this.knockbackStrength > 0) {
            float f1 = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
            if (f1 > 0.0F)
              entitylivingbase.addVelocity(this.motionX * this.knockbackStrength * 0.6000000238418579D / f1, 0.1D, this.motionZ * this.knockbackStrength * 0.6000000238418579D / f1); 
          } 
          if (this.shootingEntity instanceof EntityLivingBase) {
            EnchantmentHelper.applyThornEnchantments(entitylivingbase, this.shootingEntity);
            EnchantmentHelper.applyArthropodEnchantments((EntityLivingBase)this.shootingEntity, (Entity)entitylivingbase);
          } 
          arrowHit(entitylivingbase);
          if (this.shootingEntity != null && entitylivingbase != this.shootingEntity && entitylivingbase instanceof EntityPlayer && this.shootingEntity instanceof EntityPlayerMP)
            ((EntityPlayerMP)this.shootingEntity).connection.sendPacket((Packet)new SPacketChangeGameState(6, 0.0F)); 
        } 
        playSound(SoundEvents.ENTITY_ARROW_HIT, 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
        if (!(entity instanceof net.minecraft.entity.monster.EntityEnderman))
          setDead(); 
      } else {
        this.motionX *= -0.10000000149011612D;
        this.motionY *= -0.10000000149011612D;
        this.motionZ *= -0.10000000149011612D;
        this.rotationYaw += 180.0F;
        this.prevRotationYaw += 180.0F;
        this.ticksInAir = 0;
        if (!this.world.isRemote && this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ < 0.0010000000474974513D) {
          if (this.pickupStatus == PickupStatus.ALLOWED)
            entityDropItem(getArrowStack(), 0.1F); 
          setDead();
        } 
      } 
    } else {
      BlockPos blockpos = raytraceResultIn.getBlockPos();
      this.xTile = blockpos.getX();
      this.yTile = blockpos.getY();
      this.zTile = blockpos.getZ();
      IBlockState iblockstate = this.world.getBlockState(blockpos);
      this.inTile = iblockstate.getBlock();
      this.inData = this.inTile.getMetaFromState(iblockstate);
      this.motionX = (float)(raytraceResultIn.hitVec.xCoord - this.posX);
      this.motionY = (float)(raytraceResultIn.hitVec.yCoord - this.posY);
      this.motionZ = (float)(raytraceResultIn.hitVec.zCoord - this.posZ);
      float f2 = MathHelper.sqrt(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
      this.posX -= this.motionX / f2 * 0.05000000074505806D;
      this.posY -= this.motionY / f2 * 0.05000000074505806D;
      this.posZ -= this.motionZ / f2 * 0.05000000074505806D;
      playSound(SoundEvents.ENTITY_ARROW_HIT, 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
      this.inGround = true;
      this.arrowShake = 7;
      setIsCritical(false);
      if (iblockstate.getMaterial() != Material.AIR)
        this.inTile.onEntityCollidedWithBlock(this.world, blockpos, iblockstate, this); 
    } 
  }
  
  public void moveEntity(MoverType x, double p_70091_2_, double p_70091_4_, double p_70091_6_) {
    super.moveEntity(x, p_70091_2_, p_70091_4_, p_70091_6_);
    if (this.inGround) {
      this.xTile = MathHelper.floor(this.posX);
      this.yTile = MathHelper.floor(this.posY);
      this.zTile = MathHelper.floor(this.posZ);
    } 
  }
  
  protected void arrowHit(EntityLivingBase living) {}
  
  @Nullable
  protected Entity findEntityOnPath(Vec3d start, Vec3d end) {
    Entity entity = null;
    List<Entity> list = this.world.getEntitiesInAABBexcluding(this, getEntityBoundingBox().addCoord(this.motionX, this.motionY, this.motionZ).expandXyz(1.0D), ARROW_TARGETS);
    double d0 = 0.0D;
    for (int i = 0; i < list.size(); i++) {
      Entity entity1 = list.get(i);
      if (entity1 != this.shootingEntity || this.ticksInAir >= 5) {
        AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().expandXyz(0.30000001192092896D);
        RayTraceResult raytraceresult = axisalignedbb.calculateIntercept(start, end);
        if (raytraceresult != null) {
          double d1 = start.squareDistanceTo(raytraceresult.hitVec);
          if (d1 < d0 || d0 == 0.0D) {
            entity = entity1;
            d0 = d1;
          } 
        } 
      } 
    } 
    return entity;
  }
  
  public static void registerFixesArrow(DataFixer fixer, String name) {}
  
  public static void registerFixesArrow(DataFixer fixer) {
    registerFixesArrow(fixer, "Arrow");
  }
  
  public void writeEntityToNBT(NBTTagCompound compound) {
    compound.setInteger("xTile", this.xTile);
    compound.setInteger("yTile", this.yTile);
    compound.setInteger("zTile", this.zTile);
    compound.setShort("life", (short)this.ticksInGround);
    ResourceLocation resourcelocation = (ResourceLocation)Block.REGISTRY.getNameForObject(this.inTile);
    compound.setString("inTile", (resourcelocation == null) ? "" : resourcelocation.toString());
    compound.setByte("inData", (byte)this.inData);
    compound.setByte("shake", (byte)this.arrowShake);
    compound.setByte("inGround", (byte)(this.inGround ? 1 : 0));
    compound.setByte("pickup", (byte)this.pickupStatus.ordinal());
    compound.setDouble("damage", this.damage);
    compound.setBoolean("crit", getIsCritical());
  }
  
  public void readEntityFromNBT(NBTTagCompound compound) {
    this.xTile = compound.getInteger("xTile");
    this.yTile = compound.getInteger("yTile");
    this.zTile = compound.getInteger("zTile");
    this.ticksInGround = compound.getShort("life");
    if (compound.hasKey("inTile", 8)) {
      this.inTile = Block.getBlockFromName(compound.getString("inTile"));
    } else {
      this.inTile = Block.getBlockById(compound.getByte("inTile") & 0xFF);
    } 
    this.inData = compound.getByte("inData") & 0xFF;
    this.arrowShake = compound.getByte("shake") & 0xFF;
    this.inGround = (compound.getByte("inGround") == 1);
    if (compound.hasKey("damage", 99))
      this.damage = compound.getDouble("damage"); 
    if (compound.hasKey("pickup", 99)) {
      this.pickupStatus = PickupStatus.getByOrdinal(compound.getByte("pickup"));
    } else if (compound.hasKey("player", 99)) {
      this.pickupStatus = compound.getBoolean("player") ? PickupStatus.ALLOWED : PickupStatus.DISALLOWED;
    } 
    setIsCritical(compound.getBoolean("crit"));
  }
  
  public void onCollideWithPlayer(EntityPlayer entityIn) {
    if (!this.world.isRemote && this.inGround && this.arrowShake <= 0) {
      boolean flag = !(this.pickupStatus != PickupStatus.ALLOWED && (this.pickupStatus != PickupStatus.CREATIVE_ONLY || !entityIn.capabilities.isCreativeMode));
      if (this.pickupStatus == PickupStatus.ALLOWED && !entityIn.inventory.addItemStackToInventory(getArrowStack()))
        flag = false; 
      if (flag) {
        entityIn.onItemPickup(this, 1);
        setDead();
      } 
    } 
  }
  
  protected abstract ItemStack getArrowStack();
  
  protected boolean canTriggerWalking() {
    return false;
  }
  
  public void setDamage(double damageIn) {
    this.damage = damageIn;
  }
  
  public double getDamage() {
    return this.damage;
  }
  
  public void setKnockbackStrength(int knockbackStrengthIn) {
    this.knockbackStrength = knockbackStrengthIn;
  }
  
  public boolean canBeAttackedWithItem() {
    return false;
  }
  
  public float getEyeHeight() {
    return 0.0F;
  }
  
  public void setIsCritical(boolean critical) {
    byte b0 = ((Byte)this.dataManager.get(CRITICAL)).byteValue();
    if (critical) {
      this.dataManager.set(CRITICAL, Byte.valueOf((byte)(b0 | 0x1)));
    } else {
      this.dataManager.set(CRITICAL, Byte.valueOf((byte)(b0 & 0xFFFFFFFE)));
    } 
  }
  
  public boolean getIsCritical() {
    byte b0 = ((Byte)this.dataManager.get(CRITICAL)).byteValue();
    return ((b0 & 0x1) != 0);
  }
  
  public void func_190547_a(EntityLivingBase p_190547_1_, float p_190547_2_) {
    int i = EnchantmentHelper.getMaxEnchantmentLevel(Enchantments.POWER, p_190547_1_);
    int j = EnchantmentHelper.getMaxEnchantmentLevel(Enchantments.PUNCH, p_190547_1_);
    setDamage((p_190547_2_ * 2.0F) + this.rand.nextGaussian() * 0.25D + (this.world.getDifficulty().getDifficultyId() * 0.11F));
    if (i > 0)
      setDamage(getDamage() + i * 0.5D + 0.5D); 
    if (j > 0)
      setKnockbackStrength(j); 
    if (EnchantmentHelper.getMaxEnchantmentLevel(Enchantments.FLAME, p_190547_1_) > 0)
      setFire(100); 
  }
  
  public enum PickupStatus {
    DISALLOWED, ALLOWED, CREATIVE_ONLY;
    
    public static PickupStatus getByOrdinal(int ordinal) {
      if (ordinal < 0 || ordinal > (values()).length)
        ordinal = 0; 
      return values()[ordinal];
    }
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\entity\projectile\EntityArrow.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
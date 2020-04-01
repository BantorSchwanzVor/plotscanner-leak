package net.minecraft.entity.monster;

import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityBlaze extends EntityMob {
  private float heightOffset = 0.5F;
  
  private int heightOffsetUpdateTime;
  
  private static final DataParameter<Byte> ON_FIRE = EntityDataManager.createKey(EntityBlaze.class, DataSerializers.BYTE);
  
  public EntityBlaze(World worldIn) {
    super(worldIn);
    setPathPriority(PathNodeType.WATER, -1.0F);
    setPathPriority(PathNodeType.LAVA, 8.0F);
    setPathPriority(PathNodeType.DANGER_FIRE, 0.0F);
    setPathPriority(PathNodeType.DAMAGE_FIRE, 0.0F);
    this.isImmuneToFire = true;
    this.experienceValue = 10;
  }
  
  public static void registerFixesBlaze(DataFixer fixer) {
    EntityLiving.registerFixesMob(fixer, EntityBlaze.class);
  }
  
  protected void initEntityAI() {
    this.tasks.addTask(4, new AIFireballAttack(this));
    this.tasks.addTask(5, (EntityAIBase)new EntityAIMoveTowardsRestriction(this, 1.0D));
    this.tasks.addTask(7, (EntityAIBase)new EntityAIWanderAvoidWater(this, 1.0D, 0.0F));
    this.tasks.addTask(8, (EntityAIBase)new EntityAIWatchClosest((EntityLiving)this, EntityPlayer.class, 8.0F));
    this.tasks.addTask(8, (EntityAIBase)new EntityAILookIdle((EntityLiving)this));
    this.targetTasks.addTask(1, (EntityAIBase)new EntityAIHurtByTarget(this, true, new Class[0]));
    this.targetTasks.addTask(2, (EntityAIBase)new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
  }
  
  protected void applyEntityAttributes() {
    super.applyEntityAttributes();
    getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(6.0D);
    getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.23000000417232513D);
    getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(48.0D);
  }
  
  protected void entityInit() {
    super.entityInit();
    this.dataManager.register(ON_FIRE, Byte.valueOf((byte)0));
  }
  
  protected SoundEvent getAmbientSound() {
    return SoundEvents.ENTITY_BLAZE_AMBIENT;
  }
  
  protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
    return SoundEvents.ENTITY_BLAZE_HURT;
  }
  
  protected SoundEvent getDeathSound() {
    return SoundEvents.ENTITY_BLAZE_DEATH;
  }
  
  public int getBrightnessForRender() {
    return 15728880;
  }
  
  public float getBrightness() {
    return 1.0F;
  }
  
  public void onLivingUpdate() {
    if (!this.onGround && this.motionY < 0.0D)
      this.motionY *= 0.6D; 
    if (this.world.isRemote) {
      if (this.rand.nextInt(24) == 0 && !isSilent())
        this.world.playSound(this.posX + 0.5D, this.posY + 0.5D, this.posZ + 0.5D, SoundEvents.ENTITY_BLAZE_BURN, getSoundCategory(), 1.0F + this.rand.nextFloat(), this.rand.nextFloat() * 0.7F + 0.3F, false); 
      for (int i = 0; i < 2; i++)
        this.world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, this.posX + (this.rand.nextDouble() - 0.5D) * this.width, this.posY + this.rand.nextDouble() * this.height, this.posZ + (this.rand.nextDouble() - 0.5D) * this.width, 0.0D, 0.0D, 0.0D, new int[0]); 
    } 
    super.onLivingUpdate();
  }
  
  protected void updateAITasks() {
    if (isWet())
      attackEntityFrom(DamageSource.drown, 1.0F); 
    this.heightOffsetUpdateTime--;
    if (this.heightOffsetUpdateTime <= 0) {
      this.heightOffsetUpdateTime = 100;
      this.heightOffset = 0.5F + (float)this.rand.nextGaussian() * 3.0F;
    } 
    EntityLivingBase entitylivingbase = getAttackTarget();
    if (entitylivingbase != null && entitylivingbase.posY + entitylivingbase.getEyeHeight() > this.posY + getEyeHeight() + this.heightOffset) {
      this.motionY += (0.30000001192092896D - this.motionY) * 0.30000001192092896D;
      this.isAirBorne = true;
    } 
    super.updateAITasks();
  }
  
  public void fall(float distance, float damageMultiplier) {}
  
  public boolean isBurning() {
    return isCharged();
  }
  
  @Nullable
  protected ResourceLocation getLootTable() {
    return LootTableList.ENTITIES_BLAZE;
  }
  
  public boolean isCharged() {
    return ((((Byte)this.dataManager.get(ON_FIRE)).byteValue() & 0x1) != 0);
  }
  
  public void setOnFire(boolean onFire) {
    byte b0 = ((Byte)this.dataManager.get(ON_FIRE)).byteValue();
    if (onFire) {
      b0 = (byte)(b0 | 0x1);
    } else {
      b0 = (byte)(b0 & 0xFFFFFFFE);
    } 
    this.dataManager.set(ON_FIRE, Byte.valueOf(b0));
  }
  
  protected boolean isValidLightLevel() {
    return true;
  }
  
  static class AIFireballAttack extends EntityAIBase {
    private final EntityBlaze blaze;
    
    private int attackStep;
    
    private int attackTime;
    
    public AIFireballAttack(EntityBlaze blazeIn) {
      this.blaze = blazeIn;
      setMutexBits(3);
    }
    
    public boolean shouldExecute() {
      EntityLivingBase entitylivingbase = this.blaze.getAttackTarget();
      return (entitylivingbase != null && entitylivingbase.isEntityAlive());
    }
    
    public void startExecuting() {
      this.attackStep = 0;
    }
    
    public void resetTask() {
      this.blaze.setOnFire(false);
    }
    
    public void updateTask() {
      this.attackTime--;
      EntityLivingBase entitylivingbase = this.blaze.getAttackTarget();
      double d0 = this.blaze.getDistanceSqToEntity((Entity)entitylivingbase);
      if (d0 < 4.0D) {
        if (this.attackTime <= 0) {
          this.attackTime = 20;
          this.blaze.attackEntityAsMob((Entity)entitylivingbase);
        } 
        this.blaze.getMoveHelper().setMoveTo(entitylivingbase.posX, entitylivingbase.posY, entitylivingbase.posZ, 1.0D);
      } else if (d0 < func_191523_f() * func_191523_f()) {
        double d1 = entitylivingbase.posX - this.blaze.posX;
        double d2 = (entitylivingbase.getEntityBoundingBox()).minY + (entitylivingbase.height / 2.0F) - this.blaze.posY + (this.blaze.height / 2.0F);
        double d3 = entitylivingbase.posZ - this.blaze.posZ;
        if (this.attackTime <= 0) {
          this.attackStep++;
          if (this.attackStep == 1) {
            this.attackTime = 60;
            this.blaze.setOnFire(true);
          } else if (this.attackStep <= 4) {
            this.attackTime = 6;
          } else {
            this.attackTime = 100;
            this.attackStep = 0;
            this.blaze.setOnFire(false);
          } 
          if (this.attackStep > 1) {
            float f = MathHelper.sqrt(MathHelper.sqrt(d0)) * 0.5F;
            this.blaze.world.playEvent(null, 1018, new BlockPos((int)this.blaze.posX, (int)this.blaze.posY, (int)this.blaze.posZ), 0);
            for (int i = 0; i < 1; i++) {
              EntitySmallFireball entitysmallfireball = new EntitySmallFireball(this.blaze.world, (EntityLivingBase)this.blaze, d1 + this.blaze.getRNG().nextGaussian() * f, d2, d3 + this.blaze.getRNG().nextGaussian() * f);
              entitysmallfireball.posY = this.blaze.posY + (this.blaze.height / 2.0F) + 0.5D;
              this.blaze.world.spawnEntityInWorld((Entity)entitysmallfireball);
            } 
          } 
        } 
        this.blaze.getLookHelper().setLookPositionWithEntity((Entity)entitylivingbase, 10.0F, 10.0F);
      } else {
        this.blaze.getNavigator().clearPathEntity();
        this.blaze.getMoveHelper().setMoveTo(entitylivingbase.posX, entitylivingbase.posY, entitylivingbase.posZ, 1.0D);
      } 
      super.updateTask();
    }
    
    private double func_191523_f() {
      IAttributeInstance iattributeinstance = this.blaze.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE);
      return (iattributeinstance == null) ? 16.0D : iattributeinstance.getAttributeValue();
    }
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\entity\monster\EntityBlaze.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
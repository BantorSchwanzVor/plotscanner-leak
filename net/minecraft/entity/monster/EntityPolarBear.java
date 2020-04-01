package net.minecraft.entity.monster;

import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityPolarBear extends EntityAnimal {
  private static final DataParameter<Boolean> IS_STANDING = EntityDataManager.createKey(EntityPolarBear.class, DataSerializers.BOOLEAN);
  
  private float clientSideStandAnimation0;
  
  private float clientSideStandAnimation;
  
  private int warningSoundTicks;
  
  public EntityPolarBear(World worldIn) {
    super(worldIn);
    setSize(1.3F, 1.4F);
  }
  
  public EntityAgeable createChild(EntityAgeable ageable) {
    return (EntityAgeable)new EntityPolarBear(this.world);
  }
  
  public boolean isBreedingItem(ItemStack stack) {
    return false;
  }
  
  protected void initEntityAI() {
    super.initEntityAI();
    this.tasks.addTask(0, (EntityAIBase)new EntityAISwimming((EntityLiving)this));
    this.tasks.addTask(1, (EntityAIBase)new AIMeleeAttack());
    this.tasks.addTask(1, (EntityAIBase)new AIPanic());
    this.tasks.addTask(4, (EntityAIBase)new EntityAIFollowParent(this, 1.25D));
    this.tasks.addTask(5, (EntityAIBase)new EntityAIWander((EntityCreature)this, 1.0D));
    this.tasks.addTask(6, (EntityAIBase)new EntityAIWatchClosest((EntityLiving)this, EntityPlayer.class, 6.0F));
    this.tasks.addTask(7, (EntityAIBase)new EntityAILookIdle((EntityLiving)this));
    this.targetTasks.addTask(1, (EntityAIBase)new AIHurtByTarget());
    this.targetTasks.addTask(2, (EntityAIBase)new AIAttackPlayer());
  }
  
  protected void applyEntityAttributes() {
    super.applyEntityAttributes();
    getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30.0D);
    getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(20.0D);
    getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
    getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
    getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(6.0D);
  }
  
  protected SoundEvent getAmbientSound() {
    return isChild() ? SoundEvents.ENTITY_POLAR_BEAR_BABY_AMBIENT : SoundEvents.ENTITY_POLAR_BEAR_AMBIENT;
  }
  
  protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
    return SoundEvents.ENTITY_POLAR_BEAR_HURT;
  }
  
  protected SoundEvent getDeathSound() {
    return SoundEvents.ENTITY_POLAR_BEAR_DEATH;
  }
  
  protected void playStepSound(BlockPos pos, Block blockIn) {
    playSound(SoundEvents.ENTITY_POLAR_BEAR_STEP, 0.15F, 1.0F);
  }
  
  protected void playWarningSound() {
    if (this.warningSoundTicks <= 0) {
      playSound(SoundEvents.ENTITY_POLAR_BEAR_WARNING, 1.0F, 1.0F);
      this.warningSoundTicks = 40;
    } 
  }
  
  @Nullable
  protected ResourceLocation getLootTable() {
    return LootTableList.ENTITIES_POLAR_BEAR;
  }
  
  protected void entityInit() {
    super.entityInit();
    this.dataManager.register(IS_STANDING, Boolean.valueOf(false));
  }
  
  public void onUpdate() {
    super.onUpdate();
    if (this.world.isRemote) {
      this.clientSideStandAnimation0 = this.clientSideStandAnimation;
      if (isStanding()) {
        this.clientSideStandAnimation = MathHelper.clamp(this.clientSideStandAnimation + 1.0F, 0.0F, 6.0F);
      } else {
        this.clientSideStandAnimation = MathHelper.clamp(this.clientSideStandAnimation - 1.0F, 0.0F, 6.0F);
      } 
    } 
    if (this.warningSoundTicks > 0)
      this.warningSoundTicks--; 
  }
  
  public boolean attackEntityAsMob(Entity entityIn) {
    boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage((EntityLivingBase)this), (int)getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue());
    if (flag)
      applyEnchantments((EntityLivingBase)this, entityIn); 
    return flag;
  }
  
  public boolean isStanding() {
    return ((Boolean)this.dataManager.get(IS_STANDING)).booleanValue();
  }
  
  public void setStanding(boolean standing) {
    this.dataManager.set(IS_STANDING, Boolean.valueOf(standing));
  }
  
  public float getStandingAnimationScale(float p_189795_1_) {
    return (this.clientSideStandAnimation0 + (this.clientSideStandAnimation - this.clientSideStandAnimation0) * p_189795_1_) / 6.0F;
  }
  
  protected float getWaterSlowDown() {
    return 0.98F;
  }
  
  public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata) {
    if (livingdata instanceof GroupData) {
      if (((GroupData)livingdata).madeParent)
        setGrowingAge(-24000); 
    } else {
      GroupData entitypolarbear$groupdata = new GroupData(null);
      entitypolarbear$groupdata.madeParent = true;
      livingdata = entitypolarbear$groupdata;
    } 
    return livingdata;
  }
  
  class AIAttackPlayer extends EntityAINearestAttackableTarget<EntityPlayer> {
    public AIAttackPlayer() {
      super((EntityCreature)EntityPolarBear.this, EntityPlayer.class, 20, true, true, null);
    }
    
    public boolean shouldExecute() {
      if (EntityPolarBear.this.isChild())
        return false; 
      if (super.shouldExecute())
        for (EntityPolarBear entitypolarbear : EntityPolarBear.this.world.getEntitiesWithinAABB(EntityPolarBear.class, EntityPolarBear.this.getEntityBoundingBox().expand(8.0D, 4.0D, 8.0D))) {
          if (entitypolarbear.isChild())
            return true; 
        }  
      EntityPolarBear.this.setAttackTarget(null);
      return false;
    }
    
    protected double getTargetDistance() {
      return super.getTargetDistance() * 0.5D;
    }
  }
  
  class AIHurtByTarget extends EntityAIHurtByTarget {
    public AIHurtByTarget() {
      super((EntityCreature)EntityPolarBear.this, false, new Class[0]);
    }
    
    public void startExecuting() {
      super.startExecuting();
      if (EntityPolarBear.this.isChild()) {
        alertOthers();
        resetTask();
      } 
    }
    
    protected void setEntityAttackTarget(EntityCreature creatureIn, EntityLivingBase entityLivingBaseIn) {
      if (creatureIn instanceof EntityPolarBear && !creatureIn.isChild())
        super.setEntityAttackTarget(creatureIn, entityLivingBaseIn); 
    }
  }
  
  class AIMeleeAttack extends EntityAIAttackMelee {
    public AIMeleeAttack() {
      super((EntityCreature)EntityPolarBear.this, 1.25D, true);
    }
    
    protected void checkAndPerformAttack(EntityLivingBase p_190102_1_, double p_190102_2_) {
      double d0 = getAttackReachSqr(p_190102_1_);
      if (p_190102_2_ <= d0 && this.attackTick <= 0) {
        this.attackTick = 20;
        this.attacker.attackEntityAsMob((Entity)p_190102_1_);
        EntityPolarBear.this.setStanding(false);
      } else if (p_190102_2_ <= d0 * 2.0D) {
        if (this.attackTick <= 0) {
          EntityPolarBear.this.setStanding(false);
          this.attackTick = 20;
        } 
        if (this.attackTick <= 10) {
          EntityPolarBear.this.setStanding(true);
          EntityPolarBear.this.playWarningSound();
        } 
      } else {
        this.attackTick = 20;
        EntityPolarBear.this.setStanding(false);
      } 
    }
    
    public void resetTask() {
      EntityPolarBear.this.setStanding(false);
      super.resetTask();
    }
    
    protected double getAttackReachSqr(EntityLivingBase attackTarget) {
      return (4.0F + attackTarget.width);
    }
  }
  
  class AIPanic extends EntityAIPanic {
    public AIPanic() {
      super((EntityCreature)EntityPolarBear.this, 2.0D);
    }
    
    public boolean shouldExecute() {
      return (!EntityPolarBear.this.isChild() && !EntityPolarBear.this.isBurning()) ? false : super.shouldExecute();
    }
  }
  
  static class GroupData implements IEntityLivingData {
    public boolean madeParent;
    
    private GroupData() {}
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\entity\monster\EntityPolarBear.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
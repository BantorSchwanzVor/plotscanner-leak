package net.minecraft.entity.monster;

import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILeapAtTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateClimber;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntitySpider extends EntityMob {
  private static final DataParameter<Byte> CLIMBING = EntityDataManager.createKey(EntitySpider.class, DataSerializers.BYTE);
  
  public EntitySpider(World worldIn) {
    super(worldIn);
    setSize(1.4F, 0.9F);
  }
  
  public static void registerFixesSpider(DataFixer fixer) {
    EntityLiving.registerFixesMob(fixer, EntitySpider.class);
  }
  
  protected void initEntityAI() {
    this.tasks.addTask(1, (EntityAIBase)new EntityAISwimming((EntityLiving)this));
    this.tasks.addTask(3, (EntityAIBase)new EntityAILeapAtTarget((EntityLiving)this, 0.4F));
    this.tasks.addTask(4, (EntityAIBase)new AISpiderAttack(this));
    this.tasks.addTask(5, (EntityAIBase)new EntityAIWanderAvoidWater(this, 0.8D));
    this.tasks.addTask(6, (EntityAIBase)new EntityAIWatchClosest((EntityLiving)this, EntityPlayer.class, 8.0F));
    this.tasks.addTask(6, (EntityAIBase)new EntityAILookIdle((EntityLiving)this));
    this.targetTasks.addTask(1, (EntityAIBase)new EntityAIHurtByTarget(this, false, new Class[0]));
    this.targetTasks.addTask(2, (EntityAIBase)new AISpiderTarget<>(this, EntityPlayer.class));
    this.targetTasks.addTask(3, (EntityAIBase)new AISpiderTarget<>(this, EntityIronGolem.class));
  }
  
  public double getMountedYOffset() {
    return (this.height * 0.5F);
  }
  
  protected PathNavigate getNewNavigator(World worldIn) {
    return (PathNavigate)new PathNavigateClimber((EntityLiving)this, worldIn);
  }
  
  protected void entityInit() {
    super.entityInit();
    this.dataManager.register(CLIMBING, Byte.valueOf((byte)0));
  }
  
  public void onUpdate() {
    super.onUpdate();
    if (!this.world.isRemote)
      setBesideClimbableBlock(this.isCollidedHorizontally); 
  }
  
  protected void applyEntityAttributes() {
    super.applyEntityAttributes();
    getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(16.0D);
    getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.30000001192092896D);
  }
  
  protected SoundEvent getAmbientSound() {
    return SoundEvents.ENTITY_SPIDER_AMBIENT;
  }
  
  protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
    return SoundEvents.ENTITY_SPIDER_HURT;
  }
  
  protected SoundEvent getDeathSound() {
    return SoundEvents.ENTITY_SPIDER_DEATH;
  }
  
  protected void playStepSound(BlockPos pos, Block blockIn) {
    playSound(SoundEvents.ENTITY_SPIDER_STEP, 0.15F, 1.0F);
  }
  
  @Nullable
  protected ResourceLocation getLootTable() {
    return LootTableList.ENTITIES_SPIDER;
  }
  
  public boolean isOnLadder() {
    return isBesideClimbableBlock();
  }
  
  public void setInWeb() {}
  
  public EnumCreatureAttribute getCreatureAttribute() {
    return EnumCreatureAttribute.ARTHROPOD;
  }
  
  public boolean isPotionApplicable(PotionEffect potioneffectIn) {
    return (potioneffectIn.getPotion() == MobEffects.POISON) ? false : super.isPotionApplicable(potioneffectIn);
  }
  
  public boolean isBesideClimbableBlock() {
    return ((((Byte)this.dataManager.get(CLIMBING)).byteValue() & 0x1) != 0);
  }
  
  public void setBesideClimbableBlock(boolean climbing) {
    byte b0 = ((Byte)this.dataManager.get(CLIMBING)).byteValue();
    if (climbing) {
      b0 = (byte)(b0 | 0x1);
    } else {
      b0 = (byte)(b0 & 0xFFFFFFFE);
    } 
    this.dataManager.set(CLIMBING, Byte.valueOf(b0));
  }
  
  @Nullable
  public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
    livingdata = super.onInitialSpawn(difficulty, livingdata);
    if (this.world.rand.nextInt(100) == 0) {
      EntitySkeleton entityskeleton = new EntitySkeleton(this.world);
      entityskeleton.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
      entityskeleton.onInitialSpawn(difficulty, (IEntityLivingData)null);
      this.world.spawnEntityInWorld((Entity)entityskeleton);
      entityskeleton.startRiding((Entity)this);
    } 
    if (livingdata == null) {
      livingdata = new GroupData();
      if (this.world.getDifficulty() == EnumDifficulty.HARD && this.world.rand.nextFloat() < 0.1F * difficulty.getClampedAdditionalDifficulty())
        ((GroupData)livingdata).setRandomEffect(this.world.rand); 
    } 
    if (livingdata instanceof GroupData) {
      Potion potion = ((GroupData)livingdata).effect;
      if (potion != null)
        addPotionEffect(new PotionEffect(potion, 2147483647)); 
    } 
    return livingdata;
  }
  
  public float getEyeHeight() {
    return 0.65F;
  }
  
  static class AISpiderAttack extends EntityAIAttackMelee {
    public AISpiderAttack(EntitySpider spider) {
      super(spider, 1.0D, true);
    }
    
    public boolean continueExecuting() {
      float f = this.attacker.getBrightness();
      if (f >= 0.5F && this.attacker.getRNG().nextInt(100) == 0) {
        this.attacker.setAttackTarget(null);
        return false;
      } 
      return super.continueExecuting();
    }
    
    protected double getAttackReachSqr(EntityLivingBase attackTarget) {
      return (4.0F + attackTarget.width);
    }
  }
  
  static class AISpiderTarget<T extends EntityLivingBase> extends EntityAINearestAttackableTarget<T> {
    public AISpiderTarget(EntitySpider spider, Class<T> classTarget) {
      super(spider, classTarget, true);
    }
    
    public boolean shouldExecute() {
      float f = this.taskOwner.getBrightness();
      return (f >= 0.5F) ? false : super.shouldExecute();
    }
  }
  
  public static class GroupData implements IEntityLivingData {
    public Potion effect;
    
    public void setRandomEffect(Random rand) {
      int i = rand.nextInt(5);
      if (i <= 1) {
        this.effect = MobEffects.SPEED;
      } else if (i <= 2) {
        this.effect = MobEffects.STRENGTH;
      } else if (i <= 3) {
        this.effect = MobEffects.REGENERATION;
      } else if (i <= 4) {
        this.effect = MobEffects.INVISIBILITY;
      } 
    }
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\entity\monster\EntitySpider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
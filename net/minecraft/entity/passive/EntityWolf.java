package net.minecraft.entity.passive;

import com.google.common.base.Predicate;
import java.util.Random;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIBeg;
import net.minecraft.entity.ai.EntityAIFollowOwner;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILeapAtTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIOwnerHurtByTarget;
import net.minecraft.entity.ai.EntityAIOwnerHurtTarget;
import net.minecraft.entity.ai.EntityAISit;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITargetNonTamed;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.AbstractSkeleton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityWolf extends EntityTameable {
  private static final DataParameter<Float> DATA_HEALTH_ID = EntityDataManager.createKey(EntityWolf.class, DataSerializers.FLOAT);
  
  private static final DataParameter<Boolean> BEGGING = EntityDataManager.createKey(EntityWolf.class, DataSerializers.BOOLEAN);
  
  private static final DataParameter<Integer> COLLAR_COLOR = EntityDataManager.createKey(EntityWolf.class, DataSerializers.VARINT);
  
  private float headRotationCourse;
  
  private float headRotationCourseOld;
  
  private boolean isWet;
  
  private boolean isShaking;
  
  private float timeWolfIsShaking;
  
  private float prevTimeWolfIsShaking;
  
  public EntityWolf(World worldIn) {
    super(worldIn);
    setSize(0.6F, 0.85F);
    setTamed(false);
  }
  
  protected void initEntityAI() {
    this.aiSit = new EntityAISit(this);
    this.tasks.addTask(1, (EntityAIBase)new EntityAISwimming((EntityLiving)this));
    this.tasks.addTask(2, (EntityAIBase)this.aiSit);
    this.tasks.addTask(3, (EntityAIBase)new AIAvoidEntity<>(this, EntityLlama.class, 24.0F, 1.5D, 1.5D));
    this.tasks.addTask(4, (EntityAIBase)new EntityAILeapAtTarget((EntityLiving)this, 0.4F));
    this.tasks.addTask(5, (EntityAIBase)new EntityAIAttackMelee((EntityCreature)this, 1.0D, true));
    this.tasks.addTask(6, (EntityAIBase)new EntityAIFollowOwner(this, 1.0D, 10.0F, 2.0F));
    this.tasks.addTask(7, (EntityAIBase)new EntityAIMate(this, 1.0D));
    this.tasks.addTask(8, (EntityAIBase)new EntityAIWanderAvoidWater((EntityCreature)this, 1.0D));
    this.tasks.addTask(9, (EntityAIBase)new EntityAIBeg(this, 8.0F));
    this.tasks.addTask(10, (EntityAIBase)new EntityAIWatchClosest((EntityLiving)this, EntityPlayer.class, 8.0F));
    this.tasks.addTask(10, (EntityAIBase)new EntityAILookIdle((EntityLiving)this));
    this.targetTasks.addTask(1, (EntityAIBase)new EntityAIOwnerHurtByTarget(this));
    this.targetTasks.addTask(2, (EntityAIBase)new EntityAIOwnerHurtTarget(this));
    this.targetTasks.addTask(3, (EntityAIBase)new EntityAIHurtByTarget((EntityCreature)this, true, new Class[0]));
    this.targetTasks.addTask(4, (EntityAIBase)new EntityAITargetNonTamed(this, EntityAnimal.class, false, new Predicate<Entity>() {
            public boolean apply(@Nullable Entity p_apply_1_) {
              return !(!(p_apply_1_ instanceof EntitySheep) && !(p_apply_1_ instanceof EntityRabbit));
            }
          }));
    this.targetTasks.addTask(5, (EntityAIBase)new EntityAINearestAttackableTarget((EntityCreature)this, AbstractSkeleton.class, false));
  }
  
  protected void applyEntityAttributes() {
    super.applyEntityAttributes();
    getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.30000001192092896D);
    if (isTamed()) {
      getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20.0D);
    } else {
      getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(8.0D);
    } 
    getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(2.0D);
  }
  
  public void setAttackTarget(@Nullable EntityLivingBase entitylivingbaseIn) {
    super.setAttackTarget(entitylivingbaseIn);
    if (entitylivingbaseIn == null) {
      setAngry(false);
    } else if (!isTamed()) {
      setAngry(true);
    } 
  }
  
  protected void updateAITasks() {
    this.dataManager.set(DATA_HEALTH_ID, Float.valueOf(getHealth()));
  }
  
  protected void entityInit() {
    super.entityInit();
    this.dataManager.register(DATA_HEALTH_ID, Float.valueOf(getHealth()));
    this.dataManager.register(BEGGING, Boolean.valueOf(false));
    this.dataManager.register(COLLAR_COLOR, Integer.valueOf(EnumDyeColor.RED.getDyeDamage()));
  }
  
  protected void playStepSound(BlockPos pos, Block blockIn) {
    playSound(SoundEvents.ENTITY_WOLF_STEP, 0.15F, 1.0F);
  }
  
  public static void registerFixesWolf(DataFixer fixer) {
    EntityLiving.registerFixesMob(fixer, EntityWolf.class);
  }
  
  public void writeEntityToNBT(NBTTagCompound compound) {
    super.writeEntityToNBT(compound);
    compound.setBoolean("Angry", isAngry());
    compound.setByte("CollarColor", (byte)getCollarColor().getDyeDamage());
  }
  
  public void readEntityFromNBT(NBTTagCompound compound) {
    super.readEntityFromNBT(compound);
    setAngry(compound.getBoolean("Angry"));
    if (compound.hasKey("CollarColor", 99))
      setCollarColor(EnumDyeColor.byDyeDamage(compound.getByte("CollarColor"))); 
  }
  
  protected SoundEvent getAmbientSound() {
    if (isAngry())
      return SoundEvents.ENTITY_WOLF_GROWL; 
    if (this.rand.nextInt(3) == 0)
      return (isTamed() && ((Float)this.dataManager.get(DATA_HEALTH_ID)).floatValue() < 10.0F) ? SoundEvents.ENTITY_WOLF_WHINE : SoundEvents.ENTITY_WOLF_PANT; 
    return SoundEvents.ENTITY_WOLF_AMBIENT;
  }
  
  protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
    return SoundEvents.ENTITY_WOLF_HURT;
  }
  
  protected SoundEvent getDeathSound() {
    return SoundEvents.ENTITY_WOLF_DEATH;
  }
  
  protected float getSoundVolume() {
    return 0.4F;
  }
  
  @Nullable
  protected ResourceLocation getLootTable() {
    return LootTableList.ENTITIES_WOLF;
  }
  
  public void onLivingUpdate() {
    super.onLivingUpdate();
    if (!this.world.isRemote && this.isWet && !this.isShaking && !hasPath() && this.onGround) {
      this.isShaking = true;
      this.timeWolfIsShaking = 0.0F;
      this.prevTimeWolfIsShaking = 0.0F;
      this.world.setEntityState((Entity)this, (byte)8);
    } 
    if (!this.world.isRemote && getAttackTarget() == null && isAngry())
      setAngry(false); 
  }
  
  public void onUpdate() {
    super.onUpdate();
    this.headRotationCourseOld = this.headRotationCourse;
    if (isBegging()) {
      this.headRotationCourse += (1.0F - this.headRotationCourse) * 0.4F;
    } else {
      this.headRotationCourse += (0.0F - this.headRotationCourse) * 0.4F;
    } 
    if (isWet()) {
      this.isWet = true;
      this.isShaking = false;
      this.timeWolfIsShaking = 0.0F;
      this.prevTimeWolfIsShaking = 0.0F;
    } else if ((this.isWet || this.isShaking) && this.isShaking) {
      if (this.timeWolfIsShaking == 0.0F)
        playSound(SoundEvents.ENTITY_WOLF_SHAKE, getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F); 
      this.prevTimeWolfIsShaking = this.timeWolfIsShaking;
      this.timeWolfIsShaking += 0.05F;
      if (this.prevTimeWolfIsShaking >= 2.0F) {
        this.isWet = false;
        this.isShaking = false;
        this.prevTimeWolfIsShaking = 0.0F;
        this.timeWolfIsShaking = 0.0F;
      } 
      if (this.timeWolfIsShaking > 0.4F) {
        float f = (float)(getEntityBoundingBox()).minY;
        int i = (int)(MathHelper.sin((this.timeWolfIsShaking - 0.4F) * 3.1415927F) * 7.0F);
        for (int j = 0; j < i; j++) {
          float f1 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width * 0.5F;
          float f2 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width * 0.5F;
          this.world.spawnParticle(EnumParticleTypes.WATER_SPLASH, this.posX + f1, (f + 0.8F), this.posZ + f2, this.motionX, this.motionY, this.motionZ, new int[0]);
        } 
      } 
    } 
  }
  
  public boolean isWolfWet() {
    return this.isWet;
  }
  
  public float getShadingWhileWet(float p_70915_1_) {
    return 0.75F + (this.prevTimeWolfIsShaking + (this.timeWolfIsShaking - this.prevTimeWolfIsShaking) * p_70915_1_) / 2.0F * 0.25F;
  }
  
  public float getShakeAngle(float p_70923_1_, float p_70923_2_) {
    float f = (this.prevTimeWolfIsShaking + (this.timeWolfIsShaking - this.prevTimeWolfIsShaking) * p_70923_1_ + p_70923_2_) / 1.8F;
    if (f < 0.0F) {
      f = 0.0F;
    } else if (f > 1.0F) {
      f = 1.0F;
    } 
    return MathHelper.sin(f * 3.1415927F) * MathHelper.sin(f * 3.1415927F * 11.0F) * 0.15F * 3.1415927F;
  }
  
  public float getInterestedAngle(float p_70917_1_) {
    return (this.headRotationCourseOld + (this.headRotationCourse - this.headRotationCourseOld) * p_70917_1_) * 0.15F * 3.1415927F;
  }
  
  public float getEyeHeight() {
    return this.height * 0.8F;
  }
  
  public int getVerticalFaceSpeed() {
    return isSitting() ? 20 : super.getVerticalFaceSpeed();
  }
  
  public boolean attackEntityFrom(DamageSource source, float amount) {
    if (isEntityInvulnerable(source))
      return false; 
    Entity entity = source.getEntity();
    if (this.aiSit != null)
      this.aiSit.setSitting(false); 
    if (entity != null && !(entity instanceof EntityPlayer) && !(entity instanceof net.minecraft.entity.projectile.EntityArrow))
      amount = (amount + 1.0F) / 2.0F; 
    return super.attackEntityFrom(source, amount);
  }
  
  public boolean attackEntityAsMob(Entity entityIn) {
    boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage((EntityLivingBase)this), (int)getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue());
    if (flag)
      applyEnchantments((EntityLivingBase)this, entityIn); 
    return flag;
  }
  
  public void setTamed(boolean tamed) {
    super.setTamed(tamed);
    if (tamed) {
      getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20.0D);
    } else {
      getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(8.0D);
    } 
    getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4.0D);
  }
  
  public boolean processInteract(EntityPlayer player, EnumHand hand) {
    ItemStack itemstack = player.getHeldItem(hand);
    if (isTamed()) {
      if (!itemstack.func_190926_b())
        if (itemstack.getItem() instanceof ItemFood) {
          ItemFood itemfood = (ItemFood)itemstack.getItem();
          if (itemfood.isWolfsFavoriteMeat() && ((Float)this.dataManager.get(DATA_HEALTH_ID)).floatValue() < 20.0F) {
            if (!player.capabilities.isCreativeMode)
              itemstack.func_190918_g(1); 
            heal(itemfood.getHealAmount(itemstack));
            return true;
          } 
        } else if (itemstack.getItem() == Items.DYE) {
          EnumDyeColor enumdyecolor = EnumDyeColor.byDyeDamage(itemstack.getMetadata());
          if (enumdyecolor != getCollarColor()) {
            setCollarColor(enumdyecolor);
            if (!player.capabilities.isCreativeMode)
              itemstack.func_190918_g(1); 
            return true;
          } 
        }  
      if (isOwner((EntityLivingBase)player) && !this.world.isRemote && !isBreedingItem(itemstack)) {
        this.aiSit.setSitting(!isSitting());
        this.isJumping = false;
        this.navigator.clearPathEntity();
        setAttackTarget((EntityLivingBase)null);
      } 
    } else if (itemstack.getItem() == Items.BONE && !isAngry()) {
      if (!player.capabilities.isCreativeMode)
        itemstack.func_190918_g(1); 
      if (!this.world.isRemote)
        if (this.rand.nextInt(3) == 0) {
          func_193101_c(player);
          this.navigator.clearPathEntity();
          setAttackTarget((EntityLivingBase)null);
          this.aiSit.setSitting(true);
          setHealth(20.0F);
          playTameEffect(true);
          this.world.setEntityState((Entity)this, (byte)7);
        } else {
          playTameEffect(false);
          this.world.setEntityState((Entity)this, (byte)6);
        }  
      return true;
    } 
    return super.processInteract(player, hand);
  }
  
  public void handleStatusUpdate(byte id) {
    if (id == 8) {
      this.isShaking = true;
      this.timeWolfIsShaking = 0.0F;
      this.prevTimeWolfIsShaking = 0.0F;
    } else {
      super.handleStatusUpdate(id);
    } 
  }
  
  public float getTailRotation() {
    if (isAngry())
      return 1.5393804F; 
    return isTamed() ? ((0.55F - (getMaxHealth() - ((Float)this.dataManager.get(DATA_HEALTH_ID)).floatValue()) * 0.02F) * 3.1415927F) : 0.62831855F;
  }
  
  public boolean isBreedingItem(ItemStack stack) {
    return (stack.getItem() instanceof ItemFood && ((ItemFood)stack.getItem()).isWolfsFavoriteMeat());
  }
  
  public int getMaxSpawnedInChunk() {
    return 8;
  }
  
  public boolean isAngry() {
    return ((((Byte)this.dataManager.get(TAMED)).byteValue() & 0x2) != 0);
  }
  
  public void setAngry(boolean angry) {
    byte b0 = ((Byte)this.dataManager.get(TAMED)).byteValue();
    if (angry) {
      this.dataManager.set(TAMED, Byte.valueOf((byte)(b0 | 0x2)));
    } else {
      this.dataManager.set(TAMED, Byte.valueOf((byte)(b0 & 0xFFFFFFFD)));
    } 
  }
  
  public EnumDyeColor getCollarColor() {
    return EnumDyeColor.byDyeDamage(((Integer)this.dataManager.get(COLLAR_COLOR)).intValue() & 0xF);
  }
  
  public void setCollarColor(EnumDyeColor collarcolor) {
    this.dataManager.set(COLLAR_COLOR, Integer.valueOf(collarcolor.getDyeDamage()));
  }
  
  public EntityWolf createChild(EntityAgeable ageable) {
    EntityWolf entitywolf = new EntityWolf(this.world);
    UUID uuid = getOwnerId();
    if (uuid != null) {
      entitywolf.setOwnerId(uuid);
      entitywolf.setTamed(true);
    } 
    return entitywolf;
  }
  
  public void setBegging(boolean beg) {
    this.dataManager.set(BEGGING, Boolean.valueOf(beg));
  }
  
  public boolean canMateWith(EntityAnimal otherAnimal) {
    if (otherAnimal == this)
      return false; 
    if (!isTamed())
      return false; 
    if (!(otherAnimal instanceof EntityWolf))
      return false; 
    EntityWolf entitywolf = (EntityWolf)otherAnimal;
    if (!entitywolf.isTamed())
      return false; 
    if (entitywolf.isSitting())
      return false; 
    return (isInLove() && entitywolf.isInLove());
  }
  
  public boolean isBegging() {
    return ((Boolean)this.dataManager.get(BEGGING)).booleanValue();
  }
  
  public boolean shouldAttackEntity(EntityLivingBase p_142018_1_, EntityLivingBase p_142018_2_) {
    if (!(p_142018_1_ instanceof net.minecraft.entity.monster.EntityCreeper) && !(p_142018_1_ instanceof net.minecraft.entity.monster.EntityGhast)) {
      if (p_142018_1_ instanceof EntityWolf) {
        EntityWolf entitywolf = (EntityWolf)p_142018_1_;
        if (entitywolf.isTamed() && entitywolf.getOwner() == p_142018_2_)
          return false; 
      } 
      if (p_142018_1_ instanceof EntityPlayer && p_142018_2_ instanceof EntityPlayer && !((EntityPlayer)p_142018_2_).canAttackPlayer((EntityPlayer)p_142018_1_))
        return false; 
      return !(p_142018_1_ instanceof AbstractHorse && ((AbstractHorse)p_142018_1_).isTame());
    } 
    return false;
  }
  
  public boolean canBeLeashedTo(EntityPlayer player) {
    return (!isAngry() && super.canBeLeashedTo(player));
  }
  
  class AIAvoidEntity<T extends Entity> extends EntityAIAvoidEntity<T> {
    private final EntityWolf field_190856_d;
    
    public AIAvoidEntity(EntityWolf p_i47251_2_, Class<T> p_i47251_3_, float p_i47251_4_, double p_i47251_5_, double p_i47251_7_) {
      super((EntityCreature)p_i47251_2_, p_i47251_3_, p_i47251_4_, p_i47251_5_, p_i47251_7_);
      this.field_190856_d = p_i47251_2_;
    }
    
    public boolean shouldExecute() {
      if (super.shouldExecute() && this.closestLivingEntity instanceof EntityLlama)
        return (!this.field_190856_d.isTamed() && func_190854_a((EntityLlama)this.closestLivingEntity)); 
      return false;
    }
    
    private boolean func_190854_a(EntityLlama p_190854_1_) {
      return (p_190854_1_.func_190707_dL() >= EntityWolf.this.rand.nextInt(5));
    }
    
    public void startExecuting() {
      EntityWolf.this.setAttackTarget((EntityLivingBase)null);
      super.startExecuting();
    }
    
    public void updateTask() {
      EntityWolf.this.setAttackTarget((EntityLivingBase)null);
      super.updateTask();
    }
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\entity\passive\EntityWolf.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
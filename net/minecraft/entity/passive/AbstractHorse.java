package net.minecraft.entity.passive;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.IJumpingMount;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAIRunAroundLikeCrazy;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.ContainerHorseChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.IInventoryChangedListener;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.server.management.PreYggdrasilConverter;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.IDataWalker;
import net.minecraft.util.datafix.walkers.ItemStackData;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public abstract class AbstractHorse extends EntityAnimal implements IInventoryChangedListener, IJumpingMount {
  private static final Predicate<Entity> IS_HORSE_BREEDING = new Predicate<Entity>() {
      public boolean apply(@Nullable Entity p_apply_1_) {
        return (p_apply_1_ instanceof AbstractHorse && ((AbstractHorse)p_apply_1_).isBreeding());
      }
    };
  
  protected static final IAttribute JUMP_STRENGTH = (IAttribute)(new RangedAttribute(null, "horse.jumpStrength", 0.7D, 0.0D, 2.0D)).setDescription("Jump Strength").setShouldWatch(true);
  
  private static final DataParameter<Byte> STATUS = EntityDataManager.createKey(AbstractHorse.class, DataSerializers.BYTE);
  
  private static final DataParameter<Optional<UUID>> OWNER_UNIQUE_ID = EntityDataManager.createKey(AbstractHorse.class, DataSerializers.OPTIONAL_UNIQUE_ID);
  
  private int field_190689_bJ;
  
  private int openMouthCounter;
  
  private int jumpRearingCounter;
  
  public int tailCounter;
  
  public int sprintCounter;
  
  protected boolean horseJumping;
  
  protected ContainerHorseChest horseChest;
  
  protected int temper;
  
  protected float jumpPower;
  
  private boolean allowStandSliding;
  
  private float headLean;
  
  private float prevHeadLean;
  
  private float rearingAmount;
  
  private float prevRearingAmount;
  
  private float mouthOpenness;
  
  private float prevMouthOpenness;
  
  protected boolean field_190688_bE = true;
  
  protected int gallopTime;
  
  public AbstractHorse(World p_i47299_1_) {
    super(p_i47299_1_);
    setSize(1.3964844F, 1.6F);
    this.stepHeight = 1.0F;
    initHorseChest();
  }
  
  protected void initEntityAI() {
    this.tasks.addTask(0, (EntityAIBase)new EntityAISwimming((EntityLiving)this));
    this.tasks.addTask(1, (EntityAIBase)new EntityAIPanic((EntityCreature)this, 1.2D));
    this.tasks.addTask(1, (EntityAIBase)new EntityAIRunAroundLikeCrazy(this, 1.2D));
    this.tasks.addTask(2, (EntityAIBase)new EntityAIMate(this, 1.0D, AbstractHorse.class));
    this.tasks.addTask(4, (EntityAIBase)new EntityAIFollowParent(this, 1.0D));
    this.tasks.addTask(6, (EntityAIBase)new EntityAIWanderAvoidWater((EntityCreature)this, 0.7D));
    this.tasks.addTask(7, (EntityAIBase)new EntityAIWatchClosest((EntityLiving)this, EntityPlayer.class, 6.0F));
    this.tasks.addTask(8, (EntityAIBase)new EntityAILookIdle((EntityLiving)this));
  }
  
  protected void entityInit() {
    super.entityInit();
    this.dataManager.register(STATUS, Byte.valueOf((byte)0));
    this.dataManager.register(OWNER_UNIQUE_ID, Optional.absent());
  }
  
  protected boolean getHorseWatchableBoolean(int p_110233_1_) {
    return ((((Byte)this.dataManager.get(STATUS)).byteValue() & p_110233_1_) != 0);
  }
  
  protected void setHorseWatchableBoolean(int p_110208_1_, boolean p_110208_2_) {
    byte b0 = ((Byte)this.dataManager.get(STATUS)).byteValue();
    if (p_110208_2_) {
      this.dataManager.set(STATUS, Byte.valueOf((byte)(b0 | p_110208_1_)));
    } else {
      this.dataManager.set(STATUS, Byte.valueOf((byte)(b0 & (p_110208_1_ ^ 0xFFFFFFFF))));
    } 
  }
  
  public boolean isTame() {
    return getHorseWatchableBoolean(2);
  }
  
  @Nullable
  public UUID getOwnerUniqueId() {
    return (UUID)((Optional)this.dataManager.get(OWNER_UNIQUE_ID)).orNull();
  }
  
  public void setOwnerUniqueId(@Nullable UUID uniqueId) {
    this.dataManager.set(OWNER_UNIQUE_ID, Optional.fromNullable(uniqueId));
  }
  
  public float getHorseSize() {
    return 0.5F;
  }
  
  public void setScaleForAge(boolean child) {
    setScale(child ? getHorseSize() : 1.0F);
  }
  
  public boolean isHorseJumping() {
    return this.horseJumping;
  }
  
  public void setHorseTamed(boolean tamed) {
    setHorseWatchableBoolean(2, tamed);
  }
  
  public void setHorseJumping(boolean jumping) {
    this.horseJumping = jumping;
  }
  
  public boolean canBeLeashedTo(EntityPlayer player) {
    return (super.canBeLeashedTo(player) && getCreatureAttribute() != EnumCreatureAttribute.UNDEAD);
  }
  
  protected void onLeashDistance(float p_142017_1_) {
    if (p_142017_1_ > 6.0F && isEatingHaystack())
      setEatingHaystack(false); 
  }
  
  public boolean isEatingHaystack() {
    return getHorseWatchableBoolean(16);
  }
  
  public boolean isRearing() {
    return getHorseWatchableBoolean(32);
  }
  
  public boolean isBreeding() {
    return getHorseWatchableBoolean(8);
  }
  
  public void setBreeding(boolean breeding) {
    setHorseWatchableBoolean(8, breeding);
  }
  
  public void setHorseSaddled(boolean saddled) {
    setHorseWatchableBoolean(4, saddled);
  }
  
  public int getTemper() {
    return this.temper;
  }
  
  public void setTemper(int temperIn) {
    this.temper = temperIn;
  }
  
  public int increaseTemper(int p_110198_1_) {
    int i = MathHelper.clamp(getTemper() + p_110198_1_, 0, func_190676_dC());
    setTemper(i);
    return i;
  }
  
  public boolean attackEntityFrom(DamageSource source, float amount) {
    Entity entity = source.getEntity();
    return (isBeingRidden() && entity != null && isRidingOrBeingRiddenBy(entity)) ? false : super.attackEntityFrom(source, amount);
  }
  
  public boolean canBePushed() {
    return !isBeingRidden();
  }
  
  private void eatingHorse() {
    openHorseMouth();
    if (!isSilent())
      this.world.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_HORSE_EAT, getSoundCategory(), 1.0F, 1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F); 
  }
  
  public void fall(float distance, float damageMultiplier) {
    if (distance > 1.0F)
      playSound(SoundEvents.ENTITY_HORSE_LAND, 0.4F, 1.0F); 
    int i = MathHelper.ceil((distance * 0.5F - 3.0F) * damageMultiplier);
    if (i > 0) {
      attackEntityFrom(DamageSource.fall, i);
      if (isBeingRidden())
        for (Entity entity : getRecursivePassengers())
          entity.attackEntityFrom(DamageSource.fall, i);  
      IBlockState iblockstate = this.world.getBlockState(new BlockPos(this.posX, this.posY - 0.2D - this.prevRotationYaw, this.posZ));
      Block block = iblockstate.getBlock();
      if (iblockstate.getMaterial() != Material.AIR && !isSilent()) {
        SoundType soundtype = block.getSoundType();
        this.world.playSound(null, this.posX, this.posY, this.posZ, soundtype.getStepSound(), getSoundCategory(), soundtype.getVolume() * 0.5F, soundtype.getPitch() * 0.75F);
      } 
    } 
  }
  
  protected int func_190686_di() {
    return 2;
  }
  
  protected void initHorseChest() {
    ContainerHorseChest containerhorsechest = this.horseChest;
    this.horseChest = new ContainerHorseChest("HorseChest", func_190686_di());
    this.horseChest.setCustomName(getName());
    if (containerhorsechest != null) {
      containerhorsechest.removeInventoryChangeListener(this);
      int i = Math.min(containerhorsechest.getSizeInventory(), this.horseChest.getSizeInventory());
      for (int j = 0; j < i; j++) {
        ItemStack itemstack = containerhorsechest.getStackInSlot(j);
        if (!itemstack.func_190926_b())
          this.horseChest.setInventorySlotContents(j, itemstack.copy()); 
      } 
    } 
    this.horseChest.addInventoryChangeListener(this);
    updateHorseSlots();
  }
  
  protected void updateHorseSlots() {
    if (!this.world.isRemote)
      setHorseSaddled((!this.horseChest.getStackInSlot(0).func_190926_b() && func_190685_dA())); 
  }
  
  public void onInventoryChanged(IInventory invBasic) {
    boolean flag = isHorseSaddled();
    updateHorseSlots();
    if (this.ticksExisted > 20 && !flag && isHorseSaddled())
      playSound(SoundEvents.ENTITY_HORSE_SADDLE, 0.5F, 1.0F); 
  }
  
  @Nullable
  protected AbstractHorse getClosestHorse(Entity entityIn, double distance) {
    double d0 = Double.MAX_VALUE;
    Entity entity = null;
    for (Entity entity1 : this.world.getEntitiesInAABBexcluding(entityIn, entityIn.getEntityBoundingBox().addCoord(distance, distance, distance), IS_HORSE_BREEDING)) {
      double d1 = entity1.getDistanceSq(entityIn.posX, entityIn.posY, entityIn.posZ);
      if (d1 < d0) {
        entity = entity1;
        d0 = d1;
      } 
    } 
    return (AbstractHorse)entity;
  }
  
  public double getHorseJumpStrength() {
    return getEntityAttribute(JUMP_STRENGTH).getAttributeValue();
  }
  
  @Nullable
  protected SoundEvent getDeathSound() {
    openHorseMouth();
    return null;
  }
  
  @Nullable
  protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
    openHorseMouth();
    if (this.rand.nextInt(3) == 0)
      makeHorseRear(); 
    return null;
  }
  
  @Nullable
  protected SoundEvent getAmbientSound() {
    openHorseMouth();
    if (this.rand.nextInt(10) == 0 && !isMovementBlocked())
      makeHorseRear(); 
    return null;
  }
  
  public boolean func_190685_dA() {
    return true;
  }
  
  public boolean isHorseSaddled() {
    return getHorseWatchableBoolean(4);
  }
  
  @Nullable
  protected SoundEvent getAngrySound() {
    openHorseMouth();
    makeHorseRear();
    return null;
  }
  
  protected void playStepSound(BlockPos pos, Block blockIn) {
    if (!blockIn.getDefaultState().getMaterial().isLiquid()) {
      SoundType soundtype = blockIn.getSoundType();
      if (this.world.getBlockState(pos.up()).getBlock() == Blocks.SNOW_LAYER)
        soundtype = Blocks.SNOW_LAYER.getSoundType(); 
      if (isBeingRidden() && this.field_190688_bE) {
        this.gallopTime++;
        if (this.gallopTime > 5 && this.gallopTime % 3 == 0) {
          func_190680_a(soundtype);
        } else if (this.gallopTime <= 5) {
          playSound(SoundEvents.ENTITY_HORSE_STEP_WOOD, soundtype.getVolume() * 0.15F, soundtype.getPitch());
        } 
      } else if (soundtype == SoundType.WOOD) {
        playSound(SoundEvents.ENTITY_HORSE_STEP_WOOD, soundtype.getVolume() * 0.15F, soundtype.getPitch());
      } else {
        playSound(SoundEvents.ENTITY_HORSE_STEP, soundtype.getVolume() * 0.15F, soundtype.getPitch());
      } 
    } 
  }
  
  protected void func_190680_a(SoundType p_190680_1_) {
    playSound(SoundEvents.ENTITY_HORSE_GALLOP, p_190680_1_.getVolume() * 0.15F, p_190680_1_.getPitch());
  }
  
  protected void applyEntityAttributes() {
    super.applyEntityAttributes();
    getAttributeMap().registerAttribute(JUMP_STRENGTH);
    getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(53.0D);
    getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.22499999403953552D);
  }
  
  public int getMaxSpawnedInChunk() {
    return 6;
  }
  
  public int func_190676_dC() {
    return 100;
  }
  
  protected float getSoundVolume() {
    return 0.8F;
  }
  
  public int getTalkInterval() {
    return 400;
  }
  
  public void openGUI(EntityPlayer playerEntity) {
    if (!this.world.isRemote && (!isBeingRidden() || isPassenger((Entity)playerEntity)) && isTame()) {
      this.horseChest.setCustomName(getName());
      playerEntity.openGuiHorseInventory(this, (IInventory)this.horseChest);
    } 
  }
  
  protected boolean func_190678_b(EntityPlayer p_190678_1_, ItemStack p_190678_2_) {
    boolean flag = false;
    float f = 0.0F;
    int i = 0;
    int j = 0;
    Item item = p_190678_2_.getItem();
    if (item == Items.WHEAT) {
      f = 2.0F;
      i = 20;
      j = 3;
    } else if (item == Items.SUGAR) {
      f = 1.0F;
      i = 30;
      j = 3;
    } else if (item == Item.getItemFromBlock(Blocks.HAY_BLOCK)) {
      f = 20.0F;
      i = 180;
    } else if (item == Items.APPLE) {
      f = 3.0F;
      i = 60;
      j = 3;
    } else if (item == Items.GOLDEN_CARROT) {
      f = 4.0F;
      i = 60;
      j = 5;
      if (isTame() && getGrowingAge() == 0 && !isInLove()) {
        flag = true;
        setInLove(p_190678_1_);
      } 
    } else if (item == Items.GOLDEN_APPLE) {
      f = 10.0F;
      i = 240;
      j = 10;
      if (isTame() && getGrowingAge() == 0 && !isInLove()) {
        flag = true;
        setInLove(p_190678_1_);
      } 
    } 
    if (getHealth() < getMaxHealth() && f > 0.0F) {
      heal(f);
      flag = true;
    } 
    if (isChild() && i > 0) {
      this.world.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, this.posX + (this.rand.nextFloat() * this.width * 2.0F) - this.width, this.posY + 0.5D + (this.rand.nextFloat() * this.height), this.posZ + (this.rand.nextFloat() * this.width * 2.0F) - this.width, 0.0D, 0.0D, 0.0D, new int[0]);
      if (!this.world.isRemote)
        addGrowth(i); 
      flag = true;
    } 
    if (j > 0 && (flag || !isTame()) && getTemper() < func_190676_dC()) {
      flag = true;
      if (!this.world.isRemote)
        increaseTemper(j); 
    } 
    if (flag)
      eatingHorse(); 
    return flag;
  }
  
  protected void mountTo(EntityPlayer player) {
    player.rotationYaw = this.rotationYaw;
    player.rotationPitch = this.rotationPitch;
    setEatingHaystack(false);
    setRearing(false);
    if (!this.world.isRemote)
      player.startRiding((Entity)this); 
  }
  
  protected boolean isMovementBlocked() {
    return !((!super.isMovementBlocked() || !isBeingRidden() || !isHorseSaddled()) && !isEatingHaystack() && !isRearing());
  }
  
  public boolean isBreedingItem(ItemStack stack) {
    return false;
  }
  
  private void moveTail() {
    this.tailCounter = 1;
  }
  
  public void onDeath(DamageSource cause) {
    super.onDeath(cause);
    if (!this.world.isRemote && this.horseChest != null)
      for (int i = 0; i < this.horseChest.getSizeInventory(); i++) {
        ItemStack itemstack = this.horseChest.getStackInSlot(i);
        if (!itemstack.func_190926_b())
          entityDropItem(itemstack, 0.0F); 
      }  
  }
  
  public void onLivingUpdate() {
    if (this.rand.nextInt(200) == 0)
      moveTail(); 
    super.onLivingUpdate();
    if (!this.world.isRemote) {
      if (this.rand.nextInt(900) == 0 && this.deathTime == 0)
        heal(1.0F); 
      if (func_190684_dE()) {
        if (!isEatingHaystack() && !isBeingRidden() && this.rand.nextInt(300) == 0 && this.world.getBlockState(new BlockPos(MathHelper.floor(this.posX), MathHelper.floor(this.posY) - 1, MathHelper.floor(this.posZ))).getBlock() == Blocks.GRASS)
          setEatingHaystack(true); 
        if (isEatingHaystack() && ++this.field_190689_bJ > 50) {
          this.field_190689_bJ = 0;
          setEatingHaystack(false);
        } 
      } 
      func_190679_dD();
    } 
  }
  
  protected void func_190679_dD() {
    if (isBreeding() && isChild() && !isEatingHaystack()) {
      AbstractHorse abstracthorse = getClosestHorse((Entity)this, 16.0D);
      if (abstracthorse != null && getDistanceSqToEntity((Entity)abstracthorse) > 4.0D)
        this.navigator.getPathToEntityLiving((Entity)abstracthorse); 
    } 
  }
  
  public boolean func_190684_dE() {
    return true;
  }
  
  public void onUpdate() {
    super.onUpdate();
    if (this.openMouthCounter > 0 && ++this.openMouthCounter > 30) {
      this.openMouthCounter = 0;
      setHorseWatchableBoolean(64, false);
    } 
    if (canPassengerSteer() && this.jumpRearingCounter > 0 && ++this.jumpRearingCounter > 20) {
      this.jumpRearingCounter = 0;
      setRearing(false);
    } 
    if (this.tailCounter > 0 && ++this.tailCounter > 8)
      this.tailCounter = 0; 
    if (this.sprintCounter > 0) {
      this.sprintCounter++;
      if (this.sprintCounter > 300)
        this.sprintCounter = 0; 
    } 
    this.prevHeadLean = this.headLean;
    if (isEatingHaystack()) {
      this.headLean += (1.0F - this.headLean) * 0.4F + 0.05F;
      if (this.headLean > 1.0F)
        this.headLean = 1.0F; 
    } else {
      this.headLean += (0.0F - this.headLean) * 0.4F - 0.05F;
      if (this.headLean < 0.0F)
        this.headLean = 0.0F; 
    } 
    this.prevRearingAmount = this.rearingAmount;
    if (isRearing()) {
      this.headLean = 0.0F;
      this.prevHeadLean = this.headLean;
      this.rearingAmount += (1.0F - this.rearingAmount) * 0.4F + 0.05F;
      if (this.rearingAmount > 1.0F)
        this.rearingAmount = 1.0F; 
    } else {
      this.allowStandSliding = false;
      this.rearingAmount += (0.8F * this.rearingAmount * this.rearingAmount * this.rearingAmount - this.rearingAmount) * 0.6F - 0.05F;
      if (this.rearingAmount < 0.0F)
        this.rearingAmount = 0.0F; 
    } 
    this.prevMouthOpenness = this.mouthOpenness;
    if (getHorseWatchableBoolean(64)) {
      this.mouthOpenness += (1.0F - this.mouthOpenness) * 0.7F + 0.05F;
      if (this.mouthOpenness > 1.0F)
        this.mouthOpenness = 1.0F; 
    } else {
      this.mouthOpenness += (0.0F - this.mouthOpenness) * 0.7F - 0.05F;
      if (this.mouthOpenness < 0.0F)
        this.mouthOpenness = 0.0F; 
    } 
  }
  
  private void openHorseMouth() {
    if (!this.world.isRemote) {
      this.openMouthCounter = 1;
      setHorseWatchableBoolean(64, true);
    } 
  }
  
  public void setEatingHaystack(boolean p_110227_1_) {
    setHorseWatchableBoolean(16, p_110227_1_);
  }
  
  public void setRearing(boolean rearing) {
    if (rearing)
      setEatingHaystack(false); 
    setHorseWatchableBoolean(32, rearing);
  }
  
  private void makeHorseRear() {
    if (canPassengerSteer()) {
      this.jumpRearingCounter = 1;
      setRearing(true);
    } 
  }
  
  public void func_190687_dF() {
    makeHorseRear();
    SoundEvent soundevent = getAngrySound();
    if (soundevent != null)
      playSound(soundevent, getSoundVolume(), getSoundPitch()); 
  }
  
  public boolean setTamedBy(EntityPlayer player) {
    setOwnerUniqueId(player.getUniqueID());
    setHorseTamed(true);
    if (player instanceof EntityPlayerMP)
      CriteriaTriggers.field_193136_w.func_193178_a((EntityPlayerMP)player, this); 
    this.world.setEntityState((Entity)this, (byte)7);
    return true;
  }
  
  public void func_191986_a(float p_191986_1_, float p_191986_2_, float p_191986_3_) {
    if (isBeingRidden() && canBeSteered() && isHorseSaddled()) {
      EntityLivingBase entitylivingbase = (EntityLivingBase)getControllingPassenger();
      this.rotationYaw = entitylivingbase.rotationYaw;
      this.prevRotationYaw = this.rotationYaw;
      this.rotationPitch = entitylivingbase.rotationPitch * 0.5F;
      setRotation(this.rotationYaw, this.rotationPitch);
      this.renderYawOffset = this.rotationYaw;
      this.rotationYawHead = this.renderYawOffset;
      p_191986_1_ = entitylivingbase.moveStrafing * 0.5F;
      p_191986_3_ = entitylivingbase.field_191988_bg;
      if (p_191986_3_ <= 0.0F) {
        p_191986_3_ *= 0.25F;
        this.gallopTime = 0;
      } 
      if (this.onGround && this.jumpPower == 0.0F && isRearing() && !this.allowStandSliding) {
        p_191986_1_ = 0.0F;
        p_191986_3_ = 0.0F;
      } 
      if (this.jumpPower > 0.0F && !isHorseJumping() && this.onGround) {
        this.motionY = getHorseJumpStrength() * this.jumpPower;
        if (isPotionActive(MobEffects.JUMP_BOOST))
          this.motionY += ((getActivePotionEffect(MobEffects.JUMP_BOOST).getAmplifier() + 1) * 0.1F); 
        setHorseJumping(true);
        this.isAirBorne = true;
        if (p_191986_3_ > 0.0F) {
          float f = MathHelper.sin(this.rotationYaw * 0.017453292F);
          float f1 = MathHelper.cos(this.rotationYaw * 0.017453292F);
          this.motionX += (-0.4F * f * this.jumpPower);
          this.motionZ += (0.4F * f1 * this.jumpPower);
          playSound(SoundEvents.ENTITY_HORSE_JUMP, 0.4F, 1.0F);
        } 
        this.jumpPower = 0.0F;
      } 
      this.jumpMovementFactor = getAIMoveSpeed() * 0.1F;
      if (canPassengerSteer()) {
        setAIMoveSpeed((float)getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue());
        super.func_191986_a(p_191986_1_, p_191986_2_, p_191986_3_);
      } else if (entitylivingbase instanceof EntityPlayer) {
        this.motionX = 0.0D;
        this.motionY = 0.0D;
        this.motionZ = 0.0D;
      } 
      if (this.onGround) {
        this.jumpPower = 0.0F;
        setHorseJumping(false);
      } 
      this.prevLimbSwingAmount = this.limbSwingAmount;
      double d1 = this.posX - this.prevPosX;
      double d0 = this.posZ - this.prevPosZ;
      float f2 = MathHelper.sqrt(d1 * d1 + d0 * d0) * 4.0F;
      if (f2 > 1.0F)
        f2 = 1.0F; 
      this.limbSwingAmount += (f2 - this.limbSwingAmount) * 0.4F;
      this.limbSwing += this.limbSwingAmount;
    } else {
      this.jumpMovementFactor = 0.02F;
      super.func_191986_a(p_191986_1_, p_191986_2_, p_191986_3_);
    } 
  }
  
  public static void func_190683_c(DataFixer p_190683_0_, Class<?> p_190683_1_) {
    EntityLiving.registerFixesMob(p_190683_0_, p_190683_1_);
    p_190683_0_.registerWalker(FixTypes.ENTITY, (IDataWalker)new ItemStackData(p_190683_1_, new String[] { "SaddleItem" }));
  }
  
  public void writeEntityToNBT(NBTTagCompound compound) {
    super.writeEntityToNBT(compound);
    compound.setBoolean("EatingHaystack", isEatingHaystack());
    compound.setBoolean("Bred", isBreeding());
    compound.setInteger("Temper", getTemper());
    compound.setBoolean("Tame", isTame());
    if (getOwnerUniqueId() != null)
      compound.setString("OwnerUUID", getOwnerUniqueId().toString()); 
    if (!this.horseChest.getStackInSlot(0).func_190926_b())
      compound.setTag("SaddleItem", (NBTBase)this.horseChest.getStackInSlot(0).writeToNBT(new NBTTagCompound())); 
  }
  
  public void readEntityFromNBT(NBTTagCompound compound) {
    String s;
    super.readEntityFromNBT(compound);
    setEatingHaystack(compound.getBoolean("EatingHaystack"));
    setBreeding(compound.getBoolean("Bred"));
    setTemper(compound.getInteger("Temper"));
    setHorseTamed(compound.getBoolean("Tame"));
    if (compound.hasKey("OwnerUUID", 8)) {
      s = compound.getString("OwnerUUID");
    } else {
      String s1 = compound.getString("Owner");
      s = PreYggdrasilConverter.convertMobOwnerIfNeeded(getServer(), s1);
    } 
    if (!s.isEmpty())
      setOwnerUniqueId(UUID.fromString(s)); 
    IAttributeInstance iattributeinstance = getAttributeMap().getAttributeInstanceByName("Speed");
    if (iattributeinstance != null)
      getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(iattributeinstance.getBaseValue() * 0.25D); 
    if (compound.hasKey("SaddleItem", 10)) {
      ItemStack itemstack = new ItemStack(compound.getCompoundTag("SaddleItem"));
      if (itemstack.getItem() == Items.SADDLE)
        this.horseChest.setInventorySlotContents(0, itemstack); 
    } 
    updateHorseSlots();
  }
  
  public boolean canMateWith(EntityAnimal otherAnimal) {
    return false;
  }
  
  protected boolean canMate() {
    return (!isBeingRidden() && !isRiding() && isTame() && !isChild() && getHealth() >= getMaxHealth() && isInLove());
  }
  
  @Nullable
  public EntityAgeable createChild(EntityAgeable ageable) {
    return null;
  }
  
  protected void func_190681_a(EntityAgeable p_190681_1_, AbstractHorse p_190681_2_) {
    double d0 = getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getBaseValue() + p_190681_1_.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getBaseValue() + getModifiedMaxHealth();
    p_190681_2_.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(d0 / 3.0D);
    double d1 = getEntityAttribute(JUMP_STRENGTH).getBaseValue() + p_190681_1_.getEntityAttribute(JUMP_STRENGTH).getBaseValue() + getModifiedJumpStrength();
    p_190681_2_.getEntityAttribute(JUMP_STRENGTH).setBaseValue(d1 / 3.0D);
    double d2 = getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getBaseValue() + p_190681_1_.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getBaseValue() + getModifiedMovementSpeed();
    p_190681_2_.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(d2 / 3.0D);
  }
  
  public boolean canBeSteered() {
    return getControllingPassenger() instanceof EntityLivingBase;
  }
  
  public float getGrassEatingAmount(float p_110258_1_) {
    return this.prevHeadLean + (this.headLean - this.prevHeadLean) * p_110258_1_;
  }
  
  public float getRearingAmount(float p_110223_1_) {
    return this.prevRearingAmount + (this.rearingAmount - this.prevRearingAmount) * p_110223_1_;
  }
  
  public float getMouthOpennessAngle(float p_110201_1_) {
    return this.prevMouthOpenness + (this.mouthOpenness - this.prevMouthOpenness) * p_110201_1_;
  }
  
  public void setJumpPower(int jumpPowerIn) {
    if (isHorseSaddled()) {
      if (jumpPowerIn < 0) {
        jumpPowerIn = 0;
      } else {
        this.allowStandSliding = true;
        makeHorseRear();
      } 
      if (jumpPowerIn >= 90) {
        this.jumpPower = 1.0F;
      } else {
        this.jumpPower = 0.4F + 0.4F * jumpPowerIn / 90.0F;
      } 
    } 
  }
  
  public boolean canJump() {
    return isHorseSaddled();
  }
  
  public void handleStartJump(int p_184775_1_) {
    this.allowStandSliding = true;
    makeHorseRear();
  }
  
  public void handleStopJump() {}
  
  protected void spawnHorseParticles(boolean p_110216_1_) {
    EnumParticleTypes enumparticletypes = p_110216_1_ ? EnumParticleTypes.HEART : EnumParticleTypes.SMOKE_NORMAL;
    for (int i = 0; i < 7; i++) {
      double d0 = this.rand.nextGaussian() * 0.02D;
      double d1 = this.rand.nextGaussian() * 0.02D;
      double d2 = this.rand.nextGaussian() * 0.02D;
      this.world.spawnParticle(enumparticletypes, this.posX + (this.rand.nextFloat() * this.width * 2.0F) - this.width, this.posY + 0.5D + (this.rand.nextFloat() * this.height), this.posZ + (this.rand.nextFloat() * this.width * 2.0F) - this.width, d0, d1, d2, new int[0]);
    } 
  }
  
  public void handleStatusUpdate(byte id) {
    if (id == 7) {
      spawnHorseParticles(true);
    } else if (id == 6) {
      spawnHorseParticles(false);
    } else {
      super.handleStatusUpdate(id);
    } 
  }
  
  public void updatePassenger(Entity passenger) {
    super.updatePassenger(passenger);
    if (passenger instanceof EntityLiving) {
      EntityLiving entityliving = (EntityLiving)passenger;
      this.renderYawOffset = entityliving.renderYawOffset;
    } 
    if (this.prevRearingAmount > 0.0F) {
      float f3 = MathHelper.sin(this.renderYawOffset * 0.017453292F);
      float f = MathHelper.cos(this.renderYawOffset * 0.017453292F);
      float f1 = 0.7F * this.prevRearingAmount;
      float f2 = 0.15F * this.prevRearingAmount;
      passenger.setPosition(this.posX + (f1 * f3), this.posY + getMountedYOffset() + passenger.getYOffset() + f2, this.posZ - (f1 * f));
      if (passenger instanceof EntityLivingBase)
        ((EntityLivingBase)passenger).renderYawOffset = this.renderYawOffset; 
    } 
  }
  
  protected float getModifiedMaxHealth() {
    return 15.0F + this.rand.nextInt(8) + this.rand.nextInt(9);
  }
  
  protected double getModifiedJumpStrength() {
    return 0.4000000059604645D + this.rand.nextDouble() * 0.2D + this.rand.nextDouble() * 0.2D + this.rand.nextDouble() * 0.2D;
  }
  
  protected double getModifiedMovementSpeed() {
    return (0.44999998807907104D + this.rand.nextDouble() * 0.3D + this.rand.nextDouble() * 0.3D + this.rand.nextDouble() * 0.3D) * 0.25D;
  }
  
  public boolean isOnLadder() {
    return false;
  }
  
  public float getEyeHeight() {
    return this.height;
  }
  
  public boolean func_190677_dK() {
    return false;
  }
  
  public boolean func_190682_f(ItemStack p_190682_1_) {
    return false;
  }
  
  public boolean replaceItemInInventory(int inventorySlot, ItemStack itemStackIn) {
    int i = inventorySlot - 400;
    if (i >= 0 && i < 2 && i < this.horseChest.getSizeInventory()) {
      if (i == 0 && itemStackIn.getItem() != Items.SADDLE)
        return false; 
      if (i != 1 || (func_190677_dK() && func_190682_f(itemStackIn))) {
        this.horseChest.setInventorySlotContents(i, itemStackIn);
        updateHorseSlots();
        return true;
      } 
      return false;
    } 
    int j = inventorySlot - 500 + 2;
    if (j >= 2 && j < this.horseChest.getSizeInventory()) {
      this.horseChest.setInventorySlotContents(j, itemStackIn);
      return true;
    } 
    return false;
  }
  
  @Nullable
  public Entity getControllingPassenger() {
    return getPassengers().isEmpty() ? null : getPassengers().get(0);
  }
  
  @Nullable
  public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
    livingdata = super.onInitialSpawn(difficulty, livingdata);
    if (this.rand.nextInt(5) == 0)
      setGrowingAge(-24000); 
    return livingdata;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\entity\passive\AbstractHorse.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
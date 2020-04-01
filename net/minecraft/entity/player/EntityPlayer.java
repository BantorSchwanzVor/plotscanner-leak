package net.minecraft.entity.player;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.passive.EntityParrot;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryEnderChest;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.Packet;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.CommandBlockBaseLogic;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.tileentity.TileEntityStructure;
import net.minecraft.util.CooldownTracker;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.FoodStats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.DataFixesManager;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.IDataFixer;
import net.minecraft.util.datafix.IDataWalker;
import net.minecraft.util.datafix.IFixType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.GameType;
import net.minecraft.world.IInteractionObject;
import net.minecraft.world.LockCode;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public abstract class EntityPlayer extends EntityLivingBase {
  private static final DataParameter<Float> ABSORPTION = EntityDataManager.createKey(EntityPlayer.class, DataSerializers.FLOAT);
  
  private static final DataParameter<Integer> PLAYER_SCORE = EntityDataManager.createKey(EntityPlayer.class, DataSerializers.VARINT);
  
  protected static final DataParameter<Byte> PLAYER_MODEL_FLAG = EntityDataManager.createKey(EntityPlayer.class, DataSerializers.BYTE);
  
  protected static final DataParameter<Byte> MAIN_HAND = EntityDataManager.createKey(EntityPlayer.class, DataSerializers.BYTE);
  
  protected static final DataParameter<NBTTagCompound> field_192032_bt = EntityDataManager.createKey(EntityPlayer.class, DataSerializers.field_192734_n);
  
  protected static final DataParameter<NBTTagCompound> field_192033_bu = EntityDataManager.createKey(EntityPlayer.class, DataSerializers.field_192734_n);
  
  public InventoryPlayer inventory = new InventoryPlayer(this);
  
  protected InventoryEnderChest theInventoryEnderChest = new InventoryEnderChest();
  
  public Container inventoryContainer;
  
  public Container openContainer;
  
  protected FoodStats foodStats = new FoodStats();
  
  protected int flyToggleTimer;
  
  public float prevCameraYaw;
  
  public float cameraYaw;
  
  public int xpCooldown;
  
  public double prevChasingPosX;
  
  public double prevChasingPosY;
  
  public double prevChasingPosZ;
  
  public double chasingPosX;
  
  public double chasingPosY;
  
  public double chasingPosZ;
  
  protected boolean sleeping;
  
  public BlockPos bedLocation;
  
  private int sleepTimer;
  
  public float renderOffsetX;
  
  public float renderOffsetY;
  
  public float renderOffsetZ;
  
  private BlockPos spawnChunk;
  
  private boolean spawnForced;
  
  public PlayerCapabilities capabilities = new PlayerCapabilities();
  
  public int experienceLevel;
  
  public int experienceTotal;
  
  public float experience;
  
  protected int xpSeed;
  
  protected float speedInAir = 0.02F;
  
  private int lastXPSound;
  
  private final GameProfile gameProfile;
  
  private boolean hasReducedDebug;
  
  private ItemStack itemStackMainHand = ItemStack.field_190927_a;
  
  private final CooldownTracker cooldownTracker = createCooldownTracker();
  
  @Nullable
  public EntityFishHook fishEntity;
  
  protected CooldownTracker createCooldownTracker() {
    return new CooldownTracker();
  }
  
  public EntityPlayer(World worldIn, GameProfile gameProfileIn) {
    super(worldIn);
    setUniqueId(getUUID(gameProfileIn));
    this.gameProfile = gameProfileIn;
    this.inventoryContainer = (Container)new ContainerPlayer(this.inventory, !worldIn.isRemote, this);
    this.openContainer = this.inventoryContainer;
    BlockPos blockpos = worldIn.getSpawnPoint();
    setLocationAndAngles(blockpos.getX() + 0.5D, (blockpos.getY() + 1), blockpos.getZ() + 0.5D, 0.0F, 0.0F);
    this.unused180 = 180.0F;
  }
  
  protected void applyEntityAttributes() {
    super.applyEntityAttributes();
    getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(1.0D);
    getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.10000000149011612D);
    getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_SPEED);
    getAttributeMap().registerAttribute(SharedMonsterAttributes.LUCK);
  }
  
  protected void entityInit() {
    super.entityInit();
    this.dataManager.register(ABSORPTION, Float.valueOf(0.0F));
    this.dataManager.register(PLAYER_SCORE, Integer.valueOf(0));
    this.dataManager.register(PLAYER_MODEL_FLAG, Byte.valueOf((byte)0));
    this.dataManager.register(MAIN_HAND, Byte.valueOf((byte)1));
    this.dataManager.register(field_192032_bt, new NBTTagCompound());
    this.dataManager.register(field_192033_bu, new NBTTagCompound());
  }
  
  public void onUpdate() {
    this.noClip = isSpectator();
    if (isSpectator())
      this.onGround = false; 
    if (this.xpCooldown > 0)
      this.xpCooldown--; 
    if (isPlayerSleeping()) {
      this.sleepTimer++;
      if (this.sleepTimer > 100)
        this.sleepTimer = 100; 
      if (!this.world.isRemote)
        if (!isInBed()) {
          wakeUpPlayer(true, true, false);
        } else if (this.world.isDaytime()) {
          wakeUpPlayer(false, true, true);
        }  
    } else if (this.sleepTimer > 0) {
      this.sleepTimer++;
      if (this.sleepTimer >= 110)
        this.sleepTimer = 0; 
    } 
    super.onUpdate();
    if (!this.world.isRemote && this.openContainer != null && !this.openContainer.canInteractWith(this)) {
      closeScreen();
      this.openContainer = this.inventoryContainer;
    } 
    if (isBurning() && this.capabilities.disableDamage)
      extinguish(); 
    updateCape();
    if (!this.world.isRemote) {
      this.foodStats.onUpdate(this);
      addStat(StatList.PLAY_ONE_MINUTE);
      if (isEntityAlive())
        addStat(StatList.TIME_SINCE_DEATH); 
      if (isSneaking())
        addStat(StatList.SNEAK_TIME); 
    } 
    int i = 29999999;
    double d0 = MathHelper.clamp(this.posX, -2.9999999E7D, 2.9999999E7D);
    double d1 = MathHelper.clamp(this.posZ, -2.9999999E7D, 2.9999999E7D);
    if (d0 != this.posX || d1 != this.posZ)
      setPosition(d0, this.posY, d1); 
    this.ticksSinceLastSwing++;
    ItemStack itemstack = getHeldItemMainhand();
    if (!ItemStack.areItemStacksEqual(this.itemStackMainHand, itemstack)) {
      if (!ItemStack.areItemsEqualIgnoreDurability(this.itemStackMainHand, itemstack))
        resetCooldown(); 
      this.itemStackMainHand = itemstack.func_190926_b() ? ItemStack.field_190927_a : itemstack.copy();
    } 
    this.cooldownTracker.tick();
    updateSize();
  }
  
  private void updateCape() {
    this.prevChasingPosX = this.chasingPosX;
    this.prevChasingPosY = this.chasingPosY;
    this.prevChasingPosZ = this.chasingPosZ;
    double d0 = this.posX - this.chasingPosX;
    double d1 = this.posY - this.chasingPosY;
    double d2 = this.posZ - this.chasingPosZ;
    double d3 = 10.0D;
    if (d0 > 10.0D) {
      this.chasingPosX = this.posX;
      this.prevChasingPosX = this.chasingPosX;
    } 
    if (d2 > 10.0D) {
      this.chasingPosZ = this.posZ;
      this.prevChasingPosZ = this.chasingPosZ;
    } 
    if (d1 > 10.0D) {
      this.chasingPosY = this.posY;
      this.prevChasingPosY = this.chasingPosY;
    } 
    if (d0 < -10.0D) {
      this.chasingPosX = this.posX;
      this.prevChasingPosX = this.chasingPosX;
    } 
    if (d2 < -10.0D) {
      this.chasingPosZ = this.posZ;
      this.prevChasingPosZ = this.chasingPosZ;
    } 
    if (d1 < -10.0D) {
      this.chasingPosY = this.posY;
      this.prevChasingPosY = this.chasingPosY;
    } 
    this.chasingPosX += d0 * 0.25D;
    this.chasingPosZ += d2 * 0.25D;
    this.chasingPosY += d1 * 0.25D;
  }
  
  protected void updateSize() {
    float f;
    float f1;
    if (isElytraFlying()) {
      f = 0.6F;
      f1 = 0.6F;
    } else if (isPlayerSleeping()) {
      f = 0.2F;
      f1 = 0.2F;
    } else if (isSneaking()) {
      f = 0.6F;
      f1 = 1.65F;
    } else {
      f = 0.6F;
      f1 = 1.8F;
    } 
    if (f != this.width || f1 != this.height) {
      AxisAlignedBB axisalignedbb = getEntityBoundingBox();
      axisalignedbb = new AxisAlignedBB(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ, axisalignedbb.minX + f, axisalignedbb.minY + f1, axisalignedbb.minZ + f);
      if (!this.world.collidesWithAnyBlock(axisalignedbb))
        setSize(f, f1); 
    } 
  }
  
  public int getMaxInPortalTime() {
    return this.capabilities.disableDamage ? 1 : 80;
  }
  
  protected SoundEvent getSwimSound() {
    return SoundEvents.ENTITY_PLAYER_SWIM;
  }
  
  protected SoundEvent getSplashSound() {
    return SoundEvents.ENTITY_PLAYER_SPLASH;
  }
  
  public int getPortalCooldown() {
    return 10;
  }
  
  public void playSound(SoundEvent soundIn, float volume, float pitch) {
    this.world.playSound(this, this.posX, this.posY, this.posZ, soundIn, getSoundCategory(), volume, pitch);
  }
  
  public SoundCategory getSoundCategory() {
    return SoundCategory.PLAYERS;
  }
  
  protected int func_190531_bD() {
    return 20;
  }
  
  public void handleStatusUpdate(byte id) {
    if (id == 9) {
      onItemUseFinish();
    } else if (id == 23) {
      this.hasReducedDebug = false;
    } else if (id == 22) {
      this.hasReducedDebug = true;
    } else {
      super.handleStatusUpdate(id);
    } 
  }
  
  protected boolean isMovementBlocked() {
    return !(getHealth() > 0.0F && !isPlayerSleeping());
  }
  
  protected void closeScreen() {
    this.openContainer = this.inventoryContainer;
  }
  
  public void updateRidden() {
    if (!this.world.isRemote && isSneaking() && isRiding()) {
      dismountRidingEntity();
      setSneaking(false);
    } else {
      double d0 = this.posX;
      double d1 = this.posY;
      double d2 = this.posZ;
      float f = this.rotationYaw;
      float f1 = this.rotationPitch;
      super.updateRidden();
      this.prevCameraYaw = this.cameraYaw;
      this.cameraYaw = 0.0F;
      addMountedMovementStat(this.posX - d0, this.posY - d1, this.posZ - d2);
      if (getRidingEntity() instanceof EntityPig) {
        this.rotationPitch = f1;
        this.rotationYaw = f;
        this.renderYawOffset = ((EntityPig)getRidingEntity()).renderYawOffset;
      } 
    } 
  }
  
  public void preparePlayerToSpawn() {
    setSize(0.6F, 1.8F);
    super.preparePlayerToSpawn();
    setHealth(getMaxHealth());
    this.deathTime = 0;
  }
  
  protected void updateEntityActionState() {
    super.updateEntityActionState();
    updateArmSwingProgress();
    this.rotationYawHead = this.rotationYaw;
  }
  
  public void onLivingUpdate() {
    if (this.flyToggleTimer > 0)
      this.flyToggleTimer--; 
    if (this.world.getDifficulty() == EnumDifficulty.PEACEFUL && this.world.getGameRules().getBoolean("naturalRegeneration")) {
      if (getHealth() < getMaxHealth() && this.ticksExisted % 20 == 0)
        heal(1.0F); 
      if (this.foodStats.needFood() && this.ticksExisted % 10 == 0)
        this.foodStats.setFoodLevel(this.foodStats.getFoodLevel() + 1); 
    } 
    this.inventory.decrementAnimations();
    this.prevCameraYaw = this.cameraYaw;
    super.onLivingUpdate();
    IAttributeInstance iattributeinstance = getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
    if (!this.world.isRemote)
      iattributeinstance.setBaseValue(this.capabilities.getWalkSpeed()); 
    this.jumpMovementFactor = this.speedInAir;
    if (isSprinting())
      this.jumpMovementFactor = (float)(this.jumpMovementFactor + this.speedInAir * 0.3D); 
    setAIMoveSpeed((float)iattributeinstance.getAttributeValue());
    float f = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
    float f1 = (float)(Math.atan(-this.motionY * 0.20000000298023224D) * 15.0D);
    if (f > 0.1F)
      f = 0.1F; 
    if (!this.onGround || getHealth() <= 0.0F)
      f = 0.0F; 
    if (this.onGround || getHealth() <= 0.0F)
      f1 = 0.0F; 
    this.cameraYaw += (f - this.cameraYaw) * 0.4F;
    this.cameraPitch += (f1 - this.cameraPitch) * 0.8F;
    if (getHealth() > 0.0F && !isSpectator()) {
      AxisAlignedBB axisalignedbb;
      if (isRiding() && !(getRidingEntity()).isDead) {
        axisalignedbb = getEntityBoundingBox().union(getRidingEntity().getEntityBoundingBox()).expand(1.0D, 0.0D, 1.0D);
      } else {
        axisalignedbb = getEntityBoundingBox().expand(1.0D, 0.5D, 1.0D);
      } 
      List<Entity> list = this.world.getEntitiesWithinAABBExcludingEntity((Entity)this, axisalignedbb);
      for (int i = 0; i < list.size(); i++) {
        Entity entity = list.get(i);
        if (!entity.isDead)
          collideWithPlayer(entity); 
      } 
    } 
    func_192028_j(func_192023_dk());
    func_192028_j(func_192025_dl());
    if ((!this.world.isRemote && (this.fallDistance > 0.5F || isInWater() || isRiding())) || this.capabilities.isFlying)
      func_192030_dh(); 
  }
  
  private void func_192028_j(@Nullable NBTTagCompound p_192028_1_) {
    if ((p_192028_1_ != null && !p_192028_1_.hasKey("Silent")) || !p_192028_1_.getBoolean("Silent")) {
      String s = p_192028_1_.getString("id");
      if (s.equals(EntityList.func_191306_a(EntityParrot.class).toString()))
        EntityParrot.func_192005_a(this.world, (Entity)this); 
    } 
  }
  
  private void collideWithPlayer(Entity entityIn) {
    entityIn.onCollideWithPlayer(this);
  }
  
  public int getScore() {
    return ((Integer)this.dataManager.get(PLAYER_SCORE)).intValue();
  }
  
  public void setScore(int scoreIn) {
    this.dataManager.set(PLAYER_SCORE, Integer.valueOf(scoreIn));
  }
  
  public void addScore(int scoreIn) {
    int i = getScore();
    this.dataManager.set(PLAYER_SCORE, Integer.valueOf(i + scoreIn));
  }
  
  public void onDeath(DamageSource cause) {
    super.onDeath(cause);
    setSize(0.2F, 0.2F);
    setPosition(this.posX, this.posY, this.posZ);
    this.motionY = 0.10000000149011612D;
    if ("Notch".equals(getName()))
      dropItem(new ItemStack(Items.APPLE, 1), true, false); 
    if (!this.world.getGameRules().getBoolean("keepInventory") && !isSpectator()) {
      func_190776_cN();
      this.inventory.dropAllItems();
    } 
    if (cause != null) {
      this.motionX = (-MathHelper.cos((this.attackedAtYaw + this.rotationYaw) * 0.017453292F) * 0.1F);
      this.motionZ = (-MathHelper.sin((this.attackedAtYaw + this.rotationYaw) * 0.017453292F) * 0.1F);
    } else {
      this.motionX = 0.0D;
      this.motionZ = 0.0D;
    } 
    addStat(StatList.DEATHS);
    takeStat(StatList.TIME_SINCE_DEATH);
    extinguish();
    setFlag(0, false);
  }
  
  protected void func_190776_cN() {
    for (int i = 0; i < this.inventory.getSizeInventory(); i++) {
      ItemStack itemstack = this.inventory.getStackInSlot(i);
      if (!itemstack.func_190926_b() && EnchantmentHelper.func_190939_c(itemstack))
        this.inventory.removeStackFromSlot(i); 
    } 
  }
  
  protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
    if (p_184601_1_ == DamageSource.onFire)
      return SoundEvents.field_193806_fH; 
    return (p_184601_1_ == DamageSource.drown) ? SoundEvents.field_193805_fG : SoundEvents.ENTITY_PLAYER_HURT;
  }
  
  protected SoundEvent getDeathSound() {
    return SoundEvents.ENTITY_PLAYER_DEATH;
  }
  
  @Nullable
  public EntityItem dropItem(boolean dropAll) {
    return dropItem(this.inventory.decrStackSize(this.inventory.currentItem, (dropAll && !this.inventory.getCurrentItem().func_190926_b()) ? this.inventory.getCurrentItem().func_190916_E() : 1), false, true);
  }
  
  @Nullable
  public EntityItem dropItem(ItemStack itemStackIn, boolean unused) {
    return dropItem(itemStackIn, false, unused);
  }
  
  @Nullable
  public EntityItem dropItem(ItemStack droppedItem, boolean dropAround, boolean traceItem) {
    if (droppedItem.func_190926_b())
      return null; 
    double d0 = this.posY - 0.30000001192092896D + getEyeHeight();
    EntityItem entityitem = new EntityItem(this.world, this.posX, d0, this.posZ, droppedItem);
    entityitem.setPickupDelay(40);
    if (traceItem)
      entityitem.setThrower(getName()); 
    if (dropAround) {
      float f = this.rand.nextFloat() * 0.5F;
      float f1 = this.rand.nextFloat() * 6.2831855F;
      entityitem.motionX = (-MathHelper.sin(f1) * f);
      entityitem.motionZ = (MathHelper.cos(f1) * f);
      entityitem.motionY = 0.20000000298023224D;
    } else {
      float f2 = 0.3F;
      entityitem.motionX = (-MathHelper.sin(this.rotationYaw * 0.017453292F) * MathHelper.cos(this.rotationPitch * 0.017453292F) * f2);
      entityitem.motionZ = (MathHelper.cos(this.rotationYaw * 0.017453292F) * MathHelper.cos(this.rotationPitch * 0.017453292F) * f2);
      entityitem.motionY = (-MathHelper.sin(this.rotationPitch * 0.017453292F) * f2 + 0.1F);
      float f3 = this.rand.nextFloat() * 6.2831855F;
      f2 = 0.02F * this.rand.nextFloat();
      entityitem.motionX += Math.cos(f3) * f2;
      entityitem.motionY += ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F);
      entityitem.motionZ += Math.sin(f3) * f2;
    } 
    ItemStack itemstack = dropItemAndGetStack(entityitem);
    if (traceItem) {
      if (!itemstack.func_190926_b())
        addStat(StatList.getDroppedObjectStats(itemstack.getItem()), droppedItem.func_190916_E()); 
      addStat(StatList.DROP);
    } 
    return entityitem;
  }
  
  protected ItemStack dropItemAndGetStack(EntityItem p_184816_1_) {
    this.world.spawnEntityInWorld((Entity)p_184816_1_);
    return p_184816_1_.getEntityItem();
  }
  
  public float getDigSpeed(IBlockState state) {
    float f = this.inventory.getStrVsBlock(state);
    if (f > 1.0F) {
      int i = EnchantmentHelper.getEfficiencyModifier(this);
      ItemStack itemstack = getHeldItemMainhand();
      if (i > 0 && !itemstack.func_190926_b())
        f += (i * i + 1); 
    } 
    if (isPotionActive(MobEffects.HASTE))
      f *= 1.0F + (getActivePotionEffect(MobEffects.HASTE).getAmplifier() + 1) * 0.2F; 
    if (isPotionActive(MobEffects.MINING_FATIGUE)) {
      float f1;
      switch (getActivePotionEffect(MobEffects.MINING_FATIGUE).getAmplifier()) {
        case 0:
          f1 = 0.3F;
          break;
        case 1:
          f1 = 0.09F;
          break;
        case 2:
          f1 = 0.0027F;
          break;
        default:
          f1 = 8.1E-4F;
          break;
      } 
      f *= f1;
    } 
    if (isInsideOfMaterial(Material.WATER) && !EnchantmentHelper.getAquaAffinityModifier(this))
      f /= 5.0F; 
    if (!this.onGround)
      f /= 5.0F; 
    return f;
  }
  
  public boolean canHarvestBlock(IBlockState state) {
    return this.inventory.canHarvestBlock(state);
  }
  
  public static void registerFixesPlayer(DataFixer fixer) {
    fixer.registerWalker(FixTypes.PLAYER, new IDataWalker() {
          public NBTTagCompound process(IDataFixer fixer, NBTTagCompound compound, int versionIn) {
            DataFixesManager.processInventory(fixer, compound, versionIn, "Inventory");
            DataFixesManager.processInventory(fixer, compound, versionIn, "EnderItems");
            if (compound.hasKey("ShoulderEntityLeft", 10))
              compound.setTag("ShoulderEntityLeft", (NBTBase)fixer.process((IFixType)FixTypes.ENTITY, compound.getCompoundTag("ShoulderEntityLeft"), versionIn)); 
            if (compound.hasKey("ShoulderEntityRight", 10))
              compound.setTag("ShoulderEntityRight", (NBTBase)fixer.process((IFixType)FixTypes.ENTITY, compound.getCompoundTag("ShoulderEntityRight"), versionIn)); 
            return compound;
          }
        });
  }
  
  public void readEntityFromNBT(NBTTagCompound compound) {
    super.readEntityFromNBT(compound);
    setUniqueId(getUUID(this.gameProfile));
    NBTTagList nbttaglist = compound.getTagList("Inventory", 10);
    this.inventory.readFromNBT(nbttaglist);
    this.inventory.currentItem = compound.getInteger("SelectedItemSlot");
    this.sleeping = compound.getBoolean("Sleeping");
    this.sleepTimer = compound.getShort("SleepTimer");
    this.experience = compound.getFloat("XpP");
    this.experienceLevel = compound.getInteger("XpLevel");
    this.experienceTotal = compound.getInteger("XpTotal");
    this.xpSeed = compound.getInteger("XpSeed");
    if (this.xpSeed == 0)
      this.xpSeed = this.rand.nextInt(); 
    setScore(compound.getInteger("Score"));
    if (this.sleeping) {
      this.bedLocation = new BlockPos((Entity)this);
      wakeUpPlayer(true, true, false);
    } 
    if (compound.hasKey("SpawnX", 99) && compound.hasKey("SpawnY", 99) && compound.hasKey("SpawnZ", 99)) {
      this.spawnChunk = new BlockPos(compound.getInteger("SpawnX"), compound.getInteger("SpawnY"), compound.getInteger("SpawnZ"));
      this.spawnForced = compound.getBoolean("SpawnForced");
    } 
    this.foodStats.readNBT(compound);
    this.capabilities.readCapabilitiesFromNBT(compound);
    if (compound.hasKey("EnderItems", 9)) {
      NBTTagList nbttaglist1 = compound.getTagList("EnderItems", 10);
      this.theInventoryEnderChest.loadInventoryFromNBT(nbttaglist1);
    } 
    if (compound.hasKey("ShoulderEntityLeft", 10))
      func_192029_h(compound.getCompoundTag("ShoulderEntityLeft")); 
    if (compound.hasKey("ShoulderEntityRight", 10))
      func_192031_i(compound.getCompoundTag("ShoulderEntityRight")); 
  }
  
  public void writeEntityToNBT(NBTTagCompound compound) {
    super.writeEntityToNBT(compound);
    compound.setInteger("DataVersion", 1343);
    compound.setTag("Inventory", (NBTBase)this.inventory.writeToNBT(new NBTTagList()));
    compound.setInteger("SelectedItemSlot", this.inventory.currentItem);
    compound.setBoolean("Sleeping", this.sleeping);
    compound.setShort("SleepTimer", (short)this.sleepTimer);
    compound.setFloat("XpP", this.experience);
    compound.setInteger("XpLevel", this.experienceLevel);
    compound.setInteger("XpTotal", this.experienceTotal);
    compound.setInteger("XpSeed", this.xpSeed);
    compound.setInteger("Score", getScore());
    if (this.spawnChunk != null) {
      compound.setInteger("SpawnX", this.spawnChunk.getX());
      compound.setInteger("SpawnY", this.spawnChunk.getY());
      compound.setInteger("SpawnZ", this.spawnChunk.getZ());
      compound.setBoolean("SpawnForced", this.spawnForced);
    } 
    this.foodStats.writeNBT(compound);
    this.capabilities.writeCapabilitiesToNBT(compound);
    compound.setTag("EnderItems", (NBTBase)this.theInventoryEnderChest.saveInventoryToNBT());
    if (!func_192023_dk().hasNoTags())
      compound.setTag("ShoulderEntityLeft", (NBTBase)func_192023_dk()); 
    if (!func_192025_dl().hasNoTags())
      compound.setTag("ShoulderEntityRight", (NBTBase)func_192025_dl()); 
  }
  
  public boolean attackEntityFrom(DamageSource source, float amount) {
    if (isEntityInvulnerable(source))
      return false; 
    if (this.capabilities.disableDamage && !source.canHarmInCreative())
      return false; 
    this.entityAge = 0;
    if (getHealth() <= 0.0F)
      return false; 
    if (isPlayerSleeping() && !this.world.isRemote)
      wakeUpPlayer(true, true, false); 
    func_192030_dh();
    if (source.isDifficultyScaled()) {
      if (this.world.getDifficulty() == EnumDifficulty.PEACEFUL)
        amount = 0.0F; 
      if (this.world.getDifficulty() == EnumDifficulty.EASY)
        amount = Math.min(amount / 2.0F + 1.0F, amount); 
      if (this.world.getDifficulty() == EnumDifficulty.HARD)
        amount = amount * 3.0F / 2.0F; 
    } 
    return (amount == 0.0F) ? false : super.attackEntityFrom(source, amount);
  }
  
  protected void func_190629_c(EntityLivingBase p_190629_1_) {
    super.func_190629_c(p_190629_1_);
    if (p_190629_1_.getHeldItemMainhand().getItem() instanceof net.minecraft.item.ItemAxe)
      func_190777_m(true); 
  }
  
  public boolean canAttackPlayer(EntityPlayer other) {
    Team team = getTeam();
    Team team1 = other.getTeam();
    if (team == null)
      return true; 
    return !team.isSameTeam(team1) ? true : team.getAllowFriendlyFire();
  }
  
  protected void damageArmor(float damage) {
    this.inventory.damageArmor(damage);
  }
  
  protected void damageShield(float damage) {
    if (damage >= 3.0F && this.activeItemStack.getItem() == Items.SHIELD) {
      int i = 1 + MathHelper.floor(damage);
      this.activeItemStack.damageItem(i, this);
      if (this.activeItemStack.func_190926_b()) {
        EnumHand enumhand = getActiveHand();
        if (enumhand == EnumHand.MAIN_HAND) {
          setItemStackToSlot(EntityEquipmentSlot.MAINHAND, ItemStack.field_190927_a);
        } else {
          setItemStackToSlot(EntityEquipmentSlot.OFFHAND, ItemStack.field_190927_a);
        } 
        this.activeItemStack = ItemStack.field_190927_a;
        playSound(SoundEvents.ITEM_SHIELD_BREAK, 0.8F, 0.8F + this.world.rand.nextFloat() * 0.4F);
      } 
    } 
  }
  
  public float getArmorVisibility() {
    int i = 0;
    for (ItemStack itemstack : this.inventory.armorInventory) {
      if (!itemstack.func_190926_b())
        i++; 
    } 
    return i / this.inventory.armorInventory.size();
  }
  
  protected void damageEntity(DamageSource damageSrc, float damageAmount) {
    if (!isEntityInvulnerable(damageSrc)) {
      damageAmount = applyArmorCalculations(damageSrc, damageAmount);
      damageAmount = applyPotionDamageCalculations(damageSrc, damageAmount);
      float f = damageAmount;
      damageAmount = Math.max(damageAmount - getAbsorptionAmount(), 0.0F);
      setAbsorptionAmount(getAbsorptionAmount() - f - damageAmount);
      if (damageAmount != 0.0F) {
        addExhaustion(damageSrc.getHungerDamage());
        float f1 = getHealth();
        setHealth(getHealth() - damageAmount);
        getCombatTracker().trackDamage(damageSrc, f1, damageAmount);
        if (damageAmount < 3.4028235E37F)
          addStat(StatList.DAMAGE_TAKEN, Math.round(damageAmount * 10.0F)); 
      } 
    } 
  }
  
  public void openEditSign(TileEntitySign signTile) {}
  
  public void displayGuiEditCommandCart(CommandBlockBaseLogic commandBlock) {}
  
  public void displayGuiCommandBlock(TileEntityCommandBlock commandBlock) {}
  
  public void openEditStructure(TileEntityStructure structure) {}
  
  public void displayVillagerTradeGui(IMerchant villager) {}
  
  public void displayGUIChest(IInventory chestInventory) {}
  
  public void openGuiHorseInventory(AbstractHorse horse, IInventory inventoryIn) {}
  
  public void displayGui(IInteractionObject guiOwner) {}
  
  public void openBook(ItemStack stack, EnumHand hand) {}
  
  public EnumActionResult func_190775_a(Entity p_190775_1_, EnumHand p_190775_2_) {
    if (isSpectator()) {
      if (p_190775_1_ instanceof IInventory)
        displayGUIChest((IInventory)p_190775_1_); 
      return EnumActionResult.PASS;
    } 
    ItemStack itemstack = getHeldItem(p_190775_2_);
    ItemStack itemstack1 = itemstack.func_190926_b() ? ItemStack.field_190927_a : itemstack.copy();
    if (p_190775_1_.processInitialInteract(this, p_190775_2_)) {
      if (this.capabilities.isCreativeMode && itemstack == getHeldItem(p_190775_2_) && itemstack.func_190916_E() < itemstack1.func_190916_E())
        itemstack.func_190920_e(itemstack1.func_190916_E()); 
      return EnumActionResult.SUCCESS;
    } 
    if (!itemstack.func_190926_b() && p_190775_1_ instanceof EntityLivingBase) {
      if (this.capabilities.isCreativeMode)
        itemstack = itemstack1; 
      if (itemstack.interactWithEntity(this, (EntityLivingBase)p_190775_1_, p_190775_2_)) {
        if (itemstack.func_190926_b() && !this.capabilities.isCreativeMode)
          setHeldItem(p_190775_2_, ItemStack.field_190927_a); 
        return EnumActionResult.SUCCESS;
      } 
    } 
    return EnumActionResult.PASS;
  }
  
  public double getYOffset() {
    return -0.35D;
  }
  
  public void dismountRidingEntity() {
    super.dismountRidingEntity();
    this.rideCooldown = 0;
  }
  
  public void attackTargetEntityWithCurrentItem(Entity targetEntity) {
    if (targetEntity.canBeAttackedWithItem())
      if (!targetEntity.hitByEntity((Entity)this)) {
        float f1, f = (float)getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
        if (targetEntity instanceof EntityLivingBase) {
          f1 = EnchantmentHelper.getModifierForCreature(getHeldItemMainhand(), ((EntityLivingBase)targetEntity).getCreatureAttribute());
        } else {
          f1 = EnchantmentHelper.getModifierForCreature(getHeldItemMainhand(), EnumCreatureAttribute.UNDEFINED);
        } 
        float f2 = getCooledAttackStrength(0.5F);
        f *= 0.2F + f2 * f2 * 0.8F;
        f1 *= f2;
        resetCooldown();
        if (f > 0.0F || f1 > 0.0F) {
          boolean flag = (f2 > 0.9F);
          boolean flag1 = false;
          int i = 0;
          i += EnchantmentHelper.getKnockbackModifier(this);
          if (isSprinting() && flag) {
            this.world.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_KNOCKBACK, getSoundCategory(), 1.0F, 1.0F);
            i++;
            flag1 = true;
          } 
          boolean flag2 = (flag && this.fallDistance > 0.0F && !this.onGround && !isOnLadder() && !isInWater() && !isPotionActive(MobEffects.BLINDNESS) && !isRiding() && targetEntity instanceof EntityLivingBase);
          flag2 = (flag2 && !isSprinting());
          if (flag2)
            f *= 1.5F; 
          f += f1;
          boolean flag3 = false;
          double d0 = (this.distanceWalkedModified - this.prevDistanceWalkedModified);
          if (flag && !flag2 && !flag1 && this.onGround && d0 < getAIMoveSpeed()) {
            ItemStack itemstack = getHeldItem(EnumHand.MAIN_HAND);
            if (itemstack.getItem() instanceof net.minecraft.item.ItemSword)
              flag3 = true; 
          } 
          float f4 = 0.0F;
          boolean flag4 = false;
          int j = EnchantmentHelper.getFireAspectModifier(this);
          if (targetEntity instanceof EntityLivingBase) {
            f4 = ((EntityLivingBase)targetEntity).getHealth();
            if (j > 0 && !targetEntity.isBurning()) {
              flag4 = true;
              targetEntity.setFire(1);
            } 
          } 
          double d1 = targetEntity.motionX;
          double d2 = targetEntity.motionY;
          double d3 = targetEntity.motionZ;
          boolean flag5 = targetEntity.attackEntityFrom(DamageSource.causePlayerDamage(this), f);
          if (flag5) {
            EntityLivingBase entityLivingBase;
            if (i > 0) {
              if (targetEntity instanceof EntityLivingBase) {
                ((EntityLivingBase)targetEntity).knockBack((Entity)this, i * 0.5F, MathHelper.sin(this.rotationYaw * 0.017453292F), -MathHelper.cos(this.rotationYaw * 0.017453292F));
              } else {
                targetEntity.addVelocity((-MathHelper.sin(this.rotationYaw * 0.017453292F) * i * 0.5F), 0.1D, (MathHelper.cos(this.rotationYaw * 0.017453292F) * i * 0.5F));
              } 
              this.motionX *= 0.6D;
              this.motionZ *= 0.6D;
              setSprinting(false);
            } 
            if (flag3) {
              float f3 = 1.0F + EnchantmentHelper.func_191527_a(this) * f;
              for (EntityLivingBase entitylivingbase : this.world.getEntitiesWithinAABB(EntityLivingBase.class, targetEntity.getEntityBoundingBox().expand(1.0D, 0.25D, 1.0D))) {
                if (entityLivingBase != this && entityLivingBase != targetEntity && !isOnSameTeam((Entity)entityLivingBase) && getDistanceSqToEntity((Entity)entityLivingBase) < 9.0D) {
                  entityLivingBase.knockBack((Entity)this, 0.4F, MathHelper.sin(this.rotationYaw * 0.017453292F), -MathHelper.cos(this.rotationYaw * 0.017453292F));
                  entityLivingBase.attackEntityFrom(DamageSource.causePlayerDamage(this), f3);
                } 
              } 
              this.world.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, getSoundCategory(), 1.0F, 1.0F);
              spawnSweepParticles();
            } 
            if (targetEntity instanceof EntityPlayerMP && targetEntity.velocityChanged) {
              ((EntityPlayerMP)targetEntity).connection.sendPacket((Packet)new SPacketEntityVelocity(targetEntity));
              targetEntity.velocityChanged = false;
              targetEntity.motionX = d1;
              targetEntity.motionY = d2;
              targetEntity.motionZ = d3;
            } 
            if (flag2) {
              this.world.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_CRIT, getSoundCategory(), 1.0F, 1.0F);
              onCriticalHit(targetEntity);
            } 
            if (!flag2 && !flag3)
              if (flag) {
                this.world.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_STRONG, getSoundCategory(), 1.0F, 1.0F);
              } else {
                this.world.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_WEAK, getSoundCategory(), 1.0F, 1.0F);
              }  
            if (f1 > 0.0F)
              onEnchantmentCritical(targetEntity); 
            setLastAttacker(targetEntity);
            if (targetEntity instanceof EntityLivingBase)
              EnchantmentHelper.applyThornEnchantments((EntityLivingBase)targetEntity, (Entity)this); 
            EnchantmentHelper.applyArthropodEnchantments(this, targetEntity);
            ItemStack itemstack1 = getHeldItemMainhand();
            Entity entity = targetEntity;
            if (targetEntity instanceof MultiPartEntityPart) {
              IEntityMultiPart ientitymultipart = ((MultiPartEntityPart)targetEntity).entityDragonObj;
              if (ientitymultipart instanceof EntityLivingBase)
                entityLivingBase = (EntityLivingBase)ientitymultipart; 
            } 
            if (!itemstack1.func_190926_b() && entityLivingBase instanceof EntityLivingBase) {
              itemstack1.hitEntity(entityLivingBase, this);
              if (itemstack1.func_190926_b())
                setHeldItem(EnumHand.MAIN_HAND, ItemStack.field_190927_a); 
            } 
            if (targetEntity instanceof EntityLivingBase) {
              float f5 = f4 - ((EntityLivingBase)targetEntity).getHealth();
              addStat(StatList.DAMAGE_DEALT, Math.round(f5 * 10.0F));
              if (j > 0)
                targetEntity.setFire(j * 4); 
              if (this.world instanceof WorldServer && f5 > 2.0F) {
                int k = (int)(f5 * 0.5D);
                ((WorldServer)this.world).spawnParticle(EnumParticleTypes.DAMAGE_INDICATOR, targetEntity.posX, targetEntity.posY + (targetEntity.height * 0.5F), targetEntity.posZ, k, 0.1D, 0.0D, 0.1D, 0.2D, new int[0]);
              } 
            } 
            addExhaustion(0.1F);
          } else {
            this.world.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_NODAMAGE, getSoundCategory(), 1.0F, 1.0F);
            if (flag4)
              targetEntity.extinguish(); 
          } 
        } 
      }  
  }
  
  public void func_190777_m(boolean p_190777_1_) {
    float f = 0.25F + EnchantmentHelper.getEfficiencyModifier(this) * 0.05F;
    if (p_190777_1_)
      f += 0.75F; 
    if (this.rand.nextFloat() < f) {
      getCooldownTracker().setCooldown(Items.SHIELD, 100);
      resetActiveHand();
      this.world.setEntityState((Entity)this, (byte)30);
    } 
  }
  
  public void onCriticalHit(Entity entityHit) {}
  
  public void onEnchantmentCritical(Entity entityHit) {}
  
  public void spawnSweepParticles() {
    double d0 = -MathHelper.sin(this.rotationYaw * 0.017453292F);
    double d1 = MathHelper.cos(this.rotationYaw * 0.017453292F);
    if (this.world instanceof WorldServer)
      ((WorldServer)this.world).spawnParticle(EnumParticleTypes.SWEEP_ATTACK, this.posX + d0, this.posY + this.height * 0.5D, this.posZ + d1, 0, d0, 0.0D, d1, 0.0D, new int[0]); 
  }
  
  public void respawnPlayer() {}
  
  public void setDead() {
    super.setDead();
    this.inventoryContainer.onContainerClosed(this);
    if (this.openContainer != null)
      this.openContainer.onContainerClosed(this); 
  }
  
  public boolean isEntityInsideOpaqueBlock() {
    return (!this.sleeping && super.isEntityInsideOpaqueBlock());
  }
  
  public boolean isUser() {
    return false;
  }
  
  public GameProfile getGameProfile() {
    return this.gameProfile;
  }
  
  public SleepResult trySleep(BlockPos bedLocation) {
    EnumFacing enumfacing = (EnumFacing)this.world.getBlockState(bedLocation).getValue((IProperty)BlockHorizontal.FACING);
    if (!this.world.isRemote) {
      if (isPlayerSleeping() || !isEntityAlive())
        return SleepResult.OTHER_PROBLEM; 
      if (!this.world.provider.isSurfaceWorld())
        return SleepResult.NOT_POSSIBLE_HERE; 
      if (this.world.isDaytime())
        return SleepResult.NOT_POSSIBLE_NOW; 
      if (!func_190774_a(bedLocation, enumfacing))
        return SleepResult.TOO_FAR_AWAY; 
      double d0 = 8.0D;
      double d1 = 5.0D;
      List<EntityMob> list = this.world.getEntitiesWithinAABB(EntityMob.class, new AxisAlignedBB(bedLocation.getX() - 8.0D, bedLocation.getY() - 5.0D, bedLocation.getZ() - 8.0D, bedLocation.getX() + 8.0D, bedLocation.getY() + 5.0D, bedLocation.getZ() + 8.0D), new SleepEnemyPredicate(this, null));
      if (!list.isEmpty())
        return SleepResult.NOT_SAFE; 
    } 
    if (isRiding())
      dismountRidingEntity(); 
    func_192030_dh();
    setSize(0.2F, 0.2F);
    if (this.world.isBlockLoaded(bedLocation)) {
      float f1 = 0.5F + enumfacing.getFrontOffsetX() * 0.4F;
      float f = 0.5F + enumfacing.getFrontOffsetZ() * 0.4F;
      setRenderOffsetForSleep(enumfacing);
      setPosition((bedLocation.getX() + f1), (bedLocation.getY() + 0.6875F), (bedLocation.getZ() + f));
    } else {
      setPosition((bedLocation.getX() + 0.5F), (bedLocation.getY() + 0.6875F), (bedLocation.getZ() + 0.5F));
    } 
    this.sleeping = true;
    this.sleepTimer = 0;
    this.bedLocation = bedLocation;
    this.motionX = 0.0D;
    this.motionY = 0.0D;
    this.motionZ = 0.0D;
    if (!this.world.isRemote)
      this.world.updateAllPlayersSleepingFlag(); 
    return SleepResult.OK;
  }
  
  private boolean func_190774_a(BlockPos p_190774_1_, EnumFacing p_190774_2_) {
    if (Math.abs(this.posX - p_190774_1_.getX()) <= 3.0D && Math.abs(this.posY - p_190774_1_.getY()) <= 2.0D && Math.abs(this.posZ - p_190774_1_.getZ()) <= 3.0D)
      return true; 
    BlockPos blockpos = p_190774_1_.offset(p_190774_2_.getOpposite());
    return (Math.abs(this.posX - blockpos.getX()) <= 3.0D && Math.abs(this.posY - blockpos.getY()) <= 2.0D && Math.abs(this.posZ - blockpos.getZ()) <= 3.0D);
  }
  
  private void setRenderOffsetForSleep(EnumFacing p_175139_1_) {
    this.renderOffsetX = -1.8F * p_175139_1_.getFrontOffsetX();
    this.renderOffsetZ = -1.8F * p_175139_1_.getFrontOffsetZ();
  }
  
  public void wakeUpPlayer(boolean immediately, boolean updateWorldFlag, boolean setSpawn) {
    setSize(0.6F, 1.8F);
    IBlockState iblockstate = this.world.getBlockState(this.bedLocation);
    if (this.bedLocation != null && iblockstate.getBlock() == Blocks.BED) {
      this.world.setBlockState(this.bedLocation, iblockstate.withProperty((IProperty)BlockBed.OCCUPIED, Boolean.valueOf(false)), 4);
      BlockPos blockpos = BlockBed.getSafeExitLocation(this.world, this.bedLocation, 0);
      if (blockpos == null)
        blockpos = this.bedLocation.up(); 
      setPosition((blockpos.getX() + 0.5F), (blockpos.getY() + 0.1F), (blockpos.getZ() + 0.5F));
    } 
    this.sleeping = false;
    if (!this.world.isRemote && updateWorldFlag)
      this.world.updateAllPlayersSleepingFlag(); 
    this.sleepTimer = immediately ? 0 : 100;
    if (setSpawn)
      setSpawnPoint(this.bedLocation, false); 
  }
  
  private boolean isInBed() {
    return (this.world.getBlockState(this.bedLocation).getBlock() == Blocks.BED);
  }
  
  @Nullable
  public static BlockPos getBedSpawnLocation(World worldIn, BlockPos bedLocation, boolean forceSpawn) {
    Block block = worldIn.getBlockState(bedLocation).getBlock();
    if (block != Blocks.BED) {
      if (!forceSpawn)
        return null; 
      boolean flag = block.canSpawnInBlock();
      boolean flag1 = worldIn.getBlockState(bedLocation.up()).getBlock().canSpawnInBlock();
      return (flag && flag1) ? bedLocation : null;
    } 
    return BlockBed.getSafeExitLocation(worldIn, bedLocation, 0);
  }
  
  public float getBedOrientationInDegrees() {
    if (this.bedLocation != null) {
      EnumFacing enumfacing = (EnumFacing)this.world.getBlockState(this.bedLocation).getValue((IProperty)BlockHorizontal.FACING);
      switch (enumfacing) {
        case SOUTH:
          return 90.0F;
        case WEST:
          return 0.0F;
        case NORTH:
          return 270.0F;
        case EAST:
          return 180.0F;
      } 
    } 
    return 0.0F;
  }
  
  public boolean isPlayerSleeping() {
    return this.sleeping;
  }
  
  public boolean isPlayerFullyAsleep() {
    return (this.sleeping && this.sleepTimer >= 100);
  }
  
  public int getSleepTimer() {
    return this.sleepTimer;
  }
  
  public void addChatComponentMessage(ITextComponent chatComponent, boolean p_146105_2_) {}
  
  public BlockPos getBedLocation() {
    return this.spawnChunk;
  }
  
  public boolean isSpawnForced() {
    return this.spawnForced;
  }
  
  public void setSpawnPoint(BlockPos pos, boolean forced) {
    if (pos != null) {
      this.spawnChunk = pos;
      this.spawnForced = forced;
    } else {
      this.spawnChunk = null;
      this.spawnForced = false;
    } 
  }
  
  public void addStat(StatBase stat) {
    addStat(stat, 1);
  }
  
  public void addStat(StatBase stat, int amount) {}
  
  public void takeStat(StatBase stat) {}
  
  public void func_192021_a(List<IRecipe> p_192021_1_) {}
  
  public void func_193102_a(ResourceLocation[] p_193102_1_) {}
  
  public void func_192022_b(List<IRecipe> p_192022_1_) {}
  
  public void jump() {
    super.jump();
    addStat(StatList.JUMP);
    if (isSprinting()) {
      addExhaustion(0.2F);
    } else {
      addExhaustion(0.05F);
    } 
  }
  
  public void func_191986_a(float p_191986_1_, float p_191986_2_, float p_191986_3_) {
    double d0 = this.posX;
    double d1 = this.posY;
    double d2 = this.posZ;
    if (this.capabilities.isFlying && !isRiding()) {
      double d3 = this.motionY;
      float f = this.jumpMovementFactor;
      this.jumpMovementFactor = this.capabilities.getFlySpeed() * (isSprinting() ? 2 : true);
      super.func_191986_a(p_191986_1_, p_191986_2_, p_191986_3_);
      this.motionY = d3 * 0.6D;
      this.jumpMovementFactor = f;
      this.fallDistance = 0.0F;
      setFlag(7, false);
    } else {
      super.func_191986_a(p_191986_1_, p_191986_2_, p_191986_3_);
    } 
    addMovementStat(this.posX - d0, this.posY - d1, this.posZ - d2);
  }
  
  public float getAIMoveSpeed() {
    return (float)getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue();
  }
  
  public void addMovementStat(double p_71000_1_, double p_71000_3_, double p_71000_5_) {
    if (!isRiding())
      if (isInsideOfMaterial(Material.WATER)) {
        int i = Math.round(MathHelper.sqrt(p_71000_1_ * p_71000_1_ + p_71000_3_ * p_71000_3_ + p_71000_5_ * p_71000_5_) * 100.0F);
        if (i > 0) {
          addStat(StatList.DIVE_ONE_CM, i);
          addExhaustion(0.01F * i * 0.01F);
        } 
      } else if (isInWater()) {
        int j = Math.round(MathHelper.sqrt(p_71000_1_ * p_71000_1_ + p_71000_5_ * p_71000_5_) * 100.0F);
        if (j > 0) {
          addStat(StatList.SWIM_ONE_CM, j);
          addExhaustion(0.01F * j * 0.01F);
        } 
      } else if (isOnLadder()) {
        if (p_71000_3_ > 0.0D)
          addStat(StatList.CLIMB_ONE_CM, (int)Math.round(p_71000_3_ * 100.0D)); 
      } else if (this.onGround) {
        int k = Math.round(MathHelper.sqrt(p_71000_1_ * p_71000_1_ + p_71000_5_ * p_71000_5_) * 100.0F);
        if (k > 0)
          if (isSprinting()) {
            addStat(StatList.SPRINT_ONE_CM, k);
            addExhaustion(0.1F * k * 0.01F);
          } else if (isSneaking()) {
            addStat(StatList.CROUCH_ONE_CM, k);
            addExhaustion(0.0F * k * 0.01F);
          } else {
            addStat(StatList.WALK_ONE_CM, k);
            addExhaustion(0.0F * k * 0.01F);
          }  
      } else if (isElytraFlying()) {
        int l = Math.round(MathHelper.sqrt(p_71000_1_ * p_71000_1_ + p_71000_3_ * p_71000_3_ + p_71000_5_ * p_71000_5_) * 100.0F);
        addStat(StatList.AVIATE_ONE_CM, l);
      } else {
        int i1 = Math.round(MathHelper.sqrt(p_71000_1_ * p_71000_1_ + p_71000_5_ * p_71000_5_) * 100.0F);
        if (i1 > 25)
          addStat(StatList.FLY_ONE_CM, i1); 
      }  
  }
  
  private void addMountedMovementStat(double p_71015_1_, double p_71015_3_, double p_71015_5_) {
    if (isRiding()) {
      int i = Math.round(MathHelper.sqrt(p_71015_1_ * p_71015_1_ + p_71015_3_ * p_71015_3_ + p_71015_5_ * p_71015_5_) * 100.0F);
      if (i > 0)
        if (getRidingEntity() instanceof net.minecraft.entity.item.EntityMinecart) {
          addStat(StatList.MINECART_ONE_CM, i);
        } else if (getRidingEntity() instanceof net.minecraft.entity.item.EntityBoat) {
          addStat(StatList.BOAT_ONE_CM, i);
        } else if (getRidingEntity() instanceof EntityPig) {
          addStat(StatList.PIG_ONE_CM, i);
        } else if (getRidingEntity() instanceof AbstractHorse) {
          addStat(StatList.HORSE_ONE_CM, i);
        }  
    } 
  }
  
  public void fall(float distance, float damageMultiplier) {
    if (!this.capabilities.allowFlying) {
      if (distance >= 2.0F)
        addStat(StatList.FALL_ONE_CM, (int)Math.round(distance * 100.0D)); 
      super.fall(distance, damageMultiplier);
    } 
  }
  
  protected void resetHeight() {
    if (!isSpectator())
      super.resetHeight(); 
  }
  
  protected SoundEvent getFallSound(int heightIn) {
    return (heightIn > 4) ? SoundEvents.ENTITY_PLAYER_BIG_FALL : SoundEvents.ENTITY_PLAYER_SMALL_FALL;
  }
  
  public void onKillEntity(EntityLivingBase entityLivingIn) {
    EntityList.EntityEggInfo entitylist$entityegginfo = (EntityList.EntityEggInfo)EntityList.ENTITY_EGGS.get(EntityList.func_191301_a((Entity)entityLivingIn));
    if (entitylist$entityegginfo != null)
      addStat(entitylist$entityegginfo.killEntityStat); 
  }
  
  public void setInWeb() {
    if (!this.capabilities.isFlying)
      super.setInWeb(); 
  }
  
  public void addExperience(int amount) {
    addScore(amount);
    int i = Integer.MAX_VALUE - this.experienceTotal;
    if (amount > i)
      amount = i; 
    this.experience += amount / xpBarCap();
    for (this.experienceTotal += amount; this.experience >= 1.0F; this.experience /= xpBarCap()) {
      this.experience = (this.experience - 1.0F) * xpBarCap();
      addExperienceLevel(1);
    } 
  }
  
  public int getXPSeed() {
    return this.xpSeed;
  }
  
  public void func_192024_a(ItemStack p_192024_1_, int p_192024_2_) {
    this.experienceLevel -= p_192024_2_;
    if (this.experienceLevel < 0) {
      this.experienceLevel = 0;
      this.experience = 0.0F;
      this.experienceTotal = 0;
    } 
    this.xpSeed = this.rand.nextInt();
  }
  
  public void addExperienceLevel(int levels) {
    this.experienceLevel += levels;
    if (this.experienceLevel < 0) {
      this.experienceLevel = 0;
      this.experience = 0.0F;
      this.experienceTotal = 0;
    } 
    if (levels > 0 && this.experienceLevel % 5 == 0 && this.lastXPSound < this.ticksExisted - 100.0F) {
      float f = (this.experienceLevel > 30) ? 1.0F : (this.experienceLevel / 30.0F);
      this.world.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_PLAYER_LEVELUP, getSoundCategory(), f * 0.75F, 1.0F);
      this.lastXPSound = this.ticksExisted;
    } 
  }
  
  public int xpBarCap() {
    if (this.experienceLevel >= 30)
      return 112 + (this.experienceLevel - 30) * 9; 
    return (this.experienceLevel >= 15) ? (37 + (this.experienceLevel - 15) * 5) : (7 + this.experienceLevel * 2);
  }
  
  public void addExhaustion(float exhaustion) {
    if (!this.capabilities.disableDamage)
      if (!this.world.isRemote)
        this.foodStats.addExhaustion(exhaustion);  
  }
  
  public FoodStats getFoodStats() {
    return this.foodStats;
  }
  
  public boolean canEat(boolean ignoreHunger) {
    return ((ignoreHunger || this.foodStats.needFood()) && !this.capabilities.disableDamage);
  }
  
  public boolean shouldHeal() {
    return (getHealth() > 0.0F && getHealth() < getMaxHealth());
  }
  
  public boolean isAllowEdit() {
    return this.capabilities.allowEdit;
  }
  
  public boolean canPlayerEdit(BlockPos pos, EnumFacing facing, ItemStack stack) {
    if (this.capabilities.allowEdit)
      return true; 
    if (stack.func_190926_b())
      return false; 
    BlockPos blockpos = pos.offset(facing.getOpposite());
    Block block = this.world.getBlockState(blockpos).getBlock();
    return !(!stack.canPlaceOn(block) && !stack.canEditBlocks());
  }
  
  protected int getExperiencePoints(EntityPlayer player) {
    if (!this.world.getGameRules().getBoolean("keepInventory") && !isSpectator()) {
      int i = this.experienceLevel * 7;
      return (i > 100) ? 100 : i;
    } 
    return 0;
  }
  
  protected boolean isPlayer() {
    return true;
  }
  
  public boolean getAlwaysRenderNameTagForRender() {
    return true;
  }
  
  protected boolean canTriggerWalking() {
    return !this.capabilities.isFlying;
  }
  
  public void sendPlayerAbilities() {}
  
  public void setGameType(GameType gameType) {}
  
  public String getName() {
    return this.gameProfile.getName();
  }
  
  public InventoryEnderChest getInventoryEnderChest() {
    return this.theInventoryEnderChest;
  }
  
  public ItemStack getItemStackFromSlot(EntityEquipmentSlot slotIn) {
    if (slotIn == EntityEquipmentSlot.MAINHAND)
      return this.inventory.getCurrentItem(); 
    if (slotIn == EntityEquipmentSlot.OFFHAND)
      return (ItemStack)this.inventory.offHandInventory.get(0); 
    return (slotIn.getSlotType() == EntityEquipmentSlot.Type.ARMOR) ? (ItemStack)this.inventory.armorInventory.get(slotIn.getIndex()) : ItemStack.field_190927_a;
  }
  
  public void setItemStackToSlot(EntityEquipmentSlot slotIn, ItemStack stack) {
    if (slotIn == EntityEquipmentSlot.MAINHAND) {
      playEquipSound(stack);
      this.inventory.mainInventory.set(this.inventory.currentItem, stack);
    } else if (slotIn == EntityEquipmentSlot.OFFHAND) {
      playEquipSound(stack);
      this.inventory.offHandInventory.set(0, stack);
    } else if (slotIn.getSlotType() == EntityEquipmentSlot.Type.ARMOR) {
      playEquipSound(stack);
      this.inventory.armorInventory.set(slotIn.getIndex(), stack);
    } 
  }
  
  public boolean func_191521_c(ItemStack p_191521_1_) {
    playEquipSound(p_191521_1_);
    return this.inventory.addItemStackToInventory(p_191521_1_);
  }
  
  public Iterable<ItemStack> getHeldEquipment() {
    return Lists.newArrayList((Object[])new ItemStack[] { getHeldItemMainhand(), getHeldItemOffhand() });
  }
  
  public Iterable<ItemStack> getArmorInventoryList() {
    return (Iterable<ItemStack>)this.inventory.armorInventory;
  }
  
  public boolean func_192027_g(NBTTagCompound p_192027_1_) {
    if (!isRiding() && this.onGround && !isInWater()) {
      if (func_192023_dk().hasNoTags()) {
        func_192029_h(p_192027_1_);
        return true;
      } 
      if (func_192025_dl().hasNoTags()) {
        func_192031_i(p_192027_1_);
        return true;
      } 
      return false;
    } 
    return false;
  }
  
  protected void func_192030_dh() {
    func_192026_k(func_192023_dk());
    func_192029_h(new NBTTagCompound());
    func_192026_k(func_192025_dl());
    func_192031_i(new NBTTagCompound());
  }
  
  private void func_192026_k(@Nullable NBTTagCompound p_192026_1_) {
    if (!this.world.isRemote && !p_192026_1_.hasNoTags()) {
      Entity entity = EntityList.createEntityFromNBT(p_192026_1_, this.world);
      if (entity instanceof EntityTameable)
        ((EntityTameable)entity).setOwnerId(this.entityUniqueID); 
      entity.setPosition(this.posX, this.posY + 0.699999988079071D, this.posZ);
      this.world.spawnEntityInWorld(entity);
    } 
  }
  
  public boolean isInvisibleToPlayer(EntityPlayer player) {
    if (!isInvisible())
      return false; 
    if (player.isSpectator())
      return false; 
    Team team = getTeam();
    return !(team != null && player != null && player.getTeam() == team && team.getSeeFriendlyInvisiblesEnabled());
  }
  
  public boolean isPushedByWater() {
    return !this.capabilities.isFlying;
  }
  
  public Scoreboard getWorldScoreboard() {
    return this.world.getScoreboard();
  }
  
  public Team getTeam() {
    return (Team)getWorldScoreboard().getPlayersTeam(getName());
  }
  
  public ITextComponent getDisplayName() {
    TextComponentString textComponentString = new TextComponentString(ScorePlayerTeam.formatPlayerName(getTeam(), getName()));
    textComponentString.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/msg " + getName() + " "));
    textComponentString.getStyle().setHoverEvent(getHoverEvent());
    textComponentString.getStyle().setInsertion(getName());
    return (ITextComponent)textComponentString;
  }
  
  public float getEyeHeight() {
    float f = 1.62F;
    if (isPlayerSleeping()) {
      f = 0.2F;
    } else if (!isSneaking() && this.height != 1.65F) {
      if (isElytraFlying() || this.height == 0.6F)
        f = 0.4F; 
    } else {
      f -= 0.08F;
    } 
    return f;
  }
  
  public void setAbsorptionAmount(float amount) {
    if (amount < 0.0F)
      amount = 0.0F; 
    getDataManager().set(ABSORPTION, Float.valueOf(amount));
  }
  
  public float getAbsorptionAmount() {
    return ((Float)getDataManager().get(ABSORPTION)).floatValue();
  }
  
  public static UUID getUUID(GameProfile profile) {
    UUID uuid = profile.getId();
    if (uuid == null)
      uuid = getOfflineUUID(profile.getName()); 
    return uuid;
  }
  
  public static UUID getOfflineUUID(String username) {
    return UUID.nameUUIDFromBytes(("OfflinePlayer:" + username).getBytes(StandardCharsets.UTF_8));
  }
  
  public boolean canOpen(LockCode code) {
    if (code.isEmpty())
      return true; 
    ItemStack itemstack = getHeldItemMainhand();
    return (!itemstack.func_190926_b() && itemstack.hasDisplayName()) ? itemstack.getDisplayName().equals(code.getLock()) : false;
  }
  
  public boolean isWearing(EnumPlayerModelParts part) {
    return ((((Byte)getDataManager().get(PLAYER_MODEL_FLAG)).byteValue() & part.getPartMask()) == part.getPartMask());
  }
  
  public boolean sendCommandFeedback() {
    return (getServer()).worldServers[0].getGameRules().getBoolean("sendCommandFeedback");
  }
  
  public boolean replaceItemInInventory(int inventorySlot, ItemStack itemStackIn) {
    EntityEquipmentSlot entityequipmentslot;
    if (inventorySlot >= 0 && inventorySlot < this.inventory.mainInventory.size()) {
      this.inventory.setInventorySlotContents(inventorySlot, itemStackIn);
      return true;
    } 
    if (inventorySlot == 100 + EntityEquipmentSlot.HEAD.getIndex()) {
      entityequipmentslot = EntityEquipmentSlot.HEAD;
    } else if (inventorySlot == 100 + EntityEquipmentSlot.CHEST.getIndex()) {
      entityequipmentslot = EntityEquipmentSlot.CHEST;
    } else if (inventorySlot == 100 + EntityEquipmentSlot.LEGS.getIndex()) {
      entityequipmentslot = EntityEquipmentSlot.LEGS;
    } else if (inventorySlot == 100 + EntityEquipmentSlot.FEET.getIndex()) {
      entityequipmentslot = EntityEquipmentSlot.FEET;
    } else {
      entityequipmentslot = null;
    } 
    if (inventorySlot == 98) {
      setItemStackToSlot(EntityEquipmentSlot.MAINHAND, itemStackIn);
      return true;
    } 
    if (inventorySlot == 99) {
      setItemStackToSlot(EntityEquipmentSlot.OFFHAND, itemStackIn);
      return true;
    } 
    if (entityequipmentslot == null) {
      int i = inventorySlot - 200;
      if (i >= 0 && i < this.theInventoryEnderChest.getSizeInventory()) {
        this.theInventoryEnderChest.setInventorySlotContents(i, itemStackIn);
        return true;
      } 
      return false;
    } 
    if (!itemStackIn.func_190926_b())
      if (!(itemStackIn.getItem() instanceof net.minecraft.item.ItemArmor) && !(itemStackIn.getItem() instanceof net.minecraft.item.ItemElytra)) {
        if (entityequipmentslot != EntityEquipmentSlot.HEAD)
          return false; 
      } else if (EntityLiving.getSlotForItemStack(itemStackIn) != entityequipmentslot) {
        return false;
      }  
    this.inventory.setInventorySlotContents(entityequipmentslot.getIndex() + this.inventory.mainInventory.size(), itemStackIn);
    return true;
  }
  
  public boolean hasReducedDebug() {
    return this.hasReducedDebug;
  }
  
  public void setReducedDebug(boolean reducedDebug) {
    this.hasReducedDebug = reducedDebug;
  }
  
  public EnumHandSide getPrimaryHand() {
    return (((Byte)this.dataManager.get(MAIN_HAND)).byteValue() == 0) ? EnumHandSide.LEFT : EnumHandSide.RIGHT;
  }
  
  public void setPrimaryHand(EnumHandSide hand) {
    this.dataManager.set(MAIN_HAND, Byte.valueOf((byte)((hand == EnumHandSide.LEFT) ? 0 : 1)));
  }
  
  public NBTTagCompound func_192023_dk() {
    return (NBTTagCompound)this.dataManager.get(field_192032_bt);
  }
  
  protected void func_192029_h(NBTTagCompound p_192029_1_) {
    this.dataManager.set(field_192032_bt, p_192029_1_);
  }
  
  public NBTTagCompound func_192025_dl() {
    return (NBTTagCompound)this.dataManager.get(field_192033_bu);
  }
  
  protected void func_192031_i(NBTTagCompound p_192031_1_) {
    this.dataManager.set(field_192033_bu, p_192031_1_);
  }
  
  public float getCooldownPeriod() {
    return (float)(1.0D / getEntityAttribute(SharedMonsterAttributes.ATTACK_SPEED).getAttributeValue() * 20.0D);
  }
  
  public float getCooledAttackStrength(float adjustTicks) {
    return MathHelper.clamp((this.ticksSinceLastSwing + adjustTicks) / getCooldownPeriod(), 0.0F, 1.0F);
  }
  
  public void resetCooldown() {
    this.ticksSinceLastSwing = 0;
  }
  
  public CooldownTracker getCooldownTracker() {
    return this.cooldownTracker;
  }
  
  public void applyEntityCollision(Entity entityIn) {
    if (!isPlayerSleeping())
      super.applyEntityCollision(entityIn); 
  }
  
  public float getLuck() {
    return (float)getEntityAttribute(SharedMonsterAttributes.LUCK).getAttributeValue();
  }
  
  public boolean canUseCommandBlock() {
    return (this.capabilities.isCreativeMode && canCommandSenderUseCommand(2, ""));
  }
  
  public abstract boolean isSpectator();
  
  public abstract boolean isCreative();
  
  public enum EnumChatVisibility {
    FULL(0, "options.chat.visibility.full"),
    SYSTEM(1, "options.chat.visibility.system"),
    HIDDEN(2, "options.chat.visibility.hidden");
    
    private static final EnumChatVisibility[] ID_LOOKUP = new EnumChatVisibility[(values()).length];
    
    private final int chatVisibility;
    
    private final String resourceKey;
    
    static {
      byte b;
      int i;
      EnumChatVisibility[] arrayOfEnumChatVisibility;
      for (i = (arrayOfEnumChatVisibility = values()).length, b = 0; b < i; ) {
        EnumChatVisibility entityplayer$enumchatvisibility = arrayOfEnumChatVisibility[b];
        ID_LOOKUP[entityplayer$enumchatvisibility.chatVisibility] = entityplayer$enumchatvisibility;
        b++;
      } 
    }
    
    EnumChatVisibility(int id, String resourceKey) {
      this.chatVisibility = id;
      this.resourceKey = resourceKey;
    }
    
    public int getChatVisibility() {
      return this.chatVisibility;
    }
    
    public static EnumChatVisibility getEnumChatVisibility(int id) {
      return ID_LOOKUP[id % ID_LOOKUP.length];
    }
    
    public String getResourceKey() {
      return this.resourceKey;
    }
  }
  
  static class SleepEnemyPredicate implements Predicate<EntityMob> {
    private final EntityPlayer field_192387_a;
    
    private SleepEnemyPredicate(EntityPlayer p_i47461_1_) {
      this.field_192387_a = p_i47461_1_;
    }
    
    public boolean apply(@Nullable EntityMob p_apply_1_) {
      return p_apply_1_.func_191990_c(this.field_192387_a);
    }
  }
  
  public enum SleepResult {
    OK, NOT_POSSIBLE_HERE, NOT_POSSIBLE_NOW, TOO_FAR_AWAY, OTHER_PROBLEM, NOT_SAFE;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\entity\player\EntityPlayer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
package net.minecraft.entity;

import com.google.common.base.Objects;
import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.BlockTrapDoor;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentFrostWalker;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.ai.attributes.AttributeMap;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemElytra;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.Packet;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SPacketAnimation;
import net.minecraft.network.play.server.SPacketCollectItem;
import net.minecraft.network.play.server.SPacketEntityEquipment;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import net.minecraft.stats.StatList;
import net.minecraft.util.CombatRules;
import net.minecraft.util.CombatTracker;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class EntityLivingBase extends Entity {
  private static final Logger field_190632_a = LogManager.getLogger();
  
  private static final UUID SPRINTING_SPEED_BOOST_ID = UUID.fromString("662A6B8D-DA3E-4C1C-8813-96EA6097278D");
  
  private static final AttributeModifier SPRINTING_SPEED_BOOST = (new AttributeModifier(SPRINTING_SPEED_BOOST_ID, "Sprinting speed boost", 0.30000001192092896D, 2)).setSaved(false);
  
  protected static final DataParameter<Byte> HAND_STATES = EntityDataManager.createKey(EntityLivingBase.class, DataSerializers.BYTE);
  
  private static final DataParameter<Float> HEALTH = EntityDataManager.createKey(EntityLivingBase.class, DataSerializers.FLOAT);
  
  private static final DataParameter<Integer> POTION_EFFECTS = EntityDataManager.createKey(EntityLivingBase.class, DataSerializers.VARINT);
  
  private static final DataParameter<Boolean> HIDE_PARTICLES = EntityDataManager.createKey(EntityLivingBase.class, DataSerializers.BOOLEAN);
  
  private static final DataParameter<Integer> ARROW_COUNT_IN_ENTITY = EntityDataManager.createKey(EntityLivingBase.class, DataSerializers.VARINT);
  
  private AbstractAttributeMap attributeMap;
  
  private final CombatTracker _combatTracker = new CombatTracker(this);
  
  private final Map<Potion, PotionEffect> activePotionsMap = Maps.newHashMap();
  
  private final NonNullList<ItemStack> handInventory = NonNullList.func_191197_a(2, ItemStack.field_190927_a);
  
  private final NonNullList<ItemStack> armorArray = NonNullList.func_191197_a(4, ItemStack.field_190927_a);
  
  public boolean isSwingInProgress;
  
  public EnumHand swingingHand;
  
  public int swingProgressInt;
  
  public int arrowHitTimer;
  
  public int hurtTime;
  
  public int maxHurtTime;
  
  public float attackedAtYaw;
  
  public int deathTime;
  
  public float prevSwingProgress;
  
  public float swingProgress;
  
  protected int ticksSinceLastSwing;
  
  public float prevLimbSwingAmount;
  
  public float limbSwingAmount;
  
  public float limbSwing;
  
  public int maxHurtResistantTime = 20;
  
  public float prevCameraPitch;
  
  public float cameraPitch;
  
  public float randomUnused2;
  
  public float randomUnused1;
  
  public float renderYawOffset;
  
  public float prevRenderYawOffset;
  
  public float rotationYawHead;
  
  public float prevRotationYawHead;
  
  public float jumpMovementFactor = 0.02F;
  
  protected EntityPlayer attackingPlayer;
  
  protected int recentlyHit;
  
  protected boolean dead;
  
  protected int entityAge;
  
  protected float prevOnGroundSpeedFactor;
  
  protected float onGroundSpeedFactor;
  
  protected float movedDistance;
  
  protected float prevMovedDistance;
  
  protected float unused180;
  
  protected int scoreValue;
  
  protected float lastDamage;
  
  protected boolean isJumping;
  
  public float moveStrafing;
  
  public float moveForward;
  
  public float field_191988_bg;
  
  public float randomYawVelocity;
  
  protected int newPosRotationIncrements;
  
  protected double interpTargetX;
  
  protected double interpTargetY;
  
  protected double interpTargetZ;
  
  protected double interpTargetYaw;
  
  protected double interpTargetPitch;
  
  private boolean potionsNeedUpdate = true;
  
  private EntityLivingBase entityLivingToAttack;
  
  private int revengeTimer;
  
  private EntityLivingBase lastAttacker;
  
  private int lastAttackerTime;
  
  private float landMovementFactor;
  
  private int jumpTicks;
  
  private float absorptionAmount;
  
  protected ItemStack activeItemStack = ItemStack.field_190927_a;
  
  protected int activeItemStackUseCount;
  
  protected int ticksElytraFlying;
  
  private BlockPos prevBlockpos;
  
  private DamageSource lastDamageSource;
  
  private long lastDamageStamp;
  
  public void onKillCommand() {
    attackEntityFrom(DamageSource.outOfWorld, Float.MAX_VALUE);
  }
  
  public EntityLivingBase(World worldIn) {
    super(worldIn);
    applyEntityAttributes();
    setHealth(getMaxHealth());
    this.preventEntitySpawning = true;
    this.randomUnused1 = (float)((Math.random() + 1.0D) * 0.009999999776482582D);
    setPosition(this.posX, this.posY, this.posZ);
    this.randomUnused2 = (float)Math.random() * 12398.0F;
    this.rotationYaw = (float)(Math.random() * 6.283185307179586D);
    this.rotationYawHead = this.rotationYaw;
    this.stepHeight = 0.6F;
  }
  
  protected void entityInit() {
    this.dataManager.register(HAND_STATES, Byte.valueOf((byte)0));
    this.dataManager.register(POTION_EFFECTS, Integer.valueOf(0));
    this.dataManager.register(HIDE_PARTICLES, Boolean.valueOf(false));
    this.dataManager.register(ARROW_COUNT_IN_ENTITY, Integer.valueOf(0));
    this.dataManager.register(HEALTH, Float.valueOf(1.0F));
  }
  
  protected void applyEntityAttributes() {
    getAttributeMap().registerAttribute(SharedMonsterAttributes.MAX_HEALTH);
    getAttributeMap().registerAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE);
    getAttributeMap().registerAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
    getAttributeMap().registerAttribute(SharedMonsterAttributes.ARMOR);
    getAttributeMap().registerAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS);
  }
  
  protected void updateFallState(double y, boolean onGroundIn, IBlockState state, BlockPos pos) {
    if (!isInWater())
      handleWaterMovement(); 
    if (!this.world.isRemote && this.fallDistance > 3.0F && onGroundIn) {
      float f = MathHelper.ceil(this.fallDistance - 3.0F);
      if (state.getMaterial() != Material.AIR) {
        double d0 = Math.min((0.2F + f / 15.0F), 2.5D);
        int i = (int)(150.0D * d0);
        ((WorldServer)this.world).spawnParticle(EnumParticleTypes.BLOCK_DUST, this.posX, this.posY, this.posZ, i, 0.0D, 0.0D, 0.0D, 0.15000000596046448D, new int[] { Block.getStateId(state) });
      } 
    } 
    super.updateFallState(y, onGroundIn, state, pos);
  }
  
  public boolean canBreatheUnderwater() {
    return false;
  }
  
  public void onEntityUpdate() {
    this.prevSwingProgress = this.swingProgress;
    super.onEntityUpdate();
    this.world.theProfiler.startSection("livingEntityBaseTick");
    boolean flag = this instanceof EntityPlayer;
    if (isEntityAlive())
      if (isEntityInsideOpaqueBlock()) {
        attackEntityFrom(DamageSource.inWall, 1.0F);
      } else if (flag && !this.world.getWorldBorder().contains(getEntityBoundingBox())) {
        double d0 = this.world.getWorldBorder().getClosestDistance(this) + this.world.getWorldBorder().getDamageBuffer();
        if (d0 < 0.0D) {
          double d1 = this.world.getWorldBorder().getDamageAmount();
          if (d1 > 0.0D)
            attackEntityFrom(DamageSource.inWall, Math.max(1, MathHelper.floor(-d0 * d1))); 
        } 
      }  
    if (isImmuneToFire() || this.world.isRemote)
      extinguish(); 
    boolean flag1 = (flag && ((EntityPlayer)this).capabilities.disableDamage);
    if (isEntityAlive()) {
      if (!isInsideOfMaterial(Material.WATER)) {
        setAir(300);
      } else {
        if (!canBreatheUnderwater() && !isPotionActive(MobEffects.WATER_BREATHING) && !flag1) {
          setAir(decreaseAirSupply(getAir()));
          if (getAir() == -20) {
            setAir(0);
            for (int i = 0; i < 8; i++) {
              float f2 = this.rand.nextFloat() - this.rand.nextFloat();
              float f = this.rand.nextFloat() - this.rand.nextFloat();
              float f1 = this.rand.nextFloat() - this.rand.nextFloat();
              this.world.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX + f2, this.posY + f, this.posZ + f1, this.motionX, this.motionY, this.motionZ, new int[0]);
            } 
            attackEntityFrom(DamageSource.drown, 2.0F);
          } 
        } 
        if (!this.world.isRemote && isRiding() && getRidingEntity() instanceof EntityLivingBase)
          dismountRidingEntity(); 
      } 
      if (!this.world.isRemote) {
        BlockPos blockpos = new BlockPos(this);
        if (!Objects.equal(this.prevBlockpos, blockpos)) {
          this.prevBlockpos = blockpos;
          frostWalk(blockpos);
        } 
      } 
    } 
    if (isEntityAlive() && isWet())
      extinguish(); 
    this.prevCameraPitch = this.cameraPitch;
    if (this.hurtTime > 0)
      this.hurtTime--; 
    if (this.hurtResistantTime > 0 && !(this instanceof EntityPlayerMP))
      this.hurtResistantTime--; 
    if (getHealth() <= 0.0F)
      onDeathUpdate(); 
    if (this.recentlyHit > 0) {
      this.recentlyHit--;
    } else {
      this.attackingPlayer = null;
    } 
    if (this.lastAttacker != null && !this.lastAttacker.isEntityAlive())
      this.lastAttacker = null; 
    if (this.entityLivingToAttack != null)
      if (!this.entityLivingToAttack.isEntityAlive()) {
        setRevengeTarget((EntityLivingBase)null);
      } else if (this.ticksExisted - this.revengeTimer > 100) {
        setRevengeTarget((EntityLivingBase)null);
      }  
    updatePotionEffects();
    this.prevMovedDistance = this.movedDistance;
    this.prevRenderYawOffset = this.renderYawOffset;
    this.prevRotationYawHead = this.rotationYawHead;
    this.prevRotationYaw = this.rotationYaw;
    this.prevRotationPitch = this.rotationPitch;
    this.world.theProfiler.endSection();
  }
  
  protected void frostWalk(BlockPos pos) {
    int i = EnchantmentHelper.getMaxEnchantmentLevel(Enchantments.FROST_WALKER, this);
    if (i > 0)
      EnchantmentFrostWalker.freezeNearby(this, this.world, pos, i); 
  }
  
  public boolean isChild() {
    return false;
  }
  
  protected void onDeathUpdate() {
    this.deathTime++;
    if (this.deathTime == 20) {
      if (!this.world.isRemote && (isPlayer() || (this.recentlyHit > 0 && canDropLoot() && this.world.getGameRules().getBoolean("doMobLoot")))) {
        int i = getExperiencePoints(this.attackingPlayer);
        while (i > 0) {
          int j = EntityXPOrb.getXPSplit(i);
          i -= j;
          this.world.spawnEntityInWorld((Entity)new EntityXPOrb(this.world, this.posX, this.posY, this.posZ, j));
        } 
      } 
      setDead();
      for (int k = 0; k < 20; k++) {
        double d2 = this.rand.nextGaussian() * 0.02D;
        double d0 = this.rand.nextGaussian() * 0.02D;
        double d1 = this.rand.nextGaussian() * 0.02D;
        this.world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, this.posX + (this.rand.nextFloat() * this.width * 2.0F) - this.width, this.posY + (this.rand.nextFloat() * this.height), this.posZ + (this.rand.nextFloat() * this.width * 2.0F) - this.width, d2, d0, d1, new int[0]);
      } 
    } 
  }
  
  protected boolean canDropLoot() {
    return !isChild();
  }
  
  protected int decreaseAirSupply(int air) {
    int i = EnchantmentHelper.getRespirationModifier(this);
    return (i > 0 && this.rand.nextInt(i + 1) > 0) ? air : (air - 1);
  }
  
  protected int getExperiencePoints(EntityPlayer player) {
    return 0;
  }
  
  protected boolean isPlayer() {
    return false;
  }
  
  public Random getRNG() {
    return this.rand;
  }
  
  @Nullable
  public EntityLivingBase getAITarget() {
    return this.entityLivingToAttack;
  }
  
  public int getRevengeTimer() {
    return this.revengeTimer;
  }
  
  public void setRevengeTarget(@Nullable EntityLivingBase livingBase) {
    this.entityLivingToAttack = livingBase;
    this.revengeTimer = this.ticksExisted;
  }
  
  public EntityLivingBase getLastAttacker() {
    return this.lastAttacker;
  }
  
  public int getLastAttackerTime() {
    return this.lastAttackerTime;
  }
  
  public void setLastAttacker(Entity entityIn) {
    if (entityIn instanceof EntityLivingBase) {
      this.lastAttacker = (EntityLivingBase)entityIn;
    } else {
      this.lastAttacker = null;
    } 
    this.lastAttackerTime = this.ticksExisted;
  }
  
  public int getAge() {
    return this.entityAge;
  }
  
  protected void playEquipSound(ItemStack stack) {
    if (!stack.func_190926_b()) {
      SoundEvent soundevent = SoundEvents.ITEM_ARMOR_EQUIP_GENERIC;
      Item item = stack.getItem();
      if (item instanceof ItemArmor) {
        soundevent = ((ItemArmor)item).getArmorMaterial().getSoundEvent();
      } else if (item == Items.ELYTRA) {
        soundevent = SoundEvents.field_191258_p;
      } 
      playSound(soundevent, 1.0F, 1.0F);
    } 
  }
  
  public void writeEntityToNBT(NBTTagCompound compound) {
    compound.setFloat("Health", getHealth());
    compound.setShort("HurtTime", (short)this.hurtTime);
    compound.setInteger("HurtByTimestamp", this.revengeTimer);
    compound.setShort("DeathTime", (short)this.deathTime);
    compound.setFloat("AbsorptionAmount", getAbsorptionAmount());
    byte b;
    int i;
    EntityEquipmentSlot[] arrayOfEntityEquipmentSlot;
    for (i = (arrayOfEntityEquipmentSlot = EntityEquipmentSlot.values()).length, b = 0; b < i; ) {
      EntityEquipmentSlot entityequipmentslot = arrayOfEntityEquipmentSlot[b];
      ItemStack itemstack = getItemStackFromSlot(entityequipmentslot);
      if (!itemstack.func_190926_b())
        getAttributeMap().removeAttributeModifiers(itemstack.getAttributeModifiers(entityequipmentslot)); 
      b++;
    } 
    compound.setTag("Attributes", (NBTBase)SharedMonsterAttributes.writeBaseAttributeMapToNBT(getAttributeMap()));
    for (i = (arrayOfEntityEquipmentSlot = EntityEquipmentSlot.values()).length, b = 0; b < i; ) {
      EntityEquipmentSlot entityequipmentslot1 = arrayOfEntityEquipmentSlot[b];
      ItemStack itemstack1 = getItemStackFromSlot(entityequipmentslot1);
      if (!itemstack1.func_190926_b())
        getAttributeMap().applyAttributeModifiers(itemstack1.getAttributeModifiers(entityequipmentslot1)); 
      b++;
    } 
    if (!this.activePotionsMap.isEmpty()) {
      NBTTagList nbttaglist = new NBTTagList();
      for (PotionEffect potioneffect : this.activePotionsMap.values())
        nbttaglist.appendTag((NBTBase)potioneffect.writeCustomPotionEffectToNBT(new NBTTagCompound())); 
      compound.setTag("ActiveEffects", (NBTBase)nbttaglist);
    } 
    compound.setBoolean("FallFlying", isElytraFlying());
  }
  
  public void readEntityFromNBT(NBTTagCompound compound) {
    setAbsorptionAmount(compound.getFloat("AbsorptionAmount"));
    if (compound.hasKey("Attributes", 9) && this.world != null && !this.world.isRemote)
      SharedMonsterAttributes.setAttributeModifiers(getAttributeMap(), compound.getTagList("Attributes", 10)); 
    if (compound.hasKey("ActiveEffects", 9)) {
      NBTTagList nbttaglist = compound.getTagList("ActiveEffects", 10);
      for (int i = 0; i < nbttaglist.tagCount(); i++) {
        NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
        PotionEffect potioneffect = PotionEffect.readCustomPotionEffectFromNBT(nbttagcompound);
        if (potioneffect != null)
          this.activePotionsMap.put(potioneffect.getPotion(), potioneffect); 
      } 
    } 
    if (compound.hasKey("Health", 99))
      setHealth(compound.getFloat("Health")); 
    this.hurtTime = compound.getShort("HurtTime");
    this.deathTime = compound.getShort("DeathTime");
    this.revengeTimer = compound.getInteger("HurtByTimestamp");
    if (compound.hasKey("Team", 8)) {
      String s = compound.getString("Team");
      boolean flag = this.world.getScoreboard().addPlayerToTeam(getCachedUniqueIdString(), s);
      if (!flag)
        field_190632_a.warn("Unable to add mob to team \"" + s + "\" (that team probably doesn't exist)"); 
    } 
    if (compound.getBoolean("FallFlying"))
      setFlag(7, true); 
  }
  
  protected void updatePotionEffects() {
    Iterator<Potion> iterator = this.activePotionsMap.keySet().iterator();
    try {
      while (iterator.hasNext()) {
        Potion potion = iterator.next();
        PotionEffect potioneffect = this.activePotionsMap.get(potion);
        if (!potioneffect.onUpdate(this)) {
          if (!this.world.isRemote) {
            iterator.remove();
            onFinishedPotionEffect(potioneffect);
          } 
          continue;
        } 
        if (potioneffect.getDuration() % 600 == 0)
          onChangedPotionEffect(potioneffect, false); 
      } 
    } catch (ConcurrentModificationException concurrentModificationException) {}
    if (this.potionsNeedUpdate) {
      if (!this.world.isRemote)
        updatePotionMetadata(); 
      this.potionsNeedUpdate = false;
    } 
    int i = ((Integer)this.dataManager.get(POTION_EFFECTS)).intValue();
    boolean flag1 = ((Boolean)this.dataManager.get(HIDE_PARTICLES)).booleanValue();
    if (i > 0) {
      boolean flag;
      int j;
      if (isInvisible()) {
        flag = (this.rand.nextInt(15) == 0);
      } else {
        flag = this.rand.nextBoolean();
      } 
      if (flag1)
        j = flag & ((this.rand.nextInt(5) == 0) ? 1 : 0); 
      if (j != 0 && i > 0) {
        double d0 = (i >> 16 & 0xFF) / 255.0D;
        double d1 = (i >> 8 & 0xFF) / 255.0D;
        double d2 = (i >> 0 & 0xFF) / 255.0D;
        this.world.spawnParticle(flag1 ? EnumParticleTypes.SPELL_MOB_AMBIENT : EnumParticleTypes.SPELL_MOB, this.posX + (this.rand.nextDouble() - 0.5D) * this.width, this.posY + this.rand.nextDouble() * this.height, this.posZ + (this.rand.nextDouble() - 0.5D) * this.width, d0, d1, d2, new int[0]);
      } 
    } 
  }
  
  protected void updatePotionMetadata() {
    if (this.activePotionsMap.isEmpty()) {
      resetPotionEffectMetadata();
      setInvisible(false);
    } else {
      Collection<PotionEffect> collection = this.activePotionsMap.values();
      this.dataManager.set(HIDE_PARTICLES, Boolean.valueOf(areAllPotionsAmbient(collection)));
      this.dataManager.set(POTION_EFFECTS, Integer.valueOf(PotionUtils.getPotionColorFromEffectList(collection)));
      setInvisible(isPotionActive(MobEffects.INVISIBILITY));
    } 
  }
  
  public static boolean areAllPotionsAmbient(Collection<PotionEffect> potionEffects) {
    for (PotionEffect potioneffect : potionEffects) {
      if (!potioneffect.getIsAmbient())
        return false; 
    } 
    return true;
  }
  
  protected void resetPotionEffectMetadata() {
    this.dataManager.set(HIDE_PARTICLES, Boolean.valueOf(false));
    this.dataManager.set(POTION_EFFECTS, Integer.valueOf(0));
  }
  
  public void clearActivePotions() {
    if (!this.world.isRemote) {
      Iterator<PotionEffect> iterator = this.activePotionsMap.values().iterator();
      while (iterator.hasNext()) {
        onFinishedPotionEffect(iterator.next());
        iterator.remove();
      } 
    } 
  }
  
  public Collection<PotionEffect> getActivePotionEffects() {
    return this.activePotionsMap.values();
  }
  
  public Map<Potion, PotionEffect> func_193076_bZ() {
    return this.activePotionsMap;
  }
  
  public boolean isPotionActive(Potion potionIn) {
    return this.activePotionsMap.containsKey(potionIn);
  }
  
  @Nullable
  public PotionEffect getActivePotionEffect(Potion potionIn) {
    return this.activePotionsMap.get(potionIn);
  }
  
  public void addPotionEffect(PotionEffect potioneffectIn) {
    if (isPotionApplicable(potioneffectIn)) {
      PotionEffect potioneffect = this.activePotionsMap.get(potioneffectIn.getPotion());
      if (potioneffect == null) {
        this.activePotionsMap.put(potioneffectIn.getPotion(), potioneffectIn);
        onNewPotionEffect(potioneffectIn);
      } else {
        potioneffect.combine(potioneffectIn);
        onChangedPotionEffect(potioneffect, true);
      } 
    } 
  }
  
  public boolean isPotionApplicable(PotionEffect potioneffectIn) {
    if (getCreatureAttribute() == EnumCreatureAttribute.UNDEAD) {
      Potion potion = potioneffectIn.getPotion();
      if (potion == MobEffects.REGENERATION || potion == MobEffects.POISON)
        return false; 
    } 
    return true;
  }
  
  public boolean isEntityUndead() {
    return (getCreatureAttribute() == EnumCreatureAttribute.UNDEAD);
  }
  
  @Nullable
  public PotionEffect removeActivePotionEffect(@Nullable Potion potioneffectin) {
    return this.activePotionsMap.remove(potioneffectin);
  }
  
  public void removePotionEffect(Potion potionIn) {
    PotionEffect potioneffect = removeActivePotionEffect(potionIn);
    if (potioneffect != null)
      onFinishedPotionEffect(potioneffect); 
  }
  
  protected void onNewPotionEffect(PotionEffect id) {
    this.potionsNeedUpdate = true;
    if (!this.world.isRemote)
      id.getPotion().applyAttributesModifiersToEntity(this, getAttributeMap(), id.getAmplifier()); 
  }
  
  protected void onChangedPotionEffect(PotionEffect id, boolean p_70695_2_) {
    this.potionsNeedUpdate = true;
    if (p_70695_2_ && !this.world.isRemote) {
      Potion potion = id.getPotion();
      potion.removeAttributesModifiersFromEntity(this, getAttributeMap(), id.getAmplifier());
      potion.applyAttributesModifiersToEntity(this, getAttributeMap(), id.getAmplifier());
    } 
  }
  
  protected void onFinishedPotionEffect(PotionEffect effect) {
    this.potionsNeedUpdate = true;
    if (!this.world.isRemote)
      effect.getPotion().removeAttributesModifiersFromEntity(this, getAttributeMap(), effect.getAmplifier()); 
  }
  
  public void heal(float healAmount) {
    float f = getHealth();
    if (f > 0.0F)
      setHealth(f + healAmount); 
  }
  
  public final float getHealth() {
    return ((Float)this.dataManager.get(HEALTH)).floatValue();
  }
  
  public void setHealth(float health) {
    this.dataManager.set(HEALTH, Float.valueOf(MathHelper.clamp(health, 0.0F, getMaxHealth())));
  }
  
  public boolean attackEntityFrom(DamageSource source, float amount) {
    if (isEntityInvulnerable(source))
      return false; 
    if (this.world.isRemote)
      return false; 
    this.entityAge = 0;
    if (getHealth() <= 0.0F)
      return false; 
    if (source.isFireDamage() && isPotionActive(MobEffects.FIRE_RESISTANCE))
      return false; 
    float f = amount;
    if ((source == DamageSource.anvil || source == DamageSource.fallingBlock) && !getItemStackFromSlot(EntityEquipmentSlot.HEAD).func_190926_b()) {
      getItemStackFromSlot(EntityEquipmentSlot.HEAD).damageItem((int)(amount * 4.0F + this.rand.nextFloat() * amount * 2.0F), this);
      amount *= 0.75F;
    } 
    boolean flag = false;
    if (amount > 0.0F && canBlockDamageSource(source)) {
      damageShield(amount);
      amount = 0.0F;
      if (!source.isProjectile()) {
        Entity entity = source.getSourceOfDamage();
        if (entity instanceof EntityLivingBase)
          func_190629_c((EntityLivingBase)entity); 
      } 
      flag = true;
    } 
    this.limbSwingAmount = 1.5F;
    boolean flag1 = true;
    if (this.hurtResistantTime > this.maxHurtResistantTime / 2.0F) {
      if (amount <= this.lastDamage)
        return false; 
      damageEntity(source, amount - this.lastDamage);
      this.lastDamage = amount;
      flag1 = false;
    } else {
      this.lastDamage = amount;
      this.hurtResistantTime = this.maxHurtResistantTime;
      damageEntity(source, amount);
      this.maxHurtTime = 10;
      this.hurtTime = this.maxHurtTime;
    } 
    this.attackedAtYaw = 0.0F;
    Entity entity1 = source.getEntity();
    if (entity1 != null) {
      if (entity1 instanceof EntityLivingBase)
        setRevengeTarget((EntityLivingBase)entity1); 
      if (entity1 instanceof EntityPlayer) {
        this.recentlyHit = 100;
        this.attackingPlayer = (EntityPlayer)entity1;
      } else if (entity1 instanceof EntityWolf) {
        EntityWolf entitywolf = (EntityWolf)entity1;
        if (entitywolf.isTamed()) {
          this.recentlyHit = 100;
          this.attackingPlayer = null;
        } 
      } 
    } 
    if (flag1) {
      if (flag) {
        this.world.setEntityState(this, (byte)29);
      } else if (source instanceof EntityDamageSource && ((EntityDamageSource)source).getIsThornsDamage()) {
        this.world.setEntityState(this, (byte)33);
      } else {
        byte b0;
        if (source == DamageSource.drown) {
          b0 = 36;
        } else if (source.isFireDamage()) {
          b0 = 37;
        } else {
          b0 = 2;
        } 
        this.world.setEntityState(this, b0);
      } 
      if (source != DamageSource.drown && (!flag || amount > 0.0F))
        setBeenAttacked(); 
      if (entity1 != null) {
        double d1 = entity1.posX - this.posX;
        double d0;
        for (d0 = entity1.posZ - this.posZ; d1 * d1 + d0 * d0 < 1.0E-4D; d0 = (Math.random() - Math.random()) * 0.01D)
          d1 = (Math.random() - Math.random()) * 0.01D; 
        this.attackedAtYaw = (float)(MathHelper.atan2(d0, d1) * 57.29577951308232D - this.rotationYaw);
        knockBack(entity1, 0.4F, d1, d0);
      } else {
        this.attackedAtYaw = ((int)(Math.random() * 2.0D) * 180);
      } 
    } 
    if (getHealth() <= 0.0F) {
      if (!func_190628_d(source)) {
        SoundEvent soundevent = getDeathSound();
        if (flag1 && soundevent != null)
          playSound(soundevent, getSoundVolume(), getSoundPitch()); 
        onDeath(source);
      } 
    } else if (flag1) {
      playHurtSound(source);
    } 
    boolean flag2 = !(flag && amount <= 0.0F);
    if (flag2) {
      this.lastDamageSource = source;
      this.lastDamageStamp = this.world.getTotalWorldTime();
    } 
    if (this instanceof EntityPlayerMP)
      CriteriaTriggers.field_192128_h.func_192200_a((EntityPlayerMP)this, source, f, amount, flag); 
    if (entity1 instanceof EntityPlayerMP)
      CriteriaTriggers.field_192127_g.func_192220_a((EntityPlayerMP)entity1, this, source, f, amount, flag); 
    return flag2;
  }
  
  protected void func_190629_c(EntityLivingBase p_190629_1_) {
    p_190629_1_.knockBack(this, 0.5F, this.posX - p_190629_1_.posX, this.posZ - p_190629_1_.posZ);
  }
  
  private boolean func_190628_d(DamageSource p_190628_1_) {
    if (p_190628_1_.canHarmInCreative())
      return false; 
    ItemStack itemstack = null;
    byte b;
    int i;
    EnumHand[] arrayOfEnumHand;
    for (i = (arrayOfEnumHand = EnumHand.values()).length, b = 0; b < i; ) {
      EnumHand enumhand = arrayOfEnumHand[b];
      ItemStack itemstack1 = getHeldItem(enumhand);
      if (itemstack1.getItem() == Items.field_190929_cY) {
        itemstack = itemstack1.copy();
        itemstack1.func_190918_g(1);
        break;
      } 
      b++;
    } 
    if (itemstack != null) {
      if (this instanceof EntityPlayerMP) {
        EntityPlayerMP entityplayermp = (EntityPlayerMP)this;
        entityplayermp.addStat(StatList.getObjectUseStats(Items.field_190929_cY));
        CriteriaTriggers.field_193130_A.func_193187_a(entityplayermp, itemstack);
      } 
      setHealth(1.0F);
      clearActivePotions();
      addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 900, 1));
      addPotionEffect(new PotionEffect(MobEffects.ABSORPTION, 100, 1));
      this.world.setEntityState(this, (byte)35);
    } 
    return (itemstack != null);
  }
  
  @Nullable
  public DamageSource getLastDamageSource() {
    if (this.world.getTotalWorldTime() - this.lastDamageStamp > 40L)
      this.lastDamageSource = null; 
    return this.lastDamageSource;
  }
  
  protected void playHurtSound(DamageSource source) {
    SoundEvent soundevent = getHurtSound(source);
    if (soundevent != null)
      playSound(soundevent, getSoundVolume(), getSoundPitch()); 
  }
  
  private boolean canBlockDamageSource(DamageSource damageSourceIn) {
    if (!damageSourceIn.isUnblockable() && isActiveItemStackBlocking()) {
      Vec3d vec3d = damageSourceIn.getDamageLocation();
      if (vec3d != null) {
        Vec3d vec3d1 = getLook(1.0F);
        Vec3d vec3d2 = vec3d.subtractReverse(new Vec3d(this.posX, this.posY, this.posZ)).normalize();
        vec3d2 = new Vec3d(vec3d2.xCoord, 0.0D, vec3d2.zCoord);
        if (vec3d2.dotProduct(vec3d1) < 0.0D)
          return true; 
      } 
    } 
    return false;
  }
  
  public void renderBrokenItemStack(ItemStack stack) {
    playSound(SoundEvents.ENTITY_ITEM_BREAK, 0.8F, 0.8F + this.world.rand.nextFloat() * 0.4F);
    for (int i = 0; i < 5; i++) {
      Vec3d vec3d = new Vec3d((this.rand.nextFloat() - 0.5D) * 0.1D, Math.random() * 0.1D + 0.1D, 0.0D);
      vec3d = vec3d.rotatePitch(-this.rotationPitch * 0.017453292F);
      vec3d = vec3d.rotateYaw(-this.rotationYaw * 0.017453292F);
      double d0 = -this.rand.nextFloat() * 0.6D - 0.3D;
      Vec3d vec3d1 = new Vec3d((this.rand.nextFloat() - 0.5D) * 0.3D, d0, 0.6D);
      vec3d1 = vec3d1.rotatePitch(-this.rotationPitch * 0.017453292F);
      vec3d1 = vec3d1.rotateYaw(-this.rotationYaw * 0.017453292F);
      vec3d1 = vec3d1.addVector(this.posX, this.posY + getEyeHeight(), this.posZ);
      this.world.spawnParticle(EnumParticleTypes.ITEM_CRACK, vec3d1.xCoord, vec3d1.yCoord, vec3d1.zCoord, vec3d.xCoord, vec3d.yCoord + 0.05D, vec3d.zCoord, new int[] { Item.getIdFromItem(stack.getItem()) });
    } 
  }
  
  public void onDeath(DamageSource cause) {
    if (!this.dead) {
      Entity entity = cause.getEntity();
      EntityLivingBase entitylivingbase = getAttackingEntity();
      if (this.scoreValue >= 0 && entitylivingbase != null)
        entitylivingbase.func_191956_a(this, this.scoreValue, cause); 
      if (entity != null)
        entity.onKillEntity(this); 
      this.dead = true;
      getCombatTracker().reset();
      if (!this.world.isRemote) {
        int i = 0;
        if (entity instanceof EntityPlayer)
          i = EnchantmentHelper.getLootingModifier((EntityLivingBase)entity); 
        if (canDropLoot() && this.world.getGameRules().getBoolean("doMobLoot")) {
          boolean flag = (this.recentlyHit > 0);
          dropLoot(flag, i, cause);
        } 
      } 
      this.world.setEntityState(this, (byte)3);
    } 
  }
  
  protected void dropLoot(boolean wasRecentlyHit, int lootingModifier, DamageSource source) {
    dropFewItems(wasRecentlyHit, lootingModifier);
    dropEquipment(wasRecentlyHit, lootingModifier);
  }
  
  protected void dropEquipment(boolean wasRecentlyHit, int lootingModifier) {}
  
  public void knockBack(Entity entityIn, float strength, double xRatio, double zRatio) {
    if (this.rand.nextDouble() >= getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).getAttributeValue()) {
      this.isAirBorne = true;
      float f = MathHelper.sqrt(xRatio * xRatio + zRatio * zRatio);
      this.motionX /= 2.0D;
      this.motionZ /= 2.0D;
      this.motionX -= xRatio / f * strength;
      this.motionZ -= zRatio / f * strength;
      if (this.onGround) {
        this.motionY /= 2.0D;
        this.motionY += strength;
        if (this.motionY > 0.4000000059604645D)
          this.motionY = 0.4000000059604645D; 
      } 
    } 
  }
  
  @Nullable
  protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
    return SoundEvents.ENTITY_GENERIC_HURT;
  }
  
  @Nullable
  protected SoundEvent getDeathSound() {
    return SoundEvents.ENTITY_GENERIC_DEATH;
  }
  
  protected SoundEvent getFallSound(int heightIn) {
    return (heightIn > 4) ? SoundEvents.ENTITY_GENERIC_BIG_FALL : SoundEvents.ENTITY_GENERIC_SMALL_FALL;
  }
  
  protected void dropFewItems(boolean wasRecentlyHit, int lootingModifier) {}
  
  public boolean isOnLadder() {
    int i = MathHelper.floor(this.posX);
    int j = MathHelper.floor((getEntityBoundingBox()).minY);
    int k = MathHelper.floor(this.posZ);
    if (this instanceof EntityPlayer && ((EntityPlayer)this).isSpectator())
      return false; 
    BlockPos blockpos = new BlockPos(i, j, k);
    IBlockState iblockstate = this.world.getBlockState(blockpos);
    Block block = iblockstate.getBlock();
    if (block != Blocks.LADDER && block != Blocks.VINE)
      return (block instanceof BlockTrapDoor && canGoThroughtTrapDoorOnLadder(blockpos, iblockstate)); 
    return true;
  }
  
  private boolean canGoThroughtTrapDoorOnLadder(BlockPos pos, IBlockState state) {
    if (((Boolean)state.getValue((IProperty)BlockTrapDoor.OPEN)).booleanValue()) {
      IBlockState iblockstate = this.world.getBlockState(pos.down());
      if (iblockstate.getBlock() == Blocks.LADDER && iblockstate.getValue((IProperty)BlockLadder.FACING) == state.getValue((IProperty)BlockTrapDoor.FACING))
        return true; 
    } 
    return false;
  }
  
  public boolean isEntityAlive() {
    return (!this.isDead && getHealth() > 0.0F);
  }
  
  public void fall(float distance, float damageMultiplier) {
    super.fall(distance, damageMultiplier);
    PotionEffect potioneffect = getActivePotionEffect(MobEffects.JUMP_BOOST);
    float f = (potioneffect == null) ? 0.0F : (potioneffect.getAmplifier() + 1);
    int i = MathHelper.ceil((distance - 3.0F - f) * damageMultiplier);
    if (i > 0) {
      playSound(getFallSound(i), 1.0F, 1.0F);
      attackEntityFrom(DamageSource.fall, i);
      int j = MathHelper.floor(this.posX);
      int k = MathHelper.floor(this.posY - 0.20000000298023224D);
      int l = MathHelper.floor(this.posZ);
      IBlockState iblockstate = this.world.getBlockState(new BlockPos(j, k, l));
      if (iblockstate.getMaterial() != Material.AIR) {
        SoundType soundtype = iblockstate.getBlock().getSoundType();
        playSound(soundtype.getFallSound(), soundtype.getVolume() * 0.5F, soundtype.getPitch() * 0.75F);
      } 
    } 
  }
  
  public void performHurtAnimation() {
    this.maxHurtTime = 10;
    this.hurtTime = this.maxHurtTime;
    this.attackedAtYaw = 0.0F;
  }
  
  public int getTotalArmorValue() {
    IAttributeInstance iattributeinstance = getEntityAttribute(SharedMonsterAttributes.ARMOR);
    return MathHelper.floor(iattributeinstance.getAttributeValue());
  }
  
  protected void damageArmor(float damage) {}
  
  protected void damageShield(float damage) {}
  
  protected float applyArmorCalculations(DamageSource source, float damage) {
    if (!source.isUnblockable()) {
      damageArmor(damage);
      damage = CombatRules.getDamageAfterAbsorb(damage, getTotalArmorValue(), (float)getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
    } 
    return damage;
  }
  
  protected float applyPotionDamageCalculations(DamageSource source, float damage) {
    if (source.isDamageAbsolute())
      return damage; 
    if (isPotionActive(MobEffects.RESISTANCE) && source != DamageSource.outOfWorld) {
      int i = (getActivePotionEffect(MobEffects.RESISTANCE).getAmplifier() + 1) * 5;
      int j = 25 - i;
      float f = damage * j;
      damage = f / 25.0F;
    } 
    if (damage <= 0.0F)
      return 0.0F; 
    int k = EnchantmentHelper.getEnchantmentModifierDamage(getArmorInventoryList(), source);
    if (k > 0)
      damage = CombatRules.getDamageAfterMagicAbsorb(damage, k); 
    return damage;
  }
  
  protected void damageEntity(DamageSource damageSrc, float damageAmount) {
    if (!isEntityInvulnerable(damageSrc)) {
      damageAmount = applyArmorCalculations(damageSrc, damageAmount);
      damageAmount = applyPotionDamageCalculations(damageSrc, damageAmount);
      float f = damageAmount;
      damageAmount = Math.max(damageAmount - getAbsorptionAmount(), 0.0F);
      setAbsorptionAmount(getAbsorptionAmount() - f - damageAmount);
      if (damageAmount != 0.0F) {
        float f1 = getHealth();
        setHealth(f1 - damageAmount);
        getCombatTracker().trackDamage(damageSrc, f1, damageAmount);
        setAbsorptionAmount(getAbsorptionAmount() - damageAmount);
      } 
    } 
  }
  
  public CombatTracker getCombatTracker() {
    return this._combatTracker;
  }
  
  @Nullable
  public EntityLivingBase getAttackingEntity() {
    if (this._combatTracker.getBestAttacker() != null)
      return this._combatTracker.getBestAttacker(); 
    if (this.attackingPlayer != null)
      return (EntityLivingBase)this.attackingPlayer; 
    return (this.entityLivingToAttack != null) ? this.entityLivingToAttack : null;
  }
  
  public final float getMaxHealth() {
    return (float)getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getAttributeValue();
  }
  
  public final int getArrowCountInEntity() {
    return ((Integer)this.dataManager.get(ARROW_COUNT_IN_ENTITY)).intValue();
  }
  
  public final void setArrowCountInEntity(int count) {
    this.dataManager.set(ARROW_COUNT_IN_ENTITY, Integer.valueOf(count));
  }
  
  private int getArmSwingAnimationEnd() {
    if (isPotionActive(MobEffects.HASTE))
      return 6 - 1 + getActivePotionEffect(MobEffects.HASTE).getAmplifier(); 
    return isPotionActive(MobEffects.MINING_FATIGUE) ? (6 + (1 + getActivePotionEffect(MobEffects.MINING_FATIGUE).getAmplifier()) * 2) : 6;
  }
  
  public void swingArm(EnumHand hand) {
    if (!this.isSwingInProgress || this.swingProgressInt >= getArmSwingAnimationEnd() / 2 || this.swingProgressInt < 0) {
      this.swingProgressInt = -1;
      this.isSwingInProgress = true;
      this.swingingHand = hand;
      if (this.world instanceof WorldServer)
        ((WorldServer)this.world).getEntityTracker().sendToAllTrackingEntity(this, (Packet<?>)new SPacketAnimation(this, (hand == EnumHand.MAIN_HAND) ? 0 : 3)); 
    } 
  }
  
  public void handleStatusUpdate(byte id) {
    boolean flag = (id == 33);
    boolean flag1 = (id == 36);
    boolean flag2 = (id == 37);
    if (id != 2 && !flag && !flag1 && !flag2) {
      if (id == 3) {
        SoundEvent soundevent1 = getDeathSound();
        if (soundevent1 != null)
          playSound(soundevent1, getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F); 
        setHealth(0.0F);
        onDeath(DamageSource.generic);
      } else if (id == 30) {
        playSound(SoundEvents.ITEM_SHIELD_BREAK, 0.8F, 0.8F + this.world.rand.nextFloat() * 0.4F);
      } else if (id == 29) {
        playSound(SoundEvents.ITEM_SHIELD_BLOCK, 1.0F, 0.8F + this.world.rand.nextFloat() * 0.4F);
      } else {
        super.handleStatusUpdate(id);
      } 
    } else {
      DamageSource damagesource;
      this.limbSwingAmount = 1.5F;
      this.hurtResistantTime = this.maxHurtResistantTime;
      this.maxHurtTime = 10;
      this.hurtTime = this.maxHurtTime;
      this.attackedAtYaw = 0.0F;
      if (flag)
        playSound(SoundEvents.ENCHANT_THORNS_HIT, getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F); 
      if (flag2) {
        damagesource = DamageSource.onFire;
      } else if (flag1) {
        damagesource = DamageSource.drown;
      } else {
        damagesource = DamageSource.generic;
      } 
      SoundEvent soundevent = getHurtSound(damagesource);
      if (soundevent != null)
        playSound(soundevent, getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F); 
      attackEntityFrom(DamageSource.generic, 0.0F);
    } 
  }
  
  protected void kill() {
    attackEntityFrom(DamageSource.outOfWorld, 4.0F);
  }
  
  protected void updateArmSwingProgress() {
    int i = getArmSwingAnimationEnd();
    if (this.isSwingInProgress) {
      this.swingProgressInt++;
      if (this.swingProgressInt >= i) {
        this.swingProgressInt = 0;
        this.isSwingInProgress = false;
      } 
    } else {
      this.swingProgressInt = 0;
    } 
    this.swingProgress = this.swingProgressInt / i;
  }
  
  public IAttributeInstance getEntityAttribute(IAttribute attribute) {
    return getAttributeMap().getAttributeInstance(attribute);
  }
  
  public AbstractAttributeMap getAttributeMap() {
    if (this.attributeMap == null)
      this.attributeMap = (AbstractAttributeMap)new AttributeMap(); 
    return this.attributeMap;
  }
  
  public EnumCreatureAttribute getCreatureAttribute() {
    return EnumCreatureAttribute.UNDEFINED;
  }
  
  public ItemStack getHeldItemMainhand() {
    return getItemStackFromSlot(EntityEquipmentSlot.MAINHAND);
  }
  
  public ItemStack getHeldItemOffhand() {
    return getItemStackFromSlot(EntityEquipmentSlot.OFFHAND);
  }
  
  public ItemStack getHeldItem(EnumHand hand) {
    if (hand == EnumHand.MAIN_HAND)
      return getItemStackFromSlot(EntityEquipmentSlot.MAINHAND); 
    if (hand == EnumHand.OFF_HAND)
      return getItemStackFromSlot(EntityEquipmentSlot.OFFHAND); 
    throw new IllegalArgumentException("Invalid hand " + hand);
  }
  
  public void setHeldItem(EnumHand hand, ItemStack stack) {
    if (hand == EnumHand.MAIN_HAND) {
      setItemStackToSlot(EntityEquipmentSlot.MAINHAND, stack);
    } else {
      if (hand != EnumHand.OFF_HAND)
        throw new IllegalArgumentException("Invalid hand " + hand); 
      setItemStackToSlot(EntityEquipmentSlot.OFFHAND, stack);
    } 
  }
  
  public boolean func_190630_a(EntityEquipmentSlot p_190630_1_) {
    return !getItemStackFromSlot(p_190630_1_).func_190926_b();
  }
  
  public void setSprinting(boolean sprinting) {
    super.setSprinting(sprinting);
    IAttributeInstance iattributeinstance = getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
    if (iattributeinstance.getModifier(SPRINTING_SPEED_BOOST_ID) != null)
      iattributeinstance.removeModifier(SPRINTING_SPEED_BOOST); 
    if (sprinting)
      iattributeinstance.applyModifier(SPRINTING_SPEED_BOOST); 
  }
  
  protected float getSoundVolume() {
    return 1.0F;
  }
  
  protected float getSoundPitch() {
    return isChild() ? ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.5F) : ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
  }
  
  protected boolean isMovementBlocked() {
    return (getHealth() <= 0.0F);
  }
  
  public void dismountEntity(Entity entityIn) {
    if (!(entityIn instanceof net.minecraft.entity.item.EntityBoat) && !(entityIn instanceof net.minecraft.entity.passive.AbstractHorse)) {
      double d1 = entityIn.posX;
      double d13 = (entityIn.getEntityBoundingBox()).minY + entityIn.height;
      double d14 = entityIn.posZ;
      EnumFacing enumfacing1 = entityIn.getAdjustedHorizontalFacing();
      if (enumfacing1 != null) {
        EnumFacing enumfacing = enumfacing1.rotateY();
        int[][] aint1 = { { 0, 1 }, { 0, -1 }, { -1, 1 }, { -1, -1 }, { 1, 1 }, { 1, -1 }, { -1 }, { 1 }, { 0, 1 } };
        double d5 = Math.floor(this.posX) + 0.5D;
        double d6 = Math.floor(this.posZ) + 0.5D;
        double d7 = (getEntityBoundingBox()).maxX - (getEntityBoundingBox()).minX;
        double d8 = (getEntityBoundingBox()).maxZ - (getEntityBoundingBox()).minZ;
        AxisAlignedBB axisalignedbb = new AxisAlignedBB(d5 - d7 / 2.0D, (entityIn.getEntityBoundingBox()).minY, d6 - d8 / 2.0D, d5 + d7 / 2.0D, Math.floor((entityIn.getEntityBoundingBox()).minY) + this.height, d6 + d8 / 2.0D);
        byte b;
        int i, arrayOfInt1[][];
        for (i = (arrayOfInt1 = aint1).length, b = 0; b < i; ) {
          int[] aint = arrayOfInt1[b];
          double d9 = (enumfacing1.getFrontOffsetX() * aint[0] + enumfacing.getFrontOffsetX() * aint[1]);
          double d10 = (enumfacing1.getFrontOffsetZ() * aint[0] + enumfacing.getFrontOffsetZ() * aint[1]);
          double d11 = d5 + d9;
          double d12 = d6 + d10;
          AxisAlignedBB axisalignedbb1 = axisalignedbb.offset(d9, 0.0D, d10);
          if (!this.world.collidesWithAnyBlock(axisalignedbb1)) {
            if (this.world.getBlockState(new BlockPos(d11, this.posY, d12)).isFullyOpaque()) {
              setPositionAndUpdate(d11, this.posY + 1.0D, d12);
              return;
            } 
            BlockPos blockpos = new BlockPos(d11, this.posY - 1.0D, d12);
            if (this.world.getBlockState(blockpos).isFullyOpaque() || this.world.getBlockState(blockpos).getMaterial() == Material.WATER) {
              d1 = d11;
              d13 = this.posY + 1.0D;
              d14 = d12;
            } 
          } else if (!this.world.collidesWithAnyBlock(axisalignedbb1.offset(0.0D, 1.0D, 0.0D)) && this.world.getBlockState(new BlockPos(d11, this.posY + 1.0D, d12)).isFullyOpaque()) {
            d1 = d11;
            d13 = this.posY + 2.0D;
            d14 = d12;
          } 
          b++;
        } 
      } 
      setPositionAndUpdate(d1, d13, d14);
    } else {
      float f;
      double d0 = (this.width / 2.0F + entityIn.width / 2.0F) + 0.4D;
      if (entityIn instanceof net.minecraft.entity.item.EntityBoat) {
        f = 0.0F;
      } else {
        f = 1.5707964F * ((getPrimaryHand() == EnumHandSide.RIGHT) ? -1 : true);
      } 
      float f1 = -MathHelper.sin(-this.rotationYaw * 0.017453292F - 3.1415927F + f);
      float f2 = -MathHelper.cos(-this.rotationYaw * 0.017453292F - 3.1415927F + f);
      double d2 = (Math.abs(f1) > Math.abs(f2)) ? (d0 / Math.abs(f1)) : (d0 / Math.abs(f2));
      double d3 = this.posX + f1 * d2;
      double d4 = this.posZ + f2 * d2;
      setPosition(d3, entityIn.posY + entityIn.height + 0.001D, d4);
      if (this.world.collidesWithAnyBlock(getEntityBoundingBox())) {
        setPosition(d3, entityIn.posY + entityIn.height + 1.001D, d4);
        if (this.world.collidesWithAnyBlock(getEntityBoundingBox()))
          setPosition(entityIn.posX, entityIn.posY + this.height + 0.001D, entityIn.posZ); 
      } 
    } 
  }
  
  public boolean getAlwaysRenderNameTagForRender() {
    return getAlwaysRenderNameTag();
  }
  
  protected float getJumpUpwardsMotion() {
    return 0.42F;
  }
  
  protected void jump() {
    this.motionY = getJumpUpwardsMotion();
    if (isPotionActive(MobEffects.JUMP_BOOST))
      this.motionY += ((getActivePotionEffect(MobEffects.JUMP_BOOST).getAmplifier() + 1) * 0.1F); 
    if (isSprinting()) {
      float f = this.rotationYaw * 0.017453292F;
      this.motionX -= (MathHelper.sin(f) * 0.2F);
      this.motionZ += (MathHelper.cos(f) * 0.2F);
    } 
    this.isAirBorne = true;
  }
  
  protected void handleJumpWater() {
    this.motionY += 0.03999999910593033D;
  }
  
  protected void handleJumpLava() {
    this.motionY += 0.03999999910593033D;
  }
  
  protected float getWaterSlowDown() {
    return 0.8F;
  }
  
  public void func_191986_a(float p_191986_1_, float p_191986_2_, float p_191986_3_) {
    if (isServerWorld() || canPassengerSteer())
      if (!isInWater() || (this instanceof EntityPlayer && ((EntityPlayer)this).capabilities.isFlying)) {
        if (!isInLava() || (this instanceof EntityPlayer && ((EntityPlayer)this).capabilities.isFlying)) {
          if (isElytraFlying()) {
            if (this.motionY > -0.5D)
              this.fallDistance = 1.0F; 
            Vec3d vec3d = getLookVec();
            float f = this.rotationPitch * 0.017453292F;
            double d6 = Math.sqrt(vec3d.xCoord * vec3d.xCoord + vec3d.zCoord * vec3d.zCoord);
            double d8 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
            double d1 = vec3d.lengthVector();
            float f4 = MathHelper.cos(f);
            f4 = (float)(f4 * f4 * Math.min(1.0D, d1 / 0.4D));
            this.motionY += -0.08D + f4 * 0.06D;
            if (this.motionY < 0.0D && d6 > 0.0D) {
              double d2 = this.motionY * -0.1D * f4;
              this.motionY += d2;
              this.motionX += vec3d.xCoord * d2 / d6;
              this.motionZ += vec3d.zCoord * d2 / d6;
            } 
            if (f < 0.0F) {
              double d10 = d8 * -MathHelper.sin(f) * 0.04D;
              this.motionY += d10 * 3.2D;
              this.motionX -= vec3d.xCoord * d10 / d6;
              this.motionZ -= vec3d.zCoord * d10 / d6;
            } 
            if (d6 > 0.0D) {
              this.motionX += (vec3d.xCoord / d6 * d8 - this.motionX) * 0.1D;
              this.motionZ += (vec3d.zCoord / d6 * d8 - this.motionZ) * 0.1D;
            } 
            this.motionX *= 0.9900000095367432D;
            this.motionY *= 0.9800000190734863D;
            this.motionZ *= 0.9900000095367432D;
            moveEntity(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
            if (this.isCollidedHorizontally && !this.world.isRemote) {
              double d11 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
              double d3 = d8 - d11;
              float f5 = (float)(d3 * 10.0D - 3.0D);
              if (f5 > 0.0F) {
                playSound(getFallSound((int)f5), 1.0F, 1.0F);
                attackEntityFrom(DamageSource.flyIntoWall, f5);
              } 
            } 
            if (this.onGround && !this.world.isRemote)
              setFlag(7, false); 
          } else {
            float f8, f6 = 0.91F;
            BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain(this.posX, (getEntityBoundingBox()).minY - 1.0D, this.posZ);
            if (this.onGround)
              f6 = (this.world.getBlockState((BlockPos)blockpos$pooledmutableblockpos).getBlock()).slipperiness * 0.91F; 
            float f7 = 0.16277136F / f6 * f6 * f6;
            if (this.onGround) {
              f8 = getAIMoveSpeed() * f7;
            } else {
              f8 = this.jumpMovementFactor;
            } 
            func_191958_b(p_191986_1_, p_191986_2_, p_191986_3_, f8);
            f6 = 0.91F;
            if (this.onGround)
              f6 = (this.world.getBlockState((BlockPos)blockpos$pooledmutableblockpos.setPos(this.posX, (getEntityBoundingBox()).minY - 1.0D, this.posZ)).getBlock()).slipperiness * 0.91F; 
            if (isOnLadder()) {
              float f9 = 0.15F;
              this.motionX = MathHelper.clamp(this.motionX, -0.15000000596046448D, 0.15000000596046448D);
              this.motionZ = MathHelper.clamp(this.motionZ, -0.15000000596046448D, 0.15000000596046448D);
              this.fallDistance = 0.0F;
              if (this.motionY < -0.15D)
                this.motionY = -0.15D; 
              boolean flag = (isSneaking() && this instanceof EntityPlayer);
              if (flag && this.motionY < 0.0D)
                this.motionY = 0.0D; 
            } 
            moveEntity(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
            if (this.isCollidedHorizontally && isOnLadder())
              this.motionY = 0.2D; 
            if (isPotionActive(MobEffects.LEVITATION)) {
              this.motionY += (0.05D * (getActivePotionEffect(MobEffects.LEVITATION).getAmplifier() + 1) - this.motionY) * 0.2D;
            } else {
              blockpos$pooledmutableblockpos.setPos(this.posX, 0.0D, this.posZ);
              if (!this.world.isRemote || (this.world.isBlockLoaded((BlockPos)blockpos$pooledmutableblockpos) && this.world.getChunkFromBlockCoords((BlockPos)blockpos$pooledmutableblockpos).isLoaded())) {
                if (!hasNoGravity())
                  this.motionY -= 0.08D; 
              } else if (this.posY > 0.0D) {
                this.motionY = -0.1D;
              } else {
                this.motionY = 0.0D;
              } 
            } 
            this.motionY *= 0.9800000190734863D;
            this.motionX *= f6;
            this.motionZ *= f6;
            blockpos$pooledmutableblockpos.release();
          } 
        } else {
          double d4 = this.posY;
          func_191958_b(p_191986_1_, p_191986_2_, p_191986_3_, 0.02F);
          moveEntity(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
          this.motionX *= 0.5D;
          this.motionY *= 0.5D;
          this.motionZ *= 0.5D;
          if (!hasNoGravity())
            this.motionY -= 0.02D; 
          if (this.isCollidedHorizontally && isOffsetPositionInLiquid(this.motionX, this.motionY + 0.6000000238418579D - this.posY + d4, this.motionZ))
            this.motionY = 0.30000001192092896D; 
        } 
      } else {
        double d0 = this.posY;
        float f1 = getWaterSlowDown();
        float f2 = 0.02F;
        float f3 = EnchantmentHelper.getDepthStriderModifier(this);
        if (f3 > 3.0F)
          f3 = 3.0F; 
        if (!this.onGround)
          f3 *= 0.5F; 
        if (f3 > 0.0F) {
          f1 += (0.54600006F - f1) * f3 / 3.0F;
          f2 += (getAIMoveSpeed() - f2) * f3 / 3.0F;
        } 
        func_191958_b(p_191986_1_, p_191986_2_, p_191986_3_, f2);
        moveEntity(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
        this.motionX *= f1;
        this.motionY *= 0.800000011920929D;
        this.motionZ *= f1;
        if (!hasNoGravity())
          this.motionY -= 0.02D; 
        if (this.isCollidedHorizontally && isOffsetPositionInLiquid(this.motionX, this.motionY + 0.6000000238418579D - this.posY + d0, this.motionZ))
          this.motionY = 0.30000001192092896D; 
      }  
    this.prevLimbSwingAmount = this.limbSwingAmount;
    double d5 = this.posX - this.prevPosX;
    double d7 = this.posZ - this.prevPosZ;
    double d9 = (this instanceof net.minecraft.entity.passive.EntityFlying) ? (this.posY - this.prevPosY) : 0.0D;
    float f10 = MathHelper.sqrt(d5 * d5 + d9 * d9 + d7 * d7) * 4.0F;
    if (f10 > 1.0F)
      f10 = 1.0F; 
    this.limbSwingAmount += (f10 - this.limbSwingAmount) * 0.4F;
    this.limbSwing += this.limbSwingAmount;
  }
  
  public float getAIMoveSpeed() {
    return this.landMovementFactor;
  }
  
  public void setAIMoveSpeed(float speedIn) {
    this.landMovementFactor = speedIn;
  }
  
  public boolean attackEntityAsMob(Entity entityIn) {
    setLastAttacker(entityIn);
    return false;
  }
  
  public boolean isPlayerSleeping() {
    return false;
  }
  
  public void onUpdate() {
    super.onUpdate();
    updateActiveHand();
    if (!this.world.isRemote) {
      int i = getArrowCountInEntity();
      if (i > 0) {
        if (this.arrowHitTimer <= 0)
          this.arrowHitTimer = 20 * (30 - i); 
        this.arrowHitTimer--;
        if (this.arrowHitTimer <= 0)
          setArrowCountInEntity(i - 1); 
      } 
      byte b;
      int j;
      EntityEquipmentSlot[] arrayOfEntityEquipmentSlot;
      for (j = (arrayOfEntityEquipmentSlot = EntityEquipmentSlot.values()).length, b = 0; b < j; ) {
        ItemStack itemstack;
        EntityEquipmentSlot entityequipmentslot = arrayOfEntityEquipmentSlot[b];
        switch (entityequipmentslot.getSlotType()) {
          case HAND:
            itemstack = (ItemStack)this.handInventory.get(entityequipmentslot.getIndex());
            break;
          case null:
            itemstack = (ItemStack)this.armorArray.get(entityequipmentslot.getIndex());
            break;
          default:
            b++;
            continue;
        } 
        ItemStack itemstack1 = getItemStackFromSlot(entityequipmentslot);
        if (!ItemStack.areItemStacksEqual(itemstack1, itemstack)) {
          ((WorldServer)this.world).getEntityTracker().sendToAllTrackingEntity(this, (Packet<?>)new SPacketEntityEquipment(getEntityId(), entityequipmentslot, itemstack1));
          if (!itemstack.func_190926_b())
            getAttributeMap().removeAttributeModifiers(itemstack.getAttributeModifiers(entityequipmentslot)); 
          if (!itemstack1.func_190926_b())
            getAttributeMap().applyAttributeModifiers(itemstack1.getAttributeModifiers(entityequipmentslot)); 
          switch (entityequipmentslot.getSlotType()) {
            case HAND:
              this.handInventory.set(entityequipmentslot.getIndex(), itemstack1.func_190926_b() ? ItemStack.field_190927_a : itemstack1.copy());
            case null:
              this.armorArray.set(entityequipmentslot.getIndex(), itemstack1.func_190926_b() ? ItemStack.field_190927_a : itemstack1.copy());
          } 
        } 
      } 
      if (this.ticksExisted % 20 == 0)
        getCombatTracker().reset(); 
      if (!this.glowing) {
        boolean flag = isPotionActive(MobEffects.GLOWING);
        if (getFlag(6) != flag)
          setFlag(6, flag); 
      } 
    } 
    onLivingUpdate();
    double d0 = this.posX - this.prevPosX;
    double d1 = this.posZ - this.prevPosZ;
    float f3 = (float)(d0 * d0 + d1 * d1);
    float f4 = this.renderYawOffset;
    float f5 = 0.0F;
    this.prevOnGroundSpeedFactor = this.onGroundSpeedFactor;
    float f = 0.0F;
    if (f3 > 0.0025000002F) {
      f = 1.0F;
      f5 = (float)Math.sqrt(f3) * 3.0F;
      float f1 = (float)MathHelper.atan2(d1, d0) * 57.295776F - 90.0F;
      float f2 = MathHelper.abs(MathHelper.wrapDegrees(this.rotationYaw) - f1);
      if (95.0F < f2 && f2 < 265.0F) {
        float f6 = f1 - 180.0F;
      } else {
        f4 = f1;
      } 
    } 
    if (this.swingProgress > 0.0F)
      f4 = this.rotationYaw; 
    if (!this.onGround)
      f = 0.0F; 
    this.onGroundSpeedFactor += (f - this.onGroundSpeedFactor) * 0.3F;
    this.world.theProfiler.startSection("headTurn");
    f5 = updateDistance(f4, f5);
    this.world.theProfiler.endSection();
    this.world.theProfiler.startSection("rangeChecks");
    while (this.rotationYaw - this.prevRotationYaw < -180.0F)
      this.prevRotationYaw -= 360.0F; 
    while (this.rotationYaw - this.prevRotationYaw >= 180.0F)
      this.prevRotationYaw += 360.0F; 
    while (this.renderYawOffset - this.prevRenderYawOffset < -180.0F)
      this.prevRenderYawOffset -= 360.0F; 
    while (this.renderYawOffset - this.prevRenderYawOffset >= 180.0F)
      this.prevRenderYawOffset += 360.0F; 
    while (this.rotationPitch - this.prevRotationPitch < -180.0F)
      this.prevRotationPitch -= 360.0F; 
    while (this.rotationPitch - this.prevRotationPitch >= 180.0F)
      this.prevRotationPitch += 360.0F; 
    while (this.rotationYawHead - this.prevRotationYawHead < -180.0F)
      this.prevRotationYawHead -= 360.0F; 
    while (this.rotationYawHead - this.prevRotationYawHead >= 180.0F)
      this.prevRotationYawHead += 360.0F; 
    this.world.theProfiler.endSection();
    this.movedDistance += f5;
    if (isElytraFlying()) {
      this.ticksElytraFlying++;
    } else {
      this.ticksElytraFlying = 0;
    } 
  }
  
  protected float updateDistance(float p_110146_1_, float p_110146_2_) {
    float f = MathHelper.wrapDegrees(p_110146_1_ - this.renderYawOffset);
    this.renderYawOffset += f * 0.3F;
    float f1 = MathHelper.wrapDegrees(this.rotationYaw - this.renderYawOffset);
    boolean flag = !(f1 >= -90.0F && f1 < 90.0F);
    if (f1 < -75.0F)
      f1 = -75.0F; 
    if (f1 >= 75.0F)
      f1 = 75.0F; 
    this.renderYawOffset = this.rotationYaw - f1;
    if (f1 * f1 > 2500.0F)
      this.renderYawOffset += f1 * 0.2F; 
    if (flag)
      p_110146_2_ *= -1.0F; 
    return p_110146_2_;
  }
  
  public void onLivingUpdate() {
    if (this.jumpTicks > 0)
      this.jumpTicks--; 
    if (this.newPosRotationIncrements > 0 && !canPassengerSteer()) {
      double d0 = this.posX + (this.interpTargetX - this.posX) / this.newPosRotationIncrements;
      double d1 = this.posY + (this.interpTargetY - this.posY) / this.newPosRotationIncrements;
      double d2 = this.posZ + (this.interpTargetZ - this.posZ) / this.newPosRotationIncrements;
      double d3 = MathHelper.wrapDegrees(this.interpTargetYaw - this.rotationYaw);
      this.rotationYaw = (float)(this.rotationYaw + d3 / this.newPosRotationIncrements);
      this.rotationPitch = (float)(this.rotationPitch + (this.interpTargetPitch - this.rotationPitch) / this.newPosRotationIncrements);
      this.newPosRotationIncrements--;
      setPosition(d0, d1, d2);
      setRotation(this.rotationYaw, this.rotationPitch);
    } else if (!isServerWorld()) {
      this.motionX *= 0.98D;
      this.motionY *= 0.98D;
      this.motionZ *= 0.98D;
    } 
    if (Math.abs(this.motionX) < 0.003D)
      this.motionX = 0.0D; 
    if (Math.abs(this.motionY) < 0.003D)
      this.motionY = 0.0D; 
    if (Math.abs(this.motionZ) < 0.003D)
      this.motionZ = 0.0D; 
    this.world.theProfiler.startSection("ai");
    if (isMovementBlocked()) {
      this.isJumping = false;
      this.moveStrafing = 0.0F;
      this.field_191988_bg = 0.0F;
      this.randomYawVelocity = 0.0F;
    } else if (isServerWorld()) {
      this.world.theProfiler.startSection("newAi");
      updateEntityActionState();
      this.world.theProfiler.endSection();
    } 
    this.world.theProfiler.endSection();
    this.world.theProfiler.startSection("jump");
    if (this.isJumping) {
      if (isInWater()) {
        handleJumpWater();
      } else if (isInLava()) {
        handleJumpLava();
      } else if (this.onGround && this.jumpTicks == 0) {
        jump();
        this.jumpTicks = 10;
      } 
    } else {
      this.jumpTicks = 0;
    } 
    this.world.theProfiler.endSection();
    this.world.theProfiler.startSection("travel");
    this.moveStrafing *= 0.98F;
    this.field_191988_bg *= 0.98F;
    this.randomYawVelocity *= 0.9F;
    updateElytra();
    func_191986_a(this.moveStrafing, this.moveForward, this.field_191988_bg);
    this.world.theProfiler.endSection();
    this.world.theProfiler.startSection("push");
    collideWithNearbyEntities();
    this.world.theProfiler.endSection();
  }
  
  private void updateElytra() {
    boolean flag = getFlag(7);
    if (flag && !this.onGround && !isRiding()) {
      ItemStack itemstack = getItemStackFromSlot(EntityEquipmentSlot.CHEST);
      if (itemstack.getItem() == Items.ELYTRA && ItemElytra.isBroken(itemstack)) {
        flag = true;
        if (!this.world.isRemote && (this.ticksElytraFlying + 1) % 20 == 0)
          itemstack.damageItem(1, this); 
      } else {
        flag = false;
      } 
    } else {
      flag = false;
    } 
    if (!this.world.isRemote)
      setFlag(7, flag); 
  }
  
  protected void updateEntityActionState() {}
  
  protected void collideWithNearbyEntities() {
    List<Entity> list = this.world.getEntitiesInAABBexcluding(this, getEntityBoundingBox(), EntitySelectors.getTeamCollisionPredicate(this));
    if (!list.isEmpty()) {
      int i = this.world.getGameRules().getInt("maxEntityCramming");
      if (i > 0 && list.size() > i - 1 && this.rand.nextInt(4) == 0) {
        int j = 0;
        for (int k = 0; k < list.size(); k++) {
          if (!((Entity)list.get(k)).isRiding())
            j++; 
        } 
        if (j > i - 1)
          attackEntityFrom(DamageSource.field_191291_g, 6.0F); 
      } 
      for (int l = 0; l < list.size(); l++) {
        Entity entity = list.get(l);
        collideWithEntity(entity);
      } 
    } 
  }
  
  protected void collideWithEntity(Entity entityIn) {
    entityIn.applyEntityCollision(this);
  }
  
  public void dismountRidingEntity() {
    Entity entity = getRidingEntity();
    super.dismountRidingEntity();
    if (entity != null && entity != getRidingEntity() && !this.world.isRemote)
      dismountEntity(entity); 
  }
  
  public void updateRidden() {
    super.updateRidden();
    this.prevOnGroundSpeedFactor = this.onGroundSpeedFactor;
    this.onGroundSpeedFactor = 0.0F;
    this.fallDistance = 0.0F;
  }
  
  public void setPositionAndRotationDirect(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean teleport) {
    this.interpTargetX = x;
    this.interpTargetY = y;
    this.interpTargetZ = z;
    this.interpTargetYaw = yaw;
    this.interpTargetPitch = pitch;
    this.newPosRotationIncrements = posRotationIncrements;
  }
  
  public void setJumping(boolean jumping) {
    this.isJumping = jumping;
  }
  
  public void onItemPickup(Entity entityIn, int quantity) {
    if (!entityIn.isDead && !this.world.isRemote) {
      EntityTracker entitytracker = ((WorldServer)this.world).getEntityTracker();
      if (entityIn instanceof net.minecraft.entity.item.EntityItem || entityIn instanceof net.minecraft.entity.projectile.EntityArrow || entityIn instanceof EntityXPOrb)
        entitytracker.sendToAllTrackingEntity(entityIn, (Packet<?>)new SPacketCollectItem(entityIn.getEntityId(), getEntityId(), quantity)); 
    } 
  }
  
  public boolean canEntityBeSeen(Entity entityIn) {
    return (this.world.rayTraceBlocks(new Vec3d(this.posX, this.posY + getEyeHeight(), this.posZ), new Vec3d(entityIn.posX, entityIn.posY + entityIn.getEyeHeight(), entityIn.posZ), false, true, false) == null);
  }
  
  public Vec3d getLook(float partialTicks) {
    if (partialTicks == 1.0F)
      return getVectorForRotation(this.rotationPitch, this.rotationYawHead); 
    float f = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * partialTicks;
    float f1 = this.prevRotationYawHead + (this.rotationYawHead - this.prevRotationYawHead) * partialTicks;
    return getVectorForRotation(f, f1);
  }
  
  public float getSwingProgress(float partialTickTime) {
    float f = this.swingProgress - this.prevSwingProgress;
    if (f < 0.0F)
      f++; 
    return this.prevSwingProgress + f * partialTickTime;
  }
  
  public boolean isServerWorld() {
    return !this.world.isRemote;
  }
  
  public boolean canBeCollidedWith() {
    return !this.isDead;
  }
  
  public boolean canBePushed() {
    return (isEntityAlive() && !isOnLadder());
  }
  
  protected void setBeenAttacked() {
    this.velocityChanged = (this.rand.nextDouble() >= getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).getAttributeValue());
  }
  
  public float getRotationYawHead() {
    return this.rotationYawHead;
  }
  
  public void setRotationYawHead(float rotation) {
    this.rotationYawHead = rotation;
  }
  
  public void setRenderYawOffset(float offset) {
    this.renderYawOffset = offset;
  }
  
  public float getAbsorptionAmount() {
    return this.absorptionAmount;
  }
  
  public void setAbsorptionAmount(float amount) {
    if (amount < 0.0F)
      amount = 0.0F; 
    this.absorptionAmount = amount;
  }
  
  public void sendEnterCombat() {}
  
  public void sendEndCombat() {}
  
  protected void markPotionsDirty() {
    this.potionsNeedUpdate = true;
  }
  
  public boolean isHandActive() {
    return ((((Byte)this.dataManager.get(HAND_STATES)).byteValue() & 0x1) > 0);
  }
  
  public EnumHand getActiveHand() {
    return ((((Byte)this.dataManager.get(HAND_STATES)).byteValue() & 0x2) > 0) ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND;
  }
  
  protected void updateActiveHand() {
    if (isHandActive()) {
      ItemStack itemstack = getHeldItem(getActiveHand());
      if (itemstack == this.activeItemStack) {
        if (getItemInUseCount() <= 25 && getItemInUseCount() % 4 == 0)
          updateItemUse(this.activeItemStack, 5); 
        if (--this.activeItemStackUseCount == 0 && !this.world.isRemote)
          onItemUseFinish(); 
      } else {
        resetActiveHand();
      } 
    } 
  }
  
  public void setActiveHand(EnumHand hand) {
    ItemStack itemstack = getHeldItem(hand);
    if (!itemstack.func_190926_b() && !isHandActive()) {
      this.activeItemStack = itemstack;
      this.activeItemStackUseCount = itemstack.getMaxItemUseDuration();
      if (!this.world.isRemote) {
        int i = 1;
        if (hand == EnumHand.OFF_HAND)
          i |= 0x2; 
        this.dataManager.set(HAND_STATES, Byte.valueOf((byte)i));
      } 
    } 
  }
  
  public void notifyDataManagerChange(DataParameter<?> key) {
    super.notifyDataManagerChange(key);
    if (HAND_STATES.equals(key) && this.world.isRemote)
      if (isHandActive() && this.activeItemStack.func_190926_b()) {
        this.activeItemStack = getHeldItem(getActiveHand());
        if (!this.activeItemStack.func_190926_b())
          this.activeItemStackUseCount = this.activeItemStack.getMaxItemUseDuration(); 
      } else if (!isHandActive() && !this.activeItemStack.func_190926_b()) {
        this.activeItemStack = ItemStack.field_190927_a;
        this.activeItemStackUseCount = 0;
      }  
  }
  
  protected void updateItemUse(ItemStack stack, int eatingParticleCount) {
    if (!stack.func_190926_b() && isHandActive()) {
      if (stack.getItemUseAction() == EnumAction.DRINK)
        playSound(SoundEvents.ENTITY_GENERIC_DRINK, 0.5F, this.world.rand.nextFloat() * 0.1F + 0.9F); 
      if (stack.getItemUseAction() == EnumAction.EAT) {
        for (int i = 0; i < eatingParticleCount; i++) {
          Vec3d vec3d = new Vec3d((this.rand.nextFloat() - 0.5D) * 0.1D, Math.random() * 0.1D + 0.1D, 0.0D);
          vec3d = vec3d.rotatePitch(-this.rotationPitch * 0.017453292F);
          vec3d = vec3d.rotateYaw(-this.rotationYaw * 0.017453292F);
          double d0 = -this.rand.nextFloat() * 0.6D - 0.3D;
          Vec3d vec3d1 = new Vec3d((this.rand.nextFloat() - 0.5D) * 0.3D, d0, 0.6D);
          vec3d1 = vec3d1.rotatePitch(-this.rotationPitch * 0.017453292F);
          vec3d1 = vec3d1.rotateYaw(-this.rotationYaw * 0.017453292F);
          vec3d1 = vec3d1.addVector(this.posX, this.posY + getEyeHeight(), this.posZ);
          if (stack.getHasSubtypes()) {
            this.world.spawnParticle(EnumParticleTypes.ITEM_CRACK, vec3d1.xCoord, vec3d1.yCoord, vec3d1.zCoord, vec3d.xCoord, vec3d.yCoord + 0.05D, vec3d.zCoord, new int[] { Item.getIdFromItem(stack.getItem()), stack.getMetadata() });
          } else {
            this.world.spawnParticle(EnumParticleTypes.ITEM_CRACK, vec3d1.xCoord, vec3d1.yCoord, vec3d1.zCoord, vec3d.xCoord, vec3d.yCoord + 0.05D, vec3d.zCoord, new int[] { Item.getIdFromItem(stack.getItem()) });
          } 
        } 
        playSound(SoundEvents.ENTITY_GENERIC_EAT, 0.5F + 0.5F * this.rand.nextInt(2), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
      } 
    } 
  }
  
  protected void onItemUseFinish() {
    if (!this.activeItemStack.func_190926_b() && isHandActive()) {
      updateItemUse(this.activeItemStack, 16);
      setHeldItem(getActiveHand(), this.activeItemStack.onItemUseFinish(this.world, this));
      resetActiveHand();
    } 
  }
  
  public ItemStack getActiveItemStack() {
    return this.activeItemStack;
  }
  
  public int getItemInUseCount() {
    return this.activeItemStackUseCount;
  }
  
  public int getItemInUseMaxCount() {
    return isHandActive() ? (this.activeItemStack.getMaxItemUseDuration() - getItemInUseCount()) : 0;
  }
  
  public void stopActiveHand() {
    if (!this.activeItemStack.func_190926_b())
      this.activeItemStack.onPlayerStoppedUsing(this.world, this, getItemInUseCount()); 
    resetActiveHand();
  }
  
  public void resetActiveHand() {
    if (!this.world.isRemote)
      this.dataManager.set(HAND_STATES, Byte.valueOf((byte)0)); 
    this.activeItemStack = ItemStack.field_190927_a;
    this.activeItemStackUseCount = 0;
  }
  
  public boolean isActiveItemStackBlocking() {
    if (isHandActive() && !this.activeItemStack.func_190926_b()) {
      Item item = this.activeItemStack.getItem();
      if (item.getItemUseAction(this.activeItemStack) != EnumAction.BLOCK)
        return false; 
      return (item.getMaxItemUseDuration(this.activeItemStack) - this.activeItemStackUseCount >= 5);
    } 
    return false;
  }
  
  public boolean isElytraFlying() {
    return getFlag(7);
  }
  
  public int getTicksElytraFlying() {
    return this.ticksElytraFlying;
  }
  
  public boolean attemptTeleport(double x, double y, double z) {
    double d0 = this.posX;
    double d1 = this.posY;
    double d2 = this.posZ;
    this.posX = x;
    this.posY = y;
    this.posZ = z;
    boolean flag = false;
    BlockPos blockpos = new BlockPos(this);
    World world = this.world;
    Random random = getRNG();
    if (world.isBlockLoaded(blockpos)) {
      boolean flag1 = false;
      while (!flag1 && blockpos.getY() > 0) {
        BlockPos blockpos1 = blockpos.down();
        IBlockState iblockstate = world.getBlockState(blockpos1);
        if (iblockstate.getMaterial().blocksMovement()) {
          flag1 = true;
          continue;
        } 
        this.posY--;
        blockpos = blockpos1;
      } 
      if (flag1) {
        setPositionAndUpdate(this.posX, this.posY, this.posZ);
        if (world.getCollisionBoxes(this, getEntityBoundingBox()).isEmpty() && !world.containsAnyLiquid(getEntityBoundingBox()))
          flag = true; 
      } 
    } 
    if (!flag) {
      setPositionAndUpdate(d0, d1, d2);
      return false;
    } 
    int i = 128;
    for (int j = 0; j < 128; j++) {
      double d6 = j / 127.0D;
      float f = (random.nextFloat() - 0.5F) * 0.2F;
      float f1 = (random.nextFloat() - 0.5F) * 0.2F;
      float f2 = (random.nextFloat() - 0.5F) * 0.2F;
      double d3 = d0 + (this.posX - d0) * d6 + (random.nextDouble() - 0.5D) * this.width * 2.0D;
      double d4 = d1 + (this.posY - d1) * d6 + random.nextDouble() * this.height;
      double d5 = d2 + (this.posZ - d2) * d6 + (random.nextDouble() - 0.5D) * this.width * 2.0D;
      world.spawnParticle(EnumParticleTypes.PORTAL, d3, d4, d5, f, f1, f2, new int[0]);
    } 
    if (this instanceof EntityCreature)
      ((EntityCreature)this).getNavigator().clearPathEntity(); 
    return true;
  }
  
  public boolean canBeHitWithPotion() {
    return true;
  }
  
  public boolean func_190631_cK() {
    return true;
  }
  
  public void func_191987_a(BlockPos p_191987_1_, boolean p_191987_2_) {}
  
  public abstract Iterable<ItemStack> getArmorInventoryList();
  
  public abstract ItemStack getItemStackFromSlot(EntityEquipmentSlot paramEntityEquipmentSlot);
  
  public abstract void setItemStackToSlot(EntityEquipmentSlot paramEntityEquipmentSlot, ItemStack paramItemStack);
  
  public abstract EnumHandSide getPrimaryHand();
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\entity\EntityLivingBase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
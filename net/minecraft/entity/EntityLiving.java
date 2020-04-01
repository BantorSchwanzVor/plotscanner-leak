package net.minecraft.entity;

import com.google.common.collect.Maps;
import java.util.Arrays;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.ai.EntityJumpHelper;
import net.minecraft.entity.ai.EntityLookHelper;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.ai.EntitySenses;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.Packet;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SPacketEntityAttach;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.IDataWalker;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTable;
import optifine.Config;
import optifine.Reflector;

public abstract class EntityLiving extends EntityLivingBase {
  private static final DataParameter<Byte> AI_FLAGS = EntityDataManager.createKey(EntityLiving.class, DataSerializers.BYTE);
  
  public int livingSoundTime;
  
  protected int experienceValue;
  
  private final EntityLookHelper lookHelper;
  
  protected EntityMoveHelper moveHelper;
  
  protected EntityJumpHelper jumpHelper;
  
  private final EntityBodyHelper bodyHelper;
  
  protected PathNavigate navigator;
  
  protected final EntityAITasks tasks;
  
  protected final EntityAITasks targetTasks;
  
  private EntityLivingBase attackTarget;
  
  private final EntitySenses senses;
  
  private final NonNullList<ItemStack> inventoryHands = NonNullList.func_191197_a(2, ItemStack.field_190927_a);
  
  protected float[] inventoryHandsDropChances = new float[2];
  
  private final NonNullList<ItemStack> inventoryArmor = NonNullList.func_191197_a(4, ItemStack.field_190927_a);
  
  protected float[] inventoryArmorDropChances = new float[4];
  
  private boolean canPickUpLoot;
  
  private boolean persistenceRequired;
  
  private final Map<PathNodeType, Float> mapPathPriority = Maps.newEnumMap(PathNodeType.class);
  
  private ResourceLocation deathLootTable;
  
  private long deathLootTableSeed;
  
  private boolean isLeashed;
  
  private Entity leashedToEntity;
  
  private NBTTagCompound leashNBTTag;
  
  public int randomMobsId = 0;
  
  public Biome spawnBiome = null;
  
  public BlockPos spawnPosition = null;
  
  private UUID teamUuid = null;
  
  private String teamUuidString = null;
  
  public EntityLiving(World worldIn) {
    super(worldIn);
    this.tasks = new EntityAITasks((worldIn != null && worldIn.theProfiler != null) ? worldIn.theProfiler : null);
    this.targetTasks = new EntityAITasks((worldIn != null && worldIn.theProfiler != null) ? worldIn.theProfiler : null);
    this.lookHelper = new EntityLookHelper(this);
    this.moveHelper = new EntityMoveHelper(this);
    this.jumpHelper = new EntityJumpHelper(this);
    this.bodyHelper = createBodyHelper();
    this.navigator = getNewNavigator(worldIn);
    this.senses = new EntitySenses(this);
    Arrays.fill(this.inventoryArmorDropChances, 0.085F);
    Arrays.fill(this.inventoryHandsDropChances, 0.085F);
    if (worldIn != null && !worldIn.isRemote)
      initEntityAI(); 
    UUID uuid = getUniqueID();
    long i = uuid.getLeastSignificantBits();
    this.randomMobsId = (int)(i & 0x7FFFFFFFL);
  }
  
  protected void initEntityAI() {}
  
  protected void applyEntityAttributes() {
    super.applyEntityAttributes();
    getAttributeMap().registerAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(16.0D);
  }
  
  protected PathNavigate getNewNavigator(World worldIn) {
    return (PathNavigate)new PathNavigateGround(this, worldIn);
  }
  
  public float getPathPriority(PathNodeType nodeType) {
    Float f = this.mapPathPriority.get(nodeType);
    return (f == null) ? nodeType.getPriority() : f.floatValue();
  }
  
  public void setPathPriority(PathNodeType nodeType, float priority) {
    this.mapPathPriority.put(nodeType, Float.valueOf(priority));
  }
  
  protected EntityBodyHelper createBodyHelper() {
    return new EntityBodyHelper(this);
  }
  
  public EntityLookHelper getLookHelper() {
    return this.lookHelper;
  }
  
  public EntityMoveHelper getMoveHelper() {
    return this.moveHelper;
  }
  
  public EntityJumpHelper getJumpHelper() {
    return this.jumpHelper;
  }
  
  public PathNavigate getNavigator() {
    return this.navigator;
  }
  
  public EntitySenses getEntitySenses() {
    return this.senses;
  }
  
  @Nullable
  public EntityLivingBase getAttackTarget() {
    return this.attackTarget;
  }
  
  public void setAttackTarget(@Nullable EntityLivingBase entitylivingbaseIn) {
    this.attackTarget = entitylivingbaseIn;
    Reflector.callVoid(Reflector.ForgeHooks_onLivingSetAttackTarget, new Object[] { this, entitylivingbaseIn });
  }
  
  public boolean canAttackClass(Class<? extends EntityLivingBase> cls) {
    return (cls != EntityGhast.class);
  }
  
  public void eatGrassBonus() {}
  
  protected void entityInit() {
    super.entityInit();
    this.dataManager.register(AI_FLAGS, Byte.valueOf((byte)0));
  }
  
  public int getTalkInterval() {
    return 80;
  }
  
  public void playLivingSound() {
    SoundEvent soundevent = getAmbientSound();
    if (soundevent != null)
      playSound(soundevent, getSoundVolume(), getSoundPitch()); 
  }
  
  public void onEntityUpdate() {
    super.onEntityUpdate();
    this.world.theProfiler.startSection("mobBaseTick");
    if (isEntityAlive() && this.rand.nextInt(1000) < this.livingSoundTime++) {
      applyEntityAI();
      playLivingSound();
    } 
    this.world.theProfiler.endSection();
  }
  
  protected void playHurtSound(DamageSource source) {
    applyEntityAI();
    super.playHurtSound(source);
  }
  
  private void applyEntityAI() {
    this.livingSoundTime = -getTalkInterval();
  }
  
  protected int getExperiencePoints(EntityPlayer player) {
    if (this.experienceValue > 0) {
      int i = this.experienceValue;
      for (int j = 0; j < this.inventoryArmor.size(); j++) {
        if (!((ItemStack)this.inventoryArmor.get(j)).func_190926_b() && this.inventoryArmorDropChances[j] <= 1.0F)
          i += 1 + this.rand.nextInt(3); 
      } 
      for (int k = 0; k < this.inventoryHands.size(); k++) {
        if (!((ItemStack)this.inventoryHands.get(k)).func_190926_b() && this.inventoryHandsDropChances[k] <= 1.0F)
          i += 1 + this.rand.nextInt(3); 
      } 
      return i;
    } 
    return this.experienceValue;
  }
  
  public void spawnExplosionParticle() {
    if (this.world.isRemote) {
      for (int i = 0; i < 20; i++) {
        double d0 = this.rand.nextGaussian() * 0.02D;
        double d1 = this.rand.nextGaussian() * 0.02D;
        double d2 = this.rand.nextGaussian() * 0.02D;
        double d3 = 10.0D;
        this.world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, this.posX + (this.rand.nextFloat() * this.width * 2.0F) - this.width - d0 * 10.0D, this.posY + (this.rand.nextFloat() * this.height) - d1 * 10.0D, this.posZ + (this.rand.nextFloat() * this.width * 2.0F) - this.width - d2 * 10.0D, d0, d1, d2, new int[0]);
      } 
    } else {
      this.world.setEntityState(this, (byte)20);
    } 
  }
  
  public void handleStatusUpdate(byte id) {
    if (id == 20) {
      spawnExplosionParticle();
    } else {
      super.handleStatusUpdate(id);
    } 
  }
  
  public void onUpdate() {
    if (Config.isSmoothWorld() && canSkipUpdate()) {
      onUpdateMinimal();
    } else {
      super.onUpdate();
      if (!this.world.isRemote) {
        updateLeashedState();
        if (this.ticksExisted % 5 == 0) {
          boolean flag = !(getControllingPassenger() instanceof EntityLiving);
          boolean flag1 = !(getRidingEntity() instanceof net.minecraft.entity.item.EntityBoat);
          this.tasks.setControlFlag(1, flag);
          this.tasks.setControlFlag(4, (flag && flag1));
          this.tasks.setControlFlag(2, flag);
        } 
      } 
    } 
  }
  
  protected float updateDistance(float p_110146_1_, float p_110146_2_) {
    this.bodyHelper.updateRenderAngles();
    return p_110146_2_;
  }
  
  @Nullable
  protected SoundEvent getAmbientSound() {
    return null;
  }
  
  @Nullable
  protected Item getDropItem() {
    return null;
  }
  
  protected void dropFewItems(boolean wasRecentlyHit, int lootingModifier) {
    Item item = getDropItem();
    if (item != null) {
      int i = this.rand.nextInt(3);
      if (lootingModifier > 0)
        i += this.rand.nextInt(lootingModifier + 1); 
      for (int j = 0; j < i; j++)
        dropItem(item, 1); 
    } 
  }
  
  public static void registerFixesMob(DataFixer fixer, Class<?> name) {
    fixer.registerWalker(FixTypes.ENTITY, (IDataWalker)new ItemStackDataLists(name, new String[] { "ArmorItems", "HandItems" }));
  }
  
  public void writeEntityToNBT(NBTTagCompound compound) {
    super.writeEntityToNBT(compound);
    compound.setBoolean("CanPickUpLoot", canPickUpLoot());
    compound.setBoolean("PersistenceRequired", this.persistenceRequired);
    NBTTagList nbttaglist = new NBTTagList();
    for (ItemStack itemstack : this.inventoryArmor) {
      NBTTagCompound nbttagcompound = new NBTTagCompound();
      if (!itemstack.func_190926_b())
        itemstack.writeToNBT(nbttagcompound); 
      nbttaglist.appendTag((NBTBase)nbttagcompound);
    } 
    compound.setTag("ArmorItems", (NBTBase)nbttaglist);
    NBTTagList nbttaglist1 = new NBTTagList();
    for (ItemStack itemstack1 : this.inventoryHands) {
      NBTTagCompound nbttagcompound1 = new NBTTagCompound();
      if (!itemstack1.func_190926_b())
        itemstack1.writeToNBT(nbttagcompound1); 
      nbttaglist1.appendTag((NBTBase)nbttagcompound1);
    } 
    compound.setTag("HandItems", (NBTBase)nbttaglist1);
    NBTTagList nbttaglist2 = new NBTTagList();
    byte b;
    int i;
    float[] arrayOfFloat1;
    for (i = (arrayOfFloat1 = this.inventoryArmorDropChances).length, b = 0; b < i; ) {
      float f = arrayOfFloat1[b];
      nbttaglist2.appendTag((NBTBase)new NBTTagFloat(f));
      b++;
    } 
    compound.setTag("ArmorDropChances", (NBTBase)nbttaglist2);
    NBTTagList nbttaglist3 = new NBTTagList();
    float[] arrayOfFloat2;
    for (int j = (arrayOfFloat2 = this.inventoryHandsDropChances).length; i < j; ) {
      float f1 = arrayOfFloat2[i];
      nbttaglist3.appendTag((NBTBase)new NBTTagFloat(f1));
      i++;
    } 
    compound.setTag("HandDropChances", (NBTBase)nbttaglist3);
    compound.setBoolean("Leashed", this.isLeashed);
    if (this.leashedToEntity != null) {
      NBTTagCompound nbttagcompound2 = new NBTTagCompound();
      if (this.leashedToEntity instanceof EntityLivingBase) {
        UUID uuid = this.leashedToEntity.getUniqueID();
        nbttagcompound2.setUniqueId("UUID", uuid);
      } else if (this.leashedToEntity instanceof EntityHanging) {
        BlockPos blockpos = ((EntityHanging)this.leashedToEntity).getHangingPosition();
        nbttagcompound2.setInteger("X", blockpos.getX());
        nbttagcompound2.setInteger("Y", blockpos.getY());
        nbttagcompound2.setInteger("Z", blockpos.getZ());
      } 
      compound.setTag("Leash", (NBTBase)nbttagcompound2);
    } 
    compound.setBoolean("LeftHanded", isLeftHanded());
    if (this.deathLootTable != null) {
      compound.setString("DeathLootTable", this.deathLootTable.toString());
      if (this.deathLootTableSeed != 0L)
        compound.setLong("DeathLootTableSeed", this.deathLootTableSeed); 
    } 
    if (isAIDisabled())
      compound.setBoolean("NoAI", isAIDisabled()); 
  }
  
  public void readEntityFromNBT(NBTTagCompound compound) {
    super.readEntityFromNBT(compound);
    if (compound.hasKey("CanPickUpLoot", 1))
      setCanPickUpLoot(compound.getBoolean("CanPickUpLoot")); 
    this.persistenceRequired = compound.getBoolean("PersistenceRequired");
    if (compound.hasKey("ArmorItems", 9)) {
      NBTTagList nbttaglist = compound.getTagList("ArmorItems", 10);
      for (int i = 0; i < this.inventoryArmor.size(); i++)
        this.inventoryArmor.set(i, new ItemStack(nbttaglist.getCompoundTagAt(i))); 
    } 
    if (compound.hasKey("HandItems", 9)) {
      NBTTagList nbttaglist1 = compound.getTagList("HandItems", 10);
      for (int j = 0; j < this.inventoryHands.size(); j++)
        this.inventoryHands.set(j, new ItemStack(nbttaglist1.getCompoundTagAt(j))); 
    } 
    if (compound.hasKey("ArmorDropChances", 9)) {
      NBTTagList nbttaglist2 = compound.getTagList("ArmorDropChances", 5);
      for (int k = 0; k < nbttaglist2.tagCount(); k++)
        this.inventoryArmorDropChances[k] = nbttaglist2.getFloatAt(k); 
    } 
    if (compound.hasKey("HandDropChances", 9)) {
      NBTTagList nbttaglist3 = compound.getTagList("HandDropChances", 5);
      for (int l = 0; l < nbttaglist3.tagCount(); l++)
        this.inventoryHandsDropChances[l] = nbttaglist3.getFloatAt(l); 
    } 
    this.isLeashed = compound.getBoolean("Leashed");
    if (this.isLeashed && compound.hasKey("Leash", 10))
      this.leashNBTTag = compound.getCompoundTag("Leash"); 
    setLeftHanded(compound.getBoolean("LeftHanded"));
    if (compound.hasKey("DeathLootTable", 8)) {
      this.deathLootTable = new ResourceLocation(compound.getString("DeathLootTable"));
      this.deathLootTableSeed = compound.getLong("DeathLootTableSeed");
    } 
    setNoAI(compound.getBoolean("NoAI"));
  }
  
  @Nullable
  protected ResourceLocation getLootTable() {
    return null;
  }
  
  protected void dropLoot(boolean wasRecentlyHit, int lootingModifier, DamageSource source) {
    ResourceLocation resourcelocation = this.deathLootTable;
    if (resourcelocation == null)
      resourcelocation = getLootTable(); 
    if (resourcelocation != null) {
      LootTable loottable = this.world.getLootTableManager().getLootTableFromLocation(resourcelocation);
      this.deathLootTable = null;
      LootContext.Builder lootcontext$builder = (new LootContext.Builder((WorldServer)this.world)).withLootedEntity(this).withDamageSource(source);
      if (wasRecentlyHit && this.attackingPlayer != null)
        lootcontext$builder = lootcontext$builder.withPlayer(this.attackingPlayer).withLuck(this.attackingPlayer.getLuck()); 
      for (ItemStack itemstack : loottable.generateLootForPools((this.deathLootTableSeed == 0L) ? this.rand : new Random(this.deathLootTableSeed), lootcontext$builder.build()))
        entityDropItem(itemstack, 0.0F); 
      dropEquipment(wasRecentlyHit, lootingModifier);
    } else {
      super.dropLoot(wasRecentlyHit, lootingModifier, source);
    } 
  }
  
  public void func_191989_p(float p_191989_1_) {
    this.field_191988_bg = p_191989_1_;
  }
  
  public void setMoveForward(float amount) {
    this.moveForward = amount;
  }
  
  public void setMoveStrafing(float amount) {
    this.moveStrafing = amount;
  }
  
  public void setAIMoveSpeed(float speedIn) {
    super.setAIMoveSpeed(speedIn);
    func_191989_p(speedIn);
  }
  
  public void onLivingUpdate() {
    super.onLivingUpdate();
    this.world.theProfiler.startSection("looting");
    if (!this.world.isRemote && canPickUpLoot() && !this.dead && this.world.getGameRules().getBoolean("mobGriefing"))
      for (EntityItem entityitem : this.world.getEntitiesWithinAABB(EntityItem.class, getEntityBoundingBox().expand(1.0D, 0.0D, 1.0D))) {
        if (!entityitem.isDead && !entityitem.getEntityItem().func_190926_b() && !entityitem.cannotPickup())
          updateEquipmentIfNeeded(entityitem); 
      }  
    this.world.theProfiler.endSection();
  }
  
  protected void updateEquipmentIfNeeded(EntityItem itemEntity) {
    ItemStack itemstack = itemEntity.getEntityItem();
    EntityEquipmentSlot entityequipmentslot = getSlotForItemStack(itemstack);
    boolean flag = true;
    ItemStack itemstack1 = getItemStackFromSlot(entityequipmentslot);
    if (!itemstack1.func_190926_b())
      if (entityequipmentslot.getSlotType() == EntityEquipmentSlot.Type.HAND) {
        if (itemstack.getItem() instanceof ItemSword && !(itemstack1.getItem() instanceof ItemSword)) {
          flag = true;
        } else if (itemstack.getItem() instanceof ItemSword && itemstack1.getItem() instanceof ItemSword) {
          ItemSword itemsword = (ItemSword)itemstack.getItem();
          ItemSword itemsword1 = (ItemSword)itemstack1.getItem();
          if (itemsword.getDamageVsEntity() == itemsword1.getDamageVsEntity()) {
            flag = !(itemstack.getMetadata() <= itemstack1.getMetadata() && (!itemstack.hasTagCompound() || itemstack1.hasTagCompound()));
          } else {
            flag = (itemsword.getDamageVsEntity() > itemsword1.getDamageVsEntity());
          } 
        } else if (itemstack.getItem() instanceof net.minecraft.item.ItemBow && itemstack1.getItem() instanceof net.minecraft.item.ItemBow) {
          flag = (itemstack.hasTagCompound() && !itemstack1.hasTagCompound());
        } else {
          flag = false;
        } 
      } else if (itemstack.getItem() instanceof ItemArmor && !(itemstack1.getItem() instanceof ItemArmor)) {
        flag = true;
      } else if (itemstack.getItem() instanceof ItemArmor && itemstack1.getItem() instanceof ItemArmor && !EnchantmentHelper.func_190938_b(itemstack1)) {
        ItemArmor itemarmor = (ItemArmor)itemstack.getItem();
        ItemArmor itemarmor1 = (ItemArmor)itemstack1.getItem();
        if (itemarmor.damageReduceAmount == itemarmor1.damageReduceAmount) {
          flag = !(itemstack.getMetadata() <= itemstack1.getMetadata() && (!itemstack.hasTagCompound() || itemstack1.hasTagCompound()));
        } else {
          flag = (itemarmor.damageReduceAmount > itemarmor1.damageReduceAmount);
        } 
      } else {
        flag = false;
      }  
    if (flag && canEquipItem(itemstack)) {
      double d0;
      switch (entityequipmentslot.getSlotType()) {
        case HAND:
          d0 = this.inventoryHandsDropChances[entityequipmentslot.getIndex()];
          break;
        case null:
          d0 = this.inventoryArmorDropChances[entityequipmentslot.getIndex()];
          break;
        default:
          d0 = 0.0D;
          break;
      } 
      if (!itemstack1.func_190926_b() && (this.rand.nextFloat() - 0.1F) < d0)
        entityDropItem(itemstack1, 0.0F); 
      setItemStackToSlot(entityequipmentslot, itemstack);
      switch (entityequipmentslot.getSlotType()) {
        case HAND:
          this.inventoryHandsDropChances[entityequipmentslot.getIndex()] = 2.0F;
          break;
        case null:
          this.inventoryArmorDropChances[entityequipmentslot.getIndex()] = 2.0F;
          break;
      } 
      this.persistenceRequired = true;
      onItemPickup((Entity)itemEntity, itemstack.func_190916_E());
      itemEntity.setDead();
    } 
  }
  
  protected boolean canEquipItem(ItemStack stack) {
    return true;
  }
  
  protected boolean canDespawn() {
    return true;
  }
  
  protected void despawnEntity() {
    Object object = null;
    Object object1 = Reflector.getFieldValue(Reflector.Event_Result_DEFAULT);
    Object object2 = Reflector.getFieldValue(Reflector.Event_Result_DENY);
    if (this.persistenceRequired) {
      this.entityAge = 0;
    } else if ((this.entityAge & 0x1F) == 31 && (object = Reflector.call(Reflector.ForgeEventFactory_canEntityDespawn, new Object[] { this })) != object1) {
      if (object == object2) {
        this.entityAge = 0;
      } else {
        setDead();
      } 
    } else {
      EntityPlayer entityPlayer = this.world.getClosestPlayerToEntity(this, -1.0D);
      if (entityPlayer != null) {
        double d0 = ((Entity)entityPlayer).posX - this.posX;
        double d1 = ((Entity)entityPlayer).posY - this.posY;
        double d2 = ((Entity)entityPlayer).posZ - this.posZ;
        double d3 = d0 * d0 + d1 * d1 + d2 * d2;
        if (canDespawn() && d3 > 16384.0D)
          setDead(); 
        if (this.entityAge > 600 && this.rand.nextInt(800) == 0 && d3 > 1024.0D && canDespawn()) {
          setDead();
        } else if (d3 < 1024.0D) {
          this.entityAge = 0;
        } 
      } 
    } 
  }
  
  protected final void updateEntityActionState() {
    this.entityAge++;
    this.world.theProfiler.startSection("checkDespawn");
    despawnEntity();
    this.world.theProfiler.endSection();
    this.world.theProfiler.startSection("sensing");
    this.senses.clearSensingCache();
    this.world.theProfiler.endSection();
    this.world.theProfiler.startSection("targetSelector");
    this.targetTasks.onUpdateTasks();
    this.world.theProfiler.endSection();
    this.world.theProfiler.startSection("goalSelector");
    this.tasks.onUpdateTasks();
    this.world.theProfiler.endSection();
    this.world.theProfiler.startSection("navigation");
    this.navigator.onUpdateNavigation();
    this.world.theProfiler.endSection();
    this.world.theProfiler.startSection("mob tick");
    updateAITasks();
    this.world.theProfiler.endSection();
    if (isRiding() && getRidingEntity() instanceof EntityLiving) {
      EntityLiving entityliving = (EntityLiving)getRidingEntity();
      entityliving.getNavigator().setPath(getNavigator().getPath(), 1.5D);
      entityliving.getMoveHelper().read(getMoveHelper());
    } 
    this.world.theProfiler.startSection("controls");
    this.world.theProfiler.startSection("move");
    this.moveHelper.onUpdateMoveHelper();
    this.world.theProfiler.endStartSection("look");
    this.lookHelper.onUpdateLook();
    this.world.theProfiler.endStartSection("jump");
    this.jumpHelper.doJump();
    this.world.theProfiler.endSection();
    this.world.theProfiler.endSection();
  }
  
  protected void updateAITasks() {}
  
  public int getVerticalFaceSpeed() {
    return 40;
  }
  
  public int getHorizontalFaceSpeed() {
    return 10;
  }
  
  public void faceEntity(Entity entityIn, float maxYawIncrease, float maxPitchIncrease) {
    double d2, d0 = entityIn.posX - this.posX;
    double d1 = entityIn.posZ - this.posZ;
    if (entityIn instanceof EntityLivingBase) {
      EntityLivingBase entitylivingbase = (EntityLivingBase)entityIn;
      d2 = entitylivingbase.posY + entitylivingbase.getEyeHeight() - this.posY + getEyeHeight();
    } else {
      d2 = ((entityIn.getEntityBoundingBox()).minY + (entityIn.getEntityBoundingBox()).maxY) / 2.0D - this.posY + getEyeHeight();
    } 
    double d3 = MathHelper.sqrt(d0 * d0 + d1 * d1);
    float f = (float)(MathHelper.atan2(d1, d0) * 57.29577951308232D) - 90.0F;
    float f1 = (float)-(MathHelper.atan2(d2, d3) * 57.29577951308232D);
    this.rotationPitch = updateRotation(this.rotationPitch, f1, maxPitchIncrease);
    this.rotationYaw = updateRotation(this.rotationYaw, f, maxYawIncrease);
  }
  
  private float updateRotation(float angle, float targetAngle, float maxIncrease) {
    float f = MathHelper.wrapDegrees(targetAngle - angle);
    if (f > maxIncrease)
      f = maxIncrease; 
    if (f < -maxIncrease)
      f = -maxIncrease; 
    return angle + f;
  }
  
  public boolean getCanSpawnHere() {
    IBlockState iblockstate = this.world.getBlockState((new BlockPos(this)).down());
    return iblockstate.canEntitySpawn(this);
  }
  
  public boolean isNotColliding() {
    return (!this.world.containsAnyLiquid(getEntityBoundingBox()) && this.world.getCollisionBoxes(this, getEntityBoundingBox()).isEmpty() && this.world.checkNoEntityCollision(getEntityBoundingBox(), this));
  }
  
  public float getRenderSizeModifier() {
    return 1.0F;
  }
  
  public int getMaxSpawnedInChunk() {
    return 4;
  }
  
  public int getMaxFallHeight() {
    if (getAttackTarget() == null)
      return 3; 
    int i = (int)(getHealth() - getMaxHealth() * 0.33F);
    i -= (3 - this.world.getDifficulty().getDifficultyId()) * 4;
    if (i < 0)
      i = 0; 
    return i + 3;
  }
  
  public Iterable<ItemStack> getHeldEquipment() {
    return (Iterable<ItemStack>)this.inventoryHands;
  }
  
  public Iterable<ItemStack> getArmorInventoryList() {
    return (Iterable<ItemStack>)this.inventoryArmor;
  }
  
  public ItemStack getItemStackFromSlot(EntityEquipmentSlot slotIn) {
    switch (slotIn.getSlotType()) {
      case HAND:
        return (ItemStack)this.inventoryHands.get(slotIn.getIndex());
      case null:
        return (ItemStack)this.inventoryArmor.get(slotIn.getIndex());
    } 
    return ItemStack.field_190927_a;
  }
  
  public void setItemStackToSlot(EntityEquipmentSlot slotIn, ItemStack stack) {
    switch (slotIn.getSlotType()) {
      case HAND:
        this.inventoryHands.set(slotIn.getIndex(), stack);
        break;
      case null:
        this.inventoryArmor.set(slotIn.getIndex(), stack);
        break;
    } 
  }
  
  protected void dropEquipment(boolean wasRecentlyHit, int lootingModifier) {
    byte b;
    int i;
    EntityEquipmentSlot[] arrayOfEntityEquipmentSlot;
    for (i = (arrayOfEntityEquipmentSlot = EntityEquipmentSlot.values()).length, b = 0; b < i; ) {
      double d0;
      EntityEquipmentSlot entityequipmentslot = arrayOfEntityEquipmentSlot[b];
      ItemStack itemstack = getItemStackFromSlot(entityequipmentslot);
      switch (entityequipmentslot.getSlotType()) {
        case HAND:
          d0 = this.inventoryHandsDropChances[entityequipmentslot.getIndex()];
          break;
        case null:
          d0 = this.inventoryArmorDropChances[entityequipmentslot.getIndex()];
          break;
        default:
          d0 = 0.0D;
          break;
      } 
      boolean flag = (d0 > 1.0D);
      if (!itemstack.func_190926_b() && !EnchantmentHelper.func_190939_c(itemstack) && (wasRecentlyHit || flag) && (this.rand.nextFloat() - lootingModifier * 0.01F) < d0) {
        if (!flag && itemstack.isItemStackDamageable())
          itemstack.setItemDamage(itemstack.getMaxDamage() - this.rand.nextInt(1 + this.rand.nextInt(Math.max(itemstack.getMaxDamage() - 3, 1)))); 
        entityDropItem(itemstack, 0.0F);
      } 
      b++;
    } 
  }
  
  protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
    if (this.rand.nextFloat() < 0.15F * difficulty.getClampedAdditionalDifficulty()) {
      int i = this.rand.nextInt(2);
      float f = (this.world.getDifficulty() == EnumDifficulty.HARD) ? 0.1F : 0.25F;
      if (this.rand.nextFloat() < 0.095F)
        i++; 
      if (this.rand.nextFloat() < 0.095F)
        i++; 
      if (this.rand.nextFloat() < 0.095F)
        i++; 
      boolean flag = true;
      byte b;
      int j;
      EntityEquipmentSlot[] arrayOfEntityEquipmentSlot;
      for (j = (arrayOfEntityEquipmentSlot = EntityEquipmentSlot.values()).length, b = 0; b < j; ) {
        EntityEquipmentSlot entityequipmentslot = arrayOfEntityEquipmentSlot[b];
        if (entityequipmentslot.getSlotType() == EntityEquipmentSlot.Type.ARMOR) {
          ItemStack itemstack = getItemStackFromSlot(entityequipmentslot);
          if (!flag && this.rand.nextFloat() < f)
            break; 
          flag = false;
          if (itemstack.func_190926_b()) {
            Item item = getArmorByChance(entityequipmentslot, i);
            if (item != null)
              setItemStackToSlot(entityequipmentslot, new ItemStack(item)); 
          } 
        } 
        b++;
      } 
    } 
  }
  
  public static EntityEquipmentSlot getSlotForItemStack(ItemStack stack) {
    if (stack.getItem() != Item.getItemFromBlock(Blocks.PUMPKIN) && stack.getItem() != Items.SKULL) {
      if (stack.getItem() instanceof ItemArmor)
        return ((ItemArmor)stack.getItem()).armorType; 
      if (stack.getItem() == Items.ELYTRA)
        return EntityEquipmentSlot.CHEST; 
      boolean flag = (stack.getItem() == Items.SHIELD);
      if (Reflector.ForgeItem_isShield.exists())
        flag = Reflector.callBoolean(stack.getItem(), Reflector.ForgeItem_isShield, new Object[] { stack, null }); 
      return flag ? EntityEquipmentSlot.OFFHAND : EntityEquipmentSlot.MAINHAND;
    } 
    return EntityEquipmentSlot.HEAD;
  }
  
  @Nullable
  public static Item getArmorByChance(EntityEquipmentSlot slotIn, int chance) {
    switch (slotIn) {
      case HEAD:
        if (chance == 0)
          return (Item)Items.LEATHER_HELMET; 
        if (chance == 1)
          return (Item)Items.GOLDEN_HELMET; 
        if (chance == 2)
          return (Item)Items.CHAINMAIL_HELMET; 
        if (chance == 3)
          return (Item)Items.IRON_HELMET; 
        if (chance == 4)
          return (Item)Items.DIAMOND_HELMET; 
      case null:
        if (chance == 0)
          return (Item)Items.LEATHER_CHESTPLATE; 
        if (chance == 1)
          return (Item)Items.GOLDEN_CHESTPLATE; 
        if (chance == 2)
          return (Item)Items.CHAINMAIL_CHESTPLATE; 
        if (chance == 3)
          return (Item)Items.IRON_CHESTPLATE; 
        if (chance == 4)
          return (Item)Items.DIAMOND_CHESTPLATE; 
      case LEGS:
        if (chance == 0)
          return (Item)Items.LEATHER_LEGGINGS; 
        if (chance == 1)
          return (Item)Items.GOLDEN_LEGGINGS; 
        if (chance == 2)
          return (Item)Items.CHAINMAIL_LEGGINGS; 
        if (chance == 3)
          return (Item)Items.IRON_LEGGINGS; 
        if (chance == 4)
          return (Item)Items.DIAMOND_LEGGINGS; 
      case FEET:
        if (chance == 0)
          return (Item)Items.LEATHER_BOOTS; 
        if (chance == 1)
          return (Item)Items.GOLDEN_BOOTS; 
        if (chance == 2)
          return (Item)Items.CHAINMAIL_BOOTS; 
        if (chance == 3)
          return (Item)Items.IRON_BOOTS; 
        if (chance == 4)
          return (Item)Items.DIAMOND_BOOTS; 
        break;
    } 
    return null;
  }
  
  protected void setEnchantmentBasedOnDifficulty(DifficultyInstance difficulty) {
    float f = difficulty.getClampedAdditionalDifficulty();
    if (!getHeldItemMainhand().func_190926_b() && this.rand.nextFloat() < 0.25F * f)
      setItemStackToSlot(EntityEquipmentSlot.MAINHAND, EnchantmentHelper.addRandomEnchantment(this.rand, getHeldItemMainhand(), (int)(5.0F + f * this.rand.nextInt(18)), false)); 
    byte b;
    int i;
    EntityEquipmentSlot[] arrayOfEntityEquipmentSlot;
    for (i = (arrayOfEntityEquipmentSlot = EntityEquipmentSlot.values()).length, b = 0; b < i; ) {
      EntityEquipmentSlot entityequipmentslot = arrayOfEntityEquipmentSlot[b];
      if (entityequipmentslot.getSlotType() == EntityEquipmentSlot.Type.ARMOR) {
        ItemStack itemstack = getItemStackFromSlot(entityequipmentslot);
        if (!itemstack.func_190926_b() && this.rand.nextFloat() < 0.5F * f)
          setItemStackToSlot(entityequipmentslot, EnchantmentHelper.addRandomEnchantment(this.rand, itemstack, (int)(5.0F + f * this.rand.nextInt(18)), false)); 
      } 
      b++;
    } 
  }
  
  @Nullable
  public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
    getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).applyModifier(new AttributeModifier("Random spawn bonus", this.rand.nextGaussian() * 0.05D, 1));
    if (this.rand.nextFloat() < 0.05F) {
      setLeftHanded(true);
    } else {
      setLeftHanded(false);
    } 
    return livingdata;
  }
  
  public boolean canBeSteered() {
    return false;
  }
  
  public void enablePersistence() {
    this.persistenceRequired = true;
  }
  
  public void setDropChance(EntityEquipmentSlot slotIn, float chance) {
    switch (slotIn.getSlotType()) {
      case HAND:
        this.inventoryHandsDropChances[slotIn.getIndex()] = chance;
        break;
      case null:
        this.inventoryArmorDropChances[slotIn.getIndex()] = chance;
        break;
    } 
  }
  
  public boolean canPickUpLoot() {
    return this.canPickUpLoot;
  }
  
  public void setCanPickUpLoot(boolean canPickup) {
    this.canPickUpLoot = canPickup;
  }
  
  public boolean isNoDespawnRequired() {
    return this.persistenceRequired;
  }
  
  public final boolean processInitialInteract(EntityPlayer player, EnumHand stack) {
    if (getLeashed() && getLeashedToEntity() == player) {
      clearLeashed(true, !player.capabilities.isCreativeMode);
      return true;
    } 
    ItemStack itemstack = player.getHeldItem(stack);
    if (itemstack.getItem() == Items.LEAD && canBeLeashedTo(player)) {
      setLeashedToEntity((Entity)player, true);
      itemstack.func_190918_g(1);
      return true;
    } 
    return processInteract(player, stack) ? true : super.processInitialInteract(player, stack);
  }
  
  protected boolean processInteract(EntityPlayer player, EnumHand hand) {
    return false;
  }
  
  protected void updateLeashedState() {
    if (this.leashNBTTag != null)
      recreateLeash(); 
    if (this.isLeashed) {
      if (!isEntityAlive())
        clearLeashed(true, true); 
      if (this.leashedToEntity == null || this.leashedToEntity.isDead)
        clearLeashed(true, true); 
    } 
  }
  
  public void clearLeashed(boolean sendPacket, boolean dropLead) {
    if (this.isLeashed) {
      this.isLeashed = false;
      this.leashedToEntity = null;
      if (!this.world.isRemote && dropLead)
        dropItem(Items.LEAD, 1); 
      if (!this.world.isRemote && sendPacket && this.world instanceof WorldServer)
        ((WorldServer)this.world).getEntityTracker().sendToAllTrackingEntity(this, (Packet<?>)new SPacketEntityAttach(this, null)); 
    } 
  }
  
  public boolean canBeLeashedTo(EntityPlayer player) {
    return (!getLeashed() && !(this instanceof net.minecraft.entity.monster.IMob));
  }
  
  public boolean getLeashed() {
    return this.isLeashed;
  }
  
  public Entity getLeashedToEntity() {
    return this.leashedToEntity;
  }
  
  public void setLeashedToEntity(Entity entityIn, boolean sendAttachNotification) {
    this.isLeashed = true;
    this.leashedToEntity = entityIn;
    if (!this.world.isRemote && sendAttachNotification && this.world instanceof WorldServer)
      ((WorldServer)this.world).getEntityTracker().sendToAllTrackingEntity(this, (Packet<?>)new SPacketEntityAttach(this, this.leashedToEntity)); 
    if (isRiding())
      dismountRidingEntity(); 
  }
  
  public boolean startRiding(Entity entityIn, boolean force) {
    boolean flag = super.startRiding(entityIn, force);
    if (flag && getLeashed())
      clearLeashed(true, true); 
    return flag;
  }
  
  private void recreateLeash() {
    if (this.isLeashed && this.leashNBTTag != null)
      if (this.leashNBTTag.hasUniqueId("UUID")) {
        UUID uuid = this.leashNBTTag.getUniqueId("UUID");
        for (EntityLivingBase entitylivingbase : this.world.getEntitiesWithinAABB(EntityLivingBase.class, getEntityBoundingBox().expandXyz(10.0D))) {
          if (entitylivingbase.getUniqueID().equals(uuid)) {
            setLeashedToEntity(entitylivingbase, true);
            break;
          } 
        } 
      } else if (this.leashNBTTag.hasKey("X", 99) && this.leashNBTTag.hasKey("Y", 99) && this.leashNBTTag.hasKey("Z", 99)) {
        BlockPos blockpos = new BlockPos(this.leashNBTTag.getInteger("X"), this.leashNBTTag.getInteger("Y"), this.leashNBTTag.getInteger("Z"));
        EntityLeashKnot entityleashknot = EntityLeashKnot.getKnotForPosition(this.world, blockpos);
        if (entityleashknot == null)
          entityleashknot = EntityLeashKnot.createKnot(this.world, blockpos); 
        setLeashedToEntity(entityleashknot, true);
      } else {
        clearLeashed(false, true);
      }  
    this.leashNBTTag = null;
  }
  
  public boolean replaceItemInInventory(int inventorySlot, ItemStack itemStackIn) {
    EntityEquipmentSlot entityequipmentslot;
    if (inventorySlot == 98) {
      entityequipmentslot = EntityEquipmentSlot.MAINHAND;
    } else if (inventorySlot == 99) {
      entityequipmentslot = EntityEquipmentSlot.OFFHAND;
    } else if (inventorySlot == 100 + EntityEquipmentSlot.HEAD.getIndex()) {
      entityequipmentslot = EntityEquipmentSlot.HEAD;
    } else if (inventorySlot == 100 + EntityEquipmentSlot.CHEST.getIndex()) {
      entityequipmentslot = EntityEquipmentSlot.CHEST;
    } else if (inventorySlot == 100 + EntityEquipmentSlot.LEGS.getIndex()) {
      entityequipmentslot = EntityEquipmentSlot.LEGS;
    } else {
      if (inventorySlot != 100 + EntityEquipmentSlot.FEET.getIndex())
        return false; 
      entityequipmentslot = EntityEquipmentSlot.FEET;
    } 
    if (!itemStackIn.func_190926_b() && !isItemStackInSlot(entityequipmentslot, itemStackIn) && entityequipmentslot != EntityEquipmentSlot.HEAD)
      return false; 
    setItemStackToSlot(entityequipmentslot, itemStackIn);
    return true;
  }
  
  public boolean canPassengerSteer() {
    return (canBeSteered() && super.canPassengerSteer());
  }
  
  public static boolean isItemStackInSlot(EntityEquipmentSlot slotIn, ItemStack stack) {
    EntityEquipmentSlot entityequipmentslot = getSlotForItemStack(stack);
    return !(entityequipmentslot != slotIn && (entityequipmentslot != EntityEquipmentSlot.MAINHAND || slotIn != EntityEquipmentSlot.OFFHAND) && (entityequipmentslot != EntityEquipmentSlot.OFFHAND || slotIn != EntityEquipmentSlot.MAINHAND));
  }
  
  public boolean isServerWorld() {
    return (super.isServerWorld() && !isAIDisabled());
  }
  
  public void setNoAI(boolean disable) {
    byte b0 = ((Byte)this.dataManager.get(AI_FLAGS)).byteValue();
    this.dataManager.set(AI_FLAGS, Byte.valueOf(disable ? (byte)(b0 | 0x1) : (byte)(b0 & 0xFFFFFFFE)));
  }
  
  public void setLeftHanded(boolean disable) {
    byte b0 = ((Byte)this.dataManager.get(AI_FLAGS)).byteValue();
    this.dataManager.set(AI_FLAGS, Byte.valueOf(disable ? (byte)(b0 | 0x2) : (byte)(b0 & 0xFFFFFFFD)));
  }
  
  public boolean isAIDisabled() {
    return ((((Byte)this.dataManager.get(AI_FLAGS)).byteValue() & 0x1) != 0);
  }
  
  public boolean isLeftHanded() {
    return ((((Byte)this.dataManager.get(AI_FLAGS)).byteValue() & 0x2) != 0);
  }
  
  public EnumHandSide getPrimaryHand() {
    return isLeftHanded() ? EnumHandSide.LEFT : EnumHandSide.RIGHT;
  }
  
  private boolean canSkipUpdate() {
    if (isChild())
      return false; 
    if (this.hurtTime > 0)
      return false; 
    if (this.ticksExisted < 20)
      return false; 
    World world = getEntityWorld();
    if (world == null)
      return false; 
    if (world.playerEntities.size() != 1)
      return false; 
    Entity entity = world.playerEntities.get(0);
    double d0 = Math.max(Math.abs(this.posX - entity.posX) - 16.0D, 0.0D);
    double d1 = Math.max(Math.abs(this.posZ - entity.posZ) - 16.0D, 0.0D);
    double d2 = d0 * d0 + d1 * d1;
    return !isInRangeToRenderDist(d2);
  }
  
  private void onUpdateMinimal() {
    this.entityAge++;
    if (this instanceof net.minecraft.entity.monster.EntityMob) {
      float f = getBrightness();
      if (f > 0.5F)
        this.entityAge += 2; 
    } 
    despawnEntity();
  }
  
  public Team getTeam() {
    UUID uuid = getUniqueID();
    if (this.teamUuid != uuid) {
      this.teamUuid = uuid;
      this.teamUuidString = uuid.toString();
    } 
    return (Team)this.world.getScoreboard().getPlayersTeam(this.teamUuidString);
  }
  
  public enum SpawnPlacementType {
    ON_GROUND, IN_AIR, IN_WATER;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\entity\EntityLiving.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
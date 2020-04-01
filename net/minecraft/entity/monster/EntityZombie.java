package net.minecraft.entity.monster;

import java.util.Calendar;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIBreakDoor;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveThroughVillage;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityAIZombieAttack;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityZombie extends EntityMob {
  protected static final IAttribute SPAWN_REINFORCEMENTS_CHANCE = (IAttribute)(new RangedAttribute(null, "zombie.spawnReinforcements", 0.0D, 0.0D, 1.0D)).setDescription("Spawn Reinforcements Chance");
  
  private static final UUID BABY_SPEED_BOOST_ID = UUID.fromString("B9766B59-9566-4402-BC1F-2EE2A276D836");
  
  private static final AttributeModifier BABY_SPEED_BOOST = new AttributeModifier(BABY_SPEED_BOOST_ID, "Baby speed boost", 0.5D, 1);
  
  private static final DataParameter<Boolean> IS_CHILD = EntityDataManager.createKey(EntityZombie.class, DataSerializers.BOOLEAN);
  
  private static final DataParameter<Integer> VILLAGER_TYPE = EntityDataManager.createKey(EntityZombie.class, DataSerializers.VARINT);
  
  private static final DataParameter<Boolean> ARMS_RAISED = EntityDataManager.createKey(EntityZombie.class, DataSerializers.BOOLEAN);
  
  private final EntityAIBreakDoor breakDoor = new EntityAIBreakDoor((EntityLiving)this);
  
  private boolean isBreakDoorsTaskSet;
  
  private float zombieWidth = -1.0F;
  
  private float zombieHeight;
  
  public EntityZombie(World worldIn) {
    super(worldIn);
    setSize(0.6F, 1.95F);
  }
  
  protected void initEntityAI() {
    this.tasks.addTask(0, (EntityAIBase)new EntityAISwimming((EntityLiving)this));
    this.tasks.addTask(2, (EntityAIBase)new EntityAIZombieAttack(this, 1.0D, false));
    this.tasks.addTask(5, (EntityAIBase)new EntityAIMoveTowardsRestriction(this, 1.0D));
    this.tasks.addTask(7, (EntityAIBase)new EntityAIWanderAvoidWater(this, 1.0D));
    this.tasks.addTask(8, (EntityAIBase)new EntityAIWatchClosest((EntityLiving)this, EntityPlayer.class, 8.0F));
    this.tasks.addTask(8, (EntityAIBase)new EntityAILookIdle((EntityLiving)this));
    applyEntityAI();
  }
  
  protected void applyEntityAI() {
    this.tasks.addTask(6, (EntityAIBase)new EntityAIMoveThroughVillage(this, 1.0D, false));
    this.targetTasks.addTask(1, (EntityAIBase)new EntityAIHurtByTarget(this, true, new Class[] { EntityPigZombie.class }));
    this.targetTasks.addTask(2, (EntityAIBase)new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
    this.targetTasks.addTask(3, (EntityAIBase)new EntityAINearestAttackableTarget(this, EntityVillager.class, false));
    this.targetTasks.addTask(3, (EntityAIBase)new EntityAINearestAttackableTarget(this, EntityIronGolem.class, true));
  }
  
  protected void applyEntityAttributes() {
    super.applyEntityAttributes();
    getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(35.0D);
    getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.23000000417232513D);
    getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(3.0D);
    getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(2.0D);
    getAttributeMap().registerAttribute(SPAWN_REINFORCEMENTS_CHANCE).setBaseValue(this.rand.nextDouble() * 0.10000000149011612D);
  }
  
  protected void entityInit() {
    super.entityInit();
    getDataManager().register(IS_CHILD, Boolean.valueOf(false));
    getDataManager().register(VILLAGER_TYPE, Integer.valueOf(0));
    getDataManager().register(ARMS_RAISED, Boolean.valueOf(false));
  }
  
  public void setArmsRaised(boolean armsRaised) {
    getDataManager().set(ARMS_RAISED, Boolean.valueOf(armsRaised));
  }
  
  public boolean isArmsRaised() {
    return ((Boolean)getDataManager().get(ARMS_RAISED)).booleanValue();
  }
  
  public boolean isBreakDoorsTaskSet() {
    return this.isBreakDoorsTaskSet;
  }
  
  public void setBreakDoorsAItask(boolean enabled) {
    if (this.isBreakDoorsTaskSet != enabled) {
      this.isBreakDoorsTaskSet = enabled;
      ((PathNavigateGround)getNavigator()).setBreakDoors(enabled);
      if (enabled) {
        this.tasks.addTask(1, (EntityAIBase)this.breakDoor);
      } else {
        this.tasks.removeTask((EntityAIBase)this.breakDoor);
      } 
    } 
  }
  
  public boolean isChild() {
    return ((Boolean)getDataManager().get(IS_CHILD)).booleanValue();
  }
  
  protected int getExperiencePoints(EntityPlayer player) {
    if (isChild())
      this.experienceValue = (int)(this.experienceValue * 2.5F); 
    return super.getExperiencePoints(player);
  }
  
  public void setChild(boolean childZombie) {
    getDataManager().set(IS_CHILD, Boolean.valueOf(childZombie));
    if (this.world != null && !this.world.isRemote) {
      IAttributeInstance iattributeinstance = getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
      iattributeinstance.removeModifier(BABY_SPEED_BOOST);
      if (childZombie)
        iattributeinstance.applyModifier(BABY_SPEED_BOOST); 
    } 
    setChildSize(childZombie);
  }
  
  public void notifyDataManagerChange(DataParameter<?> key) {
    if (IS_CHILD.equals(key))
      setChildSize(isChild()); 
    super.notifyDataManagerChange(key);
  }
  
  public void onLivingUpdate() {
    if (this.world.isDaytime() && !this.world.isRemote && !isChild() && func_190730_o()) {
      float f = getBrightness();
      if (f > 0.5F && this.rand.nextFloat() * 30.0F < (f - 0.4F) * 2.0F && this.world.canSeeSky(new BlockPos(this.posX, this.posY + getEyeHeight(), this.posZ))) {
        boolean flag = true;
        ItemStack itemstack = getItemStackFromSlot(EntityEquipmentSlot.HEAD);
        if (!itemstack.func_190926_b()) {
          if (itemstack.isItemStackDamageable()) {
            itemstack.setItemDamage(itemstack.getItemDamage() + this.rand.nextInt(2));
            if (itemstack.getItemDamage() >= itemstack.getMaxDamage()) {
              renderBrokenItemStack(itemstack);
              setItemStackToSlot(EntityEquipmentSlot.HEAD, ItemStack.field_190927_a);
            } 
          } 
          flag = false;
        } 
        if (flag)
          setFire(8); 
      } 
    } 
    super.onLivingUpdate();
  }
  
  protected boolean func_190730_o() {
    return true;
  }
  
  public boolean attackEntityFrom(DamageSource source, float amount) {
    if (super.attackEntityFrom(source, amount)) {
      EntityLivingBase entitylivingbase = getAttackTarget();
      if (entitylivingbase == null && source.getEntity() instanceof EntityLivingBase)
        entitylivingbase = (EntityLivingBase)source.getEntity(); 
      if (entitylivingbase != null && this.world.getDifficulty() == EnumDifficulty.HARD && this.rand.nextFloat() < getEntityAttribute(SPAWN_REINFORCEMENTS_CHANCE).getAttributeValue() && this.world.getGameRules().getBoolean("doMobSpawning")) {
        int i = MathHelper.floor(this.posX);
        int j = MathHelper.floor(this.posY);
        int k = MathHelper.floor(this.posZ);
        EntityZombie entityzombie = new EntityZombie(this.world);
        for (int l = 0; l < 50; l++) {
          int i1 = i + MathHelper.getInt(this.rand, 7, 40) * MathHelper.getInt(this.rand, -1, 1);
          int j1 = j + MathHelper.getInt(this.rand, 7, 40) * MathHelper.getInt(this.rand, -1, 1);
          int k1 = k + MathHelper.getInt(this.rand, 7, 40) * MathHelper.getInt(this.rand, -1, 1);
          if (this.world.getBlockState(new BlockPos(i1, j1 - 1, k1)).isFullyOpaque() && this.world.getLightFromNeighbors(new BlockPos(i1, j1, k1)) < 10) {
            entityzombie.setPosition(i1, j1, k1);
            if (!this.world.isAnyPlayerWithinRangeAt(i1, j1, k1, 7.0D) && this.world.checkNoEntityCollision(entityzombie.getEntityBoundingBox(), (Entity)entityzombie) && this.world.getCollisionBoxes((Entity)entityzombie, entityzombie.getEntityBoundingBox()).isEmpty() && !this.world.containsAnyLiquid(entityzombie.getEntityBoundingBox())) {
              this.world.spawnEntityInWorld((Entity)entityzombie);
              entityzombie.setAttackTarget(entitylivingbase);
              entityzombie.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos((Entity)entityzombie)), (IEntityLivingData)null);
              getEntityAttribute(SPAWN_REINFORCEMENTS_CHANCE).applyModifier(new AttributeModifier("Zombie reinforcement caller charge", -0.05000000074505806D, 0));
              entityzombie.getEntityAttribute(SPAWN_REINFORCEMENTS_CHANCE).applyModifier(new AttributeModifier("Zombie reinforcement callee charge", -0.05000000074505806D, 0));
              break;
            } 
          } 
        } 
      } 
      return true;
    } 
    return false;
  }
  
  public boolean attackEntityAsMob(Entity entityIn) {
    boolean flag = super.attackEntityAsMob(entityIn);
    if (flag) {
      float f = this.world.getDifficultyForLocation(new BlockPos((Entity)this)).getAdditionalDifficulty();
      if (getHeldItemMainhand().func_190926_b() && isBurning() && this.rand.nextFloat() < f * 0.3F)
        entityIn.setFire(2 * (int)f); 
    } 
    return flag;
  }
  
  protected SoundEvent getAmbientSound() {
    return SoundEvents.ENTITY_ZOMBIE_AMBIENT;
  }
  
  protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
    return SoundEvents.ENTITY_ZOMBIE_HURT;
  }
  
  protected SoundEvent getDeathSound() {
    return SoundEvents.ENTITY_ZOMBIE_DEATH;
  }
  
  protected SoundEvent func_190731_di() {
    return SoundEvents.ENTITY_ZOMBIE_STEP;
  }
  
  protected void playStepSound(BlockPos pos, Block blockIn) {
    playSound(func_190731_di(), 0.15F, 1.0F);
  }
  
  public EnumCreatureAttribute getCreatureAttribute() {
    return EnumCreatureAttribute.UNDEAD;
  }
  
  @Nullable
  protected ResourceLocation getLootTable() {
    return LootTableList.ENTITIES_ZOMBIE;
  }
  
  protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
    super.setEquipmentBasedOnDifficulty(difficulty);
    if (this.rand.nextFloat() < ((this.world.getDifficulty() == EnumDifficulty.HARD) ? 0.05F : 0.01F)) {
      int i = this.rand.nextInt(3);
      if (i == 0) {
        setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
      } else {
        setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SHOVEL));
      } 
    } 
  }
  
  public static void registerFixesZombie(DataFixer fixer) {
    EntityLiving.registerFixesMob(fixer, EntityZombie.class);
  }
  
  public void writeEntityToNBT(NBTTagCompound compound) {
    super.writeEntityToNBT(compound);
    if (isChild())
      compound.setBoolean("IsBaby", true); 
    compound.setBoolean("CanBreakDoors", isBreakDoorsTaskSet());
  }
  
  public void readEntityFromNBT(NBTTagCompound compound) {
    super.readEntityFromNBT(compound);
    if (compound.getBoolean("IsBaby"))
      setChild(true); 
    setBreakDoorsAItask(compound.getBoolean("CanBreakDoors"));
  }
  
  public void onKillEntity(EntityLivingBase entityLivingIn) {
    super.onKillEntity(entityLivingIn);
    if ((this.world.getDifficulty() == EnumDifficulty.NORMAL || this.world.getDifficulty() == EnumDifficulty.HARD) && entityLivingIn instanceof EntityVillager) {
      if (this.world.getDifficulty() != EnumDifficulty.HARD && this.rand.nextBoolean())
        return; 
      EntityVillager entityvillager = (EntityVillager)entityLivingIn;
      EntityZombieVillager entityzombievillager = new EntityZombieVillager(this.world);
      entityzombievillager.copyLocationAndAnglesFrom((Entity)entityvillager);
      this.world.removeEntity((Entity)entityvillager);
      entityzombievillager.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos((Entity)entityzombievillager)), new GroupData(false, null));
      entityzombievillager.func_190733_a(entityvillager.getProfession());
      entityzombievillager.setChild(entityvillager.isChild());
      entityzombievillager.setNoAI(entityvillager.isAIDisabled());
      if (entityvillager.hasCustomName()) {
        entityzombievillager.setCustomNameTag(entityvillager.getCustomNameTag());
        entityzombievillager.setAlwaysRenderNameTag(entityvillager.getAlwaysRenderNameTag());
      } 
      this.world.spawnEntityInWorld((Entity)entityzombievillager);
      this.world.playEvent(null, 1026, new BlockPos((Entity)this), 0);
    } 
  }
  
  public float getEyeHeight() {
    float f = 1.74F;
    if (isChild())
      f = (float)(f - 0.81D); 
    return f;
  }
  
  protected boolean canEquipItem(ItemStack stack) {
    return (stack.getItem() == Items.EGG && isChild() && isRiding()) ? false : super.canEquipItem(stack);
  }
  
  @Nullable
  public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
    livingdata = super.onInitialSpawn(difficulty, livingdata);
    float f = difficulty.getClampedAdditionalDifficulty();
    setCanPickUpLoot((this.rand.nextFloat() < 0.55F * f));
    if (livingdata == null)
      livingdata = new GroupData((this.world.rand.nextFloat() < 0.05F), null); 
    if (livingdata instanceof GroupData) {
      GroupData entityzombie$groupdata = (GroupData)livingdata;
      if (entityzombie$groupdata.isChild) {
        setChild(true);
        if (this.world.rand.nextFloat() < 0.05D) {
          List<EntityChicken> list = this.world.getEntitiesWithinAABB(EntityChicken.class, getEntityBoundingBox().expand(5.0D, 3.0D, 5.0D), EntitySelectors.IS_STANDALONE);
          if (!list.isEmpty()) {
            EntityChicken entitychicken = list.get(0);
            entitychicken.setChickenJockey(true);
            startRiding((Entity)entitychicken);
          } 
        } else if (this.world.rand.nextFloat() < 0.05D) {
          EntityChicken entitychicken1 = new EntityChicken(this.world);
          entitychicken1.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
          entitychicken1.onInitialSpawn(difficulty, null);
          entitychicken1.setChickenJockey(true);
          this.world.spawnEntityInWorld((Entity)entitychicken1);
          startRiding((Entity)entitychicken1);
        } 
      } 
    } 
    setBreakDoorsAItask((this.rand.nextFloat() < f * 0.1F));
    setEquipmentBasedOnDifficulty(difficulty);
    setEnchantmentBasedOnDifficulty(difficulty);
    if (getItemStackFromSlot(EntityEquipmentSlot.HEAD).func_190926_b()) {
      Calendar calendar = this.world.getCurrentDate();
      if (calendar.get(2) + 1 == 10 && calendar.get(5) == 31 && this.rand.nextFloat() < 0.25F) {
        setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack((this.rand.nextFloat() < 0.1F) ? Blocks.LIT_PUMPKIN : Blocks.PUMPKIN));
        this.inventoryArmorDropChances[EntityEquipmentSlot.HEAD.getIndex()] = 0.0F;
      } 
    } 
    getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).applyModifier(new AttributeModifier("Random spawn bonus", this.rand.nextDouble() * 0.05000000074505806D, 0));
    double d0 = this.rand.nextDouble() * 1.5D * f;
    if (d0 > 1.0D)
      getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).applyModifier(new AttributeModifier("Random zombie-spawn bonus", d0, 2)); 
    if (this.rand.nextFloat() < f * 0.05F) {
      getEntityAttribute(SPAWN_REINFORCEMENTS_CHANCE).applyModifier(new AttributeModifier("Leader zombie bonus", this.rand.nextDouble() * 0.25D + 0.5D, 0));
      getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).applyModifier(new AttributeModifier("Leader zombie bonus", this.rand.nextDouble() * 3.0D + 1.0D, 2));
      setBreakDoorsAItask(true);
    } 
    return livingdata;
  }
  
  public void setChildSize(boolean isChild) {
    multiplySize(isChild ? 0.5F : 1.0F);
  }
  
  protected final void setSize(float width, float height) {
    boolean flag = (this.zombieWidth > 0.0F && this.zombieHeight > 0.0F);
    this.zombieWidth = width;
    this.zombieHeight = height;
    if (!flag)
      multiplySize(1.0F); 
  }
  
  protected final void multiplySize(float size) {
    super.setSize(this.zombieWidth * size, this.zombieHeight * size);
  }
  
  public double getYOffset() {
    return isChild() ? 0.0D : -0.45D;
  }
  
  public void onDeath(DamageSource cause) {
    super.onDeath(cause);
    if (cause.getEntity() instanceof EntityCreeper) {
      EntityCreeper entitycreeper = (EntityCreeper)cause.getEntity();
      if (entitycreeper.getPowered() && entitycreeper.isAIEnabled()) {
        entitycreeper.incrementDroppedSkulls();
        ItemStack itemstack = func_190732_dj();
        if (!itemstack.func_190926_b())
          entityDropItem(itemstack, 0.0F); 
      } 
    } 
  }
  
  protected ItemStack func_190732_dj() {
    return new ItemStack(Items.SKULL, 1, 2);
  }
  
  class GroupData implements IEntityLivingData {
    public boolean isChild;
    
    private GroupData(boolean p_i47328_2_) {
      this.isChild = p_i47328_2_;
    }
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\entity\monster\EntityZombie.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
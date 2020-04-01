package net.minecraft.entity.passive;

import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIFollowOwner;
import net.minecraft.entity.ai.EntityAILeapAtTarget;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAIOcelotAttack;
import net.minecraft.entity.ai.EntityAIOcelotSit;
import net.minecraft.entity.ai.EntityAISit;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITargetNonTamed;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityOcelot extends EntityTameable {
  private static final DataParameter<Integer> OCELOT_VARIANT = EntityDataManager.createKey(EntityOcelot.class, DataSerializers.VARINT);
  
  private EntityAIAvoidEntity<EntityPlayer> avoidEntity;
  
  private EntityAITempt aiTempt;
  
  public EntityOcelot(World worldIn) {
    super(worldIn);
    setSize(0.6F, 0.7F);
  }
  
  protected void initEntityAI() {
    this.aiSit = new EntityAISit(this);
    this.aiTempt = new EntityAITempt((EntityCreature)this, 0.6D, Items.FISH, true);
    this.tasks.addTask(1, (EntityAIBase)new EntityAISwimming((EntityLiving)this));
    this.tasks.addTask(2, (EntityAIBase)this.aiSit);
    this.tasks.addTask(3, (EntityAIBase)this.aiTempt);
    this.tasks.addTask(5, (EntityAIBase)new EntityAIFollowOwner(this, 1.0D, 10.0F, 5.0F));
    this.tasks.addTask(6, (EntityAIBase)new EntityAIOcelotSit(this, 0.8D));
    this.tasks.addTask(7, (EntityAIBase)new EntityAILeapAtTarget((EntityLiving)this, 0.3F));
    this.tasks.addTask(8, (EntityAIBase)new EntityAIOcelotAttack((EntityLiving)this));
    this.tasks.addTask(9, (EntityAIBase)new EntityAIMate(this, 0.8D));
    this.tasks.addTask(10, (EntityAIBase)new EntityAIWanderAvoidWater((EntityCreature)this, 0.8D, 1.0000001E-5F));
    this.tasks.addTask(11, (EntityAIBase)new EntityAIWatchClosest((EntityLiving)this, EntityPlayer.class, 10.0F));
    this.targetTasks.addTask(1, (EntityAIBase)new EntityAITargetNonTamed(this, EntityChicken.class, false, null));
  }
  
  protected void entityInit() {
    super.entityInit();
    this.dataManager.register(OCELOT_VARIANT, Integer.valueOf(0));
  }
  
  public void updateAITasks() {
    if (getMoveHelper().isUpdating()) {
      double d0 = getMoveHelper().getSpeed();
      if (d0 == 0.6D) {
        setSneaking(true);
        setSprinting(false);
      } else if (d0 == 1.33D) {
        setSneaking(false);
        setSprinting(true);
      } else {
        setSneaking(false);
        setSprinting(false);
      } 
    } else {
      setSneaking(false);
      setSprinting(false);
    } 
  }
  
  protected boolean canDespawn() {
    return (!isTamed() && this.ticksExisted > 2400);
  }
  
  protected void applyEntityAttributes() {
    super.applyEntityAttributes();
    getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(10.0D);
    getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.30000001192092896D);
  }
  
  public void fall(float distance, float damageMultiplier) {}
  
  public static void registerFixesOcelot(DataFixer fixer) {
    EntityLiving.registerFixesMob(fixer, EntityOcelot.class);
  }
  
  public void writeEntityToNBT(NBTTagCompound compound) {
    super.writeEntityToNBT(compound);
    compound.setInteger("CatType", getTameSkin());
  }
  
  public void readEntityFromNBT(NBTTagCompound compound) {
    super.readEntityFromNBT(compound);
    setTameSkin(compound.getInteger("CatType"));
  }
  
  @Nullable
  protected SoundEvent getAmbientSound() {
    if (isTamed()) {
      if (isInLove())
        return SoundEvents.ENTITY_CAT_PURR; 
      return (this.rand.nextInt(4) == 0) ? SoundEvents.ENTITY_CAT_PURREOW : SoundEvents.ENTITY_CAT_AMBIENT;
    } 
    return null;
  }
  
  protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
    return SoundEvents.ENTITY_CAT_HURT;
  }
  
  protected SoundEvent getDeathSound() {
    return SoundEvents.ENTITY_CAT_DEATH;
  }
  
  protected float getSoundVolume() {
    return 0.4F;
  }
  
  public boolean attackEntityAsMob(Entity entityIn) {
    return entityIn.attackEntityFrom(DamageSource.causeMobDamage((EntityLivingBase)this), 3.0F);
  }
  
  public boolean attackEntityFrom(DamageSource source, float amount) {
    if (isEntityInvulnerable(source))
      return false; 
    if (this.aiSit != null)
      this.aiSit.setSitting(false); 
    return super.attackEntityFrom(source, amount);
  }
  
  @Nullable
  protected ResourceLocation getLootTable() {
    return LootTableList.ENTITIES_OCELOT;
  }
  
  public boolean processInteract(EntityPlayer player, EnumHand hand) {
    ItemStack itemstack = player.getHeldItem(hand);
    if (isTamed()) {
      if (isOwner((EntityLivingBase)player) && !this.world.isRemote && !isBreedingItem(itemstack))
        this.aiSit.setSitting(!isSitting()); 
    } else if ((this.aiTempt == null || this.aiTempt.isRunning()) && itemstack.getItem() == Items.FISH && player.getDistanceSqToEntity((Entity)this) < 9.0D) {
      if (!player.capabilities.isCreativeMode)
        itemstack.func_190918_g(1); 
      if (!this.world.isRemote)
        if (this.rand.nextInt(3) == 0) {
          func_193101_c(player);
          setTameSkin(1 + this.world.rand.nextInt(3));
          playTameEffect(true);
          this.aiSit.setSitting(true);
          this.world.setEntityState((Entity)this, (byte)7);
        } else {
          playTameEffect(false);
          this.world.setEntityState((Entity)this, (byte)6);
        }  
      return true;
    } 
    return super.processInteract(player, hand);
  }
  
  public EntityOcelot createChild(EntityAgeable ageable) {
    EntityOcelot entityocelot = new EntityOcelot(this.world);
    if (isTamed()) {
      entityocelot.setOwnerId(getOwnerId());
      entityocelot.setTamed(true);
      entityocelot.setTameSkin(getTameSkin());
    } 
    return entityocelot;
  }
  
  public boolean isBreedingItem(ItemStack stack) {
    return (stack.getItem() == Items.FISH);
  }
  
  public boolean canMateWith(EntityAnimal otherAnimal) {
    if (otherAnimal == this)
      return false; 
    if (!isTamed())
      return false; 
    if (!(otherAnimal instanceof EntityOcelot))
      return false; 
    EntityOcelot entityocelot = (EntityOcelot)otherAnimal;
    if (!entityocelot.isTamed())
      return false; 
    return (isInLove() && entityocelot.isInLove());
  }
  
  public int getTameSkin() {
    return ((Integer)this.dataManager.get(OCELOT_VARIANT)).intValue();
  }
  
  public void setTameSkin(int skinId) {
    this.dataManager.set(OCELOT_VARIANT, Integer.valueOf(skinId));
  }
  
  public boolean getCanSpawnHere() {
    return (this.world.rand.nextInt(3) != 0);
  }
  
  public boolean isNotColliding() {
    if (this.world.checkNoEntityCollision(getEntityBoundingBox(), (Entity)this) && this.world.getCollisionBoxes((Entity)this, getEntityBoundingBox()).isEmpty() && !this.world.containsAnyLiquid(getEntityBoundingBox())) {
      BlockPos blockpos = new BlockPos(this.posX, (getEntityBoundingBox()).minY, this.posZ);
      if (blockpos.getY() < this.world.getSeaLevel())
        return false; 
      IBlockState iblockstate = this.world.getBlockState(blockpos.down());
      Block block = iblockstate.getBlock();
      if (block == Blocks.GRASS || iblockstate.getMaterial() == Material.LEAVES)
        return true; 
    } 
    return false;
  }
  
  public String getName() {
    if (hasCustomName())
      return getCustomNameTag(); 
    return isTamed() ? I18n.translateToLocal("entity.Cat.name") : super.getName();
  }
  
  protected void setupTamedAI() {
    if (this.avoidEntity == null)
      this.avoidEntity = new EntityAIAvoidEntity((EntityCreature)this, EntityPlayer.class, 16.0F, 0.8D, 1.33D); 
    this.tasks.removeTask((EntityAIBase)this.avoidEntity);
    if (!isTamed())
      this.tasks.addTask(4, (EntityAIBase)this.avoidEntity); 
  }
  
  @Nullable
  public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
    livingdata = super.onInitialSpawn(difficulty, livingdata);
    if (getTameSkin() == 0 && this.world.rand.nextInt(7) == 0)
      for (int i = 0; i < 2; i++) {
        EntityOcelot entityocelot = new EntityOcelot(this.world);
        entityocelot.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
        entityocelot.setGrowingAge(-24000);
        this.world.spawnEntityInWorld((Entity)entityocelot);
      }  
    return livingdata;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\entity\passive\EntityOcelot.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
package net.minecraft.entity.passive;

import com.google.common.collect.Sets;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
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
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityPig extends EntityAnimal {
  private static final DataParameter<Boolean> SADDLED = EntityDataManager.createKey(EntityPig.class, DataSerializers.BOOLEAN);
  
  private static final DataParameter<Integer> field_191520_bx = EntityDataManager.createKey(EntityPig.class, DataSerializers.VARINT);
  
  private static final Set<Item> TEMPTATION_ITEMS = Sets.newHashSet((Object[])new Item[] { Items.CARROT, Items.POTATO, Items.BEETROOT });
  
  private boolean boosting;
  
  private int boostTime;
  
  private int totalBoostTime;
  
  public EntityPig(World worldIn) {
    super(worldIn);
    setSize(0.9F, 0.9F);
  }
  
  protected void initEntityAI() {
    this.tasks.addTask(0, (EntityAIBase)new EntityAISwimming((EntityLiving)this));
    this.tasks.addTask(1, (EntityAIBase)new EntityAIPanic((EntityCreature)this, 1.25D));
    this.tasks.addTask(3, (EntityAIBase)new EntityAIMate(this, 1.0D));
    this.tasks.addTask(4, (EntityAIBase)new EntityAITempt((EntityCreature)this, 1.2D, Items.CARROT_ON_A_STICK, false));
    this.tasks.addTask(4, (EntityAIBase)new EntityAITempt((EntityCreature)this, 1.2D, false, TEMPTATION_ITEMS));
    this.tasks.addTask(5, (EntityAIBase)new EntityAIFollowParent(this, 1.1D));
    this.tasks.addTask(6, (EntityAIBase)new EntityAIWanderAvoidWater((EntityCreature)this, 1.0D));
    this.tasks.addTask(7, (EntityAIBase)new EntityAIWatchClosest((EntityLiving)this, EntityPlayer.class, 6.0F));
    this.tasks.addTask(8, (EntityAIBase)new EntityAILookIdle((EntityLiving)this));
  }
  
  protected void applyEntityAttributes() {
    super.applyEntityAttributes();
    getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(10.0D);
    getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
  }
  
  @Nullable
  public Entity getControllingPassenger() {
    return getPassengers().isEmpty() ? null : getPassengers().get(0);
  }
  
  public boolean canBeSteered() {
    Entity entity = getControllingPassenger();
    if (!(entity instanceof EntityPlayer))
      return false; 
    EntityPlayer entityplayer = (EntityPlayer)entity;
    return !(entityplayer.getHeldItemMainhand().getItem() != Items.CARROT_ON_A_STICK && entityplayer.getHeldItemOffhand().getItem() != Items.CARROT_ON_A_STICK);
  }
  
  public void notifyDataManagerChange(DataParameter<?> key) {
    if (field_191520_bx.equals(key) && this.world.isRemote) {
      this.boosting = true;
      this.boostTime = 0;
      this.totalBoostTime = ((Integer)this.dataManager.get(field_191520_bx)).intValue();
    } 
    super.notifyDataManagerChange(key);
  }
  
  protected void entityInit() {
    super.entityInit();
    this.dataManager.register(SADDLED, Boolean.valueOf(false));
    this.dataManager.register(field_191520_bx, Integer.valueOf(0));
  }
  
  public static void registerFixesPig(DataFixer fixer) {
    EntityLiving.registerFixesMob(fixer, EntityPig.class);
  }
  
  public void writeEntityToNBT(NBTTagCompound compound) {
    super.writeEntityToNBT(compound);
    compound.setBoolean("Saddle", getSaddled());
  }
  
  public void readEntityFromNBT(NBTTagCompound compound) {
    super.readEntityFromNBT(compound);
    setSaddled(compound.getBoolean("Saddle"));
  }
  
  protected SoundEvent getAmbientSound() {
    return SoundEvents.ENTITY_PIG_AMBIENT;
  }
  
  protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
    return SoundEvents.ENTITY_PIG_HURT;
  }
  
  protected SoundEvent getDeathSound() {
    return SoundEvents.ENTITY_PIG_DEATH;
  }
  
  protected void playStepSound(BlockPos pos, Block blockIn) {
    playSound(SoundEvents.ENTITY_PIG_STEP, 0.15F, 1.0F);
  }
  
  public boolean processInteract(EntityPlayer player, EnumHand hand) {
    if (!super.processInteract(player, hand)) {
      ItemStack itemstack = player.getHeldItem(hand);
      if (itemstack.getItem() == Items.NAME_TAG) {
        itemstack.interactWithEntity(player, (EntityLivingBase)this, hand);
        return true;
      } 
      if (getSaddled() && !isBeingRidden()) {
        if (!this.world.isRemote)
          player.startRiding((Entity)this); 
        return true;
      } 
      if (itemstack.getItem() == Items.SADDLE) {
        itemstack.interactWithEntity(player, (EntityLivingBase)this, hand);
        return true;
      } 
      return false;
    } 
    return true;
  }
  
  public void onDeath(DamageSource cause) {
    super.onDeath(cause);
    if (!this.world.isRemote)
      if (getSaddled())
        dropItem(Items.SADDLE, 1);  
  }
  
  @Nullable
  protected ResourceLocation getLootTable() {
    return LootTableList.ENTITIES_PIG;
  }
  
  public boolean getSaddled() {
    return ((Boolean)this.dataManager.get(SADDLED)).booleanValue();
  }
  
  public void setSaddled(boolean saddled) {
    if (saddled) {
      this.dataManager.set(SADDLED, Boolean.valueOf(true));
    } else {
      this.dataManager.set(SADDLED, Boolean.valueOf(false));
    } 
  }
  
  public void onStruckByLightning(EntityLightningBolt lightningBolt) {
    if (!this.world.isRemote && !this.isDead) {
      EntityPigZombie entitypigzombie = new EntityPigZombie(this.world);
      entitypigzombie.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.GOLDEN_SWORD));
      entitypigzombie.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
      entitypigzombie.setNoAI(isAIDisabled());
      if (hasCustomName()) {
        entitypigzombie.setCustomNameTag(getCustomNameTag());
        entitypigzombie.setAlwaysRenderNameTag(getAlwaysRenderNameTag());
      } 
      this.world.spawnEntityInWorld((Entity)entitypigzombie);
      setDead();
    } 
  }
  
  public void func_191986_a(float p_191986_1_, float p_191986_2_, float p_191986_3_) {
    Entity entity = getPassengers().isEmpty() ? null : getPassengers().get(0);
    if (isBeingRidden() && canBeSteered()) {
      this.rotationYaw = entity.rotationYaw;
      this.prevRotationYaw = this.rotationYaw;
      this.rotationPitch = entity.rotationPitch * 0.5F;
      setRotation(this.rotationYaw, this.rotationPitch);
      this.renderYawOffset = this.rotationYaw;
      this.rotationYawHead = this.rotationYaw;
      this.stepHeight = 1.0F;
      this.jumpMovementFactor = getAIMoveSpeed() * 0.1F;
      if (this.boosting && this.boostTime++ > this.totalBoostTime)
        this.boosting = false; 
      if (canPassengerSteer()) {
        float f = (float)getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue() * 0.225F;
        if (this.boosting)
          f += f * 1.15F * MathHelper.sin(this.boostTime / this.totalBoostTime * 3.1415927F); 
        setAIMoveSpeed(f);
        super.func_191986_a(0.0F, 0.0F, 1.0F);
      } else {
        this.motionX = 0.0D;
        this.motionY = 0.0D;
        this.motionZ = 0.0D;
      } 
      this.prevLimbSwingAmount = this.limbSwingAmount;
      double d1 = this.posX - this.prevPosX;
      double d0 = this.posZ - this.prevPosZ;
      float f1 = MathHelper.sqrt(d1 * d1 + d0 * d0) * 4.0F;
      if (f1 > 1.0F)
        f1 = 1.0F; 
      this.limbSwingAmount += (f1 - this.limbSwingAmount) * 0.4F;
      this.limbSwing += this.limbSwingAmount;
    } else {
      this.stepHeight = 0.5F;
      this.jumpMovementFactor = 0.02F;
      super.func_191986_a(p_191986_1_, p_191986_2_, p_191986_3_);
    } 
  }
  
  public boolean boost() {
    if (this.boosting)
      return false; 
    this.boosting = true;
    this.boostTime = 0;
    this.totalBoostTime = getRNG().nextInt(841) + 140;
    getDataManager().set(field_191520_bx, Integer.valueOf(this.totalBoostTime));
    return true;
  }
  
  public EntityPig createChild(EntityAgeable ageable) {
    return new EntityPig(this.world);
  }
  
  public boolean isBreedingItem(ItemStack stack) {
    return TEMPTATION_ITEMS.contains(stack.getItem());
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\entity\passive\EntityPig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
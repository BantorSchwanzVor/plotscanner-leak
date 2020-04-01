package net.minecraft.entity.passive;

import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackRanged;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILlamaFollowCaravan;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAIRunAroundLikeCrazy;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityLlamaSpit;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityLlama extends AbstractChestHorse implements IRangedAttackMob {
  private static final DataParameter<Integer> field_190720_bG = EntityDataManager.createKey(EntityLlama.class, DataSerializers.VARINT);
  
  private static final DataParameter<Integer> field_190721_bH = EntityDataManager.createKey(EntityLlama.class, DataSerializers.VARINT);
  
  private static final DataParameter<Integer> field_190722_bI = EntityDataManager.createKey(EntityLlama.class, DataSerializers.VARINT);
  
  private boolean field_190723_bJ;
  
  @Nullable
  private EntityLlama field_190724_bK;
  
  @Nullable
  private EntityLlama field_190725_bL;
  
  public EntityLlama(World p_i47297_1_) {
    super(p_i47297_1_);
    setSize(0.9F, 1.87F);
  }
  
  private void func_190706_p(int p_190706_1_) {
    this.dataManager.set(field_190720_bG, Integer.valueOf(Math.max(1, Math.min(5, p_190706_1_))));
  }
  
  private void func_190705_dT() {
    int i = (this.rand.nextFloat() < 0.04F) ? 5 : 3;
    func_190706_p(1 + this.rand.nextInt(i));
  }
  
  public int func_190707_dL() {
    return ((Integer)this.dataManager.get(field_190720_bG)).intValue();
  }
  
  public void writeEntityToNBT(NBTTagCompound compound) {
    super.writeEntityToNBT(compound);
    compound.setInteger("Variant", func_190719_dM());
    compound.setInteger("Strength", func_190707_dL());
    if (!this.horseChest.getStackInSlot(1).func_190926_b())
      compound.setTag("DecorItem", (NBTBase)this.horseChest.getStackInSlot(1).writeToNBT(new NBTTagCompound())); 
  }
  
  public void readEntityFromNBT(NBTTagCompound compound) {
    func_190706_p(compound.getInteger("Strength"));
    super.readEntityFromNBT(compound);
    func_190710_o(compound.getInteger("Variant"));
    if (compound.hasKey("DecorItem", 10))
      this.horseChest.setInventorySlotContents(1, new ItemStack(compound.getCompoundTag("DecorItem"))); 
    updateHorseSlots();
  }
  
  protected void initEntityAI() {
    this.tasks.addTask(0, (EntityAIBase)new EntityAISwimming((EntityLiving)this));
    this.tasks.addTask(1, (EntityAIBase)new EntityAIRunAroundLikeCrazy(this, 1.2D));
    this.tasks.addTask(2, (EntityAIBase)new EntityAILlamaFollowCaravan(this, 2.0999999046325684D));
    this.tasks.addTask(3, (EntityAIBase)new EntityAIAttackRanged(this, 1.25D, 40, 20.0F));
    this.tasks.addTask(3, (EntityAIBase)new EntityAIPanic((EntityCreature)this, 1.2D));
    this.tasks.addTask(4, (EntityAIBase)new EntityAIMate(this, 1.0D));
    this.tasks.addTask(5, (EntityAIBase)new EntityAIFollowParent(this, 1.0D));
    this.tasks.addTask(6, (EntityAIBase)new EntityAIWanderAvoidWater((EntityCreature)this, 0.7D));
    this.tasks.addTask(7, (EntityAIBase)new EntityAIWatchClosest((EntityLiving)this, EntityPlayer.class, 6.0F));
    this.tasks.addTask(8, (EntityAIBase)new EntityAILookIdle((EntityLiving)this));
    this.targetTasks.addTask(1, (EntityAIBase)new AIHurtByTarget(this));
    this.targetTasks.addTask(2, (EntityAIBase)new AIDefendTarget(this));
  }
  
  protected void applyEntityAttributes() {
    super.applyEntityAttributes();
    getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(40.0D);
  }
  
  protected void entityInit() {
    super.entityInit();
    this.dataManager.register(field_190720_bG, Integer.valueOf(0));
    this.dataManager.register(field_190721_bH, Integer.valueOf(-1));
    this.dataManager.register(field_190722_bI, Integer.valueOf(0));
  }
  
  public int func_190719_dM() {
    return MathHelper.clamp(((Integer)this.dataManager.get(field_190722_bI)).intValue(), 0, 3);
  }
  
  public void func_190710_o(int p_190710_1_) {
    this.dataManager.set(field_190722_bI, Integer.valueOf(p_190710_1_));
  }
  
  protected int func_190686_di() {
    return func_190695_dh() ? (2 + 3 * func_190696_dl()) : super.func_190686_di();
  }
  
  public void updatePassenger(Entity passenger) {
    if (isPassenger(passenger)) {
      float f = MathHelper.cos(this.renderYawOffset * 0.017453292F);
      float f1 = MathHelper.sin(this.renderYawOffset * 0.017453292F);
      float f2 = 0.3F;
      passenger.setPosition(this.posX + (0.3F * f1), this.posY + getMountedYOffset() + passenger.getYOffset(), this.posZ - (0.3F * f));
    } 
  }
  
  public double getMountedYOffset() {
    return this.height * 0.67D;
  }
  
  public boolean canBeSteered() {
    return false;
  }
  
  protected boolean func_190678_b(EntityPlayer p_190678_1_, ItemStack p_190678_2_) {
    int i = 0;
    int j = 0;
    float f = 0.0F;
    boolean flag = false;
    Item item = p_190678_2_.getItem();
    if (item == Items.WHEAT) {
      i = 10;
      j = 3;
      f = 2.0F;
    } else if (item == Item.getItemFromBlock(Blocks.HAY_BLOCK)) {
      i = 90;
      j = 6;
      f = 10.0F;
      if (isTame() && getGrowingAge() == 0) {
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
    if (flag && !isSilent())
      this.world.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.field_191253_dD, getSoundCategory(), 1.0F, 1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F); 
    return flag;
  }
  
  protected boolean isMovementBlocked() {
    return !(getHealth() > 0.0F && !isEatingHaystack());
  }
  
  @Nullable
  public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
    int i;
    livingdata = super.onInitialSpawn(difficulty, livingdata);
    func_190705_dT();
    if (livingdata instanceof GroupData) {
      i = ((GroupData)livingdata).field_190886_a;
    } else {
      i = this.rand.nextInt(4);
      livingdata = new GroupData(i, null);
    } 
    func_190710_o(i);
    return livingdata;
  }
  
  public boolean func_190717_dN() {
    return (func_190704_dO() != null);
  }
  
  protected SoundEvent getAngrySound() {
    return SoundEvents.field_191250_dA;
  }
  
  protected SoundEvent getAmbientSound() {
    return SoundEvents.field_191260_dz;
  }
  
  protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
    return SoundEvents.field_191254_dE;
  }
  
  protected SoundEvent getDeathSound() {
    return SoundEvents.field_191252_dC;
  }
  
  protected void playStepSound(BlockPos pos, Block blockIn) {
    playSound(SoundEvents.field_191256_dG, 0.15F, 1.0F);
  }
  
  protected void func_190697_dk() {
    playSound(SoundEvents.field_191251_dB, 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
  }
  
  public void func_190687_dF() {
    SoundEvent soundevent = getAngrySound();
    if (soundevent != null)
      playSound(soundevent, getSoundVolume(), getSoundPitch()); 
  }
  
  @Nullable
  protected ResourceLocation getLootTable() {
    return LootTableList.field_191187_aw;
  }
  
  public int func_190696_dl() {
    return func_190707_dL();
  }
  
  public boolean func_190677_dK() {
    return true;
  }
  
  public boolean func_190682_f(ItemStack p_190682_1_) {
    return (p_190682_1_.getItem() == Item.getItemFromBlock(Blocks.CARPET));
  }
  
  public boolean func_190685_dA() {
    return false;
  }
  
  public void onInventoryChanged(IInventory invBasic) {
    EnumDyeColor enumdyecolor = func_190704_dO();
    super.onInventoryChanged(invBasic);
    EnumDyeColor enumdyecolor1 = func_190704_dO();
    if (this.ticksExisted > 20 && enumdyecolor1 != null && enumdyecolor1 != enumdyecolor)
      playSound(SoundEvents.field_191257_dH, 0.5F, 1.0F); 
  }
  
  protected void updateHorseSlots() {
    if (!this.world.isRemote) {
      super.updateHorseSlots();
      func_190702_g(this.horseChest.getStackInSlot(1));
    } 
  }
  
  private void func_190711_a(@Nullable EnumDyeColor p_190711_1_) {
    this.dataManager.set(field_190721_bH, Integer.valueOf((p_190711_1_ == null) ? -1 : p_190711_1_.getMetadata()));
  }
  
  private void func_190702_g(ItemStack p_190702_1_) {
    if (func_190682_f(p_190702_1_)) {
      func_190711_a(EnumDyeColor.byMetadata(p_190702_1_.getMetadata()));
    } else {
      func_190711_a((EnumDyeColor)null);
    } 
  }
  
  @Nullable
  public EnumDyeColor func_190704_dO() {
    int i = ((Integer)this.dataManager.get(field_190721_bH)).intValue();
    return (i == -1) ? null : EnumDyeColor.byMetadata(i);
  }
  
  public int func_190676_dC() {
    return 30;
  }
  
  public boolean canMateWith(EntityAnimal otherAnimal) {
    return (otherAnimal != this && otherAnimal instanceof EntityLlama && canMate() && ((EntityLlama)otherAnimal).canMate());
  }
  
  public EntityLlama createChild(EntityAgeable ageable) {
    EntityLlama entityllama = new EntityLlama(this.world);
    func_190681_a(ageable, entityllama);
    EntityLlama entityllama1 = (EntityLlama)ageable;
    int i = this.rand.nextInt(Math.max(func_190707_dL(), entityllama1.func_190707_dL())) + 1;
    if (this.rand.nextFloat() < 0.03F)
      i++; 
    entityllama.func_190706_p(i);
    entityllama.func_190710_o(this.rand.nextBoolean() ? func_190719_dM() : entityllama1.func_190719_dM());
    return entityllama;
  }
  
  private void func_190713_e(EntityLivingBase p_190713_1_) {
    EntityLlamaSpit entityllamaspit = new EntityLlamaSpit(this.world, this);
    double d0 = p_190713_1_.posX - this.posX;
    double d1 = (p_190713_1_.getEntityBoundingBox()).minY + (p_190713_1_.height / 3.0F) - entityllamaspit.posY;
    double d2 = p_190713_1_.posZ - this.posZ;
    float f = MathHelper.sqrt(d0 * d0 + d2 * d2) * 0.2F;
    entityllamaspit.setThrowableHeading(d0, d1 + f, d2, 1.5F, 10.0F);
    this.world.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.field_191255_dF, getSoundCategory(), 1.0F, 1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F);
    this.world.spawnEntityInWorld((Entity)entityllamaspit);
    this.field_190723_bJ = true;
  }
  
  private void func_190714_x(boolean p_190714_1_) {
    this.field_190723_bJ = p_190714_1_;
  }
  
  public void fall(float distance, float damageMultiplier) {
    int i = MathHelper.ceil((distance * 0.5F - 3.0F) * damageMultiplier);
    if (i > 0) {
      if (distance >= 6.0F) {
        attackEntityFrom(DamageSource.fall, i);
        if (isBeingRidden())
          for (Entity entity : getRecursivePassengers())
            entity.attackEntityFrom(DamageSource.fall, i);  
      } 
      IBlockState iblockstate = this.world.getBlockState(new BlockPos(this.posX, this.posY - 0.2D - this.prevRotationYaw, this.posZ));
      Block block = iblockstate.getBlock();
      if (iblockstate.getMaterial() != Material.AIR && !isSilent()) {
        SoundType soundtype = block.getSoundType();
        this.world.playSound(null, this.posX, this.posY, this.posZ, soundtype.getStepSound(), getSoundCategory(), soundtype.getVolume() * 0.5F, soundtype.getPitch() * 0.75F);
      } 
    } 
  }
  
  public void func_190709_dP() {
    if (this.field_190724_bK != null)
      this.field_190724_bK.field_190725_bL = null; 
    this.field_190724_bK = null;
  }
  
  public void func_190715_a(EntityLlama p_190715_1_) {
    this.field_190724_bK = p_190715_1_;
    this.field_190724_bK.field_190725_bL = this;
  }
  
  public boolean func_190712_dQ() {
    return (this.field_190725_bL != null);
  }
  
  public boolean func_190718_dR() {
    return (this.field_190724_bK != null);
  }
  
  @Nullable
  public EntityLlama func_190716_dS() {
    return this.field_190724_bK;
  }
  
  protected double func_190634_dg() {
    return 2.0D;
  }
  
  protected void func_190679_dD() {
    if (!func_190718_dR() && isChild())
      super.func_190679_dD(); 
  }
  
  public boolean func_190684_dE() {
    return false;
  }
  
  public void attackEntityWithRangedAttack(EntityLivingBase target, float distanceFactor) {
    func_190713_e(target);
  }
  
  public void setSwingingArms(boolean swingingArms) {}
  
  static class AIDefendTarget extends EntityAINearestAttackableTarget<EntityWolf> {
    public AIDefendTarget(EntityLlama p_i47285_1_) {
      super((EntityCreature)p_i47285_1_, EntityWolf.class, 16, false, true, null);
    }
    
    public boolean shouldExecute() {
      if (super.shouldExecute() && this.targetEntity != null && !((EntityWolf)this.targetEntity).isTamed())
        return true; 
      this.taskOwner.setAttackTarget(null);
      return false;
    }
    
    protected double getTargetDistance() {
      return super.getTargetDistance() * 0.25D;
    }
  }
  
  static class AIHurtByTarget extends EntityAIHurtByTarget {
    public AIHurtByTarget(EntityLlama p_i47282_1_) {
      super((EntityCreature)p_i47282_1_, false, new Class[0]);
    }
    
    public boolean continueExecuting() {
      if (this.taskOwner instanceof EntityLlama) {
        EntityLlama entityllama = (EntityLlama)this.taskOwner;
        if (entityllama.field_190723_bJ) {
          entityllama.func_190714_x(false);
          return false;
        } 
      } 
      return super.continueExecuting();
    }
  }
  
  static class GroupData implements IEntityLivingData {
    public int field_190886_a;
    
    private GroupData(int p_i47283_1_) {
      this.field_190886_a = p_i47283_1_;
    }
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\entity\passive\EntityLlama.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
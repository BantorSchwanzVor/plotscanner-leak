package net.minecraft.entity.monster;

import java.util.Calendar;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIAttackRangedBow;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIFleeSun;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIRestrictSun;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public abstract class AbstractSkeleton extends EntityMob implements IRangedAttackMob {
  private static final DataParameter<Boolean> SWINGING_ARMS = EntityDataManager.createKey(AbstractSkeleton.class, DataSerializers.BOOLEAN);
  
  private final EntityAIAttackRangedBow<AbstractSkeleton> aiArrowAttack = new EntityAIAttackRangedBow(this, 1.0D, 20, 15.0F);
  
  private final EntityAIAttackMelee aiAttackOnCollide = new EntityAIAttackMelee(this, 1.2D, false) {
      public void resetTask() {
        super.resetTask();
        AbstractSkeleton.this.setSwingingArms(false);
      }
      
      public void startExecuting() {
        super.startExecuting();
        AbstractSkeleton.this.setSwingingArms(true);
      }
    };
  
  public AbstractSkeleton(World p_i47289_1_) {
    super(p_i47289_1_);
    setSize(0.6F, 1.99F);
    setCombatTask();
  }
  
  protected void initEntityAI() {
    this.tasks.addTask(1, (EntityAIBase)new EntityAISwimming((EntityLiving)this));
    this.tasks.addTask(2, (EntityAIBase)new EntityAIRestrictSun(this));
    this.tasks.addTask(3, (EntityAIBase)new EntityAIFleeSun(this, 1.0D));
    this.tasks.addTask(3, (EntityAIBase)new EntityAIAvoidEntity(this, EntityWolf.class, 6.0F, 1.0D, 1.2D));
    this.tasks.addTask(5, (EntityAIBase)new EntityAIWanderAvoidWater(this, 1.0D));
    this.tasks.addTask(6, (EntityAIBase)new EntityAIWatchClosest((EntityLiving)this, EntityPlayer.class, 8.0F));
    this.tasks.addTask(6, (EntityAIBase)new EntityAILookIdle((EntityLiving)this));
    this.targetTasks.addTask(1, (EntityAIBase)new EntityAIHurtByTarget(this, false, new Class[0]));
    this.targetTasks.addTask(2, (EntityAIBase)new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
    this.targetTasks.addTask(3, (EntityAIBase)new EntityAINearestAttackableTarget(this, EntityIronGolem.class, true));
  }
  
  protected void applyEntityAttributes() {
    super.applyEntityAttributes();
    getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
  }
  
  protected void entityInit() {
    super.entityInit();
    this.dataManager.register(SWINGING_ARMS, Boolean.valueOf(false));
  }
  
  protected void playStepSound(BlockPos pos, Block blockIn) {
    playSound(func_190727_o(), 0.15F, 1.0F);
  }
  
  abstract SoundEvent func_190727_o();
  
  public EnumCreatureAttribute getCreatureAttribute() {
    return EnumCreatureAttribute.UNDEAD;
  }
  
  public void onLivingUpdate() {
    if (this.world.isDaytime() && !this.world.isRemote) {
      float f = getBrightness();
      BlockPos blockpos = (getRidingEntity() instanceof net.minecraft.entity.item.EntityBoat) ? (new BlockPos(this.posX, Math.round(this.posY), this.posZ)).up() : new BlockPos(this.posX, Math.round(this.posY), this.posZ);
      if (f > 0.5F && this.rand.nextFloat() * 30.0F < (f - 0.4F) * 2.0F && this.world.canSeeSky(blockpos)) {
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
  
  public void updateRidden() {
    super.updateRidden();
    if (getRidingEntity() instanceof EntityCreature) {
      EntityCreature entitycreature = (EntityCreature)getRidingEntity();
      this.renderYawOffset = entitycreature.renderYawOffset;
    } 
  }
  
  protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
    super.setEquipmentBasedOnDifficulty(difficulty);
    setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack((Item)Items.BOW));
  }
  
  @Nullable
  public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
    livingdata = super.onInitialSpawn(difficulty, livingdata);
    setEquipmentBasedOnDifficulty(difficulty);
    setEnchantmentBasedOnDifficulty(difficulty);
    setCombatTask();
    setCanPickUpLoot((this.rand.nextFloat() < 0.55F * difficulty.getClampedAdditionalDifficulty()));
    if (getItemStackFromSlot(EntityEquipmentSlot.HEAD).func_190926_b()) {
      Calendar calendar = this.world.getCurrentDate();
      if (calendar.get(2) + 1 == 10 && calendar.get(5) == 31 && this.rand.nextFloat() < 0.25F) {
        setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack((this.rand.nextFloat() < 0.1F) ? Blocks.LIT_PUMPKIN : Blocks.PUMPKIN));
        this.inventoryArmorDropChances[EntityEquipmentSlot.HEAD.getIndex()] = 0.0F;
      } 
    } 
    return livingdata;
  }
  
  public void setCombatTask() {
    if (this.world != null && !this.world.isRemote) {
      this.tasks.removeTask((EntityAIBase)this.aiAttackOnCollide);
      this.tasks.removeTask((EntityAIBase)this.aiArrowAttack);
      ItemStack itemstack = getHeldItemMainhand();
      if (itemstack.getItem() == Items.BOW) {
        int i = 20;
        if (this.world.getDifficulty() != EnumDifficulty.HARD)
          i = 40; 
        this.aiArrowAttack.setAttackCooldown(i);
        this.tasks.addTask(4, (EntityAIBase)this.aiArrowAttack);
      } else {
        this.tasks.addTask(4, (EntityAIBase)this.aiAttackOnCollide);
      } 
    } 
  }
  
  public void attackEntityWithRangedAttack(EntityLivingBase target, float distanceFactor) {
    EntityArrow entityarrow = func_190726_a(distanceFactor);
    double d0 = target.posX - this.posX;
    double d1 = (target.getEntityBoundingBox()).minY + (target.height / 3.0F) - entityarrow.posY;
    double d2 = target.posZ - this.posZ;
    double d3 = MathHelper.sqrt(d0 * d0 + d2 * d2);
    entityarrow.setThrowableHeading(d0, d1 + d3 * 0.20000000298023224D, d2, 1.6F, (14 - this.world.getDifficulty().getDifficultyId() * 4));
    playSound(SoundEvents.ENTITY_SKELETON_SHOOT, 1.0F, 1.0F / (getRNG().nextFloat() * 0.4F + 0.8F));
    this.world.spawnEntityInWorld((Entity)entityarrow);
  }
  
  protected EntityArrow func_190726_a(float p_190726_1_) {
    EntityTippedArrow entitytippedarrow = new EntityTippedArrow(this.world, (EntityLivingBase)this);
    entitytippedarrow.func_190547_a((EntityLivingBase)this, p_190726_1_);
    return (EntityArrow)entitytippedarrow;
  }
  
  public void readEntityFromNBT(NBTTagCompound compound) {
    super.readEntityFromNBT(compound);
    setCombatTask();
  }
  
  public void setItemStackToSlot(EntityEquipmentSlot slotIn, ItemStack stack) {
    super.setItemStackToSlot(slotIn, stack);
    if (!this.world.isRemote && slotIn == EntityEquipmentSlot.MAINHAND)
      setCombatTask(); 
  }
  
  public float getEyeHeight() {
    return 1.74F;
  }
  
  public double getYOffset() {
    return -0.6D;
  }
  
  public boolean isSwingingArms() {
    return ((Boolean)this.dataManager.get(SWINGING_ARMS)).booleanValue();
  }
  
  public void setSwingingArms(boolean swingingArms) {
    this.dataManager.set(SWINGING_ARMS, Boolean.valueOf(swingingArms));
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\entity\monster\AbstractSkeleton.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
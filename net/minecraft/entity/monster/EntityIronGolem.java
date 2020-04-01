package net.minecraft.entity.monster;

import com.google.common.base.Predicate;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIDefendVillage;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookAtVillager;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveThroughVillage;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAIMoveTowardsTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.village.Village;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityIronGolem extends EntityGolem {
  protected static final DataParameter<Byte> PLAYER_CREATED = EntityDataManager.createKey(EntityIronGolem.class, DataSerializers.BYTE);
  
  private int homeCheckTimer;
  
  @Nullable
  Village villageObj;
  
  private int attackTimer;
  
  private int holdRoseTick;
  
  public EntityIronGolem(World worldIn) {
    super(worldIn);
    setSize(1.4F, 2.7F);
  }
  
  protected void initEntityAI() {
    this.tasks.addTask(1, (EntityAIBase)new EntityAIAttackMelee(this, 1.0D, true));
    this.tasks.addTask(2, (EntityAIBase)new EntityAIMoveTowardsTarget(this, 0.9D, 32.0F));
    this.tasks.addTask(3, (EntityAIBase)new EntityAIMoveThroughVillage(this, 0.6D, true));
    this.tasks.addTask(4, (EntityAIBase)new EntityAIMoveTowardsRestriction(this, 1.0D));
    this.tasks.addTask(5, (EntityAIBase)new EntityAILookAtVillager(this));
    this.tasks.addTask(6, (EntityAIBase)new EntityAIWanderAvoidWater(this, 0.6D));
    this.tasks.addTask(7, (EntityAIBase)new EntityAIWatchClosest((EntityLiving)this, EntityPlayer.class, 6.0F));
    this.tasks.addTask(8, (EntityAIBase)new EntityAILookIdle((EntityLiving)this));
    this.targetTasks.addTask(1, (EntityAIBase)new EntityAIDefendVillage(this));
    this.targetTasks.addTask(2, (EntityAIBase)new EntityAIHurtByTarget(this, false, new Class[0]));
    this.targetTasks.addTask(3, (EntityAIBase)new EntityAINearestAttackableTarget(this, EntityLiving.class, 10, false, true, new Predicate<EntityLiving>() {
            public boolean apply(@Nullable EntityLiving p_apply_1_) {
              return (p_apply_1_ != null && IMob.VISIBLE_MOB_SELECTOR.apply(p_apply_1_) && !(p_apply_1_ instanceof EntityCreeper));
            }
          }));
  }
  
  protected void entityInit() {
    super.entityInit();
    this.dataManager.register(PLAYER_CREATED, Byte.valueOf((byte)0));
  }
  
  protected void updateAITasks() {
    if (--this.homeCheckTimer <= 0) {
      this.homeCheckTimer = 70 + this.rand.nextInt(50);
      this.villageObj = this.world.getVillageCollection().getNearestVillage(new BlockPos((Entity)this), 32);
      if (this.villageObj == null) {
        detachHome();
      } else {
        BlockPos blockpos = this.villageObj.getCenter();
        setHomePosAndDistance(blockpos, (int)(this.villageObj.getVillageRadius() * 0.6F));
      } 
    } 
    super.updateAITasks();
  }
  
  protected void applyEntityAttributes() {
    super.applyEntityAttributes();
    getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(100.0D);
    getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
    getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
  }
  
  protected int decreaseAirSupply(int air) {
    return air;
  }
  
  protected void collideWithEntity(Entity entityIn) {
    if (entityIn instanceof IMob && !(entityIn instanceof EntityCreeper) && getRNG().nextInt(20) == 0)
      setAttackTarget((EntityLivingBase)entityIn); 
    super.collideWithEntity(entityIn);
  }
  
  public void onLivingUpdate() {
    super.onLivingUpdate();
    if (this.attackTimer > 0)
      this.attackTimer--; 
    if (this.holdRoseTick > 0)
      this.holdRoseTick--; 
    if (this.motionX * this.motionX + this.motionZ * this.motionZ > 2.500000277905201E-7D && this.rand.nextInt(5) == 0) {
      int i = MathHelper.floor(this.posX);
      int j = MathHelper.floor(this.posY - 0.20000000298023224D);
      int k = MathHelper.floor(this.posZ);
      IBlockState iblockstate = this.world.getBlockState(new BlockPos(i, j, k));
      if (iblockstate.getMaterial() != Material.AIR)
        this.world.spawnParticle(EnumParticleTypes.BLOCK_CRACK, this.posX + (this.rand.nextFloat() - 0.5D) * this.width, (getEntityBoundingBox()).minY + 0.1D, this.posZ + (this.rand.nextFloat() - 0.5D) * this.width, 4.0D * (this.rand.nextFloat() - 0.5D), 0.5D, (this.rand.nextFloat() - 0.5D) * 4.0D, new int[] { Block.getStateId(iblockstate) }); 
    } 
  }
  
  public boolean canAttackClass(Class<? extends EntityLivingBase> cls) {
    if (isPlayerCreated() && EntityPlayer.class.isAssignableFrom(cls))
      return false; 
    return (cls == EntityCreeper.class) ? false : super.canAttackClass(cls);
  }
  
  public static void registerFixesIronGolem(DataFixer fixer) {
    EntityLiving.registerFixesMob(fixer, EntityIronGolem.class);
  }
  
  public void writeEntityToNBT(NBTTagCompound compound) {
    super.writeEntityToNBT(compound);
    compound.setBoolean("PlayerCreated", isPlayerCreated());
  }
  
  public void readEntityFromNBT(NBTTagCompound compound) {
    super.readEntityFromNBT(compound);
    setPlayerCreated(compound.getBoolean("PlayerCreated"));
  }
  
  public boolean attackEntityAsMob(Entity entityIn) {
    this.attackTimer = 10;
    this.world.setEntityState((Entity)this, (byte)4);
    boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage((EntityLivingBase)this), (7 + this.rand.nextInt(15)));
    if (flag) {
      entityIn.motionY += 0.4000000059604645D;
      applyEnchantments((EntityLivingBase)this, entityIn);
    } 
    playSound(SoundEvents.ENTITY_IRONGOLEM_ATTACK, 1.0F, 1.0F);
    return flag;
  }
  
  public void handleStatusUpdate(byte id) {
    if (id == 4) {
      this.attackTimer = 10;
      playSound(SoundEvents.ENTITY_IRONGOLEM_ATTACK, 1.0F, 1.0F);
    } else if (id == 11) {
      this.holdRoseTick = 400;
    } else if (id == 34) {
      this.holdRoseTick = 0;
    } else {
      super.handleStatusUpdate(id);
    } 
  }
  
  public Village getVillage() {
    return this.villageObj;
  }
  
  public int getAttackTimer() {
    return this.attackTimer;
  }
  
  public void setHoldingRose(boolean p_70851_1_) {
    if (p_70851_1_) {
      this.holdRoseTick = 400;
      this.world.setEntityState((Entity)this, (byte)11);
    } else {
      this.holdRoseTick = 0;
      this.world.setEntityState((Entity)this, (byte)34);
    } 
  }
  
  protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
    return SoundEvents.ENTITY_IRONGOLEM_HURT;
  }
  
  protected SoundEvent getDeathSound() {
    return SoundEvents.ENTITY_IRONGOLEM_DEATH;
  }
  
  protected void playStepSound(BlockPos pos, Block blockIn) {
    playSound(SoundEvents.ENTITY_IRONGOLEM_STEP, 1.0F, 1.0F);
  }
  
  @Nullable
  protected ResourceLocation getLootTable() {
    return LootTableList.ENTITIES_IRON_GOLEM;
  }
  
  public int getHoldRoseTick() {
    return this.holdRoseTick;
  }
  
  public boolean isPlayerCreated() {
    return ((((Byte)this.dataManager.get(PLAYER_CREATED)).byteValue() & 0x1) != 0);
  }
  
  public void setPlayerCreated(boolean playerCreated) {
    byte b0 = ((Byte)this.dataManager.get(PLAYER_CREATED)).byteValue();
    if (playerCreated) {
      this.dataManager.set(PLAYER_CREATED, Byte.valueOf((byte)(b0 | 0x1)));
    } else {
      this.dataManager.set(PLAYER_CREATED, Byte.valueOf((byte)(b0 & 0xFFFFFFFE)));
    } 
  }
  
  public void onDeath(DamageSource cause) {
    if (!isPlayerCreated() && this.attackingPlayer != null && this.villageObj != null)
      this.villageObj.modifyPlayerReputation(this.attackingPlayer.getName(), -5); 
    super.onDeath(cause);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\entity\monster\EntityIronGolem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
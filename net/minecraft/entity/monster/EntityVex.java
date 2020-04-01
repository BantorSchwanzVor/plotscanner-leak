package net.minecraft.entity.monster;

import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityVex extends EntityMob {
  protected static final DataParameter<Byte> field_190664_a = EntityDataManager.createKey(EntityVex.class, DataSerializers.BYTE);
  
  private EntityLiving field_190665_b;
  
  @Nullable
  private BlockPos field_190666_c;
  
  private boolean field_190667_bw;
  
  private int field_190668_bx;
  
  public EntityVex(World p_i47280_1_) {
    super(p_i47280_1_);
    this.isImmuneToFire = true;
    this.moveHelper = new AIMoveControl(this);
    setSize(0.4F, 0.8F);
    this.experienceValue = 3;
  }
  
  public void moveEntity(MoverType x, double p_70091_2_, double p_70091_4_, double p_70091_6_) {
    super.moveEntity(x, p_70091_2_, p_70091_4_, p_70091_6_);
    doBlockCollisions();
  }
  
  public void onUpdate() {
    this.noClip = true;
    super.onUpdate();
    this.noClip = false;
    setNoGravity(true);
    if (this.field_190667_bw && --this.field_190668_bx <= 0) {
      this.field_190668_bx = 20;
      attackEntityFrom(DamageSource.starve, 1.0F);
    } 
  }
  
  protected void initEntityAI() {
    super.initEntityAI();
    this.tasks.addTask(0, (EntityAIBase)new EntityAISwimming((EntityLiving)this));
    this.tasks.addTask(4, new AIChargeAttack());
    this.tasks.addTask(8, new AIMoveRandom());
    this.tasks.addTask(9, (EntityAIBase)new EntityAIWatchClosest((EntityLiving)this, EntityPlayer.class, 3.0F, 1.0F));
    this.tasks.addTask(10, (EntityAIBase)new EntityAIWatchClosest((EntityLiving)this, EntityLiving.class, 8.0F));
    this.targetTasks.addTask(1, (EntityAIBase)new EntityAIHurtByTarget(this, true, new Class[] { EntityVex.class }));
    this.targetTasks.addTask(2, (EntityAIBase)new AICopyOwnerTarget(this));
    this.targetTasks.addTask(3, (EntityAIBase)new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
  }
  
  protected void applyEntityAttributes() {
    super.applyEntityAttributes();
    getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(14.0D);
    getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4.0D);
  }
  
  protected void entityInit() {
    super.entityInit();
    this.dataManager.register(field_190664_a, Byte.valueOf((byte)0));
  }
  
  public static void func_190663_b(DataFixer p_190663_0_) {
    EntityLiving.registerFixesMob(p_190663_0_, EntityVex.class);
  }
  
  public void readEntityFromNBT(NBTTagCompound compound) {
    super.readEntityFromNBT(compound);
    if (compound.hasKey("BoundX"))
      this.field_190666_c = new BlockPos(compound.getInteger("BoundX"), compound.getInteger("BoundY"), compound.getInteger("BoundZ")); 
    if (compound.hasKey("LifeTicks"))
      func_190653_a(compound.getInteger("LifeTicks")); 
  }
  
  public void writeEntityToNBT(NBTTagCompound compound) {
    super.writeEntityToNBT(compound);
    if (this.field_190666_c != null) {
      compound.setInteger("BoundX", this.field_190666_c.getX());
      compound.setInteger("BoundY", this.field_190666_c.getY());
      compound.setInteger("BoundZ", this.field_190666_c.getZ());
    } 
    if (this.field_190667_bw)
      compound.setInteger("LifeTicks", this.field_190668_bx); 
  }
  
  public EntityLiving func_190645_o() {
    return this.field_190665_b;
  }
  
  @Nullable
  public BlockPos func_190646_di() {
    return this.field_190666_c;
  }
  
  public void func_190651_g(@Nullable BlockPos p_190651_1_) {
    this.field_190666_c = p_190651_1_;
  }
  
  private boolean func_190656_b(int p_190656_1_) {
    int i = ((Byte)this.dataManager.get(field_190664_a)).byteValue();
    return ((i & p_190656_1_) != 0);
  }
  
  private void func_190660_a(int p_190660_1_, boolean p_190660_2_) {
    int i = ((Byte)this.dataManager.get(field_190664_a)).byteValue();
    if (p_190660_2_) {
      i |= p_190660_1_;
    } else {
      i &= p_190660_1_ ^ 0xFFFFFFFF;
    } 
    this.dataManager.set(field_190664_a, Byte.valueOf((byte)(i & 0xFF)));
  }
  
  public boolean func_190647_dj() {
    return func_190656_b(1);
  }
  
  public void func_190648_a(boolean p_190648_1_) {
    func_190660_a(1, p_190648_1_);
  }
  
  public void func_190658_a(EntityLiving p_190658_1_) {
    this.field_190665_b = p_190658_1_;
  }
  
  public void func_190653_a(int p_190653_1_) {
    this.field_190667_bw = true;
    this.field_190668_bx = p_190653_1_;
  }
  
  protected SoundEvent getAmbientSound() {
    return SoundEvents.field_191264_hc;
  }
  
  protected SoundEvent getDeathSound() {
    return SoundEvents.field_191266_he;
  }
  
  protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
    return SoundEvents.field_191267_hf;
  }
  
  @Nullable
  protected ResourceLocation getLootTable() {
    return LootTableList.field_191188_ax;
  }
  
  public int getBrightnessForRender() {
    return 15728880;
  }
  
  public float getBrightness() {
    return 1.0F;
  }
  
  @Nullable
  public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
    setEquipmentBasedOnDifficulty(difficulty);
    setEnchantmentBasedOnDifficulty(difficulty);
    return super.onInitialSpawn(difficulty, livingdata);
  }
  
  protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
    setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
    setDropChance(EntityEquipmentSlot.MAINHAND, 0.0F);
  }
  
  class AIChargeAttack extends EntityAIBase {
    public AIChargeAttack() {
      setMutexBits(1);
    }
    
    public boolean shouldExecute() {
      if (EntityVex.this.getAttackTarget() != null && !EntityVex.this.getMoveHelper().isUpdating() && EntityVex.this.rand.nextInt(7) == 0)
        return (EntityVex.this.getDistanceSqToEntity((Entity)EntityVex.this.getAttackTarget()) > 4.0D); 
      return false;
    }
    
    public boolean continueExecuting() {
      return (EntityVex.this.getMoveHelper().isUpdating() && EntityVex.this.func_190647_dj() && EntityVex.this.getAttackTarget() != null && EntityVex.this.getAttackTarget().isEntityAlive());
    }
    
    public void startExecuting() {
      EntityLivingBase entitylivingbase = EntityVex.this.getAttackTarget();
      Vec3d vec3d = entitylivingbase.getPositionEyes(1.0F);
      EntityVex.this.moveHelper.setMoveTo(vec3d.xCoord, vec3d.yCoord, vec3d.zCoord, 1.0D);
      EntityVex.this.func_190648_a(true);
      EntityVex.this.playSound(SoundEvents.field_191265_hd, 1.0F, 1.0F);
    }
    
    public void resetTask() {
      EntityVex.this.func_190648_a(false);
    }
    
    public void updateTask() {
      EntityLivingBase entitylivingbase = EntityVex.this.getAttackTarget();
      if (EntityVex.this.getEntityBoundingBox().intersectsWith(entitylivingbase.getEntityBoundingBox())) {
        EntityVex.this.attackEntityAsMob((Entity)entitylivingbase);
        EntityVex.this.func_190648_a(false);
      } else {
        double d0 = EntityVex.this.getDistanceSqToEntity((Entity)entitylivingbase);
        if (d0 < 9.0D) {
          Vec3d vec3d = entitylivingbase.getPositionEyes(1.0F);
          EntityVex.this.moveHelper.setMoveTo(vec3d.xCoord, vec3d.yCoord, vec3d.zCoord, 1.0D);
        } 
      } 
    }
  }
  
  class AICopyOwnerTarget extends EntityAITarget {
    public AICopyOwnerTarget(EntityCreature p_i47231_2_) {
      super(p_i47231_2_, false);
    }
    
    public boolean shouldExecute() {
      return (EntityVex.this.field_190665_b != null && EntityVex.this.field_190665_b.getAttackTarget() != null && isSuitableTarget(EntityVex.this.field_190665_b.getAttackTarget(), false));
    }
    
    public void startExecuting() {
      EntityVex.this.setAttackTarget(EntityVex.this.field_190665_b.getAttackTarget());
      super.startExecuting();
    }
  }
  
  class AIMoveControl extends EntityMoveHelper {
    public AIMoveControl(EntityVex p_i47230_2_) {
      super((EntityLiving)p_i47230_2_);
    }
    
    public void onUpdateMoveHelper() {
      if (this.action == EntityMoveHelper.Action.MOVE_TO) {
        double d0 = this.posX - EntityVex.this.posX;
        double d1 = this.posY - EntityVex.this.posY;
        double d2 = this.posZ - EntityVex.this.posZ;
        double d3 = d0 * d0 + d1 * d1 + d2 * d2;
        d3 = MathHelper.sqrt(d3);
        if (d3 < EntityVex.this.getEntityBoundingBox().getAverageEdgeLength()) {
          this.action = EntityMoveHelper.Action.WAIT;
          EntityVex.this.motionX *= 0.5D;
          EntityVex.this.motionY *= 0.5D;
          EntityVex.this.motionZ *= 0.5D;
        } else {
          EntityVex.this.motionX += d0 / d3 * 0.05D * this.speed;
          EntityVex.this.motionY += d1 / d3 * 0.05D * this.speed;
          EntityVex.this.motionZ += d2 / d3 * 0.05D * this.speed;
          if (EntityVex.this.getAttackTarget() == null) {
            EntityVex.this.rotationYaw = -((float)MathHelper.atan2(EntityVex.this.motionX, EntityVex.this.motionZ)) * 57.295776F;
            EntityVex.this.renderYawOffset = EntityVex.this.rotationYaw;
          } else {
            double d4 = (EntityVex.this.getAttackTarget()).posX - EntityVex.this.posX;
            double d5 = (EntityVex.this.getAttackTarget()).posZ - EntityVex.this.posZ;
            EntityVex.this.rotationYaw = -((float)MathHelper.atan2(d4, d5)) * 57.295776F;
            EntityVex.this.renderYawOffset = EntityVex.this.rotationYaw;
          } 
        } 
      } 
    }
  }
  
  class AIMoveRandom extends EntityAIBase {
    public AIMoveRandom() {
      setMutexBits(1);
    }
    
    public boolean shouldExecute() {
      return (!EntityVex.this.getMoveHelper().isUpdating() && EntityVex.this.rand.nextInt(7) == 0);
    }
    
    public boolean continueExecuting() {
      return false;
    }
    
    public void updateTask() {
      BlockPos blockpos = EntityVex.this.func_190646_di();
      if (blockpos == null)
        blockpos = new BlockPos((Entity)EntityVex.this); 
      for (int i = 0; i < 3; i++) {
        BlockPos blockpos1 = blockpos.add(EntityVex.this.rand.nextInt(15) - 7, EntityVex.this.rand.nextInt(11) - 5, EntityVex.this.rand.nextInt(15) - 7);
        if (EntityVex.this.world.isAirBlock(blockpos1)) {
          EntityVex.this.moveHelper.setMoveTo(blockpos1.getX() + 0.5D, blockpos1.getY() + 0.5D, blockpos1.getZ() + 0.5D, 0.25D);
          if (EntityVex.this.getAttackTarget() == null)
            EntityVex.this.getLookHelper().setLookPosition(blockpos1.getX() + 0.5D, blockpos1.getY() + 0.5D, blockpos1.getZ() + 0.5D, 180.0F, 20.0F); 
          break;
        } 
      } 
    }
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\entity\monster\EntityVex.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
package net.minecraft.entity.monster;

import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIFindEntityNearestPlayer;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityGhast extends EntityFlying implements IMob {
  private static final DataParameter<Boolean> ATTACKING = EntityDataManager.createKey(EntityGhast.class, DataSerializers.BOOLEAN);
  
  private int explosionStrength = 1;
  
  public EntityGhast(World worldIn) {
    super(worldIn);
    setSize(4.0F, 4.0F);
    this.isImmuneToFire = true;
    this.experienceValue = 5;
    this.moveHelper = new GhastMoveHelper(this);
  }
  
  protected void initEntityAI() {
    this.tasks.addTask(5, new AIRandomFly(this));
    this.tasks.addTask(7, new AILookAround(this));
    this.tasks.addTask(7, new AIFireballAttack(this));
    this.targetTasks.addTask(1, (EntityAIBase)new EntityAIFindEntityNearestPlayer((EntityLiving)this));
  }
  
  public boolean isAttacking() {
    return ((Boolean)this.dataManager.get(ATTACKING)).booleanValue();
  }
  
  public void setAttacking(boolean attacking) {
    this.dataManager.set(ATTACKING, Boolean.valueOf(attacking));
  }
  
  public int getFireballStrength() {
    return this.explosionStrength;
  }
  
  public void onUpdate() {
    super.onUpdate();
    if (!this.world.isRemote && this.world.getDifficulty() == EnumDifficulty.PEACEFUL)
      setDead(); 
  }
  
  public boolean attackEntityFrom(DamageSource source, float amount) {
    if (isEntityInvulnerable(source))
      return false; 
    if (source.getSourceOfDamage() instanceof EntityLargeFireball && source.getEntity() instanceof net.minecraft.entity.player.EntityPlayer) {
      super.attackEntityFrom(source, 1000.0F);
      return true;
    } 
    return super.attackEntityFrom(source, amount);
  }
  
  protected void entityInit() {
    super.entityInit();
    this.dataManager.register(ATTACKING, Boolean.valueOf(false));
  }
  
  protected void applyEntityAttributes() {
    super.applyEntityAttributes();
    getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(10.0D);
    getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(100.0D);
  }
  
  public SoundCategory getSoundCategory() {
    return SoundCategory.HOSTILE;
  }
  
  protected SoundEvent getAmbientSound() {
    return SoundEvents.ENTITY_GHAST_AMBIENT;
  }
  
  protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
    return SoundEvents.ENTITY_GHAST_HURT;
  }
  
  protected SoundEvent getDeathSound() {
    return SoundEvents.ENTITY_GHAST_DEATH;
  }
  
  @Nullable
  protected ResourceLocation getLootTable() {
    return LootTableList.ENTITIES_GHAST;
  }
  
  protected float getSoundVolume() {
    return 10.0F;
  }
  
  public boolean getCanSpawnHere() {
    return (this.rand.nextInt(20) == 0 && super.getCanSpawnHere() && this.world.getDifficulty() != EnumDifficulty.PEACEFUL);
  }
  
  public int getMaxSpawnedInChunk() {
    return 1;
  }
  
  public static void registerFixesGhast(DataFixer fixer) {
    EntityLiving.registerFixesMob(fixer, EntityGhast.class);
  }
  
  public void writeEntityToNBT(NBTTagCompound compound) {
    super.writeEntityToNBT(compound);
    compound.setInteger("ExplosionPower", this.explosionStrength);
  }
  
  public void readEntityFromNBT(NBTTagCompound compound) {
    super.readEntityFromNBT(compound);
    if (compound.hasKey("ExplosionPower", 99))
      this.explosionStrength = compound.getInteger("ExplosionPower"); 
  }
  
  public float getEyeHeight() {
    return 2.6F;
  }
  
  static class AIFireballAttack extends EntityAIBase {
    private final EntityGhast parentEntity;
    
    public int attackTimer;
    
    public AIFireballAttack(EntityGhast ghast) {
      this.parentEntity = ghast;
    }
    
    public boolean shouldExecute() {
      return (this.parentEntity.getAttackTarget() != null);
    }
    
    public void startExecuting() {
      this.attackTimer = 0;
    }
    
    public void resetTask() {
      this.parentEntity.setAttacking(false);
    }
    
    public void updateTask() {
      EntityLivingBase entitylivingbase = this.parentEntity.getAttackTarget();
      double d0 = 64.0D;
      if (entitylivingbase.getDistanceSqToEntity((Entity)this.parentEntity) < 4096.0D && this.parentEntity.canEntityBeSeen((Entity)entitylivingbase)) {
        World world = this.parentEntity.world;
        this.attackTimer++;
        if (this.attackTimer == 10)
          world.playEvent(null, 1015, new BlockPos((Entity)this.parentEntity), 0); 
        if (this.attackTimer == 20) {
          double d1 = 4.0D;
          Vec3d vec3d = this.parentEntity.getLook(1.0F);
          double d2 = entitylivingbase.posX - this.parentEntity.posX + vec3d.xCoord * 4.0D;
          double d3 = (entitylivingbase.getEntityBoundingBox()).minY + (entitylivingbase.height / 2.0F) - 0.5D + this.parentEntity.posY + (this.parentEntity.height / 2.0F);
          double d4 = entitylivingbase.posZ - this.parentEntity.posZ + vec3d.zCoord * 4.0D;
          world.playEvent(null, 1016, new BlockPos((Entity)this.parentEntity), 0);
          EntityLargeFireball entitylargefireball = new EntityLargeFireball(world, (EntityLivingBase)this.parentEntity, d2, d3, d4);
          entitylargefireball.explosionPower = this.parentEntity.getFireballStrength();
          entitylargefireball.posX = this.parentEntity.posX + vec3d.xCoord * 4.0D;
          entitylargefireball.posY = this.parentEntity.posY + (this.parentEntity.height / 2.0F) + 0.5D;
          entitylargefireball.posZ = this.parentEntity.posZ + vec3d.zCoord * 4.0D;
          world.spawnEntityInWorld((Entity)entitylargefireball);
          this.attackTimer = -40;
        } 
      } else if (this.attackTimer > 0) {
        this.attackTimer--;
      } 
      this.parentEntity.setAttacking((this.attackTimer > 10));
    }
  }
  
  static class AILookAround extends EntityAIBase {
    private final EntityGhast parentEntity;
    
    public AILookAround(EntityGhast ghast) {
      this.parentEntity = ghast;
      setMutexBits(2);
    }
    
    public boolean shouldExecute() {
      return true;
    }
    
    public void updateTask() {
      if (this.parentEntity.getAttackTarget() == null) {
        this.parentEntity.rotationYaw = -((float)MathHelper.atan2(this.parentEntity.motionX, this.parentEntity.motionZ)) * 57.295776F;
        this.parentEntity.renderYawOffset = this.parentEntity.rotationYaw;
      } else {
        EntityLivingBase entitylivingbase = this.parentEntity.getAttackTarget();
        double d0 = 64.0D;
        if (entitylivingbase.getDistanceSqToEntity((Entity)this.parentEntity) < 4096.0D) {
          double d1 = entitylivingbase.posX - this.parentEntity.posX;
          double d2 = entitylivingbase.posZ - this.parentEntity.posZ;
          this.parentEntity.rotationYaw = -((float)MathHelper.atan2(d1, d2)) * 57.295776F;
          this.parentEntity.renderYawOffset = this.parentEntity.rotationYaw;
        } 
      } 
    }
  }
  
  static class AIRandomFly extends EntityAIBase {
    private final EntityGhast parentEntity;
    
    public AIRandomFly(EntityGhast ghast) {
      this.parentEntity = ghast;
      setMutexBits(1);
    }
    
    public boolean shouldExecute() {
      EntityMoveHelper entitymovehelper = this.parentEntity.getMoveHelper();
      if (!entitymovehelper.isUpdating())
        return true; 
      double d0 = entitymovehelper.getX() - this.parentEntity.posX;
      double d1 = entitymovehelper.getY() - this.parentEntity.posY;
      double d2 = entitymovehelper.getZ() - this.parentEntity.posZ;
      double d3 = d0 * d0 + d1 * d1 + d2 * d2;
      return !(d3 >= 1.0D && d3 <= 3600.0D);
    }
    
    public boolean continueExecuting() {
      return false;
    }
    
    public void startExecuting() {
      Random random = this.parentEntity.getRNG();
      double d0 = this.parentEntity.posX + ((random.nextFloat() * 2.0F - 1.0F) * 16.0F);
      double d1 = this.parentEntity.posY + ((random.nextFloat() * 2.0F - 1.0F) * 16.0F);
      double d2 = this.parentEntity.posZ + ((random.nextFloat() * 2.0F - 1.0F) * 16.0F);
      this.parentEntity.getMoveHelper().setMoveTo(d0, d1, d2, 1.0D);
    }
  }
  
  static class GhastMoveHelper extends EntityMoveHelper {
    private final EntityGhast parentEntity;
    
    private int courseChangeCooldown;
    
    public GhastMoveHelper(EntityGhast ghast) {
      super((EntityLiving)ghast);
      this.parentEntity = ghast;
    }
    
    public void onUpdateMoveHelper() {
      if (this.action == EntityMoveHelper.Action.MOVE_TO) {
        double d0 = this.posX - this.parentEntity.posX;
        double d1 = this.posY - this.parentEntity.posY;
        double d2 = this.posZ - this.parentEntity.posZ;
        double d3 = d0 * d0 + d1 * d1 + d2 * d2;
        if (this.courseChangeCooldown-- <= 0) {
          this.courseChangeCooldown += this.parentEntity.getRNG().nextInt(5) + 2;
          d3 = MathHelper.sqrt(d3);
          if (isNotColliding(this.posX, this.posY, this.posZ, d3)) {
            this.parentEntity.motionX += d0 / d3 * 0.1D;
            this.parentEntity.motionY += d1 / d3 * 0.1D;
            this.parentEntity.motionZ += d2 / d3 * 0.1D;
          } else {
            this.action = EntityMoveHelper.Action.WAIT;
          } 
        } 
      } 
    }
    
    private boolean isNotColliding(double x, double y, double z, double p_179926_7_) {
      double d0 = (x - this.parentEntity.posX) / p_179926_7_;
      double d1 = (y - this.parentEntity.posY) / p_179926_7_;
      double d2 = (z - this.parentEntity.posZ) / p_179926_7_;
      AxisAlignedBB axisalignedbb = this.parentEntity.getEntityBoundingBox();
      for (int i = 1; i < p_179926_7_; i++) {
        axisalignedbb = axisalignedbb.offset(d0, d1, d2);
        if (!this.parentEntity.world.getCollisionBoxes((Entity)this.parentEntity, axisalignedbb).isEmpty())
          return false; 
      } 
      return true;
    }
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\entity\monster\EntityGhast.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
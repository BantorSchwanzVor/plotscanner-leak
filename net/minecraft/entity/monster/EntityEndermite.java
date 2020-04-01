package net.minecraft.entity.monster;

import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityEndermite extends EntityMob {
  private int lifetime;
  
  private boolean playerSpawned;
  
  public EntityEndermite(World worldIn) {
    super(worldIn);
    this.experienceValue = 3;
    setSize(0.4F, 0.3F);
  }
  
  protected void initEntityAI() {
    this.tasks.addTask(1, (EntityAIBase)new EntityAISwimming((EntityLiving)this));
    this.tasks.addTask(2, (EntityAIBase)new EntityAIAttackMelee(this, 1.0D, false));
    this.tasks.addTask(3, (EntityAIBase)new EntityAIWanderAvoidWater(this, 1.0D));
    this.tasks.addTask(7, (EntityAIBase)new EntityAIWatchClosest((EntityLiving)this, EntityPlayer.class, 8.0F));
    this.tasks.addTask(8, (EntityAIBase)new EntityAILookIdle((EntityLiving)this));
    this.targetTasks.addTask(1, (EntityAIBase)new EntityAIHurtByTarget(this, true, new Class[0]));
    this.targetTasks.addTask(2, (EntityAIBase)new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
  }
  
  public float getEyeHeight() {
    return 0.1F;
  }
  
  protected void applyEntityAttributes() {
    super.applyEntityAttributes();
    getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(8.0D);
    getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
    getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(2.0D);
  }
  
  protected boolean canTriggerWalking() {
    return false;
  }
  
  protected SoundEvent getAmbientSound() {
    return SoundEvents.ENTITY_ENDERMITE_AMBIENT;
  }
  
  protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
    return SoundEvents.ENTITY_ENDERMITE_HURT;
  }
  
  protected SoundEvent getDeathSound() {
    return SoundEvents.ENTITY_ENDERMITE_DEATH;
  }
  
  protected void playStepSound(BlockPos pos, Block blockIn) {
    playSound(SoundEvents.ENTITY_ENDERMITE_STEP, 0.15F, 1.0F);
  }
  
  @Nullable
  protected ResourceLocation getLootTable() {
    return LootTableList.ENTITIES_ENDERMITE;
  }
  
  public static void registerFixesEndermite(DataFixer fixer) {
    EntityLiving.registerFixesMob(fixer, EntityEndermite.class);
  }
  
  public void readEntityFromNBT(NBTTagCompound compound) {
    super.readEntityFromNBT(compound);
    this.lifetime = compound.getInteger("Lifetime");
    this.playerSpawned = compound.getBoolean("PlayerSpawned");
  }
  
  public void writeEntityToNBT(NBTTagCompound compound) {
    super.writeEntityToNBT(compound);
    compound.setInteger("Lifetime", this.lifetime);
    compound.setBoolean("PlayerSpawned", this.playerSpawned);
  }
  
  public void onUpdate() {
    this.renderYawOffset = this.rotationYaw;
    super.onUpdate();
  }
  
  public void setRenderYawOffset(float offset) {
    this.rotationYaw = offset;
    super.setRenderYawOffset(offset);
  }
  
  public double getYOffset() {
    return 0.1D;
  }
  
  public boolean isSpawnedByPlayer() {
    return this.playerSpawned;
  }
  
  public void setSpawnedByPlayer(boolean spawnedByPlayer) {
    this.playerSpawned = spawnedByPlayer;
  }
  
  public void onLivingUpdate() {
    super.onLivingUpdate();
    if (this.world.isRemote) {
      for (int i = 0; i < 2; i++)
        this.world.spawnParticle(EnumParticleTypes.PORTAL, this.posX + (this.rand.nextDouble() - 0.5D) * this.width, this.posY + this.rand.nextDouble() * this.height, this.posZ + (this.rand.nextDouble() - 0.5D) * this.width, (this.rand.nextDouble() - 0.5D) * 2.0D, -this.rand.nextDouble(), (this.rand.nextDouble() - 0.5D) * 2.0D, new int[0]); 
    } else {
      if (!isNoDespawnRequired())
        this.lifetime++; 
      if (this.lifetime >= 2400)
        setDead(); 
    } 
  }
  
  protected boolean isValidLightLevel() {
    return true;
  }
  
  public boolean getCanSpawnHere() {
    if (super.getCanSpawnHere()) {
      EntityPlayer entityplayer = this.world.getClosestPlayerToEntity((Entity)this, 5.0D);
      return (entityplayer == null);
    } 
    return false;
  }
  
  public EnumCreatureAttribute getCreatureAttribute() {
    return EnumCreatureAttribute.ARTHROPOD;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\entity\monster\EntityEndermite.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
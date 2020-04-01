package net.minecraft.entity.monster;

import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityPigZombie extends EntityZombie {
  private static final UUID ATTACK_SPEED_BOOST_MODIFIER_UUID = UUID.fromString("49455A49-7EC5-45BA-B886-3B90B23A1718");
  
  private static final AttributeModifier ATTACK_SPEED_BOOST_MODIFIER = (new AttributeModifier(ATTACK_SPEED_BOOST_MODIFIER_UUID, "Attacking speed boost", 0.05D, 0)).setSaved(false);
  
  private int angerLevel;
  
  private int randomSoundDelay;
  
  private UUID angerTargetUUID;
  
  public EntityPigZombie(World worldIn) {
    super(worldIn);
    this.isImmuneToFire = true;
  }
  
  public void setRevengeTarget(@Nullable EntityLivingBase livingBase) {
    super.setRevengeTarget(livingBase);
    if (livingBase != null)
      this.angerTargetUUID = livingBase.getUniqueID(); 
  }
  
  protected void applyEntityAI() {
    this.targetTasks.addTask(1, (EntityAIBase)new AIHurtByAggressor(this));
    this.targetTasks.addTask(2, (EntityAIBase)new AITargetAggressor(this));
  }
  
  protected void applyEntityAttributes() {
    super.applyEntityAttributes();
    getEntityAttribute(SPAWN_REINFORCEMENTS_CHANCE).setBaseValue(0.0D);
    getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.23000000417232513D);
    getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(5.0D);
  }
  
  protected void updateAITasks() {
    IAttributeInstance iattributeinstance = getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
    if (isAngry()) {
      if (!isChild() && !iattributeinstance.hasModifier(ATTACK_SPEED_BOOST_MODIFIER))
        iattributeinstance.applyModifier(ATTACK_SPEED_BOOST_MODIFIER); 
      this.angerLevel--;
    } else if (iattributeinstance.hasModifier(ATTACK_SPEED_BOOST_MODIFIER)) {
      iattributeinstance.removeModifier(ATTACK_SPEED_BOOST_MODIFIER);
    } 
    if (this.randomSoundDelay > 0 && --this.randomSoundDelay == 0)
      playSound(SoundEvents.ENTITY_ZOMBIE_PIG_ANGRY, getSoundVolume() * 2.0F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F) * 1.8F); 
    if (this.angerLevel > 0 && this.angerTargetUUID != null && getAITarget() == null) {
      EntityPlayer entityplayer = this.world.getPlayerEntityByUUID(this.angerTargetUUID);
      setRevengeTarget((EntityLivingBase)entityplayer);
      this.attackingPlayer = entityplayer;
      this.recentlyHit = getRevengeTimer();
    } 
    super.updateAITasks();
  }
  
  public boolean getCanSpawnHere() {
    return (this.world.getDifficulty() != EnumDifficulty.PEACEFUL);
  }
  
  public boolean isNotColliding() {
    return (this.world.checkNoEntityCollision(getEntityBoundingBox(), (Entity)this) && this.world.getCollisionBoxes((Entity)this, getEntityBoundingBox()).isEmpty() && !this.world.containsAnyLiquid(getEntityBoundingBox()));
  }
  
  public static void registerFixesPigZombie(DataFixer fixer) {
    EntityLiving.registerFixesMob(fixer, EntityPigZombie.class);
  }
  
  public void writeEntityToNBT(NBTTagCompound compound) {
    super.writeEntityToNBT(compound);
    compound.setShort("Anger", (short)this.angerLevel);
    if (this.angerTargetUUID != null) {
      compound.setString("HurtBy", this.angerTargetUUID.toString());
    } else {
      compound.setString("HurtBy", "");
    } 
  }
  
  public void readEntityFromNBT(NBTTagCompound compound) {
    super.readEntityFromNBT(compound);
    this.angerLevel = compound.getShort("Anger");
    String s = compound.getString("HurtBy");
    if (!s.isEmpty()) {
      this.angerTargetUUID = UUID.fromString(s);
      EntityPlayer entityplayer = this.world.getPlayerEntityByUUID(this.angerTargetUUID);
      setRevengeTarget((EntityLivingBase)entityplayer);
      if (entityplayer != null) {
        this.attackingPlayer = entityplayer;
        this.recentlyHit = getRevengeTimer();
      } 
    } 
  }
  
  public boolean attackEntityFrom(DamageSource source, float amount) {
    if (isEntityInvulnerable(source))
      return false; 
    Entity entity = source.getEntity();
    if (entity instanceof EntityPlayer)
      becomeAngryAt(entity); 
    return super.attackEntityFrom(source, amount);
  }
  
  private void becomeAngryAt(Entity p_70835_1_) {
    this.angerLevel = 400 + this.rand.nextInt(400);
    this.randomSoundDelay = this.rand.nextInt(40);
    if (p_70835_1_ instanceof EntityLivingBase)
      setRevengeTarget((EntityLivingBase)p_70835_1_); 
  }
  
  public boolean isAngry() {
    return (this.angerLevel > 0);
  }
  
  protected SoundEvent getAmbientSound() {
    return SoundEvents.ENTITY_ZOMBIE_PIG_AMBIENT;
  }
  
  protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
    return SoundEvents.ENTITY_ZOMBIE_PIG_HURT;
  }
  
  protected SoundEvent getDeathSound() {
    return SoundEvents.ENTITY_ZOMBIE_PIG_DEATH;
  }
  
  @Nullable
  protected ResourceLocation getLootTable() {
    return LootTableList.ENTITIES_ZOMBIE_PIGMAN;
  }
  
  public boolean processInteract(EntityPlayer player, EnumHand hand) {
    return false;
  }
  
  protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
    setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.GOLDEN_SWORD));
  }
  
  protected ItemStack func_190732_dj() {
    return ItemStack.field_190927_a;
  }
  
  public boolean func_191990_c(EntityPlayer p_191990_1_) {
    return isAngry();
  }
  
  static class AIHurtByAggressor extends EntityAIHurtByTarget {
    public AIHurtByAggressor(EntityPigZombie p_i45828_1_) {
      super(p_i45828_1_, true, new Class[0]);
    }
    
    protected void setEntityAttackTarget(EntityCreature creatureIn, EntityLivingBase entityLivingBaseIn) {
      super.setEntityAttackTarget(creatureIn, entityLivingBaseIn);
      if (creatureIn instanceof EntityPigZombie)
        ((EntityPigZombie)creatureIn).becomeAngryAt((Entity)entityLivingBaseIn); 
    }
  }
  
  static class AITargetAggressor extends EntityAINearestAttackableTarget<EntityPlayer> {
    public AITargetAggressor(EntityPigZombie p_i45829_1_) {
      super(p_i45829_1_, EntityPlayer.class, true);
    }
    
    public boolean shouldExecute() {
      return (((EntityPigZombie)this.taskOwner).isAngry() && super.shouldExecute());
    }
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\entity\monster\EntityPigZombie.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
package net.minecraft.entity.monster;

import java.util.Collection;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAreaEffectCloud;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAICreeperSwell;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityCreeper extends EntityMob {
  private static final DataParameter<Integer> STATE = EntityDataManager.createKey(EntityCreeper.class, DataSerializers.VARINT);
  
  private static final DataParameter<Boolean> POWERED = EntityDataManager.createKey(EntityCreeper.class, DataSerializers.BOOLEAN);
  
  private static final DataParameter<Boolean> IGNITED = EntityDataManager.createKey(EntityCreeper.class, DataSerializers.BOOLEAN);
  
  private int lastActiveTime;
  
  private int timeSinceIgnited;
  
  private int fuseTime = 30;
  
  private int explosionRadius = 3;
  
  private int droppedSkulls;
  
  public EntityCreeper(World worldIn) {
    super(worldIn);
    setSize(0.6F, 1.7F);
  }
  
  protected void initEntityAI() {
    this.tasks.addTask(1, (EntityAIBase)new EntityAISwimming((EntityLiving)this));
    this.tasks.addTask(2, (EntityAIBase)new EntityAICreeperSwell(this));
    this.tasks.addTask(3, (EntityAIBase)new EntityAIAvoidEntity(this, EntityOcelot.class, 6.0F, 1.0D, 1.2D));
    this.tasks.addTask(4, (EntityAIBase)new EntityAIAttackMelee(this, 1.0D, false));
    this.tasks.addTask(5, (EntityAIBase)new EntityAIWanderAvoidWater(this, 0.8D));
    this.tasks.addTask(6, (EntityAIBase)new EntityAIWatchClosest((EntityLiving)this, EntityPlayer.class, 8.0F));
    this.tasks.addTask(6, (EntityAIBase)new EntityAILookIdle((EntityLiving)this));
    this.targetTasks.addTask(1, (EntityAIBase)new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
    this.targetTasks.addTask(2, (EntityAIBase)new EntityAIHurtByTarget(this, false, new Class[0]));
  }
  
  protected void applyEntityAttributes() {
    super.applyEntityAttributes();
    getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
  }
  
  public int getMaxFallHeight() {
    return (getAttackTarget() == null) ? 3 : (3 + (int)(getHealth() - 1.0F));
  }
  
  public void fall(float distance, float damageMultiplier) {
    super.fall(distance, damageMultiplier);
    this.timeSinceIgnited = (int)(this.timeSinceIgnited + distance * 1.5F);
    if (this.timeSinceIgnited > this.fuseTime - 5)
      this.timeSinceIgnited = this.fuseTime - 5; 
  }
  
  protected void entityInit() {
    super.entityInit();
    this.dataManager.register(STATE, Integer.valueOf(-1));
    this.dataManager.register(POWERED, Boolean.valueOf(false));
    this.dataManager.register(IGNITED, Boolean.valueOf(false));
  }
  
  public static void registerFixesCreeper(DataFixer fixer) {
    EntityLiving.registerFixesMob(fixer, EntityCreeper.class);
  }
  
  public void writeEntityToNBT(NBTTagCompound compound) {
    super.writeEntityToNBT(compound);
    if (((Boolean)this.dataManager.get(POWERED)).booleanValue())
      compound.setBoolean("powered", true); 
    compound.setShort("Fuse", (short)this.fuseTime);
    compound.setByte("ExplosionRadius", (byte)this.explosionRadius);
    compound.setBoolean("ignited", hasIgnited());
  }
  
  public void readEntityFromNBT(NBTTagCompound compound) {
    super.readEntityFromNBT(compound);
    this.dataManager.set(POWERED, Boolean.valueOf(compound.getBoolean("powered")));
    if (compound.hasKey("Fuse", 99))
      this.fuseTime = compound.getShort("Fuse"); 
    if (compound.hasKey("ExplosionRadius", 99))
      this.explosionRadius = compound.getByte("ExplosionRadius"); 
    if (compound.getBoolean("ignited"))
      ignite(); 
  }
  
  public void onUpdate() {
    if (isEntityAlive()) {
      this.lastActiveTime = this.timeSinceIgnited;
      if (hasIgnited())
        setCreeperState(1); 
      int i = getCreeperState();
      if (i > 0 && this.timeSinceIgnited == 0)
        playSound(SoundEvents.ENTITY_CREEPER_PRIMED, 1.0F, 0.5F); 
      this.timeSinceIgnited += i;
      if (this.timeSinceIgnited < 0)
        this.timeSinceIgnited = 0; 
      if (this.timeSinceIgnited >= this.fuseTime) {
        this.timeSinceIgnited = this.fuseTime;
        explode();
      } 
    } 
    super.onUpdate();
  }
  
  protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
    return SoundEvents.ENTITY_CREEPER_HURT;
  }
  
  protected SoundEvent getDeathSound() {
    return SoundEvents.ENTITY_CREEPER_DEATH;
  }
  
  public void onDeath(DamageSource cause) {
    super.onDeath(cause);
    if (this.world.getGameRules().getBoolean("doMobLoot"))
      if (cause.getEntity() instanceof EntitySkeleton) {
        int i = Item.getIdFromItem(Items.RECORD_13);
        int j = Item.getIdFromItem(Items.RECORD_WAIT);
        int k = i + this.rand.nextInt(j - i + 1);
        dropItem(Item.getItemById(k), 1);
      } else if (cause.getEntity() instanceof EntityCreeper && cause.getEntity() != this && ((EntityCreeper)cause.getEntity()).getPowered() && ((EntityCreeper)cause.getEntity()).isAIEnabled()) {
        ((EntityCreeper)cause.getEntity()).incrementDroppedSkulls();
        entityDropItem(new ItemStack(Items.SKULL, 1, 4), 0.0F);
      }  
  }
  
  public boolean attackEntityAsMob(Entity entityIn) {
    return true;
  }
  
  public boolean getPowered() {
    return ((Boolean)this.dataManager.get(POWERED)).booleanValue();
  }
  
  public float getCreeperFlashIntensity(float p_70831_1_) {
    return (this.lastActiveTime + (this.timeSinceIgnited - this.lastActiveTime) * p_70831_1_) / (this.fuseTime - 2);
  }
  
  @Nullable
  protected ResourceLocation getLootTable() {
    return LootTableList.ENTITIES_CREEPER;
  }
  
  public int getCreeperState() {
    return ((Integer)this.dataManager.get(STATE)).intValue();
  }
  
  public void setCreeperState(int state) {
    this.dataManager.set(STATE, Integer.valueOf(state));
  }
  
  public void onStruckByLightning(EntityLightningBolt lightningBolt) {
    super.onStruckByLightning(lightningBolt);
    this.dataManager.set(POWERED, Boolean.valueOf(true));
  }
  
  protected boolean processInteract(EntityPlayer player, EnumHand hand) {
    ItemStack itemstack = player.getHeldItem(hand);
    if (itemstack.getItem() == Items.FLINT_AND_STEEL) {
      this.world.playSound(player, this.posX, this.posY, this.posZ, SoundEvents.ITEM_FLINTANDSTEEL_USE, getSoundCategory(), 1.0F, this.rand.nextFloat() * 0.4F + 0.8F);
      player.swingArm(hand);
      if (!this.world.isRemote) {
        ignite();
        itemstack.damageItem(1, (EntityLivingBase)player);
        return true;
      } 
    } 
    return super.processInteract(player, hand);
  }
  
  private void explode() {
    if (!this.world.isRemote) {
      boolean flag = this.world.getGameRules().getBoolean("mobGriefing");
      float f = getPowered() ? 2.0F : 1.0F;
      this.dead = true;
      this.world.createExplosion((Entity)this, this.posX, this.posY, this.posZ, this.explosionRadius * f, flag);
      setDead();
      func_190741_do();
    } 
  }
  
  private void func_190741_do() {
    Collection<PotionEffect> collection = getActivePotionEffects();
    if (!collection.isEmpty()) {
      EntityAreaEffectCloud entityareaeffectcloud = new EntityAreaEffectCloud(this.world, this.posX, this.posY, this.posZ);
      entityareaeffectcloud.setRadius(2.5F);
      entityareaeffectcloud.setRadiusOnUse(-0.5F);
      entityareaeffectcloud.setWaitTime(10);
      entityareaeffectcloud.setDuration(entityareaeffectcloud.getDuration() / 2);
      entityareaeffectcloud.setRadiusPerTick(-entityareaeffectcloud.getRadius() / entityareaeffectcloud.getDuration());
      for (PotionEffect potioneffect : collection)
        entityareaeffectcloud.addEffect(new PotionEffect(potioneffect)); 
      this.world.spawnEntityInWorld((Entity)entityareaeffectcloud);
    } 
  }
  
  public boolean hasIgnited() {
    return ((Boolean)this.dataManager.get(IGNITED)).booleanValue();
  }
  
  public void ignite() {
    this.dataManager.set(IGNITED, Boolean.valueOf(true));
  }
  
  public boolean isAIEnabled() {
    return (this.droppedSkulls < 1 && this.world.getGameRules().getBoolean("doMobLoot"));
  }
  
  public void incrementDroppedSkulls() {
    this.droppedSkulls++;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\entity\monster\EntityCreeper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
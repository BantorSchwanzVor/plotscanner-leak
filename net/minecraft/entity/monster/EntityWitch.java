package net.minecraft.entity.monster;

import java.util.List;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackRanged;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.PotionTypes;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityWitch extends EntityMob implements IRangedAttackMob {
  private static final UUID MODIFIER_UUID = UUID.fromString("5CD17E52-A79A-43D3-A529-90FDE04B181E");
  
  private static final AttributeModifier MODIFIER = (new AttributeModifier(MODIFIER_UUID, "Drinking speed penalty", -0.25D, 0)).setSaved(false);
  
  private static final DataParameter<Boolean> IS_AGGRESSIVE = EntityDataManager.createKey(EntityWitch.class, DataSerializers.BOOLEAN);
  
  private int witchAttackTimer;
  
  public EntityWitch(World worldIn) {
    super(worldIn);
    setSize(0.6F, 1.95F);
  }
  
  public static void registerFixesWitch(DataFixer fixer) {
    EntityLiving.registerFixesMob(fixer, EntityWitch.class);
  }
  
  protected void initEntityAI() {
    this.tasks.addTask(1, (EntityAIBase)new EntityAISwimming((EntityLiving)this));
    this.tasks.addTask(2, (EntityAIBase)new EntityAIAttackRanged(this, 1.0D, 60, 10.0F));
    this.tasks.addTask(2, (EntityAIBase)new EntityAIWanderAvoidWater(this, 1.0D));
    this.tasks.addTask(3, (EntityAIBase)new EntityAIWatchClosest((EntityLiving)this, EntityPlayer.class, 8.0F));
    this.tasks.addTask(3, (EntityAIBase)new EntityAILookIdle((EntityLiving)this));
    this.targetTasks.addTask(1, (EntityAIBase)new EntityAIHurtByTarget(this, false, new Class[0]));
    this.targetTasks.addTask(2, (EntityAIBase)new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
  }
  
  protected void entityInit() {
    super.entityInit();
    getDataManager().register(IS_AGGRESSIVE, Boolean.valueOf(false));
  }
  
  protected SoundEvent getAmbientSound() {
    return SoundEvents.ENTITY_WITCH_AMBIENT;
  }
  
  protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
    return SoundEvents.ENTITY_WITCH_HURT;
  }
  
  protected SoundEvent getDeathSound() {
    return SoundEvents.ENTITY_WITCH_DEATH;
  }
  
  public void setAggressive(boolean aggressive) {
    getDataManager().set(IS_AGGRESSIVE, Boolean.valueOf(aggressive));
  }
  
  public boolean isDrinkingPotion() {
    return ((Boolean)getDataManager().get(IS_AGGRESSIVE)).booleanValue();
  }
  
  protected void applyEntityAttributes() {
    super.applyEntityAttributes();
    getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(26.0D);
    getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
  }
  
  public void onLivingUpdate() {
    if (!this.world.isRemote) {
      if (isDrinkingPotion()) {
        if (this.witchAttackTimer-- <= 0) {
          setAggressive(false);
          ItemStack itemstack = getHeldItemMainhand();
          setItemStackToSlot(EntityEquipmentSlot.MAINHAND, ItemStack.field_190927_a);
          if (itemstack.getItem() == Items.POTIONITEM) {
            List<PotionEffect> list = PotionUtils.getEffectsFromStack(itemstack);
            if (list != null)
              for (PotionEffect potioneffect : list)
                addPotionEffect(new PotionEffect(potioneffect));  
          } 
          getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).removeModifier(MODIFIER);
        } 
      } else {
        PotionType potiontype = null;
        if (this.rand.nextFloat() < 0.15F && isInsideOfMaterial(Material.WATER) && !isPotionActive(MobEffects.WATER_BREATHING)) {
          potiontype = PotionTypes.WATER_BREATHING;
        } else if (this.rand.nextFloat() < 0.15F && (isBurning() || (getLastDamageSource() != null && getLastDamageSource().isFireDamage())) && !isPotionActive(MobEffects.FIRE_RESISTANCE)) {
          potiontype = PotionTypes.FIRE_RESISTANCE;
        } else if (this.rand.nextFloat() < 0.05F && getHealth() < getMaxHealth()) {
          potiontype = PotionTypes.HEALING;
        } else if (this.rand.nextFloat() < 0.5F && getAttackTarget() != null && !isPotionActive(MobEffects.SPEED) && getAttackTarget().getDistanceSqToEntity((Entity)this) > 121.0D) {
          potiontype = PotionTypes.SWIFTNESS;
        } 
        if (potiontype != null) {
          setItemStackToSlot(EntityEquipmentSlot.MAINHAND, PotionUtils.addPotionToItemStack(new ItemStack((Item)Items.POTIONITEM), potiontype));
          this.witchAttackTimer = getHeldItemMainhand().getMaxItemUseDuration();
          setAggressive(true);
          this.world.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_WITCH_DRINK, getSoundCategory(), 1.0F, 0.8F + this.rand.nextFloat() * 0.4F);
          IAttributeInstance iattributeinstance = getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
          iattributeinstance.removeModifier(MODIFIER);
          iattributeinstance.applyModifier(MODIFIER);
        } 
      } 
      if (this.rand.nextFloat() < 7.5E-4F)
        this.world.setEntityState((Entity)this, (byte)15); 
    } 
    super.onLivingUpdate();
  }
  
  public void handleStatusUpdate(byte id) {
    if (id == 15) {
      for (int i = 0; i < this.rand.nextInt(35) + 10; i++)
        this.world.spawnParticle(EnumParticleTypes.SPELL_WITCH, this.posX + this.rand.nextGaussian() * 0.12999999523162842D, (getEntityBoundingBox()).maxY + 0.5D + this.rand.nextGaussian() * 0.12999999523162842D, this.posZ + this.rand.nextGaussian() * 0.12999999523162842D, 0.0D, 0.0D, 0.0D, new int[0]); 
    } else {
      super.handleStatusUpdate(id);
    } 
  }
  
  protected float applyPotionDamageCalculations(DamageSource source, float damage) {
    damage = super.applyPotionDamageCalculations(source, damage);
    if (source.getEntity() == this)
      damage = 0.0F; 
    if (source.isMagicDamage())
      damage = (float)(damage * 0.15D); 
    return damage;
  }
  
  @Nullable
  protected ResourceLocation getLootTable() {
    return LootTableList.ENTITIES_WITCH;
  }
  
  public void attackEntityWithRangedAttack(EntityLivingBase target, float distanceFactor) {
    if (!isDrinkingPotion()) {
      double d0 = target.posY + target.getEyeHeight() - 1.100000023841858D;
      double d1 = target.posX + target.motionX - this.posX;
      double d2 = d0 - this.posY;
      double d3 = target.posZ + target.motionZ - this.posZ;
      float f = MathHelper.sqrt(d1 * d1 + d3 * d3);
      PotionType potiontype = PotionTypes.HARMING;
      if (f >= 8.0F && !target.isPotionActive(MobEffects.SLOWNESS)) {
        potiontype = PotionTypes.SLOWNESS;
      } else if (target.getHealth() >= 8.0F && !target.isPotionActive(MobEffects.POISON)) {
        potiontype = PotionTypes.POISON;
      } else if (f <= 3.0F && !target.isPotionActive(MobEffects.WEAKNESS) && this.rand.nextFloat() < 0.25F) {
        potiontype = PotionTypes.WEAKNESS;
      } 
      EntityPotion entitypotion = new EntityPotion(this.world, (EntityLivingBase)this, PotionUtils.addPotionToItemStack(new ItemStack((Item)Items.SPLASH_POTION), potiontype));
      entitypotion.rotationPitch -= -20.0F;
      entitypotion.setThrowableHeading(d1, d2 + (f * 0.2F), d3, 0.75F, 8.0F);
      this.world.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_WITCH_THROW, getSoundCategory(), 1.0F, 0.8F + this.rand.nextFloat() * 0.4F);
      this.world.spawnEntityInWorld((Entity)entitypotion);
    } 
  }
  
  public float getEyeHeight() {
    return 1.62F;
  }
  
  public void setSwingingArms(boolean swingingArms) {}
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\entity\monster\EntityWitch.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
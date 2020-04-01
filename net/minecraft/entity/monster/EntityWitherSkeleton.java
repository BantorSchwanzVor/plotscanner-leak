package net.minecraft.entity.monster;

import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityWitherSkeleton extends AbstractSkeleton {
  public EntityWitherSkeleton(World p_i47278_1_) {
    super(p_i47278_1_);
    setSize(0.7F, 2.4F);
    this.isImmuneToFire = true;
  }
  
  public static void func_190729_b(DataFixer p_190729_0_) {
    EntityLiving.registerFixesMob(p_190729_0_, EntityWitherSkeleton.class);
  }
  
  @Nullable
  protected ResourceLocation getLootTable() {
    return LootTableList.ENTITIES_WITHER_SKELETON;
  }
  
  protected SoundEvent getAmbientSound() {
    return SoundEvents.ENTITY_WITHER_SKELETON_AMBIENT;
  }
  
  protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
    return SoundEvents.ENTITY_WITHER_SKELETON_HURT;
  }
  
  protected SoundEvent getDeathSound() {
    return SoundEvents.ENTITY_WITHER_SKELETON_DEATH;
  }
  
  SoundEvent func_190727_o() {
    return SoundEvents.ENTITY_WITHER_SKELETON_STEP;
  }
  
  public void onDeath(DamageSource cause) {
    super.onDeath(cause);
    if (cause.getEntity() instanceof EntityCreeper) {
      EntityCreeper entitycreeper = (EntityCreeper)cause.getEntity();
      if (entitycreeper.getPowered() && entitycreeper.isAIEnabled()) {
        entitycreeper.incrementDroppedSkulls();
        entityDropItem(new ItemStack(Items.SKULL, 1, 1), 0.0F);
      } 
    } 
  }
  
  protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
    setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.STONE_SWORD));
  }
  
  protected void setEnchantmentBasedOnDifficulty(DifficultyInstance difficulty) {}
  
  @Nullable
  public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
    IEntityLivingData ientitylivingdata = super.onInitialSpawn(difficulty, livingdata);
    getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4.0D);
    setCombatTask();
    return ientitylivingdata;
  }
  
  public float getEyeHeight() {
    return 2.1F;
  }
  
  public boolean attackEntityAsMob(Entity entityIn) {
    if (!super.attackEntityAsMob(entityIn))
      return false; 
    if (entityIn instanceof EntityLivingBase)
      ((EntityLivingBase)entityIn).addPotionEffect(new PotionEffect(MobEffects.WITHER, 200)); 
    return true;
  }
  
  protected EntityArrow func_190726_a(float p_190726_1_) {
    EntityArrow entityarrow = super.func_190726_a(p_190726_1_);
    entityarrow.setFire(100);
    return entityarrow;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\entity\monster\EntityWitherSkeleton.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
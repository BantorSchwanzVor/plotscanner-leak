package net.minecraft.entity.monster;

import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityMagmaCube extends EntitySlime {
  public EntityMagmaCube(World worldIn) {
    super(worldIn);
    this.isImmuneToFire = true;
  }
  
  public static void registerFixesMagmaCube(DataFixer fixer) {
    EntityLiving.registerFixesMob(fixer, EntityMagmaCube.class);
  }
  
  protected void applyEntityAttributes() {
    super.applyEntityAttributes();
    getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.20000000298023224D);
  }
  
  public boolean getCanSpawnHere() {
    return (this.world.getDifficulty() != EnumDifficulty.PEACEFUL);
  }
  
  public boolean isNotColliding() {
    return (this.world.checkNoEntityCollision(getEntityBoundingBox(), (Entity)this) && this.world.getCollisionBoxes((Entity)this, getEntityBoundingBox()).isEmpty() && !this.world.containsAnyLiquid(getEntityBoundingBox()));
  }
  
  protected void setSlimeSize(int size, boolean p_70799_2_) {
    super.setSlimeSize(size, p_70799_2_);
    getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue((size * 3));
  }
  
  public int getBrightnessForRender() {
    return 15728880;
  }
  
  public float getBrightness() {
    return 1.0F;
  }
  
  protected EnumParticleTypes getParticleType() {
    return EnumParticleTypes.FLAME;
  }
  
  protected EntitySlime createInstance() {
    return new EntityMagmaCube(this.world);
  }
  
  @Nullable
  protected ResourceLocation getLootTable() {
    return isSmallSlime() ? LootTableList.EMPTY : LootTableList.ENTITIES_MAGMA_CUBE;
  }
  
  public boolean isBurning() {
    return false;
  }
  
  protected int getJumpDelay() {
    return super.getJumpDelay() * 4;
  }
  
  protected void alterSquishAmount() {
    this.squishAmount *= 0.9F;
  }
  
  protected void jump() {
    this.motionY = (0.42F + getSlimeSize() * 0.1F);
    this.isAirBorne = true;
  }
  
  protected void handleJumpLava() {
    this.motionY = (0.22F + getSlimeSize() * 0.05F);
    this.isAirBorne = true;
  }
  
  public void fall(float distance, float damageMultiplier) {}
  
  protected boolean canDamagePlayer() {
    return true;
  }
  
  protected int getAttackStrength() {
    return super.getAttackStrength() + 2;
  }
  
  protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
    return isSmallSlime() ? SoundEvents.ENTITY_SMALL_MAGMACUBE_HURT : SoundEvents.ENTITY_MAGMACUBE_HURT;
  }
  
  protected SoundEvent getDeathSound() {
    return isSmallSlime() ? SoundEvents.ENTITY_SMALL_MAGMACUBE_DEATH : SoundEvents.ENTITY_MAGMACUBE_DEATH;
  }
  
  protected SoundEvent getSquishSound() {
    return isSmallSlime() ? SoundEvents.ENTITY_SMALL_MAGMACUBE_SQUISH : SoundEvents.ENTITY_MAGMACUBE_SQUISH;
  }
  
  protected SoundEvent getJumpSound() {
    return SoundEvents.ENTITY_MAGMACUBE_JUMP;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\entity\monster\EntityMagmaCube.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
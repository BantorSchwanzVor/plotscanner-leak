package net.minecraft.entity.monster;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;

public abstract class EntityMob extends EntityCreature implements IMob {
  public EntityMob(World worldIn) {
    super(worldIn);
    this.experienceValue = 5;
  }
  
  public SoundCategory getSoundCategory() {
    return SoundCategory.HOSTILE;
  }
  
  public void onLivingUpdate() {
    updateArmSwingProgress();
    float f = getBrightness();
    if (f > 0.5F)
      this.entityAge += 2; 
    super.onLivingUpdate();
  }
  
  public void onUpdate() {
    super.onUpdate();
    if (!this.world.isRemote && this.world.getDifficulty() == EnumDifficulty.PEACEFUL)
      setDead(); 
  }
  
  protected SoundEvent getSwimSound() {
    return SoundEvents.ENTITY_HOSTILE_SWIM;
  }
  
  protected SoundEvent getSplashSound() {
    return SoundEvents.ENTITY_HOSTILE_SPLASH;
  }
  
  public boolean attackEntityFrom(DamageSource source, float amount) {
    return isEntityInvulnerable(source) ? false : super.attackEntityFrom(source, amount);
  }
  
  protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
    return SoundEvents.ENTITY_HOSTILE_HURT;
  }
  
  protected SoundEvent getDeathSound() {
    return SoundEvents.ENTITY_HOSTILE_DEATH;
  }
  
  protected SoundEvent getFallSound(int heightIn) {
    return (heightIn > 4) ? SoundEvents.ENTITY_HOSTILE_BIG_FALL : SoundEvents.ENTITY_HOSTILE_SMALL_FALL;
  }
  
  public boolean attackEntityAsMob(Entity entityIn) {
    float f = (float)getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
    int i = 0;
    if (entityIn instanceof EntityLivingBase) {
      f += EnchantmentHelper.getModifierForCreature(getHeldItemMainhand(), ((EntityLivingBase)entityIn).getCreatureAttribute());
      i += EnchantmentHelper.getKnockbackModifier((EntityLivingBase)this);
    } 
    boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage((EntityLivingBase)this), f);
    if (flag) {
      if (i > 0 && entityIn instanceof EntityLivingBase) {
        ((EntityLivingBase)entityIn).knockBack((Entity)this, i * 0.5F, MathHelper.sin(this.rotationYaw * 0.017453292F), -MathHelper.cos(this.rotationYaw * 0.017453292F));
        this.motionX *= 0.6D;
        this.motionZ *= 0.6D;
      } 
      int j = EnchantmentHelper.getFireAspectModifier((EntityLivingBase)this);
      if (j > 0)
        entityIn.setFire(j * 4); 
      if (entityIn instanceof EntityPlayer) {
        EntityPlayer entityplayer = (EntityPlayer)entityIn;
        ItemStack itemstack = getHeldItemMainhand();
        ItemStack itemstack1 = entityplayer.isHandActive() ? entityplayer.getActiveItemStack() : ItemStack.field_190927_a;
        if (!itemstack.func_190926_b() && !itemstack1.func_190926_b() && itemstack.getItem() instanceof net.minecraft.item.ItemAxe && itemstack1.getItem() == Items.SHIELD) {
          float f1 = 0.25F + EnchantmentHelper.getEfficiencyModifier((EntityLivingBase)this) * 0.05F;
          if (this.rand.nextFloat() < f1) {
            entityplayer.getCooldownTracker().setCooldown(Items.SHIELD, 100);
            this.world.setEntityState((Entity)entityplayer, (byte)30);
          } 
        } 
      } 
      applyEnchantments((EntityLivingBase)this, entityIn);
    } 
    return flag;
  }
  
  public float getBlockPathWeight(BlockPos pos) {
    return 0.5F - this.world.getLightBrightness(pos);
  }
  
  protected boolean isValidLightLevel() {
    BlockPos blockpos = new BlockPos(this.posX, (getEntityBoundingBox()).minY, this.posZ);
    if (this.world.getLightFor(EnumSkyBlock.SKY, blockpos) > this.rand.nextInt(32))
      return false; 
    int i = this.world.getLightFromNeighbors(blockpos);
    if (this.world.isThundering()) {
      int j = this.world.getSkylightSubtracted();
      this.world.setSkylightSubtracted(10);
      i = this.world.getLightFromNeighbors(blockpos);
      this.world.setSkylightSubtracted(j);
    } 
    return (i <= this.rand.nextInt(8));
  }
  
  public boolean getCanSpawnHere() {
    return (this.world.getDifficulty() != EnumDifficulty.PEACEFUL && isValidLightLevel() && super.getCanSpawnHere());
  }
  
  protected void applyEntityAttributes() {
    super.applyEntityAttributes();
    getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
  }
  
  protected boolean canDropLoot() {
    return true;
  }
  
  public boolean func_191990_c(EntityPlayer p_191990_1_) {
    return true;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\entity\monster\EntityMob.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
package net.minecraft.entity.passive;

import javax.annotation.Nullable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAISkeletonRiders;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntitySkeletonHorse extends AbstractHorse {
  private final EntityAISkeletonRiders skeletonTrapAI = new EntityAISkeletonRiders(this);
  
  private boolean skeletonTrap;
  
  private int skeletonTrapTime;
  
  public EntitySkeletonHorse(World p_i47295_1_) {
    super(p_i47295_1_);
  }
  
  protected void applyEntityAttributes() {
    super.applyEntityAttributes();
    getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(15.0D);
    getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.20000000298023224D);
    getEntityAttribute(JUMP_STRENGTH).setBaseValue(getModifiedJumpStrength());
  }
  
  protected SoundEvent getAmbientSound() {
    super.getAmbientSound();
    return SoundEvents.ENTITY_SKELETON_HORSE_AMBIENT;
  }
  
  protected SoundEvent getDeathSound() {
    super.getDeathSound();
    return SoundEvents.ENTITY_SKELETON_HORSE_DEATH;
  }
  
  protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
    super.getHurtSound(p_184601_1_);
    return SoundEvents.ENTITY_SKELETON_HORSE_HURT;
  }
  
  public EnumCreatureAttribute getCreatureAttribute() {
    return EnumCreatureAttribute.UNDEAD;
  }
  
  public double getMountedYOffset() {
    return super.getMountedYOffset() - 0.1875D;
  }
  
  @Nullable
  protected ResourceLocation getLootTable() {
    return LootTableList.ENTITIES_SKELETON_HORSE;
  }
  
  public void onLivingUpdate() {
    super.onLivingUpdate();
    if (func_190690_dh() && this.skeletonTrapTime++ >= 18000)
      setDead(); 
  }
  
  public static void func_190692_b(DataFixer p_190692_0_) {
    AbstractHorse.func_190683_c(p_190692_0_, EntitySkeletonHorse.class);
  }
  
  public void writeEntityToNBT(NBTTagCompound compound) {
    super.writeEntityToNBT(compound);
    compound.setBoolean("SkeletonTrap", func_190690_dh());
    compound.setInteger("SkeletonTrapTime", this.skeletonTrapTime);
  }
  
  public void readEntityFromNBT(NBTTagCompound compound) {
    super.readEntityFromNBT(compound);
    func_190691_p(compound.getBoolean("SkeletonTrap"));
    this.skeletonTrapTime = compound.getInteger("SkeletonTrapTime");
  }
  
  public boolean func_190690_dh() {
    return this.skeletonTrap;
  }
  
  public void func_190691_p(boolean p_190691_1_) {
    if (p_190691_1_ != this.skeletonTrap) {
      this.skeletonTrap = p_190691_1_;
      if (p_190691_1_) {
        this.tasks.addTask(1, (EntityAIBase)this.skeletonTrapAI);
      } else {
        this.tasks.removeTask((EntityAIBase)this.skeletonTrapAI);
      } 
    } 
  }
  
  public boolean processInteract(EntityPlayer player, EnumHand hand) {
    ItemStack itemstack = player.getHeldItem(hand);
    boolean flag = !itemstack.func_190926_b();
    if (flag && itemstack.getItem() == Items.SPAWN_EGG)
      return super.processInteract(player, hand); 
    if (!isTame())
      return false; 
    if (isChild())
      return super.processInteract(player, hand); 
    if (player.isSneaking()) {
      openGUI(player);
      return true;
    } 
    if (isBeingRidden())
      return super.processInteract(player, hand); 
    if (flag) {
      if (itemstack.getItem() == Items.SADDLE && !isHorseSaddled()) {
        openGUI(player);
        return true;
      } 
      if (itemstack.interactWithEntity(player, (EntityLivingBase)this, hand))
        return true; 
    } 
    mountTo(player);
    return true;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\entity\passive\EntitySkeletonHorse.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
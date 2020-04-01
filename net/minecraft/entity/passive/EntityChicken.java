package net.minecraft.entity.passive;

import com.google.common.collect.Sets;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityChicken extends EntityAnimal {
  private static final Set<Item> TEMPTATION_ITEMS = Sets.newHashSet((Object[])new Item[] { Items.WHEAT_SEEDS, Items.MELON_SEEDS, Items.PUMPKIN_SEEDS, Items.BEETROOT_SEEDS });
  
  public float wingRotation;
  
  public float destPos;
  
  public float oFlapSpeed;
  
  public float oFlap;
  
  public float wingRotDelta = 1.0F;
  
  public int timeUntilNextEgg;
  
  public boolean chickenJockey;
  
  public EntityChicken(World worldIn) {
    super(worldIn);
    setSize(0.4F, 0.7F);
    this.timeUntilNextEgg = this.rand.nextInt(6000) + 6000;
    setPathPriority(PathNodeType.WATER, 0.0F);
  }
  
  protected void initEntityAI() {
    this.tasks.addTask(0, (EntityAIBase)new EntityAISwimming((EntityLiving)this));
    this.tasks.addTask(1, (EntityAIBase)new EntityAIPanic((EntityCreature)this, 1.4D));
    this.tasks.addTask(2, (EntityAIBase)new EntityAIMate(this, 1.0D));
    this.tasks.addTask(3, (EntityAIBase)new EntityAITempt((EntityCreature)this, 1.0D, false, TEMPTATION_ITEMS));
    this.tasks.addTask(4, (EntityAIBase)new EntityAIFollowParent(this, 1.1D));
    this.tasks.addTask(5, (EntityAIBase)new EntityAIWanderAvoidWater((EntityCreature)this, 1.0D));
    this.tasks.addTask(6, (EntityAIBase)new EntityAIWatchClosest((EntityLiving)this, EntityPlayer.class, 6.0F));
    this.tasks.addTask(7, (EntityAIBase)new EntityAILookIdle((EntityLiving)this));
  }
  
  public float getEyeHeight() {
    return this.height;
  }
  
  protected void applyEntityAttributes() {
    super.applyEntityAttributes();
    getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(4.0D);
    getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
  }
  
  public void onLivingUpdate() {
    super.onLivingUpdate();
    this.oFlap = this.wingRotation;
    this.oFlapSpeed = this.destPos;
    this.destPos = (float)(this.destPos + (this.onGround ? -1 : 4) * 0.3D);
    this.destPos = MathHelper.clamp(this.destPos, 0.0F, 1.0F);
    if (!this.onGround && this.wingRotDelta < 1.0F)
      this.wingRotDelta = 1.0F; 
    this.wingRotDelta = (float)(this.wingRotDelta * 0.9D);
    if (!this.onGround && this.motionY < 0.0D)
      this.motionY *= 0.6D; 
    this.wingRotation += this.wingRotDelta * 2.0F;
    if (!this.world.isRemote && !isChild() && !isChickenJockey() && --this.timeUntilNextEgg <= 0) {
      playSound(SoundEvents.ENTITY_CHICKEN_EGG, 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
      dropItem(Items.EGG, 1);
      this.timeUntilNextEgg = this.rand.nextInt(6000) + 6000;
    } 
  }
  
  public void fall(float distance, float damageMultiplier) {}
  
  protected SoundEvent getAmbientSound() {
    return SoundEvents.ENTITY_CHICKEN_AMBIENT;
  }
  
  protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
    return SoundEvents.ENTITY_CHICKEN_HURT;
  }
  
  protected SoundEvent getDeathSound() {
    return SoundEvents.ENTITY_CHICKEN_DEATH;
  }
  
  protected void playStepSound(BlockPos pos, Block blockIn) {
    playSound(SoundEvents.ENTITY_CHICKEN_STEP, 0.15F, 1.0F);
  }
  
  @Nullable
  protected ResourceLocation getLootTable() {
    return LootTableList.ENTITIES_CHICKEN;
  }
  
  public EntityChicken createChild(EntityAgeable ageable) {
    return new EntityChicken(this.world);
  }
  
  public boolean isBreedingItem(ItemStack stack) {
    return TEMPTATION_ITEMS.contains(stack.getItem());
  }
  
  protected int getExperiencePoints(EntityPlayer player) {
    return isChickenJockey() ? 10 : super.getExperiencePoints(player);
  }
  
  public static void registerFixesChicken(DataFixer fixer) {
    EntityLiving.registerFixesMob(fixer, EntityChicken.class);
  }
  
  public void readEntityFromNBT(NBTTagCompound compound) {
    super.readEntityFromNBT(compound);
    this.chickenJockey = compound.getBoolean("IsChickenJockey");
    if (compound.hasKey("EggLayTime"))
      this.timeUntilNextEgg = compound.getInteger("EggLayTime"); 
  }
  
  public void writeEntityToNBT(NBTTagCompound compound) {
    super.writeEntityToNBT(compound);
    compound.setBoolean("IsChickenJockey", this.chickenJockey);
    compound.setInteger("EggLayTime", this.timeUntilNextEgg);
  }
  
  protected boolean canDespawn() {
    return (isChickenJockey() && !isBeingRidden());
  }
  
  public void updatePassenger(Entity passenger) {
    super.updatePassenger(passenger);
    float f = MathHelper.sin(this.renderYawOffset * 0.017453292F);
    float f1 = MathHelper.cos(this.renderYawOffset * 0.017453292F);
    float f2 = 0.1F;
    float f3 = 0.0F;
    passenger.setPosition(this.posX + (0.1F * f), this.posY + (this.height * 0.5F) + passenger.getYOffset() + 0.0D, this.posZ - (0.1F * f1));
    if (passenger instanceof EntityLivingBase)
      ((EntityLivingBase)passenger).renderYawOffset = this.renderYawOffset; 
  }
  
  public boolean isChickenJockey() {
    return this.chickenJockey;
  }
  
  public void setChickenJockey(boolean jockey) {
    this.chickenJockey = jockey;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\entity\passive\EntityChicken.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
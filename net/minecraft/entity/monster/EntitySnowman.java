package net.minecraft.entity.monster;

import javax.annotation.Nullable;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackRanged;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntitySnowman extends EntityGolem implements IRangedAttackMob {
  private static final DataParameter<Byte> PUMPKIN_EQUIPPED = EntityDataManager.createKey(EntitySnowman.class, DataSerializers.BYTE);
  
  public EntitySnowman(World worldIn) {
    super(worldIn);
    setSize(0.7F, 1.9F);
  }
  
  public static void registerFixesSnowman(DataFixer fixer) {
    EntityLiving.registerFixesMob(fixer, EntitySnowman.class);
  }
  
  protected void initEntityAI() {
    this.tasks.addTask(1, (EntityAIBase)new EntityAIAttackRanged(this, 1.25D, 20, 10.0F));
    this.tasks.addTask(2, (EntityAIBase)new EntityAIWanderAvoidWater(this, 1.0D, 1.0000001E-5F));
    this.tasks.addTask(3, (EntityAIBase)new EntityAIWatchClosest((EntityLiving)this, EntityPlayer.class, 6.0F));
    this.tasks.addTask(4, (EntityAIBase)new EntityAILookIdle((EntityLiving)this));
    this.targetTasks.addTask(1, (EntityAIBase)new EntityAINearestAttackableTarget(this, EntityLiving.class, 10, true, false, IMob.MOB_SELECTOR));
  }
  
  protected void applyEntityAttributes() {
    super.applyEntityAttributes();
    getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(4.0D);
    getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.20000000298023224D);
  }
  
  protected void entityInit() {
    super.entityInit();
    this.dataManager.register(PUMPKIN_EQUIPPED, Byte.valueOf((byte)16));
  }
  
  public void writeEntityToNBT(NBTTagCompound compound) {
    super.writeEntityToNBT(compound);
    compound.setBoolean("Pumpkin", isPumpkinEquipped());
  }
  
  public void readEntityFromNBT(NBTTagCompound compound) {
    super.readEntityFromNBT(compound);
    if (compound.hasKey("Pumpkin"))
      setPumpkinEquipped(compound.getBoolean("Pumpkin")); 
  }
  
  public void onLivingUpdate() {
    super.onLivingUpdate();
    if (!this.world.isRemote) {
      int i = MathHelper.floor(this.posX);
      int j = MathHelper.floor(this.posY);
      int k = MathHelper.floor(this.posZ);
      if (isWet())
        attackEntityFrom(DamageSource.drown, 1.0F); 
      if (this.world.getBiome(new BlockPos(i, 0, k)).getFloatTemperature(new BlockPos(i, j, k)) > 1.0F)
        attackEntityFrom(DamageSource.onFire, 1.0F); 
      if (!this.world.getGameRules().getBoolean("mobGriefing"))
        return; 
      for (int l = 0; l < 4; l++) {
        i = MathHelper.floor(this.posX + ((l % 2 * 2 - 1) * 0.25F));
        j = MathHelper.floor(this.posY);
        k = MathHelper.floor(this.posZ + ((l / 2 % 2 * 2 - 1) * 0.25F));
        BlockPos blockpos = new BlockPos(i, j, k);
        if (this.world.getBlockState(blockpos).getMaterial() == Material.AIR && this.world.getBiome(blockpos).getFloatTemperature(blockpos) < 0.8F && Blocks.SNOW_LAYER.canPlaceBlockAt(this.world, blockpos))
          this.world.setBlockState(blockpos, Blocks.SNOW_LAYER.getDefaultState()); 
      } 
    } 
  }
  
  @Nullable
  protected ResourceLocation getLootTable() {
    return LootTableList.ENTITIES_SNOWMAN;
  }
  
  public void attackEntityWithRangedAttack(EntityLivingBase target, float distanceFactor) {
    EntitySnowball entitysnowball = new EntitySnowball(this.world, (EntityLivingBase)this);
    double d0 = target.posY + target.getEyeHeight() - 1.100000023841858D;
    double d1 = target.posX - this.posX;
    double d2 = d0 - entitysnowball.posY;
    double d3 = target.posZ - this.posZ;
    float f = MathHelper.sqrt(d1 * d1 + d3 * d3) * 0.2F;
    entitysnowball.setThrowableHeading(d1, d2 + f, d3, 1.6F, 12.0F);
    playSound(SoundEvents.ENTITY_SNOWMAN_SHOOT, 1.0F, 1.0F / (getRNG().nextFloat() * 0.4F + 0.8F));
    this.world.spawnEntityInWorld((Entity)entitysnowball);
  }
  
  public float getEyeHeight() {
    return 1.7F;
  }
  
  protected boolean processInteract(EntityPlayer player, EnumHand hand) {
    ItemStack itemstack = player.getHeldItem(hand);
    if (itemstack.getItem() == Items.SHEARS && isPumpkinEquipped() && !this.world.isRemote) {
      setPumpkinEquipped(false);
      itemstack.damageItem(1, (EntityLivingBase)player);
    } 
    return super.processInteract(player, hand);
  }
  
  public boolean isPumpkinEquipped() {
    return ((((Byte)this.dataManager.get(PUMPKIN_EQUIPPED)).byteValue() & 0x10) != 0);
  }
  
  public void setPumpkinEquipped(boolean pumpkinEquipped) {
    byte b0 = ((Byte)this.dataManager.get(PUMPKIN_EQUIPPED)).byteValue();
    if (pumpkinEquipped) {
      this.dataManager.set(PUMPKIN_EQUIPPED, Byte.valueOf((byte)(b0 | 0x10)));
    } else {
      this.dataManager.set(PUMPKIN_EQUIPPED, Byte.valueOf((byte)(b0 & 0xFFFFFFEF)));
    } 
  }
  
  @Nullable
  protected SoundEvent getAmbientSound() {
    return SoundEvents.ENTITY_SNOWMAN_AMBIENT;
  }
  
  @Nullable
  protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
    return SoundEvents.ENTITY_SNOWMAN_HURT;
  }
  
  @Nullable
  protected SoundEvent getDeathSound() {
    return SoundEvents.ENTITY_SNOWMAN_DEATH;
  }
  
  public void setSwingingArms(boolean swingingArms) {}
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\entity\monster\EntitySnowman.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
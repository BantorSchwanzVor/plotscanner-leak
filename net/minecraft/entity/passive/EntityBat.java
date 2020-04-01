package net.minecraft.entity.passive;

import java.util.Calendar;
import javax.annotation.Nullable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityBat extends EntityAmbientCreature {
  private static final DataParameter<Byte> HANGING = EntityDataManager.createKey(EntityBat.class, DataSerializers.BYTE);
  
  private BlockPos spawnPosition;
  
  public EntityBat(World worldIn) {
    super(worldIn);
    setSize(0.5F, 0.9F);
    setIsBatHanging(true);
  }
  
  protected void entityInit() {
    super.entityInit();
    this.dataManager.register(HANGING, Byte.valueOf((byte)0));
  }
  
  protected float getSoundVolume() {
    return 0.1F;
  }
  
  protected float getSoundPitch() {
    return super.getSoundPitch() * 0.95F;
  }
  
  @Nullable
  public SoundEvent getAmbientSound() {
    return (getIsBatHanging() && this.rand.nextInt(4) != 0) ? null : SoundEvents.ENTITY_BAT_AMBIENT;
  }
  
  protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
    return SoundEvents.ENTITY_BAT_HURT;
  }
  
  protected SoundEvent getDeathSound() {
    return SoundEvents.ENTITY_BAT_DEATH;
  }
  
  public boolean canBePushed() {
    return false;
  }
  
  protected void collideWithEntity(Entity entityIn) {}
  
  protected void collideWithNearbyEntities() {}
  
  protected void applyEntityAttributes() {
    super.applyEntityAttributes();
    getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(6.0D);
  }
  
  public boolean getIsBatHanging() {
    return ((((Byte)this.dataManager.get(HANGING)).byteValue() & 0x1) != 0);
  }
  
  public void setIsBatHanging(boolean isHanging) {
    byte b0 = ((Byte)this.dataManager.get(HANGING)).byteValue();
    if (isHanging) {
      this.dataManager.set(HANGING, Byte.valueOf((byte)(b0 | 0x1)));
    } else {
      this.dataManager.set(HANGING, Byte.valueOf((byte)(b0 & 0xFFFFFFFE)));
    } 
  }
  
  public void onUpdate() {
    super.onUpdate();
    if (getIsBatHanging()) {
      this.motionX = 0.0D;
      this.motionY = 0.0D;
      this.motionZ = 0.0D;
      this.posY = MathHelper.floor(this.posY) + 1.0D - this.height;
    } else {
      this.motionY *= 0.6000000238418579D;
    } 
  }
  
  protected void updateAITasks() {
    super.updateAITasks();
    BlockPos blockpos = new BlockPos((Entity)this);
    BlockPos blockpos1 = blockpos.up();
    if (getIsBatHanging()) {
      if (this.world.getBlockState(blockpos1).isNormalCube()) {
        if (this.rand.nextInt(200) == 0)
          this.rotationYawHead = this.rand.nextInt(360); 
        if (this.world.getNearestPlayerNotCreative((Entity)this, 4.0D) != null) {
          setIsBatHanging(false);
          this.world.playEvent(null, 1025, blockpos, 0);
        } 
      } else {
        setIsBatHanging(false);
        this.world.playEvent(null, 1025, blockpos, 0);
      } 
    } else {
      if (this.spawnPosition != null && (!this.world.isAirBlock(this.spawnPosition) || this.spawnPosition.getY() < 1))
        this.spawnPosition = null; 
      if (this.spawnPosition == null || this.rand.nextInt(30) == 0 || this.spawnPosition.distanceSq((int)this.posX, (int)this.posY, (int)this.posZ) < 4.0D)
        this.spawnPosition = new BlockPos((int)this.posX + this.rand.nextInt(7) - this.rand.nextInt(7), (int)this.posY + this.rand.nextInt(6) - 2, (int)this.posZ + this.rand.nextInt(7) - this.rand.nextInt(7)); 
      double d0 = this.spawnPosition.getX() + 0.5D - this.posX;
      double d1 = this.spawnPosition.getY() + 0.1D - this.posY;
      double d2 = this.spawnPosition.getZ() + 0.5D - this.posZ;
      this.motionX += (Math.signum(d0) * 0.5D - this.motionX) * 0.10000000149011612D;
      this.motionY += (Math.signum(d1) * 0.699999988079071D - this.motionY) * 0.10000000149011612D;
      this.motionZ += (Math.signum(d2) * 0.5D - this.motionZ) * 0.10000000149011612D;
      float f = (float)(MathHelper.atan2(this.motionZ, this.motionX) * 57.29577951308232D) - 90.0F;
      float f1 = MathHelper.wrapDegrees(f - this.rotationYaw);
      this.field_191988_bg = 0.5F;
      this.rotationYaw += f1;
      if (this.rand.nextInt(100) == 0 && this.world.getBlockState(blockpos1).isNormalCube())
        setIsBatHanging(true); 
    } 
  }
  
  protected boolean canTriggerWalking() {
    return false;
  }
  
  public void fall(float distance, float damageMultiplier) {}
  
  protected void updateFallState(double y, boolean onGroundIn, IBlockState state, BlockPos pos) {}
  
  public boolean doesEntityNotTriggerPressurePlate() {
    return true;
  }
  
  public boolean attackEntityFrom(DamageSource source, float amount) {
    if (isEntityInvulnerable(source))
      return false; 
    if (!this.world.isRemote && getIsBatHanging())
      setIsBatHanging(false); 
    return super.attackEntityFrom(source, amount);
  }
  
  public static void registerFixesBat(DataFixer fixer) {
    EntityLiving.registerFixesMob(fixer, EntityBat.class);
  }
  
  public void readEntityFromNBT(NBTTagCompound compound) {
    super.readEntityFromNBT(compound);
    this.dataManager.set(HANGING, Byte.valueOf(compound.getByte("BatFlags")));
  }
  
  public void writeEntityToNBT(NBTTagCompound compound) {
    super.writeEntityToNBT(compound);
    compound.setByte("BatFlags", ((Byte)this.dataManager.get(HANGING)).byteValue());
  }
  
  public boolean getCanSpawnHere() {
    BlockPos blockpos = new BlockPos(this.posX, (getEntityBoundingBox()).minY, this.posZ);
    if (blockpos.getY() >= this.world.getSeaLevel())
      return false; 
    int i = this.world.getLightFromNeighbors(blockpos);
    int j = 4;
    if (isDateAroundHalloween(this.world.getCurrentDate())) {
      j = 7;
    } else if (this.rand.nextBoolean()) {
      return false;
    } 
    return (i > this.rand.nextInt(j)) ? false : super.getCanSpawnHere();
  }
  
  private boolean isDateAroundHalloween(Calendar p_175569_1_) {
    return !((p_175569_1_.get(2) + 1 != 10 || p_175569_1_.get(5) < 20) && (p_175569_1_.get(2) + 1 != 11 || p_175569_1_.get(5) > 3));
  }
  
  public float getEyeHeight() {
    return this.height / 2.0F;
  }
  
  @Nullable
  protected ResourceLocation getLootTable() {
    return LootTableList.ENTITIES_BAT;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\entity\passive\EntityBat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
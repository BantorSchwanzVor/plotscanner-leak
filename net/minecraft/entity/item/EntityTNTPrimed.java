package net.minecraft.entity.item;

import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class EntityTNTPrimed extends Entity {
  private static final DataParameter<Integer> FUSE = EntityDataManager.createKey(EntityTNTPrimed.class, DataSerializers.VARINT);
  
  @Nullable
  private EntityLivingBase tntPlacedBy;
  
  private int fuse;
  
  public EntityTNTPrimed(World worldIn) {
    super(worldIn);
    this.fuse = 80;
    this.preventEntitySpawning = true;
    this.isImmuneToFire = true;
    setSize(0.98F, 0.98F);
  }
  
  public EntityTNTPrimed(World worldIn, double x, double y, double z, EntityLivingBase igniter) {
    this(worldIn);
    setPosition(x, y, z);
    float f = (float)(Math.random() * 6.283185307179586D);
    this.motionX = (-((float)Math.sin(f)) * 0.02F);
    this.motionY = 0.20000000298023224D;
    this.motionZ = (-((float)Math.cos(f)) * 0.02F);
    setFuse(80);
    this.prevPosX = x;
    this.prevPosY = y;
    this.prevPosZ = z;
    this.tntPlacedBy = igniter;
  }
  
  protected void entityInit() {
    this.dataManager.register(FUSE, Integer.valueOf(80));
  }
  
  protected boolean canTriggerWalking() {
    return false;
  }
  
  public boolean canBeCollidedWith() {
    return !this.isDead;
  }
  
  public void onUpdate() {
    this.prevPosX = this.posX;
    this.prevPosY = this.posY;
    this.prevPosZ = this.posZ;
    if (!hasNoGravity())
      this.motionY -= 0.03999999910593033D; 
    moveEntity(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
    this.motionX *= 0.9800000190734863D;
    this.motionY *= 0.9800000190734863D;
    this.motionZ *= 0.9800000190734863D;
    if (this.onGround) {
      this.motionX *= 0.699999988079071D;
      this.motionZ *= 0.699999988079071D;
      this.motionY *= -0.5D;
    } 
    this.fuse--;
    if (this.fuse <= 0) {
      setDead();
      if (!this.world.isRemote)
        explode(); 
    } else {
      handleWaterMovement();
      this.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.posX, this.posY + 0.5D, this.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
    } 
  }
  
  private void explode() {
    float f = 4.0F;
    this.world.createExplosion(this, this.posX, this.posY + (this.height / 16.0F), this.posZ, 4.0F, true);
  }
  
  protected void writeEntityToNBT(NBTTagCompound compound) {
    compound.setShort("Fuse", (short)getFuse());
  }
  
  protected void readEntityFromNBT(NBTTagCompound compound) {
    setFuse(compound.getShort("Fuse"));
  }
  
  @Nullable
  public EntityLivingBase getTntPlacedBy() {
    return this.tntPlacedBy;
  }
  
  public float getEyeHeight() {
    return 0.0F;
  }
  
  public void setFuse(int fuseIn) {
    this.dataManager.set(FUSE, Integer.valueOf(fuseIn));
    this.fuse = fuseIn;
  }
  
  public void notifyDataManagerChange(DataParameter<?> key) {
    if (FUSE.equals(key))
      this.fuse = getFuseDataManager(); 
  }
  
  public int getFuseDataManager() {
    return ((Integer)this.dataManager.get(FUSE)).intValue();
  }
  
  public int getFuse() {
    return this.fuse;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\entity\item\EntityTNTPrimed.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
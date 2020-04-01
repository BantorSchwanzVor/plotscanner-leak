package net.minecraft.entity.item;

import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class EntityEnderEye extends Entity {
  private double targetX;
  
  private double targetY;
  
  private double targetZ;
  
  private int despawnTimer;
  
  private boolean shatterOrDrop;
  
  public EntityEnderEye(World worldIn) {
    super(worldIn);
    setSize(0.25F, 0.25F);
  }
  
  protected void entityInit() {}
  
  public boolean isInRangeToRenderDist(double distance) {
    double d0 = getEntityBoundingBox().getAverageEdgeLength() * 4.0D;
    if (Double.isNaN(d0))
      d0 = 4.0D; 
    d0 *= 64.0D;
    return (distance < d0 * d0);
  }
  
  public EntityEnderEye(World worldIn, double x, double y, double z) {
    super(worldIn);
    this.despawnTimer = 0;
    setSize(0.25F, 0.25F);
    setPosition(x, y, z);
  }
  
  public void moveTowards(BlockPos pos) {
    double d0 = pos.getX();
    int i = pos.getY();
    double d1 = pos.getZ();
    double d2 = d0 - this.posX;
    double d3 = d1 - this.posZ;
    float f = MathHelper.sqrt(d2 * d2 + d3 * d3);
    if (f > 12.0F) {
      this.targetX = this.posX + d2 / f * 12.0D;
      this.targetZ = this.posZ + d3 / f * 12.0D;
      this.targetY = this.posY + 8.0D;
    } else {
      this.targetX = d0;
      this.targetY = i;
      this.targetZ = d1;
    } 
    this.despawnTimer = 0;
    this.shatterOrDrop = (this.rand.nextInt(5) > 0);
  }
  
  public void setVelocity(double x, double y, double z) {
    this.motionX = x;
    this.motionY = y;
    this.motionZ = z;
    if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
      float f = MathHelper.sqrt(x * x + z * z);
      this.rotationYaw = (float)(MathHelper.atan2(x, z) * 57.29577951308232D);
      this.rotationPitch = (float)(MathHelper.atan2(y, f) * 57.29577951308232D);
      this.prevRotationYaw = this.rotationYaw;
      this.prevRotationPitch = this.rotationPitch;
    } 
  }
  
  public void onUpdate() {
    this.lastTickPosX = this.posX;
    this.lastTickPosY = this.posY;
    this.lastTickPosZ = this.posZ;
    super.onUpdate();
    this.posX += this.motionX;
    this.posY += this.motionY;
    this.posZ += this.motionZ;
    float f = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
    this.rotationYaw = (float)(MathHelper.atan2(this.motionX, this.motionZ) * 57.29577951308232D);
    for (this.rotationPitch = (float)(MathHelper.atan2(this.motionY, f) * 57.29577951308232D); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F);
    while (this.rotationPitch - this.prevRotationPitch >= 180.0F)
      this.prevRotationPitch += 360.0F; 
    while (this.rotationYaw - this.prevRotationYaw < -180.0F)
      this.prevRotationYaw -= 360.0F; 
    while (this.rotationYaw - this.prevRotationYaw >= 180.0F)
      this.prevRotationYaw += 360.0F; 
    this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
    this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;
    if (!this.world.isRemote) {
      double d0 = this.targetX - this.posX;
      double d1 = this.targetZ - this.posZ;
      float f1 = (float)Math.sqrt(d0 * d0 + d1 * d1);
      float f2 = (float)MathHelper.atan2(d1, d0);
      double d2 = f + (f1 - f) * 0.0025D;
      if (f1 < 1.0F) {
        d2 *= 0.8D;
        this.motionY *= 0.8D;
      } 
      this.motionX = Math.cos(f2) * d2;
      this.motionZ = Math.sin(f2) * d2;
      if (this.posY < this.targetY) {
        this.motionY += (1.0D - this.motionY) * 0.014999999664723873D;
      } else {
        this.motionY += (-1.0D - this.motionY) * 0.014999999664723873D;
      } 
    } 
    float f3 = 0.25F;
    if (isInWater()) {
      for (int i = 0; i < 4; i++)
        this.world.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX - this.motionX * 0.25D, this.posY - this.motionY * 0.25D, this.posZ - this.motionZ * 0.25D, this.motionX, this.motionY, this.motionZ, new int[0]); 
    } else {
      this.world.spawnParticle(EnumParticleTypes.PORTAL, this.posX - this.motionX * 0.25D + this.rand.nextDouble() * 0.6D - 0.3D, this.posY - this.motionY * 0.25D - 0.5D, this.posZ - this.motionZ * 0.25D + this.rand.nextDouble() * 0.6D - 0.3D, this.motionX, this.motionY, this.motionZ, new int[0]);
    } 
    if (!this.world.isRemote) {
      setPosition(this.posX, this.posY, this.posZ);
      this.despawnTimer++;
      if (this.despawnTimer > 80 && !this.world.isRemote) {
        playSound(SoundEvents.field_193777_bb, 1.0F, 1.0F);
        setDead();
        if (this.shatterOrDrop) {
          this.world.spawnEntityInWorld(new EntityItem(this.world, this.posX, this.posY, this.posZ, new ItemStack(Items.ENDER_EYE)));
        } else {
          this.world.playEvent(2003, new BlockPos(this), 0);
        } 
      } 
    } 
  }
  
  public void writeEntityToNBT(NBTTagCompound compound) {}
  
  public void readEntityFromNBT(NBTTagCompound compound) {}
  
  public float getBrightness() {
    return 1.0F;
  }
  
  public int getBrightnessForRender() {
    return 15728880;
  }
  
  public boolean canBeAttackedWithItem() {
    return false;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\entity\item\EntityEnderEye.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
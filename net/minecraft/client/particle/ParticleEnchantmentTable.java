package net.minecraft.client.particle;

import net.minecraft.world.World;

public class ParticleEnchantmentTable extends Particle {
  private final float oSize;
  
  private final double coordX;
  
  private final double coordY;
  
  private final double coordZ;
  
  protected ParticleEnchantmentTable(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn) {
    super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
    this.motionX = xSpeedIn;
    this.motionY = ySpeedIn;
    this.motionZ = zSpeedIn;
    this.coordX = xCoordIn;
    this.coordY = yCoordIn;
    this.coordZ = zCoordIn;
    this.prevPosX = xCoordIn + xSpeedIn;
    this.prevPosY = yCoordIn + ySpeedIn;
    this.prevPosZ = zCoordIn + zSpeedIn;
    this.posX = this.prevPosX;
    this.posY = this.prevPosY;
    this.posZ = this.prevPosZ;
    float f = this.rand.nextFloat() * 0.6F + 0.4F;
    this.particleScale = this.rand.nextFloat() * 0.5F + 0.2F;
    this.oSize = this.particleScale;
    this.particleRed = 0.9F * f;
    this.particleGreen = 0.9F * f;
    this.particleBlue = f;
    this.particleMaxAge = (int)(Math.random() * 10.0D) + 30;
    setParticleTextureIndex((int)(Math.random() * 26.0D + 1.0D + 224.0D));
  }
  
  public void moveEntity(double x, double y, double z) {
    setEntityBoundingBox(getEntityBoundingBox().offset(x, y, z));
    resetPositionToBB();
  }
  
  public int getBrightnessForRender(float p_189214_1_) {
    int i = super.getBrightnessForRender(p_189214_1_);
    float f = this.particleAge / this.particleMaxAge;
    f *= f;
    f *= f;
    int j = i & 0xFF;
    int k = i >> 16 & 0xFF;
    k += (int)(f * 15.0F * 16.0F);
    if (k > 240)
      k = 240; 
    return j | k << 16;
  }
  
  public void onUpdate() {
    this.prevPosX = this.posX;
    this.prevPosY = this.posY;
    this.prevPosZ = this.posZ;
    float f = this.particleAge / this.particleMaxAge;
    f = 1.0F - f;
    float f1 = 1.0F - f;
    f1 *= f1;
    f1 *= f1;
    this.posX = this.coordX + this.motionX * f;
    this.posY = this.coordY + this.motionY * f - (f1 * 1.2F);
    this.posZ = this.coordZ + this.motionZ * f;
    if (this.particleAge++ >= this.particleMaxAge)
      setExpired(); 
  }
  
  public static class EnchantmentTable implements IParticleFactory {
    public Particle createParticle(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_) {
      return new ParticleEnchantmentTable(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
    }
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\particle\ParticleEnchantmentTable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
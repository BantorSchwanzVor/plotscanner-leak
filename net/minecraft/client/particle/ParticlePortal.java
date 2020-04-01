package net.minecraft.client.particle;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class ParticlePortal extends Particle {
  private final float portalParticleScale;
  
  private final double portalPosX;
  
  private final double portalPosY;
  
  private final double portalPosZ;
  
  protected ParticlePortal(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn) {
    super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
    this.motionX = xSpeedIn;
    this.motionY = ySpeedIn;
    this.motionZ = zSpeedIn;
    this.posX = xCoordIn;
    this.posY = yCoordIn;
    this.posZ = zCoordIn;
    this.portalPosX = this.posX;
    this.portalPosY = this.posY;
    this.portalPosZ = this.posZ;
    float f = this.rand.nextFloat() * 0.6F + 0.4F;
    this.particleScale = this.rand.nextFloat() * 0.2F + 0.5F;
    this.portalParticleScale = this.particleScale;
    this.particleRed = f * 0.9F;
    this.particleGreen = f * 0.3F;
    this.particleBlue = f;
    this.particleMaxAge = (int)(Math.random() * 10.0D) + 40;
    setParticleTextureIndex((int)(Math.random() * 8.0D));
  }
  
  public void moveEntity(double x, double y, double z) {
    setEntityBoundingBox(getEntityBoundingBox().offset(x, y, z));
    resetPositionToBB();
  }
  
  public void renderParticle(BufferBuilder worldRendererIn, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
    float f = (this.particleAge + partialTicks) / this.particleMaxAge;
    f = 1.0F - f;
    f *= f;
    f = 1.0F - f;
    this.particleScale = this.portalParticleScale * f;
    super.renderParticle(worldRendererIn, entityIn, partialTicks, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);
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
    float f1 = -f + f * f * 2.0F;
    float f2 = 1.0F - f1;
    this.posX = this.portalPosX + this.motionX * f2;
    this.posY = this.portalPosY + this.motionY * f2 + (1.0F - f);
    this.posZ = this.portalPosZ + this.motionZ * f2;
    if (this.particleAge++ >= this.particleMaxAge)
      setExpired(); 
  }
  
  public static class Factory implements IParticleFactory {
    public Particle createParticle(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_) {
      return new ParticlePortal(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
    }
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\particle\ParticlePortal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
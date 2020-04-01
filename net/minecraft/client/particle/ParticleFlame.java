package net.minecraft.client.particle;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class ParticleFlame extends Particle {
  private final float flameScale;
  
  protected ParticleFlame(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn) {
    super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
    this.motionX = this.motionX * 0.009999999776482582D + xSpeedIn;
    this.motionY = this.motionY * 0.009999999776482582D + ySpeedIn;
    this.motionZ = this.motionZ * 0.009999999776482582D + zSpeedIn;
    this.posX += ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.05F);
    this.posY += ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.05F);
    this.posZ += ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.05F);
    this.flameScale = this.particleScale;
    this.particleRed = 1.0F;
    this.particleGreen = 1.0F;
    this.particleBlue = 1.0F;
    this.particleMaxAge = (int)(8.0D / (Math.random() * 0.8D + 0.2D)) + 4;
    setParticleTextureIndex(48);
  }
  
  public void moveEntity(double x, double y, double z) {
    setEntityBoundingBox(getEntityBoundingBox().offset(x, y, z));
    resetPositionToBB();
  }
  
  public void renderParticle(BufferBuilder worldRendererIn, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
    float f = (this.particleAge + partialTicks) / this.particleMaxAge;
    this.particleScale = this.flameScale * (1.0F - f * f * 0.5F);
    super.renderParticle(worldRendererIn, entityIn, partialTicks, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);
  }
  
  public int getBrightnessForRender(float p_189214_1_) {
    float f = (this.particleAge + p_189214_1_) / this.particleMaxAge;
    f = MathHelper.clamp(f, 0.0F, 1.0F);
    int i = super.getBrightnessForRender(p_189214_1_);
    int j = i & 0xFF;
    int k = i >> 16 & 0xFF;
    j += (int)(f * 15.0F * 16.0F);
    if (j > 240)
      j = 240; 
    return j | k << 16;
  }
  
  public void onUpdate() {
    this.prevPosX = this.posX;
    this.prevPosY = this.posY;
    this.prevPosZ = this.posZ;
    if (this.particleAge++ >= this.particleMaxAge)
      setExpired(); 
    moveEntity(this.motionX, this.motionY, this.motionZ);
    this.motionX *= 0.9599999785423279D;
    this.motionY *= 0.9599999785423279D;
    this.motionZ *= 0.9599999785423279D;
    if (this.isCollided) {
      this.motionX *= 0.699999988079071D;
      this.motionZ *= 0.699999988079071D;
    } 
  }
  
  public static class Factory implements IParticleFactory {
    public Particle createParticle(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_) {
      return new ParticleFlame(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
    }
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\particle\ParticleFlame.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
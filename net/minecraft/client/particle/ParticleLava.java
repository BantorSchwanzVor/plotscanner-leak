package net.minecraft.client.particle;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class ParticleLava extends Particle {
  private final float lavaParticleScale;
  
  protected ParticleLava(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn) {
    super(worldIn, xCoordIn, yCoordIn, zCoordIn, 0.0D, 0.0D, 0.0D);
    this.motionX *= 0.800000011920929D;
    this.motionY *= 0.800000011920929D;
    this.motionZ *= 0.800000011920929D;
    this.motionY = (this.rand.nextFloat() * 0.4F + 0.05F);
    this.particleRed = 1.0F;
    this.particleGreen = 1.0F;
    this.particleBlue = 1.0F;
    this.particleScale *= this.rand.nextFloat() * 2.0F + 0.2F;
    this.lavaParticleScale = this.particleScale;
    this.particleMaxAge = (int)(16.0D / (Math.random() * 0.8D + 0.2D));
    setParticleTextureIndex(49);
  }
  
  public int getBrightnessForRender(float p_189214_1_) {
    int i = super.getBrightnessForRender(p_189214_1_);
    int j = 240;
    int k = i >> 16 & 0xFF;
    return 0xF0 | k << 16;
  }
  
  public void renderParticle(BufferBuilder worldRendererIn, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
    float f = (this.particleAge + partialTicks) / this.particleMaxAge;
    this.particleScale = this.lavaParticleScale * (1.0F - f * f);
    super.renderParticle(worldRendererIn, entityIn, partialTicks, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);
  }
  
  public void onUpdate() {
    this.prevPosX = this.posX;
    this.prevPosY = this.posY;
    this.prevPosZ = this.posZ;
    if (this.particleAge++ >= this.particleMaxAge)
      setExpired(); 
    float f = this.particleAge / this.particleMaxAge;
    if (this.rand.nextFloat() > f)
      this.worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.posX, this.posY, this.posZ, this.motionX, this.motionY, this.motionZ, new int[0]); 
    this.motionY -= 0.03D;
    moveEntity(this.motionX, this.motionY, this.motionZ);
    this.motionX *= 0.9990000128746033D;
    this.motionY *= 0.9990000128746033D;
    this.motionZ *= 0.9990000128746033D;
    if (this.isCollided) {
      this.motionX *= 0.699999988079071D;
      this.motionZ *= 0.699999988079071D;
    } 
  }
  
  public static class Factory implements IParticleFactory {
    public Particle createParticle(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_) {
      return new ParticleLava(worldIn, xCoordIn, yCoordIn, zCoordIn);
    }
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\particle\ParticleLava.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
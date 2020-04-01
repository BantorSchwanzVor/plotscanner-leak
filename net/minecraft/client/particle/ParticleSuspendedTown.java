package net.minecraft.client.particle;

import net.minecraft.world.World;

public class ParticleSuspendedTown extends Particle {
  protected ParticleSuspendedTown(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double speedIn) {
    super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, speedIn);
    float f = this.rand.nextFloat() * 0.1F + 0.2F;
    this.particleRed = f;
    this.particleGreen = f;
    this.particleBlue = f;
    setParticleTextureIndex(0);
    setSize(0.02F, 0.02F);
    this.particleScale *= this.rand.nextFloat() * 0.6F + 0.5F;
    this.motionX *= 0.019999999552965164D;
    this.motionY *= 0.019999999552965164D;
    this.motionZ *= 0.019999999552965164D;
    this.particleMaxAge = (int)(20.0D / (Math.random() * 0.8D + 0.2D));
  }
  
  public void moveEntity(double x, double y, double z) {
    setEntityBoundingBox(getEntityBoundingBox().offset(x, y, z));
    resetPositionToBB();
  }
  
  public void onUpdate() {
    this.prevPosX = this.posX;
    this.prevPosY = this.posY;
    this.prevPosZ = this.posZ;
    moveEntity(this.motionX, this.motionY, this.motionZ);
    this.motionX *= 0.99D;
    this.motionY *= 0.99D;
    this.motionZ *= 0.99D;
    if (this.particleMaxAge-- <= 0)
      setExpired(); 
  }
  
  public static class Factory implements IParticleFactory {
    public Particle createParticle(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_) {
      return new ParticleSuspendedTown(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
    }
  }
  
  public static class HappyVillagerFactory implements IParticleFactory {
    public Particle createParticle(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_) {
      Particle particle = new ParticleSuspendedTown(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
      particle.setParticleTextureIndex(82);
      particle.setRBGColorF(1.0F, 1.0F, 1.0F);
      return particle;
    }
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\particle\ParticleSuspendedTown.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
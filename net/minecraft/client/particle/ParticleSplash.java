package net.minecraft.client.particle;

import net.minecraft.world.World;

public class ParticleSplash extends ParticleRain {
  protected ParticleSplash(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn) {
    super(worldIn, xCoordIn, yCoordIn, zCoordIn);
    this.particleGravity = 0.04F;
    nextTextureIndexX();
    if (ySpeedIn == 0.0D && (xSpeedIn != 0.0D || zSpeedIn != 0.0D)) {
      this.motionX = xSpeedIn;
      this.motionY = ySpeedIn + 0.1D;
      this.motionZ = zSpeedIn;
    } 
  }
  
  public static class Factory implements IParticleFactory {
    public Particle createParticle(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_) {
      return new ParticleSplash(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
    }
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\particle\ParticleSplash.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
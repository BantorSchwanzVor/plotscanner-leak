package net.minecraft.client.particle;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class ParticleExplosionHuge extends Particle {
  private int timeSinceStart;
  
  private final int maximumTime = 8;
  
  protected ParticleExplosionHuge(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double p_i1214_8_, double p_i1214_10_, double p_i1214_12_) {
    super(worldIn, xCoordIn, yCoordIn, zCoordIn, 0.0D, 0.0D, 0.0D);
  }
  
  public void renderParticle(BufferBuilder worldRendererIn, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {}
  
  public void onUpdate() {
    for (int i = 0; i < 6; i++) {
      double d0 = this.posX + (this.rand.nextDouble() - this.rand.nextDouble()) * 4.0D;
      double d1 = this.posY + (this.rand.nextDouble() - this.rand.nextDouble()) * 4.0D;
      double d2 = this.posZ + (this.rand.nextDouble() - this.rand.nextDouble()) * 4.0D;
      this.worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, d0, d1, d2, (this.timeSinceStart / 8.0F), 0.0D, 0.0D, new int[0]);
    } 
    this.timeSinceStart++;
    if (this.timeSinceStart == 8)
      setExpired(); 
  }
  
  public int getFXLayer() {
    return 1;
  }
  
  public static class Factory implements IParticleFactory {
    public Particle createParticle(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_) {
      return new ParticleExplosionHuge(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
    }
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\particle\ParticleExplosionHuge.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
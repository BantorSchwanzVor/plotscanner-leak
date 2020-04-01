package net.minecraft.client.particle;

import net.minecraft.world.World;

public class ParticleSimpleAnimated extends Particle {
  private final int textureIdx;
  
  private final int numAgingFrames;
  
  private final float yAccel;
  
  private float field_191239_M = 0.91F;
  
  private float fadeTargetRed;
  
  private float fadeTargetGreen;
  
  private float fadeTargetBlue;
  
  private boolean fadingColor;
  
  public ParticleSimpleAnimated(World worldIn, double x, double y, double z, int textureIdxIn, int numFrames, float yAccelIn) {
    super(worldIn, x, y, z);
    this.textureIdx = textureIdxIn;
    this.numAgingFrames = numFrames;
    this.yAccel = yAccelIn;
  }
  
  public void setColor(int p_187146_1_) {
    float f = ((p_187146_1_ & 0xFF0000) >> 16) / 255.0F;
    float f1 = ((p_187146_1_ & 0xFF00) >> 8) / 255.0F;
    float f2 = ((p_187146_1_ & 0xFF) >> 0) / 255.0F;
    float f3 = 1.0F;
    setRBGColorF(f * 1.0F, f1 * 1.0F, f2 * 1.0F);
  }
  
  public void setColorFade(int rgb) {
    this.fadeTargetRed = ((rgb & 0xFF0000) >> 16) / 255.0F;
    this.fadeTargetGreen = ((rgb & 0xFF00) >> 8) / 255.0F;
    this.fadeTargetBlue = ((rgb & 0xFF) >> 0) / 255.0F;
    this.fadingColor = true;
  }
  
  public boolean isTransparent() {
    return true;
  }
  
  public void onUpdate() {
    this.prevPosX = this.posX;
    this.prevPosY = this.posY;
    this.prevPosZ = this.posZ;
    if (this.particleAge++ >= this.particleMaxAge)
      setExpired(); 
    if (this.particleAge > this.particleMaxAge / 2) {
      setAlphaF(1.0F - (this.particleAge - (this.particleMaxAge / 2)) / this.particleMaxAge);
      if (this.fadingColor) {
        this.particleRed += (this.fadeTargetRed - this.particleRed) * 0.2F;
        this.particleGreen += (this.fadeTargetGreen - this.particleGreen) * 0.2F;
        this.particleBlue += (this.fadeTargetBlue - this.particleBlue) * 0.2F;
      } 
    } 
    setParticleTextureIndex(this.textureIdx + this.numAgingFrames - 1 - this.particleAge * this.numAgingFrames / this.particleMaxAge);
    this.motionY += this.yAccel;
    moveEntity(this.motionX, this.motionY, this.motionZ);
    this.motionX *= this.field_191239_M;
    this.motionY *= this.field_191239_M;
    this.motionZ *= this.field_191239_M;
    if (this.isCollided) {
      this.motionX *= 0.699999988079071D;
      this.motionZ *= 0.699999988079071D;
    } 
  }
  
  public int getBrightnessForRender(float p_189214_1_) {
    return 15728880;
  }
  
  protected void func_191238_f(float p_191238_1_) {
    this.field_191239_M = p_191238_1_;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\particle\ParticleSimpleAnimated.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
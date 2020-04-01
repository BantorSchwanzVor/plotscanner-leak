package net.minecraft.client.particle;

import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class ParticleFallingDust extends Particle {
  float oSize;
  
  final float rotSpeed;
  
  protected ParticleFallingDust(World p_i47135_1_, double p_i47135_2_, double p_i47135_4_, double p_i47135_6_, float p_i47135_8_, float p_i47135_9_, float p_i47135_10_) {
    super(p_i47135_1_, p_i47135_2_, p_i47135_4_, p_i47135_6_, 0.0D, 0.0D, 0.0D);
    this.motionX = 0.0D;
    this.motionY = 0.0D;
    this.motionZ = 0.0D;
    this.particleRed = p_i47135_8_;
    this.particleGreen = p_i47135_9_;
    this.particleBlue = p_i47135_10_;
    float f = 0.9F;
    this.particleScale *= 0.75F;
    this.particleScale *= 0.9F;
    this.oSize = this.particleScale;
    this.particleMaxAge = (int)(32.0D / (Math.random() * 0.8D + 0.2D));
    this.particleMaxAge = (int)(this.particleMaxAge * 0.9F);
    this.rotSpeed = ((float)Math.random() - 0.5F) * 0.1F;
    this.particleAngle = (float)Math.random() * 6.2831855F;
  }
  
  public void renderParticle(BufferBuilder worldRendererIn, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
    float f = (this.particleAge + partialTicks) / this.particleMaxAge * 32.0F;
    f = MathHelper.clamp(f, 0.0F, 1.0F);
    this.particleScale = this.oSize * f;
    super.renderParticle(worldRendererIn, entityIn, partialTicks, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);
  }
  
  public void onUpdate() {
    this.prevPosX = this.posX;
    this.prevPosY = this.posY;
    this.prevPosZ = this.posZ;
    if (this.particleAge++ >= this.particleMaxAge)
      setExpired(); 
    this.prevParticleAngle = this.particleAngle;
    this.particleAngle += 3.1415927F * this.rotSpeed * 2.0F;
    if (this.isCollided)
      this.prevParticleAngle = this.particleAngle = 0.0F; 
    setParticleTextureIndex(7 - this.particleAge * 8 / this.particleMaxAge);
    moveEntity(this.motionX, this.motionY, this.motionZ);
    this.motionY -= 0.003000000026077032D;
    this.motionY = Math.max(this.motionY, -0.14000000059604645D);
  }
  
  public static class Factory implements IParticleFactory {
    @Nullable
    public Particle createParticle(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_) {
      IBlockState iblockstate = Block.getStateById(p_178902_15_[0]);
      if (iblockstate.getBlock() != Blocks.AIR && iblockstate.getRenderType() == EnumBlockRenderType.INVISIBLE)
        return null; 
      int i = Minecraft.getMinecraft().getBlockColors().getColor(iblockstate, worldIn, new BlockPos(xCoordIn, yCoordIn, zCoordIn));
      if (iblockstate.getBlock() instanceof BlockFalling)
        i = ((BlockFalling)iblockstate.getBlock()).getDustColor(iblockstate); 
      float f = (i >> 16 & 0xFF) / 255.0F;
      float f1 = (i >> 8 & 0xFF) / 255.0F;
      float f2 = (i & 0xFF) / 255.0F;
      return new ParticleFallingDust(worldIn, xCoordIn, yCoordIn, zCoordIn, f, f1, f2);
    }
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\particle\ParticleFallingDust.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
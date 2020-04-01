package net.minecraft.client.entity;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

public class EntityOtherPlayerMP extends AbstractClientPlayer {
  private int otherPlayerMPPosRotationIncrements;
  
  private double otherPlayerMPX;
  
  private double otherPlayerMPY;
  
  private double otherPlayerMPZ;
  
  private double otherPlayerMPYaw;
  
  private double otherPlayerMPPitch;
  
  public EntityOtherPlayerMP(World worldIn, GameProfile gameProfileIn) {
    super(worldIn, gameProfileIn);
    this.stepHeight = 1.0F;
    this.noClip = true;
    this.renderOffsetY = 0.25F;
  }
  
  public boolean isInRangeToRenderDist(double distance) {
    double d0 = getEntityBoundingBox().getAverageEdgeLength() * 10.0D;
    if (Double.isNaN(d0))
      d0 = 1.0D; 
    d0 = d0 * 64.0D * getRenderDistanceWeight();
    return (distance < d0 * d0);
  }
  
  public boolean attackEntityFrom(DamageSource source, float amount) {
    return true;
  }
  
  public void setPositionAndRotationDirect(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean teleport) {
    this.otherPlayerMPX = x;
    this.otherPlayerMPY = y;
    this.otherPlayerMPZ = z;
    this.otherPlayerMPYaw = yaw;
    this.otherPlayerMPPitch = pitch;
    this.otherPlayerMPPosRotationIncrements = posRotationIncrements;
  }
  
  public void onUpdate() {
    this.renderOffsetY = 0.0F;
    super.onUpdate();
    this.prevLimbSwingAmount = this.limbSwingAmount;
    double d0 = this.posX - this.prevPosX;
    double d1 = this.posZ - this.prevPosZ;
    float f = MathHelper.sqrt(d0 * d0 + d1 * d1) * 4.0F;
    if (f > 1.0F)
      f = 1.0F; 
    this.limbSwingAmount += (f - this.limbSwingAmount) * 0.4F;
    this.limbSwing += this.limbSwingAmount;
  }
  
  public void onLivingUpdate() {
    if (this.otherPlayerMPPosRotationIncrements > 0) {
      double d0 = this.posX + (this.otherPlayerMPX - this.posX) / this.otherPlayerMPPosRotationIncrements;
      double d1 = this.posY + (this.otherPlayerMPY - this.posY) / this.otherPlayerMPPosRotationIncrements;
      double d2 = this.posZ + (this.otherPlayerMPZ - this.posZ) / this.otherPlayerMPPosRotationIncrements;
      double d3;
      for (d3 = this.otherPlayerMPYaw - this.rotationYaw; d3 < -180.0D; d3 += 360.0D);
      while (d3 >= 180.0D)
        d3 -= 360.0D; 
      this.rotationYaw = (float)(this.rotationYaw + d3 / this.otherPlayerMPPosRotationIncrements);
      this.rotationPitch = (float)(this.rotationPitch + (this.otherPlayerMPPitch - this.rotationPitch) / this.otherPlayerMPPosRotationIncrements);
      this.otherPlayerMPPosRotationIncrements--;
      setPosition(d0, d1, d2);
      setRotation(this.rotationYaw, this.rotationPitch);
    } 
    this.prevCameraYaw = this.cameraYaw;
    updateArmSwingProgress();
    float f1 = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
    float f = (float)Math.atan(-this.motionY * 0.20000000298023224D) * 15.0F;
    if (f1 > 0.1F)
      f1 = 0.1F; 
    if (!this.onGround || getHealth() <= 0.0F)
      f1 = 0.0F; 
    if (this.onGround || getHealth() <= 0.0F)
      f = 0.0F; 
    this.cameraYaw += (f1 - this.cameraYaw) * 0.4F;
    this.cameraPitch += (f - this.cameraPitch) * 0.8F;
    this.world.theProfiler.startSection("push");
    collideWithNearbyEntities();
    this.world.theProfiler.endSection();
  }
  
  public void addChatMessage(ITextComponent component) {
    (Minecraft.getMinecraft()).ingameGUI.getChatGUI().printChatMessage(component);
  }
  
  public boolean canCommandSenderUseCommand(int permLevel, String commandName) {
    return false;
  }
  
  public BlockPos getPosition() {
    return new BlockPos(this.posX + 0.5D, this.posY + 0.5D, this.posZ + 0.5D);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\entity\EntityOtherPlayerMP.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
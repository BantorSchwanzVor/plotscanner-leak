package net.minecraft.entity.boss.dragon.phase;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class PhaseSittingScanning extends PhaseSittingBase {
  private int scanningTime;
  
  public PhaseSittingScanning(EntityDragon dragonIn) {
    super(dragonIn);
  }
  
  public void doLocalUpdate() {
    this.scanningTime++;
    EntityPlayer entityPlayer = this.dragon.world.getNearestAttackablePlayer((Entity)this.dragon, 20.0D, 10.0D);
    if (entityPlayer != null) {
      if (this.scanningTime > 25) {
        this.dragon.getPhaseManager().setPhase(PhaseList.SITTING_ATTACKING);
      } else {
        Vec3d vec3d = (new Vec3d(((EntityLivingBase)entityPlayer).posX - this.dragon.posX, 0.0D, ((EntityLivingBase)entityPlayer).posZ - this.dragon.posZ)).normalize();
        Vec3d vec3d1 = (new Vec3d(MathHelper.sin(this.dragon.rotationYaw * 0.017453292F), 0.0D, -MathHelper.cos(this.dragon.rotationYaw * 0.017453292F))).normalize();
        float f = (float)vec3d1.dotProduct(vec3d);
        float f1 = (float)(Math.acos(f) * 57.29577951308232D) + 0.5F;
        if (f1 < 0.0F || f1 > 10.0F) {
          double d0 = ((EntityLivingBase)entityPlayer).posX - this.dragon.dragonPartHead.posX;
          double d1 = ((EntityLivingBase)entityPlayer).posZ - this.dragon.dragonPartHead.posZ;
          double d2 = MathHelper.clamp(MathHelper.wrapDegrees(180.0D - MathHelper.atan2(d0, d1) * 57.29577951308232D - this.dragon.rotationYaw), -100.0D, 100.0D);
          this.dragon.randomYawVelocity *= 0.8F;
          float f2 = MathHelper.sqrt(d0 * d0 + d1 * d1) + 1.0F;
          float f3 = f2;
          if (f2 > 40.0F)
            f2 = 40.0F; 
          this.dragon.randomYawVelocity = (float)(this.dragon.randomYawVelocity + d2 * (0.7F / f2 / f3));
          this.dragon.rotationYaw += this.dragon.randomYawVelocity;
        } 
      } 
    } else if (this.scanningTime >= 100) {
      entityPlayer = this.dragon.world.getNearestAttackablePlayer((Entity)this.dragon, 150.0D, 150.0D);
      this.dragon.getPhaseManager().setPhase(PhaseList.TAKEOFF);
      if (entityPlayer != null) {
        this.dragon.getPhaseManager().setPhase(PhaseList.CHARGING_PLAYER);
        ((PhaseChargingPlayer)this.dragon.getPhaseManager().<PhaseChargingPlayer>getPhase(PhaseList.CHARGING_PLAYER)).setTarget(new Vec3d(((EntityLivingBase)entityPlayer).posX, ((EntityLivingBase)entityPlayer).posY, ((EntityLivingBase)entityPlayer).posZ));
      } 
    } 
  }
  
  public void initPhase() {
    this.scanningTime = 0;
  }
  
  public PhaseList<PhaseSittingScanning> getPhaseList() {
    return PhaseList.SITTING_SCANNING;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\entity\boss\dragon\phase\PhaseSittingScanning.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
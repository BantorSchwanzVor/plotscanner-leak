package net.minecraft.entity.boss.dragon.phase;

import javax.annotation.Nullable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.gen.feature.WorldGenEndPodium;

public class PhaseHoldingPattern extends PhaseBase {
  private Path currentPath;
  
  private Vec3d targetLocation;
  
  private boolean clockwise;
  
  public PhaseHoldingPattern(EntityDragon dragonIn) {
    super(dragonIn);
  }
  
  public PhaseList<PhaseHoldingPattern> getPhaseList() {
    return PhaseList.HOLDING_PATTERN;
  }
  
  public void doLocalUpdate() {
    double d0 = (this.targetLocation == null) ? 0.0D : this.targetLocation.squareDistanceTo(this.dragon.posX, this.dragon.posY, this.dragon.posZ);
    if (d0 < 100.0D || d0 > 22500.0D || this.dragon.isCollidedHorizontally || this.dragon.isCollidedVertically)
      findNewTarget(); 
  }
  
  public void initPhase() {
    this.currentPath = null;
    this.targetLocation = null;
  }
  
  @Nullable
  public Vec3d getTargetLocation() {
    return this.targetLocation;
  }
  
  private void findNewTarget() {
    if (this.currentPath != null && this.currentPath.isFinished()) {
      BlockPos blockpos = this.dragon.world.getTopSolidOrLiquidBlock(new BlockPos((Vec3i)WorldGenEndPodium.END_PODIUM_LOCATION));
      int i = (this.dragon.getFightManager() == null) ? 0 : this.dragon.getFightManager().getNumAliveCrystals();
      if (this.dragon.getRNG().nextInt(i + 3) == 0) {
        this.dragon.getPhaseManager().setPhase(PhaseList.LANDING_APPROACH);
        return;
      } 
      double d0 = 64.0D;
      EntityPlayer entityplayer = this.dragon.world.getNearestAttackablePlayer(blockpos, d0, d0);
      if (entityplayer != null)
        d0 = entityplayer.getDistanceSqToCenter(blockpos) / 512.0D; 
      if (entityplayer != null && (this.dragon.getRNG().nextInt(MathHelper.abs((int)d0) + 2) == 0 || this.dragon.getRNG().nextInt(i + 2) == 0)) {
        strafePlayer(entityplayer);
        return;
      } 
    } 
    if (this.currentPath == null || this.currentPath.isFinished()) {
      int j = this.dragon.initPathPoints();
      int k = j;
      if (this.dragon.getRNG().nextInt(8) == 0) {
        this.clockwise = !this.clockwise;
        k = j + 6;
      } 
      if (this.clockwise) {
        k++;
      } else {
        k--;
      } 
      if (this.dragon.getFightManager() != null && this.dragon.getFightManager().getNumAliveCrystals() >= 0) {
        k %= 12;
        if (k < 0)
          k += 12; 
      } else {
        k -= 12;
        k &= 0x7;
        k += 12;
      } 
      this.currentPath = this.dragon.findPath(j, k, null);
      if (this.currentPath != null)
        this.currentPath.incrementPathIndex(); 
    } 
    navigateToNextPathNode();
  }
  
  private void strafePlayer(EntityPlayer player) {
    this.dragon.getPhaseManager().setPhase(PhaseList.STRAFE_PLAYER);
    ((PhaseStrafePlayer)this.dragon.getPhaseManager().<PhaseStrafePlayer>getPhase(PhaseList.STRAFE_PLAYER)).setTarget((EntityLivingBase)player);
  }
  
  private void navigateToNextPathNode() {
    if (this.currentPath != null && !this.currentPath.isFinished()) {
      double d2;
      Vec3d vec3d = this.currentPath.getCurrentPos();
      this.currentPath.incrementPathIndex();
      double d0 = vec3d.xCoord;
      double d1 = vec3d.zCoord;
      do {
        d2 = vec3d.yCoord + (this.dragon.getRNG().nextFloat() * 20.0F);
      } while (d2 < vec3d.yCoord);
      this.targetLocation = new Vec3d(d0, d2, d1);
    } 
  }
  
  public void onCrystalDestroyed(EntityEnderCrystal crystal, BlockPos pos, DamageSource dmgSrc, @Nullable EntityPlayer plyr) {
    if (plyr != null && !plyr.capabilities.disableDamage)
      strafePlayer(plyr); 
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\entity\boss\dragon\phase\PhaseHoldingPattern.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
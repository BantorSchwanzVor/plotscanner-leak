package net.minecraft.entity.boss.dragon.phase;

import javax.annotation.Nullable;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.gen.feature.WorldGenEndPodium;

public class PhaseTakeoff extends PhaseBase {
  private boolean firstTick;
  
  private Path currentPath;
  
  private Vec3d targetLocation;
  
  public PhaseTakeoff(EntityDragon dragonIn) {
    super(dragonIn);
  }
  
  public void doLocalUpdate() {
    if (!this.firstTick && this.currentPath != null) {
      BlockPos blockpos = this.dragon.world.getTopSolidOrLiquidBlock(WorldGenEndPodium.END_PODIUM_LOCATION);
      double d0 = this.dragon.getDistanceSqToCenter(blockpos);
      if (d0 > 100.0D)
        this.dragon.getPhaseManager().setPhase(PhaseList.HOLDING_PATTERN); 
    } else {
      this.firstTick = false;
      findNewTarget();
    } 
  }
  
  public void initPhase() {
    this.firstTick = true;
    this.currentPath = null;
    this.targetLocation = null;
  }
  
  private void findNewTarget() {
    int i = this.dragon.initPathPoints();
    Vec3d vec3d = this.dragon.getHeadLookVec(1.0F);
    int j = this.dragon.getNearestPpIdx(-vec3d.xCoord * 40.0D, 105.0D, -vec3d.zCoord * 40.0D);
    if (this.dragon.getFightManager() != null && this.dragon.getFightManager().getNumAliveCrystals() > 0) {
      j %= 12;
      if (j < 0)
        j += 12; 
    } else {
      j -= 12;
      j &= 0x7;
      j += 12;
    } 
    this.currentPath = this.dragon.findPath(i, j, null);
    if (this.currentPath != null) {
      this.currentPath.incrementPathIndex();
      navigateToNextPathNode();
    } 
  }
  
  private void navigateToNextPathNode() {
    double d0;
    Vec3d vec3d = this.currentPath.getCurrentPos();
    this.currentPath.incrementPathIndex();
    do {
      d0 = vec3d.yCoord + (this.dragon.getRNG().nextFloat() * 20.0F);
    } while (d0 < vec3d.yCoord);
    this.targetLocation = new Vec3d(vec3d.xCoord, d0, vec3d.zCoord);
  }
  
  @Nullable
  public Vec3d getTargetLocation() {
    return this.targetLocation;
  }
  
  public PhaseList<PhaseTakeoff> getPhaseList() {
    return PhaseList.TAKEOFF;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\entity\boss\dragon\phase\PhaseTakeoff.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
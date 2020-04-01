package net.minecraft.entity.boss.dragon.phase;

import javax.annotation.Nullable;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.gen.feature.WorldGenEndPodium;

public class PhaseDying extends PhaseBase {
  private Vec3d targetLocation;
  
  private int time;
  
  public PhaseDying(EntityDragon dragonIn) {
    super(dragonIn);
  }
  
  public void doClientRenderEffects() {
    if (this.time++ % 10 == 0) {
      float f = (this.dragon.getRNG().nextFloat() - 0.5F) * 8.0F;
      float f1 = (this.dragon.getRNG().nextFloat() - 0.5F) * 4.0F;
      float f2 = (this.dragon.getRNG().nextFloat() - 0.5F) * 8.0F;
      this.dragon.world.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, this.dragon.posX + f, this.dragon.posY + 2.0D + f1, this.dragon.posZ + f2, 0.0D, 0.0D, 0.0D, new int[0]);
    } 
  }
  
  public void doLocalUpdate() {
    this.time++;
    if (this.targetLocation == null) {
      BlockPos blockpos = this.dragon.world.getHeight(WorldGenEndPodium.END_PODIUM_LOCATION);
      this.targetLocation = new Vec3d(blockpos.getX(), blockpos.getY(), blockpos.getZ());
    } 
    double d0 = this.targetLocation.squareDistanceTo(this.dragon.posX, this.dragon.posY, this.dragon.posZ);
    if (d0 >= 100.0D && d0 <= 22500.0D && !this.dragon.isCollidedHorizontally && !this.dragon.isCollidedVertically) {
      this.dragon.setHealth(1.0F);
    } else {
      this.dragon.setHealth(0.0F);
    } 
  }
  
  public void initPhase() {
    this.targetLocation = null;
    this.time = 0;
  }
  
  public float getMaxRiseOrFall() {
    return 3.0F;
  }
  
  @Nullable
  public Vec3d getTargetLocation() {
    return this.targetLocation;
  }
  
  public PhaseList<PhaseDying> getPhaseList() {
    return PhaseList.DYING;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\entity\boss\dragon\phase\PhaseDying.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
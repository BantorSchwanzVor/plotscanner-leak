package net.minecraft.entity.boss.dragon.phase;

import javax.annotation.Nullable;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.util.math.Vec3d;

public class PhaseHover extends PhaseBase {
  private Vec3d targetLocation;
  
  public PhaseHover(EntityDragon dragonIn) {
    super(dragonIn);
  }
  
  public void doLocalUpdate() {
    if (this.targetLocation == null)
      this.targetLocation = new Vec3d(this.dragon.posX, this.dragon.posY, this.dragon.posZ); 
  }
  
  public boolean getIsStationary() {
    return true;
  }
  
  public void initPhase() {
    this.targetLocation = null;
  }
  
  public float getMaxRiseOrFall() {
    return 1.0F;
  }
  
  @Nullable
  public Vec3d getTargetLocation() {
    return this.targetLocation;
  }
  
  public PhaseList<PhaseHover> getPhaseList() {
    return PhaseList.HOVER;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\entity\boss\dragon\phase\PhaseHover.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
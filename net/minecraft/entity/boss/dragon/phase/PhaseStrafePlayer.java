package net.minecraft.entity.boss.dragon.phase;

import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.projectile.EntityDragonFireball;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PhaseStrafePlayer extends PhaseBase {
  private static final Logger LOGGER = LogManager.getLogger();
  
  private int fireballCharge;
  
  private Path currentPath;
  
  private Vec3d targetLocation;
  
  private EntityLivingBase attackTarget;
  
  private boolean holdingPatternClockwise;
  
  public PhaseStrafePlayer(EntityDragon dragonIn) {
    super(dragonIn);
  }
  
  public void doLocalUpdate() {
    if (this.attackTarget == null) {
      LOGGER.warn("Skipping player strafe phase because no player was found");
      this.dragon.getPhaseManager().setPhase(PhaseList.HOLDING_PATTERN);
    } else {
      if (this.currentPath != null && this.currentPath.isFinished()) {
        double d0 = this.attackTarget.posX;
        double d1 = this.attackTarget.posZ;
        double d2 = d0 - this.dragon.posX;
        double d3 = d1 - this.dragon.posZ;
        double d4 = MathHelper.sqrt(d2 * d2 + d3 * d3);
        double d5 = Math.min(0.4000000059604645D + d4 / 80.0D - 1.0D, 10.0D);
        this.targetLocation = new Vec3d(d0, this.attackTarget.posY + d5, d1);
      } 
      double d12 = (this.targetLocation == null) ? 0.0D : this.targetLocation.squareDistanceTo(this.dragon.posX, this.dragon.posY, this.dragon.posZ);
      if (d12 < 100.0D || d12 > 22500.0D)
        findNewTarget(); 
      double d13 = 64.0D;
      if (this.attackTarget.getDistanceSqToEntity((Entity)this.dragon) < 4096.0D) {
        if (this.dragon.canEntityBeSeen((Entity)this.attackTarget)) {
          this.fireballCharge++;
          Vec3d vec3d1 = (new Vec3d(this.attackTarget.posX - this.dragon.posX, 0.0D, this.attackTarget.posZ - this.dragon.posZ)).normalize();
          Vec3d vec3d = (new Vec3d(MathHelper.sin(this.dragon.rotationYaw * 0.017453292F), 0.0D, -MathHelper.cos(this.dragon.rotationYaw * 0.017453292F))).normalize();
          float f1 = (float)vec3d.dotProduct(vec3d1);
          float f = (float)(Math.acos(f1) * 57.29577951308232D);
          f += 0.5F;
          if (this.fireballCharge >= 5 && f >= 0.0F && f < 10.0F) {
            double d14 = 1.0D;
            Vec3d vec3d2 = this.dragon.getLook(1.0F);
            double d6 = this.dragon.dragonPartHead.posX - vec3d2.xCoord * 1.0D;
            double d7 = this.dragon.dragonPartHead.posY + (this.dragon.dragonPartHead.height / 2.0F) + 0.5D;
            double d8 = this.dragon.dragonPartHead.posZ - vec3d2.zCoord * 1.0D;
            double d9 = this.attackTarget.posX - d6;
            double d10 = this.attackTarget.posY + (this.attackTarget.height / 2.0F) - d7 + (this.dragon.dragonPartHead.height / 2.0F);
            double d11 = this.attackTarget.posZ - d8;
            this.dragon.world.playEvent(null, 1017, new BlockPos((Entity)this.dragon), 0);
            EntityDragonFireball entitydragonfireball = new EntityDragonFireball(this.dragon.world, (EntityLivingBase)this.dragon, d9, d10, d11);
            entitydragonfireball.setLocationAndAngles(d6, d7, d8, 0.0F, 0.0F);
            this.dragon.world.spawnEntityInWorld((Entity)entitydragonfireball);
            this.fireballCharge = 0;
            if (this.currentPath != null)
              while (!this.currentPath.isFinished())
                this.currentPath.incrementPathIndex();  
            this.dragon.getPhaseManager().setPhase(PhaseList.HOLDING_PATTERN);
          } 
        } else if (this.fireballCharge > 0) {
          this.fireballCharge--;
        } 
      } else if (this.fireballCharge > 0) {
        this.fireballCharge--;
      } 
    } 
  }
  
  private void findNewTarget() {
    if (this.currentPath == null || this.currentPath.isFinished()) {
      int i = this.dragon.initPathPoints();
      int j = i;
      if (this.dragon.getRNG().nextInt(8) == 0) {
        this.holdingPatternClockwise = !this.holdingPatternClockwise;
        j = i + 6;
      } 
      if (this.holdingPatternClockwise) {
        j++;
      } else {
        j--;
      } 
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
      if (this.currentPath != null)
        this.currentPath.incrementPathIndex(); 
    } 
    navigateToNextPathNode();
  }
  
  private void navigateToNextPathNode() {
    if (this.currentPath != null && !this.currentPath.isFinished()) {
      double d1;
      Vec3d vec3d = this.currentPath.getCurrentPos();
      this.currentPath.incrementPathIndex();
      double d0 = vec3d.xCoord;
      double d2 = vec3d.zCoord;
      do {
        d1 = vec3d.yCoord + (this.dragon.getRNG().nextFloat() * 20.0F);
      } while (d1 < vec3d.yCoord);
      this.targetLocation = new Vec3d(d0, d1, d2);
    } 
  }
  
  public void initPhase() {
    this.fireballCharge = 0;
    this.targetLocation = null;
    this.currentPath = null;
    this.attackTarget = null;
  }
  
  public void setTarget(EntityLivingBase p_188686_1_) {
    this.attackTarget = p_188686_1_;
    int i = this.dragon.initPathPoints();
    int j = this.dragon.getNearestPpIdx(this.attackTarget.posX, this.attackTarget.posY, this.attackTarget.posZ);
    int k = MathHelper.floor(this.attackTarget.posX);
    int l = MathHelper.floor(this.attackTarget.posZ);
    double d0 = k - this.dragon.posX;
    double d1 = l - this.dragon.posZ;
    double d2 = MathHelper.sqrt(d0 * d0 + d1 * d1);
    double d3 = Math.min(0.4000000059604645D + d2 / 80.0D - 1.0D, 10.0D);
    int i1 = MathHelper.floor(this.attackTarget.posY + d3);
    PathPoint pathpoint = new PathPoint(k, i1, l);
    this.currentPath = this.dragon.findPath(i, j, pathpoint);
    if (this.currentPath != null) {
      this.currentPath.incrementPathIndex();
      navigateToNextPathNode();
    } 
  }
  
  @Nullable
  public Vec3d getTargetLocation() {
    return this.targetLocation;
  }
  
  public PhaseList<PhaseStrafePlayer> getPhaseList() {
    return PhaseList.STRAFE_PLAYER;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\entity\boss\dragon\phase\PhaseStrafePlayer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
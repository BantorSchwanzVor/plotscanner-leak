package net.minecraft.entity.ai;

import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class EntityAIFollowOwner extends EntityAIBase {
  private final EntityTameable thePet;
  
  private EntityLivingBase theOwner;
  
  World theWorld;
  
  private final double followSpeed;
  
  private final PathNavigate petPathfinder;
  
  private int timeToRecalcPath;
  
  float maxDist;
  
  float minDist;
  
  private float oldWaterCost;
  
  public EntityAIFollowOwner(EntityTameable thePetIn, double followSpeedIn, float minDistIn, float maxDistIn) {
    this.thePet = thePetIn;
    this.theWorld = thePetIn.world;
    this.followSpeed = followSpeedIn;
    this.petPathfinder = thePetIn.getNavigator();
    this.minDist = minDistIn;
    this.maxDist = maxDistIn;
    setMutexBits(3);
    if (!(thePetIn.getNavigator() instanceof net.minecraft.pathfinding.PathNavigateGround) && !(thePetIn.getNavigator() instanceof net.minecraft.pathfinding.PathNavigateFlying))
      throw new IllegalArgumentException("Unsupported mob type for FollowOwnerGoal"); 
  }
  
  public boolean shouldExecute() {
    EntityLivingBase entitylivingbase = this.thePet.getOwner();
    if (entitylivingbase == null)
      return false; 
    if (entitylivingbase instanceof EntityPlayer && ((EntityPlayer)entitylivingbase).isSpectator())
      return false; 
    if (this.thePet.isSitting())
      return false; 
    if (this.thePet.getDistanceSqToEntity((Entity)entitylivingbase) < (this.minDist * this.minDist))
      return false; 
    this.theOwner = entitylivingbase;
    return true;
  }
  
  public boolean continueExecuting() {
    return (!this.petPathfinder.noPath() && this.thePet.getDistanceSqToEntity((Entity)this.theOwner) > (this.maxDist * this.maxDist) && !this.thePet.isSitting());
  }
  
  public void startExecuting() {
    this.timeToRecalcPath = 0;
    this.oldWaterCost = this.thePet.getPathPriority(PathNodeType.WATER);
    this.thePet.setPathPriority(PathNodeType.WATER, 0.0F);
  }
  
  public void resetTask() {
    this.theOwner = null;
    this.petPathfinder.clearPathEntity();
    this.thePet.setPathPriority(PathNodeType.WATER, this.oldWaterCost);
  }
  
  public void updateTask() {
    this.thePet.getLookHelper().setLookPositionWithEntity((Entity)this.theOwner, 10.0F, this.thePet.getVerticalFaceSpeed());
    if (!this.thePet.isSitting())
      if (--this.timeToRecalcPath <= 0) {
        this.timeToRecalcPath = 10;
        if (!this.petPathfinder.tryMoveToEntityLiving((Entity)this.theOwner, this.followSpeed))
          if (!this.thePet.getLeashed() && !this.thePet.isRiding())
            if (this.thePet.getDistanceSqToEntity((Entity)this.theOwner) >= 144.0D) {
              int i = MathHelper.floor(this.theOwner.posX) - 2;
              int j = MathHelper.floor(this.theOwner.posZ) - 2;
              int k = MathHelper.floor((this.theOwner.getEntityBoundingBox()).minY);
              for (int l = 0; l <= 4; l++) {
                for (int i1 = 0; i1 <= 4; i1++) {
                  if ((l < 1 || i1 < 1 || l > 3 || i1 > 3) && func_192381_a(i, j, k, l, i1)) {
                    this.thePet.setLocationAndAngles(((i + l) + 0.5F), k, ((j + i1) + 0.5F), this.thePet.rotationYaw, this.thePet.rotationPitch);
                    this.petPathfinder.clearPathEntity();
                    return;
                  } 
                } 
              } 
            }   
      }  
  }
  
  protected boolean func_192381_a(int p_192381_1_, int p_192381_2_, int p_192381_3_, int p_192381_4_, int p_192381_5_) {
    BlockPos blockpos = new BlockPos(p_192381_1_ + p_192381_4_, p_192381_3_ - 1, p_192381_2_ + p_192381_5_);
    IBlockState iblockstate = this.theWorld.getBlockState(blockpos);
    return (iblockstate.func_193401_d((IBlockAccess)this.theWorld, blockpos, EnumFacing.DOWN) == BlockFaceShape.SOLID && iblockstate.canEntitySpawn((Entity)this.thePet) && this.theWorld.isAirBlock(blockpos.up()) && this.theWorld.isAirBlock(blockpos.up(2)));
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\entity\ai\EntityAIFollowOwner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
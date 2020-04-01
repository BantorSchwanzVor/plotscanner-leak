package net.minecraft.entity.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.math.BlockPos;
import net.minecraft.village.Village;
import net.minecraft.village.VillageDoorInfo;

public class EntityAIRestrictOpenDoor extends EntityAIBase {
  private final EntityCreature entityObj;
  
  private VillageDoorInfo frontDoor;
  
  public EntityAIRestrictOpenDoor(EntityCreature creatureIn) {
    this.entityObj = creatureIn;
    if (!(creatureIn.getNavigator() instanceof PathNavigateGround))
      throw new IllegalArgumentException("Unsupported mob type for RestrictOpenDoorGoal"); 
  }
  
  public boolean shouldExecute() {
    if (this.entityObj.world.isDaytime())
      return false; 
    BlockPos blockpos = new BlockPos((Entity)this.entityObj);
    Village village = this.entityObj.world.getVillageCollection().getNearestVillage(blockpos, 16);
    if (village == null)
      return false; 
    this.frontDoor = village.getNearestDoor(blockpos);
    if (this.frontDoor == null)
      return false; 
    return (this.frontDoor.getDistanceToInsideBlockSq(blockpos) < 2.25D);
  }
  
  public boolean continueExecuting() {
    if (this.entityObj.world.isDaytime())
      return false; 
    return (!this.frontDoor.getIsDetachedFromVillageFlag() && this.frontDoor.isInsideSide(new BlockPos((Entity)this.entityObj)));
  }
  
  public void startExecuting() {
    ((PathNavigateGround)this.entityObj.getNavigator()).setBreakDoors(false);
    ((PathNavigateGround)this.entityObj.getNavigator()).setEnterDoors(false);
  }
  
  public void resetTask() {
    ((PathNavigateGround)this.entityObj.getNavigator()).setBreakDoors(true);
    ((PathNavigateGround)this.entityObj.getNavigator()).setEnterDoors(true);
    this.frontDoor = null;
  }
  
  public void updateTask() {
    this.frontDoor.incrementDoorOpeningRestrictionCounter();
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\entity\ai\EntityAIRestrictOpenDoor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
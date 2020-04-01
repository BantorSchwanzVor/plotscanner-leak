package net.minecraft.entity.ai;

import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.passive.EntityVillager;

public class EntityAIFollowGolem extends EntityAIBase {
  private final EntityVillager theVillager;
  
  private EntityIronGolem theGolem;
  
  private int takeGolemRoseTick;
  
  private boolean tookGolemRose;
  
  public EntityAIFollowGolem(EntityVillager theVillagerIn) {
    this.theVillager = theVillagerIn;
    setMutexBits(3);
  }
  
  public boolean shouldExecute() {
    if (this.theVillager.getGrowingAge() >= 0)
      return false; 
    if (!this.theVillager.world.isDaytime())
      return false; 
    List<EntityIronGolem> list = this.theVillager.world.getEntitiesWithinAABB(EntityIronGolem.class, this.theVillager.getEntityBoundingBox().expand(6.0D, 2.0D, 6.0D));
    if (list.isEmpty())
      return false; 
    for (EntityIronGolem entityirongolem : list) {
      if (entityirongolem.getHoldRoseTick() > 0) {
        this.theGolem = entityirongolem;
        break;
      } 
    } 
    return (this.theGolem != null);
  }
  
  public boolean continueExecuting() {
    return (this.theGolem.getHoldRoseTick() > 0);
  }
  
  public void startExecuting() {
    this.takeGolemRoseTick = this.theVillager.getRNG().nextInt(320);
    this.tookGolemRose = false;
    this.theGolem.getNavigator().clearPathEntity();
  }
  
  public void resetTask() {
    this.theGolem = null;
    this.theVillager.getNavigator().clearPathEntity();
  }
  
  public void updateTask() {
    this.theVillager.getLookHelper().setLookPositionWithEntity((Entity)this.theGolem, 30.0F, 30.0F);
    if (this.theGolem.getHoldRoseTick() == this.takeGolemRoseTick) {
      this.theVillager.getNavigator().tryMoveToEntityLiving((Entity)this.theGolem, 0.5D);
      this.tookGolemRose = true;
    } 
    if (this.tookGolemRose && this.theVillager.getDistanceSqToEntity((Entity)this.theGolem) < 4.0D) {
      this.theGolem.setHoldingRose(false);
      this.theVillager.getNavigator().clearPathEntity();
    } 
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\entity\ai\EntityAIFollowGolem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
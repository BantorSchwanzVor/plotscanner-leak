package net.minecraft.entity.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;

public class EntityAITradePlayer extends EntityAIBase {
  private final EntityVillager villager;
  
  public EntityAITradePlayer(EntityVillager villagerIn) {
    this.villager = villagerIn;
    setMutexBits(5);
  }
  
  public boolean shouldExecute() {
    if (!this.villager.isEntityAlive())
      return false; 
    if (this.villager.isInWater())
      return false; 
    if (!this.villager.onGround)
      return false; 
    if (this.villager.velocityChanged)
      return false; 
    EntityPlayer entityplayer = this.villager.getCustomer();
    if (entityplayer == null)
      return false; 
    if (this.villager.getDistanceSqToEntity((Entity)entityplayer) > 16.0D)
      return false; 
    return (entityplayer.openContainer != null);
  }
  
  public void startExecuting() {
    this.villager.getNavigator().clearPathEntity();
  }
  
  public void resetTask() {
    this.villager.setCustomer(null);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\entity\ai\EntityAITradePlayer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
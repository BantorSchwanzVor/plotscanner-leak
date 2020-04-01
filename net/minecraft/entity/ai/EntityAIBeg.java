package net.minecraft.entity.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class EntityAIBeg extends EntityAIBase {
  private final EntityWolf theWolf;
  
  private EntityPlayer thePlayer;
  
  private final World worldObject;
  
  private final float minPlayerDistance;
  
  private int timeoutCounter;
  
  public EntityAIBeg(EntityWolf wolf, float minDistance) {
    this.theWolf = wolf;
    this.worldObject = wolf.world;
    this.minPlayerDistance = minDistance;
    setMutexBits(2);
  }
  
  public boolean shouldExecute() {
    this.thePlayer = this.worldObject.getClosestPlayerToEntity((Entity)this.theWolf, this.minPlayerDistance);
    return (this.thePlayer == null) ? false : hasPlayerGotBoneInHand(this.thePlayer);
  }
  
  public boolean continueExecuting() {
    if (!this.thePlayer.isEntityAlive())
      return false; 
    if (this.theWolf.getDistanceSqToEntity((Entity)this.thePlayer) > (this.minPlayerDistance * this.minPlayerDistance))
      return false; 
    return (this.timeoutCounter > 0 && hasPlayerGotBoneInHand(this.thePlayer));
  }
  
  public void startExecuting() {
    this.theWolf.setBegging(true);
    this.timeoutCounter = 40 + this.theWolf.getRNG().nextInt(40);
  }
  
  public void resetTask() {
    this.theWolf.setBegging(false);
    this.thePlayer = null;
  }
  
  public void updateTask() {
    this.theWolf.getLookHelper().setLookPosition(this.thePlayer.posX, this.thePlayer.posY + this.thePlayer.getEyeHeight(), this.thePlayer.posZ, 10.0F, this.theWolf.getVerticalFaceSpeed());
    this.timeoutCounter--;
  }
  
  private boolean hasPlayerGotBoneInHand(EntityPlayer player) {
    byte b;
    int i;
    EnumHand[] arrayOfEnumHand;
    for (i = (arrayOfEnumHand = EnumHand.values()).length, b = 0; b < i; ) {
      EnumHand enumhand = arrayOfEnumHand[b];
      ItemStack itemstack = player.getHeldItem(enumhand);
      if (this.theWolf.isTamed() && itemstack.getItem() == Items.BONE)
        return true; 
      if (this.theWolf.isBreedingItem(itemstack))
        return true; 
      b++;
    } 
    return false;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\entity\ai\EntityAIBeg.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
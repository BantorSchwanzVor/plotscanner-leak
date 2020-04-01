package net.minecraft.entity.ai;

import com.google.common.collect.Sets;
import java.util.Set;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class EntityAITempt extends EntityAIBase {
  private final EntityCreature temptedEntity;
  
  private final double speed;
  
  private double targetX;
  
  private double targetY;
  
  private double targetZ;
  
  private double pitch;
  
  private double yaw;
  
  private EntityPlayer temptingPlayer;
  
  private int delayTemptCounter;
  
  private boolean isRunning;
  
  private final Set<Item> temptItem;
  
  private final boolean scaredByPlayerMovement;
  
  public EntityAITempt(EntityCreature temptedEntityIn, double speedIn, Item temptItemIn, boolean scaredByPlayerMovementIn) {
    this(temptedEntityIn, speedIn, scaredByPlayerMovementIn, Sets.newHashSet((Object[])new Item[] { temptItemIn }));
  }
  
  public EntityAITempt(EntityCreature temptedEntityIn, double speedIn, boolean scaredByPlayerMovementIn, Set<Item> temptItemIn) {
    this.temptedEntity = temptedEntityIn;
    this.speed = speedIn;
    this.temptItem = temptItemIn;
    this.scaredByPlayerMovement = scaredByPlayerMovementIn;
    setMutexBits(3);
    if (!(temptedEntityIn.getNavigator() instanceof net.minecraft.pathfinding.PathNavigateGround))
      throw new IllegalArgumentException("Unsupported mob type for TemptGoal"); 
  }
  
  public boolean shouldExecute() {
    if (this.delayTemptCounter > 0) {
      this.delayTemptCounter--;
      return false;
    } 
    this.temptingPlayer = this.temptedEntity.world.getClosestPlayerToEntity((Entity)this.temptedEntity, 10.0D);
    if (this.temptingPlayer == null)
      return false; 
    return !(!isTempting(this.temptingPlayer.getHeldItemMainhand()) && !isTempting(this.temptingPlayer.getHeldItemOffhand()));
  }
  
  protected boolean isTempting(ItemStack stack) {
    return this.temptItem.contains(stack.getItem());
  }
  
  public boolean continueExecuting() {
    if (this.scaredByPlayerMovement) {
      if (this.temptedEntity.getDistanceSqToEntity((Entity)this.temptingPlayer) < 36.0D) {
        if (this.temptingPlayer.getDistanceSq(this.targetX, this.targetY, this.targetZ) > 0.010000000000000002D)
          return false; 
        if (Math.abs(this.temptingPlayer.rotationPitch - this.pitch) > 5.0D || Math.abs(this.temptingPlayer.rotationYaw - this.yaw) > 5.0D)
          return false; 
      } else {
        this.targetX = this.temptingPlayer.posX;
        this.targetY = this.temptingPlayer.posY;
        this.targetZ = this.temptingPlayer.posZ;
      } 
      this.pitch = this.temptingPlayer.rotationPitch;
      this.yaw = this.temptingPlayer.rotationYaw;
    } 
    return shouldExecute();
  }
  
  public void startExecuting() {
    this.targetX = this.temptingPlayer.posX;
    this.targetY = this.temptingPlayer.posY;
    this.targetZ = this.temptingPlayer.posZ;
    this.isRunning = true;
  }
  
  public void resetTask() {
    this.temptingPlayer = null;
    this.temptedEntity.getNavigator().clearPathEntity();
    this.delayTemptCounter = 100;
    this.isRunning = false;
  }
  
  public void updateTask() {
    this.temptedEntity.getLookHelper().setLookPositionWithEntity((Entity)this.temptingPlayer, (this.temptedEntity.getHorizontalFaceSpeed() + 20), this.temptedEntity.getVerticalFaceSpeed());
    if (this.temptedEntity.getDistanceSqToEntity((Entity)this.temptingPlayer) < 6.25D) {
      this.temptedEntity.getNavigator().clearPathEntity();
    } else {
      this.temptedEntity.getNavigator().tryMoveToEntityLiving((Entity)this.temptingPlayer, this.speed);
    } 
  }
  
  public boolean isRunning() {
    return this.isRunning;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\entity\ai\EntityAITempt.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
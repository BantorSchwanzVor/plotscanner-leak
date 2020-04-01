package net.minecraft.entity.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;

public class EntityAIRunAroundLikeCrazy extends EntityAIBase {
  private final AbstractHorse horseHost;
  
  private final double speed;
  
  private double targetX;
  
  private double targetY;
  
  private double targetZ;
  
  public EntityAIRunAroundLikeCrazy(AbstractHorse horse, double speedIn) {
    this.horseHost = horse;
    this.speed = speedIn;
    setMutexBits(1);
  }
  
  public boolean shouldExecute() {
    if (!this.horseHost.isTame() && this.horseHost.isBeingRidden()) {
      Vec3d vec3d = RandomPositionGenerator.findRandomTarget((EntityCreature)this.horseHost, 5, 4);
      if (vec3d == null)
        return false; 
      this.targetX = vec3d.xCoord;
      this.targetY = vec3d.yCoord;
      this.targetZ = vec3d.zCoord;
      return true;
    } 
    return false;
  }
  
  public void startExecuting() {
    this.horseHost.getNavigator().tryMoveToXYZ(this.targetX, this.targetY, this.targetZ, this.speed);
  }
  
  public boolean continueExecuting() {
    return (!this.horseHost.isTame() && !this.horseHost.getNavigator().noPath() && this.horseHost.isBeingRidden());
  }
  
  public void updateTask() {
    if (!this.horseHost.isTame() && this.horseHost.getRNG().nextInt(50) == 0) {
      Entity entity = this.horseHost.getPassengers().get(0);
      if (entity == null)
        return; 
      if (entity instanceof EntityPlayer) {
        int i = this.horseHost.getTemper();
        int j = this.horseHost.func_190676_dC();
        if (j > 0 && this.horseHost.getRNG().nextInt(j) < i) {
          this.horseHost.setTamedBy((EntityPlayer)entity);
          return;
        } 
        this.horseHost.increaseTemper(5);
      } 
      this.horseHost.removePassengers();
      this.horseHost.func_190687_dF();
      this.horseHost.world.setEntityState((Entity)this.horseHost, (byte)6);
    } 
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\entity\ai\EntityAIRunAroundLikeCrazy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
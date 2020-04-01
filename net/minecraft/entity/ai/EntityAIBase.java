package net.minecraft.entity.ai;

public abstract class EntityAIBase {
  private int mutexBits;
  
  public abstract boolean shouldExecute();
  
  public boolean continueExecuting() {
    return shouldExecute();
  }
  
  public boolean isInterruptible() {
    return true;
  }
  
  public void startExecuting() {}
  
  public void resetTask() {}
  
  public void updateTask() {}
  
  public void setMutexBits(int mutexBitsIn) {
    this.mutexBits = mutexBitsIn;
  }
  
  public int getMutexBits() {
    return this.mutexBits;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\entity\ai\EntityAIBase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
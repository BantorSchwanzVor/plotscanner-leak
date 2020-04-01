package net.minecraft.world.storage;

public class WorldSavedDataCallableSave implements Runnable {
  private final WorldSavedData data;
  
  public WorldSavedDataCallableSave(WorldSavedData dataIn) {
    this.data = dataIn;
  }
  
  public void run() {
    this.data.markDirty();
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\world\storage\WorldSavedDataCallableSave.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
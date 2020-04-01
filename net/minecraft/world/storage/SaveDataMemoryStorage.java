package net.minecraft.world.storage;

import javax.annotation.Nullable;

public class SaveDataMemoryStorage extends MapStorage {
  public SaveDataMemoryStorage() {
    super(null);
  }
  
  @Nullable
  public WorldSavedData getOrLoadData(Class<? extends WorldSavedData> clazz, String dataIdentifier) {
    return this.loadedDataMap.get(dataIdentifier);
  }
  
  public void setData(String dataIdentifier, WorldSavedData data) {
    this.loadedDataMap.put(dataIdentifier, data);
  }
  
  public void saveAllData() {}
  
  public int getUniqueDataId(String key) {
    return 0;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\world\storage\SaveDataMemoryStorage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
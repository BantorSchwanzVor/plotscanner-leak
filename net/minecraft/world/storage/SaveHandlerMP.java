package net.minecraft.world.storage;

import java.io.File;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.storage.IChunkLoader;
import net.minecraft.world.gen.structure.template.TemplateManager;

public class SaveHandlerMP implements ISaveHandler {
  public WorldInfo loadWorldInfo() {
    return null;
  }
  
  public void checkSessionLock() throws MinecraftException {}
  
  public IChunkLoader getChunkLoader(WorldProvider provider) {
    return null;
  }
  
  public void saveWorldInfoWithPlayer(WorldInfo worldInformation, NBTTagCompound tagCompound) {}
  
  public void saveWorldInfo(WorldInfo worldInformation) {}
  
  public IPlayerFileData getPlayerNBTManager() {
    return null;
  }
  
  public void flush() {}
  
  public File getMapFileFromName(String mapName) {
    return null;
  }
  
  public File getWorldDirectory() {
    return null;
  }
  
  public TemplateManager getStructureTemplateManager() {
    return null;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\world\storage\SaveHandlerMP.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
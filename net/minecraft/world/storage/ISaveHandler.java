package net.minecraft.world.storage;

import java.io.File;
import javax.annotation.Nullable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.storage.IChunkLoader;
import net.minecraft.world.gen.structure.template.TemplateManager;

public interface ISaveHandler {
  @Nullable
  WorldInfo loadWorldInfo();
  
  void checkSessionLock() throws MinecraftException;
  
  IChunkLoader getChunkLoader(WorldProvider paramWorldProvider);
  
  void saveWorldInfoWithPlayer(WorldInfo paramWorldInfo, NBTTagCompound paramNBTTagCompound);
  
  void saveWorldInfo(WorldInfo paramWorldInfo);
  
  IPlayerFileData getPlayerNBTManager();
  
  void flush();
  
  File getWorldDirectory();
  
  File getMapFileFromName(String paramString);
  
  TemplateManager getStructureTemplateManager();
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\world\storage\ISaveHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
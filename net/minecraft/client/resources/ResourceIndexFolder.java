package net.minecraft.client.resources;

import java.io.File;
import net.minecraft.util.ResourceLocation;

public class ResourceIndexFolder extends ResourceIndex {
  private final File baseDir;
  
  public ResourceIndexFolder(File folder) {
    this.baseDir = folder;
  }
  
  public File getFile(ResourceLocation location) {
    return new File(this.baseDir, location.toString().replace(':', '/'));
  }
  
  public File getPackMcmeta() {
    return new File(this.baseDir, "pack.mcmeta");
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\resources\ResourceIndexFolder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
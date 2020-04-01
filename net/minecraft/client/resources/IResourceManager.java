package net.minecraft.client.resources;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import net.minecraft.util.ResourceLocation;

public interface IResourceManager {
  Set<String> getResourceDomains();
  
  IResource getResource(ResourceLocation paramResourceLocation) throws IOException;
  
  List<IResource> getAllResources(ResourceLocation paramResourceLocation) throws IOException;
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\resources\IResourceManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
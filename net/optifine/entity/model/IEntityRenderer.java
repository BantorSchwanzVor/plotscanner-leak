package net.optifine.entity.model;

import net.minecraft.util.ResourceLocation;

public interface IEntityRenderer {
  Class getEntityClass();
  
  void setEntityClass(Class paramClass);
  
  ResourceLocation getLocationTextureCustom();
  
  void setLocationTextureCustom(ResourceLocation paramResourceLocation);
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\optifine\entity\model\IEntityRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
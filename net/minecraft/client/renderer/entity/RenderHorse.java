package net.minecraft.client.renderer.entity;

import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelHorse;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.LayeredTexture;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.util.ResourceLocation;

public class RenderHorse extends RenderLiving<EntityHorse> {
  private static final Map<String, ResourceLocation> LAYERED_LOCATION_CACHE = Maps.newHashMap();
  
  public RenderHorse(RenderManager p_i47205_1_) {
    super(p_i47205_1_, (ModelBase)new ModelHorse(), 0.75F);
  }
  
  protected ResourceLocation getEntityTexture(EntityHorse entity) {
    String s = entity.getHorseTexture();
    ResourceLocation resourcelocation = LAYERED_LOCATION_CACHE.get(s);
    if (resourcelocation == null) {
      resourcelocation = new ResourceLocation(s);
      Minecraft.getMinecraft().getTextureManager().loadTexture(resourcelocation, (ITextureObject)new LayeredTexture(entity.getVariantTexturePaths()));
      LAYERED_LOCATION_CACHE.put(s, resourcelocation);
    } 
    return resourcelocation;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\renderer\entity\RenderHorse.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
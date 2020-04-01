package net.minecraft.client.renderer.entity;

import net.minecraft.client.renderer.entity.layers.LayerStrayClothing;
import net.minecraft.entity.monster.AbstractSkeleton;
import net.minecraft.util.ResourceLocation;

public class RenderStray extends RenderSkeleton {
  private static final ResourceLocation STRAY_SKELETON_TEXTURES = new ResourceLocation("textures/entity/skeleton/stray.png");
  
  public RenderStray(RenderManager p_i47191_1_) {
    super(p_i47191_1_);
    addLayer(new LayerStrayClothing(this));
  }
  
  protected ResourceLocation getEntityTexture(AbstractSkeleton entity) {
    return STRAY_SKELETON_TEXTURES;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\renderer\entity\RenderStray.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
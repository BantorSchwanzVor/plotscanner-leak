package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelPig;
import net.minecraft.client.renderer.entity.layers.LayerSaddle;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.util.ResourceLocation;

public class RenderPig extends RenderLiving<EntityPig> {
  private static final ResourceLocation PIG_TEXTURES = new ResourceLocation("textures/entity/pig/pig.png");
  
  public RenderPig(RenderManager p_i47198_1_) {
    super(p_i47198_1_, (ModelBase)new ModelPig(), 0.7F);
    addLayer(new LayerSaddle(this));
  }
  
  protected ResourceLocation getEntityTexture(EntityPig entity) {
    return PIG_TEXTURES;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\renderer\entity\RenderPig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
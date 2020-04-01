package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelLlama;
import net.minecraft.client.renderer.entity.layers.LayerLlamaDecor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityLlama;
import net.minecraft.util.ResourceLocation;

public class RenderLlama extends RenderLiving<EntityLlama> {
  private static final ResourceLocation[] field_191350_a = new ResourceLocation[] { new ResourceLocation("textures/entity/llama/llama_creamy.png"), new ResourceLocation("textures/entity/llama/llama_white.png"), new ResourceLocation("textures/entity/llama/llama_brown.png"), new ResourceLocation("textures/entity/llama/llama_gray.png") };
  
  public RenderLlama(RenderManager p_i47203_1_) {
    super(p_i47203_1_, (ModelBase)new ModelLlama(0.0F), 0.7F);
    addLayer(new LayerLlamaDecor(this));
  }
  
  protected ResourceLocation getEntityTexture(EntityLlama entity) {
    return field_191350_a[entity.func_190719_dM()];
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\renderer\entity\RenderLlama.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
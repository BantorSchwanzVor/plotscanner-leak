package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelSheep2;
import net.minecraft.client.renderer.entity.layers.LayerSheepWool;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.util.ResourceLocation;

public class RenderSheep extends RenderLiving<EntitySheep> {
  private static final ResourceLocation SHEARED_SHEEP_TEXTURES = new ResourceLocation("textures/entity/sheep/sheep.png");
  
  public RenderSheep(RenderManager p_i47195_1_) {
    super(p_i47195_1_, (ModelBase)new ModelSheep2(), 0.7F);
    addLayer(new LayerSheepWool(this));
  }
  
  protected ResourceLocation getEntityTexture(EntitySheep entity) {
    return SHEARED_SHEEP_TEXTURES;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\renderer\entity\RenderSheep.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
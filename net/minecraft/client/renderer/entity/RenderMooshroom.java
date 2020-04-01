package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelCow;
import net.minecraft.client.renderer.entity.layers.LayerMooshroomMushroom;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityMooshroom;
import net.minecraft.util.ResourceLocation;

public class RenderMooshroom extends RenderLiving<EntityMooshroom> {
  private static final ResourceLocation MOOSHROOM_TEXTURES = new ResourceLocation("textures/entity/cow/mooshroom.png");
  
  public RenderMooshroom(RenderManager p_i47200_1_) {
    super(p_i47200_1_, (ModelBase)new ModelCow(), 0.7F);
    addLayer(new LayerMooshroomMushroom(this));
  }
  
  public ModelCow getMainModel() {
    return (ModelCow)super.getMainModel();
  }
  
  protected ResourceLocation getEntityTexture(EntityMooshroom entity) {
    return MOOSHROOM_TEXTURES;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\renderer\entity\RenderMooshroom.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelCow;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.util.ResourceLocation;

public class RenderCow extends RenderLiving<EntityCow> {
  private static final ResourceLocation COW_TEXTURES = new ResourceLocation("textures/entity/cow/cow.png");
  
  public RenderCow(RenderManager p_i47210_1_) {
    super(p_i47210_1_, (ModelBase)new ModelCow(), 0.7F);
  }
  
  protected ResourceLocation getEntityTexture(EntityCow entity) {
    return COW_TEXTURES;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\renderer\entity\RenderCow.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
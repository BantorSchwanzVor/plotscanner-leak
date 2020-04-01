package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBlaze;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.util.ResourceLocation;

public class RenderBlaze extends RenderLiving<EntityBlaze> {
  private static final ResourceLocation BLAZE_TEXTURES = new ResourceLocation("textures/entity/blaze.png");
  
  public RenderBlaze(RenderManager renderManagerIn) {
    super(renderManagerIn, (ModelBase)new ModelBlaze(), 0.5F);
  }
  
  protected ResourceLocation getEntityTexture(EntityBlaze entity) {
    return BLAZE_TEXTURES;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\renderer\entity\RenderBlaze.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
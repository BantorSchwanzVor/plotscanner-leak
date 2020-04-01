package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelMagmaCube;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.util.ResourceLocation;

public class RenderMagmaCube extends RenderLiving<EntityMagmaCube> {
  private static final ResourceLocation MAGMA_CUBE_TEXTURES = new ResourceLocation("textures/entity/slime/magmacube.png");
  
  public RenderMagmaCube(RenderManager renderManagerIn) {
    super(renderManagerIn, (ModelBase)new ModelMagmaCube(), 0.25F);
  }
  
  protected ResourceLocation getEntityTexture(EntityMagmaCube entity) {
    return MAGMA_CUBE_TEXTURES;
  }
  
  protected void preRenderCallback(EntityMagmaCube entitylivingbaseIn, float partialTickTime) {
    int i = entitylivingbaseIn.getSlimeSize();
    float f = (entitylivingbaseIn.prevSquishFactor + (entitylivingbaseIn.squishFactor - entitylivingbaseIn.prevSquishFactor) * partialTickTime) / (i * 0.5F + 1.0F);
    float f1 = 1.0F / (f + 1.0F);
    GlStateManager.scale(f1 * i, 1.0F / f1 * i, f1 * i);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\renderer\entity\RenderMagmaCube.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
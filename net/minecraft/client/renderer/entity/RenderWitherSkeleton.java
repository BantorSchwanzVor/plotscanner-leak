package net.minecraft.client.renderer.entity;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.AbstractSkeleton;
import net.minecraft.util.ResourceLocation;

public class RenderWitherSkeleton extends RenderSkeleton {
  private static final ResourceLocation WITHER_SKELETON_TEXTURES = new ResourceLocation("textures/entity/skeleton/wither_skeleton.png");
  
  public RenderWitherSkeleton(RenderManager p_i47188_1_) {
    super(p_i47188_1_);
  }
  
  protected ResourceLocation getEntityTexture(AbstractSkeleton entity) {
    return WITHER_SKELETON_TEXTURES;
  }
  
  protected void preRenderCallback(AbstractSkeleton entitylivingbaseIn, float partialTickTime) {
    GlStateManager.scale(1.2F, 1.2F, 1.2F);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\renderer\entity\RenderWitherSkeleton.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
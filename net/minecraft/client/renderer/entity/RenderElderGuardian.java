package net.minecraft.client.renderer.entity;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.util.ResourceLocation;

public class RenderElderGuardian extends RenderGuardian {
  private static final ResourceLocation GUARDIAN_ELDER_TEXTURE = new ResourceLocation("textures/entity/guardian_elder.png");
  
  public RenderElderGuardian(RenderManager p_i47209_1_) {
    super(p_i47209_1_);
  }
  
  protected void preRenderCallback(EntityGuardian entitylivingbaseIn, float partialTickTime) {
    GlStateManager.scale(2.35F, 2.35F, 2.35F);
  }
  
  protected ResourceLocation getEntityTexture(EntityGuardian entity) {
    return GUARDIAN_ELDER_TEXTURE;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\renderer\entity\RenderElderGuardian.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
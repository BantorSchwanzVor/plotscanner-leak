package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelSquid;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.util.ResourceLocation;

public class RenderSquid extends RenderLiving<EntitySquid> {
  private static final ResourceLocation SQUID_TEXTURES = new ResourceLocation("textures/entity/squid.png");
  
  public RenderSquid(RenderManager p_i47192_1_) {
    super(p_i47192_1_, (ModelBase)new ModelSquid(), 0.7F);
  }
  
  protected ResourceLocation getEntityTexture(EntitySquid entity) {
    return SQUID_TEXTURES;
  }
  
  protected void rotateCorpse(EntitySquid entityLiving, float p_77043_2_, float p_77043_3_, float partialTicks) {
    float f = entityLiving.prevSquidPitch + (entityLiving.squidPitch - entityLiving.prevSquidPitch) * partialTicks;
    float f1 = entityLiving.prevSquidYaw + (entityLiving.squidYaw - entityLiving.prevSquidYaw) * partialTicks;
    GlStateManager.translate(0.0F, 0.5F, 0.0F);
    GlStateManager.rotate(180.0F - p_77043_3_, 0.0F, 1.0F, 0.0F);
    GlStateManager.rotate(f, 1.0F, 0.0F, 0.0F);
    GlStateManager.rotate(f1, 0.0F, 1.0F, 0.0F);
    GlStateManager.translate(0.0F, -1.2F, 0.0F);
  }
  
  protected float handleRotationFloat(EntitySquid livingBase, float partialTicks) {
    return livingBase.lastTentacleAngle + (livingBase.tentacleAngle - livingBase.lastTentacleAngle) * partialTicks;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\renderer\entity\RenderSquid.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
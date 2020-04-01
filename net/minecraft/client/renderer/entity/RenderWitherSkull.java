package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelSkeletonHead;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityWitherSkull;
import net.minecraft.util.ResourceLocation;

public class RenderWitherSkull extends Render<EntityWitherSkull> {
  private static final ResourceLocation INVULNERABLE_WITHER_TEXTURES = new ResourceLocation("textures/entity/wither/wither_invulnerable.png");
  
  private static final ResourceLocation WITHER_TEXTURES = new ResourceLocation("textures/entity/wither/wither.png");
  
  private final ModelSkeletonHead skeletonHeadModel = new ModelSkeletonHead();
  
  public RenderWitherSkull(RenderManager renderManagerIn) {
    super(renderManagerIn);
  }
  
  private float getRenderYaw(float p_82400_1_, float p_82400_2_, float p_82400_3_) {
    float f;
    for (f = p_82400_2_ - p_82400_1_; f < -180.0F; f += 360.0F);
    while (f >= 180.0F)
      f -= 360.0F; 
    return p_82400_1_ + p_82400_3_ * f;
  }
  
  public void doRender(EntityWitherSkull entity, double x, double y, double z, float entityYaw, float partialTicks) {
    GlStateManager.pushMatrix();
    GlStateManager.disableCull();
    float f = getRenderYaw(entity.prevRotationYaw, entity.rotationYaw, partialTicks);
    float f1 = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks;
    GlStateManager.translate((float)x, (float)y, (float)z);
    float f2 = 0.0625F;
    GlStateManager.enableRescaleNormal();
    GlStateManager.scale(-1.0F, -1.0F, 1.0F);
    GlStateManager.enableAlpha();
    bindEntityTexture(entity);
    if (this.renderOutlines) {
      GlStateManager.enableColorMaterial();
      GlStateManager.enableOutlineMode(getTeamColor(entity));
    } 
    this.skeletonHeadModel.render((Entity)entity, 0.0F, 0.0F, 0.0F, f, f1, 0.0625F);
    if (this.renderOutlines) {
      GlStateManager.disableOutlineMode();
      GlStateManager.disableColorMaterial();
    } 
    GlStateManager.popMatrix();
    super.doRender(entity, x, y, z, entityYaw, partialTicks);
  }
  
  protected ResourceLocation getEntityTexture(EntityWitherSkull entity) {
    return entity.isInvulnerable() ? INVULNERABLE_WITHER_TEXTURES : WITHER_TEXTURES;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\renderer\entity\RenderWitherSkull.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
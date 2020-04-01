package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelLeashKnot;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLeashKnot;
import net.minecraft.util.ResourceLocation;

public class RenderLeashKnot extends Render<EntityLeashKnot> {
  private static final ResourceLocation LEASH_KNOT_TEXTURES = new ResourceLocation("textures/entity/lead_knot.png");
  
  private final ModelLeashKnot leashKnotModel = new ModelLeashKnot();
  
  public RenderLeashKnot(RenderManager renderManagerIn) {
    super(renderManagerIn);
  }
  
  public void doRender(EntityLeashKnot entity, double x, double y, double z, float entityYaw, float partialTicks) {
    GlStateManager.pushMatrix();
    GlStateManager.disableCull();
    GlStateManager.translate((float)x, (float)y, (float)z);
    float f = 0.0625F;
    GlStateManager.enableRescaleNormal();
    GlStateManager.scale(-1.0F, -1.0F, 1.0F);
    GlStateManager.enableAlpha();
    bindEntityTexture(entity);
    if (this.renderOutlines) {
      GlStateManager.enableColorMaterial();
      GlStateManager.enableOutlineMode(getTeamColor(entity));
    } 
    this.leashKnotModel.render((Entity)entity, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
    if (this.renderOutlines) {
      GlStateManager.disableOutlineMode();
      GlStateManager.disableColorMaterial();
    } 
    GlStateManager.popMatrix();
    super.doRender(entity, x, y, z, entityYaw, partialTicks);
  }
  
  protected ResourceLocation getEntityTexture(EntityLeashKnot entity) {
    return LEASH_KNOT_TEXTURES;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\renderer\entity\RenderLeashKnot.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
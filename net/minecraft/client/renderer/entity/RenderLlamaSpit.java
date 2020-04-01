package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelLlamaSpit;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityLlamaSpit;
import net.minecraft.util.ResourceLocation;

public class RenderLlamaSpit extends Render<EntityLlamaSpit> {
  private static final ResourceLocation field_191333_a = new ResourceLocation("textures/entity/llama/spit.png");
  
  private final ModelLlamaSpit field_191334_f = new ModelLlamaSpit();
  
  public RenderLlamaSpit(RenderManager p_i47202_1_) {
    super(p_i47202_1_);
  }
  
  public void doRender(EntityLlamaSpit entity, double x, double y, double z, float entityYaw, float partialTicks) {
    GlStateManager.pushMatrix();
    GlStateManager.translate((float)x, (float)y + 0.15F, (float)z);
    GlStateManager.rotate(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks - 90.0F, 0.0F, 1.0F, 0.0F);
    GlStateManager.rotate(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks, 0.0F, 0.0F, 1.0F);
    bindEntityTexture(entity);
    if (this.renderOutlines) {
      GlStateManager.enableColorMaterial();
      GlStateManager.enableOutlineMode(getTeamColor(entity));
    } 
    this.field_191334_f.render((Entity)entity, partialTicks, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
    if (this.renderOutlines) {
      GlStateManager.disableOutlineMode();
      GlStateManager.disableColorMaterial();
    } 
    GlStateManager.popMatrix();
    super.doRender(entity, x, y, z, entityYaw, partialTicks);
  }
  
  protected ResourceLocation getEntityTexture(EntityLlamaSpit entity) {
    return field_191333_a;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\renderer\entity\RenderLlamaSpit.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
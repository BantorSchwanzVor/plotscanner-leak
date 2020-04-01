package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelEnderCrystal;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class RenderEnderCrystal extends Render<EntityEnderCrystal> {
  private static final ResourceLocation ENDER_CRYSTAL_TEXTURES = new ResourceLocation("textures/entity/endercrystal/endercrystal.png");
  
  private final ModelBase modelEnderCrystal = (ModelBase)new ModelEnderCrystal(0.0F, true);
  
  private final ModelBase modelEnderCrystalNoBase = (ModelBase)new ModelEnderCrystal(0.0F, false);
  
  public RenderEnderCrystal(RenderManager renderManagerIn) {
    super(renderManagerIn);
    this.shadowSize = 0.5F;
  }
  
  public void doRender(EntityEnderCrystal entity, double x, double y, double z, float entityYaw, float partialTicks) {
    float f = entity.innerRotation + partialTicks;
    GlStateManager.pushMatrix();
    GlStateManager.translate((float)x, (float)y, (float)z);
    bindTexture(ENDER_CRYSTAL_TEXTURES);
    float f1 = MathHelper.sin(f * 0.2F) / 2.0F + 0.5F;
    f1 = f1 * f1 + f1;
    if (this.renderOutlines) {
      GlStateManager.enableColorMaterial();
      GlStateManager.enableOutlineMode(getTeamColor(entity));
    } 
    if (entity.shouldShowBottom()) {
      this.modelEnderCrystal.render((Entity)entity, 0.0F, f * 3.0F, f1 * 0.2F, 0.0F, 0.0F, 0.0625F);
    } else {
      this.modelEnderCrystalNoBase.render((Entity)entity, 0.0F, f * 3.0F, f1 * 0.2F, 0.0F, 0.0F, 0.0625F);
    } 
    if (this.renderOutlines) {
      GlStateManager.disableOutlineMode();
      GlStateManager.disableColorMaterial();
    } 
    GlStateManager.popMatrix();
    BlockPos blockpos = entity.getBeamTarget();
    if (blockpos != null) {
      bindTexture(RenderDragon.ENDERCRYSTAL_BEAM_TEXTURES);
      float f2 = blockpos.getX() + 0.5F;
      float f3 = blockpos.getY() + 0.5F;
      float f4 = blockpos.getZ() + 0.5F;
      double d0 = f2 - entity.posX;
      double d1 = f3 - entity.posY;
      double d2 = f4 - entity.posZ;
      RenderDragon.renderCrystalBeams(x + d0, y - 0.3D + (f1 * 0.4F) + d1, z + d2, partialTicks, f2, f3, f4, entity.innerRotation, entity.posX, entity.posY, entity.posZ);
    } 
    super.doRender(entity, x, y, z, entityYaw, partialTicks);
  }
  
  protected ResourceLocation getEntityTexture(EntityEnderCrystal entity) {
    return ENDER_CRYSTAL_TEXTURES;
  }
  
  public boolean shouldRender(EntityEnderCrystal livingEntity, ICamera camera, double camX, double camY, double camZ) {
    return !(!super.shouldRender(livingEntity, camera, camX, camY, camZ) && livingEntity.getBeamTarget() == null);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\renderer\entity\RenderEnderCrystal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
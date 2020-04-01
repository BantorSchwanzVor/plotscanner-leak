package net.minecraft.client.renderer.entity;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.util.math.MathHelper;

public abstract class RenderArrow<T extends EntityArrow> extends Render<T> {
  public RenderArrow(RenderManager renderManagerIn) {
    super(renderManagerIn);
  }
  
  public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks) {
    bindEntityTexture(entity);
    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    GlStateManager.pushMatrix();
    GlStateManager.disableLighting();
    GlStateManager.translate((float)x, (float)y, (float)z);
    GlStateManager.rotate(((EntityArrow)entity).prevRotationYaw + (((EntityArrow)entity).rotationYaw - ((EntityArrow)entity).prevRotationYaw) * partialTicks - 90.0F, 0.0F, 1.0F, 0.0F);
    GlStateManager.rotate(((EntityArrow)entity).prevRotationPitch + (((EntityArrow)entity).rotationPitch - ((EntityArrow)entity).prevRotationPitch) * partialTicks, 0.0F, 0.0F, 1.0F);
    Tessellator tessellator = Tessellator.getInstance();
    BufferBuilder bufferbuilder = tessellator.getBuffer();
    int i = 0;
    float f = 0.0F;
    float f1 = 0.5F;
    float f2 = 0.0F;
    float f3 = 0.15625F;
    float f4 = 0.0F;
    float f5 = 0.15625F;
    float f6 = 0.15625F;
    float f7 = 0.3125F;
    float f8 = 0.05625F;
    GlStateManager.enableRescaleNormal();
    float f9 = ((EntityArrow)entity).arrowShake - partialTicks;
    if (f9 > 0.0F) {
      float f10 = -MathHelper.sin(f9 * 3.0F) * f9;
      GlStateManager.rotate(f10, 0.0F, 0.0F, 1.0F);
    } 
    GlStateManager.rotate(45.0F, 1.0F, 0.0F, 0.0F);
    GlStateManager.scale(0.05625F, 0.05625F, 0.05625F);
    GlStateManager.translate(-4.0F, 0.0F, 0.0F);
    if (this.renderOutlines) {
      GlStateManager.enableColorMaterial();
      GlStateManager.enableOutlineMode(getTeamColor(entity));
    } 
    GlStateManager.glNormal3f(0.05625F, 0.0F, 0.0F);
    bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
    bufferbuilder.pos(-7.0D, -2.0D, -2.0D).tex(0.0D, 0.15625D).endVertex();
    bufferbuilder.pos(-7.0D, -2.0D, 2.0D).tex(0.15625D, 0.15625D).endVertex();
    bufferbuilder.pos(-7.0D, 2.0D, 2.0D).tex(0.15625D, 0.3125D).endVertex();
    bufferbuilder.pos(-7.0D, 2.0D, -2.0D).tex(0.0D, 0.3125D).endVertex();
    tessellator.draw();
    GlStateManager.glNormal3f(-0.05625F, 0.0F, 0.0F);
    bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
    bufferbuilder.pos(-7.0D, 2.0D, -2.0D).tex(0.0D, 0.15625D).endVertex();
    bufferbuilder.pos(-7.0D, 2.0D, 2.0D).tex(0.15625D, 0.15625D).endVertex();
    bufferbuilder.pos(-7.0D, -2.0D, 2.0D).tex(0.15625D, 0.3125D).endVertex();
    bufferbuilder.pos(-7.0D, -2.0D, -2.0D).tex(0.0D, 0.3125D).endVertex();
    tessellator.draw();
    for (int j = 0; j < 4; j++) {
      GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
      GlStateManager.glNormal3f(0.0F, 0.0F, 0.05625F);
      bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
      bufferbuilder.pos(-8.0D, -2.0D, 0.0D).tex(0.0D, 0.0D).endVertex();
      bufferbuilder.pos(8.0D, -2.0D, 0.0D).tex(0.5D, 0.0D).endVertex();
      bufferbuilder.pos(8.0D, 2.0D, 0.0D).tex(0.5D, 0.15625D).endVertex();
      bufferbuilder.pos(-8.0D, 2.0D, 0.0D).tex(0.0D, 0.15625D).endVertex();
      tessellator.draw();
    } 
    if (this.renderOutlines) {
      GlStateManager.disableOutlineMode();
      GlStateManager.disableColorMaterial();
    } 
    GlStateManager.disableRescaleNormal();
    GlStateManager.enableLighting();
    GlStateManager.popMatrix();
    super.doRender(entity, x, y, z, entityYaw, partialTicks);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\renderer\entity\RenderArrow.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
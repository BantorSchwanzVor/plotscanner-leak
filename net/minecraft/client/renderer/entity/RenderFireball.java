package net.minecraft.client.renderer.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.init.Items;
import net.minecraft.util.ResourceLocation;

public class RenderFireball extends Render<EntityFireball> {
  private final float scale;
  
  public RenderFireball(RenderManager renderManagerIn, float scaleIn) {
    super(renderManagerIn);
    this.scale = scaleIn;
  }
  
  public void doRender(EntityFireball entity, double x, double y, double z, float entityYaw, float partialTicks) {
    GlStateManager.pushMatrix();
    bindEntityTexture(entity);
    GlStateManager.translate((float)x, (float)y, (float)z);
    GlStateManager.enableRescaleNormal();
    GlStateManager.scale(this.scale, this.scale, this.scale);
    TextureAtlasSprite textureatlassprite = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getParticleIcon(Items.FIRE_CHARGE);
    Tessellator tessellator = Tessellator.getInstance();
    BufferBuilder bufferbuilder = tessellator.getBuffer();
    float f = textureatlassprite.getMinU();
    float f1 = textureatlassprite.getMaxU();
    float f2 = textureatlassprite.getMinV();
    float f3 = textureatlassprite.getMaxV();
    float f4 = 1.0F;
    float f5 = 0.5F;
    float f6 = 0.25F;
    GlStateManager.rotate(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
    GlStateManager.rotate(((this.renderManager.options.thirdPersonView == 2) ? -1 : true) * -this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
    if (this.renderOutlines) {
      GlStateManager.enableColorMaterial();
      GlStateManager.enableOutlineMode(getTeamColor(entity));
    } 
    bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_NORMAL);
    bufferbuilder.pos(-0.5D, -0.25D, 0.0D).tex(f, f3).normal(0.0F, 1.0F, 0.0F).endVertex();
    bufferbuilder.pos(0.5D, -0.25D, 0.0D).tex(f1, f3).normal(0.0F, 1.0F, 0.0F).endVertex();
    bufferbuilder.pos(0.5D, 0.75D, 0.0D).tex(f1, f2).normal(0.0F, 1.0F, 0.0F).endVertex();
    bufferbuilder.pos(-0.5D, 0.75D, 0.0D).tex(f, f2).normal(0.0F, 1.0F, 0.0F).endVertex();
    tessellator.draw();
    if (this.renderOutlines) {
      GlStateManager.disableOutlineMode();
      GlStateManager.disableColorMaterial();
    } 
    GlStateManager.disableRescaleNormal();
    GlStateManager.popMatrix();
    super.doRender(entity, x, y, z, entityYaw, partialTicks);
  }
  
  protected ResourceLocation getEntityTexture(EntityFireball entity) {
    return TextureMap.LOCATION_BLOCKS_TEXTURE;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\renderer\entity\RenderFireball.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
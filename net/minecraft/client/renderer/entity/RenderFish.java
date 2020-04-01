package net.minecraft.client.renderer.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class RenderFish extends Render<EntityFishHook> {
  private static final ResourceLocation FISH_PARTICLES = new ResourceLocation("textures/particle/particles.png");
  
  public RenderFish(RenderManager renderManagerIn) {
    super(renderManagerIn);
  }
  
  public void doRender(EntityFishHook entity, double x, double y, double z, float entityYaw, float partialTicks) {
    EntityPlayer entityplayer = entity.func_190619_l();
    if (entityplayer != null && !this.renderOutlines) {
      double d4, d5, d6, d7;
      GlStateManager.pushMatrix();
      GlStateManager.translate((float)x, (float)y, (float)z);
      GlStateManager.enableRescaleNormal();
      GlStateManager.scale(0.5F, 0.5F, 0.5F);
      bindEntityTexture(entity);
      Tessellator tessellator = Tessellator.getInstance();
      BufferBuilder bufferbuilder = tessellator.getBuffer();
      int i = 1;
      int j = 2;
      float f = 0.0625F;
      float f1 = 0.125F;
      float f2 = 0.125F;
      float f3 = 0.1875F;
      float f4 = 1.0F;
      float f5 = 0.5F;
      float f6 = 0.5F;
      GlStateManager.rotate(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
      GlStateManager.rotate(((this.renderManager.options.thirdPersonView == 2) ? -1 : true) * -this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
      if (this.renderOutlines) {
        GlStateManager.enableColorMaterial();
        GlStateManager.enableOutlineMode(getTeamColor(entity));
      } 
      bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_NORMAL);
      bufferbuilder.pos(-0.5D, -0.5D, 0.0D).tex(0.0625D, 0.1875D).normal(0.0F, 1.0F, 0.0F).endVertex();
      bufferbuilder.pos(0.5D, -0.5D, 0.0D).tex(0.125D, 0.1875D).normal(0.0F, 1.0F, 0.0F).endVertex();
      bufferbuilder.pos(0.5D, 0.5D, 0.0D).tex(0.125D, 0.125D).normal(0.0F, 1.0F, 0.0F).endVertex();
      bufferbuilder.pos(-0.5D, 0.5D, 0.0D).tex(0.0625D, 0.125D).normal(0.0F, 1.0F, 0.0F).endVertex();
      tessellator.draw();
      if (this.renderOutlines) {
        GlStateManager.disableOutlineMode();
        GlStateManager.disableColorMaterial();
      } 
      GlStateManager.disableRescaleNormal();
      GlStateManager.popMatrix();
      int k = (entityplayer.getPrimaryHand() == EnumHandSide.RIGHT) ? 1 : -1;
      ItemStack itemstack = entityplayer.getHeldItemMainhand();
      if (itemstack.getItem() != Items.FISHING_ROD)
        k = -k; 
      float f7 = entityplayer.getSwingProgress(partialTicks);
      float f8 = MathHelper.sin(MathHelper.sqrt(f7) * 3.1415927F);
      float f9 = (entityplayer.prevRenderYawOffset + (entityplayer.renderYawOffset - entityplayer.prevRenderYawOffset) * partialTicks) * 0.017453292F;
      double d0 = MathHelper.sin(f9);
      double d1 = MathHelper.cos(f9);
      double d2 = k * 0.35D;
      double d3 = 0.8D;
      if ((this.renderManager.options == null || this.renderManager.options.thirdPersonView <= 0) && entityplayer == (Minecraft.getMinecraft()).player) {
        float f10 = this.renderManager.options.fovSetting;
        f10 /= 100.0F;
        Vec3d vec3d = new Vec3d(k * -0.36D * f10, -0.045D * f10, 0.4D);
        vec3d = vec3d.rotatePitch(-(entityplayer.prevRotationPitch + (entityplayer.rotationPitch - entityplayer.prevRotationPitch) * partialTicks) * 0.017453292F);
        vec3d = vec3d.rotateYaw(-(entityplayer.prevRotationYaw + (entityplayer.rotationYaw - entityplayer.prevRotationYaw) * partialTicks) * 0.017453292F);
        vec3d = vec3d.rotateYaw(f8 * 0.5F);
        vec3d = vec3d.rotatePitch(-f8 * 0.7F);
        d4 = entityplayer.prevPosX + (entityplayer.posX - entityplayer.prevPosX) * partialTicks + vec3d.xCoord;
        d5 = entityplayer.prevPosY + (entityplayer.posY - entityplayer.prevPosY) * partialTicks + vec3d.yCoord;
        d6 = entityplayer.prevPosZ + (entityplayer.posZ - entityplayer.prevPosZ) * partialTicks + vec3d.zCoord;
        d7 = entityplayer.getEyeHeight();
      } else {
        d4 = entityplayer.prevPosX + (entityplayer.posX - entityplayer.prevPosX) * partialTicks - d1 * d2 - d0 * 0.8D;
        d5 = entityplayer.prevPosY + entityplayer.getEyeHeight() + (entityplayer.posY - entityplayer.prevPosY) * partialTicks - 0.45D;
        d6 = entityplayer.prevPosZ + (entityplayer.posZ - entityplayer.prevPosZ) * partialTicks - d0 * d2 + d1 * 0.8D;
        d7 = entityplayer.isSneaking() ? -0.1875D : 0.0D;
      } 
      double d13 = entity.prevPosX + (entity.posX - entity.prevPosX) * partialTicks;
      double d8 = entity.prevPosY + (entity.posY - entity.prevPosY) * partialTicks + 0.25D;
      double d9 = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * partialTicks;
      double d10 = (float)(d4 - d13);
      double d11 = (float)(d5 - d8) + d7;
      double d12 = (float)(d6 - d9);
      GlStateManager.disableTexture2D();
      GlStateManager.disableLighting();
      bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
      int l = 16;
      for (int i1 = 0; i1 <= 16; i1++) {
        float f11 = i1 / 16.0F;
        bufferbuilder.pos(x + d10 * f11, y + d11 * (f11 * f11 + f11) * 0.5D + 0.25D, z + d12 * f11).color(0, 0, 0, 255).endVertex();
      } 
      tessellator.draw();
      GlStateManager.enableLighting();
      GlStateManager.enableTexture2D();
      super.doRender(entity, x, y, z, entityYaw, partialTicks);
    } 
  }
  
  protected ResourceLocation getEntityTexture(EntityFishHook entity) {
    return FISH_PARTICLES;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\renderer\entity\RenderFish.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
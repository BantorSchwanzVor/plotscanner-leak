package net.minecraft.client.renderer.entity.layers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderSpider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.util.ResourceLocation;
import optifine.Config;
import shadersmod.client.Shaders;

public class LayerSpiderEyes<T extends EntitySpider> implements LayerRenderer<T> {
  private static final ResourceLocation SPIDER_EYES = new ResourceLocation("textures/entity/spider_eyes.png");
  
  private final RenderSpider<T> spiderRenderer;
  
  public LayerSpiderEyes(RenderSpider<T> spiderRendererIn) {
    this.spiderRenderer = spiderRendererIn;
  }
  
  public void doRenderLayer(T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
    this.spiderRenderer.bindTexture(SPIDER_EYES);
    GlStateManager.enableBlend();
    GlStateManager.disableAlpha();
    GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
    if (entitylivingbaseIn.isInvisible()) {
      GlStateManager.depthMask(false);
    } else {
      GlStateManager.depthMask(true);
    } 
    int i = 61680;
    int j = i % 65536;
    int k = i / 65536;
    OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, j, k);
    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    (Minecraft.getMinecraft()).entityRenderer.func_191514_d(true);
    if (Config.isShaders())
      Shaders.beginSpiderEyes(); 
    (Config.getRenderGlobal()).renderOverlayEyes = true;
    this.spiderRenderer.getMainModel().render((Entity)entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
    (Config.getRenderGlobal()).renderOverlayEyes = false;
    if (Config.isShaders())
      Shaders.endSpiderEyes(); 
    (Minecraft.getMinecraft()).entityRenderer.func_191514_d(false);
    i = entitylivingbaseIn.getBrightnessForRender();
    j = i % 65536;
    k = i / 65536;
    OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, j, k);
    this.spiderRenderer.setLightmap((EntityLiving)entitylivingbaseIn);
    GlStateManager.disableBlend();
    GlStateManager.enableAlpha();
  }
  
  public boolean shouldCombineTextures() {
    return false;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\renderer\entity\layers\LayerSpiderEyes.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
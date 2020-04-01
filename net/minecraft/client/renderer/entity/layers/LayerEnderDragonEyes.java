package net.minecraft.client.renderer.entity.layers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderDragon;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.util.ResourceLocation;
import optifine.Config;
import shadersmod.client.Shaders;

public class LayerEnderDragonEyes implements LayerRenderer<EntityDragon> {
  private static final ResourceLocation TEXTURE = new ResourceLocation("textures/entity/enderdragon/dragon_eyes.png");
  
  private final RenderDragon dragonRenderer;
  
  public LayerEnderDragonEyes(RenderDragon dragonRendererIn) {
    this.dragonRenderer = dragonRendererIn;
  }
  
  public void doRenderLayer(EntityDragon entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
    this.dragonRenderer.bindTexture(TEXTURE);
    GlStateManager.enableBlend();
    GlStateManager.disableAlpha();
    GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
    GlStateManager.disableLighting();
    GlStateManager.depthFunc(514);
    int i = 61680;
    int j = 61680;
    int k = 0;
    OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 61680.0F, 0.0F);
    GlStateManager.enableLighting();
    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    (Minecraft.getMinecraft()).entityRenderer.func_191514_d(true);
    if (Config.isShaders())
      Shaders.beginSpiderEyes(); 
    (Config.getRenderGlobal()).renderOverlayEyes = true;
    this.dragonRenderer.getMainModel().render((Entity)entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
    (Config.getRenderGlobal()).renderOverlayEyes = false;
    if (Config.isShaders())
      Shaders.endSpiderEyes(); 
    (Minecraft.getMinecraft()).entityRenderer.func_191514_d(false);
    this.dragonRenderer.setLightmap((EntityLiving)entitylivingbaseIn);
    GlStateManager.disableBlend();
    GlStateManager.enableAlpha();
    GlStateManager.depthFunc(515);
  }
  
  public boolean shouldCombineTextures() {
    return false;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\renderer\entity\layers\LayerEnderDragonEyes.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
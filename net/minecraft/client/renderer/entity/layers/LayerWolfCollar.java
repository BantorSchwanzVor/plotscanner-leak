package net.minecraft.client.renderer.entity.layers;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderWolf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.util.ResourceLocation;
import optifine.Config;
import optifine.CustomColors;

public class LayerWolfCollar implements LayerRenderer<EntityWolf> {
  private static final ResourceLocation WOLF_COLLAR = new ResourceLocation("textures/entity/wolf/wolf_collar.png");
  
  private final RenderWolf wolfRenderer;
  
  public LayerWolfCollar(RenderWolf wolfRendererIn) {
    this.wolfRenderer = wolfRendererIn;
  }
  
  public void doRenderLayer(EntityWolf entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
    if (entitylivingbaseIn.isTamed() && !entitylivingbaseIn.isInvisible()) {
      this.wolfRenderer.bindTexture(WOLF_COLLAR);
      float[] afloat = entitylivingbaseIn.getCollarColor().func_193349_f();
      if (Config.isCustomColors())
        afloat = CustomColors.getWolfCollarColors(entitylivingbaseIn.getCollarColor(), afloat); 
      GlStateManager.color(afloat[0], afloat[1], afloat[2]);
      this.wolfRenderer.getMainModel().render((Entity)entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
    } 
  }
  
  public boolean shouldCombineTextures() {
    return true;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\renderer\entity\layers\LayerWolfCollar.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
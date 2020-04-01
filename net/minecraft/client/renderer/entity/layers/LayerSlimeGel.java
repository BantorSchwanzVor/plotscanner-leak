package net.minecraft.client.renderer.entity.layers;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelSlime;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderSlime;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntitySlime;

public class LayerSlimeGel implements LayerRenderer<EntitySlime> {
  private final RenderSlime slimeRenderer;
  
  private final ModelBase slimeModel = (ModelBase)new ModelSlime(0);
  
  public LayerSlimeGel(RenderSlime slimeRendererIn) {
    this.slimeRenderer = slimeRendererIn;
  }
  
  public void doRenderLayer(EntitySlime entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
    if (!entitylivingbaseIn.isInvisible()) {
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      GlStateManager.enableNormalize();
      GlStateManager.enableBlend();
      GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
      this.slimeModel.setModelAttributes(this.slimeRenderer.getMainModel());
      this.slimeModel.render((Entity)entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
      GlStateManager.disableBlend();
      GlStateManager.disableNormalize();
    } 
  }
  
  public boolean shouldCombineTextures() {
    return true;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\renderer\entity\layers\LayerSlimeGel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
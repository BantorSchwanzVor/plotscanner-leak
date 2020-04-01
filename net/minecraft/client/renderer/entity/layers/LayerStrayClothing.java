package net.minecraft.client.renderer.entity.layers;

import net.minecraft.client.model.ModelSkeleton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityStray;
import net.minecraft.util.ResourceLocation;

public class LayerStrayClothing implements LayerRenderer<EntityStray> {
  private static final ResourceLocation STRAY_CLOTHES_TEXTURES = new ResourceLocation("textures/entity/skeleton/stray_overlay.png");
  
  private final RenderLivingBase<?> renderer;
  
  private final ModelSkeleton layerModel = new ModelSkeleton(0.25F, true);
  
  public LayerStrayClothing(RenderLivingBase<?> p_i47183_1_) {
    this.renderer = p_i47183_1_;
  }
  
  public void doRenderLayer(EntityStray entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
    this.layerModel.setModelAttributes(this.renderer.getMainModel());
    this.layerModel.setLivingAnimations((EntityLivingBase)entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks);
    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    this.renderer.bindTexture(STRAY_CLOTHES_TEXTURES);
    this.layerModel.render((Entity)entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
  }
  
  public boolean shouldCombineTextures() {
    return true;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\renderer\entity\layers\LayerStrayClothing.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
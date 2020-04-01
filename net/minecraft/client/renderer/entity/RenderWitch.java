package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelWitch;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerHeldItemWitch;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.util.ResourceLocation;

public class RenderWitch extends RenderLiving<EntityWitch> {
  private static final ResourceLocation WITCH_TEXTURES = new ResourceLocation("textures/entity/witch.png");
  
  public RenderWitch(RenderManager renderManagerIn) {
    super(renderManagerIn, (ModelBase)new ModelWitch(0.0F), 0.5F);
    addLayer(new LayerHeldItemWitch(this));
  }
  
  public ModelWitch getMainModel() {
    return (ModelWitch)super.getMainModel();
  }
  
  public void doRender(EntityWitch entity, double x, double y, double z, float entityYaw, float partialTicks) {
    ((ModelWitch)this.mainModel).holdingItem = !entity.getHeldItemMainhand().func_190926_b();
    super.doRender(entity, x, y, z, entityYaw, partialTicks);
  }
  
  protected ResourceLocation getEntityTexture(EntityWitch entity) {
    return WITCH_TEXTURES;
  }
  
  public void transformHeldFull3DItemLayer() {
    GlStateManager.translate(0.0F, 0.1875F, 0.0F);
  }
  
  protected void preRenderCallback(EntityWitch entitylivingbaseIn, float partialTickTime) {
    float f = 0.9375F;
    GlStateManager.scale(0.9375F, 0.9375F, 0.9375F);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\renderer\entity\RenderWitch.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
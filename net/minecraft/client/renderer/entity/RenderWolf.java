package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelWolf;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerWolfCollar;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.util.ResourceLocation;

public class RenderWolf extends RenderLiving<EntityWolf> {
  private static final ResourceLocation WOLF_TEXTURES = new ResourceLocation("textures/entity/wolf/wolf.png");
  
  private static final ResourceLocation TAMED_WOLF_TEXTURES = new ResourceLocation("textures/entity/wolf/wolf_tame.png");
  
  private static final ResourceLocation ANRGY_WOLF_TEXTURES = new ResourceLocation("textures/entity/wolf/wolf_angry.png");
  
  public RenderWolf(RenderManager p_i47187_1_) {
    super(p_i47187_1_, (ModelBase)new ModelWolf(), 0.5F);
    addLayer(new LayerWolfCollar(this));
  }
  
  protected float handleRotationFloat(EntityWolf livingBase, float partialTicks) {
    return livingBase.getTailRotation();
  }
  
  public void doRender(EntityWolf entity, double x, double y, double z, float entityYaw, float partialTicks) {
    if (entity.isWolfWet()) {
      float f = entity.getBrightness() * entity.getShadingWhileWet(partialTicks);
      GlStateManager.color(f, f, f);
    } 
    super.doRender(entity, x, y, z, entityYaw, partialTicks);
  }
  
  protected ResourceLocation getEntityTexture(EntityWolf entity) {
    if (entity.isTamed())
      return TAMED_WOLF_TEXTURES; 
    return entity.isAngry() ? ANRGY_WOLF_TEXTURES : WOLF_TEXTURES;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\renderer\entity\RenderWolf.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
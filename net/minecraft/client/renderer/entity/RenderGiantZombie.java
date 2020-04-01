package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityGiantZombie;
import net.minecraft.util.ResourceLocation;

public class RenderGiantZombie extends RenderLiving<EntityGiantZombie> {
  private static final ResourceLocation ZOMBIE_TEXTURES = new ResourceLocation("textures/entity/zombie/zombie.png");
  
  private final float scale;
  
  public RenderGiantZombie(RenderManager p_i47206_1_, float p_i47206_2_) {
    super(p_i47206_1_, (ModelBase)new ModelZombie(), 0.5F * p_i47206_2_);
    this.scale = p_i47206_2_;
    addLayer(new LayerHeldItem(this));
    addLayer(new LayerBipedArmor(this) {
          protected void initArmor() {
            this.modelLeggings = (ModelBase)new ModelZombie(0.5F, true);
            this.modelArmor = (ModelBase)new ModelZombie(1.0F, true);
          }
        });
  }
  
  public void transformHeldFull3DItemLayer() {
    GlStateManager.translate(0.0F, 0.1875F, 0.0F);
  }
  
  protected void preRenderCallback(EntityGiantZombie entitylivingbaseIn, float partialTickTime) {
    GlStateManager.scale(this.scale, this.scale, this.scale);
  }
  
  protected ResourceLocation getEntityTexture(EntityGiantZombie entity) {
    return ZOMBIE_TEXTURES;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\renderer\entity\RenderGiantZombie.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelSkeleton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.AbstractSkeleton;
import net.minecraft.util.ResourceLocation;

public class RenderSkeleton extends RenderBiped<AbstractSkeleton> {
  private static final ResourceLocation SKELETON_TEXTURES = new ResourceLocation("textures/entity/skeleton/skeleton.png");
  
  public RenderSkeleton(RenderManager renderManagerIn) {
    super(renderManagerIn, (ModelBiped)new ModelSkeleton(), 0.5F);
    addLayer(new LayerHeldItem(this));
    addLayer(new LayerBipedArmor(this) {
          protected void initArmor() {
            this.modelLeggings = (ModelBase)new ModelSkeleton(0.5F, true);
            this.modelArmor = (ModelBase)new ModelSkeleton(1.0F, true);
          }
        });
  }
  
  public void transformHeldFull3DItemLayer() {
    GlStateManager.translate(0.09375F, 0.1875F, 0.0F);
  }
  
  protected ResourceLocation getEntityTexture(AbstractSkeleton entity) {
    return SKELETON_TEXTURES;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\renderer\entity\RenderSkeleton.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerCustomHead;
import net.minecraft.client.renderer.entity.layers.LayerElytra;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;

public class RenderBiped<T extends EntityLiving> extends RenderLiving<T> {
  private static final ResourceLocation DEFAULT_RES_LOC = new ResourceLocation("textures/entity/steve.png");
  
  public RenderBiped(RenderManager renderManagerIn, ModelBiped modelBipedIn, float shadowSize) {
    super(renderManagerIn, (ModelBase)modelBipedIn, shadowSize);
    addLayer(new LayerCustomHead(modelBipedIn.bipedHead));
    addLayer(new LayerElytra(this));
    addLayer(new LayerHeldItem(this));
  }
  
  protected ResourceLocation getEntityTexture(T entity) {
    return DEFAULT_RES_LOC;
  }
  
  public void transformHeldFull3DItemLayer() {
    GlStateManager.translate(0.0F, 0.1875F, 0.0F);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\renderer\entity\RenderBiped.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
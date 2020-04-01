package net.optifine.entity.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelSkeletonHead;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderWitherSkull;
import net.minecraft.entity.projectile.EntityWitherSkull;
import optifine.Config;
import optifine.Reflector;

public class ModelAdapterWitherSkull extends ModelAdapter {
  public ModelAdapterWitherSkull() {
    super(EntityWitherSkull.class, "wither_skull", 0.0F);
  }
  
  public ModelBase makeModel() {
    return (ModelBase)new ModelSkeletonHead();
  }
  
  public ModelRenderer getModelRenderer(ModelBase model, String modelPart) {
    if (!(model instanceof ModelSkeletonHead))
      return null; 
    ModelSkeletonHead modelskeletonhead = (ModelSkeletonHead)model;
    return modelPart.equals("head") ? modelskeletonhead.skeletonHead : null;
  }
  
  public IEntityRenderer makeEntityRender(ModelBase modelBase, float shadowSize) {
    RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
    RenderWitherSkull renderwitherskull = new RenderWitherSkull(rendermanager);
    if (!Reflector.RenderWitherSkull_model.exists()) {
      Config.warn("Field not found: RenderWitherSkull_model");
      return null;
    } 
    Reflector.setFieldValue(renderwitherskull, Reflector.RenderWitherSkull_model, modelBase);
    renderwitherskull.shadowSize = shadowSize;
    return (IEntityRenderer)renderwitherskull;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\optifine\entity\model\ModelAdapterWitherSkull.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
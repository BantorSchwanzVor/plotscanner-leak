package net.optifine.entity.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelEvokerFangs;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.RenderEvokerFangs;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.projectile.EntityEvokerFangs;
import optifine.Config;
import optifine.Reflector;

public class ModelAdapterEvokerFangs extends ModelAdapter {
  public ModelAdapterEvokerFangs() {
    super(EntityEvokerFangs.class, "evocation_fangs", 0.0F);
  }
  
  public ModelBase makeModel() {
    return (ModelBase)new ModelEvokerFangs();
  }
  
  public ModelRenderer getModelRenderer(ModelBase model, String modelPart) {
    if (!(model instanceof ModelEvokerFangs))
      return null; 
    ModelEvokerFangs modelevokerfangs = (ModelEvokerFangs)model;
    if (modelPart.equals("base"))
      return (ModelRenderer)Reflector.getFieldValue(modelevokerfangs, Reflector.ModelEvokerFangs_ModelRenderers, 0); 
    if (modelPart.equals("upper_jaw"))
      return (ModelRenderer)Reflector.getFieldValue(modelevokerfangs, Reflector.ModelEvokerFangs_ModelRenderers, 1); 
    return modelPart.equals("lower_jaw") ? (ModelRenderer)Reflector.getFieldValue(modelevokerfangs, Reflector.ModelEvokerFangs_ModelRenderers, 2) : null;
  }
  
  public IEntityRenderer makeEntityRender(ModelBase modelBase, float shadowSize) {
    RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
    RenderEvokerFangs renderevokerfangs = new RenderEvokerFangs(rendermanager);
    if (!Reflector.RenderEvokerFangs_model.exists()) {
      Config.warn("Field not found: RenderEvokerFangs.model");
      return null;
    } 
    Reflector.setFieldValue(renderevokerfangs, Reflector.RenderEvokerFangs_model, modelBase);
    renderevokerfangs.shadowSize = shadowSize;
    return (IEntityRenderer)renderevokerfangs;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\optifine\entity\model\ModelAdapterEvokerFangs.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
package net.optifine.entity.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelLeashKnot;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.RenderLeashKnot;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLeashKnot;
import optifine.Config;
import optifine.Reflector;

public class ModelAdapterLeadKnot extends ModelAdapter {
  public ModelAdapterLeadKnot() {
    super(EntityLeashKnot.class, "lead_knot", 0.0F);
  }
  
  public ModelBase makeModel() {
    return (ModelBase)new ModelLeashKnot();
  }
  
  public ModelRenderer getModelRenderer(ModelBase model, String modelPart) {
    if (!(model instanceof ModelLeashKnot))
      return null; 
    ModelLeashKnot modelleashknot = (ModelLeashKnot)model;
    return modelPart.equals("knot") ? modelleashknot.knotRenderer : null;
  }
  
  public IEntityRenderer makeEntityRender(ModelBase modelBase, float shadowSize) {
    RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
    RenderLeashKnot renderleashknot = new RenderLeashKnot(rendermanager);
    if (!Reflector.RenderLeashKnot_leashKnotModel.exists()) {
      Config.warn("Field not found: RenderLeashKnot.leashKnotModel");
      return null;
    } 
    Reflector.setFieldValue(renderleashknot, Reflector.RenderLeashKnot_leashKnotModel, modelBase);
    renderleashknot.shadowSize = shadowSize;
    return (IEntityRenderer)renderleashknot;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\optifine\entity\model\ModelAdapterLeadKnot.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
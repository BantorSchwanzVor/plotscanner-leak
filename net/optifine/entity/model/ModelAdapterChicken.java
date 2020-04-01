package net.optifine.entity.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelChicken;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.RenderChicken;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.passive.EntityChicken;

public class ModelAdapterChicken extends ModelAdapter {
  public ModelAdapterChicken() {
    super(EntityChicken.class, "chicken", 0.3F);
  }
  
  public ModelBase makeModel() {
    return (ModelBase)new ModelChicken();
  }
  
  public ModelRenderer getModelRenderer(ModelBase model, String modelPart) {
    if (!(model instanceof ModelChicken))
      return null; 
    ModelChicken modelchicken = (ModelChicken)model;
    if (modelPart.equals("head"))
      return modelchicken.head; 
    if (modelPart.equals("body"))
      return modelchicken.body; 
    if (modelPart.equals("right_leg"))
      return modelchicken.rightLeg; 
    if (modelPart.equals("left_leg"))
      return modelchicken.leftLeg; 
    if (modelPart.equals("right_wing"))
      return modelchicken.rightWing; 
    if (modelPart.equals("left_wing"))
      return modelchicken.leftWing; 
    if (modelPart.equals("bill"))
      return modelchicken.bill; 
    return modelPart.equals("chin") ? modelchicken.chin : null;
  }
  
  public IEntityRenderer makeEntityRender(ModelBase modelBase, float shadowSize) {
    RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
    RenderChicken renderchicken = new RenderChicken(rendermanager);
    renderchicken.mainModel = modelBase;
    renderchicken.shadowSize = shadowSize;
    return (IEntityRenderer)renderchicken;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\optifine\entity\model\ModelAdapterChicken.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
package net.optifine.entity.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelWolf;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderWolf;
import net.minecraft.entity.passive.EntityWolf;
import optifine.Reflector;

public class ModelAdapterWolf extends ModelAdapter {
  public ModelAdapterWolf() {
    super(EntityWolf.class, "wolf", 0.5F);
  }
  
  public ModelBase makeModel() {
    return (ModelBase)new ModelWolf();
  }
  
  public ModelRenderer getModelRenderer(ModelBase model, String modelPart) {
    if (!(model instanceof ModelWolf))
      return null; 
    ModelWolf modelwolf = (ModelWolf)model;
    if (modelPart.equals("head"))
      return modelwolf.wolfHeadMain; 
    if (modelPart.equals("body"))
      return modelwolf.wolfBody; 
    if (modelPart.equals("leg1"))
      return modelwolf.wolfLeg1; 
    if (modelPart.equals("leg2"))
      return modelwolf.wolfLeg2; 
    if (modelPart.equals("leg3"))
      return modelwolf.wolfLeg3; 
    if (modelPart.equals("leg4"))
      return modelwolf.wolfLeg4; 
    if (modelPart.equals("tail"))
      return (ModelRenderer)Reflector.getFieldValue(modelwolf, Reflector.ModelWolf_tail); 
    return modelPart.equals("mane") ? (ModelRenderer)Reflector.getFieldValue(modelwolf, Reflector.ModelWolf_mane) : null;
  }
  
  public IEntityRenderer makeEntityRender(ModelBase modelBase, float shadowSize) {
    RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
    RenderWolf renderwolf = new RenderWolf(rendermanager);
    renderwolf.mainModel = modelBase;
    renderwolf.shadowSize = shadowSize;
    return (IEntityRenderer)renderwolf;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\optifine\entity\model\ModelAdapterWolf.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
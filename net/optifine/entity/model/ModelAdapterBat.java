package net.optifine.entity.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBat;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.RenderBat;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.passive.EntityBat;
import optifine.Reflector;

public class ModelAdapterBat extends ModelAdapter {
  public ModelAdapterBat() {
    super(EntityBat.class, "bat", 0.25F);
  }
  
  public ModelBase makeModel() {
    return (ModelBase)new ModelBat();
  }
  
  public ModelRenderer getModelRenderer(ModelBase model, String modelPart) {
    if (!(model instanceof ModelBat))
      return null; 
    ModelBat modelbat = (ModelBat)model;
    if (modelPart.equals("head"))
      return (ModelRenderer)Reflector.getFieldValue(modelbat, Reflector.ModelBat_ModelRenderers, 0); 
    if (modelPart.equals("body"))
      return (ModelRenderer)Reflector.getFieldValue(modelbat, Reflector.ModelBat_ModelRenderers, 1); 
    if (modelPart.equals("right_wing"))
      return (ModelRenderer)Reflector.getFieldValue(modelbat, Reflector.ModelBat_ModelRenderers, 2); 
    if (modelPart.equals("left_wing"))
      return (ModelRenderer)Reflector.getFieldValue(modelbat, Reflector.ModelBat_ModelRenderers, 3); 
    if (modelPart.equals("outer_right_wing"))
      return (ModelRenderer)Reflector.getFieldValue(modelbat, Reflector.ModelBat_ModelRenderers, 4); 
    return modelPart.equals("outer_left_wing") ? (ModelRenderer)Reflector.getFieldValue(modelbat, Reflector.ModelBat_ModelRenderers, 5) : null;
  }
  
  public IEntityRenderer makeEntityRender(ModelBase modelBase, float shadowSize) {
    RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
    RenderBat renderbat = new RenderBat(rendermanager);
    renderbat.mainModel = modelBase;
    renderbat.shadowSize = shadowSize;
    return (IEntityRenderer)renderbat;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\optifine\entity\model\ModelAdapterBat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
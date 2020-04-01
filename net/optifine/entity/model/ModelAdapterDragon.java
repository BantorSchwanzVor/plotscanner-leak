package net.optifine.entity.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelDragon;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.RenderDragon;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.boss.EntityDragon;
import optifine.Reflector;

public class ModelAdapterDragon extends ModelAdapter {
  public ModelAdapterDragon() {
    super(EntityDragon.class, "dragon", 0.5F);
  }
  
  public ModelBase makeModel() {
    return (ModelBase)new ModelDragon(0.0F);
  }
  
  public ModelRenderer getModelRenderer(ModelBase model, String modelPart) {
    if (!(model instanceof ModelDragon))
      return null; 
    ModelDragon modeldragon = (ModelDragon)model;
    if (modelPart.equals("head"))
      return (ModelRenderer)Reflector.getFieldValue(modeldragon, Reflector.ModelDragon_ModelRenderers, 0); 
    if (modelPart.equals("spine"))
      return (ModelRenderer)Reflector.getFieldValue(modeldragon, Reflector.ModelDragon_ModelRenderers, 1); 
    if (modelPart.equals("jaw"))
      return (ModelRenderer)Reflector.getFieldValue(modeldragon, Reflector.ModelDragon_ModelRenderers, 2); 
    if (modelPart.equals("body"))
      return (ModelRenderer)Reflector.getFieldValue(modeldragon, Reflector.ModelDragon_ModelRenderers, 3); 
    if (modelPart.equals("rear_leg"))
      return (ModelRenderer)Reflector.getFieldValue(modeldragon, Reflector.ModelDragon_ModelRenderers, 4); 
    if (modelPart.equals("front_leg"))
      return (ModelRenderer)Reflector.getFieldValue(modeldragon, Reflector.ModelDragon_ModelRenderers, 5); 
    if (modelPart.equals("rear_leg_tip"))
      return (ModelRenderer)Reflector.getFieldValue(modeldragon, Reflector.ModelDragon_ModelRenderers, 6); 
    if (modelPart.equals("front_leg_tip"))
      return (ModelRenderer)Reflector.getFieldValue(modeldragon, Reflector.ModelDragon_ModelRenderers, 7); 
    if (modelPart.equals("rear_foot"))
      return (ModelRenderer)Reflector.getFieldValue(modeldragon, Reflector.ModelDragon_ModelRenderers, 8); 
    if (modelPart.equals("front_foot"))
      return (ModelRenderer)Reflector.getFieldValue(modeldragon, Reflector.ModelDragon_ModelRenderers, 9); 
    if (modelPart.equals("wing"))
      return (ModelRenderer)Reflector.getFieldValue(modeldragon, Reflector.ModelDragon_ModelRenderers, 10); 
    return modelPart.equals("wing_tip") ? (ModelRenderer)Reflector.getFieldValue(modeldragon, Reflector.ModelDragon_ModelRenderers, 11) : null;
  }
  
  public IEntityRenderer makeEntityRender(ModelBase modelBase, float shadowSize) {
    RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
    RenderDragon renderdragon = new RenderDragon(rendermanager);
    renderdragon.mainModel = modelBase;
    renderdragon.shadowSize = shadowSize;
    return (IEntityRenderer)renderdragon;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\optifine\entity\model\ModelAdapterDragon.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
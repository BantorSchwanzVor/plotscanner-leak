package net.optifine.entity.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelSlime;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSlime;
import net.minecraft.entity.monster.EntitySlime;
import optifine.Reflector;

public class ModelAdapterSlime extends ModelAdapter {
  public ModelAdapterSlime() {
    super(EntitySlime.class, "slime", 0.25F);
  }
  
  public ModelBase makeModel() {
    return (ModelBase)new ModelSlime(16);
  }
  
  public ModelRenderer getModelRenderer(ModelBase model, String modelPart) {
    if (!(model instanceof ModelSlime))
      return null; 
    ModelSlime modelslime = (ModelSlime)model;
    if (modelPart.equals("body"))
      return (ModelRenderer)Reflector.getFieldValue(modelslime, Reflector.ModelSlime_ModelRenderers, 0); 
    if (modelPart.equals("left_eye"))
      return (ModelRenderer)Reflector.getFieldValue(modelslime, Reflector.ModelSlime_ModelRenderers, 1); 
    if (modelPart.equals("right_eye"))
      return (ModelRenderer)Reflector.getFieldValue(modelslime, Reflector.ModelSlime_ModelRenderers, 2); 
    return modelPart.equals("mouth") ? (ModelRenderer)Reflector.getFieldValue(modelslime, Reflector.ModelSlime_ModelRenderers, 3) : null;
  }
  
  public IEntityRenderer makeEntityRender(ModelBase modelBase, float shadowSize) {
    RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
    RenderSlime renderslime = new RenderSlime(rendermanager);
    renderslime.mainModel = modelBase;
    renderslime.shadowSize = shadowSize;
    return (IEntityRenderer)renderslime;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\optifine\entity\model\ModelAdapterSlime.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
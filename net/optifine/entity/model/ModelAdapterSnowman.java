package net.optifine.entity.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelSnowMan;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSnowMan;
import net.minecraft.entity.monster.EntitySnowman;

public class ModelAdapterSnowman extends ModelAdapter {
  public ModelAdapterSnowman() {
    super(EntitySnowman.class, "snow_golem", 0.5F);
  }
  
  public ModelBase makeModel() {
    return (ModelBase)new ModelSnowMan();
  }
  
  public ModelRenderer getModelRenderer(ModelBase model, String modelPart) {
    if (!(model instanceof ModelSnowMan))
      return null; 
    ModelSnowMan modelsnowman = (ModelSnowMan)model;
    if (modelPart.equals("body"))
      return modelsnowman.body; 
    if (modelPart.equals("body_bottom"))
      return modelsnowman.bottomBody; 
    if (modelPart.equals("head"))
      return modelsnowman.head; 
    if (modelPart.equals("left_hand"))
      return modelsnowman.leftHand; 
    return modelPart.equals("right_hand") ? modelsnowman.rightHand : null;
  }
  
  public IEntityRenderer makeEntityRender(ModelBase modelBase, float shadowSize) {
    RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
    RenderSnowMan rendersnowman = new RenderSnowMan(rendermanager);
    rendersnowman.mainModel = modelBase;
    rendersnowman.shadowSize = shadowSize;
    return (IEntityRenderer)rendersnowman;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\optifine\entity\model\ModelAdapterSnowman.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
package net.optifine.entity.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelCreeper;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.RenderCreeper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.monster.EntityCreeper;

public class ModelAdapterCreeper extends ModelAdapter {
  public ModelAdapterCreeper() {
    super(EntityCreeper.class, "creeper", 0.5F);
  }
  
  public ModelBase makeModel() {
    return (ModelBase)new ModelCreeper();
  }
  
  public ModelRenderer getModelRenderer(ModelBase model, String modelPart) {
    if (!(model instanceof ModelCreeper))
      return null; 
    ModelCreeper modelcreeper = (ModelCreeper)model;
    if (modelPart.equals("head"))
      return modelcreeper.head; 
    if (modelPart.equals("armor"))
      return modelcreeper.creeperArmor; 
    if (modelPart.equals("body"))
      return modelcreeper.body; 
    if (modelPart.equals("leg1"))
      return modelcreeper.leg1; 
    if (modelPart.equals("leg2"))
      return modelcreeper.leg2; 
    if (modelPart.equals("leg3"))
      return modelcreeper.leg3; 
    return modelPart.equals("leg4") ? modelcreeper.leg4 : null;
  }
  
  public IEntityRenderer makeEntityRender(ModelBase modelBase, float shadowSize) {
    RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
    RenderCreeper rendercreeper = new RenderCreeper(rendermanager);
    rendercreeper.mainModel = modelBase;
    rendercreeper.shadowSize = shadowSize;
    return (IEntityRenderer)rendercreeper;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\optifine\entity\model\ModelAdapterCreeper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
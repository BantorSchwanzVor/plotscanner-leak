package net.optifine.entity.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelWitch;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderWitch;
import net.minecraft.entity.monster.EntityWitch;
import optifine.Reflector;

public class ModelAdapterWitch extends ModelAdapter {
  public ModelAdapterWitch() {
    super(EntityWitch.class, "witch", 0.5F);
  }
  
  public ModelBase makeModel() {
    return (ModelBase)new ModelWitch(0.0F);
  }
  
  public ModelRenderer getModelRenderer(ModelBase model, String modelPart) {
    if (!(model instanceof ModelWitch))
      return null; 
    ModelWitch modelwitch = (ModelWitch)model;
    if (modelPart.equals("mole"))
      return (ModelRenderer)Reflector.getFieldValue(modelwitch, Reflector.ModelWitch_mole); 
    if (modelPart.equals("hat"))
      return (ModelRenderer)Reflector.getFieldValue(modelwitch, Reflector.ModelWitch_hat); 
    if (modelPart.equals("head"))
      return modelwitch.villagerHead; 
    if (modelPart.equals("body"))
      return modelwitch.villagerBody; 
    if (modelPart.equals("arms"))
      return modelwitch.villagerArms; 
    if (modelPart.equals("left_leg"))
      return modelwitch.leftVillagerLeg; 
    if (modelPart.equals("right_leg"))
      return modelwitch.rightVillagerLeg; 
    return modelPart.equals("nose") ? modelwitch.villagerNose : null;
  }
  
  public IEntityRenderer makeEntityRender(ModelBase modelBase, float shadowSize) {
    RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
    RenderWitch renderwitch = new RenderWitch(rendermanager);
    renderwitch.mainModel = modelBase;
    renderwitch.shadowSize = shadowSize;
    return (IEntityRenderer)renderwitch;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\optifine\entity\model\ModelAdapterWitch.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
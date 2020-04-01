package net.optifine.entity.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelArmorStand;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.RenderArmorStand;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.item.EntityArmorStand;

public class ModelAdapterArmorStand extends ModelAdapter {
  public ModelAdapterArmorStand() {
    super(EntityArmorStand.class, "armor_stand", 0.7F);
  }
  
  public ModelBase makeModel() {
    return (ModelBase)new ModelArmorStand();
  }
  
  public ModelRenderer getModelRenderer(ModelBase model, String modelPart) {
    if (!(model instanceof ModelArmorStand))
      return null; 
    ModelArmorStand modelarmorstand = (ModelArmorStand)model;
    if (modelPart.equals("right"))
      return modelarmorstand.standRightSide; 
    if (modelPart.equals("left"))
      return modelarmorstand.standLeftSide; 
    if (modelPart.equals("waist"))
      return modelarmorstand.standWaist; 
    return modelPart.equals("base") ? modelarmorstand.standBase : null;
  }
  
  public IEntityRenderer makeEntityRender(ModelBase modelBase, float shadowSize) {
    RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
    RenderArmorStand renderarmorstand = new RenderArmorStand(rendermanager);
    renderarmorstand.mainModel = modelBase;
    renderarmorstand.shadowSize = shadowSize;
    return (IEntityRenderer)renderarmorstand;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\optifine\entity\model\ModelAdapterArmorStand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
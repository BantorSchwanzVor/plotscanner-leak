package net.optifine.entity.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBoat;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.RenderBoat;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.item.EntityBoat;
import optifine.Config;
import optifine.Reflector;

public class ModelAdapterBoat extends ModelAdapter {
  public ModelAdapterBoat() {
    super(EntityBoat.class, "boat", 0.5F);
  }
  
  public ModelBase makeModel() {
    return (ModelBase)new ModelBoat();
  }
  
  public ModelRenderer getModelRenderer(ModelBase model, String modelPart) {
    if (!(model instanceof ModelBoat))
      return null; 
    ModelBoat modelboat = (ModelBoat)model;
    if (modelPart.equals("bottom"))
      return modelboat.boatSides[0]; 
    if (modelPart.equals("back"))
      return modelboat.boatSides[1]; 
    if (modelPart.equals("front"))
      return modelboat.boatSides[2]; 
    if (modelPart.equals("right"))
      return modelboat.boatSides[3]; 
    if (modelPart.equals("left"))
      return modelboat.boatSides[4]; 
    if (modelPart.equals("paddle_left"))
      return modelboat.paddles[0]; 
    if (modelPart.equals("paddle_right"))
      return modelboat.paddles[1]; 
    return modelPart.equals("bottom_no_water") ? modelboat.noWater : null;
  }
  
  public IEntityRenderer makeEntityRender(ModelBase modelBase, float shadowSize) {
    RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
    RenderBoat renderboat = new RenderBoat(rendermanager);
    if (!Reflector.RenderBoat_modelBoat.exists()) {
      Config.warn("Field not found: RenderBoat.modelBoat");
      return null;
    } 
    Reflector.setFieldValue(renderboat, Reflector.RenderBoat_modelBoat, modelBase);
    renderboat.shadowSize = shadowSize;
    return (IEntityRenderer)renderboat;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\optifine\entity\model\ModelAdapterBoat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
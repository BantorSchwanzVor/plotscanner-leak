package net.optifine.entity.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelMinecart;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderMinecart;
import net.minecraft.entity.item.EntityMinecart;
import optifine.Config;
import optifine.Reflector;

public class ModelAdapterMinecart extends ModelAdapter {
  public ModelAdapterMinecart() {
    super(EntityMinecart.class, "minecart", 0.5F);
  }
  
  protected ModelAdapterMinecart(Class entityClass, String name, float shadow) {
    super(entityClass, name, shadow);
  }
  
  public ModelBase makeModel() {
    return (ModelBase)new ModelMinecart();
  }
  
  public ModelRenderer getModelRenderer(ModelBase model, String modelPart) {
    if (!(model instanceof ModelMinecart))
      return null; 
    ModelMinecart modelminecart = (ModelMinecart)model;
    if (modelPart.equals("bottom"))
      return modelminecart.sideModels[0]; 
    if (modelPart.equals("back"))
      return modelminecart.sideModels[1]; 
    if (modelPart.equals("front"))
      return modelminecart.sideModels[2]; 
    if (modelPart.equals("right"))
      return modelminecart.sideModels[3]; 
    if (modelPart.equals("left"))
      return modelminecart.sideModels[4]; 
    return modelPart.equals("dirt") ? modelminecart.sideModels[5] : null;
  }
  
  public IEntityRenderer makeEntityRender(ModelBase modelBase, float shadowSize) {
    RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
    RenderMinecart renderminecart = new RenderMinecart(rendermanager);
    if (!Reflector.RenderMinecart_modelMinecart.exists()) {
      Config.warn("Field not found: RenderMinecart.modelMinecart");
      return null;
    } 
    Reflector.setFieldValue(renderminecart, Reflector.RenderMinecart_modelMinecart, modelBase);
    renderminecart.shadowSize = shadowSize;
    return (IEntityRenderer)renderminecart;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\optifine\entity\model\ModelAdapterMinecart.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
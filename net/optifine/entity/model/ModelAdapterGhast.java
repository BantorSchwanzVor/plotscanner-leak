package net.optifine.entity.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelGhast;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.RenderGhast;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.monster.EntityGhast;
import optifine.Config;
import optifine.Reflector;

public class ModelAdapterGhast extends ModelAdapter {
  public ModelAdapterGhast() {
    super(EntityGhast.class, "ghast", 0.5F);
  }
  
  public ModelBase makeModel() {
    return (ModelBase)new ModelGhast();
  }
  
  public ModelRenderer getModelRenderer(ModelBase model, String modelPart) {
    if (!(model instanceof ModelGhast))
      return null; 
    ModelGhast modelghast = (ModelGhast)model;
    if (modelPart.equals("body"))
      return (ModelRenderer)Reflector.getFieldValue(modelghast, Reflector.ModelGhast_body); 
    String s = "tentacle";
    if (modelPart.startsWith(s)) {
      ModelRenderer[] amodelrenderer = (ModelRenderer[])Reflector.getFieldValue(modelghast, Reflector.ModelGhast_tentacles);
      if (amodelrenderer == null)
        return null; 
      String s1 = modelPart.substring(s.length());
      int i = Config.parseInt(s1, -1);
      i--;
      return (
        i >= 0 && i < amodelrenderer.length) ? amodelrenderer[i] : null;
    } 
    return null;
  }
  
  public IEntityRenderer makeEntityRender(ModelBase modelBase, float shadowSize) {
    RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
    RenderGhast renderghast = new RenderGhast(rendermanager);
    renderghast.mainModel = modelBase;
    renderghast.shadowSize = shadowSize;
    return (IEntityRenderer)renderghast;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\optifine\entity\model\ModelAdapterGhast.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
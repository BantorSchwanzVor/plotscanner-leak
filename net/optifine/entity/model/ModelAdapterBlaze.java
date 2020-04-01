package net.optifine.entity.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBlaze;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.RenderBlaze;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.monster.EntityBlaze;
import optifine.Config;
import optifine.Reflector;

public class ModelAdapterBlaze extends ModelAdapter {
  public ModelAdapterBlaze() {
    super(EntityBlaze.class, "blaze", 0.5F);
  }
  
  public ModelBase makeModel() {
    return (ModelBase)new ModelBlaze();
  }
  
  public ModelRenderer getModelRenderer(ModelBase model, String modelPart) {
    if (!(model instanceof ModelBlaze))
      return null; 
    ModelBlaze modelblaze = (ModelBlaze)model;
    if (modelPart.equals("head"))
      return (ModelRenderer)Reflector.getFieldValue(modelblaze, Reflector.ModelBlaze_blazeHead); 
    String s = "stick";
    if (modelPart.startsWith(s)) {
      ModelRenderer[] amodelrenderer = (ModelRenderer[])Reflector.getFieldValue(modelblaze, Reflector.ModelBlaze_blazeSticks);
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
    RenderBlaze renderblaze = new RenderBlaze(rendermanager);
    renderblaze.mainModel = modelBase;
    renderblaze.shadowSize = shadowSize;
    return (IEntityRenderer)renderblaze;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\optifine\entity\model\ModelAdapterBlaze.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
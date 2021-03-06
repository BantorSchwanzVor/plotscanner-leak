package net.optifine.entity.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelEnderMite;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.RenderEndermite;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.monster.EntityEndermite;
import optifine.Config;
import optifine.Reflector;

public class ModelAdapterEndermite extends ModelAdapter {
  public ModelAdapterEndermite() {
    super(EntityEndermite.class, "endermite", 0.3F);
  }
  
  public ModelBase makeModel() {
    return (ModelBase)new ModelEnderMite();
  }
  
  public ModelRenderer getModelRenderer(ModelBase model, String modelPart) {
    if (!(model instanceof ModelEnderMite))
      return null; 
    ModelEnderMite modelendermite = (ModelEnderMite)model;
    String s = "body";
    if (modelPart.startsWith(s)) {
      ModelRenderer[] amodelrenderer = (ModelRenderer[])Reflector.getFieldValue(modelendermite, Reflector.ModelEnderMite_bodyParts);
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
    RenderEndermite renderendermite = new RenderEndermite(rendermanager);
    renderendermite.mainModel = modelBase;
    renderendermite.shadowSize = shadowSize;
    return (IEntityRenderer)renderendermite;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\optifine\entity\model\ModelAdapterEndermite.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
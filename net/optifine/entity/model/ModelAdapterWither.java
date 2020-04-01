package net.optifine.entity.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelWither;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderWither;
import net.minecraft.entity.boss.EntityWither;
import optifine.Config;
import optifine.Reflector;

public class ModelAdapterWither extends ModelAdapter {
  public ModelAdapterWither() {
    super(EntityWither.class, "wither", 0.5F);
  }
  
  public ModelBase makeModel() {
    return (ModelBase)new ModelWither(0.0F);
  }
  
  public ModelRenderer getModelRenderer(ModelBase model, String modelPart) {
    if (!(model instanceof ModelWither))
      return null; 
    ModelWither modelwither = (ModelWither)model;
    String s = "body";
    if (modelPart.startsWith(s)) {
      ModelRenderer[] amodelrenderer1 = (ModelRenderer[])Reflector.getFieldValue(modelwither, Reflector.ModelWither_bodyParts);
      if (amodelrenderer1 == null)
        return null; 
      String s3 = modelPart.substring(s.length());
      int j = Config.parseInt(s3, -1);
      j--;
      return (
        j >= 0 && j < amodelrenderer1.length) ? amodelrenderer1[j] : null;
    } 
    String s1 = "head";
    if (modelPart.startsWith(s1)) {
      ModelRenderer[] amodelrenderer = (ModelRenderer[])Reflector.getFieldValue(modelwither, Reflector.ModelWither_heads);
      if (amodelrenderer == null)
        return null; 
      String s2 = modelPart.substring(s1.length());
      int i = Config.parseInt(s2, -1);
      i--;
      return (
        i >= 0 && i < amodelrenderer.length) ? amodelrenderer[i] : null;
    } 
    return null;
  }
  
  public IEntityRenderer makeEntityRender(ModelBase modelBase, float shadowSize) {
    RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
    RenderWither renderwither = new RenderWither(rendermanager);
    renderwither.mainModel = modelBase;
    renderwither.shadowSize = shadowSize;
    return (IEntityRenderer)renderwither;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\optifine\entity\model\ModelAdapterWither.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
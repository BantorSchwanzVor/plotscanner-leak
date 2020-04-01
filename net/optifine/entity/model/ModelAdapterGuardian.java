package net.optifine.entity.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelGuardian;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.RenderGuardian;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.monster.EntityGuardian;
import optifine.Config;
import optifine.Reflector;

public class ModelAdapterGuardian extends ModelAdapter {
  public ModelAdapterGuardian() {
    super(EntityGuardian.class, "guardian", 0.5F);
  }
  
  public ModelBase makeModel() {
    return (ModelBase)new ModelGuardian();
  }
  
  public ModelRenderer getModelRenderer(ModelBase model, String modelPart) {
    if (!(model instanceof ModelGuardian))
      return null; 
    ModelGuardian modelguardian = (ModelGuardian)model;
    if (modelPart.equals("body"))
      return (ModelRenderer)Reflector.getFieldValue(modelguardian, Reflector.ModelGuardian_body); 
    if (modelPart.equals("eye"))
      return (ModelRenderer)Reflector.getFieldValue(modelguardian, Reflector.ModelGuardian_eye); 
    String s = "spine";
    if (modelPart.startsWith(s)) {
      ModelRenderer[] amodelrenderer1 = (ModelRenderer[])Reflector.getFieldValue(modelguardian, Reflector.ModelGuardian_spines);
      if (amodelrenderer1 == null)
        return null; 
      String s3 = modelPart.substring(s.length());
      int j = Config.parseInt(s3, -1);
      j--;
      return (
        j >= 0 && j < amodelrenderer1.length) ? amodelrenderer1[j] : null;
    } 
    String s1 = "tail";
    if (modelPart.startsWith(s1)) {
      ModelRenderer[] amodelrenderer = (ModelRenderer[])Reflector.getFieldValue(modelguardian, Reflector.ModelGuardian_tail);
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
    RenderGuardian renderguardian = new RenderGuardian(rendermanager);
    renderguardian.mainModel = modelBase;
    renderguardian.shadowSize = shadowSize;
    return (IEntityRenderer)renderguardian;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\optifine\entity\model\ModelAdapterGuardian.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
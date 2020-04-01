package net.optifine.entity.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelHumanoidHead;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySkullRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntitySkull;
import optifine.Config;
import optifine.Reflector;

public class ModelAdapterHeadHumanoid extends ModelAdapter {
  public ModelAdapterHeadHumanoid() {
    super(TileEntitySkull.class, "head_humanoid", 0.0F);
  }
  
  public ModelBase makeModel() {
    return (ModelBase)new ModelHumanoidHead();
  }
  
  public ModelRenderer getModelRenderer(ModelBase model, String modelPart) {
    if (!(model instanceof ModelHumanoidHead))
      return null; 
    ModelHumanoidHead modelhumanoidhead = (ModelHumanoidHead)model;
    if (modelPart.equals("head"))
      return modelhumanoidhead.skeletonHead; 
    if (modelPart.equals("head2"))
      return !Reflector.ModelHumanoidHead_head.exists() ? null : (ModelRenderer)Reflector.getFieldValue(modelhumanoidhead, Reflector.ModelHumanoidHead_head); 
    return null;
  }
  
  public IEntityRenderer makeEntityRender(ModelBase modelBase, float shadowSize) {
    TileEntitySkullRenderer tileEntitySkullRenderer;
    TileEntityRendererDispatcher tileentityrendererdispatcher = TileEntityRendererDispatcher.instance;
    TileEntitySpecialRenderer tileentityspecialrenderer = tileentityrendererdispatcher.getSpecialRendererByClass(TileEntitySkull.class);
    if (!(tileentityspecialrenderer instanceof TileEntitySkullRenderer))
      return null; 
    if (tileentityspecialrenderer.getEntityClass() == null) {
      tileEntitySkullRenderer = new TileEntitySkullRenderer();
      tileEntitySkullRenderer.setRendererDispatcher(tileentityrendererdispatcher);
    } 
    if (!Reflector.TileEntitySkullRenderer_humanoidHead.exists()) {
      Config.warn("Field not found: TileEntitySkullRenderer.humanoidHead");
      return null;
    } 
    Reflector.setFieldValue(tileEntitySkullRenderer, Reflector.TileEntitySkullRenderer_humanoidHead, modelBase);
    return (IEntityRenderer)tileEntitySkullRenderer;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\optifine\entity\model\ModelAdapterHeadHumanoid.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
package net.optifine.entity.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelDragonHead;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySkullRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntitySkull;
import optifine.Config;
import optifine.Reflector;

public class ModelAdapterHeadDragon extends ModelAdapter {
  public ModelAdapterHeadDragon() {
    super(TileEntitySkull.class, "head_dragon", 0.0F);
  }
  
  public ModelBase makeModel() {
    return (ModelBase)new ModelDragonHead(0.0F);
  }
  
  public ModelRenderer getModelRenderer(ModelBase model, String modelPart) {
    if (!(model instanceof ModelDragonHead))
      return null; 
    ModelDragonHead modeldragonhead = (ModelDragonHead)model;
    if (modelPart.equals("head"))
      return (ModelRenderer)Reflector.getFieldValue(modeldragonhead, Reflector.ModelDragonHead_head); 
    return modelPart.equals("jaw") ? (ModelRenderer)Reflector.getFieldValue(modeldragonhead, Reflector.ModelDragonHead_jaw) : null;
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
    if (!Reflector.TileEntitySkullRenderer_dragonHead.exists()) {
      Config.warn("Field not found: TileEntitySkullRenderer.dragonHead");
      return null;
    } 
    Reflector.setFieldValue(tileEntitySkullRenderer, Reflector.TileEntitySkullRenderer_dragonHead, modelBase);
    return (IEntityRenderer)tileEntitySkullRenderer;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\optifine\entity\model\ModelAdapterHeadDragon.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
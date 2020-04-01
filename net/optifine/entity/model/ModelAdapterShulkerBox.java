package net.optifine.entity.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelShulker;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntityShulkerBoxRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntityShulkerBox;
import optifine.Config;
import optifine.Reflector;

public class ModelAdapterShulkerBox extends ModelAdapter {
  public ModelAdapterShulkerBox() {
    super(TileEntityShulkerBox.class, "shulker_box", 0.0F);
  }
  
  public ModelBase makeModel() {
    return (ModelBase)new ModelShulker();
  }
  
  public ModelRenderer getModelRenderer(ModelBase model, String modelPart) {
    if (!(model instanceof ModelShulker))
      return null; 
    ModelShulker modelshulker = (ModelShulker)model;
    if (modelPart.equals("head"))
      return modelshulker.head; 
    if (modelPart.equals("base"))
      return modelshulker.base; 
    return modelPart.equals("lid") ? modelshulker.lid : null;
  }
  
  public IEntityRenderer makeEntityRender(ModelBase modelBase, float shadowSize) {
    TileEntityShulkerBoxRenderer tileEntityShulkerBoxRenderer;
    TileEntityRendererDispatcher tileentityrendererdispatcher = TileEntityRendererDispatcher.instance;
    TileEntitySpecialRenderer tileentityspecialrenderer = tileentityrendererdispatcher.getSpecialRendererByClass(TileEntityShulkerBox.class);
    if (!(tileentityspecialrenderer instanceof TileEntityShulkerBoxRenderer))
      return null; 
    if (tileentityspecialrenderer.getEntityClass() == null) {
      tileEntityShulkerBoxRenderer = new TileEntityShulkerBoxRenderer((ModelShulker)modelBase);
      tileEntityShulkerBoxRenderer.setRendererDispatcher(tileentityrendererdispatcher);
    } 
    if (!Reflector.TileEntityShulkerBoxRenderer_model.exists()) {
      Config.warn("Field not found: TileEntityShulkerBoxRenderer.model");
      return null;
    } 
    Reflector.setFieldValue(tileEntityShulkerBoxRenderer, Reflector.TileEntityShulkerBoxRenderer_model, modelBase);
    return (IEntityRenderer)tileEntityShulkerBoxRenderer;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\optifine\entity\model\ModelAdapterShulkerBox.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
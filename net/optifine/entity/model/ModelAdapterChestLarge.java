package net.optifine.entity.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelChest;
import net.minecraft.client.model.ModelLargeChest;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityChestRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntityChest;
import optifine.Config;
import optifine.Reflector;

public class ModelAdapterChestLarge extends ModelAdapter {
  public ModelAdapterChestLarge() {
    super(TileEntityChest.class, "chest_large", 0.0F);
  }
  
  public ModelBase makeModel() {
    return (ModelBase)new ModelLargeChest();
  }
  
  public ModelRenderer getModelRenderer(ModelBase model, String modelPart) {
    if (!(model instanceof ModelChest))
      return null; 
    ModelChest modelchest = (ModelChest)model;
    if (modelPart.equals("lid"))
      return modelchest.chestLid; 
    if (modelPart.equals("base"))
      return modelchest.chestBelow; 
    return modelPart.equals("knob") ? modelchest.chestKnob : null;
  }
  
  public IEntityRenderer makeEntityRender(ModelBase modelBase, float shadowSize) {
    TileEntityChestRenderer tileEntityChestRenderer;
    TileEntityRendererDispatcher tileentityrendererdispatcher = TileEntityRendererDispatcher.instance;
    TileEntitySpecialRenderer tileentityspecialrenderer = tileentityrendererdispatcher.getSpecialRendererByClass(TileEntityChest.class);
    if (!(tileentityspecialrenderer instanceof TileEntityChestRenderer))
      return null; 
    if (tileentityspecialrenderer.getEntityClass() == null) {
      tileEntityChestRenderer = new TileEntityChestRenderer();
      tileEntityChestRenderer.setRendererDispatcher(tileentityrendererdispatcher);
    } 
    if (!Reflector.TileEntityChestRenderer_largeChest.exists()) {
      Config.warn("Field not found: TileEntityChestRenderer.largeChest");
      return null;
    } 
    Reflector.setFieldValue(tileEntityChestRenderer, Reflector.TileEntityChestRenderer_largeChest, modelBase);
    return (IEntityRenderer)tileEntityChestRenderer;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\optifine\entity\model\ModelAdapterChestLarge.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
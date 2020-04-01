package net.optifine.entity.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelChest;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityEnderChestRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntityEnderChest;
import optifine.Config;
import optifine.Reflector;

public class ModelAdapterEnderChest extends ModelAdapter {
  public ModelAdapterEnderChest() {
    super(TileEntityEnderChest.class, "ender_chest", 0.0F);
  }
  
  public ModelBase makeModel() {
    return (ModelBase)new ModelChest();
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
    TileEntityEnderChestRenderer tileEntityEnderChestRenderer;
    TileEntityRendererDispatcher tileentityrendererdispatcher = TileEntityRendererDispatcher.instance;
    TileEntitySpecialRenderer tileentityspecialrenderer = tileentityrendererdispatcher.getSpecialRendererByClass(TileEntityEnderChest.class);
    if (!(tileentityspecialrenderer instanceof TileEntityEnderChestRenderer))
      return null; 
    if (tileentityspecialrenderer.getEntityClass() == null) {
      tileEntityEnderChestRenderer = new TileEntityEnderChestRenderer();
      tileEntityEnderChestRenderer.setRendererDispatcher(tileentityrendererdispatcher);
    } 
    if (!Reflector.TileEntityEnderChestRenderer_modelChest.exists()) {
      Config.warn("Field not found: TileEntityEnderChestRenderer.modelChest");
      return null;
    } 
    Reflector.setFieldValue(tileEntityEnderChestRenderer, Reflector.TileEntityEnderChestRenderer_modelChest, modelBase);
    return (IEntityRenderer)tileEntityEnderChestRenderer;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\optifine\entity\model\ModelAdapterEnderChest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
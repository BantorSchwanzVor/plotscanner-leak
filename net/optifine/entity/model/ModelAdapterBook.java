package net.optifine.entity.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBook;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityEnchantmentTableRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntityEnchantmentTable;
import optifine.Config;
import optifine.Reflector;

public class ModelAdapterBook extends ModelAdapter {
  public ModelAdapterBook() {
    super(TileEntityEnchantmentTable.class, "book", 0.0F);
  }
  
  public ModelBase makeModel() {
    return (ModelBase)new ModelBook();
  }
  
  public ModelRenderer getModelRenderer(ModelBase model, String modelPart) {
    if (!(model instanceof ModelBook))
      return null; 
    ModelBook modelbook = (ModelBook)model;
    if (modelPart.equals("cover_right"))
      return modelbook.coverRight; 
    if (modelPart.equals("cover_left"))
      return modelbook.coverLeft; 
    if (modelPart.equals("pages_right"))
      return modelbook.pagesRight; 
    if (modelPart.equals("pages_left"))
      return modelbook.pagesLeft; 
    if (modelPart.equals("flipping_page_right"))
      return modelbook.flippingPageRight; 
    if (modelPart.equals("flipping_page_left"))
      return modelbook.flippingPageLeft; 
    return modelPart.equals("book_spine") ? modelbook.bookSpine : null;
  }
  
  public IEntityRenderer makeEntityRender(ModelBase modelBase, float shadowSize) {
    TileEntityEnchantmentTableRenderer tileEntityEnchantmentTableRenderer;
    TileEntityRendererDispatcher tileentityrendererdispatcher = TileEntityRendererDispatcher.instance;
    TileEntitySpecialRenderer tileentityspecialrenderer = tileentityrendererdispatcher.getSpecialRendererByClass(TileEntityEnchantmentTable.class);
    if (!(tileentityspecialrenderer instanceof TileEntityEnchantmentTableRenderer))
      return null; 
    if (tileentityspecialrenderer.getEntityClass() == null) {
      tileEntityEnchantmentTableRenderer = new TileEntityEnchantmentTableRenderer();
      tileEntityEnchantmentTableRenderer.setRendererDispatcher(tileentityrendererdispatcher);
    } 
    if (!Reflector.TileEntityEnchantmentTableRenderer_modelBook.exists()) {
      Config.warn("Field not found: TileEntityEnchantmentTableRenderer.modelBook");
      return null;
    } 
    Reflector.setFieldValue(tileEntityEnchantmentTableRenderer, Reflector.TileEntityEnchantmentTableRenderer_modelBook, modelBase);
    return (IEntityRenderer)tileEntityEnchantmentTableRenderer;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\optifine\entity\model\ModelAdapterBook.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */